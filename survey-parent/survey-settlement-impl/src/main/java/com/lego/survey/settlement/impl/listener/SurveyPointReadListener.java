package com.lego.survey.settlement.impl.listener;

import com.alibaba.excel.read.context.AnalysisContext;
import com.lego.survey.base.exception.ExceptionBuilder;
import com.lego.survey.lib.excel.listener.ExcelListener;
import com.lego.survey.settlement.feign.SurveyPointClient;
import com.lego.survey.settlement.impl.service.ISurveyPointService;
import com.lego.survey.settlement.impl.service.ISurveyPointTypeService;
import com.lego.survey.settlement.model.entity.SurveyPointType;
import com.lego.survey.settlement.model.vo.SurveyPointVo;
import com.survey.lib.common.consts.DictConstant;
import com.survey.lib.common.consts.RespConsts;
import com.survey.lib.common.vo.RespVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

    @Autowired
    private ISurveyPointTypeService iSurveyPointTypeService;

    private String sectionCode;

    private Map<String, Integer> statusMap = new HashMap<>();


    @Override
    public void invoke(SurveyPointVo surveyPointVo, AnalysisContext analysisContext) {
        log.info("read line :{}", surveyPointVo);
        String typeStr = surveyPointVo.getTypeStr();
        SurveyPointType surveyPointType = iSurveyPointTypeService.queryByName(typeStr, sectionCode);
        if (surveyPointType != null) {
            surveyPointVo.setType(surveyPointType.getId());
        }

        String statusStr = surveyPointVo.getStatusStr();
        if (!StringUtils.isEmpty(statusStr)) {
            surveyPointVo.setStatus(statusMap.get(statusStr));
        }
        surveyPointVos.add(surveyPointVo);
    }

    @Override
    public void doAfterAllAnalysed(AnalysisContext analysisContext) {
        if (surveyPointVos == null || surveyPointVos.size() <= 0) {
            return;
        }
        try {
            RespVO respVO = iSurveyPointService.createBatch(surveyPointVos, DictConstant.TableNamePrefix.SURVEY_POINT + sectionCode);
            if (respVO.getRetCode() == RespConsts.SUCCESS_RESULT_CODE) {
                log.info("insert excel success");
            }
        } catch (DuplicateKeyException ex) {
            ex.printStackTrace();
            ExceptionBuilder.duplicateKeyException("主键冲突", 0L);
            log.info("insert excel fail");
        } finally {
            surveyPointVos.clear();
        }
    }

    public void setTableName(String sectionCode) {
        this.sectionCode = sectionCode;
        //1：正常 2: 新建 3: 停测 4: 破坏
        statusMap.put("正常", 1);
        statusMap.put("新建", 2);
        statusMap.put("停测", 3);
        statusMap.put("破坏", 4);
    }
}
