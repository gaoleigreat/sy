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
public class SurveyPointException {
    @ApiModelProperty("ID")
    private Long id;
    @ApiModelProperty("测点编号")
    private String pointCode;
    @ApiModelProperty("测量结果id")
    private Long resultId;
    @ApiModelProperty("上次测量数据")
    private String preData;
    @ApiModelProperty("本次测量数据")
    private String currentData;
    @ApiModelProperty("标段编号")
    private String sectionCode;
    @ApiModelProperty("DATA格式 单词沉降量下限,单词沉降量上限,累积沉降量下限,累积沉降量上限,沉降速率下限,沉降速率上限")
    private String limits;
    @ApiModelProperty("创建时间")
    private Date createTime;
    @ApiModelProperty("备注")
    private String mark;
    @ApiModelProperty("状态： 0 未处理  1 已处理")
    private Integer status;
    @ApiModelProperty("处理人id")
    private String closeUser;
    @ApiModelProperty("处理人名称")
    private String closeUserName;
    @ApiModelProperty("处理时间")
    private Date closeTime;
}
