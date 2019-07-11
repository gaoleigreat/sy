package com.lego.survey.gateway;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

@EnableConfigurationProperties
@EnableFeignClients("com.lego.survey")
@EnableEurekaClient
@EnableDiscoveryClient
@SpringBootApplication
public class SurveyGatewayApplication {

    public static void main(String[] args) {
        SpringApplication.run(SurveyGatewayApplication.class, args);
    }

}

