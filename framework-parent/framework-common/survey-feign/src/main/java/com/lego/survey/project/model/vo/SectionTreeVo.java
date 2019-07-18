package com.lego.survey.project.model.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import java.util.List;

/**
 * @author yanglf
 * @since 2018/12/21
 **/
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SectionTreeVo {
    /**
     * 工程项目id
     */
    @ApiModelProperty("标段ID")
    private String id;
    /**
     * 工程项目名称
     */
    @ApiModelProperty("标段名称")
    private String name;
    /**
     * 工程项目编号
     */
    @ApiModelProperty("标段编号")
    private String code;

    @ApiModelProperty("工区")
    private List<WorkspaceTreeVo> workspaceTreeVos;

}