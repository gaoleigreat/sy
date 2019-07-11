package com.lego.survey.gateway.utils;

import com.alibaba.fastjson.JSONObject;
import com.survey.lib.common.vo.RespVO;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

/**
 * @author yanglf
 * @description
 * @since 2018/12/25
 **/
public class RouteUtil {


    /**
     * 阻止路由
     *
     * @param exchange
     * @param respVO
     * @return
     */
    public static Mono<Void> writeAndReturn(ServerWebExchange exchange, RespVO respVO) {
        ServerHttpResponse response = exchange.getResponse();
        HttpHeaders respHeaders = response.getHeaders();
        respHeaders.add("Content-Type", "application/json; charset=UTF-8");
        respHeaders.add("Cache-Control", "no-store, no-cache, must-revalidate, max-age=0");
        DataBuffer dataBuffer = response.bufferFactory().wrap(JSONObject.toJSONString(respVO).getBytes());
        return response.writeWith(Mono.just(dataBuffer));
    }


    /**
     * 重定向
     * @param exchange
     * @param url
     * @return
     */
    public static Mono<Void> forward(ServerWebExchange exchange,String url) {
        ServerHttpResponse response = exchange.getResponse();
        response.setStatusCode(HttpStatus.SEE_OTHER);
        response.getHeaders().add("Location",url);
        return response.setComplete();
    }


}
