package com.lego.survey.user.feign;

import com.lego.survey.user.model.vo.LogVo;
import com.survey.lib.common.consts.DictConstant;
import com.survey.lib.common.consts.RespConsts;
import com.survey.lib.common.vo.RespDataVO;
import com.survey.lib.common.vo.RespVO;
import com.survey.lib.common.vo.RespVOBuilder;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @author yanglf
 * @description
 * @since 2018/12/26
 **/
@FeignClient(value = DictConstant.Service.USER,  path = DictConstant.Path.LOG, fallback = LogClientFallback.class)
public interface LogClient {


    /**
     * 查询操作日志
     *
     * @param pageIndex
     * @param pageSize
     * @return
     */
    @RequestMapping(value = "/list/{pageIndex}", method = RequestMethod.GET)
    RespVO<RespDataVO<LogVo>> query(@PathVariable(value = "pageIndex") int pageIndex,
                                    @RequestParam("pageSize") int pageSize);

}

@Component
class LogClientFallback implements LogClient {

    @Override
    public RespVO<RespDataVO<LogVo>> query(int pageIndex, int pageSize) {
        return RespVOBuilder.failure(RespConsts.ERROR_SERVER_CODE, "user服务不可用");
    }
}
