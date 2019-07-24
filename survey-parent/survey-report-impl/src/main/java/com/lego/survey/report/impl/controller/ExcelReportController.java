package com.lego.survey.report.impl.controller;

import com.lego.survey.project.feign.SectionClient;
import com.lego.survey.project.model.entity.Section;
import com.lego.survey.report.impl.service.IWordReportService;
import com.lego.survey.settlement.feign.ReportDataClient;
import com.lego.survey.settlement.feign.SurveyResultClient;
import com.lego.survey.settlement.model.vo.SurveyReportDataVo;
import com.lego.survey.settlement.model.vo.SurveyReportVo;
import com.lego.survey.settlement.model.vo.SurveyResultVo;
import com.lego.survey.lib.excel.ExcelService;
import com.lego.word.WReporter;
import com.lego.word.element.WObject;
import com.survey.lib.common.consts.DictConstant;
import com.survey.lib.common.consts.RespConsts;
import com.survey.lib.common.vo.RespDataVO;
import com.survey.lib.common.vo.RespVO;
import com.survey.lib.common.vo.RespVOBuilder;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author yanglf
 * @description
 * @since 2019/1/17
 **/
@RestController
@RequestMapping(DictConstant.Path.EXCEL_REPORT)
@Api(value = "ExcelReportController", description = "excel报表接口")
public class ExcelReportController {

    @Autowired
    private ExcelService excelService;

    @Autowired
    private SurveyResultClient surveyResultClient;

    @Autowired
    private SectionClient sectionClient;

    @Autowired
    private IWordReportService wordReportService;

    @Autowired
    private ReportDataClient reportDataClient;


