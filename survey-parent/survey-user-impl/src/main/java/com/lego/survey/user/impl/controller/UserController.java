package com.lego.survey.user.impl.controller;
import com.lego.survey.auth.feign.AuthClient;
import com.lego.survey.base.annotation.Operation;
import com.lego.survey.base.annotation.Resource;
import com.lego.survey.event.user.LogSender;
import com.lego.survey.user.impl.service.IUserService;
import com.lego.survey.user.model.entity.OwnProject;
import com.lego.survey.user.model.entity.OwnSection;
import com.lego.survey.user.model.entity.User;
import com.lego.survey.user.model.vo.UserAddVo;
import com.lego.survey.user.model.vo.UserVo;
import com.survey.lib.common.consts.DictConstant;
import com.survey.lib.common.consts.RespConsts;
import com.survey.lib.common.page.PagedResult;
import com.survey.lib.common.utils.HeaderUtils;
import com.survey.lib.common.utils.HttpUtils;
import com.survey.lib.common.utils.UuidUtils;
import com.survey.lib.common.vo.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import javax.servlet.http.HttpServletRequest;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.List;

/**
 * @author yanglf
 * @descript
 * @since 2018/12/17
 **/
@Validated
@RestController
@RequestMapping(DictConstant.Path.USER)
@Api(value = "UserController", description = "用户管理")
@Resource(value = "user",desc = "用户管理")
public class UserController {

    @Autowired
    private AuthClient authClient;

    @Autowired
    private IUserService iUserService;

    @Autowired
    private LogSender logSender;

