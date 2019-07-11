package com.lego.survey.settlement.impl.listener;
import com.lego.survey.event.settlement.SurveyPointResultSink;
import com.lego.survey.project.feign.SectionClient;
import com.lego.survey.project.model.vo.SectionVo;
import com.lego.survey.settlement.impl.service.ISurveyPointExceptionService;
import com.lego.survey.settlement.impl.service.ISurveyPointService;
import com.lego.survey.settlement.impl.service.ISurveyResultService;
import com.lego.survey.settlement.model.entity.SurveyResult;
import com.lego.survey.settlement.model.vo.SurveyPointExceptionVo;
import com.lego.survey.settlement.model.vo.SurveyPointVo;
import com.survey.lib.common.consts.DictConstant;
import com.survey.lib.common.consts.RespConsts;
import com.survey.lib.common.utils.CalculationUtils;
import com.survey.lib.common.utils.SnowflakeIdUtils;
import com.survey.lib.common.vo.RespVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;
import java.util.Date;
import java.util.List;

/**
 * @author yanglf
 * @description
 * @since 2019/1/21
 **/
@Slf4j
@EnableBinding(SurveyPointResultSink.class)
public class SurveyPointResultListener {

    @Autowired
    private ISurveyResultService iSurveyResultService;

    @Autowired
    private ISurveyPointService iSurveyPointService;

    @Autowired
    private ISurveyPointExceptionService iSurveyPointExceptionService;

    @Autowired
    private SectionClient sectionClient;

    /**
     * 接受批量上传 测量结果 事件
     *
     * @param surveyResults result  list
     * @param sectionId     section id
     */
    @StreamListener(value = SurveyPointResultSink.INPUT_RESULT, condition = "headers['type']==2")
    public void batchUpdateResult(@Payload List<SurveyResult> surveyResults, @Header String sectionId) {
        log.info("接收到上传成果数据:{},sectionId:{}", surveyResults, sectionId);
        if (surveyResults == null) {
            return;
        }
        surveyResults.forEach(surveyResult -> checkResultData(surveyResult, sectionId));
    }


    /**
     * 处理 上传 单个测量成果 数据  事件
     *
     * @param surveyResult result
     * @param sectionId    section id
     */
    @StreamListener(value = SurveyPointResultSink.INPUT_RESULT, condition = "headers['type']==1")
    public void uploadResult(@Payload SurveyResult surveyResult, @Header String sectionId) {
        log.info("接收到上传成果数据:{},sectionId:{}", surveyResult, sectionId);
        checkResultData(surveyResult, sectionId);
    }


    /**
     * 判断 测量成果  是否 超限
     *
     * @param surveyResult
     * @param sectionId
     */
    private void checkResultData(SurveyResult surveyResult, String sectionId) {
        SurveyPointVo surveyPointVo = iSurveyPointService.querySurveyPointByCode(surveyResult.getPointCode(), DictConstant.TableNamePrefix.SURVEY_POINT + sectionId);
        Date surveyTime = surveyResult.getSurveyTime();
        Date uploadTime = surveyResult.getUploadTime();
        Double currElevation = surveyResult.getElevation();
        double preElevation = 0;
        double onceSettlement = 0;
        double preSettlement = 0;
        boolean onceSettlementIsOver = false;
        double totalSettlement = 0;
        double preTotalSettlement = 0;
        boolean totalSettlementIsOver = false;
        double settlementRate = 0;
        double preSettlementRate = 0;
        boolean settlementRateIsOver = false;
        List<SurveyResult> preResults = iSurveyResultService.queryPreResult(surveyTime, DictConstant.TableNamePrefix.SURVEY_RESULT + sectionId, 2);
        if (preResults != null && preResults.size() > 0) {
            SurveyResult preResult = preResults.get(0);
            preElevation = preResult.getElevation();
            // 计算 当前单次沉降量 = 本次高程 - 上次高程
            onceSettlement = CalculationUtils.settlementDiff(currElevation, preElevation);
            log.info("当前单次沉降量:{}=当前高程{}-上次高程{}", onceSettlement, currElevation, preElevation);
            if (preResults.size() > 1) {
                SurveyResult pre2Result = preResults.get(1);
                Double pre2Elevation = pre2Result.getElevation();
                // 计算  上次单次沉降量 = 上次高程 - 上上次高程
                preSettlement = CalculationUtils.settlementDiff(preElevation, pre2Elevation);
                log.info("上次单次沉降量:{}=上次高程{}-上上次高程:{}", preSettlement, preElevation, pre2Elevation);
            }
            // 单次沉降量是否超限
            onceSettlementIsOver = onceIsOverrun(surveyPointVo, onceSettlement);
            log.info("当前单次沉降量是否超限:{},单次沉降量范围:{}~{}", onceSettlement, surveyPointVo.getOnceLowerLimit(), surveyPointVo.getOnceUpperLimit());
        }
        SurveyResult initResult = iSurveyResultService.queryInitResult(DictConstant.TableNamePrefix.SURVEY_RESULT + sectionId);
        if (initResult != null) {
            //  计算  当前累积沉降量 = 本次高程 - 初次高程
            Double initElevation = initResult.getElevation();
            totalSettlement = CalculationUtils.totalSettlement(currElevation, initElevation);
            log.info("当前累积沉降量:{}=当前高程{}-初始高程{}", totalSettlement, currElevation, initElevation);
            totalSettlementIsOver = totalIsOverrun(surveyPointVo, totalSettlement);
            log.info("当前累积沉降量是否超限:{},累积沉降量范围:{}~{}", totalSettlementIsOver, surveyPointVo.getTotalLowerLimit(), surveyPointVo.getTotalUpperLimit());
            //  计算  上次累积沉降量 = 上次高程  - 初始高程
            preTotalSettlement = CalculationUtils.totalSettlement(preElevation, initElevation);
            log.info("上次累积沉降量:{}=上次高程{}-初始高程{}", preTotalSettlement, preElevation, initElevation);

        }
        SurveyPointVo initPointVo = iSurveyPointService.queryInitPoint(DictConstant.TableNamePrefix.SURVEY_POINT + sectionId);
        if (initPointVo != null) {
            // 计算沉降速率  当前沉降速率 = 累积沉降量 / 历经的天数（自然天数）
            Date initTime = initPointVo.getCreateTime();
            settlementRate = CalculationUtils.settlementRate(totalSettlement, uploadTime, initTime);
            log.info("当前沉降速率:{}=累积沉降量{}/(当前上传时间{}-初始时间{})", settlementRate, uploadTime, initTime);
            settlementRateIsOver = settlementRateIsOver(surveyPointVo, settlementRate);
            log.info("当前沉降速率是否超限:{},沉降速率范围:{}~{}", settlementRateIsOver, surveyPointVo.getSpeedLowerLimit(), surveyPointVo.getSpeedUpperLimit());
            if (preResults != null && preResults.size() > 0) {
                // 计算  上次沉降速率 = 上次累积沉降量  /经历天数
                SurveyResult preResult = preResults.get(0);
                Date preUploadTime = preResult.getUploadTime();
                preSettlementRate = CalculationUtils.settlementRate(preTotalSettlement, preUploadTime, initTime);
                log.info("上次沉降速率:{}=上次累积沉降量{}/(上次上次时间{}-初始时间{})", preSettlementRate, preUploadTime, initTime);
            }
        }
        surveyResult.setSingleSettlement(onceSettlement);
        surveyResult.setCumulativeSettlement(totalSettlement);
        surveyResult.setSettlingRate(settlementRate);
        iSurveyResultService.modify(surveyResult,DictConstant.TableNamePrefix.SURVEY_RESULT+sectionId);
        saveSurveyException(surveyResult, sectionId, surveyPointVo, currElevation, preElevation, onceSettlement, preSettlement, onceSettlementIsOver, totalSettlement, preTotalSettlement, totalSettlementIsOver, settlementRate, preSettlementRate, settlementRateIsOver);
    }

