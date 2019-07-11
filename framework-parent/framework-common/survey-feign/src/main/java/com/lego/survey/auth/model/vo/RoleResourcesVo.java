package com.lego.survey.auth.model.vo;

import com.lego.survey.auth.model.entity.Resources;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @author yanglf
 * @description
 * @since 2019/7/10
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
public class RoleResourcesVo {
    @ApiModelProperty("role")
    private String role;
    @ApiModelProperty("resources")
    private List<Resources> resources;
}
