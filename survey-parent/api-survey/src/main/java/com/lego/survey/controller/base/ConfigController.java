package com.lego.survey.controller.base;
import com.lego.survey.base.exception.ExceptionBuilder;
import com.lego.survey.user.feign.ConfigClient;
import com.lego.survey.user.model.entity.Config;
import com.lego.survey.lib.swagger.ApiError;
import com.survey.lib.common.consts.DictConstant;
import com.survey.lib.common.vo.AuthVo;
import com.survey.lib.common.vo.RespVO;
import io.swagger.annotations.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

/**
 * @author yanglf
 * @description
 * @since 2019/1/3
 **/
@RestController
@RequestMapping(DictConstant.Path.CONFIG)
@Api(value = "ConfigController", description = "配置信息接口")
@ApiResponses({
        @ApiResponse(response = ApiError.class, code = 404, message = "Resources Not Found")
})
public class ConfigController {

    @Autowired
    private ConfigClient configClient;


    @ApiOperation(value = "添加配置信息",httpMethod = "POST",notes = "添加配置信息")
    @ApiImplicitParams({

    })
    @RequestMapping(value = "/create",method = RequestMethod.POST)
    public RespVO add(@RequestBody Config config, HttpServletRequest request){
        AuthVo authVo = (AuthVo) request.getAttribute("authVo");
        if(authVo==null){
            ExceptionBuilder.sessionTimeoutException();
        }
        return configClient.create(config);
    }

    @ApiOperation(value = "修改配置信息",httpMethod = "PUT",notes = "修改配置信息")
    @ApiImplicitParams({

    })
    @RequestMapping(value = "/modify",method = RequestMethod.PUT)
    public RespVO modify(@RequestBody Config config, HttpServletRequest request){
        AuthVo authVo = (AuthVo) request.getAttribute("authVo");
        if(authVo==null){
            ExceptionBuilder.sessionTimeoutException();
        }
        return configClient.modify(config);
    }


    @ApiOperation(value = "删除配置信息",httpMethod = "DELETE",notes = "删除配置信息")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "配置id", dataType = "String", required = true, paramType = "query"),
    })
    @RequestMapping(value = "/delete",method = RequestMethod.DELETE)
    public RespVO delete(String id, HttpServletRequest request){
        AuthVo authVo = (AuthVo) request.getAttribute("authVo");
        if(authVo==null){
            ExceptionBuilder.sessionTimeoutException();
        }
        return configClient.delete(id);
    }

    @ApiOperation(value = "查询配置信息",httpMethod = "GET",notes = "查询配置信息")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "配置id", dataType = "String", required = true, paramType = "query"),
    })
    @RequestMapping(value = "/info",method = RequestMethod.GET)
    public RespVO info(String id, HttpServletRequest request){
        AuthVo authVo = (AuthVo) request.getAttribute("authVo");
        if(authVo==null){
            ExceptionBuilder.sessionTimeoutException();
        }
        return configClient.info(id);
    }



    @ApiOperation(value = "查询配置信息",httpMethod = "GET",notes = "查询配置信息")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "pageIndex", value = "当前页", dataType = "int", required = true,example = "1", paramType = "path"),
            @ApiImplicitParam(name = "pageSize", value = "每页大小", dataType = "int",defaultValue = "10", example = "10",paramType = "query"),
    })
    @RequestMapping(value = "/list/{pageIndex}",method = RequestMethod.GET)
    public RespVO list(@PathVariable(value = "pageIndex") int pageIndex,
                       @RequestParam(required = false,defaultValue = "10") int pageSize,
                       HttpServletRequest request){
        AuthVo authVo = (AuthVo) request.getAttribute("authVo");
        if(authVo==null){
            ExceptionBuilder.sessionTimeoutException();
        }
        return configClient.list(pageIndex,pageSize);
    }


}
