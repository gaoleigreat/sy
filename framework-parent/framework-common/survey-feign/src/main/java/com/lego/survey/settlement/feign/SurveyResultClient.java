package com.lego.survey.settlement.feign;

import com.lego.survey.settlement.model.entity.SurveyResult;
import com.lego.survey.settlement.model.vo.SurveyResultVo;
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
@FeignClient(value = DictConstant.Service.SETTLEMENT, path = DictConstant.Path.SURVEY_RESULT, fallback = SurveyResultClientFallback.class)
public interface SurveyResultClient {

    /**
     * 添加测量结果
     *
     * @param surveyResultVo
     * @param sectionCode
     * @return
     */
    @RequestMapping(value = "/create/{sectionCode}", method = RequestMethod.POST)
    RespVO create(@Validated @RequestBody SurveyResultVo surveyResultVo,
                  @PathVariable(value = "sectionCode") String sectionCode
    );

    /**
     * 批量上传测点结果
     *
     * @param surveyResults
     * @return
     */
    @RequestMapping(value = "/uploadBatch", method = RequestMethod.POST)
    RespVO uploadBatch(@RequestBody List<SurveyResult> surveyResults
    );


    /**
     * 批量新增成果数据
     *
     * @param surveyResultVos
     * @return
     */
    @RequestMapping(value = "/createBatch", method = RequestMethod.POST)
    RespVO createBatch(@RequestBody List<SurveyResultVo> surveyResultVos
    );


    /**
     * 查询测量结果列表
     *
     * @param pageIndex
     * @param pageSize
     * @return
     */
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    RespVO<RespDataVO<SurveyResultVo>> list(
            @RequestParam(required = false, defaultValue = "1", value = "pageIndex") int pageIndex,
            @RequestParam(required = false, defaultValue = "10", value = "pageSize") int pageSize,
            @RequestParam(value = "workspaceCode") String workspaceCode,
            @RequestParam(value = "startTimestamp") Long startTimestamp,
            @RequestParam(value = "endTimestamp") Long endTimestamp
    );


    /**
     * 修改成果数据
     *
     * @param surveyResultVo
     * @param sectionCode
     * @return
     */
    @RequestMapping(value = "/modify/{sectionCode}", method = RequestMethod.PUT)
    RespVO modify(@RequestBody SurveyResultVo surveyResultVo,
                  @PathVariable(value = "sectionCode") String sectionCode
    );


    /**
     * 删除成果数据
     *
     * @param sectionCode
     * @param ids
     * @return
     */
    @RequestMapping(value = "/delete/{sectionCode}", method = RequestMethod.DELETE)
    RespVO delete(
            @PathVariable(value = "sectionCode") String sectionCode,
            @RequestParam("ids") List<Long> ids
    );

}

@Component
class SurveyResultClientFallback implements SurveyResultClient {

    @Override
    public RespVO create(SurveyResultVo surveyResultVo, String sectionCode) {
        return RespVOBuilder.failure(RespConsts.ERROR_SERVER_CODE, "settlement服务不可用");
    }

    @Override
    public RespVO uploadBatch(List<SurveyResult> surveyResults) {
        return RespVOBuilder.failure(RespConsts.ERROR_SERVER_CODE, "settlement服务不可用");
    }

    @Override
    public RespVO createBatch(List<SurveyResultVo> surveyResultVos) {
        return RespVOBuilder.failure(RespConsts.ERROR_SERVER_CODE, "settlement服务不可用");
    }

    @Override
    public RespVO<RespDataVO<SurveyResultVo>> list(int pageIndex, int pageSize, String workspaceCode, Long startTimestamp, Long endTimestamp) {
        return RespVOBuilder.failure(RespConsts.ERROR_SERVER_CODE, "settlement服务不可用");
    }

    @Override
    public RespVO modify(SurveyResultVo surveyResultVo, String sectionCode) {
        return RespVOBuilder.failure(RespConsts.ERROR_SERVER_CODE, "settlement服务不可用");
    }

    @Override
    public RespVO delete(String sectionCode, List<Long> ids) {
        return RespVOBuilder.failure(RespConsts.ERROR_SERVER_CODE, "settlement服务不可用");
    }
}
