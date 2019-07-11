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
@AllArgsConstructor
@Builder
public class SurveyOriginal {
    @ApiModelProperty("id")
    private Long id;
    @ApiModelProperty("后视点编码")
    private String bpCode;
    @ApiModelProperty("后视点类型 0 基准点 1 观测点  2 转点")
    private Integer bpType;
    @ApiModelProperty("前视点编码")
    private String fpCode;
    @ApiModelProperty("前视点类型 0 基准点 1 观测点  2 转点")
    private Integer fpType;
    @ApiModelProperty("原始测量数据 JSON数组")
    private String datas;
    @ApiModelProperty("任务中索引值")
    private Integer index;
    @ApiModelProperty("任务Id")
    private Long taskId;
    @ApiModelProperty("上传时间")
    private Date uploadTime;

}
