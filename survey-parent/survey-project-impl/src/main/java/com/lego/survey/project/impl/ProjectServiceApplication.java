package com.lego.survey.project.impl;
import com.lego.survey.event.project.SectionSource;
import com.lego.survey.event.user.LogSource;
import com.ribbon.RibbonRule;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.netflix.ribbon.RibbonClient;
import org.springframework.cloud.netflix.ribbon.RibbonClients;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.cloud.stream.annotation.EnableBinding;

/**
 * @author yanglf
 * @descript
 * @since 2018/12/21
 **/
@EnableFeignClients("com.lego.survey")
@EnableEurekaClient
@SpringBootApplication(scanBasePackages = {"com.lego.survey","com.lego.survey.lib"})
@EnableBinding({LogSource.class, SectionSource.class})
@RibbonClient(name = "ribbonRule",configuration = RibbonRule.class)
public class ProjectServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(ProjectServiceApplication.class, args);
    }

}
