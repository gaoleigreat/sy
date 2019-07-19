package com.lego.survey.zuul.filter;
import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import com.netflix.zuul.exception.ZuulException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.netflix.ribbon.RibbonHttpResponse;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.util.StreamUtils;
import javax.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;

import static org.springframework.cloud.netflix.zuul.filters.support.FilterConstants.POST_TYPE;

@Component
public class PostFilter extends ZuulFilter {

    private static final Logger accessLog = LoggerFactory.getLogger("access");

    @Override
    public String filterType() {
        return POST_TYPE;
    }

    @Override
    public int filterOrder() {
        return 2;
    }

    @Override
    public boolean shouldFilter() {
        RequestContext ctx = RequestContext.getCurrentContext();
        return ctx.sendZuulResponse();
    }

    @Override
    public Object run() throws ZuulException {
        RequestContext ctx = RequestContext.getCurrentContext();
        StringBuilder sb = new StringBuilder();
        HttpServletResponse res = ctx.getResponse();
        String pvId = (String) ctx.get("pvId");
        String body = null;
        try {
            Object zuulResponse = ctx.get("zuulResponse");
            if (zuulResponse != null) {
                RibbonHttpResponse resp = (RibbonHttpResponse) zuulResponse;
                MediaType contentType = resp.getHeaders().getContentType();
                if (contentType != null && contentType.toString().equals(MediaType.APPLICATION_JSON_UTF8_VALUE)) {
                    InputStream stream = ctx.getResponseDataStream();
                    body = StreamUtils.copyToString(stream, Charset.forName("UTF-8"));
                    ctx.setResponseBody(body);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        sb.append(System.currentTimeMillis()).append("\t").append("RETURN").append("\t").append(pvId).append("\t").append(res.getStatus())
                .append("\t").append(body);
        accessLog.info(sb.toString());

        ctx.setSendZuulResponse(true);
        ctx.setResponseStatusCode(200);
        ctx.set("pvId", pvId);
        return null;
    }
}
