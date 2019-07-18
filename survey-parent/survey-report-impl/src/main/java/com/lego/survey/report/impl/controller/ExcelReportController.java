package com.lego.survey.report.impl.controller;
import com.lego.survey.project.feign.SectionClient;
import com.lego.survey.project.model.entity.Section;
import com.lego.survey.settlement.feign.SurveyResultClient;
import com.lego.survey.settlement.model.vo.SurveyResultVo;
import com.lego.survey.lib.excel.ExcelService;
import com.survey.lib.common.consts.DictConstant;
import com.survey.lib.common.consts.RespConsts;
import com.survey.lib.common.vo.RespDataVO;
import com.survey.lib.common.vo.RespVO;
import com.survey.lib.common.vo.RespVOBuilder;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;

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


    @ApiOperation(value = "生成excel测量成果报表", notes = "生成excel测量成果报表", httpMethod = "POST")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "pageIndex", value = "当前页", dataType = "int", required = true,example = "1", paramType = "query"),
            @ApiImplicitParam(name = "pageSize", value = "每页大小", dataType = "int",defaultValue = "10",example = "10",paramType = "query"),
            @ApiImplicitParam(name = "workspaceId", value = "工区ID", dataType = "String",required = true,paramType = "path"),
            @ApiImplicitParam(name = "startTimestamp", value = "开始时间戳", dataType = "Long",example = "0",paramType = "path"),
            @ApiImplicitParam(name = "endTimestamp", value = "结束时间戳", dataType = "Long",example = "0",paramType = "path"),

    })
    @RequestMapping(value = "/generate",method = RequestMethod.POST)
    public RespVO generatePointResultExcel(@RequestParam("workspaceId") String workspaceId,
                                           @RequestParam(required = false, defaultValue = "1") int pageIndex,
                                           @RequestParam(required = false, defaultValue = "10") int pageSize,
                                           @RequestParam(required = false) Long startTimestamp,
                                           @RequestParam(required = false) Long endTimestamp
                                          ) {
       RespVO<RespDataVO<SurveyResultVo>> list = surveyResultClient.list(pageIndex, pageSize, workspaceId,startTimestamp,endTimestamp);
        if (list.getRetCode() != RespConsts.SUCCESS_RESULT_CODE) {
            return RespVOBuilder.failure("生成excel报表失败");
        }
        List<SurveyResultVo> info = list.getInfo().getList();
        RespVO<Section> sectionVoRespVO = sectionClient.queryByWorkspaceId(workspaceId);
        if(sectionVoRespVO.getRetCode()!=RespConsts.SUCCESS_RESULT_CODE){
            return RespVOBuilder.failure("生成excel报表失败");
        }
        String name = sectionVoRespVO.getInfo().getName();
        if(info==null || info.size()<=0){
            return RespVOBuilder.failure("获取不到excel报表数据");
        }
        excelService.writeExcel(info, "["+name+"]标段报表", "季度报表");
        return RespVOBuilder.success();
    }




}
