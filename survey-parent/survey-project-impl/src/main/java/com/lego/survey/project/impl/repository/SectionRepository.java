package com.lego.survey.project.impl.repository;

import com.lego.survey.project.model.entity.Section;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author yanglf
 * @description
 * @since 2018/12/21
 **/
@Repository
public interface SectionRepository extends MongoRepository<Section, String> {

    /**
     * 根据id获取标段信息
     *
     * @param id
     * @param valid
     * @return
     */
    Section findSectionByIdAndValid(String id, int valid);


    List<Section> findSectionsByValid(int valid);


    /**
     * @param valid
     * @param pageable
     * @return
     */
    Page<Section> findSectionsByValidOrderByCreateTimeDesc(int valid, Pageable pageable);


    /**
     * 查询用户下的标段
     *
     * @param masterId
     * @param valid
     * @return
     */
    List<Section> findSectionsByMasterIdAndValidOrderByCreateTimeDesc(String masterId, int valid);


    /**
     * 获取工程项目下所有的标段信息
     *
     * @param projectId
     * @return
     */
    List<Section> findSectionsByOwnerProjectIdAndValidOrderByCreateTimeDesc(String projectId, int valid);


    List<Section> findSectionsByOwnerProjectCodeAndValidOrderByCreateTimeDesc(String projectCode, int valid);


    /**
     * 根据单位id获取标段信息
     *
     * @param groupId
     * @param valid
     * @return
     */
    List<Section> findSectionsByOwnerGroupIdAndValidOrderByCreateTimeDesc(String groupId, int valid);


    /**
     * 根据编号查询标段信息
     *
     * @param code
     * @param valid
     * @return
     */
    Section findSectionByCodeAndValid(String code, int valid);


    /**
     * 根据名称获取标段信息
     *
     * @param name
     * @param valid
     * @return
     */
    Section findSectionByNameAndValid(String name, int valid);


    /**
     * 根据工区id获取标段信息
     *
     * @param workspaceId
     * @param valid
     * @return
     */
    Section findSectionByWorkSpaceIdAndValid(String workspaceId, int valid);


    /**
     * 根据工区编号获取标段信息
     *
     * @param workspaceCode
     * @param valid
     * @return
     */
    Section findSectionByWorkSpaceCodeAndValid(String workspaceCode, int valid);

    /**
     * 根据  master 角色 获取标段信息
     *
     * @param masterId
     * @param valid
     * @return
     */
    List<Section> findSectionsByMasterIdAndValid(String masterId, int valid);


    /**
     * 获取测量员所属的项目
     *
     * @param surveyId
     * @param valid
     * @return
     */
    List<Section> findSectionsBySurveyerIdAndValid(String surveyId, int valid);


}
