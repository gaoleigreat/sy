package com.lego.survey.settlement.model.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * @author yanglf
 * @description 超限列表
 * @since 2019/7/4
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
public class OverrunListVo {
    @ApiModelProperty("点名")
    private String pointName;
    @ApiModelProperty("测量时间")
    private Date surveyTime;
    @ApiModelProperty("类型")
    private String type;
    @ApiModelProperty("初始值")
    private Double initValue;
    @ApiModelProperty("上次读数")
    private Double preValue;
    @ApiModelProperty("本次读数")
    private Double curValue;
    @ApiModelProperty("本次沉降量")
    private Double curSettlement;
    @ApiModelProperty("沉降速率")
    private Double settlingRate;
    @ApiModelProperty("累计沉降量")
    private Double cumulativeSettlement;
    @ApiModelProperty("是否异常")
    private Boolean isException;
    @ApiModelProperty("备注")
    private String remark;
}
