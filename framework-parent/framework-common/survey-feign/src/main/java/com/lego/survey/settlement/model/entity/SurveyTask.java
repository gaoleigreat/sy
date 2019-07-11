package com.lego.survey.settlement.model.entity;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * @author yanglf
 * @description
 * @since 2019/1/8
 **/
@Data
@NoArgsConstructor
@Builder
@AllArgsConstructor
public class SurveyTask {
    @ApiModelProperty("id")
    private Long id;
    @ApiModelProperty("任务名称")
    private String name;
    @ApiModelProperty("任务属性json串")
    private String property;
    @ApiModelProperty("测量类型 0 单次测量 1 后前前后  2 前后后前 3 交替")
    private Integer measureType;
    // {"闭合路线", "不闭合"};
    @ApiModelProperty("行进方式 0 自由线路 1 闭合线路  2 附和线路 3 往返")
    private Integer lineWay;
    @ApiModelProperty("是否平差 1-平差，0-未平差")
    private Integer adjustment;
    @ApiModelProperty("任务创建时间")
    private Date createTime;
    @ApiModelProperty("任务结束时间")
    private Date endTime;
    @ApiModelProperty("上传时间")
    private Date uploadTime;

}
