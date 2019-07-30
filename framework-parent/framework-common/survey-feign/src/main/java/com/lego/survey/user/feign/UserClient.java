package com.lego.survey.user.feign;

import com.lego.survey.user.model.entity.User;
import com.lego.survey.user.model.vo.UserVo;
import com.survey.lib.common.consts.DictConstant;
import com.survey.lib.common.consts.RespConsts;
import com.survey.lib.common.vo.RespDataVO;
import com.survey.lib.common.vo.RespVO;
import com.survey.lib.common.vo.RespVOBuilder;
import com.survey.lib.common.vo.TokenVo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.*;

/**
 * @author yanglf
 * @descript
 * @since 2018/12/18
 **/
@FeignClient(value = DictConstant.Service.USER, path = DictConstant.Path.USER, fallback = UserClientFallback.class)
public interface UserClient {


    /**
     * 登录
     *
     * @param user
     * @param pwd
     * @return
     */
    @RequestMapping(value = "/login", method = RequestMethod.POST)
    RespVO<TokenVo> login(@RequestParam("user") String user, @RequestParam("pwd") String pwd);


    /**
     * 用户注销
     *
     * @param userId
     * @return
     */
    @RequestMapping(value = {"/logout"}, method = RequestMethod.POST)
    RespVO<TokenVo> logout(@RequestParam("userId") String userId);


    /**
     * 注册
     *
     * @return
     */
    @RequestMapping(value = "/create", method = RequestMethod.POST)
    RespVO create(@RequestBody User user);


    /**
     * 删除用户
     *
     * @param userId 需要删除用户的id
     * @return
     */
    @RequestMapping(value = "/delete", method = RequestMethod.DELETE)
    RespVO delete(@RequestParam("userId") String userId);


    /**
     * 根据id获取用户信息
     *
     * @param id
     * @return
     */
    @RequestMapping(value = "/queryById", method = RequestMethod.GET)
    RespVO<UserVo> queryUserById(@RequestParam("id") String id);


    /**
     * 根据 token 获取用户信息
     *
     * @param token
     * @return
     */
    @RequestMapping(value = "/queryByToken", method = RequestMethod.GET)
    RespVO<UserVo> queryUserByToken(@RequestParam("token") String token);


    /**
     * 修改用户信息
     *
     * @param userId
     * @param userName
     * @param name
     * @param cardId
     * @return
     */
    @RequestMapping(value = "/modify", method = RequestMethod.PUT)
    RespVO modify(@RequestParam("userId") String userId,
                  @RequestParam("userName") String userName,
                  @RequestParam("name") String name,
                  @RequestParam("cardId") String cardId);


    /**
     * 修改手机号
     *
     * @param userId
     * @param phone
     * @param activeCode
     * @param newPhone
     * @param newActiveCode
     * @return
     */
    @RequestMapping(value = "/phone/modify", method = RequestMethod.PUT)
    RespVO modifyPhone(@RequestParam("userId") String userId,
                       @RequestParam("phone") String phone,
                       @RequestParam("activeCode") String activeCode,
                       @RequestParam("newPhone") String newPhone,
                       @RequestParam("newActiveCode") String newActiveCode);


    /**
     * 修改密码
     *
     * @param phone
     * @param activeCode
     * @param password
     * @return
     */
    @RequestMapping(value = "/password/modify", method = RequestMethod.PUT)
    RespVO modifyPassword(@RequestParam("phone") String phone,
                          @RequestParam("activeCode") String activeCode,
                          @RequestParam("password") String password);


    /**
     * 查询用户列表信息
     *
     * @param pageIndex
     * @param pageSize
     * @return
     */
    @RequestMapping(value = "/list/{pageIndex}", method = RequestMethod.GET)
    RespVO<RespDataVO<UserVo>> list(@PathVariable(value = "pageIndex") int pageIndex,
                                    @RequestParam("pageSize") int pageSize);

    /**
     * 根据ID 获取用户信息
     *
     * @param userId
     * @return
     */
    @RequestMapping(value = "/findByUserId", method = RequestMethod.GET)
    User findByUserId(@RequestParam(value = "userId") String userId);

    /**
     * @param userName
     * @return
     */
    @RequestMapping(value = "/queryByUserName", method = RequestMethod.GET)
    User queryByUserName(@RequestParam(value = "userName") String userName);
}

@Component
class UserClientFallback implements UserClient {

    @Override
    public RespVO<TokenVo> login(String user, String pwd) {
        return RespVOBuilder.failure(RespConsts.ERROR_SERVER_CODE, "user服务不可用");
    }

    @Override
    public RespVO<TokenVo> logout(String userId) {
        return RespVOBuilder.failure(RespConsts.ERROR_SERVER_CODE, "user服务不可用");
    }

    @Override
    public RespVO create(User user) {
        return RespVOBuilder.failure(RespConsts.ERROR_SERVER_CODE, "user服务不可用");
    }

    @Override
    public RespVO delete(String userId) {
        return RespVOBuilder.failure(RespConsts.ERROR_SERVER_CODE, "user服务不可用");
    }

    @Override
    public RespVO<UserVo> queryUserById(String id) {
        return RespVOBuilder.failure(RespConsts.ERROR_SERVER_CODE, "user服务不可用");
    }

    @Override
    public RespVO<UserVo> queryUserByToken(String token) {
        return RespVOBuilder.failure(RespConsts.ERROR_SERVER_CODE, "user服务不可用");
    }

    @Override
    public RespVO modify(String userId, String userName, String name, String cardId) {
        return RespVOBuilder.failure(RespConsts.ERROR_SERVER_CODE, "user服务不可用");
    }

    @Override
    public RespVO modifyPhone(String userId, String phone, String activeCode, String newPhone, String newActiveCode) {
        return RespVOBuilder.failure(RespConsts.ERROR_SERVER_CODE, "user服务不可用");
    }

    @Override
    public RespVO modifyPassword(String phone, String activeCode, String password) {
        return RespVOBuilder.failure(RespConsts.ERROR_SERVER_CODE, "user服务不可用");
    }

    @Override
    public RespVO<RespDataVO<UserVo>> list(int pageIndex, int pageSize) {
        return RespVOBuilder.failure(RespConsts.ERROR_SERVER_CODE, "user服务不可用");
    }

    @Override
    public User findByUserId(String userId) {
        return null;
    }

    @Override
    public User queryByUserName(String userName) {
        return null;
    }
}
