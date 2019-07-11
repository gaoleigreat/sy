package com.lego.survey.controller.core;
import com.lego.survey.settlement.feign.SurveyPointClient;
import com.lego.survey.settlement.model.vo.SurveyPointVo;
import com.lego.survey.lib.swagger.ApiError;
import com.survey.lib.common.consts.DictConstant;
import com.survey.lib.common.vo.RespDataVO;
import com.survey.lib.common.vo.RespVO;
import io.swagger.annotations.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import javax.servlet.http.HttpServletRequest;
import java.util.List;
/**
 * @author yanglf
 * @description
 * @since 2019/1/7
 **/
@RestController
@RequestMapping(DictConstant.Path.SURVEY_POINT)
@Api(value = "SurveyPointController", description = "测点接口")
@ApiResponses({
        @ApiResponse(response = ApiError.class, code = 404, message = "Resources Not Found")
})
public class SurveyPointController {

    @Autowired
    private SurveyPointClient surveyPointClient;

    @ApiOperation(value = "添加测点", notes = "添加测点", httpMethod = "POST")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "sectionId", value = "标段id", dataType = "String",required = true, paramType = "path"),
    })
    @RequestMapping(value = "/create/{sectionId}", method = RequestMethod.POST)
    public RespVO add(@RequestBody SurveyPointVo surveyPointVo, @PathVariable(value = "sectionId") String sectionId) {
        return surveyPointClient.create(surveyPointVo, sectionId);
    }


    @ApiOperation(value = "修改测点", notes = "修改测点", httpMethod = "PUT")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "sectionId", value = "标段id", dataType = "String",required = true, paramType = "path"),
    })
    @RequestMapping(value = "/modify/{sectionId}", method = RequestMethod.PUT)
    public RespVO modify(@RequestBody SurveyPointVo surveyPointVo, @PathVariable(value = "sectionId") String sectionId) {
        // 新增项目工程
        return surveyPointClient.modify(surveyPointVo, sectionId);
    }


    @ApiOperation(value = "列举所有测点信息", notes = "列举所有测点信息", httpMethod = "GET")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "pageIndex", value = "当前页", dataType = "int", defaultValue = "1", example = "1", paramType = "query"),
            @ApiImplicitParam(name = "pageSize", value = "每页大小", dataType = "int", defaultValue = "10", example = "10", paramType = "query"),
            @ApiImplicitParam(name = "workspaceId", value = "工区id", dataType = "String",required = true, paramType = "query"),
            @ApiImplicitParam(name = "startTimestamp", value = "开始时间戳", example = "1547001887000",paramType = "query"),
            @ApiImplicitParam(name = "endTimestamp", value = "结束时间戳", example = "1547088287939",paramType = "query",dataType = "Long"),

    })
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public RespVO<RespDataVO<SurveyPointVo>> list(@RequestParam(value = "pageIndex",required = false,defaultValue = "1") int pageIndex,
                                                  @RequestParam(required = false, defaultValue = "10") int pageSize,
                                                  @RequestParam(value = "workspaceId") String workspaceId,
                                                  @RequestParam(value = "startTimestamp",required = false) Long startTimestamp,
                                                  @RequestParam(value = "endTimestamp",required = false) Long endTimestamp) {
        return surveyPointClient.list(workspaceId, pageIndex, pageSize,startTimestamp,endTimestamp);
    }


    @ApiOperation(value = "批量添加测点", notes = "批量添加测点", httpMethod = "POST")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "sectionId", value = "标段id", dataType = "String",required = true, paramType = "path"),
    })
    @RequestMapping(value = "/createBatch/{sectionId}", method = RequestMethod.POST)
    public RespVO createBatch(@RequestBody List<SurveyPointVo> surveyPointVos,
                              @PathVariable(value = "sectionId") String sectionId,
                              HttpServletRequest request) {
        return surveyPointClient.createBatch(surveyPointVos, sectionId);
    }


    @ApiOperation(value = "删除测点", httpMethod = "DELETE", notes = "删除测点")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "ids", value = "测点id", dataType = "long", required = true, example = "1", paramType = "query",allowMultiple = true),
            @ApiImplicitParam(name = "sectionId", value = "标段ID", dataType = "String", required = true, paramType = "path"),
    })
    @RequestMapping(value = "/delete/{sectionId}", method = RequestMethod.DELETE)
    public RespVO delete(
            @PathVariable(value = "sectionId") String sectionId,
            @RequestParam List<Long> ids,
            HttpServletRequest request
    ) {
        return surveyPointClient.delete(sectionId, ids);
    }

}
