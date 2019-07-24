package com.lego.survey.settlement.impl.mapper;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.lego.survey.settlement.model.entity.SurveyPoint;
import com.lego.survey.settlement.model.vo.SurveyPointVo;
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
public interface SurveyPointMapper extends Mapper<SurveyPoint> {


    /**
     * 根据表名称查询测点列表
     *
     * @param tableName
     * @return
     */
    Page<SurveyPoint> queryList(IPage iPage, @Param("tableName") String tableName, @Param("ew") QueryWrapper queryWrapper);

    /**
     * 保存测点信息
     *
     * @param surveyPoint
     * @return
     */
    Integer save(@Param("surveyPoint") SurveyPoint surveyPoint, @Param("tableName") String tableName);


    /**
     * 修改 测点信息
     *
     * @param surveyPoint
     * @param tableName
     * @return
     */
    Integer modify(@Param("surveyPoint") SurveyPoint surveyPoint, @Param("tableName") String tableName);


    /**
     * 删除测点信息
     *
     * @return
     */
    Integer deletePoint(@Param("id") Long id, @Param("tableName") String tableName);


    /**
     * 批量删除测点
     *
     * @param codes
     * @param tableName
     * @return
     */
    Integer deleteBatch(@Param("codes") List<String> codes, @Param("tableName") String tableName);


    /**
     * 批量新增测点
     *
     * @param surveyPoints
     * @param tableName
     * @return
     */
    Integer saveBatch(@Param("surveyPoints") List<SurveyPoint> surveyPoints, @Param("tableName") String tableName);


    /**
     * 查询全部数据
     *
     * @param tableName
     * @param queryWrapper
     * @return
     */
    List<SurveyPoint> queryList(@Param("tableName") String tableName, @Param("ew") QueryWrapper<SurveyPoint> queryWrapper);

    /**
     * 根据名称或编号获取测点信息
     *
     * @param wrapper
     * @param tableName
     * @return
     */
    List<SurveyPoint> queryByNameOrCode(@Param("ew") QueryWrapper<SurveyPoint> wrapper, @Param("tableName") String tableName);

    /**
     * 根据名称获取测点信息
     * @param wrapper
     * @param tableName
     * @return
     */
    List<SurveyPoint> queryByName(@Param("ew") QueryWrapper<SurveyPoint> wrapper, @Param("tableName") String tableName);

    /**
     * 获取初始测点信息
     * @param tableName
     * @return
     */
    SurveyPointVo queryInitPoint(@Param("tableName") String tableName);
}
