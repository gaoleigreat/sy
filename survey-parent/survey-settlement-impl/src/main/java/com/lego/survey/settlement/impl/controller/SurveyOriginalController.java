package com.lego.survey.settlement.impl.controller;
import com.alibaba.fastjson.JSONObject;
import com.lego.survey.auth.feign.AuthClient;
import com.lego.survey.base.annotation.Operation;
import com.lego.survey.base.annotation.Resource;
import com.lego.survey.settlement.impl.service.ISurveyOriginalService;
import com.lego.survey.settlement.model.entity.SurveyOriginal;
import com.lego.survey.settlement.model.vo.SurveyOriginalVo;
import com.lego.survey.event.user.LogSender;
import com.survey.lib.common.consts.DictConstant;
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
import java.util.List;

/**
 * @author yanglf
 * @description
 * @since 2019/1/8
 **/
@Validated
@RestController
@RequestMapping(DictConstant.Path.SURVEY_ORIGINAL)
@Api(value = "SurveyOriginalController", description = "原始数据管理")
@Resource(value = "surveyOriginal",desc = "原始数据管理")
public class SurveyOriginalController {

    @Autowired
    private ISurveyOriginalService iSurveyOriginalService;

    @Autowired
    private AuthClient authClient;

    @Autowired
    private LogSender logSender;


    @ApiOperation(value = "上传原始数据", notes = "上传原始数据", httpMethod = "POST")
    @ApiImplicitParams({

    })
    @RequestMapping(value = "/create/{sectionId}", method = RequestMethod.POST)
    @Operation(value = "/create/{sectionId}",desc = "上传原始数据")
    public RespVO create(@Validated @RequestBody SurveyOriginalVo surveyOriginalVo,
                         @PathVariable(value = "sectionId") String sectionId,
                         HttpServletRequest request) {
        HeaderVo headerVo = HeaderUtils.parseHeader(request);
        String userId = authClient.getAuthVo(headerVo).getUserId();
        //TODO 校验权限
        if (surveyOriginalVo.getId() == null) {
            surveyOriginalVo.setId(SnowflakeIdUtils.createId());
        }
        RespVO respVO = iSurveyOriginalService.create(surveyOriginalVo.getSurveyOriginal(), DictConstant.TableNamePrefix.SURVEY_ORIGINAL + sectionId);
        logSender.sendLogEvent(HttpUtils.getClientIp(request), userId, "新增原始数据:[" + surveyOriginalVo.getId() + "]");
        return respVO;
    }


    @ApiOperation(value = "批量上传原始数据", notes = "批量上传原始数据", httpMethod = "POST")
    @ApiImplicitParams({

    })

    @RequestMapping(value = "/createBatch/{sectionId}", method = RequestMethod.POST)
    public RespVO createBatch(@Validated @RequestBody List<SurveyOriginalVo> surveyOriginalVos,
                              @PathVariable(value = "sectionId") String sectionId,
                              HttpServletRequest request) {
        List<SurveyOriginal> surveyOriginals=new ArrayList<>();
        HeaderVo headerVo = HeaderUtils.parseHeader(request);
        String userId = authClient.getAuthVo(headerVo).getUserId();
        //TODO 校验权限
        surveyOriginalVos.forEach(surveyOriginal -> {
            if(surveyOriginal.getId()==null){
                surveyOriginal.setId(SnowflakeIdUtils.createId());
            }
            surveyOriginals.add(surveyOriginal.getSurveyOriginal());
        });
        RespVO respVO = iSurveyOriginalService.createBatch(surveyOriginals, DictConstant.TableNamePrefix.SURVEY_ORIGINAL + sectionId);
        logSender.sendLogEvent(HttpUtils.getClientIp(request), userId, "批量新增原始数据");
        return respVO;
    }


    @ApiOperation(value = "修改原始数据", notes = "修改原始数据", httpMethod = "PUT")
    @ApiImplicitParams({

    })
    @RequestMapping(value = "/modify/{sectionId}", method = RequestMethod.PUT)
    public RespVO modify(@Validated @RequestBody SurveyOriginalVo surveyOriginalVo,
                         @PathVariable(value = "sectionId") String sectionId,
                         HttpServletRequest request) {
        HeaderVo headerVo = HeaderUtils.parseHeader(request);
        String userId = authClient.getAuthVo(headerVo).getUserId();
        //TODO 校验权限
        RespVO respVO = iSurveyOriginalService.modify(surveyOriginalVo.getSurveyOriginal(), DictConstant.TableNamePrefix.SURVEY_ORIGINAL + sectionId);
        logSender.sendLogEvent(HttpUtils.getClientIp(request), userId, "修改原始数据:[" + surveyOriginalVo.getId() + "]");
        return respVO;
    }


    @ApiOperation(value = "查询原始数据列表", httpMethod = "GET", notes = "查询原始数据列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "pageIndex", value = "当前页", dataType = "int", required = true, example = "1", paramType = "path"),
            @ApiImplicitParam(name = "pageSize", value = "每页大小", dataType = "int", defaultValue = "10", example = "10", paramType = "query"),
            @ApiImplicitParam(name = "sectionId", value = "标段ID", dataType = "String", required = true, paramType = "path"),
            @ApiImplicitParam(name = "taskId", value = "任务id", dataType = "Long", example = "1", paramType = "query"),
    })
    @RequestMapping(value = "/list/{sectionId}/{pageIndex}", method = RequestMethod.GET)
    public RespVO<RespDataVO<SurveyOriginalVo>> query(
            @PathVariable(value = "sectionId") String sectionId,
            @PathVariable(value = "pageIndex") int pageIndex,
            @RequestParam(required = false, defaultValue = "10") int pageSize,
            @RequestParam(required = false) Long taskId
    ) {
        List<SurveyOriginalVo> surveyOriginals = iSurveyOriginalService.list(pageIndex, pageSize, taskId, DictConstant.TableNamePrefix.SURVEY_ORIGINAL + sectionId);
        return RespVOBuilder.success(surveyOriginals);
    }


    @ApiOperation(value = "删除原始数据", httpMethod = "DELETE", notes = "删除原始数据")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "ids", value = "原始数据id", dataType = "long", paramType = "query"),
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
        RespVO delete = iSurveyOriginalService.delete(ids, DictConstant.TableNamePrefix.SURVEY_ORIGINAL + sectionId);
        logSender.sendLogEvent(HttpUtils.getClientIp(request), userId, "删除原始数据:[" + JSONObject.toJSONString(ids) + "]");
        return delete;
    }



    @ApiOperation(value = "查询任务所有原始数据", httpMethod = "GET", notes = "查询任务所有原始数据列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "sectionId", value = "标段ID", dataType = "String", required = true, paramType = "path"),
            @ApiImplicitParam(name = "taskId", value = "任务id", dataType = "Long", example = "1", paramType = "query"),
    })
    @RequestMapping(value = "/list/{sectionId}", method = RequestMethod.GET)
    public RespVO<RespDataVO<SurveyOriginalVo>> queryAll(
            @PathVariable(value = "sectionId") String sectionId,
            @RequestParam Long taskId
    ) {
        List<SurveyOriginalVo> surveyOriginals = iSurveyOriginalService.list(taskId, DictConstant.TableNamePrefix.SURVEY_ORIGINAL + sectionId);
        return RespVOBuilder.success(surveyOriginals);
    }

}
