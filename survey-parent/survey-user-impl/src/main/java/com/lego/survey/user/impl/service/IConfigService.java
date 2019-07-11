package com.lego.survey.user.impl.service;

import com.lego.survey.user.model.entity.Config;
import com.survey.lib.common.page.PagedResult;
import com.survey.lib.common.vo.RespVO;

/**
 * @author yanglf
 * @description
 * @since 2019/1/3
 **/
public interface IConfigService {

    /**
     * 新增配置
     *
     * @param config
     */
    RespVO addConfig(Config config);


    /**
     * 删除配置信息
     *
     * @param id
     */
    RespVO delConfig(String id);

    /**
     * 根据id查询配置信息
     *
     * @param id
     * @return
     */
    Config queryById(String id);


    /**
     * 查询配置列表
     * @param pageIndex
     * @param pageSize
     * @return
     */
    PagedResult<Config> queryList(int pageIndex, int pageSize);


    /**
     * 通过名称获取配置信息
     * @param name
     * @return
     */
    Config  queryByName(String name);


    /**
     * 修改配置信息
     * @param config
     * @return
     */
    RespVO modify(Config config);

}
