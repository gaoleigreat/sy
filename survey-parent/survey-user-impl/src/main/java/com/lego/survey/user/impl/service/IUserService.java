package com.lego.survey.user.impl.service;

import com.lego.survey.user.model.entity.User;
import com.lego.survey.user.model.vo.UserAddVo;
import com.lego.survey.user.model.vo.UserVo;
import com.survey.lib.common.page.PagedResult;
import com.survey.lib.common.vo.RespVO;

/**
 * @author yanglf
 * @description
 * @since 2018/12/28
 **/
public interface IUserService {

    /**
     * 通过用户名或手机号获取用户信息
     *
     * @param loginName
     * @return
     */
    User queryByUserNameOrPhone(String loginName);


    /**
     * 通过手机号获取用户信息
     *
     * @param phone
     * @return
     */
    User queryByPhone(String phone);


    /**
     * 通过用户名获取用户信息
     *
     * @param userName
     * @return
     */
    User queryByUserName(String userName);


    /**
     * 修改用户信息
     *
     * @param userAddVo
     * @return
     */
    RespVO modify(UserAddVo userAddVo);


    /**
     * 删除用户信息
     *
     * @param userId
     * @return
     */
    RespVO delete(String userId);


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
    RespVO modifyPhone(String userId,
                       String phone,
                       String activeCode,
                       String newPhone,
                       String newActiveCode);


    /**
     * 修改密码
     *
     * @param userId
     * @param phone
     * @param activeCode
     * @param password
     * @return
     */
    RespVO modifyPassword(String userId,
                          String phone,
                          String activeCode,
                          String password);


    /**
     * 创建用户
     *
     * @param userAddVo
     * @return
     */
    RespVO create(UserAddVo userAddVo);


    /**
     * 根据用户id获取用户信息
     *
     * @param id
     * @return
     */
    RespVO<UserVo> queryUserById(String id);


    /**
     * 获取用户列表
     *
     * @param pageIndex
     * @param pageSize
     * @return
     */
    PagedResult<UserAddVo> queryList(int pageIndex,
                                  int pageSize,
                                  String projectCode,
                                  String sectionCode,
                                  String role,
                                  String groupId);

    /**
     * @param userId
     * @return
     */
    UserAddVo findByUserId(String userId);
}
