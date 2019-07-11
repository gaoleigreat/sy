package com.lego.survey.base.exception;

import com.survey.lib.common.consts.RespConsts;
import com.survey.lib.common.exception.*;
import com.survey.lib.common.vo.RespVO;
import com.survey.lib.common.vo.RespVOBuilder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.actuate.endpoint.invoke.ParameterMappingException;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import java.util.Set;

/**
 * @author yanglf
 * @description
 * @since 2018/12/22
 **/
@Slf4j
@ControllerAdvice
public class GlobalException {


    @ExceptionHandler(value = ParameterMappingException.class)
    @ResponseStatus(value = HttpStatus.OK)
    @ResponseBody
    public RespVO handleException(ParameterMappingException ex){
        log.error("参数缺失:{}",ex);
        return RespVOBuilder.failure(RespConsts.ERROR_OTHER_CODE,"参数["+ex.getParameter().getName()+"]缺失");
    }



    @ExceptionHandler(value = MissingServletRequestParameterException.class)
    @ResponseStatus(value = HttpStatus.OK)
    @ResponseBody
    public RespVO handleException(MissingServletRequestParameterException ex){
        log.error("参数缺失:{}",ex);
        return RespVOBuilder.failure(RespConsts.ERROR_OTHER_CODE,"参数["+ex.getParameterName()+"]缺失");
    }


    @ExceptionHandler(value = ConstraintViolationException.class)
    @ResponseStatus(value = HttpStatus.OK)
    @ResponseBody
    public RespVO handleException(ConstraintViolationException ex){
        log.error("参数检验失败:{}",ex);
        Set<ConstraintViolation<?>> constraintViolations = ex.getConstraintViolations();
        for (ConstraintViolation<?> violation : constraintViolations) {
            return RespVOBuilder.failure(RespConsts.FAIL_RESULT_CODE,violation.getMessage());
        }
        return RespVOBuilder.failure(RespConsts.FAIL_RESULT_CODE,"参数校验失败");
    }


    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    @ResponseStatus(value = HttpStatus.OK)
    @ResponseBody
    public RespVO handleException(MethodArgumentNotValidException ex){
        log.error("参数校验失败:{}",ex);
        String message = ex.getBindingResult().getAllErrors().get(0).getDefaultMessage();
        return RespVOBuilder.failure(RespConsts.FAIL_RESULT_CODE,message);
    }


    @ExceptionHandler(value = HttpMessageNotReadableException.class)
    @ResponseStatus(value = HttpStatus.OK)
    @ResponseBody
    public RespVO handleException(HttpMessageNotReadableException ex){
        log.error("数据解析异常:{}",ex);
        return RespVOBuilder.failure(RespConsts.DATA_ERROR,ex.getMessage());
    }


    @ExceptionHandler(value = DuplicateKeyException.class)
    @ResponseStatus(value = HttpStatus.OK)
    @ResponseBody
    public RespVO handleException(DuplicateKeyException ex){
        log.error("插入数据主键重复异常:{}",ex);
        return RespVOBuilder.failure(RespConsts.DATA_ERROR,ex.getMessage());
    }



    @ExceptionHandler(value = OperateFailException.class)
    @ResponseStatus(value = HttpStatus.OK)
    @ResponseBody
    public RespVO handleException(OperateFailException ex){
        log.error("操作失败:{}",ex);
        return RespVOBuilder.failure(RespConsts.FAIL_RESULT_CODE,ex.getMessage());
    }


    @ExceptionHandler(value = SessionTimeoutException.class)
    @ResponseStatus(value = HttpStatus.OK)
    @ResponseBody
    public RespVO handleException(SessionTimeoutException ex){
        log.error("登录超时:{}",ex);
        return RespVOBuilder.failure(RespConsts.FAIL_LOGIN_CODE,ex.getMessage());
    }


    @ExceptionHandler(value = UnregisteredException.class)
    @ResponseStatus(value = HttpStatus.OK)
    @ResponseBody
    public RespVO handleException(UnregisteredException ex){
        log.error("未注册:{}",ex);
        return RespVOBuilder.failure(RespConsts.FAIL_LOGIN_CODE,ex.getMessage());
    }

    @ExceptionHandler(value = UnAuthorizationException.class)
    @ResponseStatus(value = HttpStatus.OK)
    @ResponseBody
    public RespVO handleException(UnAuthorizationException ex){
        log.error("权限缺失:{}",ex);
        return RespVOBuilder.failure(RespConsts.FAIL_NOPRESSION_CODE, RespConsts.FAIL_NOPRESSION_MSG);
    }


    @ExceptionHandler(value = CallTimeoutException.class)
    @ResponseStatus(value = HttpStatus.OK)
    @ResponseBody
    public RespVO handleException(CallTimeoutException ex){
        log.error("调用失败:{}",ex);
        return RespVOBuilder.failure(RespConsts.ERROR_CALLTIMEOUT_CODE,ex.getMessage());
    }



    @ExceptionHandler(value = ServerException.class)
    @ResponseStatus(value = HttpStatus.OK)
    @ResponseBody
    public RespVO handleException(ServerException ex){
        log.error("服务异常:{}",ex);
        return RespVOBuilder.failure(RespConsts.ERROR_SERVER_CODE,ex.getMessage());
    }


    @ExceptionHandler(value = Exception.class)
    @ResponseStatus(value = HttpStatus.OK)
    @ResponseBody
    public RespVO handleException(Exception ex){
        log.error("服务器异常:{}",ex);
        return RespVOBuilder.failure(RespConsts.ERROR_SERVER_CODE,ex.getMessage());
    }
}
