package com.lego.survey.base.filter;
import com.lego.survey.base.context.RequestContext;
import com.survey.lib.common.consts.HttpConsts;
import com.survey.lib.common.vo.CurrentVo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.stereotype.Component;
import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author yanglf
 */
@Component
@ServletComponentScan
@WebFilter(filterName = "sessionFilter", urlPatterns = "/*")
public class SessionFilter implements Filter {

    private static final Logger log = LoggerFactory.getLogger(SessionFilter.class);

    @Value("${spring.application.name}")
    private String serviceName;

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        log.info("sessionFilter init... ...");
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        //设置服务key
        try{
            HttpServletRequest req;
            HttpServletResponse res;
            if(servletRequest instanceof HttpServletRequest){
                req = (HttpServletRequest) servletRequest;
                res = (HttpServletResponse) servletResponse;
                req.setCharacterEncoding("utf-8");
                res.setCharacterEncoding("utf-8");
                setRequestContext(req);
            }

            filterChain.doFilter(servletRequest,servletResponse);
        }catch (Exception e){
            log.error("", e);
        }finally {
            RequestContext.remove();
        }
    }


    private void setRequestContext(HttpServletRequest req) {
        CurrentVo current = RequestContext.initCurrent();
        String token = req.getHeader(HttpConsts.HEADER_TOKEN);
        String deviceType = req.getHeader(HttpConsts.DEVICE_TYPE);
        current.setToken(token);
        current.setDeviceType(deviceType);
    }



    @Override
    public void destroy() {

        log.info("sessionFilter destory... ...");
    }
}
