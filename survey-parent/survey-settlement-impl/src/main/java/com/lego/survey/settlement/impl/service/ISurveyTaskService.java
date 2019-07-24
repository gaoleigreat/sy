package com.lego.survey.settlement.impl.service;
import com.lego.survey.settlement.model.entity.SurveyTask;
import com.lego.survey.settlement.model.vo.SurveyTaskVo;
import com.survey.lib.common.page.PagedResult;
import com.survey.lib.common.vo.RespVO;
import java.util.List;
/**
 * @author yanglf
 * @description
 * @since 2019/1/8
 **/
public interface ISurveyTaskService {


    /**
     * 查询测点列表
     * @param pageIndex   request data page
     * @param pageSize    per page size
     * @return  query result
     */
    PagedResult<SurveyTaskVo> list(int pageIndex, int pageSize, String tableName);


    /**
     *  create survey task
     * @param surveyTaskVo   survey task
     * @return  create result
     */
    RespVO create(SurveyTaskVo surveyTaskVo, String sectionCode);


    /**
     * 修改任务
     * @param surveyTask
     * @param tableName
     * @return
     */
    RespVO modify(SurveyTask surveyTask, String tableName);


    /**
     * 删除任务
     * @param ids
     * @param tableName
     * @return
     */
    RespVO delete(List<Long> ids, String tableName);


}
