package com.lego.survey.controller.base;
import com.lego.survey.auth.feign.AuthClient;
import com.lego.survey.base.exception.ExceptionBuilder;
import com.lego.survey.lib.swagger.ApiError;
import com.survey.lib.common.consts.DictConstant;
import com.survey.lib.common.utils.HeaderUtils;
import com.survey.lib.common.vo.HeaderVo;
import com.survey.lib.common.vo.RespVO;
import com.survey.lib.common.vo.TokenVo;
import io.swagger.annotations.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import javax.servlet.http.HttpServletRequest;

/**
 * @author yanglf
 * @description
 * @since 2018/12/21
 **/
@RestController
@RequestMapping(DictConstant.Path.AUTH)
@Api(value = "AuthController", description = "权限控制服务")
@ApiResponses({
        @ApiResponse(response = ApiError.class,code = 404,message = "Resources Not Found")
})
public class AuthController {

    @Autowired
    private AuthClient authClient;


 /*   @ApiOperation(value = "刷新token",httpMethod = "POST",notes = "刷新token")
    @ApiImplicitParams({

    })
    @RequestMapping("/refresh")
    public RespVO<TokenVo> refresh(HttpServletRequest request){
        HeaderVo headerVo = HeaderUtils.parseHeader(request);
        if(headerVo==null){
            ExceptionBuilder.unKnownException();
        }
        return authClient.refresh(headerVo.getToken(),headerVo.getDeviceType());
    }*/


}
