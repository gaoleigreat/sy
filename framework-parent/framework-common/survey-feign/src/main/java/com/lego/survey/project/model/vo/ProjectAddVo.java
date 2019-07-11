package com.lego.survey.project.model.vo;

import com.lego.survey.project.model.entity.Project;
import com.lego.survey.project.model.entity.UpperGroup;
import com.survey.lib.common.utils.UuidUtils;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import java.util.Date;

/**
 * @author yanglf
 * @description
 * @since 2019/7/10
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProjectAddVo {

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

    @ApiModelProperty("所属单位")
    private String group;

    @ApiModelProperty("地址")
    private String address;



    public Project  loadProject(){
        Date currentDate=new Date();
        Project project=new Project();
        project.setValid(0);
        project.setUpdateTime(currentDate);
        project.setCreateTime(currentDate);
        project.setId(UuidUtils.generateShortUuid());
        project.setCode(this.code);
        project.setDesc(this.desc);
        project.setGroup(this.group);
        project.setName(this.name);
        project.setAddress(this.address);
        return project;
    }

}
