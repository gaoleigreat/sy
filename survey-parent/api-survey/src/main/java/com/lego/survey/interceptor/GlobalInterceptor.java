package com.lego.survey.interceptor;
import com.lego.survey.auth.feign.AuthClient;
import com.lego.survey.base.exception.ExceptionBuilder;
import com.survey.lib.common.consts.DictConstant;
import com.survey.lib.common.consts.RespConsts;
import com.survey.lib.common.utils.HeaderUtils;
import com.survey.lib.common.vo.CurrentVo;
import com.survey.lib.common.vo.HeaderVo;
import com.survey.lib.common.vo.RespVO;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * @author yanglf
 * @description
 * @since 2018/12/21
 **/
@Slf4j
public class GlobalInterceptor extends HandlerInterceptorAdapter {

    @Autowired
    private AuthClient authClient;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        String uri = request.getRequestURI();
        if(uri.endsWith("login")){
            return true;
        }
        HeaderVo headerVo = HeaderUtils.parseHeader(request);
        if(headerVo==null || StringUtils.isBlank(headerVo.getToken())){
            ExceptionBuilder.sessionTimeoutException();
            return false;
        }
        String deviceType = headerVo.getDeviceType();
        String token = headerVo.getToken();
        RespVO<CurrentVo> currentVoRespVO = authClient.parseUserToken(token, deviceType);
        if(currentVoRespVO.getRetCode()!=RespConsts.SUCCESS_RESULT_CODE){
            // token 解析异常
            ExceptionBuilder.sessionTimeoutException();
            return false;
        }
        String role = currentVoRespVO.getInfo().getRole();
        List<String> permissions = currentVoRespVO.getInfo().getPermissions();
        request.setAttribute("currentVo",currentVoRespVO.getInfo());
        //  TODO  权限校验
        if(uri.endsWith(DictConstant.RolePath.PROJECT_CREATE) || uri.endsWith(DictConstant.RolePath.PROJECT_DELETE)){
            if(!role.equals(DictConstant.Role.ADMIN)){
                ExceptionBuilder.unAuthorizationException();
                return false;
            }
        }
        return true;
    }


    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        super.postHandle(request, response, handler, modelAndView);
    }


    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        super.afterCompletion(request, response, handler, ex);
    }
}
