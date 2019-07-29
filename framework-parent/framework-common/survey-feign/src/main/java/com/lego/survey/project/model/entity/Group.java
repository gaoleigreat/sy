package com.lego.survey.project.model.entity;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import java.util.Date;

/**
 * The type Group.
 *
 * @author yanglf
 * @description 所属单位
 * @since 2018 /12/21
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Group {
    /**
     * 所属单位id
     */
    @ApiModelProperty("单位ID")
    private String id;
    /**
     * 所属单位名称
     */
    @ApiModelProperty("单位名称")
    @NotBlank(message = "单位名称不能为空")
    private String name;
    /**
     * 所属单位描述
     */
    @ApiModelProperty("单位描述")
    private String desc;

    /**
     * 上级单位
     */
    @ApiModelProperty("上级单位")
    private UpperGroup upperGroup;

    @ApiModelProperty("新增时间")
    private Date createTime;
    @ApiModelProperty("更新时间")
    private Date updateTime;
    @ApiModelProperty("是否有效 0-是;1-否")
    private Integer valid;

    @ApiModelProperty("公司地址")
    private String address;

    @ApiModelProperty("网址")
    private String internetSite;

    @ApiModelProperty("联系电话")
    private String contactNumber;

}
