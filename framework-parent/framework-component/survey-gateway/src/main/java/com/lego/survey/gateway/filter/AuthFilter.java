package com.lego.survey.gateway.filter;
import com.lego.survey.auth.feign.AuthClient;
import com.lego.survey.gateway.utils.JwtPatternUrl;
import com.lego.survey.gateway.utils.RouteUtil;
import com.survey.lib.common.consts.HttpConsts;
import com.survey.lib.common.consts.RespConsts;
import com.survey.lib.common.vo.CurrentVo;
import com.survey.lib.common.vo.RespVO;
import com.survey.lib.common.vo.RespVOBuilder;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.cloud.gateway.support.ServerWebExchangeUtils;
import org.springframework.core.Ordered;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
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

    private Logger logger = LoggerFactory.getLogger("access");


    @Autowired
    private JwtPatternUrl jwtPatternUrl;

    @Value("${server.servlet.context-path}")
    private String contextPath;


    @Value("${session.domain}")
    private String authDomain;

    @Autowired
    private AuthClient authClient;


    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest();
        ServerHttpResponse response = exchange.getResponse();
        request.mutate().header("Access-Control-Allow-Origin", request.getHeaders().getOrigin());
        request.mutate().header("Access-Control-Allow-Credentials", "true");

        try {
            StringBuilder sb = new StringBuilder();
            String uri = request.getURI().getPath();
            String remoteIp = getClientIp(request);
            long timeMillis = System.currentTimeMillis();
            String traceInfo = timeMillis + "-" + remoteIp + "-" + uri + "-" + authDomain;
            sb.append(timeMillis).append("\t").append("ACCESS").append("\t")
                    .append(remoteIp).append("\t").append(uri).append("\t");
            logger.info(sb.toString());


            // url 是否需要 token 认证
            Boolean isIgnore = isIgnore(uri);
            logger.info("url:[{}]--------isIgnore:[{}]", uri, isIgnore);
            if (isIgnore) {
                ServerWebExchange webExchange = getExchange(exchange, traceInfo, null);
                return chain.filter(webExchange).then(Mono.fromRunnable(() -> {
                    // 返回参数
                }));
            }

            String userToken = request.getHeaders().getFirst(HttpConsts.HEADER_TOKEN);
            String deviceType = request.getHeaders().getFirst(HttpConsts.DEVICE_TYPE);
            if (!org.springframework.util.StringUtils.isEmpty(userToken) && !org.springframework.util.StringUtils.isEmpty(deviceType)) {
                RespVO<CurrentVo> currentVoRespVO = authClient.parseUserToken(userToken, deviceType);
                if (currentVoRespVO.getRetCode() == RespConsts.SUCCESS_RESULT_CODE) {
                    CurrentVo currentVo = currentVoRespVO.getInfo();
                    if (currentVo != null) {
                        ServerWebExchange webExchange = getExchange(exchange, traceInfo, currentVo);
                        return chain.filter(webExchange).then(Mono.fromRunnable(() -> {
                        }));
                    }
                }
            }
            RespVO<Object> respVO = RespVOBuilder.failure(RespConsts.FAIL_LOGIN_CODE, "登录失败");
            return RouteUtil.writeAndReturn(exchange, respVO);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


    private ServerWebExchange getExchange(ServerWebExchange exchange, String traceInfo, CurrentVo currentVo) {
        ServerHttpRequest build = exchange.getRequest().mutate()
                .header("DOMAIN", authDomain)
                .header("TRACE", traceInfo)
                .build();
        if (currentVo != null) {
            build.mutate().header("userId", currentVo.getUserId());
            build.mutate().header("userName", currentVo.getUserName());
        }
        return exchange.mutate().request(build).build();
    }


    private Boolean isIgnore(String uri) {
        String s = uri.replaceAll(contextPath, "");
        List<String> urlPatterns = jwtPatternUrl.getUrlPatterns();
        if (CollectionUtils.isEmpty(urlPatterns)) {
            return false;
        }
        for (String reg : urlPatterns) {
            if (s.matches(reg)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public int getOrder() {
        return 2;
    }


    private static String getClientIp(ServerHttpRequest request) {
        String ip = request.getHeaders().getFirst("x-forwarded-for");
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeaders().getFirst("Proxy-Client-IP");
        }

        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeaders().getFirst("WL-Proxy-Client-IP");
        }

        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddress().getHostName();
        }

        if (!org.springframework.util.StringUtils.isEmpty(ip)) {
            ip = ip.split(",")[0];
        }

        return ip;
    }


}
