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

    @ApiModelProperty("所属工程code")
    private List<String> project;

    @ApiModelProperty("所属标段code")
    private List<String> section;

    @ApiModelProperty("所属模块")
    private List<String> service;

    @ApiModelProperty("工区code")
    private List<String> workSpace;

    @ApiModelProperty("是否有效 0-是;1-否")
    private Integer valid;

    @ApiModelProperty("身份证号")
    private String cardId;


    public User loadUser() {
        Date currentDate = new Date();
        User user = User.builder().createTime(currentDate)
                .id(this.id)
                .name(this.name)
                .passWord("111111")
                .permission(this.permission)
                .phone(this.phone)
                .role(this.role)
                .updateTime(currentDate)
                .userName(this.userName)
                .valid(0)
                .cardId(cardId)
                .build();
        if (this.valid != null) {
            user.setValid(this.valid);
        }
        return user;
    }

}
