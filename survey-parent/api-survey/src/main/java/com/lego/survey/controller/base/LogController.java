package com.lego.survey.controller.base;
import com.lego.survey.user.feign.LogClient;
import com.lego.survey.user.model.vo.LogVo;
import com.lego.survey.lib.swagger.ApiError;
import com.survey.lib.common.consts.DictConstant;
import com.survey.lib.common.vo.RespDataVO;
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
@RequestMapping(DictConstant.Path.LOG)
@Api(value = "UserController", description = "操作日志接口")
@ApiResponses({
        @ApiResponse(response = ApiError.class, code = 404, message = "Resources Not Found")
})
public class LogController {


    @Autowired
    private LogClient logClient;


    @ApiOperation(value = "获取操作日志列表", httpMethod = "GET", notes = "获取操作日志列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "pageIndex", value = "当前页", dataType = "int", required = true,example = "1", paramType = "path"),
            @ApiImplicitParam(name = "pageSize", value = "每页大小", dataType = "int",defaultValue = "10",example = "10",paramType = "query"),
    })
    @RequestMapping(value = "/list/{pageIndex}", method = RequestMethod.GET)
    public RespVO<RespDataVO<LogVo>> list(HttpServletRequest request,
                                          @PathVariable(value = "pageIndex") int pageIndex,
                                          @RequestParam(required = false,defaultValue = "10") int pageSize) {
        // 参数校验
        //用户权限校验
        return logClient.query(pageIndex,pageSize);
    }

}
