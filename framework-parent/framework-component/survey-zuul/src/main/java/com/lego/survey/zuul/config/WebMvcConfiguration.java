package com.lego.survey.zuul.config;

import com.alibaba.fastjson.serializer.SerializerFeature;
import com.alibaba.fastjson.support.config.FastJsonConfig;
import com.alibaba.fastjson.support.spring.FastJsonHttpMessageConverter;
import com.lego.survey.zuul.predicate.GrayAwarePredicate;
import com.lego.survey.zuul.predicate.GrayAwareRule;
import com.netflix.loadbalancer.IRule;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

/**
 * @author yanglf
 * @description
 * @since 2018/12/29
 **/
@Configuration
public class WebMvcConfiguration implements WebMvcConfigurer {

    @Override
    public void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
        FastJsonHttpMessageConverter converter = new FastJsonHttpMessageConverter();
        FastJsonConfig fastJsonConfig=new FastJsonConfig();
        fastJsonConfig.setSerializerFeatures(SerializerFeature.WriteNullListAsEmpty,
                SerializerFeature.WriteMapNullValue,
                SerializerFeature.WriteNullStringAsEmpty,
                SerializerFeature.PrettyFormat);
        converter.setFastJsonConfig(fastJsonConfig);
        converters.add(converter);
    }


    /*@Bean
    public Converter<Long, Date> convertDateTime() {
        return new Converter<Long, Date>() {
            public Date convert(Long timestamp) {
                return new Date(timestamp);
            }
        };
    }*/


    @Bean("grayAwareRule")
    @Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
   /* @ConditionalOnExpression("${define.zuul.gray} ")*/
    public IRule grayAwareRule(StringRedisTemplate stringRedisTemplate){
        return new GrayAwareRule(new GrayAwarePredicate(stringRedisTemplate));
    }


}
