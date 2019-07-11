package com.lego.survey.settlement.impl.mapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.lego.survey.settlement.model.entity.SurveyTask;
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
public interface SurveyTaskMapper extends Mapper<SurveyTask> {


    /**
     * 查询任务列表
     * @param tableName
     * @return
     */
    Page<SurveyTask> queryList(IPage iPage, @Param("tableName") String tableName, @Param("ew") QueryWrapper queryWrapper);

    /**
     * 保存任务
     * @param surveyTask
     * @return
     */
    Integer save(@Param("surveyTask") SurveyTask surveyTask, @Param("tableName") String tableName);


    /**
     * 修改 任务
     * @param surveyTask
     * @param tableName
     * @return
     */
    Integer modify(@Param("surveyTask") SurveyTask surveyTask, @Param("tableName") String tableName);


    /**
     * 删除任务
     * @param ids
     * @return
     */
    Integer deleteTask(@Param("ids") List<Long> ids, @Param("tableName") String tableName);

    /**
     * 批量删除任务
     * @param ids
     * @param tableName
     * @return
     */
    Integer deleteBatch(@Param("ids") List<Long> ids, @Param("tableName") String tableName);

    /**
     * 根据id 获取  测量任务信息
     * @param id
     * @param tableName
     * @return
     */
    SurveyTask queryById(@Param(value = "id") Long id, @Param("tableName") String tableName);
}
