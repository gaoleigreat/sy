package com.lego.survey.gateway.filter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.actuate.metrics.MetricsEndpoint;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;
import java.util.Objects;

/**
 * @author yanglf
 * @description
 * @since 2018/12/27
 **/
@Component
@Slf4j
public class RateLimitFilter implements GlobalFilter, Ordered {

    private static final String METRICS_NAME = "system.cpu.usage";
    @Autowired
    private MetricsEndpoint metricsEndpoint;

    private  static final Double MAX_USAGE=0.50D;

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        Double systemCpuUsage = metricsEndpoint.metric(METRICS_NAME, null)
                .getMeasurements().stream().filter(Objects::nonNull)
                .findFirst().map(MetricsEndpoint.Sample::getValue)
                .filter(Double::isFinite)
                .orElse(0.0);
        boolean isOpenRateLimit = systemCpuUsage > MAX_USAGE;
        log.info("system cpu usage:{},isOpenRateLimit:{}",systemCpuUsage,isOpenRateLimit);
        if(isOpenRateLimit){
            exchange.getResponse().setStatusCode(HttpStatus.TOO_MANY_REQUESTS);
            return exchange.getResponse().setComplete();
        }else{
            return chain.filter(exchange);
        }
    }

    @Override
    public int getOrder() {
        return 1;
    }
}
