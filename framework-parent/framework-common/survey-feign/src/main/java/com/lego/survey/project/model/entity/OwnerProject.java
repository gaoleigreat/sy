package com.lego.survey.project.model.entity;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author yanglf
 * @description
 * @since 2018/12/29
 **/
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OwnerProject {
    @ApiModelProperty("所属工程id")
    private String id;
    @ApiModelProperty("所属工程名称")
    private String name;
    @ApiModelProperty("工程编号")
    private String code;
}
