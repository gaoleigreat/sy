package com.lego.survey.settlement.impl.mapper;

import com.lego.survey.settlement.model.entity.BasePoint;
import com.lego.survey.lib.mybatis.mapper.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * @author yanglf
 * @description
 * @since 2019/1/7
 **/
@Repository
public interface BasePointMapper extends Mapper<BasePoint> {


    /**
     * 根据标段code获取最近的版本号
     * @param sectionCode
     * @return
     */
    Integer   queryLastVersionBySectionCode(@Param(value = "sectionCode") String sectionCode);


    List<BasePoint> queryByCodeOrName(Map<String, Object> map);
}
