package com.lego.survey.report.model.entity;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import java.util.Date;

/**
 * @author yanglf
 * @description 报表模板
 * @since 2019/1/7
 **/
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TemplateReport {
    @ApiModelProperty(value = "模板Id", example = "1")
    private Long id;
    @NotBlank(message = "模板名称不能为空")
    @ApiModelProperty("模板名称")
    private String name;
    @NotBlank(message = "模板路径不能为空")
    @ApiModelProperty("模板路径")
    private String path;
    @ApiModelProperty(value = "模板文件格式 0：EXCEL 1: WORD 2: TXT 3: PDF", example = "0")
    private Integer type;
    @ApiModelProperty("文件名称")
    private String fileName;
    @NotBlank(message = "模板命名规则不能为空")
    @ApiModelProperty("生成的报告命名规则")
    private String reportNameRule;
    @NotBlank(message = "模板场景不能为空")
    @ApiModelProperty("场景")
    private String scenes;
    @NotBlank(message = "标段标号不能为空")
    @ApiModelProperty("标段编码")
    private String sectionCode;
    @ApiModelProperty("创建人")
    private String createUser;
    @ApiModelProperty("创建时间")
    private Date createTime;
    @ApiModelProperty("更新时间")
    private Date updateTime;
}
