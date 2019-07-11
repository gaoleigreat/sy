package com.lego.survey.base.exception;

import com.survey.lib.common.consts.RespConsts;
import com.survey.lib.common.exception.*;
import com.survey.lib.common.vo.DataErrorVo;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.http.converter.HttpMessageNotReadableException;

/**
 * @author yanglf
 * @description
 * @since 2018/12/22
 **/
public class ExceptionBuilder {
    /**
     * 资源不存在
     * @param resource 资源名称
     * @param pk 资源唯一标识
     * @param val 资源唯一标识值
     * @return
     */
    public static void resourceNotFound(String resource, String pk, Object val) {
        throw new ResourceNotFoundException(resource + "[" + pk + "=" + val + "] not found.");
    }


    /**
     * 服务异常
     * @param message
     */
    public static void  serviceException(String message){
        throw  new ServerException(message);
    }

    /**
     * 未注册异常
     */
    public static void unregisteredException(){
        throw new UnregisteredException("账号未注册");
    }


    /**
     *  登录超时异常
     */
    public static void sessionTimeoutException(){
        throw new SessionTimeoutException("登录失败");
    }

    public  static  void  unAuthorizationException(){
        throw new UnAuthorizationException("权限缺失");
    }


    public static  void  unKnownException(){
        throw new UnKnownException("未知异常");
    }


    /**
     * 操作失败异常
     * @param message
     */
    public static  void  operateFailException(String message){
        throw new OperateFailException(message);
    }


    /**
     * 主键重复异常
     * @param message
     * @param id
     */
    public  static  void  duplicateKeyException(String message,Long id){
        DataErrorVo dataErrorVo=new DataErrorVo();
        dataErrorVo.setType(RespConsts.DataErrorType.DATA_TYPE_ERROR);
        dataErrorVo.setDesc(message);
        dataErrorVo.setId(id);
        throw new DuplicateKeyException(dataErrorVo.toJsonObject());

    }


    /**
     * 数据格式异常
     * @param message
     * @param id
     */
    public static void httpMessageNotReadableException(String message,Long id){
        DataErrorVo dataErrorVo=new DataErrorVo();
        dataErrorVo.setType(RespConsts.DataErrorType.DATA_TYPE_ERROR);
        dataErrorVo.setDesc(message);
        dataErrorVo.setId(id);
        throw new HttpMessageNotReadableException(dataErrorVo.toJsonObject());
    }




}
