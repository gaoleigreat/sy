package com.lego.survey.project.model.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @author yanglf
 * @description
 * @since 2018/12/22
 **/
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProjectVo {
    @ApiModelProperty("项目工程id")
    private String id;
    @ApiModelProperty("项目工程名称")
   private String name;
    @ApiModelProperty("项目工程编号")
   private String code;
    @ApiModelProperty("项目工程描述")
   private String desc;
    @ApiModelProperty("所属单位")
    private String group;
    @ApiModelProperty("项目工程包含的标段")
    private List<SectionVo> sections;
    @ApiModelProperty("地址")
    private String address;
}
