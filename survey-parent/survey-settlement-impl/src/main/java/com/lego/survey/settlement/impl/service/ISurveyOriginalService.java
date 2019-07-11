package com.lego.survey.settlement.impl.service;
import com.lego.survey.settlement.model.entity.SurveyOriginal;
import com.lego.survey.settlement.model.vo.SurveyOriginalVo;
import com.survey.lib.common.vo.RespVO;
import java.util.List;
/**
 * @author yanglf
 * @description
 * @since 2019/1/8
 **/
public interface ISurveyOriginalService {


    /**
     * 查询测点列表
     * @param pageIndex   request data page
     * @param pageSize    per page size
     * @return  query result
     */
    List<SurveyOriginalVo>   list(int pageIndex, int pageSize, Long taskId, String tableName);


    /**
     *  create survey original
     * @param surveyOriginal   survey original
     * @return  create result
     */
    RespVO create(SurveyOriginal surveyOriginal, String tableName);


    /**
     * 修改原始数据信息
     * @param surveyOriginal
     * @param tableName
     * @return
     */
    RespVO   modify(SurveyOriginal surveyOriginal, String tableName);


    /**
     * 删除原始数据
     * @param ids
     * @param tableName
     * @return
     */
    RespVO delete(List<Long> ids, String tableName);


    /**
     * 批量上传原始数据
     * @param surveyOriginals
     * @param tableName
     * @return
     */
    RespVO createBatch(List<SurveyOriginal> surveyOriginals, String tableName);
}
