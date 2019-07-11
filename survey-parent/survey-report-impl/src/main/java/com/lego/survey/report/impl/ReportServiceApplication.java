package com.lego.survey.report.impl;
import com.lego.survey.event.user.LogSource;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.cloud.stream.annotation.EnableBinding;

/**
 * @author yanglf
 * @description
 * @since 2019/1/5
 **/
@EnableBinding(LogSource.class)
@EnableEurekaClient
@EnableFeignClients(basePackages = {"com.lego.survey"})
@SpringBootApplication(scanBasePackages = {"com.lego.survey","com.lego.survey.lib"})
public class ReportServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(ReportServiceApplication.class, args);
    }
}
