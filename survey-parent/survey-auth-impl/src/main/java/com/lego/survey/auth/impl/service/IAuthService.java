package com.lego.survey.auth.impl.service;

import com.lego.survey.user.model.entity.User;
import com.survey.lib.common.vo.AuthVo;
import com.survey.lib.common.vo.TokenVo;

/**
 * @author yanglf
 * @descript
 * @since 2018/12/20
 **/
public interface IAuthService {

    /**
     * 生成用户 token
     * @param user  用户信息
     * @return
     */
    TokenVo generateUserToken(User user, String deviceType);


    /**
     * 验证用户token
     * @param token  用户 token
     * @return
     */
    AuthVo verifyUserToken(String token, String deviceType);


    /**
     * 删除用户 token
     * @param userToken
     * @return
     */
    Boolean deleteUserToken(String userToken, String deviceType);


    /**
     * 获取当前登录用户token
     * @param userId
     * @param deviceType
     * @return
     */
    String hasLogin(String userId, String deviceType);

    /**
     * 更新  session 数据
     * @param user
     * @param deviceType
     * @return
     */
    Integer setUserToken(User user,String deviceType);

    /**
     * 生成服务 token
     * @param fromService
     * @param toService
     * @return
     */
    TokenVo generateServiceToken(String fromService, String toService);


    /**
     * 验证 服务 token
     * @param token
     * @return
     */
    Boolean verifyServiceToken(String token);
}
