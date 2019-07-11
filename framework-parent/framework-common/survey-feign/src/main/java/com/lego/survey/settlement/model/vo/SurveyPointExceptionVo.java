package com.lego.survey.settlement.model.vo;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.lego.survey.settlement.model.entity.SurveyPointException;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
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
public class SurveyPointExceptionVo {
    @ApiModelProperty("ID")
    private long id;
    @ApiModelProperty("测点编号")
    @NotBlank(message = "测点编号不能为空")
    private String pointCode;
    @ApiModelProperty("测量结果id")
    private Long resultId;
    @ApiModelProperty("创建时间")
    private Date createTime;
    @ApiModelProperty("上期高程")
    private Double preElevation;
    @ApiModelProperty("上期单次沉降量")
    private Double preOffsetValue;
    @ApiModelProperty("上期累积沉降量")
    private Double preTotalOffsetValue;
    @ApiModelProperty("上期沉降速率")
    private Double preSpeed;
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
    @ApiModelProperty("状态： 0 未处理  1 已处理")
    private Integer status;
    @ApiModelProperty("测量数据")
    @NotBlank(message = "测量数据不能为空")
    private String surveyData;
    @NotBlank(message = "标段标号不能为空")
    @ApiModelProperty("标段编号")
    private String sectionCode;
    @ApiModelProperty("备注")
    private String description;
    @ApiModelProperty("处理时间")
    private Date closeTime;
    @ApiModelProperty("处理人id")
    private String closeUser;
    @ApiModelProperty("处理人名称")
    private String closeUserName;


    @JsonIgnore
    public SurveyPointException getSurveyPointException() {
        return SurveyPointException.builder().id(this.id)
                .closeTime(this.closeTime)
                .createTime(this.createTime)
                .preData(this.getFormatPreLimits())
                .currentData(this.getFormatCurLimits())
                .closeUser(this.closeUser)
                .closeUserName(this.closeUserName)
                .mark(this.description)
                .pointCode(this.pointCode)
                .resultId(this.resultId)
                .sectionCode(this.sectionCode)
                .status(this.status)
                .limits(this.getFormatLimits()).build();
    }

    @JsonIgnore
    private String getFormatLimits() {
        return String.format("%.2f, %.2f, %.2f, %.2f, %.2f, %.2f",
                this.onceLowerLimit,
                this.onceUpperLimit,
                this.totalLowerLimit,
                this.totalUpperLimit,
                this.speedLowerLimit,
                this.speedUpperLimit);
    }


    @JsonIgnore
    private String getFormatCurLimits() {
        return String.format("%.2f, %.2f, %.2f, %.2f",
                this.curElevation,
                this.curOffsetValue,
                this.curTotalOffsetValue,
                this.curSpeed);
    }


    @JsonIgnore
    private String getFormatPreLimits() {
        return String.format("%.2f, %.2f, %.2f, %.2f",
                this.preElevation,
                this.preOffsetValue,
                this.preTotalOffsetValue,
                this.preSpeed);
    }



    /**
     * LSurveyPointException 2 SurveyPointExceptionVo
     *
     * @param exception the exception
     * @return the survey point exception vo
     */
    @JsonIgnore
    public SurveyPointExceptionVo loadData(SurveyPointException exception) {
        this.id = exception.getId();
        this.pointCode = exception.getPointCode();
        this.createTime = exception.getCreateTime();
        this.status = exception.getStatus();
        this.description = exception.getMark();
        String[] lmts = exception.getLimits().split(",");
        if (lmts.length != 6) {
            return null;
        }
        this.onceLowerLimit = Double.parseDouble(lmts[0]);
        this.onceUpperLimit = Double.parseDouble(lmts[1]);
        this.totalLowerLimit = Double.parseDouble(lmts[2]);
        this.totalUpperLimit = Double.parseDouble(lmts[3]);
        this.speedLowerLimit = Double.parseDouble(lmts[4]);
        this.speedUpperLimit = Double.parseDouble(lmts[5]);

        String[] currentData = exception.getCurrentData().split(",");
        if(currentData.length !=4){
           return null;
        }
        this.curElevation=Double.parseDouble(currentData[0]);
        this.curOffsetValue=Double.parseDouble(currentData[1]);
        this.curTotalOffsetValue=Double.parseDouble(currentData[2]);
        this.curSpeed=Double.parseDouble(currentData[3]);
        return this;
    }



}
