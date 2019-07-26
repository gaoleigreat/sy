package com.lego.survey.ribbon;

import com.netflix.loadbalancer.IRule;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;

/**
 * @author yanglf
 * @description
 * @since 2019/7/17
 **/
@Configuration
public class RibbonRule {

    @Bean
    @Primary
    @Profile(value = "dev")
    public IRule myRule() {
        return new MyRule();
    }
}
