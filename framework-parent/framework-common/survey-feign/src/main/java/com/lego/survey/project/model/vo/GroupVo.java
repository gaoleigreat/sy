package com.lego.survey.project.model.vo;

import com.lego.survey.project.model.entity.UpperGroup;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author yanglf
 * @description
 * @since 2018/12/22
 **/
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GroupVo {
    @ApiModelProperty("单位ID")
    private String id;
    @ApiModelProperty("单位名称")
    private String name;
    @ApiModelProperty("单位描述")
    private String desc;
    @ApiModelProperty("上级单位")
    private UpperGroup upperGroup;
    @ApiModelProperty("公司地址")
    private String address;

    @ApiModelProperty("网址")
    private String internetSite;

    @ApiModelProperty("联系电话")
    private String contactNumber;
}
