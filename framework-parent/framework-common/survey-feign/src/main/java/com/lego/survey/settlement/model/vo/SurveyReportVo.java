package com.lego.survey.settlement.model.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class SurveyReportVo {
    @ApiModelProperty("文件名称")
    private String docname;
    @ApiModelProperty("项目名称")
    private String title;
    @ApiModelProperty("测量地址")
    private String address;
    @ApiModelProperty("测量人员")
    private String surveyer;
    @ApiModelProperty("编制人员")
    private String maker;
    @ApiModelProperty("测点类型")
    private String pontType;
    @ApiModelProperty("单次沉降量下限")
    private Double onceLowerLimit;
    @ApiModelProperty("单次沉降量上限")
    private Double onceUpperLimit;
    @ApiModelProperty("累积沉降量下限")
    private Double totalLowerLimit;
    @ApiModelProperty("累积沉降量上限")
    private Double totalUpperLimit;
    @ApiModelProperty("沉降速率下限")
    private Double speedLowerLimit;
    @ApiModelProperty("沉降速率上限")
    private Double speedUpperLimit;
    @ApiModelProperty("测量时间")
    private Date surveyTime;
    @ApiModelProperty("上次测量时间")
    private Date preSurveyTime;
    @ApiModelProperty("初始测量时间")
    private Date initSurveyTime;


}
