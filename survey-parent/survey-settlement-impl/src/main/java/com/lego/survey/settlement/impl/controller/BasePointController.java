package com.lego.survey.settlement.impl.controller;
import com.alibaba.fastjson.JSONObject;
import com.lego.survey.auth.feign.AuthClient;
import com.lego.survey.settlement.impl.service.IBasePointService;
import com.lego.survey.settlement.model.entity.BasePoint;
import com.lego.survey.settlement.model.vo.BasePointVo;
import com.lego.survey.event.user.LogSender;
import com.survey.lib.common.consts.DictConstant;
import com.survey.lib.common.utils.HeaderUtils;
import com.survey.lib.common.utils.HttpUtils;
import com.survey.lib.common.utils.SnowflakeIdUtils;
import com.survey.lib.common.vo.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.List;
/**
 * @author yanglf
 * @description
 * @since 2019/1/7
 **/
@Validated
@RestController
@RequestMapping(DictConstant.Path.BASE_POINT)
@Api(value = "BasePointController", description = "基准点接口")
public class BasePointController {

    @Autowired
    private AuthClient authClient;

    @Autowired
    private IBasePointService iBasePointService;

    @Autowired
    private LogSender logSender;


    @ApiOperation(value = "添加基准点", notes = "添加基准点", httpMethod = "POST")
    @ApiImplicitParams({

    })
    @RequestMapping(value = "/create", method = RequestMethod.POST)
    public RespVO create(@Validated @RequestBody BasePoint basePoint, HttpServletRequest request) {
        String code = basePoint.getCode();
        String name = basePoint.getName();
        String sectionCode = basePoint.getSectionCode();
        BasePoint bp=iBasePointService.queryByCodeOrName(code,name,sectionCode);
        if(bp!=null){
            return RespVOBuilder.failure("基准点已经存在");
        }
        HeaderVo headerVo = HeaderUtils.parseHeader(request);
        String userId = authClient.getAuthVo(headerVo).getUserId();
        basePoint.setId(SnowflakeIdUtils.createId());
        basePoint.setCreateUser(userId);
        RespVO respVO = iBasePointService.create(basePoint);
        // 新增日志
        logSender.sendLogEvent(HttpUtils.getClientIp(request), userId, "新增基准点:[" + basePoint.getId() + "]");
        return respVO;
    }


    @ApiOperation(value = "修改基准点", notes = "修改基准点", httpMethod = "PUT")
    @ApiImplicitParams({

    })
    @RequestMapping(value = "/modify", method = RequestMethod.PUT)
    public RespVO modify(@Validated @RequestBody BasePoint basePoint, HttpServletRequest request) {
        HeaderVo headerVo = HeaderUtils.parseHeader(request);
        String userId = authClient.getAuthVo(headerVo).getUserId();
        //TODO 校验权限
        RespVO respVO = iBasePointService.modify(basePoint);
        // 新增日志
        logSender.sendLogEvent(HttpUtils.getClientIp(request), userId, "修改基准点:[" + basePoint.getId() + "]");
        return respVO;
    }


    @ApiOperation(value = "删除基准点列表", httpMethod = "DELETE", notes = "删除基准点列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "ids", value = "删除的基准点id集合", dataType = "long", required = true, paramType = "query", allowMultiple = true),
    })
    @RequestMapping(value = "/delete", method = RequestMethod.DELETE)
    public RespVO delete(HttpServletRequest request,
                         @RequestParam List<Long> ids) {
        HeaderVo headerVo = HeaderUtils.parseHeader(request);
        CurrentVo currentVo = authClient.getAuthVo(headerVo);
        String userId = currentVo.getUserId();
        // 验证用户正确性
        RespVO delete = iBasePointService.delete(ids);
        logSender.sendLogEvent(HttpUtils.getClientIp(request), userId, "删除基准点:[" + JSONObject.toJSONString(ids) + "]");
        return delete;
    }


    @ApiOperation(value = "查询基准点列表", httpMethod = "GET", notes = "查询基准点列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "sectionCode", value = "标段编号", dataType = "String", required = true, paramType = "query"),
            @ApiImplicitParam(name = "name", value = "基准点名称", dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "startTimestamp", value = "开始时间戳", paramType = "query"),
            @ApiImplicitParam(name = "endTimestamp", value = "结束时间戳", paramType = "query"),

    })
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public RespVO<RespDataVO<BasePointVo>> query(@RequestParam String sectionCode,
                                                  @RequestParam(required = false) String name,
                                                  @RequestParam(required = false) Long startTimestamp,
                                                  @RequestParam(required = false) Long endTimestamp
    ) {
        Date startDate = null;
        Date endDate = null;
        if (startTimestamp != null && endTimestamp != null) {
            startDate = new Date(startTimestamp);
            endDate = new Date(endTimestamp);
        }
        List<BasePointVo> pointVos = iBasePointService.list(sectionCode, name, startDate, endDate);
        return RespVOBuilder.success(pointVos);
    }

}
