package com.lego.survey.project.model.entity;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import java.util.Date;
import java.util.List;

/**
 * @author yanglf
 * @description 工程项目
 * @since 2018/12/21
 **/
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Project {
    /**
     * 工程项目id
     */
    @ApiModelProperty("项目工程ID")
    private String id;
    /**
     * 工程项目名称
     */
    @ApiModelProperty("项目工程名称")
    @NotBlank(message = "项目工程名称不能为空")
    private String name;
    /**
     * 工程项目编号
     */
    @ApiModelProperty("项目工程编号")
    @NotBlank(message = "项目工程编号不能为空")
    private String code;
    /**
     * 工程项目描述
     */
    @ApiModelProperty("项目工程描述")
    @NotBlank(message = "项目工程描述不能为空")
    private String desc;
    /**
     * 关联的项目标段
     */
    @ApiModelProperty("项目工程下的标段")
    private List<OwnerSection> sections;
    @ApiModelProperty("所属单位")
    private String group;
    @ApiModelProperty("新增时间")
    private Date createTime;
    @ApiModelProperty("更新时间")
    private Date updateTime;
    @ApiModelProperty("是否有效 0-是;1-否")
    private Integer valid;
    @ApiModelProperty("地址")
    private String address;

}