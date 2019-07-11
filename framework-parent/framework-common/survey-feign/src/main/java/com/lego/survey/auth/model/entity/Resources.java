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
public class Resources {
    @ApiModelProperty("id")
    private Long id;
    @ApiModelProperty("资源id")
    private String rId;
    @ApiModelProperty("资源名称")
    private String rName;
    @ApiModelProperty("操作ID")
    private String prId;
    @ApiModelProperty("操作名称")
    private String prName;
    @ApiModelProperty("应用范围")
    private String scope;
    @ApiModelProperty("创建时间")
    private Date creationDate;
}
