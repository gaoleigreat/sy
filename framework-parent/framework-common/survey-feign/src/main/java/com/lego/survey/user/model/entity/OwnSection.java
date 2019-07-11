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
public class OwnSection {
    @ApiModelProperty("标段ID")
    private String sectionId;
    @ApiModelProperty("标段名称")
    private String sectionName;
}
