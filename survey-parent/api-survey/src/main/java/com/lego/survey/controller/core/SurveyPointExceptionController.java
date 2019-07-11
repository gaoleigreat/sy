package com.lego.survey.controller.core;
import com.lego.survey.settlement.feign.SurveyPointExceptionClient;
import com.lego.survey.settlement.model.vo.SurveyPointExceptionVo;
import com.lego.survey.lib.swagger.ApiError;
import com.survey.lib.common.consts.DictConstant;
import com.survey.lib.common.vo.RespDataVO;
import com.survey.lib.common.vo.RespVO;
import io.swagger.annotations.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

/**
 * @author yanglf
 * @description
 * @since 2019/1/15
 **/
@RestController
@RequestMapping(DictConstant.Path.SURVEY_POINT_EXCEPTION)
@Api(value = "SurveyPointExceptionController", description = "测点异常接口")
@ApiResponses({
        @ApiResponse(response = ApiError.class, code = 404, message = "Resources Not Found")
})
public class SurveyPointExceptionController {

    @Autowired
    private SurveyPointExceptionClient surveyPointExceptionClient;

    @ApiOperation(value = "添加测点异常信息", notes = "添加测点异常信息", httpMethod = "POST")
    @ApiImplicitParams({

    })
    @RequestMapping(value = "/create", method = RequestMethod.POST)
    public RespVO add(@RequestBody SurveyPointExceptionVo surveyPointExceptionVo) {
        return surveyPointExceptionClient.create(surveyPointExceptionVo);
    }


    @ApiOperation(value = "修改测点异常", notes = "修改测点异常", httpMethod = "PUT")
    @ApiImplicitParams({

    })
    @RequestMapping(value = "/modify", method = RequestMethod.PUT)
    public RespVO modify(@RequestBody SurveyPointExceptionVo surveyPointExceptionVo) {
        return surveyPointExceptionClient.modify(surveyPointExceptionVo);
    }


    @ApiOperation(value = "列举所有测点异常信息", notes = "列举所有测点异常信息", httpMethod = "GET")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "pageIndex", value = "当前页", dataType = "int", required = true, example = "1", paramType = "path"),
            @ApiImplicitParam(name = "pageSize", value = "每页大小", dataType = "int", defaultValue = "10", example = "10", paramType = "query"),
            @ApiImplicitParam(name = "sectionCode", value = "标段编号", dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "pointCode", value = "测点编号", dataType = "String", paramType = "query"),

    })
    @RequestMapping(value = "/list/{pageIndex}", method = RequestMethod.GET)
    public RespVO<RespDataVO<SurveyPointExceptionVo>> list(@PathVariable(value = "pageIndex") int pageIndex,
                                                           @RequestParam(required = false, defaultValue = "10") int pageSize,
                                                           @RequestParam(value = "sectionCode",required = false) String sectionCode,
                                                           @RequestParam(value = "pointCode",required = false) String pointCode) {
        return surveyPointExceptionClient.query(pageIndex, pageSize, sectionCode, pointCode);
    }


    @ApiOperation(value = "删除测点异常", httpMethod = "DELETE", notes = "删除测点异常")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "测点异常id", dataType = "long", required = true, example = "1", paramType = "query", allowMultiple = true),
    })
    @RequestMapping(value = "/delete", method = RequestMethod.DELETE)
    public RespVO delete(
            @RequestParam Long id,
            HttpServletRequest request
    ) {
        return surveyPointExceptionClient.delete(id);
    }


}
