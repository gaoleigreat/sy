package com.lego.survey.user.impl.service;
import com.lego.survey.user.model.entity.Log;
import com.lego.survey.user.model.vo.LogVo;
import com.survey.lib.common.page.PagedResult;
import com.survey.lib.common.vo.RespVO;

/**
 * @author yanglf
 * @description
 * @since 2018/12/28
 **/
public interface ILogService {

    /**
     * 新增日志
     * @param log
     * @return
     */
    RespVO   add(Log log);


    /**
     * 获取日志列表
     * @param pageIndex
     * @param pageSize
     * @return
     */
    RespVO<PagedResult<LogVo>>   list(int pageIndex, int pageSize);


}
