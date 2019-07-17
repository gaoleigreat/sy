package com.lego.survey.settlement.model.vo;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.lego.survey.settlement.model.entity.SurveyPoint;
import com.lego.survey.settlement.model.entity.SurveyResult;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SurveyPontResultVo {
    @ApiModelProperty("单次沉降量")
    private Double singleSettlement;
    @ApiModelProperty("累积沉降量")
    private Double cumulativeSettlement;
    @ApiModelProperty("沉降速率")
    private Double settlingRate;
    @ApiModelProperty("测量时间")
    private Date surveyTime;
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


    @JsonIgnore
    public SurveyPontResultVo loadLimit(SurveyPointVo surveyPoint) {
        this.onceLowerLimit = surveyPoint.getOnceLowerLimit();
        this.onceUpperLimit = surveyPoint.getOnceUpperLimit();
        this.totalLowerLimit = surveyPoint.getTotalLowerLimit();
        this.totalUpperLimit = surveyPoint.getTotalUpperLimit();
        this.speedLowerLimit = surveyPoint.getSpeedLowerLimit();
        this.speedUpperLimit = surveyPoint.getSpeedUpperLimit();
        return this;
    }

    @JsonIgnore
    public SurveyPontResultVo loadSurveyResult(SurveyResult surveyResult) {
        this.singleSettlement = surveyResult.getSingleSettlement();
        this.cumulativeSettlement = surveyResult.getCumulativeSettlement();
        this.settlingRate = surveyResult.getSettlingRate();
        this.surveyTime = surveyResult.getSurveyTime();
        return this;
    }
}
