package com.lego.survey.controller.core;
import com.lego.survey.settlement.feign.SurveyOriginalClient;
import com.lego.survey.settlement.model.vo.SurveyOriginalVo;
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
@RequestMapping(DictConstant.Path.SURVEY_ORIGINAL)
@Api(value = "SurveyOriginalController", description = "原始数据接口")
@ApiResponses({
        @ApiResponse(response = ApiError.class, code = 404, message = "Resources Not Found")
})
public class SurveyOriginalController {

    @Autowired
    private SurveyOriginalClient surveyOriginalClient;

    @ApiOperation(value = "添加原始数据", notes = "添加原始数据", httpMethod = "POST")
    @ApiImplicitParams({

    })
    @RequestMapping(value = "/create/{sectionCode}", method = RequestMethod.POST)
    public RespVO add(@RequestBody SurveyOriginalVo surveyOriginalVo, @PathVariable(value = "sectionCode") String sectionCode) {
        return surveyOriginalClient.create(surveyOriginalVo, sectionCode);
    }


    @ApiOperation(value = "列举所有原始数据", notes = "列举所有原始数据", httpMethod = "GET")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "pageIndex", value = "当前页", dataType = "int", required = true, example = "1", paramType = "path"),
            @ApiImplicitParam(name = "pageSize", value = "每页大小", dataType = "int", defaultValue = "10", example = "10", paramType = "query"),
            @ApiImplicitParam(name = "sectionId", value = "标段id", dataType = "String", paramType = "path"),
    })
    @RequestMapping(value = "/list/{sectionId}/{pageIndex}", method = RequestMethod.GET)
    public RespVO<RespDataVO<SurveyOriginalVo>> list(@PathVariable(value = "pageIndex") int pageIndex,
                                                   @RequestParam(required = false, defaultValue = "10") int pageSize,
                                                   @PathVariable(value = "sectionId") String sectionId) {
        return surveyOriginalClient.query(sectionId, pageIndex, pageSize);
    }


    @ApiOperation(value = "删除原始数据", httpMethod = "DELETE", notes = "删除原始数据")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "ids", value = "原始数据id", dataType = "long", required = true ,example = "1", paramType = "query",allowMultiple = true),
            @ApiImplicitParam(name = "sectionId", value = "标段ID", dataType = "String", required = true, paramType = "path"),
    })
    @RequestMapping(value = "/delete/{sectionId}", method = RequestMethod.DELETE)
    public RespVO delete(
            @PathVariable(value = "sectionId") String sectionId,
            @RequestParam List<Long> ids,
            HttpServletRequest request
    ) {
        return surveyOriginalClient.delete(sectionId, ids);
    }


    @ApiOperation(value = "修改原始数据", notes = "修改原始数据", httpMethod = "PUT")
    @ApiImplicitParams({

    })
    @RequestMapping(value = "/modify/{sectionId}", method = RequestMethod.PUT)
    public RespVO modify(@Validated @RequestBody SurveyOriginalVo surveyOriginalVo,
                         @PathVariable(value = "sectionId") String sectionId,
                         HttpServletRequest request) {
        return surveyOriginalClient.modify(surveyOriginalVo,  sectionId);
    }

}