    @ApiOperation(value = "用户登录", httpMethod = "POST", notes = "用户登录")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "user", value = "用户名", dataType = "String", required = true, paramType = "query"),
            @ApiImplicitParam(name = "pwd", value = "密码", dataType = "String", required = true, paramType = "query"),
    })
    @RequestMapping(value = {"/login"}, method = RequestMethod.POST)
    public RespVO<TokenVo> login(HttpServletRequest request,
                                 @NotBlank(message = "用户名不能为空") @RequestParam String user,
                                 @NotBlank(message = "密码不能为空") @Size(min = 6, max = 32, message = "密码长度为6-23位") @RequestParam String pwd) {
        HeaderVo headerVo = HeaderUtils.parseHeader(request);
        String deviceType = headerVo.getDeviceType();
        //  校验参数
        // 验证用户正确性
        User userObj = iUserService.queryByUserNameOrPhone(user);
        if (userObj == null) {
            return RespVOBuilder.failure(RespConsts.FAIL_LOGIN_CODE, "用户不存在");
        }
        String password = userObj.getPassWord();
       // String withMd5 = SecurityUtils.encryptionWithMd5(pwd);
        if (!pwd.equals(password)) {
            return RespVOBuilder.failure(RespConsts.FAIL_LOGIN_CODE, "密码输入错误");
        }
        // 查询登录状态
        String loginToken = authClient.loginToken(userObj.getId(), deviceType);
        if (loginToken != null) {
            authClient.delete(loginToken, deviceType);
        }
        String role = userObj.getRole();
        List<String> permission = userObj.getPermission();
        //  登录逻辑
        RespVO<TokenVo> respVO = authClient.generate(userObj, deviceType);
        logSender.sendLogEvent(HttpUtils.getClientIp(request), userObj.getId(), "用户登录");
        return respVO;
    }


    @ApiOperation(value = "用户退出登录", httpMethod = "POST", notes = "用户退出登录")
    @ApiImplicitParams({

    })
    @RequestMapping(value = {"/logout"}, method = RequestMethod.POST)
    public RespVO<TokenVo> logout(HttpServletRequest request) {
        HeaderVo headerVo = HeaderUtils.parseHeader(request);
        String userId =  request.getHeader("userId");
        String token = headerVo.getToken();
        String deviceType = headerVo.getDeviceType();
        // 验证用户正确性
        RespVO<UserVo> user = iUserService.queryUserById(userId);
        // 发布退出登录事件
        logSender.sendLogEvent(HttpUtils.getClientIp(request), userId, "用户退出登录");
        return authClient.delete(token, deviceType);
    }


    @ApiOperation(value = "修改用户信息", httpMethod = "POST", notes = "修改用户信息")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "userName", value = "用户名", dataType = "String", required = true, paramType = "query"),
            @ApiImplicitParam(name = "name", value = "昵称", dataType = "String", required = true, paramType = "query"),
            @ApiImplicitParam(name = "cardId", value = "身份证号", dataType = "String", required = true, paramType = "query"),
    })
    @Operation(value = "modify",desc = "修改用户信息")
    @RequestMapping(value = "/modify", method = RequestMethod.PUT)
    public RespVO modify(@RequestParam("userId") String userId,
                         @RequestParam("userName") String userName,
                         @RequestParam("name") String name,
                         @RequestParam("cardId") String cardId,
                         HttpServletRequest request) {
        HeaderVo headerVo = HeaderUtils.parseHeader(request);
        String id = authClient.getAuthVo(headerVo).getUserId();
        //修改用户信息
        RespVO modify = iUserService.modify(userId, userName, name, cardId);
        logSender.sendLogEvent(HttpUtils.getClientIp(request), id, "修改用户信息:[" + userId + "]");
        return modify;
    }


    @ApiOperation(value = "删除用户", httpMethod = "DELETE", notes = "删除用户")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "userId", value = "删除用户的id", dataType = "String", required = true, paramType = "query"),
    })
    @Operation(value = "delete",desc = "删除用户")
    @RequestMapping(value = "/delete", method = RequestMethod.DELETE)
    public RespVO delete(@RequestParam("userId") String userId,
                         HttpServletRequest request) {
        HeaderVo headerVo = HeaderUtils.parseHeader(request);
        String id = authClient.getAuthVo(headerVo).getUserId();
        //删除用户
        RespVO delete = iUserService.delete(userId);
        //更新日志
        logSender.sendLogEvent(HttpUtils.getClientIp(request), id, "删除用户:[" + userId + "]");
        return delete;
    }


    @ApiOperation(value = "修改手机号", httpMethod = "POST", notes = "修改手机号")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "userId", value = "用户名id", dataType = "String", required = true, paramType = "query"),
            @ApiImplicitParam(name = "phone", value = "手机号", dataType = "String", required = true, paramType = "query"),
            @ApiImplicitParam(name = "activeCode", value = "旧手机号验证码", dataType = "String", required = true, paramType = "query"),
            @ApiImplicitParam(name = "newPhone", value = "新手机号", dataType = "String", required = true, paramType = "query"),
            @ApiImplicitParam(name = "newActiveCode", value = "新手机号验证码", dataType = "String", required = true, paramType = "query"),

    })
    @RequestMapping(value = "/phone/modify", method = RequestMethod.PUT)
    public RespVO modifyPhone(@RequestParam("userId") String userId,
                              @RequestParam("phone") String phone,
                              @RequestParam("activeCode") String activeCode,
                              @RequestParam("newPhone") String newPhone,
                              @RequestParam("newActiveCode") String newActiveCode,
                              HttpServletRequest request) {
        HeaderVo headerVo = HeaderUtils.parseHeader(request);
        String id = authClient.getAuthVo(headerVo).getUserId();
        //修改用户信息
        RespVO respVO = iUserService.modifyPhone(userId, phone, activeCode, newPhone, newActiveCode);
        logSender.sendLogEvent(HttpUtils.getClientIp(request), id, "修改手机号");
        return respVO;
    }


    @ApiOperation(value = "修改密码", httpMethod = "POST", notes = "修改密码")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "phone", value = "手机号", dataType = "String", required = true, paramType = "query"),
            @ApiImplicitParam(name = "activeCode", value = "手机号验证码", dataType = "String", required = true, paramType = "query"),
            @ApiImplicitParam(name = "password", value = "新密码", dataType = "String", required = true, paramType = "query"),

    })
    @RequestMapping(value = "/password/modify", method = RequestMethod.PUT)
    public RespVO modifyPassword(@RequestParam("phone") String phone,
                                 @RequestParam("activeCode") String activeCode,
                                 @RequestParam("password") String password,
                                 HttpServletRequest request) {
        HeaderVo headerVo = HeaderUtils.parseHeader(request);
        String id = authClient.getAuthVo(headerVo).getUserId();
        RespVO respVO = iUserService.modifyPassword(id, phone, activeCode, password);
        logSender.sendLogEvent(HttpUtils.getClientIp(request), id, "修改密码");
        return respVO;
    }


    @ApiOperation(value = "新增用户", httpMethod = "POST", notes = "新增用户")
    @ApiImplicitParams({

    })
    @Operation(value = "create",desc = "新增用户")
    @RequestMapping(value = "/create", method = RequestMethod.POST)
    public RespVO create(@Validated @RequestBody UserAddVo userAddVo, HttpServletRequest request) {
        HeaderVo headerVo = HeaderUtils.parseHeader(request);
        CurrentVo currentVo = authClient.getAuthVo(headerVo);
        String userId = currentVo.getUserId();
        String userRole = currentVo.getRole();
        String role = userAddVo.getRole();
        if(!userRole.equals("admin") && !userRole.equals("master")){
            return RespVOBuilder.failure(RespConsts.FAIL_NOPRESSION_CODE,RespConsts.FAIL_NOPRESSION_MSG);
        }
        if(userRole.equals("admin")){
            if(!role.equals("surveyer") && ! role.equals("companyAdmin")){
                return RespVOBuilder.failure(RespConsts.FAIL_NOPRESSION_CODE,RespConsts.FAIL_NOPRESSION_MSG);
            }
        }else {
            if(!role.equals("surveyer")){
                return RespVOBuilder.failure(RespConsts.FAIL_NOPRESSION_CODE,RespConsts.FAIL_NOPRESSION_MSG);
            }
        }
        String phone = userAddVo.getPhone();
        User phoneUser = iUserService.queryByPhone(phone);
        if (phoneUser != null) {
            return RespVOBuilder.failure(RespConsts.FAIL_RESULT_CODE, "手机号已被添加");
        }
        String userName = userAddVo.getUserName();
        User queryByUserName = iUserService.queryByUserName(userName);
        if (queryByUserName != null) {
            return RespVOBuilder.failure(RespConsts.FAIL_RESULT_CODE, "用户名已被添加");
        }
        //根据用户信息生成用户 token
        userAddVo.setId(UuidUtils.generateShortUuid());

        RespVO respVO = iUserService.create(userAddVo);
        if(respVO.getRetCode()==RespConsts.SUCCESS_RESULT_CODE){
            String group = userAddVo.getGroup();
            List<String> projects = userAddVo.getProject();
            //TODO 更新 工程  标段  人员信息
            String userRole1 = userAddVo.getRole();

            logSender.sendLogEvent(HttpUtils.getClientIp(request), userId, "新增用户:[" + userAddVo.getId() + "]");
        }
        return respVO;
    }


    @ApiOperation(value = "根据用户id获取用户信息", httpMethod = "GET", notes = "根据用户id获取用户信息")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "用户id", dataType = "String", required = true, paramType = "query"),
    })
    @RequestMapping(value = "/queryById", method = RequestMethod.GET)
    public RespVO<UserVo> queryUserById(@RequestParam("id") String id, HttpServletRequest request) {
        return iUserService.queryUserById(id);
    }


    @ApiOperation(value = "根据token获取用户信息", httpMethod = "GET", notes = "根据token获取用户信息")
    @ApiImplicitParams({

    })
    @RequestMapping(value = "/queryByToken", method = RequestMethod.GET)
    public RespVO<UserVo> queryUserByToken(HttpServletRequest request) {
        // 验证用户正确性
        HeaderVo headerVo = HeaderUtils.parseHeader(request);
        String userId = authClient.getAuthVo(headerVo).getUserId();
        return iUserService.queryUserById(userId);
    }


    @ApiOperation(value = "获取所有用户", httpMethod = "GET", notes = "获取所有用户")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "projectId", value = "项目ID", dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "sectionId", value = "标段ID", dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "role", value = "角色", dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "groupId", value = "单位ID", dataType = "String",  paramType = "query"),
            @ApiImplicitParam(name = "pageIndex", value = "当前页", dataType = "int", required = true, example = "1", paramType = "path"),
            @ApiImplicitParam(name = "pageSize", value = "每页大小", dataType = "int", defaultValue = "10", example = "10", paramType = "query"),
    })
    @RequestMapping(value = "/list/{pageIndex}", method = RequestMethod.GET)
    public RespVO<PagedResult<UserAddVo>> list(@PathVariable(value = "pageIndex") int pageIndex,
                                    @RequestParam(required = false, defaultValue = "10") int pageSize,
                                    @RequestParam(required = false) String  projectId,
                                    @RequestParam(required = false) String sectionId,
                                    @RequestParam(required = false) String role,
                                    @RequestParam(required = false) String groupId,
                                    HttpServletRequest request) {
        HeaderVo headerVo = HeaderUtils.parseHeader(request);
        CurrentVo currentVo = authClient.getAuthVo(headerVo);
        PagedResult<UserAddVo> pagedResult = iUserService.queryList(pageIndex, pageSize,projectId,sectionId,role,groupId);
        return RespVOBuilder.success(pagedResult);
    }


    @RequestMapping(value = "/findByUserId", method = RequestMethod.GET)
    public  User findByUserId(@RequestParam String userId){
        return iUserService.findByUserId(userId);
    }


}
