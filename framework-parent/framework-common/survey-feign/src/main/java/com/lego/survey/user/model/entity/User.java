package com.lego.survey.user.model.entity;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
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
public class User {
    /**
     * 用户id
     */
    @ApiModelProperty("用户id")
    private String id;
    /**
     * 用户名
     */
    @ApiModelProperty("用户名")
    @NotBlank(message = "用户名不能为空")
    private String userName;
    /**
     * 密码
     */
    @ApiModelProperty("用户密码")
    @NotBlank(message = "密码不能为空")
    @Size(min = 6,max = 32,message = "密码长度必须为6-32位")
    private String passWord;
    /**
     * 用户昵称
     */
    @ApiModelProperty("用户昵称")
    @NotBlank(message = "用户昵称不能为空")
    private String name;
    /**
     * 用户手机号
     */
    @ApiModelProperty("用户手机号")
    @NotBlank(message = "手机号不能为空")
    private String phone;
    /**
     * 用户身份证号
     */
    @ApiModelProperty("用户身份证号")
    @NotBlank(message = "身份证号不能为空")
    private String cardId;
    /**
     * 用户角色
     */
    @ApiModelProperty("用户角色")
    private String role;
    /**
     * 所属单位
     */
    @ApiModelProperty("所属单位")
    private OwnGroup group;
    /**
     * 用户权限
     */
    @ApiModelProperty("用户权限")
    private List<String> permission;
    /**
     * 创建时间
     */
    @ApiModelProperty("创建时间")
    private Date createTime;
    /**
     * 更新时间
     */
    @ApiModelProperty("更新时间")
    private Date updateTime;
    @ApiModelProperty("是否有效 0-是;1-否")
    private Integer valid;

    @ApiModelProperty("所属工程")
    private List<OwnProject> ownProjects;

    @ApiModelProperty("所属标段")
    private List<OwnSection> ownSections;



}
