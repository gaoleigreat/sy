package com.lego.survey.base.filter;/*
package com.lego.survey.lib.web.filter;

import org.springframework.core.annotation.Order;
import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

*/
/**
 * @author yanglf
 * @description
 * @since 2019/1/25
 **//*

@Order(1)
@WebFilter(filterName = "requestReplacedFilter", urlPatterns = "/**")
public class RequestReplacedFilter implements Filter {

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        ServletRequest requestWrapper = null;
        ServletResponse responseWrapper=null;
//        logger.debug("复制request.getInputStream流");
        if (servletRequest instanceof HttpServletRequest) {
            requestWrapper = new RequestWrapper((HttpServletRequest) servletRequest);
        }
        //获取request请求中的流，将取出来的字符串保存在缓存中，同时再将该字符串再次转换成流，然后把它放入到新request对象中。
        if (requestWrapper == null) {
            filterChain.doFilter(servletRequest, servletResponse);
        } else {
            filterChain.doFilter(requestWrapper, servletResponse);
        }
    }

    @Override
    public void destroy() {

    }
}
*/
