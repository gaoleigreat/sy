package com.lego.survey.controller.base;
import com.lego.survey.base.exception.ExceptionBuilder;
import com.lego.survey.project.feign.SectionClient;
import com.lego.survey.project.model.entity.Section;
import com.lego.survey.project.model.vo.SectionVo;
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
 * @description   Section  Controller
 * @since 2018/12/28
 **/
@RestController
@RequestMapping(DictConstant.Path.SECTION)
@Api(value = "SectionController",description = "标段接口")
@ApiResponses({
        @ApiResponse(response = ApiError.class,code = 404,message = "Resources Not Found")
})
public class SectionController {

    @Autowired
    private SectionClient sectionClient;


    @ApiOperation(value = "添加标段信息",httpMethod = "POST",notes = "添加标段信息")
    @ApiImplicitParams({

    })
    @RequestMapping(value = "/create",method = RequestMethod.POST)
    public RespVO add(@RequestBody Section section, HttpServletRequest request){
        AuthVo authVo = (AuthVo) request.getAttribute("authVo");
        if(authVo==null){
            ExceptionBuilder.sessionTimeoutException();
        }
        return sectionClient.create(section);
    }



    @ApiOperation(value = "修改标段信息",httpMethod = "PUT",notes = "修改标段信息")
    @ApiImplicitParams({

    })
    @RequestMapping(value = "/modify",method = RequestMethod.PUT)
    public RespVO modify(@RequestBody Section section, HttpServletRequest request){
        AuthVo authVo = (AuthVo) request.getAttribute("authVo");
        if(authVo==null){
            ExceptionBuilder.sessionTimeoutException();
        }
        return sectionClient.modify(section);
    }


    @ApiOperation(value = "删除标段信息",httpMethod = "DELETE",notes = "删除标段信息")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "标段id", dataType = "String", required = true, paramType = "query"),
    })
    @RequestMapping(value = "/delete",method = RequestMethod.DELETE)
    public RespVO delete(@RequestBody String id, HttpServletRequest request){
        AuthVo authVo = (AuthVo) request.getAttribute("authVo");
        if(authVo==null){
            ExceptionBuilder.sessionTimeoutException();
        }
        return sectionClient.delete(id);
    }


    @ApiOperation(value = "获取标段列表", httpMethod = "GET", notes = "获取标段列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "pageIndex", value = "当前页", dataType = "int", required = true,example = "1",paramType = "path"),
            @ApiImplicitParam(name = "pageSize", value = "每页大小", dataType = "int",defaultValue = "10",example = "10",paramType = "query"),
    })
    @RequestMapping(value = "/list/{pageIndex}", method = RequestMethod.GET)
    public RespVO<RespDataVO<SectionVo>> list(HttpServletRequest request,
                                              @PathVariable(value = "pageIndex") int pageIndex,
                                              @RequestParam(required = false,defaultValue = "10") int pageSize) {
        // 参数校验
        //用户权限校验
        return sectionClient.list(pageIndex,pageSize);
    }


}
