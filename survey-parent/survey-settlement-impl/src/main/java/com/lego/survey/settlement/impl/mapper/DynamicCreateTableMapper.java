package com.lego.survey.settlement.impl.mapper;
import com.lego.survey.lib.mybatis.mapper.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
/**
 * @author yanglf
 * @description  create table   mapper
 * @since 2019/1/7
 **/
@Repository
public interface DynamicCreateTableMapper extends Mapper {


    /**
     * 创建测量点表
     * @param tableName  the table name
     * @return create table result
     */
    @Transactional(rollbackFor = RuntimeException.class)
    Integer  createSurveyPointTable(@Param("tableName") String tableName);


    /**
     * 创建成果数据表
     * @param tableName the table name
     * @return  create table result
     */
    @Transactional(rollbackFor = RuntimeException.class)
    Integer createSurveyResultTable(@Param("tableName") String tableName);

    /**
     * 创建原始数据表
     * @param tableName the table name
     * @return create table result
     */
    @Transactional(rollbackFor = RuntimeException.class)
    Integer createSurveyOriginalTable(@Param("tableName") String tableName);

    /**
     * 创建任务表
     * @param tableName  the table name
     * @return create table result
     */
    @Transactional(rollbackFor = RuntimeException.class)
    Integer createSurveyTaskTable(@Param("tableName") String tableName);
}
