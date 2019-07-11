package com.lego.survey.settlement.impl.controller;
import com.lego.survey.auth.feign.AuthClient;
import com.lego.survey.settlement.impl.service.ISurveyPointExceptionService;
import com.lego.survey.settlement.model.vo.SurveyPointExceptionVo;
import com.lego.survey.event.user.LogSender;
import com.survey.lib.common.consts.DictConstant;
import com.survey.lib.common.utils.HeaderUtils;
import com.survey.lib.common.utils.HttpUtils;
import com.survey.lib.common.utils.SnowflakeIdUtils;
import com.survey.lib.common.vo.HeaderVo;
import com.survey.lib.common.vo.RespDataVO;
import com.survey.lib.common.vo.RespVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

/**
 * @author yanglf
 * @description
 * @since 2019/1/9
 **/
@Validated
@RestController
@RequestMapping(DictConstant.Path.SURVEY_POINT_EXCEPTION)
@Api(value = "SurveyPointExceptionController", description = "基准点异常接口")
public class SurveyPointExceptionController {

    @Autowired
    private AuthClient authClient;

    @Autowired
    private LogSender logSender;

    @Autowired
    private ISurveyPointExceptionService iSurveyPointExceptionService;

    @ApiOperation(value = "添加异常报警", notes = "添加异常报警", httpMethod = "POST")
    @ApiImplicitParams({

    })
    @RequestMapping(value = "/create", method = RequestMethod.POST)
    public RespVO create(@Validated @RequestBody SurveyPointExceptionVo surveyPointExceptionVo, HttpServletRequest request) {
        HeaderVo headerVo = HeaderUtils.parseHeader(request);
        String userId = authClient.getAuthVo(headerVo).getUserId();
        //TODO 校验权限
        surveyPointExceptionVo.setId(SnowflakeIdUtils.createId());
        RespVO respVO = iSurveyPointExceptionService.create(surveyPointExceptionVo.getSurveyPointException());
        logSender.sendLogEvent(HttpUtils.getClientIp(request), userId, "新增异常报警信息:[" + surveyPointExceptionVo.getId() + "]");
        return respVO;
    }


    @ApiOperation(value = "查询基准点异常列表", httpMethod = "GET", notes = "查询基准点异常列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "pageIndex", value = "当前页", dataType = "int", required = true, example = "1", paramType = "path"),
            @ApiImplicitParam(name = "pageSize", value = "每页大小", dataType = "int", defaultValue = "10", example = "10", paramType = "query"),
            @ApiImplicitParam(name = "sectionCode", value = "标段编号", dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "pointCode", value = "测点编号", dataType = "String", paramType = "query"),
    })
    @RequestMapping(value = "/list/{pageIndex}", method = RequestMethod.GET)
    public RespVO<RespDataVO<SurveyPointExceptionVo>> query(@PathVariable(value = "pageIndex") int pageIndex,
                                                          @RequestParam(required = false, defaultValue = "10") int pageSize,
                                                          @RequestParam(required = false) String sectionCode,
                                                          @RequestParam(required = false) String pointCode) {
        // 验证用户正确性
        //根据用户信息生成用户 token
        return iSurveyPointExceptionService.list(pageIndex, pageSize, sectionCode,pointCode);
    }




    @ApiOperation(value = "删除基准点异常信息", notes = "删除基准点异常信息", httpMethod = "DELETE")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "编号", dataType = "long", required = true),
    })
    @RequestMapping(value = "/delete", method = RequestMethod.DELETE)
    public RespVO delete(@RequestParam Long id,HttpServletRequest request) {
        HeaderVo headerVo = HeaderUtils.parseHeader(request);
        String userId = authClient.getAuthVo(headerVo).getUserId();
        RespVO respVO = iSurveyPointExceptionService.delete(id);
        logSender.sendLogEvent(HttpUtils.getClientIp(request),userId,"删除异常报告信息:["+id+"]");
        return respVO;
    }

    @ApiOperation(value = "修改异常报告", notes = "修改异常报告", httpMethod = "PUT")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "id", dataType = "SurveyPointTypeVo", required = true),
    })
    @RequestMapping(value = "/modify", method = RequestMethod.PUT)
    public RespVO modify(@Validated @RequestBody SurveyPointExceptionVo surveyPointExceptionVo,HttpServletRequest request) {
        HeaderVo headerVo = HeaderUtils.parseHeader(request);
        String userId = authClient.getAuthVo(headerVo).getUserId();
        RespVO modify = iSurveyPointExceptionService.modify(surveyPointExceptionVo.getSurveyPointException());
        logSender.sendLogEvent(HttpUtils.getClientIp(request),userId,"修改异常报告信息:["+surveyPointExceptionVo.getId()+"]");
        return modify;
    }
}
