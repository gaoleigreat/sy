package com.lego.survey.settlement.impl.controller;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.lego.survey.auth.feign.AuthClient;
import com.lego.survey.base.utils.FpFileUtil;
import com.lego.survey.event.settlement.SurveyPointResultSource;
import com.lego.survey.lib.excel.ExcelService;
import com.lego.survey.project.feign.SectionClient;
import com.lego.survey.project.model.entity.OwnWorkspace;
import com.lego.survey.project.model.entity.Section;
import com.lego.survey.settlement.feign.ReportDataClient;
import com.lego.survey.settlement.impl.listener.SurveyResultReadListener;
import com.lego.survey.settlement.impl.service.ISurveyOriginalService;
import com.lego.survey.settlement.impl.service.ISurveyPointService;
import com.lego.survey.settlement.impl.service.ISurveyResultService;
import com.lego.survey.settlement.model.entity.SurveyResult;
import com.lego.survey.settlement.model.vo.*;
import com.lego.survey.event.user.LogSender;
import com.survey.lib.common.consts.DictConstant;
import com.survey.lib.common.consts.RespConsts;
import com.survey.lib.common.page.PagedResult;
import com.survey.lib.common.utils.HeaderUtils;
import com.survey.lib.common.utils.HttpUtils;
import com.survey.lib.common.utils.SnowflakeIdUtils;
import com.survey.lib.common.vo.HeaderVo;
import com.survey.lib.common.vo.RespDataVO;
import com.survey.lib.common.vo.RespVO;
import com.survey.lib.common.vo.RespVOBuilder;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.integration.support.MessageBuilder;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author yanglf
 * @description
 * @since 2019/1/8
 **/
@Validated
@RestController
@RequestMapping(DictConstant.Path.SURVEY_RESULT)
@Api(value = "SurveyResultController", description = "测量结果接口")
public class SurveyResultController {

    @Autowired
    private ISurveyResultService iSurveyResultService;

    @Autowired
    private AuthClient authClient;

    @Autowired
    private LogSender logSender;

    @Autowired
    private SurveyPointResultSource surveyPointResultSource;

    @Autowired
    private SectionClient sectionClient;


    @Autowired
    private MongoTemplate mongoTemplate;

    @Value("${fpfile.path}")
    private String fpFileRootPath;

    @Autowired
    private ISurveyOriginalService surveyOriginalService;
    @Autowired
    private ISurveyResultService surveyResultService;

    @Autowired
    private ExcelService excelService;

    @Autowired
    private SurveyResultReadListener surveyPointReadListener;

    @Autowired
    private ISurveyPointService surveyPointService;


    @ApiOperation(value = "添加成果数据", notes = "添加成果数据", httpMethod = "POST")
    @ApiImplicitParams({

    })
    @RequestMapping(value = "/create/{sectionCode}", method = RequestMethod.POST)
    public RespVO create(@Validated @RequestBody SurveyResultVo surveyResultVo,
                         @PathVariable(value = "sectionCode") String sectionCode,
                         HttpServletRequest request) {
        // TODO ID -> CODE
        HeaderVo headerVo = HeaderUtils.parseHeader(request);
        String userId = headerVo.getUserId();
        if (surveyResultVo.getId() == null) {
            surveyResultVo.setId(SnowflakeIdUtils.createId());
        }
        RespVO respVO = iSurveyResultService.create(surveyResultVo.getSurveyResult(), DictConstant.TableNamePrefix.SURVEY_RESULT + sectionCode);
        logSender.sendLogEvent(HttpUtils.getClientIp(request), userId, "新增成果数据:[" + surveyResultVo.getSurveyResult().getId() + "]");
        surveyPointResultSource.uploadResult().send(MessageBuilder.withPayload(surveyResultVo.getSurveyResult()).setHeader("type", 1).setHeader("sectionCode", sectionCode).build());
        return respVO;
    }


