package com.lego.survey.controller.core;
import com.lego.survey.settlement.feign.SurveyTaskClient;
import com.lego.survey.settlement.model.vo.SurveyTaskVo;
import com.lego.survey.lib.swagger.ApiError;
import com.survey.lib.common.consts.DictConstant;
import com.survey.lib.common.vo.RespDataVO;
import com.survey.lib.common.vo.RespVO;
import io.swagger.annotations.*;
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
@RequestMapping(DictConstant.Path.SURVEY_TASK)
@Api(value = "SurveyTaskController", description = "任务接口")
@ApiResponses({
        @ApiResponse(response = ApiError.class,code = 404,message = "Resources Not Found")
})
public class SurveyTaskController {

    @Autowired
    private SurveyTaskClient surveyTaskClient;

    @ApiOperation(value = "添加任务", notes = "添加任务", httpMethod = "POST")
    @ApiImplicitParams({

    })
    @RequestMapping(value = "/create", method = RequestMethod.POST)
    public RespVO add(@RequestBody SurveyTaskVo surveyTaskVo) {
        return surveyTaskClient.create(surveyTaskVo);
    }


    @ApiOperation(value = "列举所有任务", notes = "列举所有任务", httpMethod = "GET")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "pageIndex", value = "当前页", dataType = "int", required = true,example = "1", paramType = "path"),
            @ApiImplicitParam(name = "pageSize", value = "每页大小", dataType = "int",defaultValue = "10",example = "10",paramType = "query"),
            @ApiImplicitParam(name = "sectionId", value = "标段id", dataType = "String",paramType = "path"),
    })
    @RequestMapping(value = "/list/{sectionId}/{pageIndex}", method = RequestMethod.GET)
    public RespVO<RespDataVO<SurveyTaskVo>> list(@PathVariable(value = "pageIndex") int  pageIndex,
                                                  @RequestParam(required = false,defaultValue = "10") int pageSize,
                                                  @PathVariable(value = "sectionId") String sectionId) {
        return surveyTaskClient.query(sectionId,pageIndex,pageSize);
    }


    @ApiOperation(value = "修改任务", notes = "修改任务", httpMethod = "PUT")
    @ApiImplicitParams({

    })
    @RequestMapping(value = "/modify/{sectionId}", method = RequestMethod.PUT)
    public RespVO modify(@Validated @RequestBody SurveyTaskVo surveyTaskVo,
                         @PathVariable(value = "sectionId") String sectionId,
                         HttpServletRequest request) {
        return surveyTaskClient.modify(surveyTaskVo, sectionId);
    }


    @ApiOperation(value = "删除任务", httpMethod = "DELETE", notes = "删除任务")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "ids", value = "任务id", dataType = "long", paramType = "query",example = "1",required = true,allowMultiple = true),
            @ApiImplicitParam(name = "sectionId", value = "标段ID", dataType = "String",required = true, paramType = "path"),
    })
    @RequestMapping(value = "/delete/{sectionId}", method = RequestMethod.DELETE)
    public RespVO delete(
            @PathVariable(value = "sectionId") String sectionId,
            @RequestParam List<Long> ids,
            HttpServletRequest request
    ) {
        // 验证用户正确性
        return surveyTaskClient.delete(sectionId,ids);
    }



}
