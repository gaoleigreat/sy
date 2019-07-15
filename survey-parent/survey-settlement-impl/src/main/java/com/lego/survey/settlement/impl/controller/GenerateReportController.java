package com.lego.survey.settlement.impl.controller;

import com.lego.survey.settlement.impl.service.ISurveyOriginalService;
import com.lego.survey.settlement.impl.service.ISurveyPointService;
import com.lego.survey.settlement.impl.service.ISurveyResultService;
import com.lego.survey.settlement.model.entity.SurveyResult;
import com.lego.survey.settlement.model.vo.*;
import com.survey.lib.common.consts.DictConstant;
import com.survey.lib.common.vo.RespDataVO;
import com.survey.lib.common.vo.RespVO;
import com.survey.lib.common.vo.RespVOBuilder;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Validated
@RestController
@RequestMapping(DictConstant.Path.SURVEY_ORIGINAL)
@Api(value = "GenerateReportController", description = "生成报告接口")
public class GenerateReportController {
    @Autowired
    private ISurveyOriginalService surveyOriginalService;
    @Autowired
    private ISurveyResultService surveyResultService;

    @Autowired
    private ISurveyPointService surveyPointService;


    @ApiOperation(value = "上传数据后导出word文档", httpMethod = "GET", notes = "上传数据后导出word文档")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "sectionId", value = "标段ID", dataType = "String", paramType = "query", required = true),
            @ApiImplicitParam(name = "taskId", value = "任务ID", dataType = "Long", paramType = "query", required = true),
    })
    @RequestMapping(value = "/query/list", method = RequestMethod.GET)
    public RespVO<RespDataVO<SurveyReportDataVo>> query(@RequestParam String sectionId,
                                                        @RequestParam Long taskId) {

        //获取原始数据
        List<SurveyOriginalVo> originalVos = surveyOriginalService.list(taskId, sectionId);
        //获取原始数据ID
        List<Long> originalIds = originalVos.stream().map(SurveyOriginalVo::getId).collect(Collectors.toList());
        //获取结果数据
        List<SurveyResult> surveyResults = surveyResultService.queryResult(sectionId, originalIds);

        //测量结果
        List<SurveyReportDataVo> surveyReportDataVos = new ArrayList<>();
        surveyResults.forEach(surveyResult -> surveyReportDataVos.add(SurveyReportDataVo.builder().build().loadSurveyReportDataVo(surveyResult)));

        for (SurveyReportDataVo surveyReportDataVo : surveyReportDataVos) {

            //上次测量结果
            List<SurveyResult> tempResults = surveyResultService.queryPreResult(surveyReportDataVo.getSurveyTime(), DictConstant.TableNamePrefix.SURVEY_RESULT + sectionId, 1, surveyReportDataVo.getPointCode());
            //第一次测量结果
            List<SurveyResult> intResults = surveyResultService.queryPreResult(null, DictConstant.TableNamePrefix.SURVEY_RESULT + sectionId, 1, surveyReportDataVo.getPointCode());
            //点初始值
            SurveyPointVo surveyPointVo = surveyPointService.querySurveyPointByCode(surveyReportDataVo.getPointCode(), DictConstant.TableNamePrefix.SURVEY_POINT+ sectionId);
            surveyReportDataVo.setPointType(surveyPointVo.getType());
            surveyReportDataVo.setInitElevation(surveyPointVo.getElevation());
            surveyReportDataVo.setOnceLowerLimit(surveyPointVo.getOnceLowerLimit());
            surveyReportDataVo.setOnceUpperLimit(surveyPointVo.getOnceUpperLimit());
            surveyReportDataVo.setSpeedLowerLimit(surveyPointVo.getSpeedLowerLimit());
            surveyReportDataVo.setSpeedUpperLimit(surveyPointVo.getSpeedUpperLimit());
            surveyReportDataVo.setTotalLowerLimit(surveyPointVo.getTotalLowerLimit());
            surveyReportDataVo.setTotalUpperLimit(surveyPointVo.getTotalUpperLimit());


            if (!CollectionUtils.isEmpty(intResults) && intResults.size() > 1) {
                surveyReportDataVo.setInitSurveyTime(intResults.get(intResults.size() - 1).getSurveyTime());
            }
            if (!CollectionUtils.isEmpty(tempResults)) {
                surveyReportDataVo.setPreElevation(tempResults.get(0).getElevation());
                surveyReportDataVo.setPreSurveyTime(tempResults.get(0).getSurveyTime());
            }

        }
        return RespVOBuilder.success(surveyReportDataVos);
    }
}
