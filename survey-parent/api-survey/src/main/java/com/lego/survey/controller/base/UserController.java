package com.lego.survey.controller.base;
import com.lego.survey.base.exception.ExceptionBuilder;
import com.lego.survey.user.feign.UserClient;
import com.lego.survey.user.model.entity.User;
import com.lego.survey.user.model.vo.UserVo;
import com.lego.survey.lib.swagger.ApiError;
import com.survey.lib.common.consts.DictConstant;
import com.survey.lib.common.consts.RespConsts;
import com.survey.lib.common.vo.*;
import io.swagger.annotations.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import javax.servlet.http.HttpServletRequest;
/**
 * @author yanglf
 * @description
 * @since 2018/12/21
 **/

@RestController
@RequestMapping(DictConstant.Path.USER)
@Api(value = "UserController", description = "用户接口")
@ApiResponses({
        @ApiResponse(response = ApiError.class, code = 404, message = "Resources Not Found")
})
public class UserController {
    @Autowired
    private UserClient userClient;

    @ApiOperation(value = "用户登录", httpMethod = "POST", notes = "用户登录")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "user", value = "用户名", dataType = "String", required = true, paramType = "query"),
            @ApiImplicitParam(name = "pwd", value = "密码", dataType = "String", required = true, paramType = "query"),
    })
    @RequestMapping(value = {"/login"}, method = RequestMethod.POST)
    public RespVO<TokenVo> login(
            @RequestParam String user,
             @RequestParam String pwd) {
        // 参数校验
        if(StringUtils.isEmpty(user)){
            return RespVOBuilder.failure(RespConsts.FAIL_RESULT_CODE,"用户名不能为空");
        }
        if(StringUtils.isEmpty(pwd)){
            return RespVOBuilder.failure(RespConsts.FAIL_RESULT_CODE,"用户名不能为空");
        }
        return userClient.login(user, pwd);
    }


    @ApiOperation(value = "用户注销", httpMethod = "POST", notes = "用户注销")
    @ApiImplicitParams({

    })
    @RequestMapping(value = {"/logout"}, method = RequestMethod.POST)
    public RespVO logout(
            HttpServletRequest request) {
        // 参数校验
        AuthVo authVo = (AuthVo) request.getAttribute("authVo");
        if (authVo == null) {
            return RespVOBuilder.success();
        }
        CurrentVo currentVo = authVo.getCurrentVo();
        if(currentVo==null){
            return RespVOBuilder.success();
        }
        return userClient.logout(currentVo.getUserId());
    }


    @ApiOperation(value = "创建用户", httpMethod = "POST", notes = "创建用户")
    @ApiImplicitParams({

    })
    @RequestMapping(value = "/create", method = RequestMethod.POST)
    public RespVO create(@RequestBody User user,
                         HttpServletRequest request) {
        CurrentVo currentVo = (CurrentVo) request.getAttribute("currentVo");
        // 权限校验
        String uri = request.getRequestURI();
        if(uri.endsWith(DictConstant.RolePath.USER_CREATE)){
            String role = user.getRole();
            String voRole = currentVo.getRole();
            if(role.equals(DictConstant.Role.MASTER) || role.equals(DictConstant.Role.SECTION)){
                if(!voRole.equals(DictConstant.Role.ADMIN)){
                    ExceptionBuilder.unAuthorizationException();
                }
            }
            if(role.equals(DictConstant.Role.SURVEYER)){
                if(!voRole.equals(DictConstant.Role.SECTION)){
                    ExceptionBuilder.unAuthorizationException();
                }
            }
        }
        return userClient.create(user);
    }


    @ApiOperation(value = "获取用户信息", httpMethod = "GET", notes = "获取用户信息")
    @ApiImplicitParams({

    })
    @RequestMapping(value = "/info", method = RequestMethod.GET)
    public RespVO<UserVo> info(HttpServletRequest request) {
        CurrentVo currentVo = (CurrentVo) request.getAttribute("currentVo");
        if (currentVo == null) {
            ExceptionBuilder.sessionTimeoutException();
        }
        return userClient.queryUserById(currentVo.getUserId());
    }

    @ApiOperation(value = "修改用户信息", httpMethod = "PUT", notes = "修改用户信息")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "userName", value = "用户名", dataType = "String", required = true, paramType = "query"),
            @ApiImplicitParam(name = "name", value = "昵称", dataType = "String", required = true, paramType = "query"),
            @ApiImplicitParam(name = "cardId", value = "身份证号", dataType = "String", required = true, paramType = "query"),
    })
    @RequestMapping(value = "/modify", method = RequestMethod.PUT)
    public RespVO modify(HttpServletRequest request,
                         @RequestParam String userName,
                         @RequestParam String name,
                         @RequestParam String cardId) {
        // 参数校验
        CurrentVo currentVo = (CurrentVo) request.getAttribute("currentVo");
        if (currentVo == null) {
            ExceptionBuilder.sessionTimeoutException();
        }
        return userClient.modify(currentVo.getUserId(), userName, name, cardId);
    }


    @ApiOperation(value = "修改手机号", httpMethod = "PUT", notes = "修改手机号")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "phone", value = "旧手机号", dataType = "String", required = true, paramType = "query"),
            @ApiImplicitParam(name = "activeCode", value = "旧手机号验证码", dataType = "String", required = true, paramType = "query"),
            @ApiImplicitParam(name = "newPhone", value = "新手机号", dataType = "String", required = true, paramType = "query"),
            @ApiImplicitParam(name = "newActiveCode", value = "新手机号验证码", dataType = "String", required = true, paramType = "query"),
    })
    @RequestMapping(value = "/phone/modify", method = RequestMethod.PUT)
    public RespVO modifyPhone(HttpServletRequest request,
                              @RequestParam String phone,
                              @RequestParam String activeCode,
                              @RequestParam String newPhone,
                              @RequestParam String newActiveCode) {
        // 参数校验
        CurrentVo currentVo = (CurrentVo) request.getAttribute("currentVo");
        if (currentVo == null) {
            ExceptionBuilder.sessionTimeoutException();
        }
        return userClient.modifyPhone(currentVo.getUserId(), phone, activeCode, newPhone, newActiveCode);
    }


    @ApiOperation(value = "修改密码", httpMethod = "PUT", notes = "修改密码")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "phone", value = "手机号", dataType = "String", required = true, paramType = "query"),
            @ApiImplicitParam(name = "activeCode", value = "手机号验证码", dataType = "String", required = true, paramType = "query"),
            @ApiImplicitParam(name = "password", value = "密码", dataType = "String", required = true, paramType = "query"),
    })
    @RequestMapping(value = "/password/modify", method = RequestMethod.PUT)
    public RespVO modifyPassword(HttpServletRequest request,
                                 @RequestParam String phone,
                                 @RequestParam String activeCode,
                                 @RequestParam String password) {
        // 参数校验
        AuthVo authVo = (AuthVo) request.getAttribute("authVo");
        if (authVo == null) {
            ExceptionBuilder.sessionTimeoutException();
        }
        return userClient.modifyPassword(phone, activeCode, password);
    }


    @ApiOperation(value = "删除用户", httpMethod = "DELETE", notes = "删除用户")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "deleteId", value = "需要删除用户的id", dataType = "String", required = true, paramType = "query"),
    })
    @RequestMapping(value = "/delete", method = RequestMethod.DELETE)
    public RespVO modifyPassword(HttpServletRequest request,
                                 @RequestParam String deleteId) {
        return userClient.delete(deleteId);
    }


    @ApiOperation(value = "获取用户列表", httpMethod = "GET", notes = "获取用户列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "pageIndex", value = "当前页", dataType = "int", required = true,example = "1", paramType = "path"),
            @ApiImplicitParam(name = "pageSize", value = "每页大小", dataType = "int",defaultValue = "10",example = "10",paramType = "query"),
    })
    @RequestMapping(value = "/list/{pageIndex}", method = RequestMethod.GET)
    public RespVO<RespDataVO<UserVo>> list(HttpServletRequest request,
                                           @PathVariable(value = "pageIndex") int pageIndex,
                                           @RequestParam(required = false,defaultValue = "10") int pageSize) {
        return userClient.list(pageIndex,pageSize);
    }


}
