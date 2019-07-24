package com.lego.survey.settlement.impl.controller;
import com.alibaba.fastjson.JSONObject;
import com.lego.survey.auth.feign.AuthClient;
import com.lego.survey.base.annotation.Operation;
import com.lego.survey.base.annotation.Resource;
import com.lego.survey.project.feign.SectionClient;
import com.lego.survey.project.model.entity.Section;
import com.lego.survey.project.model.vo.SectionVo;
import com.lego.survey.settlement.impl.service.ISurveyOriginalService;
import com.lego.survey.settlement.model.entity.SurveyOriginal;
import com.lego.survey.settlement.model.vo.SurveyOriginalVo;
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
    @RequestMapping(value = "/create/{sectionCode}", method = RequestMethod.POST)
    @Operation(value = "/create/{sectionCode}",desc = "上传原始数据")
    public RespVO create(@Validated @RequestBody SurveyOriginalVo surveyOriginalVo,
                         @PathVariable(value = "sectionCode") String sectionCode,
                         HttpServletRequest request) {
        // TODO ID -> CODE
        HeaderVo headerVo = HeaderUtils.parseHeader(request);
        String userId = authClient.getAuthVo(headerVo).getUserId();
        //TODO 校验权限
        if (surveyOriginalVo.getId() == null) {
            surveyOriginalVo.setId(SnowflakeIdUtils.createId());
        }
        RespVO respVO = iSurveyOriginalService.create(surveyOriginalVo.getSurveyOriginal(), DictConstant.TableNamePrefix.SURVEY_ORIGINAL + sectionCode);
        logSender.sendLogEvent(HttpUtils.getClientIp(request), userId, "新增原始数据:[" + surveyOriginalVo.getId() + "]");
        return respVO;
    }


    @ApiOperation(value = "批量上传原始数据", notes = "批量上传原始数据", httpMethod = "POST")
    @ApiImplicitParams({

    })

    @RequestMapping(value = "/createBatch/{sectionCode}", method = RequestMethod.POST)
    public RespVO createBatch(@Validated @RequestBody List<SurveyOriginalVo> surveyOriginalVos,
                              @PathVariable(value = "sectionCode") String sectionCode,
                              HttpServletRequest request) {
        // TODO ID -> CODE
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
        RespVO respVO = iSurveyOriginalService.createBatch(surveyOriginals, DictConstant.TableNamePrefix.SURVEY_ORIGINAL + sectionCode);
        logSender.sendLogEvent(HttpUtils.getClientIp(request), userId, "批量新增原始数据");
        return respVO;
    }


    @ApiOperation(value = "修改原始数据", notes = "修改原始数据", httpMethod = "PUT")
    @ApiImplicitParams({

    })
    @RequestMapping(value = "/modify/{sectionCode}", method = RequestMethod.PUT)
    public RespVO modify(@Validated @RequestBody SurveyOriginalVo surveyOriginalVo,
                         @PathVariable(value = "sectionCode") String sectionCode,
                         HttpServletRequest request) {
        // TODO ID -> CODE
        HeaderVo headerVo = HeaderUtils.parseHeader(request);
        String userId = authClient.getAuthVo(headerVo).getUserId();
        //TODO 校验权限
        RespVO respVO = iSurveyOriginalService.modify(surveyOriginalVo.getSurveyOriginal(), DictConstant.TableNamePrefix.SURVEY_ORIGINAL + sectionCode);
        logSender.sendLogEvent(HttpUtils.getClientIp(request), userId, "修改原始数据:[" + surveyOriginalVo.getId() + "]");
        return respVO;
    }


    @ApiOperation(value = "查询原始数据列表", httpMethod = "GET", notes = "查询原始数据列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "pageIndex", value = "当前页", dataType = "int", required = true, example = "1", paramType = "path"),
            @ApiImplicitParam(name = "pageSize", value = "每页大小", dataType = "int", defaultValue = "10", example = "10", paramType = "query"),
            @ApiImplicitParam(name = "sectionCode", value = "标段CODE", dataType = "String", required = true, paramType = "path"),
            @ApiImplicitParam(name = "taskId", value = "任务id", dataType = "Long", example = "1", paramType = "query"),
    })
    @RequestMapping(value = "/list/{sectionCode}/{pageIndex}", method = RequestMethod.GET)
    public RespVO<RespDataVO<SurveyOriginalVo>> query(
            @PathVariable(value = "sectionCode") String sectionCode,
            @PathVariable(value = "pageIndex") int pageIndex,
            @RequestParam(required = false, defaultValue = "10") int pageSize,
            @RequestParam(required = false) Long taskId
    ) {
        // TODO ID -> CODE
        List<SurveyOriginalVo> surveyOriginals = iSurveyOriginalService.list(pageIndex, pageSize, taskId, DictConstant.TableNamePrefix.SURVEY_ORIGINAL + sectionCode);
        return RespVOBuilder.success(surveyOriginals);
    }


    @ApiOperation(value = "删除原始数据", httpMethod = "DELETE", notes = "删除原始数据")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "codes", value = "原始数据code", dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "sectionCode", value = "标段CODE", dataType = "String", required = true, paramType = "path"),
    })
    @RequestMapping(value = "/delete/{sectionCode}", method = RequestMethod.DELETE)
    public RespVO delete(
            @PathVariable(value = "sectionCode") String sectionCode,
            @RequestParam List<String> codes,
            HttpServletRequest request
    ) {
        // TODO ID -> CODE
        HeaderVo headerVo = HeaderUtils.parseHeader(request);
        String userId = headerVo.getUserId();
        RespVO delete = iSurveyOriginalService.delete(codes, DictConstant.TableNamePrefix.SURVEY_ORIGINAL + sectionCode);
        logSender.sendLogEvent(HttpUtils.getClientIp(request), userId, "删除原始数据:[" + JSONObject.toJSONString(codes) + "]");
        return delete;
    }



    @ApiOperation(value = "查询任务所有原始数据", httpMethod = "GET", notes = "查询任务所有原始数据列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "sectionCode", value = "标段code", dataType = "String", required = true, paramType = "path"),
            @ApiImplicitParam(name = "taskId", value = "任务id", dataType = "Long", example = "1", paramType = "query",required = true),
    })
    @RequestMapping(value = "/list/{sectionCode}", method = RequestMethod.GET)
    public RespVO<RespDataVO<SurveyOriginalVo>> queryAll(
            @PathVariable(value = "sectionCode") String sectionCode,
            @RequestParam Long taskId
    ) {
        // TODO ID -> CODE
        List<SurveyOriginalVo> surveyOriginals = iSurveyOriginalService.list(taskId,  sectionCode);
        return RespVOBuilder.success(surveyOriginals);
    }

}
