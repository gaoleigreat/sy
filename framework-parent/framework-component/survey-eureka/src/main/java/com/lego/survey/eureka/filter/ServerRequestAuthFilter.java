package com.lego.survey.eureka.filter;

import com.alibaba.fastjson.JSONObject;
import com.lego.survey.eureka.model.Instance;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.annotation.Order;
import org.springframework.util.StringUtils;

import javax.jws.soap.InitParam;
import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.annotation.WebInitParam;
import javax.servlet.http.HttpServletRequest;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;

/**
 * @author yanglf
 * @description
 * @since 2019/7/16
 **/
@Slf4j
@WebFilter(filterName = "authFilter", urlPatterns = "/*",initParams = {
        @WebInitParam(name = "black_list",value = "192.168.101.103,",description = "blackList")
})
@Order(1)
public class ServerRequestAuthFilter implements Filter {
    String blackList;

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        blackList= filterConfig.getInitParameter("black_list");
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) request;
        String requestURI = req.getRequestURI();
        String parma = getParma(req);
        if(!StringUtils.isEmpty(parma) && !StringUtils.isEmpty(blackList)){
            Instance intstance=getInstance(parma);
            Instance.InstanceBean instanceBean = intstance.getInstance();
            String ipAddr = instanceBean.getIpAddr();
            String[] split = blackList.split(",");
            if(Arrays.asList(split).contains(ipAddr)){
                return;
            }
        }
        chain.doFilter(request, response);
    }

    private Instance getInstance(String parm) {
       return JSONObject.parseObject(parm,Instance.class);
    }

    @Override
    public void destroy() {

    }


    private String getParma(HttpServletRequest request) {
        BufferedReader br = null;
        try {
            br = new BufferedReader(new InputStreamReader(request.getInputStream(), "UTF-8"));
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        String line = null;
        StringBuilder sb = new StringBuilder();
        try {
            while ((line = br.readLine()) != null) {
                sb.append(line);
            }
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return sb.toString();
    }

}
