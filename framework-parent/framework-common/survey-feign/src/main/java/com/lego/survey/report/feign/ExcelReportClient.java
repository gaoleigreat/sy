package com.lego.survey.report.feign;
import com.survey.lib.common.consts.DictConstant;
import com.survey.lib.common.consts.RespConsts;
import com.survey.lib.common.vo.RespVO;
import com.survey.lib.common.vo.RespVOBuilder;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;

/**
 * @author yanglf
 * @description
 * @since 2019/1/19
 **/
@FeignClient(value = DictConstant.Service.REPORT,path = DictConstant.Path.EXCEL_REPORT,fallback = ExcelReportClientFallback.class)
public interface ExcelReportClient {

    /**
     * generate excel report
     * @param pageIndex
     * @param pageSize
     * @param startTimestamp
     * @param endTimestamp
     * @return
     */
    @RequestMapping(value = "/generate",method = RequestMethod.POST)
    RespVO generatePointResultExcel(@RequestParam("workspaceId") String workspaceId,
                                    @RequestParam(required = false, defaultValue = "1", value = "pageIndex") int pageIndex,
                                    @RequestParam(required = false, defaultValue = "10", value = "pageSize") int pageSize,
                                    @RequestParam(required = false, value = "startTimestamp") Long startTimestamp,
                                    @RequestParam(required = false, value = "endTimestamp") Long endTimestamp
    );


    /**
     * 删除 excel 数据
     * @param file
     * @return
     */
    @RequestMapping(value = "/upload",consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    RespVO uploadPointResultExcel(@RequestPart(value = "file") MultipartFile file);


}

@Component
class  ExcelReportClientFallback implements ExcelReportClient{

    @Override
    public RespVO generatePointResultExcel(String workspaceId, int pageIndex, int pageSize, Long startTimestamp, Long endTimestamp) {
        return RespVOBuilder.failure(RespConsts.ERROR_SERVER_CODE,"report服务不可用");
    }

    @Override
    public RespVO uploadPointResultExcel(MultipartFile file) {
        return RespVOBuilder.failure(RespConsts.ERROR_SERVER_CODE,"report服务不可用");
    }
}
