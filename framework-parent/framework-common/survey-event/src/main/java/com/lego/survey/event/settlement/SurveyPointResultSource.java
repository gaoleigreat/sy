package com.lego.survey.event.settlement;

import org.springframework.cloud.stream.annotation.Output;
import org.springframework.messaging.MessageChannel;
import org.springframework.stereotype.Repository;

/**
 * @author yanglf
 * @description
 * @since 2019/1/17
 **/
@Repository
public interface SurveyPointResultSource {


    String OUTPUT_RESULT="output_result";


    /**
     * 上传测量成果
     * @return
     */
    @Output(OUTPUT_RESULT)
    MessageChannel uploadResult();



}
