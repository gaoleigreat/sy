package com.lego.survey.settlement.model.entity;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.metadata.BaseRowModel;
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
public class SurveyResult extends BaseRowModel {
    @ExcelProperty(index = 0,value = "成果ID")
    @ApiModelProperty("id")
    private Long id;
    @ExcelProperty(index = 1,value = "成果编号")
    @NotBlank(message = "成果编号不能为空")
    @ApiModelProperty("成果编号")
    private String pointCode;
    @ExcelProperty(index = 2,value = "成果名称")
    @NotBlank(message = "成果名称不能为空")
    @ApiModelProperty("成果名称")
    private String pointName;
    @ExcelProperty(index = 3,value = "测量时间",format = "yyyy/MM/dd")
    @ApiModelProperty("测量时间")
    private Date surveyTime;
    @ExcelProperty(index = 4,value = "工区编号")
    @NotBlank(message = "工区编号不能为空")
    @ApiModelProperty("工区编码")
    private String workspaceCode;
    @ExcelProperty(index = 5,value = "测量员ID")
    @NotBlank(message = "测量员id不能为空")
    @ApiModelProperty("测量员id")
    private String surveyId;
    @NotBlank(message = "测量员名称不能为空")
    @ApiModelProperty("测量员名称")
    @ExcelProperty(index = 6,value = "测量员名称")
    private String surveyer;
    @ApiModelProperty("初始平面X")
    @ExcelProperty(index = 7,value = "初始平面X")
    private Double gridX;
    @ApiModelProperty("初始平面Y")
    @ExcelProperty(index = 8,value = "初始平面Y")
    private Double gridY;
    @ApiModelProperty("初始高程")
    @ExcelProperty(index = 9,value = "初始高程")
    private Double elevation;
    @ApiModelProperty("1-正常，2-停测，3-损坏，4-新建")
    @ExcelProperty(index = 10,value = "状态")
    private Integer status;
    @ApiModelProperty("上传时间")
    @ExcelProperty(index = 11,value = "上传时间",format = "yyyy/MM/dd")
    private Date uploadTime;
    @ExcelProperty(index = 12,value = "原始数据ID")
    @ApiModelProperty("原始数据id")
    private Long originalId;
    @ApiModelProperty("单次沉降量")
    private Double singleSettlement;
    @ApiModelProperty("累积沉降量")
    private Double cumulativeSettlement;
    @ApiModelProperty("沉降速率")
    private Double settlingRate;

}
