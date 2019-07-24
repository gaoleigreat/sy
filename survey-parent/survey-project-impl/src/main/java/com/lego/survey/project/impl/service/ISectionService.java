package com.lego.survey.project.impl.service;
import com.lego.survey.project.model.entity.Master;
import com.lego.survey.project.model.entity.Section;
import com.lego.survey.project.model.entity.Surveyer;
import com.lego.survey.project.model.vo.SectionAddVo;
import com.lego.survey.project.model.vo.SectionVo;
import com.survey.lib.common.page.PagedResult;
import com.survey.lib.common.vo.RespDataVO;
import com.survey.lib.common.vo.RespVO;

import java.util.List;

/**
 * @author yanglf
 * @description
 * @since 2018/12/27
 **/
public interface ISectionService {

    /**
     * 新增工区信息
     *
     * @param section
     * @return
     */
    RespVO add(Section section);


    /**
     * 修改标段信息
     *
     * @param section
     * @return
     */
    RespVO modify(Section section);


    /**
     * 删除标段
     *
     * @param code
     * @return
     */
    RespVO delete(String code);


    /**
     * 通过id查询标段信息
     * @param id
     * @return
     */
    RespVO<SectionAddVo>   queryById(String id);


    Section   findSectionById(String id);


    /**
     * 根据master获取标段信息
     * @param masterId
     * @return
     */
    RespVO<RespDataVO<Section>>  queryByMaster(String masterId);


    /**
     * 根据工区id获取标段信息
     * @param workspaceId
     * @return
     */
    RespVO<Section>  queryByWorkspaceId(String workspaceId);


    /**
     * 根据工区编码获取标段信息
     * @param workspaceCode
     * @return
     */
    RespVO<Section>  queryByWorkspaceCode(String workspaceCode);


    /**
     * 通过编号查询标段信息
     * @param code
     * @return
     */
    SectionAddVo  queryByCode(String code);


    /**
     * 根据名称获取标段信息
     * @param name
     * @return
     */
    Section queryByName(String name);


    /**
     * 获取工程下的所有标段
     * @return
     */
    RespVO<RespDataVO<SectionVo>> queryByProjectId(String projectId);


    RespVO<RespDataVO<SectionVo>> queryByProjectCode(String projectCode);


    /**
     * 查询标段列表
     * @param pageIndex
     * @param pageSize
     * @return
     */
    PagedResult<SectionVo> list(int pageIndex, int pageSize, String projectCode);

    /**
     * 查询标段信息
     * @param projectCodes
     * @return
     */
    List<Section> findAll(List<String> projectCodes);


    /**
     * 根据标段ID获取标段管理员信息
     * @param sectionId
     * @return
     */
    List<Master>  findBySectionMasterBySectionId(String sectionId);


    /**
     * 根据标段ID获取标段测量员
     * @param sectionId
     * @return
     */
    List<Surveyer>    findSectionSurveyerBySectionId(String sectionId);


    /**
     * 根据工区ID获取工区测量员
     * @param workspaceId
     * @return
     */
    List<Surveyer>   findSurveyerByWorkspaceId(String workspaceId);


    List<Master> findBySectionMasterBySectionCode(String sectionCode);

    List<Surveyer> findSurveyerBySectionCode(String sectionCode);

    List<Surveyer> findSurveyerByWorkspaceCode(String workspaceCode);
}
