package com.lego.survey.settlement.model.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

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
public class BasePointVo {
    @ApiModelProperty("ID")
    private Long id;
    @ApiModelProperty("基准点名称")
    private String name;
    @ApiModelProperty("基准点编码")
    private String code;
    @ApiModelProperty("平面X")
    private Double gridX;
    @ApiModelProperty("平面Y")
    private Double gridY;
    @ApiModelProperty("基准点高程Z")
    private Double value;
    @ApiModelProperty("基准点类型 0 ：高程基准点 1：平面基准点 2：三维基准点")
    private Integer type;
    @ApiModelProperty("基准点版本")
    private Integer version;
    @ApiModelProperty("新增时间")
    private Date time;
    @ApiModelProperty("是否有效(0-有效;1-无效)")
    private Integer valid;
}
