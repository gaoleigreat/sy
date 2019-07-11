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
public class SectionVo {
    @ApiModelProperty("标段id")
    private String id;
    @ApiModelProperty("标段名称")
    private String name;
    @ApiModelProperty("标段编号")
    private String code;
    @ApiModelProperty("标段起始里程")
    private Double startingMileage;
    @ApiModelProperty("标段结束里程")
    private Double endMileage;
    @ApiModelProperty("场景")
    private List<String> services;
    @ApiModelProperty("标段所属单位")
    private GroupVo group;
    @ApiModelProperty("标段包含的测量员")
    private List<ColleaguesVo> colleagues;
    @ApiModelProperty("标段包含的工区")
    private List<WorkspaceVo> workspace;
    @ApiModelProperty("地址")
    private String address;
    @ApiModelProperty("标段描述")
    private String desc;

}
