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
     * @param sectionCode
     * @return
     */
    @RequestMapping(value = "/create/{sectionCode}", method = RequestMethod.POST)
    RespVO create(@RequestBody SurveyOriginalVo surveyOriginalVo,
                  @PathVariable(value = "sectionCode") String sectionCode
    );


    /**
     * 查看原始数据
     * @param sectionCode
     * @param pageIndex
     * @param pageSize
     * @return
     */
    @RequestMapping(value = "/list/{sectionCode}/{pageIndex}", method = RequestMethod.GET)
    RespVO<RespDataVO<SurveyOriginalVo>> query(
            @PathVariable(value = "sectionCode") String sectionCode,
            @PathVariable(value = "pageIndex") int pageIndex,
            @RequestParam("pageSize") int pageSize
    );


    /**
     * 修改原始数据
     * @param surveyOriginalVo
     * @param sectionCode
     * @return
     */
    @RequestMapping(value = "/modify/{sectionCode}", method = RequestMethod.PUT)
    RespVO modify(@RequestBody SurveyOriginalVo surveyOriginalVo,
                  @PathVariable(value = "sectionCode") String sectionCode
    );


    /**
     * 删除原始数据
     * @param sectionCode
     * @param ids
     * @return
     */
    @RequestMapping(value = "/delete/{sectionCode}", method = RequestMethod.DELETE)
    RespVO delete(
            @PathVariable(value = "sectionCode") String sectionCode,
            @RequestParam("ids") List<Long> ids
    );


    /**
     * 批量添加原始数据
     * @param surveyOriginalVos
     * @param sectionCode
     * @return
     */
    @RequestMapping(value = "/createBatch/{sectionCode}", method = RequestMethod.POST)
    RespVO createBatch(@RequestBody List<SurveyOriginalVo> surveyOriginalVos,
                       @PathVariable(value = "sectionCode") String sectionCode);




    /**
     * 查看熱無原始数据
     * @param sectionCode
     * @param taskId
     * @return
     */
    @RequestMapping(value = "/list/{sectionCode}", method = RequestMethod.GET)
    RespVO<RespDataVO<SurveyOriginalVo>> queryByTaskId(
            @PathVariable(value = "sectionCode") String sectionCode,
            @RequestParam("taskId") Long taskId
    );


}


@Component
class SurveyOriginalClientFallback implements SurveyOriginalClient{

    @Override
    public RespVO create(SurveyOriginalVo surveyOriginalVo, String sectionCode) {
        return RespVOBuilder.failure(RespConsts.ERROR_SERVER_CODE,"settlement服务不可用");
    }

    @Override
    public RespVO<RespDataVO<SurveyOriginalVo>> query(String sectionCode, int pageIndex, int pageSize) {
        return RespVOBuilder.failure(RespConsts.ERROR_SERVER_CODE,"settlement服务不可用");
    }

    @Override
    public RespVO modify(SurveyOriginalVo surveyOriginalVo, String sectionCode) {
        return RespVOBuilder.failure(RespConsts.ERROR_SERVER_CODE,"settlement服务不可用");
    }

    @Override
    public RespVO delete(String sectionCode, List<Long> ids) {
        return RespVOBuilder.failure(RespConsts.ERROR_SERVER_CODE,"settlement服务不可用");
    }

    @Override
    public RespVO createBatch(List<SurveyOriginalVo> surveyOriginalVos, String sectionCode) {
        return RespVOBuilder.failure(RespConsts.ERROR_SERVER_CODE,"settlement服务不可用");
    }

    @Override
    public RespVO<RespDataVO<SurveyOriginalVo>> queryByTaskId(String sectionCode, Long taskId) {
        return RespVOBuilder.failure(RespConsts.ERROR_SERVER_CODE,"settlement服务不可用");
    }
}
