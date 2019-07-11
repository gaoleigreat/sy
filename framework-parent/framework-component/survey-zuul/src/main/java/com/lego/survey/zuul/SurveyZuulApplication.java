package com.lego.survey.zuul;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.hystrix.EnableHystrix;
import org.springframework.cloud.netflix.zuul.EnableZuulProxy;
import org.springframework.cloud.openfeign.EnableFeignClients;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@SpringBootApplication(scanBasePackages = "com.lego.survey")
@EnableDiscoveryClient
@EnableHystrix
@EnableZuulProxy
@EnableSwagger2
@EnableFeignClients(basePackages = "com.lego.survey")
public class SurveyZuulApplication {

    public static void main(String[] args) {
        SpringApplication.run(SurveyZuulApplication.class, args);
    }

}

