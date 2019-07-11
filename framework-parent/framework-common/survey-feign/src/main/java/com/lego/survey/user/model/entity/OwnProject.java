package com.lego.survey.user.model.entity;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
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
public class OwnProject {
    @ApiModelProperty("工程ID")
    private String projectId;
    @ApiModelProperty("工程名称")
    private String projectName;
}
