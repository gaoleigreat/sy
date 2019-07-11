package com.lego.survey.settlement.impl.service;

import com.lego.survey.settlement.model.entity.SurveyPointException;
import com.lego.survey.settlement.model.vo.SurveyPointExceptionVo;
import com.survey.lib.common.vo.RespDataVO;
import com.survey.lib.common.vo.RespVO;
/**
 * @author yanglf
 * @description
 * @since 2019/1/9
 **/
public interface ISurveyPointExceptionService {

    /**
     * 新增基准点异常
     *
     * @param surveyPointException
     * @return
     */
    RespVO create(SurveyPointException surveyPointException);


    /**
     * 修改基准点异常信息
     * @param surveyPointException
     * @return
     */
    RespVO   modify(SurveyPointException surveyPointException);


    /**
     * 删除基准点异常
     * @param id
     * @return
     */
    RespVO   delete(Long id);


    /**
     * 获取基准点异常列表
     * @param pageIndex
     * @param pageSize
     * @return
     */
    RespVO<RespDataVO<SurveyPointExceptionVo>>   list(int pageIndex, int pageSize, String sectionCode, String pointCode);


    /**
     * 根据id获取基准点异常信息
     * @param id
     * @return
     */
    RespVO<SurveyPointException>   queryById(Long id);

}
