package com.lego.survey.settlement.model.entity;

import com.lego.survey.settlement.model.vo.SurveyPointTypeVo;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.util.StringUtils;

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
public class SurveyPointType {
    @ApiModelProperty("ID")
    private long id;

    @ApiModelProperty("类型名称")
    private String name;

    @ApiModelProperty("类型简写")
    private String code;

    @ApiModelProperty("沉降量阈值")
    private String limits;

    @ApiModelProperty("标段编码")
    private String sectionCode;

    @ApiModelProperty("创建时间")
    private Date createTime;

    @ApiModelProperty("更新时间")
    private Date updateTime;

    @ApiModelProperty("备注")
    private String comment;

    @ApiModelProperty("是否可用，0表示可用，1表示不可以使用")
    private Byte status;


    public SurveyPointTypeVo loadVo() {
        SurveyPointTypeVo surveyPointTypeVo = new SurveyPointTypeVo();
        surveyPointTypeVo.setCode(this.code);
        surveyPointTypeVo.setComment(this.comment);
        surveyPointTypeVo.setId(this.id);
        surveyPointTypeVo.setCreateTime(this.createTime);
        surveyPointTypeVo.setName(this.name);
        surveyPointTypeVo.setSectionCode(this.sectionCode);
        surveyPointTypeVo.setUpdateTime(this.updateTime);
        if (!StringUtils.isEmpty(this.limits)) {
            String[] split = limits.split(",");
            if (split.length == 6) {
                surveyPointTypeVo.setSingleSettleLowerLimit(Double.parseDouble(split[0]));
                surveyPointTypeVo.setSettleSpeedUpperLimit(Double.parseDouble(split[1]));
                surveyPointTypeVo.setTotalSettleLowerLimit(Double.parseDouble(split[2]));
                surveyPointTypeVo.setTotalSettleUpperLimit(Double.parseDouble(split[3]));
                surveyPointTypeVo.setSettleSpeedLowerLimit(Double.parseDouble(split[4]));
                surveyPointTypeVo.setSingleSettleUpperLimit(Double.parseDouble(split[5]));
            }
        }
        return surveyPointTypeVo;
    }


}
