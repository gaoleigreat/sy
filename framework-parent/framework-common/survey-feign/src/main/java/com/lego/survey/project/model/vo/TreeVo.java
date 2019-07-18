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
public class TreeVo {
    /**
     * 工程项目id
     */
    private String id;
    /**
     * 工程项目名称
     */
    private String name;
    /**
     * 工程项目编号
     */
    private String code;

    private List<TreeVo> treeVos;

}