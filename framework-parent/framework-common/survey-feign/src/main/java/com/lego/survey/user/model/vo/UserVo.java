package com.lego.survey.user.model.vo;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @author yanglf
 * @description
 * @since 2018/12/22
 **/
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserVo {
    @ApiModelProperty("用户id")
    private String id;
    @ApiModelProperty("用户名")
    private String userName;
    @ApiModelProperty("用户昵称")
    private String name;
    @ApiModelProperty("手机号")
    private String phone;
    @ApiModelProperty("身份证号")
    private String cardId;
    @ApiModelProperty("所属单位")
    private String groupName;
    @ApiModelProperty("用户角色")
    private String role;
    @ApiModelProperty("业务服务")
    private List<String> services;
    @ApiModelProperty("工程名称")
    private String projectName;
    @ApiModelProperty("标段名称")
    private String sectionName;
    @ApiModelProperty("权限")
    private List<String> permissions;
    @ApiModelProperty("所属工区")
    private String workSpace;

}
