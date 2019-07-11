package com.lego.survey.settlement.model.vo;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.lego.survey.settlement.model.entity.SurveyPointType;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import java.util.Date;

/**
 * The type Survey point type vo.
 *
 * @author Wesley.Xia
 * @description
 * @since 2019 /1/8 11:17
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SurveyPointTypeVo {
    @ApiModelProperty("ID")
    private long id;
    @NotBlank(message = "类型名称不能为空")
    @ApiModelProperty("类型名称")
    private String name;
    @NotBlank(message = "类型编号不能为空")
    @ApiModelProperty("类型编号")
    private String code;
    @ApiModelProperty("单次沉降量下限阈值")
    private double singleSettleLowerLimit;
    @ApiModelProperty("单次沉降量上限阈值")
    private double singleSettleUpperLimit;
    @ApiModelProperty("累积沉降量下限阈值")
    private double totalSettleLowerLimit;
    @ApiModelProperty("累积沉降量上限阈值")
    private double totalSettleUpperLimit;
    @ApiModelProperty("沉降速率下限阈值")
    private double settleSpeedLowerLimit;
    @ApiModelProperty("沉降速率上限阈值")
    private double settleSpeedUpperLimit;
    @NotBlank(message = "标段编号不能为空")
    @ApiModelProperty("标段编码")
    private String sectionCode;
    @ApiModelProperty("创建时间")
    private Date createTime;
    @ApiModelProperty("更新时间")
    private Date updateTime;
    @ApiModelProperty("备注")
    private String comment;


    @JsonIgnore
    public SurveyPointType getSurveyPointType() {
        return SurveyPointType.builder().id(this.id)
                .code(this.code)
                .name(this.name)
                .comment(this.comment)
                .createTime(this.createTime)
                .updateTime(this.updateTime)
                .sectionCode(this.sectionCode)
                .limits(this.getFormatLimits()).build();
    }

    @JsonIgnore
    private String getFormatLimits() {
        return String.format("%.2f, %.2f, %.2f, %.2f, %.2f, %.2f",
                this.singleSettleLowerLimit,
                this.singleSettleUpperLimit,
                this.totalSettleLowerLimit,
                this.totalSettleUpperLimit,
                this.settleSpeedLowerLimit,
                this.settleSpeedUpperLimit);
    }

    /**
     * LSurveyPointType 2 SurveyPointTypeVo
     *
     * @param type the type
     * @return the survey point type vo
     */
    @JsonIgnore
    public SurveyPointTypeVo loadData(SurveyPointType type) {
        this.id = type.getId();
        this.code = type.getCode();
        this.name = type.getName();
        this.comment = type.getComment();
        this.createTime = type.getCreateTime();
        this.updateTime = type.getUpdateTime();
        this.sectionCode = type.getSectionCode();

        String[] lmts = type.getLimits().split(",");

        if (lmts.length != 6) {
            throw new ClassCastException();
        }

        this.singleSettleLowerLimit = Double.parseDouble(lmts[0]);
        this.singleSettleUpperLimit = Double.parseDouble(lmts[1]);
        this.totalSettleLowerLimit = Double.parseDouble(lmts[2]);
        this.totalSettleUpperLimit = Double.parseDouble(lmts[3]);
        this.settleSpeedLowerLimit = Double.parseDouble(lmts[4]);
        this.settleSpeedUpperLimit = Double.parseDouble(lmts[5]);

        return this;
    }

}
