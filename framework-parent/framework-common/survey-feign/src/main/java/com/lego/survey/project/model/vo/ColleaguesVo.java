package com.lego.survey.project.model.vo;

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
public class ColleaguesVo {
    @ApiModelProperty("测量员id")
    private String id;
    @ApiModelProperty("测量员昵称")
   private String name;
    @ApiModelProperty("测量员手机号")
    private String phone;
    @ApiModelProperty("测量员角色")
    private String role;

}
