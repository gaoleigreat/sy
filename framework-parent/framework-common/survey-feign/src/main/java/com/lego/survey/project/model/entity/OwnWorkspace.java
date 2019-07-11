package com.lego.survey.project.model.entity;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @author yanglf
 * @description
 * @since 2018/12/29
 **/
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OwnWorkspace {
    @ApiModelProperty("所属工区id")
    private String id;
    @ApiModelProperty("所属工区名称")
    private String name;
    @ApiModelProperty("工区编号")
    private String code;
    @ApiModelProperty("工区类型")
    private String type;
    @ApiModelProperty("工区描述")
    private String desc;
    @ApiModelProperty("工区所属测量员")
    private List<Surveyer> surveyer;
    @ApiModelProperty("是否有效 0-是;1-否")
    private Integer valid;
}
