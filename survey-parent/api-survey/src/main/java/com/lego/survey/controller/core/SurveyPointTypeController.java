package com.lego.survey.controller.core;

import com.lego.survey.settlement.feign.SurveyPointTypeClient;
import com.lego.survey.settlement.model.vo.SurveyPointTypeVo;
import com.lego.survey.lib.swagger.ApiError;

import com.survey.lib.common.consts.DictConstant;
import com.survey.lib.common.vo.RespDataVO;
import com.survey.lib.common.vo.RespVO;
import io.swagger.annotations.*;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.web.bind.annotation.*;
/**
 * @author yanglf
 * @description
 * @since 2019/1/14
 **/
@RestController
@RequestMapping(DictConstant.Path.SURVEY_POINT_TYPE)
@Api(value = "SurveyPointTypeController", description = "测点类型接口")
@ApiResponses({
        @ApiResponse(response = ApiError.class, code = 404, message = "Resources Not Found")
})
public class SurveyPointTypeController {

    @Autowired
    private SurveyPointTypeClient surveyPointTypeClient;

    @ApiOperation(value = "添加测点分类", notes = "添加测点分类", httpMethod = "POST")

    @ApiImplicitParams({

    })
    @RequestMapping(value = "/create", method = RequestMethod.POST)

    public RespVO add(@RequestBody SurveyPointTypeVo surveyPointTypeVo) {
        return surveyPointTypeClient.create(surveyPointTypeVo);
    }


    @ApiOperation(value = "修改测点分类", notes = "修改测点分类", httpMethod = "PUT")
    @ApiImplicitParams({

    })
    @RequestMapping(value = "/modify", method = RequestMethod.PUT)
    public RespVO modify(@RequestBody SurveyPointTypeVo surveyPointTypeVo) {
        return surveyPointTypeClient.modify(surveyPointTypeVo);
    }


    @ApiOperation(value = "列举所有测点信息", notes = "列举所有测点信息", httpMethod = "GET")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "sectionCode", value = "标段编号", dataType = "String", required = true, paramType = "query"),

    })
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public RespVO<RespDataVO<SurveyPointTypeVo>> list(@RequestParam(value = "sectionCode") String sectionCode
    ) {
        return surveyPointTypeClient.list(sectionCode);
    }


    @ApiOperation(value = "删除测点分类", httpMethod = "DELETE", notes = "删除测点分类")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "测点分类id", dataType = "long", required = true, example = "1", paramType = "query", allowMultiple = true),
    })
    @RequestMapping(value = "/delete", method = RequestMethod.DELETE)
    public RespVO delete(
            @RequestParam Long id
    ) {
        return surveyPointTypeClient.delete(id);
    }

}
