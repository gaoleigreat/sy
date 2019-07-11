package com.lego.survey.settlement.impl.controller;

import com.alibaba.fastjson.JSONObject;
import com.lego.survey.auth.feign.AuthClient;
import com.lego.survey.project.feign.SectionClient;
import com.lego.survey.project.model.entity.OwnWorkspace;
import com.lego.survey.project.model.entity.Section;
import com.lego.survey.settlement.impl.service.ISurveyPointService;
import com.lego.survey.settlement.model.entity.SurveyPoint;
import com.lego.survey.settlement.model.vo.SurveyPointVo;
import com.lego.survey.event.user.LogSender;
import com.survey.lib.common.consts.DictConstant;
import com.survey.lib.common.consts.RespConsts;
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

    @ApiOperation(value = "添加测点", notes = "添加测点", httpMethod = "POST")
    @ApiImplicitParams({

    })
    @RequestMapping(value = "/create/{sectionId}", method = RequestMethod.POST)
    public RespVO create(@Validated @RequestBody SurveyPointVo surveyPointVo,
                         @PathVariable(value = "sectionId") String sectionId,
                         HttpServletRequest request) {
        String code = surveyPointVo.getCode();
        String name = surveyPointVo.getName();
        SurveyPointVo surveyPoint = iSurveyPointService.querySurveyPointByNameOrCode(name, code, DictConstant.TableNamePrefix.SURVEY_POINT + sectionId);
        if (surveyPoint != null) {
            return RespVOBuilder.failure("测点信息已经存在");
        }
        HeaderVo headerVo = HeaderUtils.parseHeader(request);
        String userId = authClient.getAuthVo(headerVo).getUserId();
        surveyPointVo.setId(SnowflakeIdUtils.createId());
        RespVO respVO = iSurveyPointService.create(surveyPointVo, DictConstant.TableNamePrefix.SURVEY_POINT + sectionId);
        logSender.sendLogEvent(HttpUtils.getClientIp(request), userId, "添加测点:[" + surveyPointVo.getId() + "]");
        return respVO;
    }


    @ApiOperation(value = "批量添加测点", notes = "批量添加测点", httpMethod = "POST")
    @ApiImplicitParams({

    })
    @RequestMapping(value = "/createBatch/{sectionId}", method = RequestMethod.POST)
    public RespVO createBatch(@Validated @RequestBody List<SurveyPointVo> surveyPointVos,
                              @PathVariable(value = "sectionId") String sectionId,
                              HttpServletRequest request) {
        HeaderVo headerVo = HeaderUtils.parseHeader(request);
        String userId = authClient.getAuthVo(headerVo).getUserId();
        List<SurveyPoint> surveyPoints = new ArrayList<>();
        //TODO 校验权限
        surveyPointVos.forEach(surveyPointVo -> {
            surveyPointVo.setId(SnowflakeIdUtils.createId());
            Date currentTime = new Date();
            surveyPointVo.setCreateTime(currentTime);
            surveyPointVo.setUpdateTime(currentTime);
            surveyPoints.add(surveyPointVo.getSurveyPoint());
        });
        RespVO respVO = iSurveyPointService.createBatch(surveyPoints, DictConstant.TableNamePrefix.SURVEY_POINT + sectionId);
        logSender.sendLogEvent(HttpUtils.getClientIp(request), userId, "批量添加测点");
        return respVO;
    }


    @ApiOperation(value = "修改测点", notes = "修改测点", httpMethod = "PUT")
    @ApiImplicitParams({

    })
    @RequestMapping(value = "/modify/{sectionId}", method = RequestMethod.PUT)
    public RespVO modify(@Validated @RequestBody SurveyPointVo surveyPointVo,
                         @PathVariable(value = "sectionId") String sectionId,
                         HttpServletRequest request) {
        HeaderVo headerVo = HeaderUtils.parseHeader(request);
        String userId = authClient.getAuthVo(headerVo).getUserId();
        //TODO 校验权限
        RespVO respVO = iSurveyPointService.modify(surveyPointVo.getSurveyPoint(), DictConstant.TableNamePrefix.SURVEY_POINT + sectionId);
        logSender.sendLogEvent(HttpUtils.getClientIp(request), userId, "修改测点:[" + surveyPointVo.getId() + "]");
        return respVO;
    }

    @ApiOperation(value = "查询测点列表", httpMethod = "GET", notes = "查询测点列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "pageIndex", value = "当前页", dataType = "int", defaultValue = "1", example = "1", paramType = "path"),
            @ApiImplicitParam(name = "pageSize", value = "每页大小", dataType = "int", defaultValue = "10", example = "10", paramType = "query"),
            @ApiImplicitParam(name = "workspaceId", value = "工区ID", dataType = "String", required = true, paramType = "query"),
            @ApiImplicitParam(name = "startTimestamp", value = "开始时间", dataType = "long", example = "1", paramType = "query"),
            @ApiImplicitParam(name = "endTimestamp", value = "结束时间", dataType = "long", example = "1", paramType = "query"),

    })
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public RespVO<RespDataVO<SurveyPointVo>> query(
            @RequestParam(value = "workspaceId") String workspaceId,
            @RequestParam(value = "pageIndex", required = false, defaultValue = "1") int pageIndex,
            @RequestParam(required = false, defaultValue = "10") int pageSize,
            @RequestParam(required = false) Long startTimestamp,
            @RequestParam(required = false) Long endTimestamp,
            HttpServletRequest request
    ) {
        HeaderVo headerVo = HeaderUtils.parseHeader(request);
        String deviceType = headerVo.getDeviceType();
        RespVO<Section> sectionRespVO = sectionClient.queryByWorkspaceId(workspaceId);
        if (sectionRespVO.getRetCode() != RespConsts.SUCCESS_RESULT_CODE) {
            return RespVOBuilder.success(new ArrayList<>());
        }
        Section section = sectionRespVO.getInfo();
        if (section == null) {
            return RespVOBuilder.success(new ArrayList<>());
        }
        String sectionId = section.getId();
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
            if (workSpace.getId().equals(workspaceId)) {
                String workspaceCode = workSpace.getCode();
                List<SurveyPointVo> pointList = iSurveyPointService.list(pageIndex, pageSize, deviceType,workspaceCode, DictConstant.TableNamePrefix.SURVEY_POINT + sectionId, startDate, endDate);
                return RespVOBuilder.success(pointList);
            }
        }
        return RespVOBuilder.success();
    }


    @ApiOperation(value = "删除测点", httpMethod = "DELETE", notes = "删除测点")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "ids", value = "测点id", dataType = "long", required = true, example = "1", paramType = "query"),
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
        RespVO delete = iSurveyPointService.delete(ids, DictConstant.TableNamePrefix.SURVEY_POINT + sectionId);
        logSender.sendLogEvent(HttpUtils.getClientIp(request), userId, "删除测点:[" + JSONObject.toJSONString(ids) + "]");
        return delete;
    }

}
