package com.lego.survey.settlement.model.vo;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.lego.survey.settlement.model.entity.SurveyResult;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import java.util.Date;
import com.lego.word.element.WObject;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SurveyReportDataVo extends WObject{

    @ApiModelProperty("ID")
    private long id;
    @ApiModelProperty("测点编号")
    @NotBlank(message = "测点编号不能为空")
    private String pointCode;
    @ApiModelProperty("测量结果id")
    private Long resultId;
    @ApiModelProperty("初始高程")
    private Double initElevation;
    @ApiModelProperty("上期高程")
    private Double preElevation;
    @ApiModelProperty("当前高程")
    private Double curElevation;
    @ApiModelProperty("当前单次沉降量")
    private Double curOffsetValue;
    @ApiModelProperty("当前累积沉降量")
    private Double curTotalOffsetValue;
    @ApiModelProperty("当前沉降速率")
    private Double curSpeed;
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
    @ApiModelProperty("测点类型")
    private String pointType;

    @ApiModelProperty("测量时间")
    private Date surveyTime;

    @ApiModelProperty("上次测量时间")
    private Date preSurveyTime;

    @ApiModelProperty("初始测量时间")
    private Date initSurveyTime;

    @ApiModelProperty("备注")
    private String remark;

    @ApiModelProperty("测量员")
    private String surveyer;

    @ApiModelProperty("工区编号")
    private String workspaceCode;




    @JsonIgnore
    public SurveyReportDataVo loadSurveyReportDataVo(SurveyResult surveyResult) {
        return SurveyReportDataVo.builder()
                .pointCode(surveyResult.getPointCode())
                .resultId(surveyResult.getId())
                .curElevation(surveyResult.getElevation())
                .pointCode(surveyResult.getPointCode())
                .curOffsetValue(surveyResult.getSingleSettlement())
                .curTotalOffsetValue(surveyResult.getCumulativeSettlement())
                .curSpeed(surveyResult.getSettlingRate())
                .surveyer(surveyResult.getSurveyer())
                .surveyTime(surveyResult.getSurveyTime())
                .workspaceCode(surveyResult.getWorkspaceCode())
                .build();

    }


    @Override
    public Object getValByKey(String s) {
        if ("id".equals(s)){
            return  this.id;
        }else if ("pointCode".equals(s)){
            return this.pointCode;
        }else if ("initElevation".equals(s)){
            return this.initElevation;
        }else if ("preElevation".equals(s)){
            return this.preElevation;
        }else if ("curElevation".equals(s)){
            return this.curElevation;
        }else if ("curOffsetValue".equals(s)){
            return this.curOffsetValue;
        }else if ("curTotalOffsetValue".equals(s)){
            return this.curTotalOffsetValue;
        }else if ("curSpeed".equals(s)){
            return this.curSpeed;
        }
        return null;
    }
}
