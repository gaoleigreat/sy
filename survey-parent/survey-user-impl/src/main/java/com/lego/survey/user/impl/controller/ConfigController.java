package com.lego.survey.user.impl.controller;

import com.lego.survey.auth.feign.AuthClient;
import com.lego.survey.base.annotation.Operation;
import com.lego.survey.base.annotation.Resource;
import com.lego.survey.event.user.LogSender;
import com.lego.survey.user.impl.service.IConfigService;
import com.lego.survey.user.model.entity.Config;
import com.survey.lib.common.consts.DictConstant;
import com.survey.lib.common.page.PagedResult;
import com.survey.lib.common.utils.HeaderUtils;
import com.survey.lib.common.utils.HttpUtils;
import com.survey.lib.common.utils.UuidUtils;
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
import java.util.Date;
import java.util.List;

/**
 * @author yanglf
 * @description
 * @since 2019/1/3
 **/
@Validated
@RestController
@RequestMapping(DictConstant.Path.CONFIG)
@Api(value = "ConfigController", description = "配置管理")
@Resource(value = "config",desc = "配置管理")
public class ConfigController {

    @Autowired
    private IConfigService iConfigService;

    @Autowired
    private AuthClient authClient;

    @Autowired
    private LogSender logSender;


    @ApiOperation(value = "查询配置信息", httpMethod = "GET", notes = "查询配置信息")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "pageIndex", value = "当前页", dataType = "int", required = true, example = "1", paramType = "path"),
            @ApiImplicitParam(name = "pageSize", value = "每页大小", dataType = "int", defaultValue = "10", example = "10", paramType = "query"),
    })
    @Operation(value = "list",desc = "查询配置信息")
    @RequestMapping(value = "/list/{pageIndex}", method = RequestMethod.GET)
    public RespVO<PagedResult<Config>> list(@PathVariable(value = "pageIndex") int pageIndex,
                                            @RequestParam(required = false, defaultValue = "10") int pageSize,
                                            HttpServletRequest request) {
        HeaderVo headerVo = HeaderUtils.parseHeader(request);
        String userId = authClient.getAuthVo(headerVo).getUserId();
        PagedResult<Config> configList = iConfigService.queryList(pageIndex, pageSize);
        return RespVOBuilder.success(configList);
    }


    @ApiOperation(value = "通过名称查询配置信息", httpMethod = "GET", notes = "通过名称查询配置信息")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "name", value = "配置名称(场景配置，权限配置，角色配置)", dataType = "String", required = true, example = "1", paramType = "query"),
    })
    @Operation(value = "queryByName",desc = "通过名称查询配置信息")
    @RequestMapping(value = "/queryByName", method = RequestMethod.GET)
    public RespVO<Config> queryByName(@RequestParam String name,
                                      HttpServletRequest request) {
        HeaderVo headerVo = HeaderUtils.parseHeader(request);
        String userId = authClient.getAuthVo(headerVo).getUserId();
        Config configList = iConfigService.queryByName(name);
        return RespVOBuilder.success(configList);
    }


    @ApiOperation(value = "新增配置信息", httpMethod = "POST", notes = "新增配置信息")
    @ApiImplicitParams({

    })
    @Operation(value = "create",desc = "新增配置信息")
    @RequestMapping(value = "/create", method = RequestMethod.POST)
    public RespVO create(@Validated @RequestBody Config config, HttpServletRequest request) {
        HeaderVo headerVo = HeaderUtils.parseHeader(request);
        String userId = authClient.getAuthVo(headerVo).getUserId();
        Config queryByName = iConfigService.queryByName(config.getName());
        RespVO respVO;
        if (queryByName != null) {
            queryByName.setUpdateTime(new Date());
            List<String> option = queryByName.getOption();
            option.addAll(config.getOption());
            queryByName.setOption(option);
            respVO = iConfigService.modify(queryByName);
        } else {
            config.setId(UuidUtils.generateShortUuid());
            respVO = iConfigService.addConfig(config);
        }
        logSender.sendLogEvent(HttpUtils.getClientIp(request), userId, "新增配置:[" + config.getId() + "]");
        return respVO;
    }


    @ApiOperation(value = "修改配置信息", httpMethod = "PUT", notes = "修改配置信息")
    @ApiImplicitParams({

    })
    @Operation(value = "modify",desc = "修改配置信息")
    @RequestMapping(value = "/modify", method = RequestMethod.PUT)
    public RespVO modify(@Validated @RequestBody Config config, HttpServletRequest request) {
        HeaderVo headerVo = HeaderUtils.parseHeader(request);
        String userId = authClient.getAuthVo(headerVo).getUserId();
        RespVO respVO = iConfigService.modify(config);
        logSender.sendLogEvent(HttpUtils.getClientIp(request), userId, "修改配置:[" + config.getId() + "]");
        return respVO;
    }


    @ApiOperation(value = "删除配置信息", httpMethod = "DELETE", notes = "删除配置信息")
    @ApiImplicitParams({

    })
    @Operation(value = "delete",desc = "删除配置信息")
    @RequestMapping(value = "/delete", method = RequestMethod.DELETE)
    public RespVO delete(String id, HttpServletRequest request) {
        HeaderVo headerVo = HeaderUtils.parseHeader(request);
        String userId = authClient.getAuthVo(headerVo).getUserId();
        RespVO respVO = iConfigService.delConfig(id);
        logSender.sendLogEvent(HttpUtils.getClientIp(request), userId, "删除配置:[" + id + "]");
        return respVO;
    }


    @ApiOperation(value = "查询配置信息", httpMethod = "GET", notes = "查询配置信息")
    @ApiImplicitParams({

    })
    @Operation(value = "info",desc = "查询配置信息")
    @RequestMapping(value = "/info", method = RequestMethod.GET)
    public RespVO<Config> info(String id, HttpServletRequest request) {
        HeaderVo headerVo = HeaderUtils.parseHeader(request);
        String userId = authClient.getAuthVo(headerVo).getUserId();
        Config config = iConfigService.queryById(id);
        return RespVOBuilder.success(config);
    }

}
