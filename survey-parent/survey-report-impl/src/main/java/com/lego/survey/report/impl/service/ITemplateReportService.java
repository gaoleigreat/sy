package com.lego.survey.report.impl.service;
import com.lego.survey.report.model.entity.TemplateReport;
import com.survey.lib.common.vo.RespDataVO;
import com.survey.lib.common.vo.RespVO;

/**
 * @author yanglf
 * @description
 * @since 2019/1/7
 **/
public interface ITemplateReportService {

    /**
     * 创建报告模板
     *
     * @param templateReport
     * @return
     */
    RespVO create(TemplateReport templateReport);


    /**
     * 删除报告模板
     *
     * @param id
     * @return
     */
    RespVO delete(Long id);


    /**
     * 修改模板信息
     *
     * @param templateReport
     * @return
     */
    RespVO modify(TemplateReport templateReport);


    /**
     * 查询报告模板列表
     *
     * @param pageIndex
     * @param pageSize
     * @return
     */
    RespVO<RespDataVO<TemplateReport>> list(int pageIndex, int pageSize, String sectionCode);


    /**
     * 根据id获取模板信息
     * @param id
     * @return
     */
    RespVO<TemplateReport> queryById(Long id);


    RespVO<TemplateReport> findByName(String name);
}
