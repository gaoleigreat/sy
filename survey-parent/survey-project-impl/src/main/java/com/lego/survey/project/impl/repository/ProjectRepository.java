package com.lego.survey.project.impl.repository;

import com.lego.survey.project.model.entity.Project;
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
public interface ProjectRepository extends MongoRepository<Project,String> {


    /**
     * 通过工程id获取工程信息
     * @param id
     * @return
     */
    Project  findProjectByIdAndValid(String id, int valid);


    /**
     * 根据名称查询项目工程信息
     * @param name
     * @return
     */
    Project  findProjectByNameAndValid(String name, int valid);


    /**
     * 根据编号获取项目工程信息
     * @param code
     * @return
     */
    Project findProjectByCodeAndValid(String code, int valid);


    /**
     * 获取单位下所有的工程信息
     * @param group
     * @param valid
     * @return
     */
    List<Project>   findProjectsByGroupAndValidOrderByCreateTimeDesc(String group, int valid);


    /**
     * 查询所有工程
     * @param valid
     * @return
     */
    Page<Project> findProjectsByValid(int valid, Pageable pageable);












}
