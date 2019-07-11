package com.lego.survey.project.model.vo;

import com.lego.survey.project.model.entity.Section;
import com.survey.lib.common.utils.UuidUtils;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

/**
 * @author yanglf
 * @description
 * @since 2019/7/11
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SectionAddVo {
    @ApiModelProperty("ID")
    private String id;
    @ApiModelProperty("标段名称")
    private String name;
    @ApiModelProperty("标段编号")
    private String code;
    @ApiModelProperty("工程ID")
    private String projectId;
    @ApiModelProperty("标段地址")
    private String address;
    @ApiModelProperty("介绍")
    private String desc;
    @ApiModelProperty("场景")
    private List<String> services;
    @ApiModelProperty()
    private List<Double> property;
    @ApiModelProperty("承包单位")
    private String groupId;

    public Section loadSection(){
        Date currentDate=new Date();
        return Section.builder()
                .code(this.code)
                .createTime(currentDate)
                .desc(this.desc)
                .id(UuidUtils.generateShortUuid())
                .name(this.name)
                .property(this.property)
                .service(this.services)
                .valid(0)
                .address(this.address)
                .updateTime(currentDate)
                .build();
    }










}
