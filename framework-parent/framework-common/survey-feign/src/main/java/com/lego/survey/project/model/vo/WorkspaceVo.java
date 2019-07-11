package com.lego.survey.project.model.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author yanglf
 * @description
 * @since 2018/12/22
 **/
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class WorkspaceVo {
    @ApiModelProperty("工区id")
    private String id;
    @ApiModelProperty("工区名称")
    private String name;
    @ApiModelProperty("工区编码")
    private String code;
    @ApiModelProperty("工区类型")
    private String type;
    @ApiModelProperty("工区权限")
    private String permission;
}
