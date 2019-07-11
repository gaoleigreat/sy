package com.lego.survey.settlement.feign;

import com.lego.survey.settlement.model.vo.SurveyOriginalVo;
import com.survey.lib.common.consts.DictConstant;
import com.survey.lib.common.consts.RespConsts;
import com.survey.lib.common.vo.RespDataVO;
import com.survey.lib.common.vo.RespVO;
import com.survey.lib.common.vo.RespVOBuilder;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author yanglf
 * @description
 * @since 2019/1/8
 **/
@FeignClient(value = DictConstant.Service.SETTLEMENT,path = DictConstant.Path.SURVEY_ORIGINAL,fallback = SurveyOriginalClientFallback.class)
public interface SurveyOriginalClient {

    /**
     * 新增原始数据
     * @param surveyOriginalVo
     * @param sectionId
     * @return
     */
    @RequestMapping(value = "/create/{sectionId}", method = RequestMethod.POST)
    RespVO create(@RequestBody SurveyOriginalVo surveyOriginalVo,
                  @PathVariable(value = "sectionId") String sectionId
    );


    /**
     * 查看原始数据
     * @param sectionId
     * @param pageIndex
     * @param pageSize
     * @return
     */
    @RequestMapping(value = "/list/{sectionId}/{pageIndex}", method = RequestMethod.GET)
    RespVO<RespDataVO<SurveyOriginalVo>> query(
            @PathVariable(value = "sectionId") String sectionId,
            @PathVariable(value = "pageIndex") int pageIndex,
            @RequestParam("pageSize") int pageSize
    );


    /**
     * 修改原始数据
     * @param surveyOriginalVo
     * @param sectionId
     * @return
     */
    @RequestMapping(value = "/modify/{sectionId}", method = RequestMethod.PUT)
    RespVO modify(@RequestBody SurveyOriginalVo surveyOriginalVo,
                  @PathVariable(value = "sectionId") String sectionId
    );


    /**
     * 删除原始数据
     * @param sectionId
     * @param ids
     * @return
     */
    @RequestMapping(value = "/delete/{sectionId}", method = RequestMethod.DELETE)
    RespVO delete(
            @PathVariable(value = "sectionId") String sectionId,
            @RequestParam("ids") List<Long> ids
    );


    /**
     * 批量添加原始数据
     * @param surveyOriginalVos
     * @param sectionId
     * @return
     */
    @RequestMapping(value = "/createBatch/{sectionId}", method = RequestMethod.POST)
    RespVO createBatch(@RequestBody List<SurveyOriginalVo> surveyOriginalVos,
                       @PathVariable(value = "sectionId") String sectionId);

}


@Component
class SurveyOriginalClientFallback implements SurveyOriginalClient{

    @Override
    public RespVO create(SurveyOriginalVo surveyOriginalVo, String sectionId) {
        return RespVOBuilder.failure(RespConsts.ERROR_SERVER_CODE,"settlement服务不可用");
    }

    @Override
    public RespVO<RespDataVO<SurveyOriginalVo>> query(String sectionId, int pageIndex, int pageSize) {
        return RespVOBuilder.failure(RespConsts.ERROR_SERVER_CODE,"settlement服务不可用");
    }

    @Override
    public RespVO modify(SurveyOriginalVo surveyOriginalVo, String sectionId) {
        return RespVOBuilder.failure(RespConsts.ERROR_SERVER_CODE,"settlement服务不可用");
    }

    @Override
    public RespVO delete(String sectionId, List<Long> ids) {
        return RespVOBuilder.failure(RespConsts.ERROR_SERVER_CODE,"settlement服务不可用");
    }

    @Override
    public RespVO createBatch(List<SurveyOriginalVo> surveyOriginalVos, String sectionId) {
        return RespVOBuilder.failure(RespConsts.ERROR_SERVER_CODE,"settlement服务不可用");
    }
}
