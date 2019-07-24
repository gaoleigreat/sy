package com.lego.survey.settlement.feign;

import com.lego.survey.settlement.model.entity.BasePoint;
import com.lego.survey.settlement.model.vo.BasePointVo;
import com.lego.survey.settlement.model.vo.SurveyReportDataVo;
import com.survey.lib.common.consts.DictConstant;
import com.survey.lib.common.consts.RespConsts;
import com.survey.lib.common.vo.RespDataVO;
import com.survey.lib.common.vo.RespVO;
import com.survey.lib.common.vo.RespVOBuilder;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * @author yanglf
 * @description
 * @since 2019/1/7
 **/
@FeignClient(value = DictConstant.Service.SETTLEMENT, path = "/reportData", fallback = ReportDataClientFallback.class)
public interface ReportDataClient {


    /**
     * 查询沉降测量报告中需要插入的数据
     */
    @RequestMapping(value = "/query/settlementData", method = RequestMethod.GET)
    RespVO<RespDataVO<SurveyReportDataVo>> queryData(@RequestParam( value = "sectionCode") String sectionCode,
                                                     @RequestParam(value = "taskId") Long taskId);
}


@Component
class ReportDataClientFallback implements ReportDataClient {


    @Override
    public RespVO queryData(String sectionCode, Long taskId) {
        return RespVOBuilder.failure(RespConsts.ERROR_SERVER_CODE, "settlement服务不可用");
    }

}
