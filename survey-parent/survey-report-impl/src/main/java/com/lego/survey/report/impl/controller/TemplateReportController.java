package com.lego.survey.report.impl.controller;
import com.lego.survey.auth.feign.AuthClient;
import com.lego.survey.report.model.entity.TemplateReport;
import com.lego.survey.report.impl.service.ITemplateReportService;
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
 * @since 2019/1/7
 **/
@Validated
@RestController
@RequestMapping(DictConstant.Path.TEMPLATE_REPORT)
@Api(value = "TemplateReportController", description = "报表模板接口")
public class TemplateReportController {

    @Autowired
    private LogSender logSender;

    @Autowired
    private AuthClient authClient;

    @Autowired
    private ITemplateReportService iTemplateReportService;


    @ApiOperation(value = "添加模板", notes = "添加模板", httpMethod = "POST")
    @ApiImplicitParams({

    })
    @RequestMapping(value = "/create", method = RequestMethod.POST)
    public RespVO create(@Validated @RequestBody TemplateReport templateReport, HttpServletRequest request) {
        HeaderVo headerVo = HeaderUtils.parseHeader(request);
        String userId = authClient.getAuthVo(headerVo).getUserId();
        //TODO 校验权限
        templateReport.setId(SnowflakeIdUtils.createId());
        templateReport.setCreateUser(userId);
        RespVO respVO = iTemplateReportService.create(templateReport);
        logSender.sendLogEvent(HttpUtils.getClientIp(request),userId,"添加报告模板:["+templateReport.getId()+"]");
        return respVO;
    }


    @ApiOperation(value = "修改模板", notes = "修改模板", httpMethod = "PUT")
    @ApiImplicitParams({

    })
    @RequestMapping(value = "/modify", method = RequestMethod.PUT)
    public RespVO modify(@Validated @RequestBody TemplateReport templateReport, HttpServletRequest request) {
        HeaderVo headerVo = HeaderUtils.parseHeader(request);
        String userId = authClient.getAuthVo(headerVo).getUserId();
        //TODO 校验权限
        RespVO respVO = iTemplateReportService.modify(templateReport);
        logSender.sendLogEvent(HttpUtils.getClientIp(request),userId,"修改报告模板:["+templateReport.getId()+"]");
        return respVO;
    }



    @ApiOperation(value = "删除模板", notes = "删除模板", httpMethod = "DELETE")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "模板id", dataType = "long",required = true,example = "1",paramType = "query"),
    })
    @RequestMapping(value = "/delete", method = RequestMethod.DELETE)
    public RespVO delete(@RequestParam Long id, HttpServletRequest request) {
        HeaderVo headerVo = HeaderUtils.parseHeader(request);
        String userId = authClient.getAuthVo(headerVo).getUserId();
        //TODO 校验权限
        RespVO respVO = iTemplateReportService.delete(id);
        logSender.sendLogEvent(HttpUtils.getClientIp(request),userId,"删除报告模板:["+id+"]");
        return respVO;
    }


    @ApiOperation(value = "列举所有报告模板信息", notes = "列举所有报告模板信息", httpMethod = "GET")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "pageIndex", value = "当前页", dataType = "int", required = true,example = "1", paramType = "path"),
            @ApiImplicitParam(name = "pageSize", value = "每页大小", dataType = "int",defaultValue = "10",example = "10",paramType = "query"),
    })
    @RequestMapping(value = "/list/{pageIndex}", method = RequestMethod.GET)
    public RespVO<RespDataVO<TemplateReport>> list(@PathVariable(value = "pageIndex") int pageIndex,
                                                   @RequestParam(required = false,defaultValue = "10") int pageSize,
                                                   @RequestParam(required = false) String sectionCode,
                                                   HttpServletRequest request) {
        HeaderVo headerVo = HeaderUtils.parseHeader(request);
        String userId = authClient.getAuthVo(headerVo).getUserId();
        // TODO 权限校验
        return iTemplateReportService.list(pageIndex, pageSize,sectionCode);
    }



    @ApiOperation(value = "根据名称获取模板信息", notes = "列举所有报告模板信息", httpMethod = "GET")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "name", value = "模板名称", dataType = "String", required = true,paramType = "query"),
    })
    @RequestMapping(value = "/findByName", method = RequestMethod.GET)
    public RespVO<TemplateReport> findByName(@RequestParam String name,
                                                   HttpServletRequest request) {
        HeaderVo headerVo = HeaderUtils.parseHeader(request);
        String userId = authClient.getAuthVo(headerVo).getUserId();
        // TODO 权限校验
        return iTemplateReportService.findByName(name);
    }



}
