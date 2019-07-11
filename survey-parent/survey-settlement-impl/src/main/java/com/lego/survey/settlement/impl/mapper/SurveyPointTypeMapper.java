package com.lego.survey.settlement.impl.mapper;

import com.lego.survey.settlement.model.entity.SurveyPointType;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;
import com.lego.survey.lib.mybatis.mapper.Mapper;

import java.util.List;

/**
 * The interface Survey point type mapper.
 *
 * @author Wesley.Xia
 * @description
 * @since 2019 /1/8 11:50
 */
@Repository
public interface SurveyPointTypeMapper extends Mapper<SurveyPointType> {

    /**
     * 获取制定标段下的测点类型
     *
     * @param sectioncode the code
     * @return the list
     */
    List<SurveyPointType> querySurveyPointTypeBySectionCode(@Param(value = "sectioncode") String sectioncode);



}
