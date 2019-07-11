package com.lego.survey.project.model.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @author yanglf
 * @description
 * @since 2019/7/6
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
public class GroupTreeVo {
    @ApiModelProperty("单位ID")
    private String id;
    @ApiModelProperty("单位名称")
    private String name;
    @ApiModelProperty("子公司")
    private List<GroupTreeVo> childGroup;
}
