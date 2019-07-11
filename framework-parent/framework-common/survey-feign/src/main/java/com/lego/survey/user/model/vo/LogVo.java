package com.lego.survey.user.model.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * @author yanglf
 * @description
 * @since 2018/12/24
 **/
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class LogVo {
    @ApiModelProperty("操作用户昵称")
    private String nickName;
    @ApiModelProperty("日志操作时间")
    private Date time;
    @ApiModelProperty("日志描述")
    private String desc;
    @ApiModelProperty("日志操作者ip")
    private String ip;
}
