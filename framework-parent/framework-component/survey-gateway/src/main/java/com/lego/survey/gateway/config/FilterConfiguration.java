/*
package com.lego.survey.gateway.config;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpHeaders;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import reactor.core.publisher.Mono;
*/
/**
 * @author yanglf
 * @descript
 * @since 2018/12/18
 **//*

@Configuration
@Slf4j
public class FilterConfiguration {

    */
/**
     * 请求日志处理
     * @return
     *//*

    @Bean
    @Order(-1)
    public GlobalFilter logFilter() {
        return ((exchange, chain) -> {
            ServerHttpRequest request = exchange.getRequest();
            log.info("请求url:{}",request.getURI());
            return chain.filter(exchange).then(Mono.fromRunnable((Runnable) () -> {
                ServerHttpResponse response = exchange.getResponse();
                log.info("请求响应码:{}",response.getStatusCode().value());
            }));
        });
    }


    */
/**
     * 黑名单
     * @return
     *//*

    @Bean
    @Order(0)
    public GlobalFilter requestFilter(){
        return ((exchange, chain) -> {
            ServerHttpRequest request = exchange.getRequest();
            String hostAddress = request.getRemoteAddress().getHostString();
            log.info("请求的ip:{}",hostAddress);
           */
/* if("192.168.101.197".equals(hostAddress)){
                try {
                    exchange.getAttributes().put(GATEWAY_REQUEST_URL_ATTR,new URI("http://www.baidu.com"));
                } catch (URISyntaxException e) {
                    e.printStackTrace();
                }
            }*//*

            return chain.filter(exchange).then(Mono.fromRunnable((Runnable) () -> {
            }));
        });
    }


    */
/**
     * 添加  header 拦截器
     * @return
     *//*

    @Bean
    @Order(3)
    public GatewayFilter addHeaderFilter(){
        return ((exchange, chain) -> {
            ServerHttpRequest request = exchange.getRequest();
            HttpHeaders headers = request.getHeaders();
           */
/* List<String> token = headers.get(HttpConsts.HEADER_TOKEN);
           exchange.getRequest().mutate().header()*//*


            return chain.filter(exchange).then(Mono.fromRunnable((Runnable) () ->{

            }));
        });
    }







}
*/
