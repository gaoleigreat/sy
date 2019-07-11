package com.lego.survey.controller.base;
import com.lego.survey.base.exception.ExceptionBuilder;
import com.lego.survey.project.feign.WorkspaceClient;
import com.lego.survey.project.model.entity.Workspace;
import com.lego.survey.lib.swagger.ApiError;
import com.survey.lib.common.consts.DictConstant;
import com.survey.lib.common.vo.AuthVo;
import com.survey.lib.common.vo.RespDataVO;
import com.survey.lib.common.vo.RespVO;
import io.swagger.annotations.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

/**
 * @author yanglf
 * @description   Workspace Controller
 * @since 2018/12/28
 **/
@RestController
@RequestMapping(DictConstant.Path.WORKSPACE)
@Api(value = "WorkspaceController",description = "工区接口")
@ApiResponses({
        @ApiResponse(response = ApiError.class,code = 404,message = "Resources Not Found")
})
public class WorkspaceController {

    @Autowired
    WorkspaceClient workspaceClient;

    @ApiOperation(value = "新增工区信息",httpMethod = "POST",notes = "新增工区信息")
    @ApiImplicitParams({

    })
    @RequestMapping(value = "/create",method = RequestMethod.POST)
    public RespVO add(@RequestBody Workspace workspace, HttpServletRequest request){
        AuthVo authVo = (AuthVo) request.getAttribute("authVo");
        if(authVo==null){
            ExceptionBuilder.sessionTimeoutException();
        }
        return workspaceClient.create(workspace);
    }

    @ApiOperation(value = "修改工区信息",httpMethod = "PUT",notes = "修改工区信息")
    @ApiImplicitParams({

    })
    @RequestMapping(value = "/modify",method = RequestMethod.PUT)
    public RespVO modify(@RequestBody Workspace workspace, HttpServletRequest request){
        AuthVo authVo = (AuthVo) request.getAttribute("authVo");
        if(authVo==null){
            ExceptionBuilder.sessionTimeoutException();
        }
        return workspaceClient.modify(workspace);
    }


    @ApiOperation(value = "删除工区信息",httpMethod = "DELETE",notes = "删除工区信息")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "workspaceId", value = "工区ID", dataType = "String",required = true,paramType = "query"),
    })
    @RequestMapping(value = "/delete",method = RequestMethod.DELETE)
    public RespVO delete(String workspaceId, HttpServletRequest request){
        AuthVo authVo = (AuthVo) request.getAttribute("authVo");
        if(authVo==null){
            ExceptionBuilder.sessionTimeoutException();
        }
        return workspaceClient.delete(workspaceId);
    }



    @ApiOperation(value = "工区信息查询",httpMethod = "GET",notes = "工区信息查询")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "标段ID", dataType = "String",required = true,paramType = "path"),
    })
    @RequestMapping(value = "/info/{id}",method = RequestMethod.GET)
    public RespVO<Workspace> info(@PathVariable String id,
                                              HttpServletRequest request){
        AuthVo authVo = (AuthVo) request.getAttribute("authVo");
        if(authVo==null){
            ExceptionBuilder.sessionTimeoutException();
        }
        return workspaceClient.info(id);
    }



    @ApiOperation(value = "工区列表信息查询",httpMethod = "GET",notes = "工区列表信息查询")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "projectId", value = "工程ID", dataType = "String",required = true,paramType = "query"),
            @ApiImplicitParam(name = "sectionId", value = "标段ID", dataType = "String",required = true,paramType = "query"),
    })
    @RequestMapping(value = "/list",method = RequestMethod.GET)
    public RespVO<RespDataVO<Workspace>> list(@RequestParam String projectId,
                                              @RequestParam String sectionId,
                                              HttpServletRequest request){
        AuthVo authVo = (AuthVo) request.getAttribute("authVo");
        if(authVo==null){
            ExceptionBuilder.sessionTimeoutException();
        }
        return workspaceClient.list(projectId,sectionId);
    }

}
