package com.lego.survey.settlement.feign;
import com.lego.survey.settlement.model.vo.SurveyPointExceptionVo;
import com.survey.lib.common.consts.DictConstant;
import com.survey.lib.common.consts.RespConsts;
import com.survey.lib.common.vo.RespDataVO;
import com.survey.lib.common.vo.RespVO;
import com.survey.lib.common.vo.RespVOBuilder;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * @author yanglf
 * @description
 * @since 2019/1/9
 **/
@FeignClient(value = DictConstant.Service.SETTLEMENT,path = DictConstant.Path.SURVEY_POINT_EXCEPTION,fallback = SurveyPointExceptionClientFallback.class)
public interface SurveyPointExceptionClient {

    /**
     * 新增异常报警信息
     * @param surveyPointExceptionVo
     * @return
     */
    @RequestMapping(value = "/create", method = RequestMethod.POST)
    RespVO create(@Validated @RequestBody SurveyPointExceptionVo surveyPointExceptionVo);


    /**
     * 查询异常报告列表
     * @param pageIndex
     * @param pageSize
     * @param sectionCode
     * @param pointCode
     * @return
     */
    @RequestMapping(value = "/list/{pageIndex}", method = RequestMethod.GET)
    RespVO<RespDataVO<SurveyPointExceptionVo>> query(@PathVariable(value = "pageIndex") int pageIndex,
                                                     @RequestParam(required = false, defaultValue = "10", value = "pageSize") int pageSize,
                                                     @RequestParam(required = false, value = "sectionCode") String sectionCode,
                                                     @RequestParam(required = false, value = "pointCode") String pointCode);


    /**
     * 删除异常警告
     * @param id
     * @return
     */
    @RequestMapping(value = "/delete", method = RequestMethod.DELETE)
    RespVO delete(@RequestParam("id") Long id);


    /**
     * 修改异常警告
     * @param surveyPointExceptionVo
     * @return
     */
    @RequestMapping(value = "/modify", method = RequestMethod.PUT)
    RespVO modify(@RequestBody SurveyPointExceptionVo surveyPointExceptionVo);


}

@Component
class  SurveyPointExceptionClientFallback implements SurveyPointExceptionClient{

    @Override
    public RespVO create(SurveyPointExceptionVo surveyPointExceptionVo) {
        return RespVOBuilder.failure(RespConsts.ERROR_SERVER_CODE,"settlement服务不可用");
    }

    @Override
    public RespVO<RespDataVO<SurveyPointExceptionVo>> query(int pageIndex, int pageSize, String sectionCode, String pointCode) {
        return RespVOBuilder.failure(RespConsts.ERROR_SERVER_CODE,"settlement服务不可用");
    }

    @Override
    public RespVO delete(Long id) {
        return RespVOBuilder.failure(RespConsts.ERROR_SERVER_CODE,"settlement服务不可用");
    }

    @Override
    public RespVO modify(SurveyPointExceptionVo surveyPointExceptionVo) {
        return RespVOBuilder.failure(RespConsts.ERROR_SERVER_CODE,"settlement服务不可用");
    }
}
