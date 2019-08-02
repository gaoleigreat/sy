package com.lego.survey.settlement.feign;

import com.lego.survey.settlement.model.entity.BasePoint;
import com.lego.survey.settlement.model.vo.BasePointVo;
import com.survey.lib.common.consts.DictConstant;
import com.survey.lib.common.consts.RespConsts;
import com.survey.lib.common.vo.RespDataVO;
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
 * @since 2019/1/7
 **/
@FeignClient(value = DictConstant.Service.SETTLEMENT,path = DictConstant.Path.BASE_POINT,fallback = BasePointClientFallback.class)
public interface BasePointClient {

    /**
     * 新增基准点
     *
     * @param basePoint base point
     * @return create base point result
     */
    @RequestMapping(value = "/create", method = RequestMethod.POST)
    RespVO create(@RequestBody BasePoint basePoint);


    /**
     * 查询基准点列表
     * @param sectionCode
     * @param name
     * @param startTimestamp
     * @param endTimestamp
     * @return
     */
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    RespVO<RespDataVO<BasePointVo>> query(@RequestParam(value = "sectionCode") String sectionCode,
                                          @RequestParam(required = false, value = "name") String name,
                                          @RequestParam(required = false, value = "startTimestamp") Long startTimestamp,
                                          @RequestParam(required = false, value = "endTimestamp") Long endTimestamp
    );

    /**
     * 修改基准点信息
     *
     * @param basePoint
     * @return
     */
    @RequestMapping(value = "/modify", method = RequestMethod.PUT)
    RespVO modify(@RequestBody BasePoint basePoint);


    /**
     * 删除基准点信息
     *
     * @param codes
     * @return
     */
    @RequestMapping(value = "/delete", method = RequestMethod.DELETE)
    RespVO delete(@RequestParam("codes") List<String> codes);

}

@Component
class BasePointClientFallback implements BasePointClient{

    @Override
    public RespVO create(BasePoint basePoint) {
        return RespVOBuilder.failure(RespConsts.ERROR_SERVER_CODE,"settlement服务不可用");
    }

    @Override
    public RespVO<RespDataVO<BasePointVo>> query(String sectionCode, String name, Long startTimestamp, Long endTimestamp) {
        return RespVOBuilder.failure(RespConsts.ERROR_SERVER_CODE,"settlement服务不可用");
    }

    @Override
    public RespVO modify(BasePoint basePoint) {
        return RespVOBuilder.failure(RespConsts.ERROR_SERVER_CODE,"settlement服务不可用");
    }

    @Override
    public RespVO delete(List<String> codes) {
        return RespVOBuilder.failure(RespConsts.ERROR_SERVER_CODE,"settlement服务不可用");
    }
}
