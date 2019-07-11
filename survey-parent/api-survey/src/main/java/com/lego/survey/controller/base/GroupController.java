package com.lego.survey.controller.base;
import com.lego.survey.project.feign.GroupClient;
import com.lego.survey.project.model.entity.Group;
import com.lego.survey.project.model.vo.GroupVo;
import com.lego.survey.lib.swagger.ApiError;
import com.survey.lib.common.consts.DictConstant;
import com.survey.lib.common.vo.RespDataVO;
import com.survey.lib.common.vo.RespVO;
import io.swagger.annotations.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @author yanglf
 */
@RestController
@RequestMapping(DictConstant.Path.GROUP)
@Api(value = "GroupController", description = "单位管理服务")
@ApiResponses({
        @ApiResponse(response = ApiError.class,code = 404,message = "Resources Not Found")
})
public class GroupController {

    @Autowired
    GroupClient groupClient;

    @ApiOperation(value = "添加单位信息", notes = "添加单位信息", httpMethod = "POST")
    @ApiImplicitParams({

    })
    @RequestMapping(value = "/create", method = RequestMethod.POST)
    public RespVO add(@RequestBody Group group) {
        // 新增项目工程
        return groupClient.create(group);
    }

    @ApiOperation(value = "修改单位信息", notes = "修改单位信息", httpMethod = "PUT")
    @ApiImplicitParams({

    })
    @RequestMapping(value = "/modify", method = RequestMethod.PUT)
    public RespVO modify(@RequestBody Group group) {
        return groupClient.modify(group);
    }


    @ApiOperation(value = "查询单位信息", notes = "查询单位信息", httpMethod = "GET")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "单位id", dataType = "String", required = true, paramType = "query"),
    })
    @RequestMapping(value = "/query", method = RequestMethod.GET)
    public RespVO<GroupVo> query(@RequestParam String id) {
        return groupClient.query(id);
    }

    @ApiOperation(value = "删除单位信息", notes = "删除单位信息", httpMethod = "DELETE")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "单位id", dataType = "String", required = true, paramType = "query"),
    })
    @RequestMapping(value = "/delete", method = RequestMethod.DELETE)
    public RespVO delete(@RequestParam String id) {
        return groupClient.delete(id);
    }

    @ApiOperation(value = "列举所有单位信息", notes = "列举所有单位信息", httpMethod = "GET")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "pageIndex", value = "当前页", dataType = "int", required = true,example = "1", paramType = "path"),
            @ApiImplicitParam(name = "pageSize", value = "每页大小", dataType = "int",defaultValue = "10",example = "10",paramType = "query"),
    })
    @RequestMapping(value = "/list/{pageIndex}", method = RequestMethod.GET)
    public RespVO<RespDataVO<GroupVo>> list(@PathVariable(value = "pageIndex") int  pageIndex,
                                            @RequestParam(required = false,defaultValue = "10") int pageSize) {
        return groupClient.list(pageIndex,pageSize);
    }

    @ApiOperation(value = "查询母公司", notes = "查询母公司", httpMethod = "GET")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "单位id", dataType = "String", required = true, paramType = "query"),
    })
    @RequestMapping(value = "/queryFull", method = RequestMethod.GET)
    public RespVO<RespDataVO<GroupVo>> queryFullGroup(@RequestParam String id) {
        return groupClient.queryFullGroup(id);
    }
}
