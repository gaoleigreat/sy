package com.lego.survey.user.impl;

import com.lego.survey.event.user.LogSource;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.cloud.stream.annotation.EnableBinding;

@EnableFeignClients(basePackages = "com.lego.survey")
@EnableEurekaClient
@SpringBootApplication(scanBasePackages = {"com.lego.survey"})
@EnableBinding(LogSource.class)
@EnableCaching
public class UserServiceImplApplication {

    public static void main(String[] args) {
        SpringApplication.run(UserServiceImplApplication.class, args);
    }

}

