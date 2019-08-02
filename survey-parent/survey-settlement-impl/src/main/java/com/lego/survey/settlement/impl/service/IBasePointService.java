package com.lego.survey.settlement.impl.service;

import com.lego.survey.settlement.model.entity.BasePoint;
import com.lego.survey.settlement.model.vo.BasePointVo;
import com.survey.lib.common.vo.RespVO;

import java.util.Date;
import java.util.List;

/**
 * @author yanglf
 * @description
 * @since 2019/1/7
 **/
public interface IBasePointService {

    /**
     * 获取基准点列表
     * @param sectionCode
     * @return
     */
    List<BasePointVo>  list(String sectionCode, String name, Date startDate, Date endDate);


    /**
     * 新增基准点
     * @param basePoint
     * @return
     */
    RespVO create(BasePoint basePoint);


    /**
     * 删除基准点
     * @param codes
     * @return
     */
    RespVO delete(List<String> codes);

    /**
     * 修改基准点信息
     * @param basePoint
     * @return
     */
    RespVO modify(BasePoint basePoint);

    /**
     * 查询基准点信息
     * @param id
     * @return
     */
    RespVO<BasePoint> info(Long id);


    /**
     * 根据 标段编码 获取最近一条数据版本
     * @param sectionCode
     * @return
     */
    int queryLastVersionBySectionCode(String sectionCode);


    /**
     * 根据 code 获取 name 查询基准点
     * @param code
     * @param name
     * @param sectionCode
     * @return
     */
    BasePoint queryByCodeOrName(String code, String name, String sectionCode);

    /**
     * @param code
     * @return
     */
    BasePoint queryByCode(String code);
}
