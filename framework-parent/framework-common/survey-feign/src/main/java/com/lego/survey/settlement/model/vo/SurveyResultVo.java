package com.lego.survey.settlement.model.vo;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.metadata.BaseRowModel;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.lego.survey.settlement.model.entity.SurveyResult;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * @author yanglf
 * @description
 * @since 2019/1/14
 **/
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SurveyResultVo extends BaseRowModel {
    @ApiModelProperty("测量结果id")
    private Long id;
    @ApiModelProperty("测点名称")
    @ExcelProperty(value = "测点名称", index = 0)
    private String pointName;
    @ApiModelProperty("测量结果编号")
    @ExcelProperty(value = "测点编码", index = 1)
    private String pointCode;
    @ApiModelProperty("测量时间")
    @ExcelProperty(value = "测量时间", index = 2, format = "yyyy/MM/dd")
    private Date surveyTime;
    @ApiModelProperty("测量值")
    @ExcelProperty(value = "测量值", index = 3)
    private Double value;
    @ApiModelProperty("所属工区")
    @ExcelProperty(value = "工区编码", index = 4)
    private String workspaceCode;
    @ApiModelProperty("测量员姓名")
    @ExcelProperty(value = "测量员名称", index = 5)
    private String surveyer;
    @ApiModelProperty("上传时间")
    private Date uploadTime;
    @ApiModelProperty("状态(1-正常，2-停测，3-损坏，4-新建)")
    @ExcelProperty(value = "状态(1-正常，2-停测，3-损坏，4-新建)", index = 6)
    private Integer status;
    @ExcelProperty(value = "原始数据ID",index = 7)
    @ApiModelProperty("原始数据ID")
    private Long originalId;


    @JsonIgnore
    public SurveyResult getSurveyResult() {
        return SurveyResult.builder()
                .surveyTime(this.surveyTime)
                .elevation(this.value)
                .id(this.id)
                .pointCode(this.pointCode)
                .pointName(this.pointName)
                .status(this.status)
                .surveyer(this.surveyer)
                .uploadTime(this.uploadTime)
                .workspaceCode(this.workspaceCode)
                .build();
    }


    @JsonIgnore
    public  SurveyResultVo loadSurveyResultVo(SurveyResult surveyResult){
        return SurveyResultVo.builder()
                .id(surveyResult.getId())
                .pointCode(surveyResult.getPointCode())
                .pointName(surveyResult.getPointName())
                .surveyTime(surveyResult.getSurveyTime())
                .value(surveyResult.getElevation())
                .workspaceCode(surveyResult.getWorkspaceCode())
                .surveyer(surveyResult.getSurveyer())
                .uploadTime(surveyResult.getUploadTime())
                .status(surveyResult.getStatus())
                .originalId(surveyResult.getOriginalId())
                .build();
    }

}
