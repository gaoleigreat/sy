package com.lego.survey.report.feign;

import com.lego.survey.report.model.entity.TemplateReport;
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
 * @since 2019/1/7
 **/
@FeignClient(value = DictConstant.Service.REPORT, path = DictConstant.Path.TEMPLATE_REPORT, fallback = TemplateReportClientFallback.class)
public interface TemplateReportClient {

    /**
     * 新增报告模板
     *
     * @param templateReport
     * @return
     */
    @RequestMapping(value = "/create", method = RequestMethod.POST)
    RespVO create(@Validated @RequestBody TemplateReport templateReport);


    /**
     * 获取模板列表
     *
     * @param pageIndex
     * @param pageSize
     * @param sectionCode
     * @return
     */
    @RequestMapping(value = "/list/{pageIndex}", method = RequestMethod.GET)
    RespVO<RespDataVO<TemplateReport>> list(@PathVariable(value = "pageIndex") int pageIndex,
                                            @RequestParam("pageSize") int pageSize,
                                            @RequestParam(value = "sectionCode", required = false) String sectionCode);


    /**
     * 修改模板信息
     *
     * @param templateReport
     * @return
     */
    @RequestMapping(value = "/modify", method = RequestMethod.PUT)
    RespVO modify(@RequestBody TemplateReport templateReport);


    /**
     * 删除模板信息
     *
     * @param id
     * @return
     */
    @RequestMapping(value = "/delete", method = RequestMethod.DELETE)
    RespVO delete(@RequestParam("id") Long id);

}

@Component
class TemplateReportClientFallback implements TemplateReportClient {

    @Override
    public RespVO create(TemplateReport templateReport) {
        return RespVOBuilder.failure(RespConsts.ERROR_SERVER_CODE, "report服务不可用");
    }

    @Override
    public RespVO<RespDataVO<TemplateReport>> list(int pageIndex, int pageSize, String sectionCode) {
        return RespVOBuilder.failure(RespConsts.ERROR_SERVER_CODE, "report服务不可用");
    }

    @Override
    public RespVO modify(TemplateReport templateReport) {
        return RespVOBuilder.failure(RespConsts.ERROR_SERVER_CODE, "report服务不可用");
    }

    @Override
    public RespVO delete(Long id) {
        return RespVOBuilder.failure(RespConsts.ERROR_SERVER_CODE, "report服务不可用");
    }
}
