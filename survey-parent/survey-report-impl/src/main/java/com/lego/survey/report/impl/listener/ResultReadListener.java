package com.lego.survey.report.impl.listener;
import com.alibaba.excel.read.context.AnalysisContext;
import com.lego.survey.base.exception.ExceptionBuilder;
import com.lego.survey.settlement.feign.SurveyResultClient;
import com.lego.survey.settlement.model.vo.SurveyResultVo;
import com.lego.survey.lib.excel.listener.ExcelListener;
import com.survey.lib.common.consts.RespConsts;
import com.survey.lib.common.vo.RespVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * @author yanglf
 * @description
 * @since 2019/1/19
 **/
@Component
@Slf4j
public class ResultReadListener extends ExcelListener<SurveyResultVo> {

    private List<SurveyResultVo> surveyResultVos = new ArrayList<>();

    @Autowired
    private SurveyResultClient surveyResultClient;


    @Override
    public void invoke(SurveyResultVo surveyResultVo, AnalysisContext analysisContext) {
        log.info("read line :{}", surveyResultVo.toString());
        surveyResultVos.add(surveyResultVo);
    }

    @Override
    public void doAfterAllAnalysed(AnalysisContext analysisContext) {
        if (surveyResultVos == null || surveyResultVos.size() <= 0) {
            return;
        }
        try {
            RespVO respVO = surveyResultClient.createBatch(surveyResultVos);
            if (respVO.getRetCode() == RespConsts.SUCCESS_RESULT_CODE) {
                log.info("insert excel success");
            }
        } catch (DuplicateKeyException ex) {
            ex.printStackTrace();
            ExceptionBuilder.duplicateKeyException("主键冲突", 0L);
            log.info("insert excel fail");
        }finally {
            surveyResultVos.clear();
        }
    }
}
