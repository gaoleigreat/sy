package com.lego.survey.event.project;
import org.springframework.cloud.stream.annotation.Output;
import org.springframework.messaging.MessageChannel;
import org.springframework.stereotype.Repository;

/**
 * @author yanglf
 * @description
 * @since 2019/1/7
 **/
@Repository
public interface SectionSource {

    String SECTION_CREATE="output_section_create";

    /**
     * 新增 section 事件
     * @return
     */
    @Output(SECTION_CREATE)
    MessageChannel createSection();

}
