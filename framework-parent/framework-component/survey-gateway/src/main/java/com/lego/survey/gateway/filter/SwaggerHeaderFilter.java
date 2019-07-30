package com.lego.survey.gateway.filter;

import com.lego.survey.gateway.provider.GatewaySwaggerProvider;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.server.ServerWebExchange;

/**
 * @author yanglf
 * @description
 * @since 2019/7/15
 **/
@Component
public class SwaggerHeaderFilter extends AbstractGatewayFilterFactory {

    private static final String HEADER_NAME = "X-Forwarded-Prefix";

    @Override
    public GatewayFilter apply(Object config) {
        return (exchange, chain) -> {
            ServerHttpRequest request = exchange.getRequest();
            String path = request.getURI().getPath();
            if (!StringUtils.endsWithIgnoreCase(path, GatewaySwaggerProvider.API_URL)) {
                return chain.filter(exchange);
            }
            String basePath = path.substring(0, path.lastIndexOf(GatewaySwaggerProvider.API_URL));
            ServerHttpRequest newRequest = request.mutate()
                    .header(HEADER_NAME, basePath)
                    .header("Forwarded", "proto")
                    .build();
            ServerWebExchange newExchange = exchange.mutate().request(newRequest).build();
            return chain.filter(newExchange);
        };
    }

}
