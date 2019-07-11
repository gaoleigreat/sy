package com.lego.survey.base.interceptor;
import com.lego.survey.base.annotation.Operation;
import com.lego.survey.base.annotation.Resource;
import com.lego.survey.base.context.RequestContext;
import com.survey.lib.common.consts.RespConsts;
import com.survey.lib.common.vo.CurrentVo;
import com.survey.lib.common.vo.RespVOBuilder;
import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Collections;

@Component
public class SecurityInterceptor implements MethodInterceptor {

    private static final Logger log = LoggerFactory.getLogger(SecurityInterceptor.class);

    @Value("${spring.application.name}")
    private String scope;

    @Override
    public Object invoke(MethodInvocation invocation) throws Throwable {
        Class<?> c = invocation.getMethod().getDeclaringClass();
        Resource resource = c.getAnnotation(Resource.class);
        Operation operation = invocation.getMethod().getAnnotation(Operation.class);

        if (null != resource && null != operation) {
            CurrentVo currentVo = RequestContext.getCurrent();
            if(null != currentVo &&  !currentVo.isPermissionChecked()){
                String permission = scope + "$" + resource.value() + "$" + operation.value();
                if(null == currentVo.getResourcesScopes() || !currentVo.getResourcesScopes().contains(permission)){
                    Class returnType = invocation.getMethod().getReturnType();
                    if(returnType.getName().equals("java.util.Map")){
                        return RespVOBuilder.failure(RespConsts.FAIL_NOPRESSION_CODE,RespConsts.FAIL_NOPRESSION_MSG);
                    }else if(returnType.getName().equals("com.survey.lib.common.vo.RespVO")){
                        return RespVOBuilder.failure(RespConsts.FAIL_NOPRESSION_CODE,RespConsts.FAIL_NOPRESSION_MSG);
                    }else if(returnType.getName().equals("java.util.List")){
                        return Collections.EMPTY_LIST;
                    }
                    return null;
                }
                currentVo.setPermissionChecked(true);
            }
        }

        return invocation.proceed();
    }

}
