package com.lego.survey.settlement.feign;

import com.lego.survey.settlement.model.vo.SurveyPointTypeVo;
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

/**
 * @author yanglf
 * @description
 * @since 2019/1/14
 **/
@FeignClient(value = DictConstant.Service.SETTLEMENT,  path = DictConstant.Path.SURVEY_POINT_TYPE, fallback = SurveyPointTypeClientFallback.class)
public interface SurveyPointTypeClient {

    /**
     * 新增测点类型
     *
     * @param spType
     * @return
     */
    @RequestMapping(value = "/create", method = RequestMethod.POST)
    RespVO create(@RequestBody SurveyPointTypeVo spType);


    /**
     * 获取测点类型列表
     *
     * @param sectionCode
     * @return
     */
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    RespVO<RespDataVO<SurveyPointTypeVo>> list(@RequestParam("sectionCode") String sectionCode
    );


    /**
     * 删除测点类型
     *
     * @param id
     * @return
     */
    @RequestMapping(value = "/delete", method = RequestMethod.DELETE)
    RespVO delete(@RequestParam("id") Long id);


    /**
     * 修改测点类型
     *
     * @param surveyPointTypeVo
     * @return
     */
    @RequestMapping(value = "/modify", method = RequestMethod.PUT)
    RespVO modify(@RequestBody SurveyPointTypeVo surveyPointTypeVo);


}

@Component
class SurveyPointTypeClientFallback implements SurveyPointTypeClient {

    @Override
    public RespVO create(SurveyPointTypeVo spType) {
        return RespVOBuilder.failure(RespConsts.ERROR_SERVER_CODE, "settlement服务不可用");
    }

    @Override
    public RespVO<RespDataVO<SurveyPointTypeVo>> list(String sectionCode) {
        return RespVOBuilder.failure(RespConsts.ERROR_SERVER_CODE, "settlement服务不可用");
    }

    @Override
    public RespVO delete(Long id) {
        return RespVOBuilder.failure(RespConsts.ERROR_SERVER_CODE, "settlement服务不可用");
    }

    @Override
    public RespVO modify(SurveyPointTypeVo surveyPointTypeVo) {
        return RespVOBuilder.failure(RespConsts.ERROR_SERVER_CODE, "settlement服务不可用");
    }
}
