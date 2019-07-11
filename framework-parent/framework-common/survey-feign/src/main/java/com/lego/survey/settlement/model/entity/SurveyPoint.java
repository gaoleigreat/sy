package com.lego.survey.settlement.model.entity;

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
public class SurveyPoint {
    @ApiModelProperty("id")
    private Long id;
    @NotBlank(message = "测点编号不能为空")
    @ApiModelProperty("测点编号")
    private String code;
    @NotBlank(message = "测点名称不能为空")
    @ApiModelProperty("测点名称")
    private String name;
    @ApiModelProperty("测点类型 建筑；地表；管线")
    private String type;
    @ApiModelProperty("初始平面X")
    private Double gridX;
    @ApiModelProperty("初始平面Y")
    private Double gridY;
    @ApiModelProperty("初始高程")
    private Double elevation;
    @ApiModelProperty("DATA格式 单词沉降量下限,单词沉降量上限,累积沉降量下限,累积沉降量上限,沉降速率下限,沉降速率上限")
    private String limits;
    @ApiModelProperty("状态： 1：正常 2: 新建 3: 停测 4: 破坏")
    private Integer status;
    @ApiModelProperty("工区编码")
    private String workspaceCode;
    @ApiModelProperty("创建时间")
    private Date createTime;
    @ApiModelProperty("更新时间")
    private Date updateTime;
    @ApiModelProperty("备注")
    private String comment;
}
