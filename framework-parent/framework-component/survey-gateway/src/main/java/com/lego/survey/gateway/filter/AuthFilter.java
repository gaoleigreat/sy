package com.lego.survey.gateway.filter;
import com.lego.survey.auth.feign.AuthClient;
import com.lego.survey.gateway.model.JwtPatternUrl;
import com.lego.survey.gateway.utils.RouteUtil;
import com.survey.lib.common.consts.HttpConsts;
import com.survey.lib.common.consts.RespConsts;
import com.survey.lib.common.vo.CurrentVo;
import com.survey.lib.common.vo.RespVO;
import com.survey.lib.common.vo.RespVOBuilder;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.HttpHeaders;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;
import java.util.List;

/**
 * @author yanglf
 * @descript
 * @since 2018/12/20
 **/
@Component
@Slf4j
public class AuthFilter implements GlobalFilter, Ordered {

    @Autowired
    private AuthClient authClient;

    @Autowired
    private JwtPatternUrl jwtPatternUrl;

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest();
        String path = request.getURI().getPath();
        if(!path.contains("/survey")){
            return chain.filter(exchange).then(Mono.fromRunnable(() -> {
            }));
        }
        // url 是否需要 token 认证
        List<String> urlPatterns = jwtPatternUrl.getUrlPatterns();
        for (String urlPattern : urlPatterns) {
            if (path.endsWith(urlPattern)) {
                return chain.filter(exchange).then(Mono.fromRunnable(() -> {
                }));
            }
        }
        // TODO 判断 url 是否需要鉴权
        HttpHeaders headers = exchange.getRequest().getHeaders();
        String userToken = headers.getFirst(HttpConsts.HEADER_TOKEN);
        String deviceType = headers.getFirst(HttpConsts.DEVICE_TYPE);
        log.info("userToken:{}", userToken);
        if (StringUtils.isBlank(userToken)) {
            // 登录超时
            RespVO<Object> respVO = RespVOBuilder.failure(RespConsts.FAIL_LOGIN_CODE, "登录失败");
            return RouteUtil.writeAndReturn(exchange,respVO);
        }

        RespVO<CurrentVo> currentVoRespVO = authClient.parseUserToken(userToken, deviceType);
        if (currentVoRespVO.getRetCode()!=RespConsts.SUCCESS_RESULT_CODE) {
           //登录超时
            RespVO<Object> respVO = RespVOBuilder.failure(RespConsts.FAIL_LOGIN_CODE, "登录失败");
            return RouteUtil.writeAndReturn(exchange,respVO);
        }
        log.info("authVo:{}", currentVoRespVO.getInfo());
        ServerHttpRequest build = exchange.getRequest().mutate().header("access-auth-uid", currentVoRespVO.getInfo().getUserId()).build();
        ServerWebExchange tokenExchange = exchange.mutate().request(build).build();
        return chain.filter(tokenExchange).then(Mono.fromRunnable(() -> {


        }));
    }

    @Override
    public int getOrder() {
        return 2;
    }
}
