package com.lego.survey.controller.base;
import com.lego.survey.base.exception.ExceptionBuilder;
import com.lego.survey.project.feign.ProjectClient;
import com.lego.survey.project.model.entity.Project;
import com.lego.survey.project.model.vo.ProjectVo;
import com.lego.survey.lib.swagger.ApiError;
import com.survey.lib.common.consts.DictConstant;
import com.survey.lib.common.vo.AuthVo;
import com.survey.lib.common.vo.CurrentVo;
import com.survey.lib.common.vo.RespDataVO;
import com.survey.lib.common.vo.RespVO;
import io.swagger.annotations.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import javax.servlet.http.HttpServletRequest;

/**
 * @author yanglf
 * @description
 * @since 2018/12/21
 **/
@RestController
@RequestMapping(DictConstant.Path.PROJECT)
@Api(value = "ProjectController",description = "项目工程接口")
@ApiResponses({
        @ApiResponse(response = ApiError.class,code = 404,message = "Resources Not Found")
})
public class ProjectController {

    @Autowired
    private ProjectClient projectClient;


    @ApiOperation(value = "添加项目工程",httpMethod = "POST",notes = "添加项目工程")
    @ApiImplicitParams({

    })
    @RequestMapping(value = "/create",method = RequestMethod.POST)
    public RespVO add(@RequestBody Project project, HttpServletRequest request){
        AuthVo authVo = (AuthVo) request.getAttribute("authVo");
        if(authVo==null){
            ExceptionBuilder.sessionTimeoutException();
        }
        return projectClient.create(project);
    }


    @ApiOperation(value = "修改项目工程",httpMethod = "PUT",notes = "修改项目工程")
    @ApiImplicitParams({

    })
    @RequestMapping(value = "/modify",method = RequestMethod.PUT)
    public RespVO modify(@RequestBody Project project,HttpServletRequest request){
        AuthVo authVo = (AuthVo) request.getAttribute("authVo");
        if(authVo==null){
            ExceptionBuilder.sessionTimeoutException();
        }
        return projectClient.modify(project);
    }


 /*   @ApiOperation(value = "获取工程信息",httpMethod = "GET",notes = "获取工程信息")
    @ApiImplicitParams({

    })
    @RequestMapping("/queryByUserId")
    public RespVO<RespDataVO<ProjectVo>> queryByUserId(HttpServletRequest request){
        AuthVo authVo = (AuthVo) request.getAttribute("authVo");
        if(authVo==null){
            ExceptionBuilder.sessionTimeoutException();
        }
        CurrentVo currentVo = authVo.getCurrentVo();
        if(currentVo==null){
            ExceptionBuilder.sessionTimeoutException();
        }
        return projectClient.queryByUserId(currentVo.getUserId());
    }*/


    @ApiOperation(value = "查询项目工程详细信息", notes = "查询项目工程详细信息", httpMethod = "GET")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "code", value = "项目工程id", dataType = "String", required = true, paramType = "query"),

    })
    @RequestMapping(value = "/info", method = RequestMethod.GET)
    public RespVO<ProjectVo> query(@RequestParam String code) {
        return projectClient.query(code);
    }


    @ApiOperation(value = "删除项目工程", notes = "删除项目工程", httpMethod = "DELETE")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "projectId", value = "项目工程id", dataType = "String", required = true, paramType = "query"),
    })
    @RequestMapping(value = "/delete", method = RequestMethod.DELETE)
    public RespVO delete(@RequestParam String projectId) {
        return projectClient.delete(projectId);
    }



    @ApiOperation(value = "查询项目工程列表", notes = "查询项目工程列表", httpMethod = "GET")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "pageIndex", value = "当前页", dataType = "int", required = true, example = "1",paramType = "path"),
            @ApiImplicitParam(name = "pageSize", value = "每页大小", dataType = "int",defaultValue = "10",example = "10", paramType = "query"),
    })
    @RequestMapping(value = "/list/{pageIndex}", method = RequestMethod.GET)
    public RespVO<RespDataVO<ProjectVo>> list(@PathVariable(value = "pageIndex") int  pageIndex,
                       @RequestParam(required = false,defaultValue = "10") int pageSize) {
        return projectClient.list(pageIndex,pageSize);
    }

}
