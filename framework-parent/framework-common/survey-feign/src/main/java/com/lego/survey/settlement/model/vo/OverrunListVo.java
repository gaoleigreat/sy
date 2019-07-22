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
    @ApiModelProperty("code")
    private String pointCode;
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
    @ApiModelProperty("0-单次沉降异常;1-累积沉降异常;2-沉降速率异常;3-单次累积沉降量异常；4-单次沉降量沉降速率异常;5-累积沉降量沉降速率异常；6-单次、累积、沉降速率异常")
    private Integer exceptionType;
    @ApiModelProperty("备注")
    private String remark;
    @ApiModelProperty("是否处理(0-未处理;1-已处理)")
    private Integer status;
    @ApiModelProperty("关闭时间")
    private Date closeTime;
    @ApiModelProperty("关闭用户名")
    private String closeUser;
    @ApiModelProperty("测点状态( 1：正常 2: 新建 3: 停测 4: 破坏)")
    private Integer pointStatus;
    @ApiModelProperty("测量员")
    private String surveyer;
    @ApiModelProperty("任务ID")
    private Long taskId;

}
