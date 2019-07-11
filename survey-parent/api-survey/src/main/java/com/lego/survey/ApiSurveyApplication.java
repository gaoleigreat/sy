package com.lego.survey;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
/**
 * @author yanglf
 */
@EnableEurekaClient
@EnableFeignClients(basePackages = "com.lego.survey")
@SpringBootApplication
@Slf4j
public class ApiSurveyApplication {

    public static void main(String[] args) {
        SpringApplication.run(ApiSurveyApplication.class, args);
    }
}

