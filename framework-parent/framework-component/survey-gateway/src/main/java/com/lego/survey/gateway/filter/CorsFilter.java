package com.lego.survey.gateway.filter;/*
package com.lego.survey.gateway.filter;

import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.web.cors.reactive.CorsUtils;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;
*/
/**
 * @author yanglf
 * @description
 * @since 2019/1/25
 **//*

public class CorsFilter implements GlobalFilter, Ordered {

    private static final String ALL = "*";

    private static final String MAX_AGE = "18000L";

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest();
        String path=request.getPath().value();
        ServerHttpResponse response = exchange.getResponse();
        if("/favicon.ico".equals(path)) {
            response.setStatusCode(HttpStatus.OK);
            return Mono.empty();
        }
        if (!CorsUtils.isCorsRequest(request)) {
            return chain.filter(exchange);
        }
        HttpHeaders requestHeaders  = request.getHeaders();
        HttpMethod requestMethod = requestHeaders .getAccessControlRequestMethod();
        HttpHeaders responseHeaders = response.getHeaders();
        responseHeaders.add(HttpHeaders.ACCESS_CONTROL_ALLOW_ORIGIN,requestHeaders.getOrigin());
        responseHeaders.addAll(HttpHeaders.ACCESS_CONTROL_ALLOW_HEADERS,requestHeaders.getAccessControlAllowHeaders());
        if (requestMethod != null) {
            responseHeaders.add(HttpHeaders.ACCESS_CONTROL_ALLOW_METHODS, requestMethod.name());
        }
        responseHeaders.add(HttpHeaders.ACCESS_CONTROL_ALLOW_CREDENTIALS, "true");
        responseHeaders.add(HttpHeaders.ACCESS_CONTROL_EXPOSE_HEADERS, ALL);
        responseHeaders.add(HttpHeaders.ACCESS_CONTROL_MAX_AGE, MAX_AGE);
        if (request.getMethod() == HttpMethod.OPTIONS) {
            response.setStatusCode(HttpStatus.OK);
            return Mono.empty();
        }
        return chain.filter(exchange);
    }

    @Override
    public int getOrder() {
        return 0;
    }
}
*/
