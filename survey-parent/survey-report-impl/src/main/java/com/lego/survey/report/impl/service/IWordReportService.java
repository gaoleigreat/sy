package com.lego.survey.report.impl.service;
import com.lego.survey.report.model.entity.TemplateReport;
import com.lego.survey.settlement.model.vo.SurveyReportVo;
import com.survey.lib.common.vo.RespDataVO;
import com.survey.lib.common.vo.RespVO;

/**
 * @author yanglf
 * @description
 * @since 2019/1/7
 **/
public interface IWordReportService {
    SurveyReportVo getSurveyReportVo(String workspaceCode);


}
