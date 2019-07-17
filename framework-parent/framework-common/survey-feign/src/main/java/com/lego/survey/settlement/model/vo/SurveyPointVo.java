package com.lego.survey.settlement.model.vo;

import com.alibaba.excel.metadata.BaseRowModel;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.lego.survey.settlement.model.entity.SurveyPoint;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import java.util.Date;

/**
 * @author yanglf
 * @description
 * @since 2019/1/8
 **/
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SurveyPointVo extends BaseRowModel {
    @ApiModelProperty("id")
    private Long id;
    @NotBlank(message = "测点编号不能为空")
    @ApiModelProperty("测点编号")
    private String code;
    @NotBlank(message = "测点名称不能为空")
    @ApiModelProperty("测点名称")
    private String name;
    @ApiModelProperty("测点类型 建筑；地表；管线")
    @NotBlank(message = "测点类型不能为空")
    private String type;
    @ApiModelProperty("初始平面X")
    private Double gridX;
    @ApiModelProperty("初始平面Y")
    private Double gridY;
    @ApiModelProperty("初始高程")
    private Double elevation;
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
    @ApiModelProperty("状态： 1：正常 2: 新建 3: 停测 4: 破坏")
    private Integer status;
    @ApiModelProperty("工区编码")
    @NotBlank(message = "工区编号不能为空")
    private String workspaceCode;
    @ApiModelProperty("创建时间")
    private Date createTime;
    @ApiModelProperty("更新时间")
    private Date updateTime;
    @ApiModelProperty("备注")
    private String comment;


    @JsonIgnore
    public SurveyPoint getSurveyPoint() {
        return SurveyPoint.builder().id(this.id)
                .code(this.code)
                .name(this.name)
                .elevation(this.elevation)
                .comment(this.comment)
                .gridX(this.gridX)
                .gridY(this.gridY)
                .name(this.name)
                .workspaceCode(this.workspaceCode)
                .status(this.status)
                .type(this.type)
                .updateTime(this.updateTime)
                .createTime(this.createTime)
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

    /**
     * LSurveyPoint 2 SurveyPointVo
     *
     * @param  surveyPoint type
     * @return the survey point  vo
     */
    @JsonIgnore
    public SurveyPointVo loadData(SurveyPoint surveyPoint) {
        this.code = surveyPoint.getCode();
        this.name = surveyPoint.getName();
        this.createTime=surveyPoint.getCreateTime();
        this.elevation=surveyPoint.getElevation();
        this.status=surveyPoint.getStatus();
        this.type=surveyPoint.getType();
        this.workspaceCode=surveyPoint.getWorkspaceCode();
        String[] limit = surveyPoint.getLimits().split(",");
        if (limit.length != 6) {
            throw new ClassCastException();
        }
        this.onceLowerLimit = Double.parseDouble(limit[0]);
        this.onceUpperLimit = Double.parseDouble(limit[1]);
        this.totalLowerLimit = Double.parseDouble(limit[2]);
        this.totalUpperLimit = Double.parseDouble(limit[3]);
        this.speedLowerLimit = Double.parseDouble(limit[4]);
        this.speedUpperLimit = Double.parseDouble(limit[5]);
        return this;
    }



}
