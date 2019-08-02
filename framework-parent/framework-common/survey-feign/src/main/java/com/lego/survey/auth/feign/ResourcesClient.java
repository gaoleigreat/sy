package com.lego.survey.auth.feign;

import com.lego.survey.auth.model.entity.Resources;
import com.survey.lib.common.consts.DictConstant;
import com.survey.lib.common.consts.RespConsts;
import com.survey.lib.common.vo.RespVO;
import com.survey.lib.common.vo.RespVOBuilder;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * @author yanglf
 * @description
 * @since 2019/7/10
 **/
@FeignClient(value = DictConstant.Service.AUTH,path = DictConstant.Path.RESOURCES,fallback = ResourcesClientFallback.class)
public interface ResourcesClient {


    /**
     * 用户权限点增量保存
     * @param scope
     * @param resources
     * @return
     */
    @RequestMapping(value = "/save", method = RequestMethod.POST)
    RespVO saveUserPermissionList(@RequestParam("scope")String scope, @RequestBody List<Resources> resources);

}


@Component
class ResourcesClientFallback implements ResourcesClient{

    @Override
    public RespVO saveUserPermissionList(String scope, List<Resources> resources) {
        return RespVOBuilder.failure(RespConsts.ERROR_SERVER_CODE,"auth服务不可用");
    }
}
