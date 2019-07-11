package com.lego.survey.auth.feign;
import com.lego.survey.user.model.entity.User;
import com.survey.lib.common.consts.DictConstant;
import com.survey.lib.common.consts.RespConsts;
import com.survey.lib.common.vo.*;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

/**
 * @author yanglf
 * @descript
 * @since 2018/12/20
 **/
@FeignClient(value = DictConstant.Service.AUTH, path = DictConstant.Path.AUTH,fallback = AuthClientFallback.class)
public interface AuthClient {

    /**
     * 生成登录 token
     *
     * @param user 用户
     * @return
     */
    @RequestMapping(value = {"/generate"}, method = RequestMethod.POST)
    RespVO<TokenVo> generate(@RequestBody User user,
                             @RequestParam("deviceType") String deviceType
    );


    /**
     * 解析 用户 token
     *
     * @param token    user token
     * @param  deviceType   deviceType
     * @return  user auth  vo
     */
    @RequestMapping(value = "/verify", method = RequestMethod.GET)
    RespVO<CurrentVo> parseUserToken(@RequestParam("token") String token, @RequestParam("deviceType") String deviceType);



    /**
     * app端删除用户 token
     *
     * @param token
     * @param deviceType
     * @return
     */
    @RequestMapping(value = "/delete", method = RequestMethod.POST)
    RespVO<TokenVo> delete(@RequestParam("token") String token,
                           @RequestParam("deviceType") String deviceType);


    /**
     * 获取用户登录token
     * @param userId
     * @param deviceType
     * @return
     */
    @RequestMapping(value = {"/loginToken"}, method = RequestMethod.GET)
    String loginToken(@RequestParam("userId") String userId,
                      @RequestParam("deviceType") String deviceType);


    /**
     * 根据header解析用户信息
     * @param headerVo
     * @return
     */
    @RequestMapping(value = "/getAuthVo",method = RequestMethod.POST)
    CurrentVo getAuthVo(@RequestBody HeaderVo headerVo);


    /**
     * 设置    session信息
     * @param userId
     * @param deviceType
     * @return
     */
    @RequestMapping(value = "/setAuthVo", method = RequestMethod.POST)
    RespVO setAuthVo(@RequestParam(value = "userId") String userId,@RequestParam(value = "deviceType") String deviceType);

}

@Component
class AuthClientFallback implements  AuthClient {

    @Override
    public RespVO<TokenVo> generate(User user, String deviceType) {
        return RespVOBuilder.failure(RespConsts.ERROR_SERVER_CODE,"auth服务不可用");
    }

    @Override
    public RespVO<CurrentVo> parseUserToken(String token, String deviceType) {
        return RespVOBuilder.failure(RespConsts.ERROR_SERVER_CODE,"auth服务不可用");
    }

    @Override
    public RespVO<TokenVo> delete(String token, String deviceType) {
        return RespVOBuilder.failure(RespConsts.ERROR_SERVER_CODE,"auth服务不可用");
    }

    @Override
    public String loginToken(String userId, String deviceType) {
        return null;
    }

    @Override
    public CurrentVo getAuthVo(HeaderVo headerVo) {
        return null;
    }

    @Override
    public RespVO setAuthVo(String userId, String deviceType) {
        return RespVOBuilder.failure(RespConsts.ERROR_SERVER_CODE,"auth服务不可用");
    }
}

