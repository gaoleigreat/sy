package com.lego.survey.auth.impl;

import com.lego.survey.event.user.LogSource;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.cloud.stream.annotation.EnableBinding;

/**
 * @author yanglf
 * @descript
 * @since 2018/12/20
 **/
@EnableBinding(LogSource.class)
@EnableConfigurationProperties
@EnableEurekaClient
@EnableFeignClients(basePackages = "com.lego.survey")
@SpringBootApplication(scanBasePackages = {"com.lego.survey"})
public class AuthServiceApplication {

    public static void main(String []args){
        SpringApplication.run(AuthServiceApplication.class,args);
    }

}
