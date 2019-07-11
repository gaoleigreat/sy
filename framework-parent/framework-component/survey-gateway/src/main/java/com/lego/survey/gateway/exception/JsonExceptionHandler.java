package com.lego.survey.gateway.exception;/*
package com.lego.survey.gateway.exception;

import com.survey.lib.common.consts.RespConsts;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.web.ErrorProperties;
import org.springframework.boot.autoconfigure.web.ResourceProperties;
import org.springframework.boot.autoconfigure.web.reactive.error.DefaultErrorWebExceptionHandler;
import org.springframework.boot.web.reactive.error.ErrorAttributes;
import org.springframework.context.ApplicationContext;
import org.springframework.http.HttpStatus;
import org.springframework.web.reactive.function.server.*;

import java.util.HashMap;
import java.util.Map;

*/
/**
 * @author yanglf
 * @description gateway 全局异常
 * @since 2018/12/22
 **//*


@Slf4j
public class JsonExceptionHandler extends DefaultErrorWebExceptionHandler {


    public JsonExceptionHandler(ErrorAttributes errorAttributes, ResourceProperties resourceProperties, ErrorProperties errorProperties, ApplicationContext applicationContext) {
        super(errorAttributes, resourceProperties, errorProperties, applicationContext);
    }

    @Override
    protected Map<String, Object> getErrorAttributes(ServerRequest request, boolean includeStackTrace) {
        int code = 500;
        Throwable error = super.getError(request);
        error.printStackTrace();
        if (error instanceof org.springframework.cloud.gateway.support.NotFoundException) {
            code = 404;
        }
        return response(code, this.buildMessage(request, error));
    }

    */
/**
     * 指定响应处理方法为JSON处理的方法
     *
     * @param errorAttributes
     **//*

    @Override
    protected RouterFunction<ServerResponse> getRoutingFunction(ErrorAttributes errorAttributes) {
        return RouterFunctions.route(RequestPredicates.all(), this::renderErrorResponse);
    }

    */
/**
     * 根据code获取对应的HttpStatus
     *
     * @param errorAttributes
     **//*

    @Override
    protected HttpStatus getHttpStatus(Map<String, Object> errorAttributes) {
        int statusCode = (int) errorAttributes.get("retCode");
        return HttpStatus.valueOf(500);
    }

    */
/**
     * 构建异常信息
     *
     * @param request
     * @param ex
     * @return
     **//*

    private String buildMessage(ServerRequest request, Throwable ex) {
        StringBuilder message = new StringBuilder("Failed to handle request [");
        message.append(request.methodName());
        message.append(" ");
        message.append(request.uri());
        message.append("]");
        if (ex != null) {
            message.append(": ");
            message.append(ex.getMessage());
        }
        return message.toString();
    }

    */
/**
     * 构建返回的JSON数据格式
     *
     * @param status       状态码
     * @param errorMessage 异常信息
     * @return
     **//*

    public static Map<String, Object> response(int status, String errorMessage) {
        log.error("status:{},errorMsg:{}", status, errorMessage);
        Map<String, Object> map = new HashMap<>();
        map.put("ret", "fail");
        //map.put("httpStatus", status);
        map.put("retCode", RespConsts.ERROR_SERVER_CODE);
        map.put("msg", errorMessage);
        map.put("data", null);
        return map;
    }

}
*/
