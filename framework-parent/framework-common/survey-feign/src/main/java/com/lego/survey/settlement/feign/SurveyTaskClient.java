package com.lego.survey.settlement.feign;

import com.lego.survey.settlement.model.vo.SurveyTaskVo;
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
@FeignClient(value = DictConstant.Service.SETTLEMENT, path = DictConstant.Path.SURVEY_TASK, fallback = SurveyTaskClientFallback.class)
public interface SurveyTaskClient {


    /**
     * 新增任务
     *
     * @param surveyTaskVo
     * @return
     */
    @RequestMapping(value = "/create", method = RequestMethod.POST)
    RespVO create(@RequestBody SurveyTaskVo surveyTaskVo
    );


    /**
     * 查看任务列表
     *
     * @param sectionCode
     * @param pageIndex
     * @param pageSize
     * @return
     */
    @RequestMapping(value = "/list/{sectionCode}/{pageIndex}", method = RequestMethod.GET)
    RespVO<RespDataVO<SurveyTaskVo>> query(
            @PathVariable(value = "sectionCode") String sectionCode,
            @PathVariable(value = "pageIndex") int pageIndex,
            @RequestParam("pageSize") int pageSize);


    /**
     * 修改任务信息
     *
     * @param surveyTaskVo
     * @param sectionCode
     * @return
     */
    @RequestMapping(value = "/modify/{sectionCode}", method = RequestMethod.PUT)
    RespVO modify(@RequestBody SurveyTaskVo surveyTaskVo,
                  @PathVariable(value = "sectionCode") String sectionCode
    );


    /**
     * 删除任务
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
class SurveyTaskClientFallback implements SurveyTaskClient {

    @Override
    public RespVO create(SurveyTaskVo surveyTaskVo) {
        return RespVOBuilder.failure(RespConsts.ERROR_SERVER_CODE, "settlement服务不可用");
    }

    @Override
    public RespVO<RespDataVO<SurveyTaskVo>> query(String sectionCode, int pageIndex, int pageSize) {
        return RespVOBuilder.failure(RespConsts.ERROR_SERVER_CODE, "settlement服务不可用");
    }

    @Override
    public RespVO modify(SurveyTaskVo surveyTaskVo, String sectionCode) {
        return RespVOBuilder.failure(RespConsts.ERROR_SERVER_CODE, "settlement服务不可用");
    }

    @Override
    public RespVO delete(String sectionCode, List<Long> ids) {
        return RespVOBuilder.failure(RespConsts.ERROR_SERVER_CODE, "settlement服务不可用");
    }
}
