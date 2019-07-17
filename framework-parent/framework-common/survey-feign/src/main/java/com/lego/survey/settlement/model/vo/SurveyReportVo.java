package com.lego.survey.settlement.model.vo;

import com.lego.word.element.WObject;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.text.SimpleDateFormat;
import java.util.Date;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class SurveyReportVo extends WObject {
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


    @Override
    public Object getValByKey(String s) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        if ("docname".equals(s)) {
            return this.docname;
        } else if ("title".equals(s)) {
            return this.title;
        } else if ("address".equals(s)) {
            return this.address;
        } else if ("surveyer".equals(s)) {
            return this.surveyer;
        } else if ("maker".equals(s)) {
            return this.maker;
        } else if ("onceLowerLimit".equals(s)) {
            return this.onceLowerLimit;
        } else if ("surveyTime".equals(s)&& this.surveyTime != null) {
            return sdf.format(this.surveyTime);
        } else if ("preSurveyTime".equals(s)&& this.preSurveyTime != null) {
            return sdf.format(this.preSurveyTime);
        } else if ("initSurveyTime".equals(s) && this.initSurveyTime != null) {
            return sdf.format(this.initSurveyTime);
        }
        return null;
    }
}
