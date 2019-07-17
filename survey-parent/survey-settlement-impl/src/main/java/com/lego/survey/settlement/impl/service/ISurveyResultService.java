package com.lego.survey.settlement.impl.service;
import com.lego.survey.settlement.model.entity.SurveyResult;
import com.lego.survey.settlement.model.vo.OverrunListVo;
import com.lego.survey.settlement.model.vo.SurveyResultVo;
import com.survey.lib.common.page.PagedResult;
import com.survey.lib.common.vo.RespVO;

import java.util.Date;
import java.util.List;

/**
 * @author yanglf
 * @description
 * @since 2019/1/8
 **/
public interface ISurveyResultService {


    /**
     * 查询测点列表
     * @param pageIndex   request data page
     * @param pageSize    per page size
     * @param  workspaceCode workspaceCode
     * @return  query result
     */
    RespVO   list(int pageIndex, int pageSize, String workspaceCode, Date startDate, Date endDate, String deviceType, String tableName);


    /**
     *  create survey point
     * @param surveyResult   survey point
     * @return  create result
     */
    RespVO create(SurveyResult surveyResult, String tableName);


    /**
     * 修改成果数据
     * @param surveyResult
     * @param tableName
     * @return
     */
    RespVO modify(SurveyResult surveyResult, String tableName);


    /**
     * 删除成果数据
     * @param ids
     * @param tableName
     * @return
     */
    RespVO delete(List<Long> ids, String tableName);


    /**
     * 批量创建成果数据
     * @param surveyResults
     * @param tableName
     * @return
     */
    RespVO createBatch(List<SurveyResult> surveyResults, String tableName);

    /**
     * 查询上一条成果数据
     * @param surveyTime
     * @param tableName
     * @return
     */
    List<SurveyResult> queryPreResult(Date surveyTime, String tableName, int count,String pointCode);

    /**
     * 查询  初始成果数据
     * @param tableName
     * @return
     */
    SurveyResult queryInitResult(String tableName);

    /**
     * 获取数据列表
     * @param pageIndex
     * @param pageSize
     * @param sectionId
     * @param type
     * @return
     */
    PagedResult<OverrunListVo> queryOverrunList(int pageIndex, Integer pageSize, String sectionId,String workspaceId, Integer type);


    /**
     * 根据原始数据Id查询成果数据
     * @param sectionId
     * @param originalIds
     * @return
     */
    List<SurveyResult> queryResult(String sectionId,List<Long> originalIds);
}
