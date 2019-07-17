package com.lego.survey.settlement.impl.mapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.lego.survey.settlement.model.entity.SurveyResult;
import com.lego.survey.lib.mybatis.mapper.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;
import java.util.List;

/**
 * @author yanglf
 * @description
 * @since 2019/1/7
 **/
@Repository
public interface SurveyResultMapper extends Mapper<SurveyResult> {


    /**
     * 根据表名称查询测点列表
     * @param tableName
     * @return
     */
    Page<SurveyResult> queryList(IPage iPage, @Param("tableName") String tableName, @Param("ew") QueryWrapper queryWrapper);


    /**
     * 根据表名称查询测点列表
     * @param tableName
     * @return
     */
    List<SurveyResult> queryList(@Param("tableName") String tableName, @Param("ew") QueryWrapper queryWrapper);

    /**
     * 保存成果数据
     * @param surveyResult
     * @return
     */
    Integer save(@Param("surveyResult") SurveyResult surveyResult, @Param("tableName") String tableName);


    /**
     * 修改 成果数据
     * @param surveyResult
     * @param tableName
     * @return
     */
    Integer modify(@Param("surveyResult") SurveyResult surveyResult, @Param("tableName") String tableName);


    /**
     * 批量删除成果数据
     * @param ids
     * @return
     */
    Integer deleteBatch(@Param("ids") List<Long> ids, @Param("tableName") String tableName);


    /**
     * 删除成果数据
     * @param id
     * @return
     */
    Integer deleteResult(@Param("id") Long id, @Param("tableName") String tableName);

    /**
     * 批量新增成果数据
     * @param surveyResults
     * @param tableName
     * @return
     */
    Integer saveBatch(@Param(value = "surveyResults") List<SurveyResult> surveyResults, @Param(value = "tableName") String tableName);

    /**
     * 查询上一条成果数据
     * @param wrapper
     * @param tableName
     * @return
     */
    List<SurveyResult> queryPreResult(@Param("ew") QueryWrapper<SurveyResult> wrapper, @Param("tableName") String tableName, @Param("count") int count);


    /**
     * 查询初始成果数据
     * @param wrapper
     * @param tableName
     * @return
     */
    SurveyResult queryInitResult(@Param("ew") QueryWrapper<SurveyResult> wrapper, @Param("tableName") String tableName);



    /**
     * 根据原始数据id查询成果数据
     * @param originalIds
     * @param tableName
     * @return
     */
    List<SurveyResult> queryResult(@Param("tableName") String tableName, @Param("originalIds") List originalIds);

}
