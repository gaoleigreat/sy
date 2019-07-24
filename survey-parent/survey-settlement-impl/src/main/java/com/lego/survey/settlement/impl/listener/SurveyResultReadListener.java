package com.lego.survey.settlement.impl.listener;

import com.alibaba.excel.read.context.AnalysisContext;
import com.lego.survey.base.exception.ExceptionBuilder;
import com.lego.survey.event.settlement.SurveyPointResultSource;
import com.lego.survey.lib.excel.listener.ExcelListener;
import com.lego.survey.project.model.entity.Section;
import com.lego.survey.settlement.impl.service.ISurveyPointService;
import com.lego.survey.settlement.impl.service.ISurveyResultService;
import com.lego.survey.settlement.model.entity.SurveyResult;
import com.lego.survey.settlement.model.vo.SurveyPointVo;
import com.lego.survey.settlement.model.vo.SurveyResultVo;
import com.survey.lib.common.consts.DictConstant;
import com.survey.lib.common.consts.RespConsts;
import com.survey.lib.common.utils.HeaderUtils;
import com.survey.lib.common.utils.HttpUtils;
import com.survey.lib.common.utils.SnowflakeIdUtils;
import com.survey.lib.common.vo.HeaderVo;
import com.survey.lib.common.vo.RespVO;
import com.survey.lib.common.vo.RespVOBuilder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.integration.support.MessageBuilder;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author yanglf
 * @description
 * @since 2019/1/19
 **/
@Component
@Slf4j
public class SurveyResultReadListener extends ExcelListener<SurveyResultVo> {

    private List<SurveyResultVo> surveyResultVos = new ArrayList<>();

    @Autowired
    private ISurveyResultService iSurveyResultService;

    private String sectionCode;

    @Autowired
    private SurveyPointResultSource surveyPointResultSource;


    @Override
    public void invoke(SurveyResultVo surveyResultVo, AnalysisContext analysisContext) {
        log.info("read line :{}", surveyResultVo);
        surveyResultVos.add(surveyResultVo);
    }

    @Override
    public void doAfterAllAnalysed(AnalysisContext analysisContext) {
        if (CollectionUtils.isEmpty(surveyResultVos)) {
            return;
        }
        try {
            saveSurveyResults(surveyResultVos);
        } finally {
            surveyResultVos.clear();
        }
    }

    private void saveSurveyResults(List<SurveyResultVo> surveyResultVos) {
        List<SurveyResult> surveyResults = new ArrayList<>();
        if (surveyResultVos != null) {
            surveyResultVos.forEach(surveyResultVo -> {
                SurveyResult surveyResult = surveyResultVo.getSurveyResult();
                setDefaultValue(surveyResult);
                surveyResults.add(surveyResult);
            });
        }
        //TODO 校验权限
        if (CollectionUtils.isEmpty(surveyResults)) {
            return;
        }
        iSurveyResultService.createBatch(surveyResults, DictConstant.TableNamePrefix.SURVEY_RESULT + sectionCode);
        // 发送上传成果 消息
        surveyPointResultSource.uploadResult().send(MessageBuilder.withPayload(surveyResults).setHeader("type", 2).setHeader("sectionCode", sectionCode).build());


    }


    private void setDefaultValue(SurveyResult surveyResult) {
        if (surveyResult.getId() == null) {
            surveyResult.setId(SnowflakeIdUtils.createId());
        }
        Date currentTime = new Date();
        if (surveyResult.getUploadTime() == null) {
            surveyResult.setUploadTime(currentTime);
        }
        if (surveyResult.getSurveyTime() == null) {
            surveyResult.setSurveyTime(currentTime);
        }
    }


    public void setTableName(String sectionCode) {
        this.sectionCode = sectionCode;
    }
}
