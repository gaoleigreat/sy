package com.lego.survey.auth.impl.controller;

import com.lego.survey.auth.impl.service.IResourcesService;
import com.lego.survey.auth.model.entity.Resources;
import com.survey.lib.common.consts.DictConstant;
import com.survey.lib.common.vo.RespVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Map;

/**
 * @author yanglf
 * @description
 * @since 2019/7/10
 **/
@RestController
@RequestMapping(DictConstant.Path.RESOURCES)
//@Resource(value="permission", desc="权限管理")
@Api(tags = "资源点管理")
public class ResourcesController {

    @Autowired
    private IResourcesService iResourcesService;

    @RequestMapping(value = "/findList", method = RequestMethod.GET)
    @ApiOperation(value = "查询列表",httpMethod = "GET")
    public List<Resources> findList(@ModelAttribute Resources resources) {

        return iResourcesService.findList(resources);
    }

    @RequestMapping(value = "/findTree", method = RequestMethod.GET)
    @ApiOperation(value = "查询权限点数结构",httpMethod = "GET")
    public List<Map<String, Object>> findTree(@ModelAttribute Resources resources) {

        return iResourcesService.findTree(resources);
    }

    @RequestMapping(value = "/insertList", method = RequestMethod.POST)
    @ApiOperation(value = "批量新增",httpMethod = "POST")
    public RespVO insertList(@RequestBody List<Resources> resourcesList) {

        return iResourcesService.insertList(resourcesList);
    }

    @RequestMapping(value = "/deleteList", method = RequestMethod.POST)
    @ApiOperation(value = "批量删除",httpMethod = "POST")
    public RespVO deleteList(@RequestBody List<Long> ids) {

        return iResourcesService.deleteList(ids);
    }

    @RequestMapping(value = "/save", method = RequestMethod.POST)
    @ApiOperation(value = "更新",httpMethod = "POST")
    public RespVO save(@RequestParam("scope") String scope, @RequestBody List<Resources> resources) {

        return iResourcesService.save(scope, resources);
    }


    @RequestMapping(value = "/queryRoleResource", method = RequestMethod.POST)
    @ApiOperation(value = "查询角色资源信息",httpMethod = "POST")
    public List<String> queryRoleResource(String role) {

        return iResourcesService.queryRoleResource(role);
    }


}
