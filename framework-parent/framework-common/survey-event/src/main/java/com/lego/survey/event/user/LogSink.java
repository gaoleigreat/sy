package com.lego.survey.event.user;

import org.springframework.cloud.stream.annotation.Input;
import org.springframework.messaging.SubscribableChannel;
import org.springframework.stereotype.Repository;

/**
 * @author yanglf
 * @description
 * @since 2019/1/2
 **/
@Repository
public interface LogSink {

    String PRINT_LOG = "input_log";

    /**
     * 订阅日志记录事件
     * @return
     */
    @Input(PRINT_LOG)
    SubscribableChannel printLog();

}
