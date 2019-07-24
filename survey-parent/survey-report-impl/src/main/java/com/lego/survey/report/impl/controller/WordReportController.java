package com.lego.survey.report.impl.controller;

import com.lego.survey.project.feign.SectionClient;
import com.lego.survey.report.impl.service.IWordReportService;
import com.lego.survey.settlement.feign.ReportDataClient;
import com.lego.survey.settlement.feign.SurveyResultClient;
import com.lego.survey.settlement.model.vo.SurveyReportDataVo;
import com.lego.survey.settlement.model.vo.SurveyReportVo;
import com.lego.word.WReporter;
import com.lego.word.element.WObject;
import com.survey.lib.common.consts.DictConstant;
import com.survey.lib.common.vo.RespDataVO;
import com.survey.lib.common.vo.RespVO;
import com.survey.lib.common.vo.RespVOBuilder;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author yanglf
 * @description
 * @since 2019/1/18
 **/
@RestController
@RequestMapping(DictConstant.Path.WORD_REPORT)
@Api(value = "WordReportController", description = "word报表接口")
public class WordReportController {

    @Autowired
    private IWordReportService wordReportService;

    @Autowired
    private ReportDataClient reportDataClient;



    @ApiOperation(value = "生成Excel测量成果报表", notes = "生成word测量成果报表", httpMethod = "POST")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "pageIndex", value = "当前页", dataType = "int", required = true,example = "1", paramType = "path"),
            @ApiImplicitParam(name = "pageSize", value = "每页大小", dataType = "int",defaultValue = "10",example = "10",paramType = "query"),
            @ApiImplicitParam(name = "sectionCode", value = "标段code", dataType = "String",required = true,paramType = "path"),
    })
    @RequestMapping(value = "/generate/{sectionCode}",method = RequestMethod.POST)
    public RespVO generatePointResultExcel(@PathVariable("sectionCode") String sectionCode,
                                           @RequestParam(required = false, defaultValue = "1") int pageIndex,
                                           @RequestParam(required = false, defaultValue = "10") int pageSize
    ) {
        return RespVOBuilder.success();
    }



    @ApiOperation(value = "生成word测量成果报表", notes = "生成word测量成果报表", httpMethod = "GET")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "sectionCode", value = "标段id", dataType = "String",required = true,paramType = "path"),
            @ApiImplicitParam(name = "taskId", value = "任务id", dataType = "String",required = true,paramType = "path"),
    })
    @RequestMapping(value = "/get/{sectionCode}/{taskId}",method = RequestMethod.GET)
    public RespVO generatePointResultWord(@PathVariable("sectionCode") String sectionCode,@PathVariable("taskId") Long taskId) throws Exception {
        //1 通过sectionCode 和taskid 查询原始测量数据
        RespVO<RespDataVO<SurveyReportDataVo>> respDataVORespVO =  reportDataClient.queryData(sectionCode,taskId);
        List<SurveyReportDataVo> surveyReportDataVoList =  respDataVORespVO.getInfo().getList();
        Set<String> typeSet = surveyReportDataVoList.stream().map(SurveyReportDataVo::getPointType).collect(Collectors.toSet());

        for (String type:typeSet) {
            List<WObject> list = surveyReportDataVoList.stream().filter(o ->o.getPointType().equals(type)).collect(Collectors.toList());
            SurveyReportDataVo surveyReportDataVo = (SurveyReportDataVo) list.get(0);
            SurveyReportVo surveyReportVo = wordReportService.getSurveyReportVo(surveyReportDataVo.getWorkspaceCode());
            surveyReportVo.setSurveyer(surveyReportDataVo.getSurveyer());
            surveyReportVo.setInitSurveyTime(surveyReportDataVo.getInitSurveyTime());
            surveyReportVo.setPreSurveyTime(surveyReportDataVo.getPreSurveyTime());
            surveyReportVo.setSurveyTime(surveyReportDataVo.getSurveyTime());
            surveyReportVo.setOnceLowerLimit(surveyReportDataVo.getOnceLowerLimit());
            surveyReportVo.setDocname(type+"沉降检测表");
            surveyReportVo.setPontType(type);

            WReporter wReporter = new WReporter("C:/Users/xiaodao/Desktop/template.docx");
            wReporter.export(list,1,1);
            wReporter.export(surveyReportVo,0);
            wReporter.generate("C:/Users/xiaodao/Desktop/result"+type+".docx");

        }






        //2 通过原始测量数据查询测量结果

        //獲取要插入excel中的数据
        //获取要替换的变量
        //获取要替换的图片

        //3 通过测量结果查询上次的测量结果

        //
        return RespVOBuilder.success();
    }


}
