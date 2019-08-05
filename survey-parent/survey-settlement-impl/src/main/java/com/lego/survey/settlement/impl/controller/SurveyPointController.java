package com.lego.survey.settlement.impl.controller;

import com.alibaba.fastjson.JSONObject;
import com.lego.survey.auth.feign.AuthClient;
import com.lego.survey.base.utils.FpFileUtil;
import com.lego.survey.lib.excel.ExcelService;
import com.lego.survey.project.feign.SectionClient;
import com.lego.survey.project.model.entity.OwnWorkspace;
import com.lego.survey.project.model.entity.Section;
import com.lego.survey.settlement.impl.listener.SurveyPointReadListener;
import com.lego.survey.settlement.impl.service.ISurveyPointService;
import com.lego.survey.settlement.model.entity.SurveyPoint;
import com.lego.survey.settlement.model.vo.SurveyPointVo;
import com.lego.survey.event.user.LogSender;
import com.survey.lib.common.consts.DictConstant;
import com.survey.lib.common.consts.RespConsts;
import com.survey.lib.common.utils.HeaderUtils;
import com.survey.lib.common.utils.HttpUtils;
import com.survey.lib.common.utils.SnowflakeIdUtils;
import com.survey.lib.common.utils.UuidUtils;
import com.survey.lib.common.vo.HeaderVo;
import com.survey.lib.common.vo.RespDataVO;
import com.survey.lib.common.vo.RespVO;
import com.survey.lib.common.vo.RespVOBuilder;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.util.StringUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author yanglf
 * @description
 * @since 2019/1/8
 **/
@Validated
@RestController
@RequestMapping(DictConstant.Path.SURVEY_POINT)
@Api(value = "SurveyPointController", description = "测点接口")
public class SurveyPointController {

    @Autowired
    private AuthClient authClient;

    @Autowired
    private ISurveyPointService iSurveyPointService;

    @Autowired
    private SectionClient sectionClient;

    @Autowired
    private LogSender logSender;

    @Autowired
    private ExcelService excelService;

    @Value("${fpfile.path}")
    private String fpFileRootPath;



    @Autowired
    private SurveyPointReadListener surveyPointReadListener;

    @ApiOperation(value = "添加测点", notes = "添加测点", httpMethod = "POST")
    @ApiImplicitParams({

    })
    @RequestMapping(value = "/create/{sectionCode}", method = RequestMethod.POST)
    public RespVO create(@Validated @RequestBody SurveyPointVo surveyPointVo,
                         @PathVariable(value = "sectionCode") String sectionCode,
                         HttpServletRequest request) {
        String code = surveyPointVo.getCode();
        String name = surveyPointVo.getName();
        SurveyPointVo surveyPoint = iSurveyPointService.querySurveyPointByNameOrCode(name, code, DictConstant.TableNamePrefix.SURVEY_POINT + sectionCode);
        if (surveyPoint != null) {
            return RespVOBuilder.failure("测点信息已经存在");
        }
        HeaderVo headerVo = HeaderUtils.parseHeader(request);
        String userId = authClient.getAuthVo(headerVo).getUserId();
        surveyPointVo.setId(SnowflakeIdUtils.createId());
        RespVO respVO = iSurveyPointService.create(surveyPointVo, DictConstant.TableNamePrefix.SURVEY_POINT + sectionCode);
        logSender.sendLogEvent(HttpUtils.getClientIp(request), userId, "添加测点:[" + surveyPointVo.getId() + "]");
        return respVO;
    }


    @ApiOperation(value = "批量添加测点", notes = "批量添加测点", httpMethod = "POST")
    @ApiImplicitParams({

    })
    @RequestMapping(value = "/createBatch/{sectionCode}", method = RequestMethod.POST)
    public RespVO createBatch(@Validated @RequestBody List<SurveyPointVo> surveyPointVos,
                              @PathVariable(value = "sectionCode") String sectionCode,
                              HttpServletRequest request) {
        HeaderVo headerVo = HeaderUtils.parseHeader(request);
        String userId = authClient.getAuthVo(headerVo).getUserId();
        //TODO 校验权限
        RespVO respVO = iSurveyPointService.createBatch(surveyPointVos, DictConstant.TableNamePrefix.SURVEY_POINT + sectionCode);
        logSender.sendLogEvent(HttpUtils.getClientIp(request), userId, "批量添加测点");
        return respVO;
    }


    @ApiOperation(value = "修改测点", notes = "修改测点", httpMethod = "PUT")
    @ApiImplicitParams({

    })
    @RequestMapping(value = "/modify/{sectionCode}", method = RequestMethod.PUT)
    public RespVO modify(@Validated @RequestBody SurveyPointVo surveyPointVo,
                         @PathVariable(value = "sectionCode") String sectionCode,
                         HttpServletRequest request) {
        HeaderVo headerVo = HeaderUtils.parseHeader(request);
        String userId = headerVo.getUserId();
        //TODO 校验权限
        RespVO respVO = iSurveyPointService.modify(surveyPointVo.getSurveyPoint(), DictConstant.TableNamePrefix.SURVEY_POINT + sectionCode);
        logSender.sendLogEvent(HttpUtils.getClientIp(request), userId, "修改测点:[" + surveyPointVo.getId() + "]");
        return respVO;
    }

