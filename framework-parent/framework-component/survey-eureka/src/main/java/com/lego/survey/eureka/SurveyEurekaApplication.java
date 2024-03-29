package com.lego.survey.eureka;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;

/**
 * @author yanglf
 */
@EnableEurekaClient
@EnableEurekaServer
@SpringBootApplication
@ServletComponentScan
public class SurveyEurekaApplication {

    public static void main(String[] args) {
        SpringApplication.run(SurveyEurekaApplication.class, args);
    }

}

