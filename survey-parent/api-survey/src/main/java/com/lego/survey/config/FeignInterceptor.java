package com.lego.survey.config;
import com.survey.lib.common.vo.AuthVo;
import feign.RequestInterceptor;
import feign.RequestTemplate;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import javax.servlet.http.HttpServletRequest;
import java.util.Enumeration;

/**
 * @author yanglf
 * @description
 * @since 2018/12/22
 **/
@Configuration
public class FeignInterceptor implements RequestInterceptor {

    @Override
    public void apply(RequestTemplate requestTemplate) {
        requestTemplate.header("fromName","api");
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder
                .getRequestAttributes();
        if(attributes==null){
            return;
        }
        HttpServletRequest request = attributes.getRequest();
        Enumeration<String> headerNames = request.getHeaderNames();
        AuthVo authVo = (AuthVo) request.getAttribute("authVo");
        if (authVo != null && authVo.getCurrentVo()!=null) {
            requestTemplate.query("userId",authVo.getCurrentVo().getUserId());
        }
        while (headerNames.hasMoreElements()) {
            String headerName = headerNames.nextElement();
            String headerValue = request.getHeader(headerName);
            requestTemplate.header(headerName,headerValue);
        }

    }
}
