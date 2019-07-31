package com.lego.survey.project.model.entity;

import com.lego.survey.project.model.vo.SectionAddVo;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.util.CollectionUtils;

import javax.validation.constraints.NotBlank;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author yanglf
 * @description 项目标段
 * @since 2018/12/21
 **/
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Section {
    /**
     * 标段id
     */
    @ApiModelProperty("标段id")
    private String id;
    /**
     * 标段名称
     */
    @ApiModelProperty("标段名称")
    @NotBlank(message = "标段名称不能为空")
    private String name;
    /**
     * 标段编号
     */
    @ApiModelProperty("标段编号")
    @NotBlank(message = "标段编号不能为空")
    private String code;
    /**
     * 标段描述
     */
    @ApiModelProperty("标段描述")
    @NotBlank(message = "标段描述不能为空")
    private String desc;
    /**
     * 服务
     */
    @ApiModelProperty("场景")
    private List<String> service;

    @ApiModelProperty("标段起始里程，标段结束里程")
    private List<Double> property;
    /**
     * 关联工区id
     */
    @ApiModelProperty("标段下的工区")
    private List<OwnWorkspace> workSpace;
    /**
     * 标段负责人id
     */
    @ApiModelProperty("标段负责人")
    private List<Master> master;
    /**
     * 工区测量员id
     */
    @ApiModelProperty("标段测量员id")
    private List<Surveyer> surveyer;
    /**
     * 所属项目id
     */
    @ApiModelProperty("标段所属项目id")
    private OwnerProject ownerProject;
    /**
     * 所属单位id
     */
    @ApiModelProperty("标段所属单位id")
    private UpperGroup ownerGroup;

    @ApiModelProperty("新增时间")
    private Date createTime;

    @ApiModelProperty("更新时间")
    private Date updateTime;
    @ApiModelProperty("是否有效 0-是;1-否")
    private Integer valid;

    @ApiModelProperty("地址")
    private String address;


    public SectionAddVo loadAddVo() {
        SectionAddVo addVo = SectionAddVo.builder().address(this.address)
                .code(this.code)
                .desc(this.desc)
                .id(this.id)
                .name(this.name)
                .property(this.property)
                .services(this.service).build();
        OwnerProject ownerProject = this.ownerProject;
        if (ownerProject != null) {
            addVo.setProjectCode(ownerProject.getCode());
        }
        UpperGroup ownerGroup = this.ownerGroup;
        if (ownerGroup != null) {
            addVo.setGroupId(ownerGroup.getId());
        }
        return addVo;
    }


    public List<Workspace> loadWorkspace() {
        List<Workspace> workspaces = new ArrayList<>();
        List<OwnWorkspace> workSpaces = this.workSpace;
        if (!CollectionUtils.isEmpty(workSpaces)) {
            for (OwnWorkspace workSpace : workSpaces) {
                Integer valid = workSpace.getValid();
                if (valid == null || valid != 0) {
                    continue;
                }
                Workspace workspace = new Workspace();
                workspace.setCode(workSpace.getCode());
                workspace.setId(workSpace.getId());
                workspace.setDesc(workSpace.getDesc());
                workspace.setName(workSpace.getName());
                workspace.setSurveyer(workSpace.getSurveyer());
                workspace.setType(workSpace.getType());
                workspaces.add(workspace);
            }
        }
        return workspaces;
    }


}
