package com.lego.survey.user.model.entity;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * @author yanglf
 * @description  日志
 * @since 2018/12/21
 **/
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Log {
    /**
     * 日志id
     */
    @ApiModelProperty("日志id")
    private String id;
    /**
     * 操作用户id
     */
    @ApiModelProperty("日志操作用户名")
    private String userId;
    /**
     * 操作时间
     */
    @ApiModelProperty("日志操作时间")
    private Date time;
    /**
     * 描述信息
     */
    @ApiModelProperty("日志描述")
    private String desc;
    /**
     * 操作IP
     */
    @ApiModelProperty("日志操作者ip")
    private String ip;
}