    @ApiOperation(value = "批量上传成果数据", notes = "批量上传成果数据", httpMethod = "POST")
    @ApiImplicitParams({

    })
    @RequestMapping(value = "/uploadBatch", method = RequestMethod.POST)
    public RespVO uploadBatch(@Validated @RequestBody List<SurveyResult> surveyResults,
                              HttpServletRequest request) {
        if (!CollectionUtils.isEmpty(surveyResults)) {
            surveyResults.forEach(this::setDefaultValue);
        }
        return saveBatch(surveyResults, request);
    }

    private void setDefaultValue(SurveyResult surveyResult) {
        if (surveyResult.getId() == null) {
            surveyResult.setId(SnowflakeIdUtils.createId());
        }
        Date currentTime = new Date();
        if (surveyResult.getUploadTime() == null) {
            surveyResult.setUploadTime(currentTime);
        }
        if (surveyResult.getSurveyTime() == null) {
            surveyResult.setSurveyTime(currentTime);
        }
    }


    @ApiOperation(value = "批量添加成果数据", notes = "批量添加成果数据", httpMethod = "POST")
    @ApiImplicitParams({

    })
    @RequestMapping(value = "/createBatch", method = RequestMethod.POST)
    public RespVO createBatch(@Validated @RequestBody List<SurveyResultVo> surveyResultVos,
                              HttpServletRequest request) {
        List<SurveyResult> surveyResults = new ArrayList<>();
        if (surveyResultVos != null) {
            surveyResultVos.forEach(surveyResultVo -> {
                SurveyResult surveyResult = surveyResultVo.getSurveyResult();
                setDefaultValue(surveyResult);
                surveyResults.add(surveyResult);
            });
        }

        return saveBatch(surveyResults, request);
    }

    private RespVO saveBatch(List<SurveyResult> surveyResults, HttpServletRequest request) {
        HeaderVo headerVo = HeaderUtils.parseHeader(request);
        String userId = headerVo.getUserId();
        //TODO 校验权限
        if (surveyResults == null || surveyResults.size() <= 0) {
            return RespVOBuilder.failure("上传失败");
        }
        String workspaceCode = surveyResults.get(0).getWorkspaceCode();
        RespVO<Section> sectionRespVO = sectionClient.queryByWorkspaceCode(workspaceCode);
        if (sectionRespVO.getRetCode() != RespConsts.SUCCESS_RESULT_CODE) {
            return RespVOBuilder.failure("上传失败");
        }
        String sectionCode = sectionRespVO.getInfo().getCode();
        RespVO respVO = iSurveyResultService.createBatch(surveyResults, DictConstant.TableNamePrefix.SURVEY_RESULT + sectionCode);
        logSender.sendLogEvent(HttpUtils.getClientIp(request), userId, "批量新增成果数据");
        // 发送上传成果 消息
        surveyPointResultSource.uploadResult().send(MessageBuilder.withPayload(surveyResults).setHeader("type", 2).setHeader("sectionCode", sectionCode).build());
        return respVO;
    }


    @ApiOperation(value = "修改成果数据", notes = "修改成果数据", httpMethod = "PUT")
    @ApiImplicitParams({

    })
    @RequestMapping(value = "/modify/{sectionCode}", method = RequestMethod.PUT)
    public RespVO modify(@Validated @RequestBody SurveyResultVo surveyResultVo,
                         @PathVariable(value = "sectionCode") String sectionCode,
                         HttpServletRequest request) {
        // TODO ID -> CODE
        HeaderVo headerVo = HeaderUtils.parseHeader(request);
        String userId = headerVo.getUserId();
        //TODO 校验权限
        RespVO respVO = iSurveyResultService.modify(surveyResultVo.getSurveyResult(), DictConstant.TableNamePrefix.SURVEY_RESULT + sectionCode);
        logSender.sendLogEvent(HttpUtils.getClientIp(request), userId, "修改成果数据:[" + surveyResultVo.getId() + "]");
        return respVO;
    }