    private void saveSurveyException(SurveyResult surveyResult, String sectionId, SurveyPointVo surveyPointVo, Double currElevation, double preElevation, double onceSettlement, double preSettlement, boolean onceSettlementIsOver, double totalSettlement, double preTotalSettlement, boolean totalSettlementIsOver, double settlementRate, double preSettlementRate, boolean settlementRateIsOver) {
        if (onceSettlementIsOver || totalSettlementIsOver || settlementRateIsOver) {
            log.info("---数据超限---");
            RespVO<SectionVo> query = sectionClient.query(sectionId);
            if (query.getRetCode() != RespConsts.SUCCESS_RESULT_CODE) {
                log.info("can not find section info");
                return;
            }
            String sectionCode = query.getInfo().getCode();
            SurveyPointExceptionVo exceptionVo = SurveyPointExceptionVo.builder()
                    .createTime(new Date())
                    .curElevation(currElevation)
                    .curOffsetValue(onceSettlement)
                    .curTotalOffsetValue(totalSettlement)
                    .curSpeed(settlementRate)
                    .description("")
                    .id(SnowflakeIdUtils.createId())
                    .onceLowerLimit(surveyPointVo.getOnceLowerLimit())
                    .onceUpperLimit(surveyPointVo.getOnceUpperLimit())
                    .speedLowerLimit(surveyPointVo.getSpeedLowerLimit())
                    .speedUpperLimit(surveyPointVo.getSpeedUpperLimit())
                    .totalLowerLimit(surveyPointVo.getTotalLowerLimit())
                    .totalUpperLimit(surveyPointVo.getTotalUpperLimit())
                    .status(0)
                    .resultId(surveyResult.getId())
                    .sectionCode(sectionCode)
                    .preElevation(preElevation)
                    .preOffsetValue(preSettlement)
                    .preTotalOffsetValue(preTotalSettlement)
                    .preSpeed(preSettlementRate)
                    .pointCode(surveyPointVo.getCode()).build();
            iSurveyPointExceptionService.create(exceptionVo.getSurveyPointException());
            log.info("----存储异常数据成果----");
        }
    }

    /**
     * 沉降速率 是否超限
     *
     * @param surveyPointVo
     * @param settlementRate
     * @return
     */
    private boolean settlementRateIsOver(SurveyPointVo surveyPointVo, double settlementRate) {
        Double speedLowerLimit = surveyPointVo.getSpeedLowerLimit();
        Double speedUpperLimit = surveyPointVo.getSpeedUpperLimit();
        return settlementRate > speedUpperLimit || settlementRate < speedLowerLimit;
    }

    /**
     * 累积沉降是否超限
     *
     * @param surveyPointVo
     * @param diff
     * @return
     */
    private boolean totalIsOverrun(SurveyPointVo surveyPointVo, double diff) {
        double totalLowerLimit = surveyPointVo.getTotalLowerLimit();
        double totalUpperLimit = surveyPointVo.getTotalUpperLimit();
        return diff > totalUpperLimit || diff < totalLowerLimit;
    }

    /**
     * 单次沉降量是否超限
     *
     * @param surveyPointVo
     * @param diff
     * @return
     */
    private boolean onceIsOverrun(SurveyPointVo surveyPointVo, double diff) {
        double onceLowerLimit = surveyPointVo.getOnceLowerLimit();
        double onceUpperLimit = surveyPointVo.getOnceUpperLimit();
        return diff > onceUpperLimit || diff < onceLowerLimit;
    }


}
