package com.lego.survey.project.impl.controller;

import com.lego.survey.auth.feign.AuthClient;
import com.lego.survey.base.annotation.Operation;
import com.lego.survey.base.annotation.Resource;
import com.lego.survey.project.impl.service.IProjectService;
import com.lego.survey.project.model.entity.OwnerProject;
import com.lego.survey.project.model.entity.Project;
import com.lego.survey.project.model.vo.ProjectAddVo;
import com.lego.survey.project.model.vo.TreeVo;
import com.lego.survey.project.model.vo.ProjectVo;
import com.lego.survey.event.user.LogSender;
import com.survey.lib.common.consts.DictConstant;
import com.survey.lib.common.consts.RespConsts;
import com.survey.lib.common.page.PagedResult;
import com.survey.lib.common.utils.HeaderUtils;
import com.survey.lib.common.utils.HttpUtils;
import com.survey.lib.common.vo.*;
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
 * @since 2018/12/21
 **/
@Validated
@RestController
@RequestMapping(DictConstant.Path.PROJECT)
@Api(value = "ProjectController", description = "工程管理")
@Resource(value = "project", desc = "工程管理")
public class ProjectController {

    @Autowired
    private IProjectService iProjectService;

    @Autowired
    private AuthClient authClient;

    @Autowired
    private LogSender logSender;


