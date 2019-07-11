package com.lego.survey.project.impl.repository;
import com.lego.survey.project.model.entity.Group;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author yanglf
 * @description
 * @since 2018/12/21
 **/
@Repository
public interface GroupRepository extends MongoRepository<Group,String> {


    /**
     * 根据id获取 单位信息
     * @param id
     * @param valid
     * @return
     */
    Group findGroupByIdAndValid(String id, int valid);


    /**
     * 查询子公司信息
     * @param upperGroupId
     * @param valid
     * @return
     */
    List<Group>  findGroupByUpperGroupIdAndValidOrderByCreateTimeDesc(String upperGroupId, int valid);

    /**
     * 根据名称查询单位信息
     * @param name
     * @param valid
     * @return
     */
    Group findGroupByNameAndValid(String name, int valid);


    /**
     * 根据状态查询公司
     * @param valid
     * @return
     */
    List<Group>  findGroupsByValid(int valid);


}