    @ApiOperation(value = "查询测点列表", httpMethod = "GET", notes = "查询测点列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "pageIndex", value = "当前页", dataType = "int", defaultValue = "1", example = "1", paramType = "path"),
            @ApiImplicitParam(name = "pageSize", value = "每页大小", dataType = "int", defaultValue = "10", example = "10", paramType = "query"),
            @ApiImplicitParam(name = "workspaceCode", value = "工区code", dataType = "String", required = true, paramType = "query"),
            @ApiImplicitParam(name = "startTimestamp", value = "开始时间", dataType = "long", example = "1", paramType = "query"),
            @ApiImplicitParam(name = "endTimestamp", value = "结束时间", dataType = "long", example = "1", paramType = "query"),

    })
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public RespVO<RespDataVO<SurveyPointVo>> query(
            @RequestParam(value = "workspaceCode") String workspaceCode,
            @RequestParam(value = "pageIndex", required = false, defaultValue = "1") int pageIndex,
            @RequestParam(required = false, defaultValue = "10") int pageSize,
            @RequestParam(required = false) Long startTimestamp,
            @RequestParam(required = false) Long endTimestamp,
            HttpServletRequest request
    ) {
        HeaderVo headerVo = HeaderUtils.parseHeader(request);
        String deviceType = headerVo.getDeviceType();
        RespVO<Section> sectionRespVO = sectionClient.queryByWorkspaceCode(workspaceCode);
        if (sectionRespVO.getRetCode() != RespConsts.SUCCESS_RESULT_CODE) {
            return RespVOBuilder.success(new ArrayList<>());
        }
        Section section = sectionRespVO.getInfo();
        if (section == null) {
            return RespVOBuilder.success(new ArrayList<>());
        }
        String sectionCode = section.getCode();
        List<OwnWorkspace> workSpaces = section.getWorkSpace();
        if (workSpaces == null) {
            return RespVOBuilder.success(new ArrayList<>());
        }
        Date startDate = null;
        Date endDate = null;
        if (startTimestamp != null && endTimestamp != null) {
            startDate = new Date(startTimestamp);
            endDate = new Date(endTimestamp);
        }
        for (OwnWorkspace workSpace : workSpaces) {
            if (workSpace.getCode().equals(workspaceCode)) {
                List<SurveyPointVo> pointList = iSurveyPointService.list(pageIndex, pageSize, deviceType,workspaceCode, DictConstant.TableNamePrefix.SURVEY_POINT + sectionCode, startDate, endDate);
                return RespVOBuilder.success(pointList);
            }
        }
        return RespVOBuilder.success();
    }


    @ApiOperation(value = "删除测点", httpMethod = "DELETE", notes = "删除测点")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "codes", value = "测点code", dataType = "String", required = true, example = "1", paramType = "query"),
            @ApiImplicitParam(name = "sectionCode", value = "标段code", dataType = "String", required = true, paramType = "path"),
    })
    @RequestMapping(value = "/delete/{sectionCode}", method = RequestMethod.DELETE)
    public RespVO delete(
            @PathVariable(value = "sectionCode") String sectionCode,
            @RequestParam List<String> codes,
            HttpServletRequest request
    ) {
        HeaderVo headerVo = HeaderUtils.parseHeader(request);
        String userId = authClient.getAuthVo(headerVo).getUserId();
        RespVO delete = iSurveyPointService.delete(codes, DictConstant.TableNamePrefix.SURVEY_POINT + sectionCode);
        logSender.sendLogEvent(HttpUtils.getClientIp(request), userId, "删除测点:[" + JSONObject.toJSONString(codes) + "]");
        return delete;
    }




    @ApiOperation(value = "Excel批量上传测点", notes = "Excel批量上传测点", httpMethod = "POST")
    @ApiImplicitParams({

    })
    @RequestMapping(value = "/uploadBatch",method = RequestMethod.POST)
    public RespVO uploadPointResultExcel(@RequestParam(value = "fileName") String fileName,
                                         @RequestParam() String workspaceCode){
        if(StringUtils.isEmpty(fileName)){
            return RespVOBuilder.failure("文件名不能为空");
        }
        String filePath = FpFileUtil.getFilePath(fpFileRootPath,fileName);
        surveyPointReadListener.setWorkspaceCode(workspaceCode);
        excelService.readExcel(filePath,surveyPointReadListener, SurveyPointVo.class,1);
        return RespVOBuilder.success();
    }




}
