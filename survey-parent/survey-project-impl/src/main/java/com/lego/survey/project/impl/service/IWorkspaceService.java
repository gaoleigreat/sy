package com.lego.survey.project.impl.service;

import com.lego.survey.project.model.entity.OwnWorkspace;
import com.lego.survey.project.model.entity.Workspace;
import com.survey.lib.common.vo.RespDataVO;
import com.survey.lib.common.vo.RespVO;

import java.util.List;

/**
 * The interface Workspace service.
 *
 * @author yanglf
 * @description
 * @since 2018 /12/27
 */
public interface IWorkspaceService {

    /**
     * 新增工区信息
     *
     * @param workSpace the work space
     * @return resp vo
     */
    RespVO add(Workspace workSpace);

    /**
     * 修改工区.
     *
     * @param workSpace the work space
     * @return the resp vo
     */
    RespVO modify(Workspace workSpace);

    /**
     * 删除工区.
     *
     * @return the resp vo
     */
    RespVO  deleteWorkSpace(String code);


    /**
     * 根据工程id和标段id获取工区信息
     * @param sectionCode
     * @return
     */
    RespVO<RespDataVO<Workspace>>  queryBySectionCode( String sectionCode);

    /**
     * 根据ID获取工区信息
     * @param id
     * @return
     */
    Workspace queryById(String id);

    /**
     * @param sectionCodes
     * @return
     */
    List<OwnWorkspace> findAll(List<String> sectionCodes);

    Workspace queryByCode(String code);
}
