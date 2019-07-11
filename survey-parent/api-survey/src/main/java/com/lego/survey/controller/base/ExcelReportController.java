package com.lego.survey.controller.base;
import com.lego.survey.report.feign.ExcelReportClient;
import com.survey.lib.common.consts.DictConstant;
import com.survey.lib.common.vo.RespVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

/**
 * @author yanglf
 * @description
 * @since 2019/1/19
 **/
@RestController
@RequestMapping(DictConstant.Path.EXCEL_REPORT)
@Api(value = "ExcelReportController", description = "excel报表接口")
public class ExcelReportController {

    @Autowired
    private ExcelReportClient excelReportClient;

    @ApiOperation(value = "生成excel测量成果报表", httpMethod = "POST", notes = "生成excel测量成果报表")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "sectionId", value = "标段id", dataType = "String", required = true, paramType = "path"),
            @ApiImplicitParam(name = "pageIndex", value = "当前页", dataType = "int", defaultValue = "1", example = "1", paramType = "path"),
            @ApiImplicitParam(name = "pageSize", value = "每页大小", dataType = "int", defaultValue = "10", example = "10", paramType = "path"),

    })
    @RequestMapping(value = "/generate", method = RequestMethod.POST)
    public RespVO generateReport(@RequestParam(value = "workspaceId") String workspaceId,
                                 @RequestParam(required = false, defaultValue = "1") int pageIndex,
                                 @RequestParam(required = false, defaultValue = "10") int pageSize,
                                 @RequestParam(required = false) Long startTimestamp,
                                 @RequestParam(required = false) Long endTimestamp) {
        return excelReportClient.generatePointResultExcel(workspaceId, pageIndex, pageSize, startTimestamp, endTimestamp);
    }


    @ApiOperation(value = "上传excel数据", httpMethod = "POST", notes = "上传excel数据")
    @ApiImplicitParams({

    })
    @RequestMapping(value = "/upload",method = RequestMethod.POST, consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public RespVO uploadReport(@RequestPart(value = "file") MultipartFile file) {
        // TODO 文件处理

        return excelReportClient.uploadPointResultExcel(file);
    }


}
