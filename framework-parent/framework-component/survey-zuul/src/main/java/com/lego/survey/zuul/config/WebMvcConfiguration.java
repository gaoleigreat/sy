package com.lego.survey.zuul.config;

import com.alibaba.fastjson.serializer.SerializeConfig;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.alibaba.fastjson.serializer.ToStringSerializer;
import com.alibaba.fastjson.support.config.FastJsonConfig;
import com.alibaba.fastjson.support.spring.FastJsonHttpMessageConverter;
import com.lego.survey.zuul.predicate.GrayAwarePredicate;
import com.lego.survey.zuul.predicate.GrayAwareRule;
import com.netflix.loadbalancer.IRule;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.context.annotation.Scope;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.context.request.RequestContextListener;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * @author yanglf
 * @description
 * @since 2018/12/29
 **/
@Configuration
public class WebMvcConfiguration implements WebMvcConfigurer {


    @Bean("grayAwareRule")
    @Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
    @Profile(value = "local")
    public IRule grayAwareRule(StringRedisTemplate stringRedisTemplate) {
        return new GrayAwareRule(new GrayAwarePredicate(stringRedisTemplate));
    }


    @Bean
    public RequestContextListener requestContextListener() {
        return new RequestContextListener();
    }

}
