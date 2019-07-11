package com.lego.survey.config;
import com.lego.survey.interceptor.GlobalInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.*;

/**
 * @author yanglf
 * @description
 * @since 2018/12/21
 **/
@Configuration
public class InterceptorConfiguration implements WebMvcConfigurer {

    @Bean
    public GlobalInterceptor globalIInterceptor() {
        return new GlobalInterceptor();
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        // 配置拦截器
        registry.addInterceptor(globalIInterceptor()).addPathPatterns("/survey/**");
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        //   自定义资源映射目录
        registry.addResourceHandler("/doc/**").addResourceLocations("classpath:/doc/");
        //registry.addResourceHandler("/file/image/**").addResourceLocations("file:D:/image/");
    }


    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        // 配置视图跳转控制器
        registry.addRedirectViewController("/swagger", "/swagger-ui.html");
        registry.addRedirectViewController("/html","/doc/html/index.html");
        registry.addRedirectViewController("/pdf","/doc/pdf/index.pdf");
    }


    @Override
    public void addCorsMappings(CorsRegistry registry) {
        // 跨域配置
        registry.addMapping("/survey/**")
                .allowedOrigins("*")
                .allowCredentials(true)
                .allowedMethods("GET","POST","DELETE","PUT")
                .maxAge(3600 * 24);
    }

   /* @Override
    public void configureViewResolvers(ViewResolverRegistry registry) {
        //配置视图解析器
        ScriptTemplateViewResolver viewResolvers=new ScriptTemplateViewResolver();
        viewResolvers.setPrefix("classpath:/templates/");
        viewResolvers.setCache(false);
        viewResolvers.setSuffix(".html");
        registry.viewResolver(viewResolvers);
    }*/

    /*@Override
    public void configureHandlerExceptionResolvers(List<HandlerExceptionResolver> resolvers) {
        resolvers.add((httpServletRequest, httpServletResponse, o, e) -> {
            ModelAndView modelAndView=new ModelAndView();
            if(e instanceof NoHandlerFoundException){
                modelAndView.setViewName("error");
            }
            return modelAndView;
        });
    }*/
}
