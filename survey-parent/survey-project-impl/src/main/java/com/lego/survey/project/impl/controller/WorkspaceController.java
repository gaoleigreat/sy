package com.lego.survey.project.impl.controller;
import com.lego.survey.auth.feign.AuthClient;
import com.lego.survey.base.annotation.Operation;
import com.lego.survey.base.annotation.Resource;
import com.lego.survey.project.impl.service.IWorkspaceService;
import com.lego.survey.project.model.entity.OwnWorkspace;
import com.lego.survey.project.model.entity.Section;
import com.lego.survey.project.model.entity.Surveyer;
import com.lego.survey.project.model.entity.Workspace;
import com.lego.survey.event.user.LogSender;
import com.survey.lib.common.consts.DictConstant;
import com.survey.lib.common.consts.RespConsts;
import com.survey.lib.common.utils.HeaderUtils;
import com.survey.lib.common.utils.HttpUtils;
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
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * @author yanglf
 * @description
 * @since 2018/12/21
 **/
@RestController
@RequestMapping(DictConstant.Path.WORKSPACE)
@Api(value = "WorkSpaceController", description = "工区管理")
@Resource(value = "workspace",desc = "工区管理")
public class WorkspaceController {

    @Autowired
    private IWorkspaceService iWorkspaceService;

    @Autowired
    private AuthClient authClient;

    @Autowired
    private LogSender logSender;

    @ApiOperation(value = "添加工区信息", notes = "添加工区信息", httpMethod = "POST")
    @ApiImplicitParams({

    })
    @Operation(value = "create",desc = "添加工区信息")
    @RequestMapping(value = "/create", method = RequestMethod.POST)
    public RespVO create(@RequestBody Workspace workspace,
                         HttpServletRequest request) {
        HeaderVo headerVo = HeaderUtils.parseHeader(request);
        String userId = authClient.getAuthVo(headerVo).getUserId();
        workspace.setId(UuidUtils.generateShortUuid());
        RespVO respVO = iWorkspaceService.add(workspace);
        if(respVO.getRetCode()== RespConsts.SUCCESS_RESULT_CODE){
            logSender.sendLogEvent(HttpUtils.getClientIp(request), userId, "新增工区:[" + workspace.getId() + "]");
        }
        return respVO;
    }


    @ApiOperation(value = "修改工区信息", httpMethod = "PUT", notes = "修改工区信息")
    @ApiImplicitParams({

    })
    @Operation(value = "modify",desc = "修改工区信息")
    @RequestMapping(value = "/modify", method = RequestMethod.PUT)
    public RespVO modify(@RequestBody Workspace workspace, HttpServletRequest request) {
        HeaderVo headerVo = HeaderUtils.parseHeader(request);
        String userId = authClient.getAuthVo(headerVo).getUserId();
        RespVO modify = iWorkspaceService.modify(workspace);
        logSender.sendLogEvent(HttpUtils.getClientIp(request), userId, "修改工区:[" + workspace.getId() + "]");
        return modify;
    }


    @ApiOperation(value = "删除工区信息", httpMethod = "DELETE", notes = "删除工区信息")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "code", value = "工区code", dataType = "String", required = true, paramType = "query"),
    })
    @Operation(value = "delete",desc = "删除工区信息")
    @RequestMapping(value = "/delete", method = RequestMethod.DELETE)
    public RespVO delete(String code, HttpServletRequest request) {
        // TODO ID -> CODE
        HeaderVo headerVo = HeaderUtils.parseHeader(request);
        String userId = authClient.getAuthVo(headerVo).getUserId();
        iWorkspaceService.deleteWorkSpace(code);
        logSender.sendLogEvent(HttpUtils.getClientIp(request), userId, "删除工区:[" + code + "]");
        return RespVOBuilder.success();
    }

    @ApiOperation(value = "根据标段code获取工区信息", notes = "根据标段code获取工区信息", httpMethod = "GET")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "sectionCode", value = "标段code", dataType = "String", required = true, paramType = "query"),
    })
    @Operation(value = "list",desc = "根据标段id获取工区信息")
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public RespVO<RespDataVO<Workspace>> list(HttpServletRequest request,
                                              @RequestParam String sectionCode) {
        // TODO ID -> CODE
        HeaderVo headerVo = HeaderUtils.parseHeader(request);
        return iWorkspaceService.queryByProjectIdAndSectionId(sectionCode);
    }

    @ApiOperation(value = "根据code获取工区信息", notes = "根据code获取工区信息", httpMethod = "GET")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "code", value = "工区code", dataType = "String", required = true, paramType = "path"),
    })
    @Operation(value = "info",desc = "根据ID获取工区信息")
    @RequestMapping(value = "/info/{code}", method = RequestMethod.GET)
    public RespVO<Workspace> info(@PathVariable(value = "code") String code,HttpServletRequest request) {
        // TODO ID -> CODE
        HeaderVo headerVo = HeaderUtils.parseHeader(request);
        Workspace workspace=iWorkspaceService.queryByCode(code);
        return RespVOBuilder.success(workspace);
    }


    @ApiOperation(value = "查询全部工区信息", notes = "查询全部工区信息", httpMethod = "GET")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "sectionCodes", value = "标段CODE", dataType = "String", paramType = "query"),
    })
    @Operation(value = "findAll", desc = "查询全部标段信息")
    @RequestMapping(value = "/findAll", method = RequestMethod.GET)
    public RespVO<RespDataVO<OwnWorkspace>> findAll(@RequestParam(required = false) List<String> sectionCodes,
                                               HttpServletRequest request) {
        // TODO ID -> CODE
        HeaderVo headerVo = HeaderUtils.parseHeader(request);
        List<OwnWorkspace> sections = iWorkspaceService.findAll(sectionCodes);
        return RespVOBuilder.success(sections);
    }


}