    @ApiOperation(value = "新增工程", notes = "新增工程", httpMethod = "POST")
    @ApiImplicitParams({

    })
    @Operation(value = "create", desc = "新增工程")
    @RequestMapping(value = "/create", method = RequestMethod.POST)
    public RespVO create(@Validated @RequestBody ProjectAddVo projectAddVo, HttpServletRequest request) {
        String projectName = projectAddVo.getName();
        Project queryByName = iProjectService.queryByName(projectName);
        if (queryByName != null) {
            return RespVOBuilder.failure(RespConsts.FAIL_RESULT_CODE, "项目名称已经存在");
        }
        String projectCode = projectAddVo.getCode();
        ProjectVo queryByCode = iProjectService.queryByCode(projectCode);
        if (queryByCode != null) {
            return RespVOBuilder.failure(RespConsts.FAIL_RESULT_CODE, "项目编号已经存在");
        }
        HeaderVo headerVo = HeaderUtils.parseHeader(request);
        CurrentVo currentVo = authClient.getAuthVo(headerVo);
        String userId = currentVo.getUserId();
        String role = currentVo.getRole();
        // TODO 权限校验
        if (!role.equals(DictConstant.Role.ADMIN)) {
            return RespVOBuilder.failure(RespConsts.FAIL_NOPRESSION_CODE, RespConsts.FAIL_NOPRESSION_MSG);
        }
        Project project = projectAddVo.loadProject();
        RespVO respVO = iProjectService.add(project);
        if (respVO.getRetCode() == RespConsts.SUCCESS_RESULT_CODE) {
            try {
                logSender.sendLogEvent(HttpUtils.getClientIp(request), userId, "新增项目工程:[" + project.getId() + "]");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return respVO;
    }


    @ApiOperation(value = "修改工程", notes = "修改工程", httpMethod = "POST")
    @ApiImplicitParams({

    })
    @Operation(value = "modify", desc = "修改工程")
    @RequestMapping(value = "/modify", method = RequestMethod.POST)
    public RespVO modify(@Validated @RequestBody Project project, HttpServletRequest request) {
        HeaderVo headerVo = HeaderUtils.parseHeader(request);
        String userId = authClient.getAuthVo(headerVo).getUserId();
        RespVO modify = iProjectService.modify(project);
        logSender.sendLogEvent(HttpUtils.getClientIp(request), userId, "修改项目工程:[" + project.getId() + "]");
        return modify;
    }


    @ApiOperation(value = "查询工程详细信息", notes = "查询工程详细信息", httpMethod = "GET")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "code", value = "工程code", dataType = "String", required = true, paramType = "query"),

    })
    @Operation(value = "info", desc = "查询工程详细信息")
    @RequestMapping(value = "/info", method = RequestMethod.GET)
    public RespVO<ProjectVo> query(@RequestParam String code, HttpServletRequest request) {
        HeaderVo headerVo = HeaderUtils.parseHeader(request);
        String userId = authClient.getAuthVo(headerVo).getUserId();
        ProjectVo projectVo = iProjectService.queryByCode(code);
        return RespVOBuilder.success(projectVo);
    }

    @ApiOperation(value = "删除工程", notes = "删除工程", httpMethod = "DELETE")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "code", value = "工程code", dataType = "String", required = true, paramType = "query"),
    })
    @Operation(value = "delete", desc = "删除工程")
    @RequestMapping(value = "/delete", method = RequestMethod.DELETE)
    public RespVO<RespDataVO<ProjectVo>> delete(HttpServletRequest request,
                                                @RequestParam String code) {
        HeaderVo headerVo = HeaderUtils.parseHeader(request);
        CurrentVo currentVo = authClient.getAuthVo(headerVo);
        String userId = currentVo.getUserId();
        String role = currentVo.getRole();
        if (!role.equals("admin")) {
            return RespVOBuilder.failure(RespConsts.FAIL_NOPRESSION_CODE, RespConsts.FAIL_NOPRESSION_MSG);
        }
        iProjectService.deleteProject(code);
        logSender.sendLogEvent(HttpUtils.getClientIp(request), userId, "删除项目工程:[" + code + "]");
        return RespVOBuilder.success();
    }


    @ApiOperation(value = "根据用户id查询项目工程", notes = "根据用户id查询项目工程", httpMethod = "GET")
    @ApiImplicitParams({


    })
    @Operation(value = "queryByUserId", desc = "根据用户id查询项目工程")
    @RequestMapping(value = "/queryByUserId", method = RequestMethod.GET)
    public RespVO<RespDataVO<ProjectVo>> queryByUserId(HttpServletRequest request) {
        String userId = request.getHeader("userId");
        return iProjectService.queryByUserId(userId);
    }


    @ApiOperation(value = "查询工程列表", notes = "查询工程列表", httpMethod = "GET")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "pageIndex", value = "当前页", dataType = "int", required = true, example = "1", paramType = "path"),
            @ApiImplicitParam(name = "pageSize", value = "每页大小", dataType = "int", defaultValue = "10", example = "10", paramType = "query"),
    })
    @Operation(value = "list", desc = "查询工程列表")
    @RequestMapping(value = "/list/{pageIndex}", method = RequestMethod.GET)
    public RespVO<PagedResult<ProjectVo>> list(@PathVariable(value = "pageIndex") int pageIndex,
                                               @RequestParam(required = false, defaultValue = "10") int pageSize,
                                               HttpServletRequest request) {
        HeaderVo headerVo = HeaderUtils.parseHeader(request);
        CurrentVo currentVo = authClient.getAuthVo(headerVo);
        PagedResult<ProjectVo> pagedResult = iProjectService.list(pageSize, pageIndex);
        return RespVOBuilder.success(pagedResult);
    }


    @ApiOperation(value = "查询全部工程", notes = "查询全部工程", httpMethod = "GET")
    @ApiImplicitParams({

    })
    @Operation(value = "findAll", desc = "查询全部工程")
    @RequestMapping(value = "/findAll", method = RequestMethod.GET)
    public RespVO<RespDataVO<OwnerProject>> findAll(HttpServletRequest request) {
        HeaderVo headerVo = HeaderUtils.parseHeader(request);
        CurrentVo currentVo = authClient.getAuthVo(headerVo);
        String role = currentVo.getRole();
        String userId = currentVo.getUserId();
        List<OwnerProject> list = iProjectService.findAll(role, userId);
        return RespVOBuilder.success(list);
    }


    @ApiOperation(value = "查询工程标段信息", notes = "查询工程标段信息", httpMethod = "GET")
    @ApiImplicitParams({

    })
    @Operation(value = "findProjectSection", desc = "查询工程标段信息")
    @RequestMapping(value = "/findProjectSection", method = RequestMethod.GET)
    public RespVO<RespDataVO<Project>> findProjectSection(HttpServletRequest request) {
        HeaderVo headerVo = HeaderUtils.parseHeader(request);
        CurrentVo currentVo = authClient.getAuthVo(headerVo);
        List<Project> list = iProjectService.findByCurrent(currentVo);
        return RespVOBuilder.success(list);
    }


    @ApiOperation(value = "查询工程标段信息", notes = "查询工程标段信息", httpMethod = "GET")
    @ApiImplicitParams({

    })
    @Operation(value = "findProjectTreeList", desc = "查询工程标段工区信息")
    @RequestMapping(value = "/findProjectTreeList", method = RequestMethod.GET)
    public RespVO<RespDataVO<TreeVo>> findProjectTreeList(HttpServletRequest request) {
        HeaderVo headerVo = HeaderUtils.parseHeader(request);
        CurrentVo currentVo = authClient.getAuthVo(headerVo);
        List<TreeVo> list = iProjectService.findTreeByCurrent(currentVo);
        return RespVOBuilder.success(list);
    }


}
