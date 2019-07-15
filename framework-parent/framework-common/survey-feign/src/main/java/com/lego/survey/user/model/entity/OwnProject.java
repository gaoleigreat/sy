package com.lego.survey.user.model.entity;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author yanglf
 * @description
 * @since 2019/7/6
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OwnProject {
    @ApiModelProperty("工程ID")
    private String id;
    @ApiModelProperty("工程名称")
    private String name;
}
