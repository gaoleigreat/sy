package com.lego.survey.settlement.impl.controller;

import com.alibaba.fastjson.JSONObject;
import com.lego.survey.auth.feign.AuthClient;
import com.lego.survey.event.settlement.SurveyPointResultSource;
import com.lego.survey.project.feign.SectionClient;
import com.lego.survey.project.model.entity.OwnWorkspace;
import com.lego.survey.project.model.entity.Section;
import com.lego.survey.settlement.impl.service.ISurveyResultService;
import com.lego.survey.settlement.model.entity.SurveyResult;
import com.lego.survey.settlement.model.vo.OverrunListVo;
import com.lego.survey.settlement.model.vo.SurveyPontResultVo;
import com.lego.survey.settlement.model.vo.SurveyResultVo;
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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.integration.support.MessageBuilder;
import org.springframework.util.CollectionUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
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


    @ApiOperation(value = "添加成果数据", notes = "添加成果数据", httpMethod = "POST")
    @ApiImplicitParams({

    })
    @RequestMapping(value = "/create/{sectionId}", method = RequestMethod.POST)
    public RespVO create(@Validated @RequestBody SurveyResultVo surveyResultVo,
                         @PathVariable(value = "sectionId") String sectionId,
                         HttpServletRequest request) {
        HeaderVo headerVo = HeaderUtils.parseHeader(request);
        String userId = authClient.getAuthVo(headerVo).getUserId();
        if (surveyResultVo.getId() == null) {
            surveyResultVo.setId(SnowflakeIdUtils.createId());
        }
        RespVO respVO = iSurveyResultService.create(surveyResultVo.getSurveyResult(), DictConstant.TableNamePrefix.SURVEY_RESULT + sectionId);
        logSender.sendLogEvent(HttpUtils.getClientIp(request), userId, "新增成果数据:[" + surveyResultVo.getSurveyResult().getId() + "]");
        surveyPointResultSource.uploadResult().send(MessageBuilder.withPayload(surveyResultVo.getSurveyResult()).setHeader("type", 1).setHeader("sectionId", sectionId).build());
        return respVO;
    }


    @ApiOperation(value = "批量上传成果数据", notes = "批量上传成果数据", httpMethod = "POST")
    @ApiImplicitParams({

    })
    @RequestMapping(value = "/uploadBatch", method = RequestMethod.POST)
    public RespVO uploadBatch(@Validated @RequestBody List<SurveyResult> surveyResults,
                              HttpServletRequest request) {
        if(!CollectionUtils.isEmpty(surveyResults)){
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
        String userId = authClient.getAuthVo(headerVo).getUserId();
        //TODO 校验权限
        if (surveyResults == null || surveyResults.size() <= 0) {
            return RespVOBuilder.failure("上传失败");
        }
        String workspaceCode = surveyResults.get(0).getWorkspaceCode();
        RespVO<Section> sectionRespVO = sectionClient.queryByWorkspaceCode(workspaceCode);
        if (sectionRespVO.getRetCode() != RespConsts.SUCCESS_RESULT_CODE) {
            return RespVOBuilder.failure("上传失败");
        }
        String sectionId = sectionRespVO.getInfo().getId();
        RespVO respVO = iSurveyResultService.createBatch(surveyResults, DictConstant.TableNamePrefix.SURVEY_RESULT + sectionId);
        logSender.sendLogEvent(HttpUtils.getClientIp(request), userId, "批量新增成果数据");
        // 发送上传成果 消息
        surveyPointResultSource.uploadResult().send(MessageBuilder.withPayload(surveyResults).setHeader("type", 2).setHeader("sectionId", sectionId).build());
        return respVO;
    }


    @ApiOperation(value = "修改成果数据", notes = "修改成果数据", httpMethod = "PUT")
    @ApiImplicitParams({

    })
    @RequestMapping(value = "/modify/{sectionId}", method = RequestMethod.PUT)
    public RespVO modify(@Validated @RequestBody SurveyResultVo surveyResultVo,
                         @PathVariable(value = "sectionId") String sectionId,
                         HttpServletRequest request) {
        HeaderVo headerVo = HeaderUtils.parseHeader(request);
        String userId = authClient.getAuthVo(headerVo).getUserId();
        //TODO 校验权限
        RespVO respVO = iSurveyResultService.modify(surveyResultVo.getSurveyResult(), DictConstant.TableNamePrefix.SURVEY_RESULT + sectionId);
        logSender.sendLogEvent(HttpUtils.getClientIp(request), userId, "修改成果数据:[" + surveyResultVo.getId() + "]");
        return respVO;
    }


    @ApiOperation(value = "查询成果列表", httpMethod = "GET", notes = "查询成果列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "pageIndex", value = "当前页", dataType = "int", defaultValue = "1", example = "1", paramType = "query"),
            @ApiImplicitParam(name = "pageSize", value = "每页大小", dataType = "int", defaultValue = "10", example = "10", paramType = "query"),
            @ApiImplicitParam(name = "workspaceId", value = "工区ID", dataType = "String", required = true, paramType = "query"),
            @ApiImplicitParam(name = "startTimestamp", value = "开始时间戳", dataType = "Long", required = true, paramType = "query"),
            @ApiImplicitParam(name = "endTimestamp", value = "结束时间戳", dataType = "Long", required = true, paramType = "query"),

    })
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public RespVO list(
            @RequestParam(required = false, defaultValue = "1") int pageIndex,
            @RequestParam(required = false, defaultValue = "10") int pageSize,
            @RequestParam(value = "workspaceId") String workspaceId,
            Long startTimestamp,
            Long endTimestamp,
            HttpServletRequest request
    ) {
        // 验证用户正确性
        HeaderVo headerVo = HeaderUtils.parseHeader(request);
        RespVO<Section> sectionRespVO = sectionClient.queryByWorkspaceId(workspaceId);
        if (sectionRespVO.getRetCode() != RespConsts.SUCCESS_RESULT_CODE) {
            return RespVOBuilder.success(new ArrayList<>());
        }
        Section info = sectionRespVO.getInfo();
        if (info == null) {
            return RespVOBuilder.success(new ArrayList<>());
        }
        List<OwnWorkspace> workSpaces = info.getWorkSpace();
        for (OwnWorkspace workSpace : workSpaces) {
            if (workSpace.getId().equals(workspaceId)) {
                String code = workSpace.getCode();
                String deviceType = headerVo.getDeviceType();
                Date startDate = null;
                Date endDate = null;
                if (startTimestamp != null && endTimestamp != null) {
                    startDate = new Date(startTimestamp);
                    endDate = new Date(endTimestamp);
                }
                 return iSurveyResultService.list(pageIndex, pageSize, code, startDate, endDate, deviceType, DictConstant.TableNamePrefix.SURVEY_RESULT + info.getId());
            }

        }

        return RespVOBuilder.failure("获取测点信息失败");
    }


    @ApiOperation(value = "删除成果数据", httpMethod = "DELETE", notes = "删除成果数据")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "ids", value = "成果数据id", dataType = "long", required = true, paramType = "path"),
            @ApiImplicitParam(name = "sectionId", value = "标段ID", dataType = "String", required = true, paramType = "path"),
    })
    @RequestMapping(value = "/delete/{sectionId}", method = RequestMethod.DELETE)
    public RespVO delete(
            @PathVariable(value = "sectionId") String sectionId,
            @RequestParam List<Long> ids,
            HttpServletRequest request
    ) {
        HeaderVo headerVo = HeaderUtils.parseHeader(request);
        String userId = authClient.getAuthVo(headerVo).getUserId();
        // 验证用户正确性
        RespVO delete = iSurveyResultService.delete(ids, DictConstant.TableNamePrefix.SURVEY_RESULT + sectionId);
        logSender.sendLogEvent(HttpUtils.getClientIp(request), userId, "删除成果数据:[" + JSONObject.toJSONString(ids) + "]");
        return delete;
    }



    @ApiOperation(value = "成果数据列表", httpMethod = "GET", notes = "成果数据列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "pageIndex", value = "当前页", dataType = "int", required = true, example = "1", paramType = "path"),
            @ApiImplicitParam(name = "pageSize", value = "每页大小", dataType = "int", defaultValue = "10", example = "10", paramType = "query"),
            @ApiImplicitParam(name = "type", value = "0-全部;1-超限", dataType = "int",defaultValue = "0", paramType = "query"),
            @ApiImplicitParam(name = "sectionId", value = "标段ID", dataType = "String", paramType = "query",required = true),
            @ApiImplicitParam(name = "workspaceId", value = "工区ID", dataType = "String", paramType = "query"),

    })
    @RequestMapping(value = "/overrunList/{pageIndex}", method = RequestMethod.GET)
    public  RespVO<PagedResult<OverrunListVo>>   queryOverrunList(@PathVariable(value = "pageIndex") int pageIndex,
                                                                  @RequestParam(required = false, defaultValue = "10") Integer pageSize,
                                                                  @RequestParam(required = false,defaultValue = "0") Integer type,
                                                                  @RequestParam String sectionId,
                                                                  @RequestParam(required = false) String workspaceId){

        PagedResult<OverrunListVo> listVoPagedResult= iSurveyResultService.queryOverrunList(pageIndex,pageSize,sectionId,workspaceId,type);
        return RespVOBuilder.success(listVoPagedResult);
    }


    @ApiOperation(value = "报警点详情", httpMethod = "GET", notes = "报警点详情")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "pageIndex", value = "当前页", dataType = "int", required = true, example = "1", paramType = "path"),
            @ApiImplicitParam(name = "pageSize", value = "每页大小", dataType = "int", defaultValue = "10", example = "10", paramType = "query"),
            @ApiImplicitParam(name = "sectionId", value = "标段ID", dataType = "String", paramType = "query",required = true),
            @ApiImplicitParam(name = "workspaceId", value = "工区ID", dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "pointCode", value = "测点code", dataType = "String", paramType = "query",required = true),
    })
    @RequestMapping(value = "/overrunDetails/{pageIndex}", method = RequestMethod.GET)
    public  RespVO<PagedResult<OverrunListVo>>   queryOverrunDetails(@PathVariable(value = "pageIndex") int pageIndex,
                                                                  @RequestParam(required = false, defaultValue = "10") Integer pageSize,
                                                                  @RequestParam(required = false,defaultValue = "0") Integer type,
                                                                  @RequestParam String sectionId,
                                                                  @RequestParam(required = false) String workspaceId,
                                                                  @RequestParam String pointCode){

        PagedResult<OverrunListVo> listVoPagedResult= iSurveyResultService.queryOverrunDetails(pageIndex,pageSize,sectionId,pointCode,workspaceId,type);
        return RespVOBuilder.success(listVoPagedResult);
    }






    @ApiOperation(value = "通过原始数据查询成果数据", httpMethod = "GET", notes = "通过原始数据查询成果数据")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "sectionId", value = "标段ID", dataType = "String", paramType = "query",required = true),
            @ApiImplicitParam(name = "originalIds", value = "原始数据id", dataType = "List", paramType = "query"),
    })
    @RequestMapping(value = "/query/list", method = RequestMethod.GET)
    public RespVO<RespDataVO<SurveyResult>> query(@RequestParam String sectionId,
                                                    @RequestParam List<Long> originalIds){
        List<SurveyResult>  surveyResults= iSurveyResultService.queryResult(sectionId, originalIds);
        return RespVOBuilder.success(surveyResults);
    }


    @ApiOperation(value = "查询测量点的测量历史数据", httpMethod = "GET", notes = "通过原始数据查询成果数据")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "sectionId", value = "标段ID", dataType = "String", paramType = "query",required = true),
            @ApiImplicitParam(name = "pointCode", value = "测点Id", dataType = "String", paramType = "query",required = true),
    })
    @RequestMapping(value = "/query/pointData", method = RequestMethod.GET)
    public RespVO<RespDataVO<SurveyPontResultVo>> queryPontData(@RequestParam String sectionId,
                                                  @RequestParam String pointCode){
        List<SurveyPontResultVo>  surveyPontResultVos= iSurveyResultService.queryPontResult(sectionId, pointCode);
        return RespVOBuilder.success(surveyPontResultVos);
    }
}