    @ApiOperation(value = "查询成果列表", httpMethod = "GET", notes = "查询成果列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "pageIndex", value = "当前页", dataType = "int", defaultValue = "1", example = "1", paramType = "query"),
            @ApiImplicitParam(name = "pageSize", value = "每页大小", dataType = "int", defaultValue = "10", example = "10", paramType = "query"),
            @ApiImplicitParam(name = "workspaceCode", value = "工区code", dataType = "String", required = true, paramType = "query"),
            @ApiImplicitParam(name = "startTimestamp", value = "开始时间戳", dataType = "Long", required = true, paramType = "query"),
            @ApiImplicitParam(name = "endTimestamp", value = "结束时间戳", dataType = "Long", required = true, paramType = "query"),

    })
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public RespVO list(
            @RequestParam(required = false, defaultValue = "1") int pageIndex,
            @RequestParam(required = false, defaultValue = "10") int pageSize,
            @RequestParam(value = "workspaceCode") String workspaceCode,
            Long startTimestamp,
            Long endTimestamp,
            HttpServletRequest request
    ) {
        // TODO ID -> CODE
        HeaderVo headerVo = HeaderUtils.parseHeader(request);
        RespVO<Section> sectionRespVO = sectionClient.queryByWorkspaceId(workspaceCode);
        if (sectionRespVO.getRetCode() != RespConsts.SUCCESS_RESULT_CODE) {
            return RespVOBuilder.success(new ArrayList<>());
        }
        Section info = sectionRespVO.getInfo();
        if (info == null) {
            return RespVOBuilder.success(new ArrayList<>());
        }
        List<OwnWorkspace> workSpaces = info.getWorkSpace();
        for (OwnWorkspace workSpace : workSpaces) {
            if (workSpace.getCode().equals(workspaceCode)) {
                String deviceType = headerVo.getDeviceType();
                Date startDate = null;
                Date endDate = null;
                if (startTimestamp != null && endTimestamp != null) {
                    startDate = new Date(startTimestamp);
                    endDate = new Date(endTimestamp);
                }
                return iSurveyResultService.list(pageIndex, pageSize, workspaceCode, startDate, endDate, deviceType, DictConstant.TableNamePrefix.SURVEY_RESULT + info.getId());
            }

        }

        return RespVOBuilder.failure("获取测点信息失败");
    }


    @ApiOperation(value = "删除成果数据", httpMethod = "DELETE", notes = "删除成果数据")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "ids", value = "成果数据id", dataType = "Long", required = true, paramType = "query"),
            @ApiImplicitParam(name = "sectionCode", value = "标段code", dataType = "String", required = true, paramType = "path"),
    })
    @RequestMapping(value = "/delete/{sectionCode}", method = RequestMethod.DELETE)
    public RespVO delete(
            @PathVariable(value = "sectionCode") String sectionCode,
            @RequestParam List<Long> ids,
            HttpServletRequest request
    ) {
        // TODO ID -> CODE
        HeaderVo headerVo = HeaderUtils.parseHeader(request);
        String userId = authClient.getAuthVo(headerVo).getUserId();
        // 验证用户正确性
        RespVO delete = iSurveyResultService.delete(ids, DictConstant.TableNamePrefix.SURVEY_RESULT + sectionCode);
        logSender.sendLogEvent(HttpUtils.getClientIp(request), userId, "删除成果数据:[" + JSONObject.toJSONString(ids) + "]");
        return delete;
    }



    @ApiOperation(value = "成果数据列表", httpMethod = "GET", notes = "成果数据列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "pageIndex", value = "当前页", dataType = "int", required = true, example = "1", paramType = "path"),
            @ApiImplicitParam(name = "pageSize", value = "每页大小", dataType = "int", defaultValue = "10", example = "10", paramType = "query"),
            @ApiImplicitParam(name = "type", value = "0-全部;1-超限", dataType = "int", defaultValue = "0", paramType = "query"),
            @ApiImplicitParam(name = "sectionCode", value = "标段CODE", dataType = "String", paramType = "query", required = true),
            @ApiImplicitParam(name = "workspaceCode", value = "工区CODE", dataType = "String", paramType = "query"),

    })
    @RequestMapping(value = "/overrunList/{pageIndex}", method = RequestMethod.GET)
    public RespVO<PagedResult<OverrunListVo>> queryOverrunList(@PathVariable(value = "pageIndex") int pageIndex,
                                                               @RequestParam(required = false, defaultValue = "10") Integer pageSize,
                                                               @RequestParam(required = false, defaultValue = "0") Integer type,
                                                               @RequestParam String sectionCode,
                                                               @RequestParam(required = false) String workspaceCode) {
        // TODO ID -> CODE
        PagedResult<OverrunListVo> listVoPagedResult = iSurveyResultService.queryOverrunList(pageIndex, pageSize, sectionCode, workspaceCode, type);
        return RespVOBuilder.success(listVoPagedResult);
    }


    @ApiOperation(value = "报警点详情", httpMethod = "GET", notes = "报警点详情")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "pageIndex", value = "当前页", dataType = "int", required = true, example = "1", paramType = "path"),
            @ApiImplicitParam(name = "pageSize", value = "每页大小", dataType = "int", defaultValue = "10", example = "10", paramType = "query"),
            @ApiImplicitParam(name = "sectionCode", value = "标段CODE", dataType = "String", paramType = "query", required = true),
            @ApiImplicitParam(name = "pointCode", value = "测点code", dataType = "String", paramType = "query", required = true),
    })
    @RequestMapping(value = "/overrunDetails/{pageIndex}", method = RequestMethod.GET)
    public  RespVO<PagedResult<OverrunListVo>>   queryOverrunDetails(@PathVariable(value = "pageIndex") int pageIndex,
                                                                  @RequestParam(required = false, defaultValue = "10") Integer pageSize,
                                                                  @RequestParam(required = false, defaultValue = "0") Integer type,
                                                                  @RequestParam String sectionCode,
                                                                  @RequestParam String pointCode) {
        // TODO ID -> CODE
        PagedResult<OverrunListVo> listVoPagedResult = iSurveyResultService.queryOverrunDetails(pageIndex, pageSize, sectionCode, pointCode, type);
        return RespVOBuilder.success(listVoPagedResult);
    }


    @ApiOperation(value = "通过原始数据查询成果数据", httpMethod = "GET", notes = "通过原始数据查询成果数据")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "sectionCode", value = "标段CODE", dataType = "String", paramType = "query", required = true),
            @ApiImplicitParam(name = "originalIds", value = "原始数据id", dataType = "List", paramType = "query"),
    })
    @RequestMapping(value = "/query/list", method = RequestMethod.GET)
    public RespVO<RespDataVO<SurveyResult>> query(@RequestParam String sectionCode,
                                                  @RequestParam List<Long> originalIds) {
        // TODO ID -> CODE
        List<SurveyResult> surveyResults = iSurveyResultService.queryResult(sectionCode, originalIds);
        return RespVOBuilder.success(surveyResults);
    }


    @ApiOperation(value = "查询测量点的测量历史数据", httpMethod = "GET", notes = "通过原始数据查询成果数据")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "sectionCode", value = "标段CODE", dataType = "String", paramType = "query", required = true),
            @ApiImplicitParam(name = "pointCode", value = "测点Id", dataType = "String", paramType = "query", required = true),
    })
    @RequestMapping(value = "/query/pointData", method = RequestMethod.GET)
    public RespVO<RespDataVO<SurveyPontResultVo>> queryPontData(@RequestParam String sectionCode,
                                                                @RequestParam String pointCode) {
        // TODO ID -> CODE
        List<SurveyPontResultVo> surveyPontResultVos = iSurveyResultService.queryPontResult(sectionCode, pointCode);
        return RespVOBuilder.success(surveyPontResultVos);
    }


    @ApiOperation(value = "生成成果报表", notes = "生成测量成果报表", httpMethod = "GET")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "sectionCode", value = "sectionCode", dataType = "String", required = true, paramType = "query"),
            @ApiImplicitParam(name = "taskId", value = "taskId", dataType = "Long", paramType = "query"),
    })
    @RequestMapping(value = "/generateDataExcel", method = RequestMethod.GET)
    public RespVO generateExcel(HttpServletResponse response, @RequestParam String sectionCode, @RequestParam Long taskId) throws Exception {
        InputStream inputStream = Thread.currentThread().getContextClassLoader().getResourceAsStream("template.xlsx");
        XSSFWorkbook workBook = new XSSFWorkbook(inputStream);
        // 给sheet命名

        String fileName = "test" + System.currentTimeMillis() + ".xlsx";
        response.setContentType("application/force-download");
        List<SurveyReportDataVo> surveyReportDataVoList = queryData(sectionCode, taskId);
        Set<String> typeSet = surveyReportDataVoList.stream().map(SurveyReportDataVo::getPointType).collect(Collectors.toSet());
        for (String type : typeSet) {
            List<SurveyReportDataVo> list = surveyReportDataVoList.stream().filter(o -> o.getPointType().equals(type)).collect(Collectors.toList());
            SurveyReportDataVo surveyReportDataVo = list.get(0);
            SurveyReportVo surveyReportVo = getSurveyReportVo(surveyReportDataVo.getWorkspaceCode());
            surveyReportVo.setSurveyer(surveyReportDataVo.getSurveyer());
            surveyReportVo.setInitSurveyTime(surveyReportDataVo.getInitSurveyTime());
            surveyReportVo.setPreSurveyTime(surveyReportDataVo.getPreSurveyTime());
            surveyReportVo.setSurveyTime(surveyReportDataVo.getSurveyTime());
            surveyReportVo.setOnceLowerLimit(surveyReportDataVo.getOnceLowerLimit());
            surveyReportVo.setDocname(type+"沉降检测表");
            surveyReportVo.setPontType(type);
            XSSFSheet sheet = workBook.cloneSheet(0,type);
            sheet.getRow(5).getCell(1).setCellValue(surveyReportVo.getDocname());
            sheet.getRow(6).getCell(1).setCellValue(surveyReportVo.getDocname());
            sheet.getRow(7).getCell(1).setCellValue(surveyReportVo.getDocname());
            for (int j =0; j<list.size();j++) {
                sheet.shiftRows(11+j, sheet.getLastRowNum(), 1, true, false);
                XSSFRow row =  sheet.getRow(11+j)==null?sheet.createRow(11+j):sheet.getRow(11+j);
                row.setRowStyle(sheet.getRow(11).getRowStyle());

                row.createCell(0).setCellValue(j);
                if (null !=list.get(j).getPointCode()) {
                    row.createCell(1).setCellValue(list.get(j).getPointCode());
                }
                if (null !=list.get(j).getInitElevation()) {
                    row.createCell(2).setCellValue(list.get(j).getInitElevation());
                }
                if (null !=list.get(j).getPreElevation()) {
                    row.createCell(3).setCellValue(list.get(j).getPreElevation());
                }
                if (null !=list.get(j).getCurElevation()) {
                    row.createCell(4).setCellValue(list.get(j).getCurElevation());
                }
                if (null !=list.get(j).getCurOffsetValue()) {
                    row.createCell(5).setCellValue(list.get(j).getCurOffsetValue());
                }
                if (null !=list.get(j).getCurSpeed()) {
                    row.createCell(6).setCellValue(list.get(j).getCurSpeed());
                }
                if (null !=list.get(j).getCurTotalOffsetValue()) {
                    row.createCell(7).setCellValue(list.get(j).getCurTotalOffsetValue());
                }
            }
        }
        // 设置文件名
        response.addHeader("Content-Disposition", "attachment;fileName=" + java.net.URLEncoder.encode(fileName, "UTF-8"));
        OutputStream out = response.getOutputStream();
        if (typeSet.size() > 0) {
            workBook.removeSheetAt(0);
        }
        // 移除workbook中的模板sheet
        workBook.write(out);

        inputStream.close();
        out.flush();
        out.close();
        return RespVOBuilder.success();
    }

    public SurveyReportVo getSurveyReportVo(String workspaceCode) {
        SurveyReportVo surveyReportVo = new SurveyReportVo();

        //用来封装所有条件的对象
        Query query = new Query();
        //用来构建条件
        Criteria criteria = new Criteria();
        criteria.and("workSpace").elemMatch(new Criteria().and("code").is(workspaceCode));
        query.addCriteria(criteria);
        JSONObject jsonObject = mongoTemplate.findOne(query, JSONObject.class, "section");
        // 标段名
        surveyReportVo.setTitle(jsonObject.getString("name"));

        //设置地址
        JSONArray jsonArray = jsonObject.getJSONArray("workSpace");
        for (int i = 0; i < jsonArray.size(); i++) {
            if (jsonArray.getJSONObject(i).getString("code").equals(workspaceCode)) {
                surveyReportVo.setAddress(jsonArray.getJSONObject(i).getString("name"));
            }
        }

        return surveyReportVo;
    }

    public List<SurveyReportDataVo> queryData(@RequestParam String sectionCode,
                                              @RequestParam Long taskId
    ) {


        //获取原始数据
        List<SurveyOriginalVo> originalVos = surveyOriginalService.list(taskId, sectionCode);
        //获取原始数据ID
        List<Long> originalIds = originalVos.stream().map(SurveyOriginalVo::getId).collect(Collectors.toList());
        //获取结果数据
        List<SurveyResult> surveyResults = surveyResultService.queryResult(sectionCode, originalIds);

        //测量结果
        List<SurveyReportDataVo> surveyReportDataVos = new ArrayList<>();
        surveyResults.forEach(surveyResult -> surveyReportDataVos.add(SurveyReportDataVo.builder().build().loadSurveyReportDataVo(surveyResult)));

        for (SurveyReportDataVo surveyReportDataVo : surveyReportDataVos) {

            //上次测量结果
            List<SurveyResult> tempResults = surveyResultService.queryPreResult(surveyReportDataVo.getSurveyTime(), DictConstant.TableNamePrefix.SURVEY_RESULT + sectionCode, 1, surveyReportDataVo.getPointCode());
            //第一次测量结果
            List<SurveyResult> intResults = surveyResultService.queryPreResult(null, DictConstant.TableNamePrefix.SURVEY_RESULT + sectionCode, 1, surveyReportDataVo.getPointCode());
            //点初始值
            SurveyPointVo surveyPointVo = surveyPointService.querySurveyPointByCode(surveyReportDataVo.getPointCode(), DictConstant.TableNamePrefix.SURVEY_POINT + sectionCode);
            surveyReportDataVo.setPointType(surveyPointVo.getType());
            surveyReportDataVo.setInitElevation(surveyPointVo.getElevation());
            surveyReportDataVo.setOnceLowerLimit(surveyPointVo.getOnceLowerLimit());
            surveyReportDataVo.setOnceUpperLimit(surveyPointVo.getOnceUpperLimit());
            surveyReportDataVo.setSpeedLowerLimit(surveyPointVo.getSpeedLowerLimit());
            surveyReportDataVo.setSpeedUpperLimit(surveyPointVo.getSpeedUpperLimit());
            surveyReportDataVo.setTotalLowerLimit(surveyPointVo.getTotalLowerLimit());
            surveyReportDataVo.setTotalUpperLimit(surveyPointVo.getTotalUpperLimit());


            if (!CollectionUtils.isEmpty(intResults) && intResults.size() > 1) {
                surveyReportDataVo.setInitSurveyTime(intResults.get(intResults.size() - 1).getSurveyTime());
            }
            if (!CollectionUtils.isEmpty(tempResults)) {
                surveyReportDataVo.setPreElevation(tempResults.get(0).getElevation());
                surveyReportDataVo.setPreSurveyTime(tempResults.get(0).getSurveyTime());
            }

        }
        return surveyReportDataVos;
    }




    @ApiOperation(value = "Excel批量上传成功数据", notes = "Excel批量上传成功数据", httpMethod = "POST")
    @ApiImplicitParams({

    })
    @RequestMapping(value = "/uploadPointResultExcel",method = RequestMethod.POST)
    public RespVO uploadPointResultExcel(@RequestPart(value = "fileName") String fileName,
                                         @RequestParam() String sectionCode){
        // TODO ID -> CODE
        if(StringUtils.isEmpty(fileName)){
            return RespVOBuilder.failure("文件名不能为空");
        }
        String filePath = FpFileUtil.getFilePath(fpFileRootPath,fileName);
        surveyPointReadListener.setTableName(sectionCode);
        excelService.readExcel(filePath,surveyPointReadListener, SurveyResultVo.class,1);
        return RespVOBuilder.success();
    }




}
