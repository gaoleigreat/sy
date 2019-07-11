package com.lego.survey.auth.impl.service;

import com.lego.survey.auth.model.entity.Resources;
import com.survey.lib.common.vo.RespVO;

import java.util.List;
import java.util.Map;

/**
 * @author yanglf
 * @description
 * @since 2019/7/10
 **/
public interface IResourcesService {

    /**
     * 查询权限点
     * @return
     */
    List<Resources> findList(Resources resources);

    /**
     * 查询权限点
     * @return
     */
    List<Map<String, Object>> findTree(Resources resources);

    /**
     * 新增权限点
     * @param resources
     * @return
     */
    RespVO insertList(List<Resources> resources);

    /**
     * 删除权限点
     * @param ids
     * @return
     */
    RespVO deleteList(List<Long> ids);

    /**
     * 增量更新
     * @param scope
     * @param resources
     * @return
     */
    RespVO save(String scope, List<Resources> resources);


    List<String> queryRoleResource(String role);
}
