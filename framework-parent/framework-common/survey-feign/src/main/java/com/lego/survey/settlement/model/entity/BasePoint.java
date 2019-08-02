package com.lego.survey.settlement.model.entity;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Date;

/**
 * @author yanglf
 * @description
 * @since 2019/1/7
 **/
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BasePoint {
    @ApiModelProperty("ID")
    private Long id;
    @NotBlank(message = "基准点编号不能为空")
    @ApiModelProperty("基准点编码")
    private String code;
    @NotBlank(message = "基准点名称不能为空")
    @ApiModelProperty("基准点名称")
    private String name;
    @NotNull(message = "基准点类型不能为空")
    @ApiModelProperty("基准点类型 0 ：高程基准点 1：平面基准点 2：三维基准点")
    private Integer type;
    @ApiModelProperty("基准点版本")
    private Integer version;
    @NotNull(message = "平面X不能为空")
    @ApiModelProperty("平面X")
    private Double gridX;
    @NotNull(message = "平面Y不能为空")
    @ApiModelProperty("平面Y")
    private Double gridY;
    @NotNull(message = "高程Z不能为空")
    @ApiModelProperty("高程Z")
    private Double elevation;
    @ApiModelProperty("是否有效0-有效;1-无效")
    private Integer valid;
    @NotBlank(message = "标段编号不能为空")
    @ApiModelProperty("标段编码")
    private String sectionCode;
    @ApiModelProperty("创建人")
    private String createUser;
    @ApiModelProperty("创建时间")
    private Date createTime;
    @ApiModelProperty("更新时间")
    private Date updateTime;
    @ApiModelProperty("备注")
    private String comment;
}
