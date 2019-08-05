package com.lego.survey.settlement.impl.listener;

import com.alibaba.excel.read.context.AnalysisContext;
import com.lego.survey.base.exception.ExceptionBuilder;
import com.lego.survey.lib.excel.listener.ExcelListener;
import com.lego.survey.project.feign.SectionClient;
import com.lego.survey.project.model.entity.Section;
import com.lego.survey.settlement.feign.SurveyPointClient;
import com.lego.survey.settlement.impl.service.ISurveyPointService;
import com.lego.survey.settlement.impl.service.ISurveyPointTypeService;
import com.lego.survey.settlement.model.entity.SurveyPointType;
import com.lego.survey.settlement.model.vo.SurveyPointTypeVo;
import com.lego.survey.settlement.model.vo.SurveyPointVo;
import com.survey.lib.common.consts.DictConstant;
import com.survey.lib.common.consts.RespConsts;
import com.survey.lib.common.vo.RespVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.validation.constraints.NotBlank;
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

    @Autowired
    private SectionClient sectionClient;

    private Map<String, Integer> statusMap = new HashMap<>();
    private String workspaceCode;
    private String sectionCode;


    @Override
    public void invoke(SurveyPointVo surveyPointVo, AnalysisContext analysisContext) {
        log.info("read line :{}", surveyPointVo);
        String typeStr = surveyPointVo.getTypeStr();
        RespVO<Section> sectionRespVO = sectionClient.queryByWorkspaceCode(workspaceCode);
        if (sectionRespVO.getRetCode() != RespConsts.SUCCESS_RESULT_CODE) {
            return;
        }
        sectionCode = sectionRespVO.getInfo().getCode();
        SurveyPointType surveyPointType = iSurveyPointTypeService.queryByName(typeStr, sectionCode);
        if (surveyPointType != null) {
            surveyPointVo.setType(surveyPointType.getId());
            SurveyPointTypeVo surveyPointTypeVo = surveyPointType.loadVo();
            surveyPointVo.setOnceLowerLimit(surveyPointTypeVo.getSettleSpeedLowerLimit());
            surveyPointVo.setOnceUpperLimit(surveyPointTypeVo.getSingleSettleUpperLimit());
            surveyPointVo.setTotalLowerLimit(surveyPointTypeVo.getTotalSettleLowerLimit());
            surveyPointVo.setTotalUpperLimit(surveyPointTypeVo.getTotalSettleUpperLimit());
            surveyPointVo.setSpeedLowerLimit(surveyPointTypeVo.getSettleSpeedLowerLimit());
            surveyPointVo.setSpeedUpperLimit(surveyPointTypeVo.getSettleSpeedUpperLimit());
        }

        String statusStr = surveyPointVo.getStatusStr();
        if (!StringUtils.isEmpty(statusStr)) {
            surveyPointVo.setStatus(statusMap.get(statusStr));
        }
        surveyPointVo.setWorkspaceCode(workspaceCode);
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

    public void setWorkspaceCode(String workspaceCode) {
        this.workspaceCode = workspaceCode;
        //1：正常 2: 新建 3: 停测 4: 破坏
        statusMap.put("正常", 1);
        statusMap.put("新建", 2);
        statusMap.put("停测", 3);
        statusMap.put("破坏", 4);
    }
}
