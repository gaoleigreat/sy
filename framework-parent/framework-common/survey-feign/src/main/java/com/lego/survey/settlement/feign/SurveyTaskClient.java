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
@FeignClient(value = DictConstant.Service.SETTLEMENT, path = DictConstant.Path.SURVEY_TASK,fallback = SurveyTaskClientFallback.class)
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
     * @param sectionId
     * @param pageIndex
     * @param pageSize
     * @return
     */
    @RequestMapping(value = "/list/{sectionId}/{pageIndex}", method = RequestMethod.GET)
    RespVO<RespDataVO<SurveyTaskVo>> query(
            @PathVariable(value = "sectionId") String sectionId,
            @PathVariable(value = "pageIndex") int pageIndex,
            @RequestParam("pageSize") int pageSize);


    /**
     * 修改任务信息
     *
     * @param surveyTaskVo
     * @param sectionId
     * @return
     */
    @RequestMapping(value = "/modify/{sectionId}", method = RequestMethod.PUT)
    RespVO modify(@RequestBody SurveyTaskVo surveyTaskVo,
                  @PathVariable(value = "sectionId") String sectionId
    );


    /**
     * 删除任务
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
class SurveyTaskClientFallback implements SurveyTaskClient{

    @Override
    public RespVO create(SurveyTaskVo surveyTaskVo) {
        return RespVOBuilder.failure(RespConsts.ERROR_SERVER_CODE,"settlement服务不可用");
    }

    @Override
    public RespVO<RespDataVO<SurveyTaskVo>> query(String sectionId, int pageIndex, int pageSize) {
        return RespVOBuilder.failure(RespConsts.ERROR_SERVER_CODE,"settlement服务不可用");
    }

    @Override
    public RespVO modify(SurveyTaskVo surveyTaskVo, String sectionId) {
        return RespVOBuilder.failure(RespConsts.ERROR_SERVER_CODE,"settlement服务不可用");
    }

    @Override
    public RespVO delete(String sectionId, List<Long> ids) {
        return RespVOBuilder.failure(RespConsts.ERROR_SERVER_CODE,"settlement服务不可用");
    }
}
