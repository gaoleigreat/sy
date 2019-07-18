package com.lego.survey.settlement.impl.listener;

import com.alibaba.excel.read.context.AnalysisContext;
import com.lego.survey.base.exception.ExceptionBuilder;
import com.lego.survey.lib.excel.listener.ExcelListener;
import com.lego.survey.settlement.feign.SurveyPointClient;
import com.lego.survey.settlement.impl.service.ISurveyPointService;
import com.lego.survey.settlement.model.vo.SurveyPointVo;
import com.survey.lib.common.consts.DictConstant;
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
public class SurveyPointReadListener extends ExcelListener<SurveyPointVo> {

    private List<SurveyPointVo> surveyPointVos = new ArrayList<>();

    @Autowired
    private ISurveyPointService iSurveyPointService;

    private String sectionId;


    @Override
    public void invoke(SurveyPointVo surveyPointVo, AnalysisContext analysisContext) {
        log.info("read line :{}", surveyPointVo);
        surveyPointVos.add(surveyPointVo);
    }

    @Override
    public void doAfterAllAnalysed(AnalysisContext analysisContext) {
        if (surveyPointVos == null || surveyPointVos.size() <= 0) {
            return;
        }
        try {
            RespVO respVO = iSurveyPointService.createBatch(surveyPointVos, DictConstant.TableNamePrefix.SURVEY_POINT+sectionId);
            if (respVO.getRetCode() == RespConsts.SUCCESS_RESULT_CODE) {
                log.info("insert excel success");
            }
        } catch (DuplicateKeyException ex) {
            ex.printStackTrace();
            ExceptionBuilder.duplicateKeyException("主键冲突", 0L);
            log.info("insert excel fail");
        }finally {
            surveyPointVos.clear();
        }
    }

    public void setTableName(String sectionId) {
        this.sectionId=sectionId;
    }
}
