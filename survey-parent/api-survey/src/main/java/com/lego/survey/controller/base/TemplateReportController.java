package com.lego.survey.controller.base;
import com.lego.survey.lib.swagger.ApiError;
import com.lego.survey.report.model.entity.TemplateReport;
import com.lego.survey.report.feign.TemplateReportClient;
import com.survey.lib.common.consts.DictConstant;
import com.survey.lib.common.vo.RespDataVO;
import com.survey.lib.common.vo.RespVO;
import io.swagger.annotations.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @author yanglf
 * @description
 * @since 2019/1/17
 **/
@RestController
@RequestMapping(DictConstant.Path.TEMPLATE_REPORT)
@Api(value = "TemplateReportController", description = "报表模板接口")
@ApiResponses({
        @ApiResponse(response = ApiError.class,code = 404,message = "Resources Not Found")
})
public class TemplateReportController {

    @Autowired
    private TemplateReportClient templateReportClient;


    @ApiOperation(value = "创建模板", httpMethod = "POST", notes = "创建模板")
    @ApiImplicitParams({

    })
    @RequestMapping(value = "/create", method = RequestMethod.POST)
    public RespVO create(@RequestBody TemplateReport templateReport) {
        return templateReportClient.create(templateReport);
    }


    @ApiOperation(value = "修改模板", httpMethod = "PUT", notes = "修改模板")
    @ApiImplicitParams({

    })
    @RequestMapping(value = "/modify", method = RequestMethod.PUT)
    public RespVO modify(@RequestBody TemplateReport templateReport) {
        return templateReportClient.modify(templateReport);
    }



    @ApiOperation(value = "删除模板", httpMethod = "DELETE", notes = "删除模板")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "模板id", example = "1", required = true, paramType = "query"),
    })
    @RequestMapping(value = "/delete", method = RequestMethod.DELETE)
    public RespVO delete(Long id) {
        return templateReportClient.delete(id);
    }



    @ApiOperation(value = "查询模板列表", httpMethod = "GET", notes = "查询模板列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "pageIndex", value = "请求页",defaultValue = "1", example = "1", required = true, paramType = "path"),
            @ApiImplicitParam(name = "pageSize", value = "每页大小",defaultValue = "10", example = "10", required = true, paramType = "query"),
            @ApiImplicitParam(name = "sectionCode", value = "标段编号",paramType = "query"),

    })
    @RequestMapping(value = "/list/{pageIndex}", method = RequestMethod.GET)
    public RespVO<RespDataVO<TemplateReport>> list(@PathVariable("pageIndex") int pageIndex,
                                                   @RequestParam(required = false,defaultValue = "10") int pageSize,
                                                   @RequestParam(required = false) String sectionCode) {
        return templateReportClient.list(pageIndex,pageSize,sectionCode);
    }

}
