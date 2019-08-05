package com.lego.survey.settlement.impl.listener;

import com.alibaba.fastjson.JSONObject;
import com.lego.survey.event.project.SectionSink;
import com.lego.survey.project.model.entity.Section;
import com.lego.survey.settlement.impl.mapper.DynamicCreateTableMapper;
import com.lego.survey.settlement.impl.service.ISurveyPointTypeService;
import com.lego.survey.settlement.model.entity.SurveyPointType;
import com.lego.survey.settlement.model.vo.SurveyPointTypeVo;
import com.lego.survey.user.feign.ConfigClient;
import com.lego.survey.user.model.entity.Config;
import com.lego.survey.user.model.vo.ConfigOptionVo;
import com.survey.lib.common.consts.DictConstant;
import com.survey.lib.common.consts.RespConsts;
import com.survey.lib.common.utils.SnowflakeIdUtils;
import com.survey.lib.common.vo.RespVO;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.messaging.Message;

import java.util.Date;
import java.util.List;

/**
 * @author yanglf
 * @description
 * @since 2019/1/7
 **/
@Slf4j(topic = "service")
@EnableBinding(SectionSink.class)
public class SectionListener {


    @Autowired
    private DynamicCreateTableMapper tableCreateMapper;

    @Autowired
    private ISurveyPointTypeService iSurveyPointTypeService;


    @StreamListener(SectionSink.SECTION_CREATE)
    public void createSection(Message<String> message) {
        log.info("接收到标段创建事件:{}", message.getPayload());
        Section section = JSONObject.parseObject(message.getPayload(), Section.class);
        if (section == null) {
            return;
        }
        String sectionCode = section.getCode();
        if (StringUtils.isBlank(sectionCode)) {
            return;
        }
        List<String> service = section.getService();
        if (service.contains(DictConstant.Scenes.CJFW)) {
            createCJFWTable(sectionCode);
            createSurveyType(sectionCode);
        }
        if (service.contains(DictConstant.Scenes.GPFW)) {
            createGPFWTable(sectionCode);
        }

    }

    private void createSurveyType(String sectionCode) {
        createType(sectionCode, "建筑", -3D, 1D, -3D, 1D, -40D, 10D);
        createType(sectionCode, "地表", -3D, 1D, -3D, 1D, -30D, 10D);
        createType(sectionCode, "管线", -3D, 1D, -3D, 1D, -30D, 10D);
    }

    private void createType(String sectionCode,
                            String name,
                            Double settleSpeedLowerLimit,
                            Double settleSpeedUpperLimit,
                            Double singleSettleLowerLimit,
                            Double singleSettleUpperLimit,
                            Double totalSettleLowerLimit,
                            Double totalSettleUpperLimit) {
        SurveyPointTypeVo surveyPointTypeVo = new SurveyPointTypeVo();
        surveyPointTypeVo.setCode(sectionCode);
        surveyPointTypeVo.setId(SnowflakeIdUtils.createId());
        surveyPointTypeVo.setComment(sectionCode);
        surveyPointTypeVo.setCreateTime(new Date());
        surveyPointTypeVo.setSettleSpeedLowerLimit(settleSpeedLowerLimit);
        surveyPointTypeVo.setSettleSpeedUpperLimit(settleSpeedUpperLimit);
        surveyPointTypeVo.setSingleSettleLowerLimit(singleSettleLowerLimit);
        surveyPointTypeVo.setSingleSettleUpperLimit(singleSettleUpperLimit);
        surveyPointTypeVo.setTotalSettleLowerLimit(totalSettleLowerLimit);
        surveyPointTypeVo.setTotalSettleUpperLimit(totalSettleUpperLimit);
        surveyPointTypeVo.setName(name);
        surveyPointTypeVo.setSectionCode(sectionCode);
        surveyPointTypeVo.setUpdateTime(new Date());
        iSurveyPointTypeService.create(surveyPointTypeVo);
    }


    /**
     * 创建管片服务对应的表
     *
     * @param sectionCode the section id
     */
    private void createGPFWTable(String sectionCode) {

    }

    /**
     * 创建沉降服务对应的表
     *
     * @param sectionCode the section code
     */
    private void createCJFWTable(String sectionCode) {
        // 创建测量点表
        String surveyPointTableName = DictConstant.TableNamePrefix.SURVEY_POINT + sectionCode;
        tableCreateMapper.createSurveyPointTable(surveyPointTableName);
        log.info("create table {}  finished", surveyPointTableName);
        // 创建 成果数据表
        String surveyResultTableName = DictConstant.TableNamePrefix.SURVEY_RESULT + sectionCode;
        tableCreateMapper.createSurveyResultTable(surveyResultTableName);
        log.info("create table {} finished", surveyResultTableName);
        //创建 原始数据表
        String surveyOriginalTableName = DictConstant.TableNamePrefix.SURVEY_ORIGINAL + sectionCode;
        tableCreateMapper.createSurveyOriginalTable(surveyOriginalTableName);
        log.info("create table {} finished", surveyOriginalTableName);
        // 创建  任务表
        String surveyTaskTableName = DictConstant.TableNamePrefix.SURVEY_TASK + sectionCode;
        tableCreateMapper.createSurveyTaskTable(surveyTaskTableName);
        log.info("create table {} finished", surveyTaskTableName);
    }

}
