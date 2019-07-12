package com.lego.survey.user.model.entity;

import com.lego.survey.user.model.vo.ConfigOptionVo;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @author yanglf
 * @description
 * @since 2018/12/21
 **/
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Config {
    /**
     * 日志id
     */
    @ApiModelProperty("配置id")
    private String id;
    /**
     * 操作用户id
     */
    @ApiModelProperty("配置名称")
    @NotBlank(message = "配置名称不能为空")
    private String name;

    @ApiModelProperty("可选项")
    private List<ConfigOptionVo> option;
    /**
     * 更新时间
     */
    @ApiModelProperty("更新时间")
    private Date updateTime;
    @ApiModelProperty("是否有效 0-是;1-否")
    private Integer valid;
}
