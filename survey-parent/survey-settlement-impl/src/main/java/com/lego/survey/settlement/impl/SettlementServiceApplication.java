package com.lego.survey.settlement.impl;
import com.lego.survey.event.settlement.SurveyPointResultSource;
import com.lego.survey.event.user.LogSource;
import org.springframework.boot.SpringApplication;
import org.springframework.cloud.client.SpringCloudApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * @author yanglf
 * @description
 * @since 2019/1/5
 **/
@EnableTransactionManagement
@EnableBinding({LogSource.class, SurveyPointResultSource.class})
@EnableFeignClients(basePackages = {"com.lego.survey"})
@ComponentScan(value = {"com.lego.survey","com.lego.survey.lib"})
@SpringCloudApplication()
public class SettlementServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(SettlementServiceApplication.class, args);
    }

}
