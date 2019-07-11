package com.lego.survey.settlement.feign;

import com.lego.survey.settlement.model.vo.SurveyPointVo;
import com.survey.lib.common.consts.DictConstant;
import com.survey.lib.common.consts.RespConsts;
import com.survey.lib.common.vo.RespDataVO;
import com.survey.lib.common.vo.RespVO;
import com.survey.lib.common.vo.RespVOBuilder;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author yanglf
 * @description
 * @since 2019/1/8
 **/
@FeignClient(value = DictConstant.Service.SETTLEMENT, path = DictConstant.Path.SURVEY_POINT,fallback = SurveyPointClientFallback.class)
public interface SurveyPointClient {


    /**
     * create survey point
     *
     * @param surveyPointVo
     * @param sectionId
     * @return
     */
    @RequestMapping(value = "/create/{sectionId}", method = RequestMethod.POST)
    RespVO create(@Validated @RequestBody SurveyPointVo surveyPointVo,
                  @PathVariable(value = "sectionId") String sectionId);


    /**批量新增测点
     * @param surveyPointVos
     * @param sectionId
     * @return
     */
    @RequestMapping(value = "/createBatch/{sectionId}", method = RequestMethod.POST)
    RespVO createBatch(@RequestBody List<SurveyPointVo> surveyPointVos,
                       @PathVariable(value = "sectionId") String sectionId
    );


    /**
     * list survey point
     *
     * @param workspaceId
     * @param pageIndex
     * @param pageSize
     * @return
     */
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    RespVO<RespDataVO<SurveyPointVo>> list(
            @RequestParam(value = "workspaceId") String workspaceId,
            @RequestParam(value = "pageIndex", required = false, defaultValue = "1") int pageIndex,
            @RequestParam(value = "pageSize", required = false, defaultValue = "10") int pageSize,
            @RequestParam(required = false, value = "startTimestamp") Long startTimestamp,
            @RequestParam(required = false, value = "endTimestamp") Long endTimestamp
    );


    /**
     * 修改测点信息
     *
     * @param surveyPointVo
     * @param sectionId
     * @return
     */
    @RequestMapping(value = "/modify/{sectionId}", method = RequestMethod.PUT)
    RespVO modify(@RequestBody SurveyPointVo surveyPointVo,
                  @PathVariable(value = "sectionId") String sectionId
    );


    /**
     * 删除测点信息
     *
     * @param sectionId
     * @param ids
     * @return
     */
    @RequestMapping(value = "/delete/{sectionId}", method = RequestMethod.DELETE)
    RespVO delete(
            @PathVariable(value = "sectionId") String sectionId,
            @RequestParam("ids") List<Long> ids
    );

}

@Component
class SurveyPointClientFallback implements SurveyPointClient{

    @Override
    public RespVO create(SurveyPointVo surveyPointVo, String sectionId) {
        return RespVOBuilder.failure(RespConsts.ERROR_SERVER_CODE,"settlement服务不可用");
    }

    @Override
    public RespVO createBatch(List<SurveyPointVo> surveyPointVos, String sectionId) {
        return RespVOBuilder.failure(RespConsts.ERROR_SERVER_CODE,"settlement服务不可用");
    }

    @Override
    public RespVO<RespDataVO<SurveyPointVo>> list(String workspaceId, int pageIndex, int pageSize, Long startTimestamp, Long endTimestamp) {
        return RespVOBuilder.failure(RespConsts.ERROR_SERVER_CODE,"settlement服务不可用");
    }

    @Override
    public RespVO modify(SurveyPointVo surveyPointVo, String sectionId) {
        return RespVOBuilder.failure(RespConsts.ERROR_SERVER_CODE,"settlement服务不可用");
    }

    @Override
    public RespVO delete(String sectionId, List<Long> ids) {
        return RespVOBuilder.failure(RespConsts.ERROR_SERVER_CODE,"settlement服务不可用");
    }
}
