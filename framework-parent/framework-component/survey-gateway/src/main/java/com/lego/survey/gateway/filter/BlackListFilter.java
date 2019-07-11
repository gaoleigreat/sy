package com.lego.survey.gateway.filter;

import com.lego.survey.gateway.utils.RouteUtil;
import com.survey.lib.common.consts.RespConsts;
import com.survey.lib.common.vo.RespVOBuilder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;
import java.net.InetSocketAddress;
/**
 * @author yanglf
 * @description
 * @since 2019/1/16
 **/
@Component
@Slf4j
public class BlackListFilter implements Ordered, GlobalFilter {

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest();
        InetSocketAddress remoteAddress = request.getRemoteAddress();
        if(remoteAddress!=null){
            String hostAddress = remoteAddress.getHostString();
            String blackPrefix = "black_ip_";
            Boolean hasKey = stringRedisTemplate.hasKey(blackPrefix +hostAddress);
            if(hasKey!=null && hasKey){
               return RouteUtil.writeAndReturn(exchange, RespVOBuilder.failure(RespConsts.ERROR_SERVER_CODE,"请求失败"));
            }
        }
        return chain.filter(exchange).then(Mono.fromRunnable(() -> {}));
    }

    @Override
    public int getOrder() {
        return 10;
    }
}
