package com.lego.survey.zuul;

import com.lego.survey.zuul.custom.CustomShutdown;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.embedded.tomcat.TomcatServletWebServerFactory;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.hystrix.EnableHystrix;
import org.springframework.cloud.netflix.zuul.EnableZuulProxy;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;

@SpringBootApplication(scanBasePackages = "com.lego.survey")
@EnableDiscoveryClient
@EnableHystrix
@EnableZuulProxy
@EnableFeignClients(basePackages = "com.lego.survey")
public class SurveyZuulApplication {

    public static void main(String[] args) {
        SpringApplication.run(SurveyZuulApplication.class, args);
    }


    @Bean
    public CustomShutdown customShutdown() {
        return new CustomShutdown();
    }

    @Bean
    public TomcatServletWebServerFactory tomcatServletWebServerFactory(CustomShutdown customShutdown) {
        TomcatServletWebServerFactory factory = new TomcatServletWebServerFactory();
        factory.addConnectorCustomizers(customShutdown);
        return factory;
    }

}

