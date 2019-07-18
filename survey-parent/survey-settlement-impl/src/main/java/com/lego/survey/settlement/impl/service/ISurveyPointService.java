package com.lego.survey.settlement.impl.service;

import com.lego.survey.settlement.model.entity.SurveyPoint;
import com.lego.survey.settlement.model.vo.SurveyPointVo;
import com.survey.lib.common.vo.RespVO;

import java.util.Date;
import java.util.List;

/**
 * @author yanglf
 * @description
 * @since 2019/1/8
 **/
public interface ISurveyPointService {


    /**
     * 查询测点列表
     * @param pageIndex   request data page
     * @param pageSize    per page size
     * @return  query result
     */
    List<SurveyPointVo>   list(int pageIndex, int pageSize, String deviceType, String workspaceCode, String tableName, Date startDate, Date endDate);


    /**
     *  create survey point
     * @param surveyPointVo   survey point
     * @return  create result
     */
    RespVO create(SurveyPointVo surveyPointVo, String tableName);


    /**
     * 删除测点信息
     * @param ids
     * @param tableName
     * @return
     */
    RespVO   delete(List<Long> ids, String tableName);


    /**
     * 更新测点信息
     * @param surveyPoint
     * @param tableName
     * @return
     */
    RespVO   modify(SurveyPoint surveyPoint, String tableName);


    /**
     * 批量新增测点
     * @param surveyPoints
     * @param tableName
     * @return
     */
    RespVO createBatch(List<SurveyPointVo> surveyPoints, String tableName);

    /**
     * 根据名称或者编号查询测点信息
     * @param name
     * @param code
     * @return
     */
    SurveyPointVo querySurveyPointByNameOrCode(String name, String code, String tableName);

    /**
     * 根据测点编码查询测点
     * @param pointCode
     * @param tableName
     * @return
     */
    SurveyPointVo querySurveyPointByCode(String pointCode, String tableName);

    /**
     * 查询初始测点信息
     * @param tableName
     * @return
     */
    SurveyPointVo queryInitPoint(String tableName);
}
