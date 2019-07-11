package com.lego.survey.monitor.config;
import de.codecentric.boot.admin.server.domain.entities.InstanceRepository;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import de.codecentric.boot.admin.server.config.AdminServerNotifierAutoConfiguration.CompositeNotifierConfiguration;
import de.codecentric.boot.admin.server.config.AdminServerNotifierAutoConfiguration.NotifierTriggerConfiguration;
/**
 * @author yanglf
 * @description
 * @since 2019/1/28
 **/
@Configuration
@ConditionalOnProperty(
        prefix = "spring.boot.admin.notify.dingtalk",
        name = {"webhook-token"}
)
@AutoConfigureBefore({NotifierTriggerConfiguration.class, CompositeNotifierConfiguration.class})
public class DingTalkNotifierConfiguration {

    @Bean
    @ConditionalOnMissingBean
    @ConfigurationProperties(prefix = "spring.boot.admin.notify.dingtalk")
    public DingTalkNotifier dingTalkNotifier(InstanceRepository repository) {
        return new DingTalkNotifier(repository);
    }

}
