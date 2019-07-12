package com.lego.survey.settlement.impl.mapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.lego.survey.settlement.model.entity.SurveyOriginal;
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
public interface SurveyOriginalMapper extends Mapper<SurveyOriginal> {


    /**
     * 查询原始数据列表
     * @param tableName
     * @return
     */
    IPage<SurveyOriginal> queryList(Page iPage, @Param("tableName") String tableName, @Param("ew") QueryWrapper queryWrapper);


    /**
     * 查询原始数据列表
     * @param tableName
     * @return
     */
    List<SurveyOriginal> queryAll(@Param("tableName") String tableName, @Param("ew") QueryWrapper queryWrapper);

    /**
     * 保存原始数据
     * @param surveyOriginal
     * @return
     */
    Integer save(@Param("surveyOriginal") SurveyOriginal surveyOriginal, @Param("tableName") String tableName);


    /**
     * 修改 原始数据
     * @param surveyOriginal
     * @param tableName
     * @return
     */
    Integer modify(@Param("surveyOriginal") SurveyOriginal surveyOriginal, @Param("tableName") String tableName);


    /**
     * 批量删除数据
     * @return
     */
    Integer deleteBatch(@Param("ids") List<Long> ids, @Param("tableName") String tableName);


    /**
     * 根据id删除原始数据
     * @param id
     * @param tableName
     * @return
     */
    Integer deleteOriginal(@Param("id") Long id, @Param("tableName") String tableName);

    /**
     * 批量新增数据
     * @param surveyOriginals
     * @param tableName
     * @return
     */
    Integer saveBatch(@Param("surveyOriginals") List<SurveyOriginal> surveyOriginals, @Param("tableName") String tableName);
}
