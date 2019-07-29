package com.lego.survey.settlement.impl.controller;
import com.alibaba.fastjson.JSONObject;
import com.lego.survey.auth.feign.AuthClient;
import com.lego.survey.project.feign.SectionClient;
import com.lego.survey.project.model.entity.Section;
import com.lego.survey.settlement.impl.service.ISurveyTaskService;
import com.lego.survey.settlement.model.vo.SurveyTaskVo;
import com.lego.survey.event.user.LogSender;
import com.survey.lib.common.consts.DictConstant;
import com.survey.lib.common.consts.RespConsts;
import com.survey.lib.common.page.PagedResult;
import com.survey.lib.common.utils.HeaderUtils;
import com.survey.lib.common.utils.HttpUtils;
import com.survey.lib.common.utils.SnowflakeIdUtils;
import com.survey.lib.common.vo.HeaderVo;
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
import java.util.List;

/**
 * @author yanglf
 * @description
 * @since 2019/1/8
 **/
@Validated
@RestController
@RequestMapping(DictConstant.Path.SURVEY_TASK)
@Api(value = "SurveyTaskController", description = "任务接口")
public class SurveyTaskController {

    @Autowired
    private ISurveyTaskService iSurveyTaskService;

    @Autowired
    private AuthClient authClient;

    @Autowired
    private LogSender logSender;

    @Autowired
    private SectionClient sectionClient;


    @ApiOperation(value = "添加任务", notes = "添加任务", httpMethod = "POST")
    @ApiImplicitParams({

    })
    @RequestMapping(value = "/create", method = RequestMethod.POST)
    public RespVO create(@Validated @RequestBody SurveyTaskVo surveyTaskVo,
                         HttpServletRequest request) {
        HeaderVo headerVo = HeaderUtils.parseHeader(request);
        String userId = authClient.getAuthVo(headerVo).getUserId();
        String workspaceCode = surveyTaskVo.getWorkspaceCode();
        RespVO<Section> sectionRespVO = sectionClient.queryByWorkspaceCode(workspaceCode);
        if(sectionRespVO.getRetCode()!= RespConsts.SUCCESS_RESULT_CODE){
            return RespVOBuilder.failure("上传失败");
        }
        Section section = sectionRespVO.getInfo();
        if(section==null){
            return RespVOBuilder.failure("上传失败");
        }
        //TODO 校验权限
        if (surveyTaskVo.getId() == null) {
            surveyTaskVo.setId(SnowflakeIdUtils.createId());
        }
        RespVO respVO = iSurveyTaskService.create(surveyTaskVo, section.getCode());
        logSender.sendLogEvent(HttpUtils.getClientIp(request),userId,"新增任务:["+surveyTaskVo.getId()+"]");
        return respVO;
    }



    @ApiOperation(value = "修改任务", notes = "修改任务", httpMethod = "PUT")
    @ApiImplicitParams({

    })
    @RequestMapping(value = "/modify/{sectionCode}", method = RequestMethod.PUT)
    public RespVO modify(@Validated @RequestBody SurveyTaskVo surveyTaskVo,
                         @PathVariable(value = "sectionCode") String sectionCode,
                         HttpServletRequest request) {
        HeaderVo headerVo = HeaderUtils.parseHeader(request);
        String userId = authClient.getAuthVo(headerVo).getUserId();
        //TODO 校验权限
        RespVO respVO = iSurveyTaskService.modify(surveyTaskVo.getSurveyTask(), DictConstant.TableNamePrefix.SURVEY_TASK + sectionCode);
        logSender.sendLogEvent(HttpUtils.getClientIp(request),userId,"修改任务:["+surveyTaskVo.getId()+"]");
        return respVO;
    }


    @ApiOperation(value = "查询任务列表", httpMethod = "GET", notes = "查询任务列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "pageIndex", value = "当前页", dataType = "int", required = true, example = "1", paramType = "path"),
            @ApiImplicitParam(name = "pageSize", value = "每页大小", dataType = "int", defaultValue = "10", example = "10", paramType = "query"),
            @ApiImplicitParam(name = "sectionCode", value = "标段CODE", dataType = "String",required = true, paramType = "path"),
    })
    @RequestMapping(value = "/list/{sectionCode}/{pageIndex}", method = RequestMethod.GET)
    public RespVO<PagedResult<SurveyTaskVo>> query(
            @PathVariable(value = "sectionCode") String sectionCode,
            @PathVariable(value = "pageIndex") int pageIndex,
            @RequestParam(required = false, defaultValue = "10") int pageSize
    ) {
        // 验证用户正确性
        PagedResult<SurveyTaskVo> surveyTasks = iSurveyTaskService.list(pageIndex, pageSize, DictConstant.TableNamePrefix.SURVEY_TASK + sectionCode);
        return RespVOBuilder.success(surveyTasks);
    }


    @ApiOperation(value = "删除任务", httpMethod = "DELETE", notes = "删除任务")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "ids", value = "任务id", required = true,dataType = "long", paramType = "query"),
            @ApiImplicitParam(name = "sectionCode", value = "标段CODE", dataType = "String",required = true, paramType = "path"),
    })
    @RequestMapping(value = "/delete/{sectionCode}", method = RequestMethod.DELETE)
    public RespVO delete(
            @PathVariable(value = "sectionCode") String sectionCode,
            @RequestParam List<Long> ids,
            HttpServletRequest request
    ) {
        HeaderVo headerVo = HeaderUtils.parseHeader(request);
        String userId = authClient.getAuthVo(headerVo).getUserId();
        // 验证用户正确性
        RespVO delete = iSurveyTaskService.delete(ids, DictConstant.TableNamePrefix.SURVEY_TASK + sectionCode);
       logSender.sendLogEvent(HttpUtils.getClientIp(request),userId,"删除任务:["+ JSONObject.toJSONString(ids) +"]");
        return delete;
    }
}
