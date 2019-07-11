package com.lego.survey.project.model.entity;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @author yanglf
 * @description  工区
 * @since 2018/12/21
 **/
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Workspace {
    /**
     * 工区id
     */
    @ApiModelProperty("工区id")
    private String id;
    /**
     * 工区名称
     */
    @ApiModelProperty("工区名称")
    private String name;
    /**
     * 工区编号
     */
    @ApiModelProperty("工区编号")
    private String code;
    /**
     * 工区类型
     */
    @ApiModelProperty("工区类型")
    private String type;
    /**
     * 工区描述
     */
    @ApiModelProperty("工区描述")
    private String desc;
    /**
     * 工区测量员
     */
    @ApiModelProperty("工区测量员")
    private List<Surveyer> surveyer;
    /**
     * 所属标段
     */
    @ApiModelProperty("所属标段")
    private String section;
    /**
     * 所属项目
     */
    @ApiModelProperty("所属工程")
    private String project;
}
