package com.lego.survey.ribbon;

import com.netflix.loadbalancer.IRule;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author yanglf
 * @description
 * @since 2019/7/17
 **/
@Configuration
public class RibbonRule {

    @Bean
    public IRule myRule() {
        return new MyRule();
    }
}
