package com.lego.survey.user.impl.repository;
import com.lego.survey.user.model.entity.User;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author yanglf
 * @description
 * @since 2018/12/21
 **/
@Repository
public interface UserRepository extends MongoRepository<User,String> {


    /**
     * 通过手机号获取用户信息
     * @param loginName
     * @param valid
     * @return
     */
    User findUserByPhoneAndValid(String loginName, int valid);


    /**
     * 通过用户名获取用户信息
     * @param userName
     * @param valid
     * @return
     */
    User findUserByUserNameAndValid(String userName, int valid);


    /**
     * 通过id获取用户信息
     * @param id
     * @param valid
     * @return
     */
    User findUserByIdAndValid(String id, int valid);


    /**
     * 根据单位id获取用户信息
     * @param groupId
     * @param valid
     * @return
     */
    List<User> findUsersByGroupIdAndValid(String groupId, int valid);



}
