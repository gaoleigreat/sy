package com.lego.survey.user.model.vo;

import com.lego.survey.user.model.entity.User;
import com.survey.lib.common.utils.SecurityUtils;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.util.StringUtils;

import javax.validation.constraints.NotBlank;
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
    @NotBlank(message = "账号不能为空")
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
    @NotBlank(message = "用户角色不能为空")
    private String role;
    /**
     * 所属单位
     */
    @ApiModelProperty("所属单位")
    @NotBlank(message = "所属单位不能为空")
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
    @NotBlank(message = "身份证号不能为空")
    private String cardId;


    public User loadUser(User user) {
        Date currentDate = new Date();
        if (user == null) {
            user = User.builder().build();
            user.setId(this.id);
            user.setPassWord("111111");
            //user.setPassWord(SecurityUtils.encryptionWithMd5(user.getPassWord()));
        }
        user.setCreateTime(currentDate);
        user.setName(this.name);
        user.setPermission(this.permission);
        user.setPhone(this.phone);
        user.setRole(this.role);
        user.setUpdateTime(currentDate);
        user.setUserName(this.userName);
        user.setValid(this.valid != null ? this.valid : 0);
        user.setCardId(this.cardId);
        return user;
    }

}
