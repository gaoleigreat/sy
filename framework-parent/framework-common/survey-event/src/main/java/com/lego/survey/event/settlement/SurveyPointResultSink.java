package com.lego.survey.event.settlement;

import org.springframework.cloud.stream.annotation.Input;
import org.springframework.messaging.SubscribableChannel;
import org.springframework.stereotype.Repository;

/**
 * @author yanglf
 * @description
 * @since 2019/1/17
 **/
@Repository
public interface SurveyPointResultSink {


    String INPUT_RESULT="input_result";


    /**
     * 接受测量成果数据
     * @return
     */
    @Input(INPUT_RESULT)
    SubscribableChannel uploadResult();


}

