package com.lego.survey.report.impl.controller;
import com.lego.survey.project.feign.SectionClient;
import com.lego.survey.settlement.feign.SurveyResultClient;
import com.survey.lib.common.consts.DictConstant;
import com.survey.lib.common.vo.RespVO;
import com.survey.lib.common.vo.RespVOBuilder;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @author yanglf
 * @description
 * @since 2019/1/18
 **/
@RestController
@RequestMapping(DictConstant.Path.PDF_REPORT)
@Api(value = "PdfReportController", description = "pdf报表接口")
public class PdfReportController {

    @Autowired
    private SurveyResultClient surveyResultClient;

    @Autowired
    private SectionClient sectionClient;


    @ApiOperation(value = "生成pdf测量成果报表", notes = "生成pdf测量成果报表", httpMethod = "POST")
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
       /* RespVO<RespDataVO<SurveyResult>> list = surveyResultClient.list(sectionId, pageIndex, pageSize);
        if (list.getRetCode() != RespConsts.SUCCESS_RESULT_CODE) {
            return RespVOBuilder.failure("生成pdf报表失败");
        }
        List<SurveyResult> info = list.getInfo().getList();
        RespVO<SectionVo> sectionVoRespVO = sectionClient.query(sectionId);
        if(sectionVoRespVO.getRetCode()!=RespConsts.SUCCESS_RESULT_CODE){
            return RespVOBuilder.failure("生成pdf报表失败");
        }
        String name = sectionVoRespVO.getInfo().getName();
        if(info==null || info.size()<=0){
            return RespVOBuilder.failure("获取不到pdf报表数据");
        }*/

        return RespVOBuilder.success();
    }



}
