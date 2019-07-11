package com.lego.survey.auth.impl.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.lego.survey.auth.model.entity.Resources;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author yanglf
 * @description
 * @since 2019/7/10
 **/
@Repository
public interface ResourcesMapper extends BaseMapper<Resources> {

    /**
     * 查询权限点
     * @return
     */
    List<Resources> findList(Resources permission);

    /**
     * 新增权限点
     * @param permissions
     * @return
     */
    Integer insertList(List<Resources> permissions);

    /**
     * 删除权限点
     * @param ids
     * @return
     */
    Integer deleteList(List<Long> ids);


    /**
     * @param id
     * @return
     */
    Resources queryById(@Param(value = "id") Long id);
}
