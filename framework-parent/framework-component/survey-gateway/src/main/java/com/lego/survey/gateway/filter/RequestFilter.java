/*
package com.lego.survey.gateway.filter;

import com.lego.survey.gateway.utils.RouteUtil;
import com.survey.lib.common.consts.RespConsts;
import com.survey.lib.common.vo.RespVOBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.concurrent.TimeUnit;

*/
/**
 * @author yanglf
 * @description
 * @since 2019/1/24
 **//*

@Component
public class RequestFilter implements Ordered, GlobalFilter {

    @Autowired
    private RedisTemplate<String, Integer> redisTemplate;

    private String PREX = "request_limit_";

    private int limitCount = 20;


    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest();
        String path = request.getURI().getPath();
        String host = request.getRemoteAddress().getHostString();
        Boolean hasKey = redisTemplate.hasKey(PREX + host + "_" + path);
        ValueOperations<String, Integer> ops = redisTemplate.opsForValue();
        if (hasKey == null || !hasKey) {
            ops.set(PREX + host + "_" + path,1,1, TimeUnit.MINUTES);
            return chain.filter(exchange);
        }

        Integer requestNum = ops.get(PREX + host + "_" + path);
        if(requestNum!=null && requestNum > limitCount){
            return RouteUtil.writeAndReturn(exchange, RespVOBuilder.failure(RespConsts.ERROR_SERVER_CODE,"请求数超限"));
        }
        ops.increment(PREX + host + "_" + path,1);

        return chain.filter(exchange);
    }

    @Override
    public int getOrder() {
        return 0;
    }
}
*/
