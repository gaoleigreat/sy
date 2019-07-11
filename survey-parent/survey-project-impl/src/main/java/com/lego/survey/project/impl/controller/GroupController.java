package com.lego.survey.project.impl.controller;
import com.lego.survey.auth.feign.AuthClient;
import com.lego.survey.base.annotation.Operation;
import com.lego.survey.base.annotation.Resource;
import com.lego.survey.project.impl.service.IGroupService;
import com.lego.survey.project.model.entity.Group;
import com.lego.survey.project.model.vo.GroupTreeVo;
import com.lego.survey.project.model.vo.GroupVo;
import com.lego.survey.event.user.LogSender;
import com.survey.lib.common.consts.DictConstant;
import com.survey.lib.common.consts.RespConsts;
import com.survey.lib.common.page.PagedResult;
import com.survey.lib.common.utils.HeaderUtils;
import com.survey.lib.common.utils.HttpUtils;
import com.survey.lib.common.utils.UuidUtils;
import com.survey.lib.common.vo.HeaderVo;
import com.survey.lib.common.vo.RespDataVO;
import com.survey.lib.common.vo.RespVO;
import com.survey.lib.common.vo.RespVOBuilder;
import io.swagger.annotations.*;
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
@RequestMapping(DictConstant.Path.GROUP)
@Api(value = "GroupController", description = "所属单位")
@Resource(value="group", desc="公司管理")
public class GroupController {

    @Autowired
    IGroupService iGroupService;

    @Autowired
    private AuthClient authClient;

    @Autowired
    private LogSender logSender;

    @ApiOperation(value = "新增公司", notes = "新增公司", httpMethod = "POST")
    @ApiImplicitParams({

    })
    @Operation(value = "create",desc = "新增公司")
    @RequestMapping(value = "/create", method = RequestMethod.POST)
    public RespVO create(@Validated @RequestBody Group group,
                         HttpServletRequest request) {
        String groupName = group.getName();
        Group queryByName = iGroupService.queryByName(groupName);
        if (queryByName != null) {
            return RespVOBuilder.failure(RespConsts.FAIL_RESULT_CODE, "单位名称已经存在");
        }
        HeaderVo headerVo = HeaderUtils.parseHeader(request);
        String userId = authClient.getAuthVo(headerVo).getUserId();
        //TODO 校验权限
        group.setId(UuidUtils.generateShortUuid());
        RespVO respVO = iGroupService.add(group);
        logSender.sendLogEvent(HttpUtils.getClientIp(request), userId, "新增单位:[" + group.getId() + "]");
        return respVO;
    }

    @ApiOperation(value = "修改公司", notes = "修改公司", httpMethod = "PUT")
    @ApiImplicitParams({

    })
    @Operation(value = "modify",desc = "新增公司")
    @RequestMapping(value = "/modify", method = RequestMethod.PUT)
    public RespVO modify(@Validated @RequestBody Group group, HttpServletRequest request) {
        HeaderVo headerVo = HeaderUtils.parseHeader(request);
        String userId = authClient.getAuthVo(headerVo).getUserId();
        iGroupService.modify(group);
        logSender.sendLogEvent(HttpUtils.getClientIp(request), userId, "修改单位信息:[" + group.getId() + "]");
        return RespVOBuilder.success();
    }


    @ApiOperation(value = "查询公司", notes = "查询公司", httpMethod = "GET")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "单位id", dataType = "String", required = true, paramType = "query"),
    })
    @Operation(value = "query",desc = "查询公司")
    @RequestMapping(value = "/query", method = RequestMethod.GET)
    public RespVO<GroupVo> query(@RequestParam String id, HttpServletRequest request) {
        HeaderVo headerVo = HeaderUtils.parseHeader(request);
        GroupVo groupVo = iGroupService.queryById(id);
        return RespVOBuilder.success(groupVo);
    }

    @ApiOperation(value = "删除公司", notes = "删除公司", httpMethod = "DELETE")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "单位id", dataType = "String", required = true, paramType = "query"),
    })
    @Operation(value = "delete",desc = "删除公司")
    @RequestMapping(value = "/delete", method = RequestMethod.DELETE)
    public RespVO delete(@RequestParam String id, HttpServletRequest request) {
        HeaderVo headerVo = HeaderUtils.parseHeader(request);
        String userId = authClient.getAuthVo(headerVo).getUserId();
        //TODO 权限校验
        iGroupService.delete(id);
        logSender.sendLogEvent(HttpUtils.getClientIp(request), userId, "删除单位:[" + id + "]");
        return RespVOBuilder.success();
    }

    @ApiOperation(value = "查询公司列表", notes = "查询公司列表", httpMethod = "GET")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "pageIndex", value = "当前页", dataType = "int", required = true, example = "1", paramType = "path"),
            @ApiImplicitParam(name = "pageSize", value = "每页大小", dataType = "int", defaultValue = "10", example = "10", paramType = "query"),
    })
    @Operation(value = "list",desc = "查询公司列表")
    @RequestMapping(value = "/list/{pageIndex}", method = RequestMethod.GET)
    public RespVO<PagedResult<GroupVo>> list(@PathVariable(value = "pageIndex") int pageIndex,
                                             @RequestParam(required = false, defaultValue = "10") int pageSize,
                                             HttpServletRequest request) {
        HeaderVo headerVo = HeaderUtils.parseHeader(request);
        String userId = authClient.getAuthVo(headerVo).getUserId();
        PagedResult<GroupVo> dataVO = iGroupService.list(pageIndex, pageSize);
        return RespVOBuilder.success(dataVO);
    }


    @ApiOperation(value = "查询公司列表", notes = "查询公司列表", httpMethod = "GET")
    @ApiImplicitParams({

    })
    @Operation(value = "findAll",desc = "查询公司列表")
    @RequestMapping(value = "/findAll", method = RequestMethod.GET)
    public RespVO<RespDataVO<Group>> list(HttpServletRequest request) {
        HeaderVo headerVo = HeaderUtils.parseHeader(request);
        String userId = authClient.getAuthVo(headerVo).getUserId();
        List<Group> groups = iGroupService.findAll();
        return RespVOBuilder.success(groups);
    }



    @ApiOperation(value = "查询公司列表", notes = "查询公司列表", httpMethod = "GET")
    @ApiImplicitParams({

    })
    @Operation(value = "findTreeList",desc = "查询公司列表")
    @RequestMapping(value = "/findTreeList", method = RequestMethod.GET)
    public RespVO<RespDataVO<GroupTreeVo>> findTreeList(HttpServletRequest request) {
        HeaderVo headerVo = HeaderUtils.parseHeader(request);
        String userId = authClient.getAuthVo(headerVo).getUserId();
        List<GroupTreeVo> groupTreeVos = iGroupService.findTreeList();
        return RespVOBuilder.success(groupTreeVos);
    }




    @ApiOperation(value = "查询母公司", notes = "查询母公司", httpMethod = "GET")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "公司id", dataType = "String", required = true, paramType = "query"),
    })
    @Operation(value = "queryFull",desc = "查询母公司")
    @RequestMapping(value = "/queryFull", method = RequestMethod.GET)
    public RespVO<RespDataVO<GroupVo>> queryFullGroup(@RequestParam String id, HttpServletRequest request) {
        HeaderVo headerVo = HeaderUtils.parseHeader(request);
        String userId = authClient.getAuthVo(headerVo).getUserId();
        //TODO 权限校验
        RespDataVO<GroupVo> completeGroup = iGroupService.listCompleteGroup(id);
        return RespVOBuilder.success(completeGroup);
    }

}
