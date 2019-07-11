package com.lego.survey.base.config;

import com.lego.survey.base.interceptor.AccessInterceptor;
import com.lego.survey.base.interceptor.AuthInterceptor;
import com.lego.survey.base.interceptor.ParameterInterceptor;
import com.lego.survey.base.interceptor.SecurityInterceptor;
import org.springframework.aop.Advisor;
import org.springframework.aop.aspectj.AspectJExpressionPointcut;
import org.springframework.aop.support.DefaultPointcutAdvisor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @author yanglf
 * @description
 * @since 2018/12/22
 **/
@Configuration
public class WebConfiguration implements WebMvcConfigurer {

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new AccessInterceptor())
                .addPathPatterns("/**");
        registry.addInterceptor(new AuthInterceptor())
                .addPathPatterns("/**");
        registry.addInterceptor(new ParameterInterceptor())
                .addPathPatterns("/**");
    }

    @Override
    public void addViewControllers(ViewControllerRegistry registry) {

    }


    /*@Bean
    public Advisor securityAdvisor(SecurityInterceptor securityInterceptor){
        AspectJExpressionPointcut pc = new AspectJExpressionPointcut();
        pc.setExpression("execution( * com.lego.survey..controller.*Controller.*(..))");
        return new DefaultPointcutAdvisor(pc, securityInterceptor);
    }*/

}
