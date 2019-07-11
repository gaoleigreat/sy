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
public class Surveyer {
    @ApiModelProperty("测量员id")
    private String id;
    @ApiModelProperty("测量员名称")
    private String name;
}
