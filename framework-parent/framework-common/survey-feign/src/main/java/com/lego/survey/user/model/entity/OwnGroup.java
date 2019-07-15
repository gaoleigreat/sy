package com.lego.survey.user.model.entity;

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
public class OwnGroup {
    @ApiModelProperty("所属单位id")
    private String id;
    @ApiModelProperty("所属单位名称")
    private String name;
}
