package com.lego.survey.controller.core;
import com.lego.survey.settlement.feign.SurveyResultClient;
import com.lego.survey.settlement.model.entity.SurveyResult;
import com.lego.survey.settlement.model.vo.SurveyResultVo;
import com.lego.survey.lib.swagger.ApiError;
import com.survey.lib.common.consts.DictConstant;
import com.survey.lib.common.vo.RespDataVO;
import com.survey.lib.common.vo.RespVO;
import io.swagger.annotations.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * @author yanglf
 * @description
 * @since 2019/1/7
 **/
@RestController
@Slf4j
@RequestMapping(DictConstant.Path.SURVEY_RESULT)
@Api(value = "SurveyResultController", description = "测量结果接口")
@ApiResponses({
        @ApiResponse(response = ApiError.class,code = 404,message = "Resources Not Found")
})
public class SurveyResultController {

    @Autowired
    private SurveyResultClient surveyResultClient;

    @ApiOperation(value = "添加测量结果", notes = "添加测量结果", httpMethod = "POST")
    @ApiImplicitParams({

    })
    @RequestMapping(value = "/create/{sectionId}", method = RequestMethod.POST)
    public RespVO add(@RequestBody SurveyResultVo surveyResultVos, @PathVariable(value = "sectionId") String sectionId) {
        // 新增项目工程
        return surveyResultClient.create(surveyResultVos,sectionId);
    }


    @ApiOperation(value = "列举所有测量结果信息", notes = "列举所有测量结果信息", httpMethod = "GET")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "pageIndex", value = "当前页", dataType = "int", defaultValue = "1",example = "1", paramType = "query"),
            @ApiImplicitParam(name = "pageSize", value = "每页大小", dataType = "int",defaultValue = "10",example = "10",paramType = "query"),
            @ApiImplicitParam(name = "workspaceId", value = "工区ID", dataType = "String",paramType = "query",required = true),
            @ApiImplicitParam(name = "startTimestamp", value = "开始时间戳", dataType = "Long",example = "0",paramType = "query"),
            @ApiImplicitParam(name = "endTimestamp", value = "结束时间戳", dataType = "Long",example = "0",paramType = "query"),

    })
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public RespVO<RespDataVO<SurveyResultVo>> list(@RequestParam(value = "pageIndex",required = false,defaultValue = "1") int  pageIndex,
                                                   @RequestParam(required = false,defaultValue = "10") int pageSize,
                                                   @RequestParam(value = "workspaceId") String workspaceId,
                                                   @RequestParam(required = false) Long startTimestamp,
                                                   @RequestParam(required = false) Long endTimestamp){
        return surveyResultClient.list(pageIndex, pageSize,workspaceId,startTimestamp,endTimestamp);
    }

    @ApiOperation(value = "批量添加成果数据", notes = "批量添加成果数据", httpMethod = "POST")
    @ApiImplicitParams({

    })
    @RequestMapping(value = "/createBatch", method = RequestMethod.POST)
    public RespVO createBatch(@Validated @RequestBody List<SurveyResultVo> surveyResultVos,
                              HttpServletRequest request) {
        return surveyResultClient.createBatch(surveyResultVos);
    }


    @ApiOperation(value = "批量添加成果数据", notes = "批量添加成果数据", httpMethod = "POST")
    @ApiImplicitParams({

    })
    @RequestMapping(value = "/uploadBatch", method = RequestMethod.POST)
    public RespVO uploadBatch(@Validated @RequestBody List<SurveyResult> surveyResults,
                              HttpServletRequest request) {
        return surveyResultClient.uploadBatch(surveyResults);
    }


    @ApiOperation(value = "修改成果数据", notes = "修改成果数据", httpMethod = "PUT")
    @ApiImplicitParams({

    })
    @RequestMapping(value = "/modify/{sectionId}", method = RequestMethod.PUT)
    public RespVO modify(@Validated @RequestBody SurveyResultVo surveyResultVo,
                         @PathVariable(value = "sectionId") String sectionId,
                         HttpServletRequest request) {
        return surveyResultClient.modify(surveyResultVo, sectionId);
    }


    @ApiOperation(value = "删除成果数据", httpMethod = "DELETE", notes = "删除成果数据")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "ids", value = "成果数据id", dataType = "long", required = true,example = "1",paramType = "path",allowMultiple = true),
            @ApiImplicitParam(name = "sectionId", value = "标段ID", dataType = "String",required = true, paramType = "path"),
    })
    @RequestMapping(value = "/delete/{sectionId}", method = RequestMethod.DELETE)
    public RespVO  delete(
            @PathVariable(value = "sectionId") String sectionId,
            @RequestParam List<Long> ids,
            HttpServletRequest request
    ) {
        return surveyResultClient.delete(sectionId,ids);
    }


}
