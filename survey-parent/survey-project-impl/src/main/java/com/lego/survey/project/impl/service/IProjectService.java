package com.lego.survey.project.impl.service;
import com.lego.survey.project.model.entity.OwnerProject;
import com.lego.survey.project.model.entity.Project;
import com.lego.survey.project.model.vo.ProjectVo;
import com.survey.lib.common.page.PagedResult;
import com.survey.lib.common.vo.CurrentVo;
import com.survey.lib.common.vo.RespDataVO;
import com.survey.lib.common.vo.RespVO;

import java.util.List;

/**
 * @author yanglf
 * @description
 * @since 2018/12/24
 **/
public interface IProjectService {

    /**
     * 新增工程项目
     * @param project
     * @return
     */
    RespVO  add(Project project);


    /**
     * 修改项目信息
     * @param project
     * @return
     */
    RespVO modify(Project project);



    /**
     * 根据用户id获取工程信息
     * @param userId
     * @return
     */
    RespVO<RespDataVO<ProjectVo>> queryByUserId(String userId);


    /**
     * 根据名称查询项目工程信息
     * @param name
     * @return
     */
    Project  queryByName(String name);


    /**
     * 根据 code 查询项目工程信息
     * @param code
     * @return
     */
    Project queryByCode(String code);


    /**
     * 通过id获取工程信息
     * @param id
     * @return
     */
    RespVO<ProjectVo> queryById(String id);


    /**
     * 删除工程项目
     * @param projectId
     * @return
     */
    RespVO   deleteProject(String projectId);


    /**
     * 查询工程列表信息
     * @param pageSize
     * @param pageIndex
     * @return
     */
    PagedResult<ProjectVo>  list(int pageSize, int pageIndex);


    /**
     * 查询全部工程
     * @return
     */
    List<OwnerProject>   findAll(String role, String userId);

    List<Project> findByCurrent(CurrentVo currentVo);
}
