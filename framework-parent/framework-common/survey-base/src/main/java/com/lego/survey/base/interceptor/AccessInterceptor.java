package com.lego.survey.base.interceptor;
import com.survey.lib.common.utils.HttpUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StreamUtils;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;

/**
 * @author yanglf
 * @description 日志拦截器
 * @since 2018/12/22
 **/
@Slf4j(topic = "access")
public class AccessInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        try {
            log.info(String.format("Request from -> IP: %s, Method: %s, URL: %s, Params: %s, Headers: %s",
                    HttpUtils.getClientIp(request),
                    request.getMethod(),
                    request.getRequestURL().toString(),
                    HttpUtils.getRequestParams(request),
                    HttpUtils.getHeaderVo(request)));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {

    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {

    }
}
