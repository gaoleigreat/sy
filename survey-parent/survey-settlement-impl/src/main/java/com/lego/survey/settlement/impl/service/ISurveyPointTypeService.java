package com.lego.survey.settlement.impl.service;
import com.lego.survey.settlement.model.entity.SurveyPointType;
import com.lego.survey.settlement.model.vo.SurveyPointTypeVo;
import com.survey.lib.common.vo.RespDataVO;
import com.survey.lib.common.vo.RespVO;
/**
 * @author Wesley.Xia
 * @description
 * @since 2019/1/8 11:15
 **/
public interface ISurveyPointTypeService {
    /**
     * 获取点类型列表
     *
     * @param sectionCode
     * @param status
     * @return
     */
    RespVO<RespDataVO<SurveyPointTypeVo>> list(String sectionCode,Integer status);


    /**
     * 新增点类型
     *
     * @param surveyPointType
     * @return
     */
    RespVO create(SurveyPointTypeVo surveyPointType);


    /**
     * 删除点类型
     *
     * @param id
     * @return
     */
    RespVO delete(Long id);

    /**
     * 修改点类型
     *
     * @param surveyPointType
     * @return
     */
    RespVO modify(SurveyPointTypeVo surveyPointType);

    /**
     * 查询测点类型信息
     *
     * @param id
     * @return
     */
    RespVO<SurveyPointTypeVo> info(Long id);


    /**
     * 根据 标段编码 获取测点类型
     *
     * @param code
     * @return
     */
    RespDataVO<SurveyPointTypeVo> querySurveyPointTypeBySectionCode(String code);

    /**
     * 根据名称和编号获取测点类型信息
     * @param name
     * @param code
     * @return
     */
    SurveyPointType queryTypeByNameOrCode(String name, String code, String sectionCode);


    /**
     * 根据名称获取测点类型信息
     * @param name
     * @param sectionCode
     * @return
     */
    SurveyPointType queryByName(String name, String sectionCode);
}
