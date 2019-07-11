package com.lego.survey.event.project;
import org.springframework.cloud.stream.annotation.Input;
import org.springframework.messaging.SubscribableChannel;
import org.springframework.stereotype.Repository;

/**
 * @author yanglf
 * @description
 * @since 2019/1/7
 **/
@Repository
public interface SectionSink {

    String SECTION_CREATE="input_section_create";

    /**
     * 创建 section 事件
     * @return
     */
    @Input(SECTION_CREATE)
    SubscribableChannel createSection();
}
