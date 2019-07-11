package com.lego.survey.project.model.entity;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Wesley.Xia
 * @description
 * @since 2018/12/28 11:21
 **/
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UpperGroup {
    @ApiModelProperty("上级单位id")
    private String id;
    @ApiModelProperty("上级单位名称")
    private String name;
}
