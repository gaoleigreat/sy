package com.lego.survey.config;

import com.alibaba.fastjson.serializer.SerializerFeature;
import com.alibaba.fastjson.support.config.FastJsonConfig;
import com.alibaba.fastjson.support.spring.FastJsonHttpMessageConverter;
import org.springframework.context.annotation.Configuration;
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


}
