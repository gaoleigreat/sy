package com.lego.survey.event.user;

import org.springframework.cloud.stream.annotation.Output;
import org.springframework.messaging.MessageChannel;
import org.springframework.stereotype.Repository;

/**
 * @author yanglf
 * @description
 * @since 2019/1/2
 **/
@Repository
public interface LogSource {

    String PRINT_LOG = "output_log";

    /**
     * 发送日志记录事件
     * @return
     */
    @Output(PRINT_LOG)
    MessageChannel printLog();


}
