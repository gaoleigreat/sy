package com.lego.survey.user.model.vo;

import com.lego.survey.user.model.entity.User;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

/**
 * @author yanglf
 * @descript
 * @since 2018/12/17
 **/
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserAddVo {
    /**
     * 用户id
     */
    @ApiModelProperty("用户id")
    private String id;
    /**
     * 用户名
     */
    @ApiModelProperty("账号")
    private String userName;
    /**
     * 用户昵称
     */
    @ApiModelProperty("姓名")
    private String name;
    /**
     * 用户手机号
     */
    @ApiModelProperty("电话")
    private String phone;
    /**
     * 用户角色
     */
    @ApiModelProperty("用户角色")
    private String role;
    /**
     * 所属单位
     */
    @ApiModelProperty("所属单位")
    private String group;
    /**
     * 用户权限
     */
    @ApiModelProperty("权限")
    private List<String> permission;

    @ApiModelProperty("所属工程")
    private List<String> project;

    @ApiModelProperty("所属标段")
    private List<String> section;

    @ApiModelProperty("所属模块")
    private List<String>  service;

    @ApiModelProperty("工区")
    private List<String> workSpace;


    public User loadUser(){
        Date currentDate=new Date();
        return User.builder().createTime(currentDate)
                .id(this.id)
                .name(this.name)
                .passWord("111111")
                .permission(this.permission)
                .phone(this.phone)
                .role(this.role)
                .updateTime(currentDate)
                .userName(this.userName)
                .valid(0)
                .build();
    }



}
