package com.lego.survey.zuul.filter;

import com.alibaba.fastjson.JSON;
import com.lego.survey.auth.feign.AuthClient;
import com.lego.survey.zuul.predicate.RibbonVersionHolder;
import com.lego.survey.zuul.utils.JwtPatternUrl;
import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import com.survey.lib.common.consts.HttpConsts;
import com.survey.lib.common.consts.RespConsts;
import com.survey.lib.common.utils.HttpUtils;
import com.survey.lib.common.vo.CurrentVo;
import com.survey.lib.common.vo.RespVO;
import com.survey.lib.common.vo.RespVOBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.security.NoSuchAlgorithmException;
import java.util.List;

import static org.springframework.cloud.netflix.zuul.filters.support.FilterConstants.PRE_TYPE;

/**
 * @author yanglf
 * @description
 * @since 2019/7/3
 **/
@Component
public class LoginFilter extends ZuulFilter {

    private Logger logger= LoggerFactory.getLogger("access");


    @Autowired
    private JwtPatternUrl jwtPatternUrl;

    @Value("${server.servlet.context-path}")
    private String contextPath;


    @Value("${session.domain}")
    private String authDomain;

    @Autowired
    private AuthClient authClient;


    @Override
    public String filterType() {
        return PRE_TYPE;
    }

    @Override
    public int filterOrder() {
        return 1;
    }

    @Override
    public boolean shouldFilter() {
        return true;
    }

    @Override
    public Object run() {
        RequestContext ctx = RequestContext.getCurrentContext();
        HttpServletRequest req = ctx.getRequest();
        HttpServletResponse res = ctx.getResponse();
        StringBuilder sb = new StringBuilder();
        //web 跨域 上线注释
        res.addHeader("Access-Control-Allow-Origin", req.getHeader("Origin"));
        res.addHeader("Access-Control-Allow-Credentials", "true");
        try {
            String uri = req.getRequestURI();
            String remoteIp = HttpUtils.getClientIp(req);
            String localIp = req.getLocalAddr();
            String pvId = HttpUtils.generatePVID(req, remoteIp, localIp);

            sb.append(System.currentTimeMillis()).append("\t").append("ACCESS").append("\t").append(pvId).append("\t")
                    .append(localIp).append("\t").append(remoteIp).append("\t").append(uri).append("\t");
            logger.info(sb.toString());

            // url 是否需要 token 认证
            Boolean isIgnore = isIgnore(uri);
            logger.info("url:[{}]--------isIgnore:[{}]",uri,isIgnore);
            if (isIgnore) {
                setRequest(ctx,null);
                ctx.set("pvId", pvId);
                return null;
            }
            String userToken = req.getHeader(HttpConsts.HEADER_TOKEN);
            String deviceType = req.getHeader(HttpConsts.DEVICE_TYPE);
            String osType = req.getHeader(HttpConsts.OS_VERSION);
            if(!StringUtils.isEmpty(osType)){
                RibbonVersionHolder.setContext(osType);
            }
            if (!StringUtils.isEmpty(userToken) && !StringUtils.isEmpty(deviceType)) {
                RespVO<CurrentVo> currentVoRespVO = authClient.parseUserToken(userToken, deviceType);
                if (currentVoRespVO.getRetCode() == RespConsts.SUCCESS_RESULT_CODE) {
                    CurrentVo currentVo = currentVoRespVO.getInfo();
                    logger.info("url:[{}]--------------currentVo:[{}]",uri,currentVo);
                    if(currentVo!=null){
                        setRequest(ctx,currentVo);
                        ctx.set("pvId", pvId);
                        return null;
                    }
                }
            }
            RespVO<Object> failure = RespVOBuilder.failure(RespConsts.FAIL_LOGIN_CODE,"登录失败");
            ctx.set("pvId", pvId);
            ctx.getResponse().setContentType("application/json;charset=utf-8");
            ctx.setSendZuulResponse(false);
            ctx.setResponseStatusCode(200);
            ctx.setResponseBody(JSON.toJSONString(failure));

            return null;
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return null;
    }

    private Boolean isIgnore(String uri) {
        String s = uri.replaceAll(contextPath, "");
        List<String> urlPatterns = jwtPatternUrl.getUrlPatterns();
        if(CollectionUtils.isEmpty(urlPatterns)){
            return false;
        }
        for (String reg : urlPatterns) {
            if (s.matches(reg)) {
                return true;
            }
        }
        return false;
    }


    private void setRequest(RequestContext ctx,CurrentVo currentVo) {
        ctx.getZuulRequestHeaders().put("DOMAIN", authDomain);
        ctx.setSendZuulResponse(true);
        ctx.setResponseStatusCode(200);
        if(currentVo!=null){
            ctx.addZuulRequestHeader("userId",currentVo.getUserId());
            ctx.addZuulRequestHeader("userName",currentVo.getUserName());
        }
    }


}
