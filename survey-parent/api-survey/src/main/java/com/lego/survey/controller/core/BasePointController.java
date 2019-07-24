package com.lego.survey.controller.core;
import com.lego.survey.settlement.feign.BasePointClient;
import com.lego.survey.settlement.model.entity.BasePoint;
import com.lego.survey.settlement.model.vo.BasePointVo;
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
@RequestMapping(DictConstant.Path.BASE_POINT)
@Api(value = "BasePointController", description = "基准点接口")
@ApiResponses({
        @ApiResponse(response = ApiError.class,code = 404,message = "Resources Not Found")
})
public class BasePointController {

    @Autowired
    private BasePointClient basePointClient;

    @ApiOperation(value = "添加基准点", notes = "添加基准点", httpMethod = "POST")
    @ApiImplicitParams({

    })
    @RequestMapping(value = "/create", method = RequestMethod.POST)
    public RespVO add(@RequestBody BasePoint basePoint) {
        // 新增项目工程
        return basePointClient.create(basePoint);
    }


    @ApiOperation(value = "列举所有基准点信息", notes = "列举所有基准点信息", httpMethod = "GET")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "sectionCode", value = "标段编号", required = true,dataType = "String",paramType = "query"),
            @ApiImplicitParam(name = "name", value = "基准点名称", dataType = "String",paramType = "query"),
            @ApiImplicitParam(name = "startTimestamp", value = "开始时间戳", example = "1547001887000",paramType = "query"),
            @ApiImplicitParam(name = "endTimestamp", value = "结束时间戳", example = "1547088287939",paramType = "query"),

    })
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public RespVO<RespDataVO<BasePointVo>> list(
                                                @RequestParam(required = false) String sectionCode,
                                                @RequestParam(required = false) String name,
                                                @RequestParam(required = false) Long startTimestamp,
                                                @RequestParam(required = false) Long endTimestamp) {
        return basePointClient.query(sectionCode,name,startTimestamp,endTimestamp);
    }


    @ApiOperation(value = "修改基准点", notes = "修改基准点", httpMethod = "PUT")
    @ApiImplicitParams({

    })
    @RequestMapping(value = "/modify", method = RequestMethod.PUT)
    public RespVO modify(@Validated @RequestBody BasePoint basePoint, HttpServletRequest request) {
        return basePointClient.modify(basePoint);
    }


    @ApiOperation(value = "删除基准点列表", httpMethod = "DELETE", notes = "删除基准点列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "codes", value = "删除的基准点id集合", dataType = "String",example = "1",required = true, paramType = "query",allowMultiple = true),
    })
    @RequestMapping(value = "/delete", method = RequestMethod.DELETE)
    public RespVO delete(HttpServletRequest request,
                         @RequestParam List<String> codes) {
        return basePointClient.delete(codes);
    }
}
