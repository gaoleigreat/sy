package com.lego.survey.settlement.model.entity;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * @author Wesley.Xia
 * @description
 * @since 2019/1/8 11:16
 **/
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SurveyPointType {
    @ApiModelProperty("ID")
    private long id;

    @ApiModelProperty("类型名称")
    private String name;

    @ApiModelProperty("类型简写")
    private String code;

    @ApiModelProperty("沉降量阈值")
    private String limits;

    @ApiModelProperty("标段编码")
    private String sectionCode;

    @ApiModelProperty("创建时间")
    private Date createTime;

    @ApiModelProperty("更新时间")
    private Date updateTime;

    @ApiModelProperty("备注")
    private String comment;

    @ApiModelProperty("是否可用，0表示可用，1表示不可以使用")
    private Byte status;
}
