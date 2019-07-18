package com.lego.survey.project.model.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @author yanglf
 * @since 2018/12/21
 **/
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class WorkspaceTreeVo {
    /**
     * 工程项目id
     */
    @ApiModelProperty("工区ID")
    private String id;
    /**
     * 工程项目名称
     */
    @ApiModelProperty("工区名称")
    private String name;
    /**
     * 工程项目编号
     */
    @ApiModelProperty("工区编号")
    private String code;

}