    @ApiOperation(value = "生成excel测量成果报表", notes = "生成excel测量成果报表", httpMethod = "POST")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "pageIndex", value = "当前页", dataType = "int", required = true, example = "1", paramType = "query"),
            @ApiImplicitParam(name = "pageSize", value = "每页大小", dataType = "int", defaultValue = "10", example = "10", paramType = "query"),
            @ApiImplicitParam(name = "workspaceId", value = "工区ID", dataType = "String", required = true, paramType = "path"),
            @ApiImplicitParam(name = "startTimestamp", value = "开始时间戳", dataType = "Long", example = "0", paramType = "path"),
            @ApiImplicitParam(name = "endTimestamp", value = "结束时间戳", dataType = "Long", example = "0", paramType = "path"),

    })
    @RequestMapping(value = "/generate", method = RequestMethod.POST)
    public RespVO generatePointResultExcel(@RequestParam("workspaceId") String workspaceId,
                                           @RequestParam(required = false, defaultValue = "1") int pageIndex,
                                           @RequestParam(required = false, defaultValue = "10") int pageSize,
                                           @RequestParam(required = false) Long startTimestamp,
                                           @RequestParam(required = false) Long endTimestamp
    ) {
        RespVO<RespDataVO<SurveyResultVo>> list = surveyResultClient.list(pageIndex, pageSize, workspaceId, startTimestamp, endTimestamp);
        if (list.getRetCode() != RespConsts.SUCCESS_RESULT_CODE) {
            return RespVOBuilder.failure("生成excel报表失败");
        }
        List<SurveyResultVo> info = list.getInfo().getList();
        RespVO<Section> sectionVoRespVO = sectionClient.queryByWorkspaceId(workspaceId);
        if (sectionVoRespVO.getRetCode() != RespConsts.SUCCESS_RESULT_CODE) {
            return RespVOBuilder.failure("生成excel报表失败");
        }
        String name = sectionVoRespVO.getInfo().getName();
        if (info == null || info.size() <= 0) {
            return RespVOBuilder.failure("获取不到excel报表数据");
        }
        excelService.writeExcel(info, "[" + name + "]标段报表", "季度报表");
        return RespVOBuilder.success();
    }

    @ApiOperation(value = "生成成果报表", notes = "生成测量成果报表", httpMethod = "GET")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "sectionCode", value = "sectionCode", dataType = "String", required = true, paramType = "query"),
            @ApiImplicitParam(name = "taskId", value = "taskId", dataType = "Long",  paramType = "query"),
    })
    @RequestMapping(value = "/generateExcel", method = RequestMethod.GET)
    public RespVO generateExcel( HttpServletResponse response,@RequestParam String sectionCode,@RequestParam Long taskId) throws Exception {
        InputStream inputStream = Thread.currentThread().getContextClassLoader().getResourceAsStream("template.xlsx");
        XSSFWorkbook workBook = new XSSFWorkbook(inputStream);
        // 给sheet命名

        String fileName = "test" + System.currentTimeMillis() + ".xlsx";
        response.setContentType("application/force-download");


        //1 通过sectionid 和taskid 查询原始测量数据
        RespVO<RespDataVO<SurveyReportDataVo>> respDataVORespVO =  reportDataClient.queryData(sectionCode,taskId);
        List<SurveyReportDataVo> surveyReportDataVoList =  respDataVORespVO.getInfo().getList();
        Set<String> typeSet = surveyReportDataVoList.stream().map(SurveyReportDataVo::getPointType).collect(Collectors.toSet());
        int i =1;
        for (String type:typeSet) {
            List<SurveyReportDataVo> list = surveyReportDataVoList.stream().filter(o ->o.getPointType().equals(type)).collect(Collectors.toList());
            SurveyReportDataVo surveyReportDataVo = list.get(0);
            SurveyReportVo surveyReportVo = wordReportService.getSurveyReportVo(surveyReportDataVo.getWorkspaceCode());
            surveyReportVo.setSurveyer(surveyReportDataVo.getSurveyer());
            surveyReportVo.setInitSurveyTime(surveyReportDataVo.getInitSurveyTime());
            surveyReportVo.setPreSurveyTime(surveyReportDataVo.getPreSurveyTime());
            surveyReportVo.setSurveyTime(surveyReportDataVo.getSurveyTime());
            surveyReportVo.setOnceLowerLimit(surveyReportDataVo.getOnceLowerLimit());
            surveyReportVo.setDocname(type+"沉降检测表");
            surveyReportVo.setPontType(type);
            XSSFSheet sheet = workBook.cloneSheet(0);
            sheet.getRow(5).getCell(1).setCellValue(surveyReportVo.getDocname());
            sheet.getRow(6).getCell(1).setCellValue(surveyReportVo.getDocname());
            sheet.getRow(7).getCell(1).setCellValue(surveyReportVo.getDocname());
            // 给sheet命名
            i++;
            workBook.setSheetName(i, type);
            for (int j =0; j<list.size();j++) {
                XSSFRow row = sheet.createRow(12+i);
                row.getCell(0).setCellValue(j);
                row.getCell(1).setCellValue(list.get(j).getPointCode());
                row.getCell(2).setCellValue(list.get(j).getPointCode());
                row.getCell(3).setCellValue(list.get(j).getPointCode());
                row.getCell(4).setCellValue(list.get(j).getPointCode());
                row.getCell(5).setCellValue(list.get(j).getPointCode());
                row.getCell(6).setCellValue(list.get(j).getPointCode());
                row.getCell(7).setCellValue(list.get(j).getPointCode());

            }

        }
        // 设置文件名
        response.addHeader("Content-Disposition", "attachment;fileName=" + java.net.URLEncoder.encode(fileName, "UTF-8"));
        OutputStream out = response.getOutputStream();
        workBook.removeSheetAt(0);
        // 移除workbook中的模板sheet
        workBook.write(out);

        inputStream.close();
        out.flush();
        out.close();
        return RespVOBuilder.success();
    }

    public static void replaceCellValue(Cell cell, Object value) {
        String val = value != null ? String.valueOf(value) : "";
        cell.setCellValue(val);
    }


}
