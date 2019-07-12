package com.lego.survey.user.model.vo;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author yanglf
 * @description
 * @since 2018/12/21
 **/
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ConfigOptionVo {
    @ApiModelProperty("配置名称")
    private String configName;
    @ApiModelProperty("id")
    private String id;
    @ApiModelProperty("名称")
    private String name;
    @ApiModelProperty("描述")
    private String desc;

}
