package com.lego.survey.auth.model.entity;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * @author yanglf
 * @description
 * @since 2019/7/10
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
public class RoleResources {
    @ApiModelProperty("id")
    private Long id;
    @ApiModelProperty("角色")
    private String role;
    @ApiModelProperty("资源id")
    private Long resourcesId;
    @ApiModelProperty("创建时间")
    private Date creationDate;
    @ApiModelProperty("创建人")
    private String createdBy;
    @ApiModelProperty("最后更新时间")
    private Date lastUpdateDate;
    @ApiModelProperty("最后更新人")
    private String lastUpdatedBy;
}
