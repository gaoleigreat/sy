package com.lego.survey.settlement.impl.controller;
import com.lego.survey.auth.feign.AuthClient;
import com.lego.survey.settlement.impl.service.ISurveyPointTypeService;
import com.lego.survey.settlement.model.entity.SurveyPointType;
import com.lego.survey.settlement.model.vo.SurveyPointTypeVo;
import com.lego.survey.event.user.LogSender;
import com.survey.lib.common.consts.DictConstant;
import com.survey.lib.common.utils.HeaderUtils;
import com.survey.lib.common.utils.HttpUtils;
import com.survey.lib.common.utils.SnowflakeIdUtils;
import com.survey.lib.common.vo.HeaderVo;
import com.survey.lib.common.vo.RespDataVO;
import com.survey.lib.common.vo.RespVO;
import com.survey.lib.common.vo.RespVOBuilder;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

/**
 * @author Wesley.Xia
 * @description
 * @since 2019/1/8 11:08
 **/
@Validated
@RestController
@RequestMapping(DictConstant.Path.SURVEY_POINT_TYPE)
@Api(value = "SurveyPointTypeController", description = "测点类型接口")
public class SurveyPointTypeController {

    @Autowired
    private AuthClient authClient;

    @Autowired
    private LogSender logSender;

    @Autowired
    private ISurveyPointTypeService iSurveyPointTypeService;

    @ApiOperation(value = "添加测点类型", notes = "添加测点类型", httpMethod = "POST")
    @ApiImplicitParams({

    })
    @RequestMapping(value = "/create", method = RequestMethod.POST)
    public RespVO create(@Validated @RequestBody SurveyPointTypeVo spType, HttpServletRequest request) {
        // 参数验证
        String name = spType.getName();
        String code = spType.getCode();
        String sectionCode = spType.getSectionCode();
        SurveyPointType surveyPointType = iSurveyPointTypeService.queryTypeByNameOrCode(name, code,sectionCode);
        if (surveyPointType != null) {
            return RespVOBuilder.failure("测点类型已经存在");
        }
        HeaderVo headerVo = HeaderUtils.parseHeader(request);
        String userId = authClient.getAuthVo(headerVo).getUserId();
        spType.setId(SnowflakeIdUtils.createId());
        RespVO respVO = iSurveyPointTypeService.create(spType);
        logSender.sendLogEvent(HttpUtils.getClientIp(request), userId, "新增测点类型:[" + spType.getId() + "");
        return respVO;
    }


    @ApiOperation(value = "查询测点类型", httpMethod = "GET", notes = "查询测点类型")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "sectionCode", value = "标段编号", dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "status", value = "是否可用，0可以，1不可以", dataType = "int", paramType = "query"),
    })
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public RespVO<RespDataVO<SurveyPointTypeVo>> list(@RequestParam(required = false) String sectionCode,
                                                      @RequestParam(required = false) Integer status
    ) {
        return iSurveyPointTypeService.list(sectionCode,status);
    }

    @ApiOperation(value = "删除测点类型", notes = "删除测点类型", httpMethod = "DELETE")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "编号", dataType = "long", required = true),
    })
    @RequestMapping(value = "/delete", method = RequestMethod.DELETE)
    public RespVO delete(@RequestParam("id") Long id, HttpServletRequest request) {
        HeaderVo headerVo = HeaderUtils.parseHeader(request);
        String userId = authClient.getAuthVo(headerVo).getUserId();
        RespVO delete = iSurveyPointTypeService.delete(id);
        logSender.sendLogEvent(HttpUtils.getClientIp(request), userId, "删除测点分类:[" + id + "]");
        return delete;
    }

    @ApiOperation(value = "修改测点类型", notes = "修改测点类型", httpMethod = "PUT")
    @ApiImplicitParams({

    })
    @RequestMapping(value = "/modify", method = RequestMethod.PUT)
    public RespVO modify(@Validated @RequestBody SurveyPointTypeVo surveyPointTypeVo, HttpServletRequest request) {
        HeaderVo headerVo = HeaderUtils.parseHeader(request);
        String userId = authClient.getAuthVo(headerVo).getUserId();
        RespVO modify = iSurveyPointTypeService.modify(surveyPointTypeVo);
        logSender.sendLogEvent(HttpUtils.getClientIp(request), userId, "修改测点分类:[" + surveyPointTypeVo.getId() + "]");
        return modify;
    }
}
