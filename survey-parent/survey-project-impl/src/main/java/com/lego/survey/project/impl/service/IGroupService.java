package com.lego.survey.project.impl.service;
import com.lego.survey.project.model.entity.Group;
import com.lego.survey.project.model.vo.GroupTreeVo;
import com.lego.survey.project.model.vo.GroupVo;
import com.survey.lib.common.page.PagedResult;
import com.survey.lib.common.vo.RespDataVO;
import com.survey.lib.common.vo.RespVO;

import java.util.List;

/**
 * The interface Group service.
 *
 * @author Wesley.Xia
 * @description
 * @since 2018 /12/26 16:56
 */
public interface IGroupService {

    /**
     * 创建单位.
     *
     * @param group the group
     * @return the resp vo
     */
    RespVO add(Group group);

    /**
     * 修改单位.
     *
     * @param group the group
     * @return the group vo
     */
    GroupVo modify(Group group);

    /**
     * 通过单位id查询
     *
     * @param id the id
     * @return the group vo
     */
    GroupVo queryById(String id);

    /**
     * @param name the name
     * @return   the group
     */
    Group queryByName(String name);

    /**
     * 删除单位
     *
     * @param id the id
     */
    void delete(String id);

    /**
     * 查询所有单位
     *
     * @return the list
     */
    PagedResult<GroupVo> list(int pageIndex, int pageSize);

    /**
     * 查询全部母公司
     *
     * @param id the id
     * @return the list
     */
    RespDataVO<GroupVo> listCompleteGroup(String id);

    /**
     * 查询全部单位信息
     * @return
     */
    List<Group> findAll();

    /**
     * @return
     */
    List<GroupTreeVo> findTreeList();
}
