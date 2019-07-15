package com.lego.survey.gateway.config;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.cloud.gateway.filter.factory.StripPrefixGatewayFilterFactory;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
/**
 * @author yanglf
 * @description
 * @since 2018/12/25
 **/
@Configuration
public class RouteConfiguration {


   /* @Bean
    @ConditionalOnExpression("${define.route.prod}")
    public RouteLocator prodRouteLocator(RouteLocatorBuilder builder) {
        StripPrefixGatewayFilterFactory.Config config = new StripPrefixGatewayFilterFactory.Config();
        config.setParts(1);
        return builder.routes()
                .route("user", r -> r.path("/survey/api-user/**").uri("lb://user-service"))
                .route("auth", r -> r.path("/survey/api-auth/**").uri("lb://auth-service"))
                .route("project",r -> r.path("/survey/api-project/**").uri("lb://project-service"))
                .build();
    }*/



   /* @Bean
    @ConditionalOnExpression("!${define.route.prod}")
    public RouteLocator devRouteLocator(RouteLocatorBuilder builder) {
        StripPrefixGatewayFilterFactory.Config config = new StripPrefixGatewayFilterFactory.Config();
        config.setParts(1);
        return builder.routes()
                .route("api", r -> r.path("/survey/**")*//*.filters(f -> f.stripPrefix(1))*//*.uri("lb://api-survey"))
                .build();
    }*/
}
