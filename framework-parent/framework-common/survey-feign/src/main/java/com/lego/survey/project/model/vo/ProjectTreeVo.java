package com.lego.survey.project.model.vo;

import com.lego.survey.project.model.entity.OwnerSection;
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
public class ProjectTreeVo {
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
    @ApiModelProperty("项目工程下的标段")
    private List<SectionVo> sections;

}