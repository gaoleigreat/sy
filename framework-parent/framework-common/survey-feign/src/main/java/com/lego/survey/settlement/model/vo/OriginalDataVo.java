package com.lego.survey.settlement.model.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * @author yanglf
 * @description
 * @since 2019/1/14
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
public class OriginalDataVo {
    private String tag;
    private Double distance;
    @ApiModelProperty("原始数据value")
    private Double value;
    @ApiModelProperty("测量时间")
    private Date surveyTime;
    @ApiModelProperty("索引")
    private Integer index;
}
