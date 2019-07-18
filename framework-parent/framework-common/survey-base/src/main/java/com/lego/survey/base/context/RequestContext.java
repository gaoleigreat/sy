package com.lego.survey.base.context;
import com.lego.survey.base.service.IAuthCheckService;
import com.survey.lib.common.vo.CurrentVo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.web.context.ContextLoader;
import org.springframework.web.context.WebApplicationContext;

import javax.servlet.ServletContext;

public class RequestContext {

    public static final Logger log = LoggerFactory.getLogger(RequestContext.class);

    private static ThreadLocal<CurrentVo> current = new ThreadLocal<CurrentVo>();

    private  static IAuthCheckService authCheckService;


    public static void setCurrnet(CurrentVo cur) {
        current.set(cur);
    }

    public static CurrentVo getCurrent() {
        CurrentVo currentVo = current.get();
        try {
            if(currentVo!=null && currentVo.getToken()!=null && currentVo.getDeviceType()!=null){
                String token = currentVo.getToken();
                String deviceType = currentVo.getDeviceType();
                if(null == authCheckService){
                    authCheckService = (IAuthCheckService) AppContext.getContext().getBean("authCheckService");
                }
                return authCheckService.getData(token,deviceType);

            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return currentVo;
    }

    public static CurrentVo getBaseCurrent(){
        CurrentVo cur = current.get();
        return cur;
    }

    public static CurrentVo initCurrent(){
        current.remove();
        current.set(new CurrentVo());

        return current.get();
    }

    public static void remove(){
        current.remove();
    }

    public static ApplicationContext getApplicationContext() {

        return AppContext.getContext();
    }

    public static WebApplicationContext getWebApplicationContext() {

        return ContextLoader.getCurrentWebApplicationContext();
    }

    public static ServletContext getServletContext() {
        WebApplicationContext wac = getWebApplicationContext();
        if (null == wac) {
            return null;
        }
        return wac.getServletContext();
    }
//
//    @Override
//    public void afterPropertiesSet() throws Exception {
//        authCheckService = (IAuthCheckService) AppContext.getContext().getBean("authCheckService");
//    }
}
