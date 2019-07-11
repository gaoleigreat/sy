package com.lego.survey.project.impl.service.impl;

import com.lego.survey.project.impl.repository.GroupRepository;
import com.lego.survey.project.impl.repository.ProjectRepository;
import com.lego.survey.project.impl.repository.SectionRepository;
import com.lego.survey.project.impl.service.IGroupService;
import com.lego.survey.project.model.entity.Group;
import com.lego.survey.project.model.entity.UpperGroup;
import com.lego.survey.project.model.vo.GroupTreeVo;
import com.lego.survey.project.model.vo.GroupVo;
import com.survey.lib.common.page.PagedResult;
import com.survey.lib.common.vo.RespDataVO;
import com.survey.lib.common.vo.RespVO;
import com.survey.lib.common.vo.RespVOBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * @author Wesley.Xia
 * @description
 * @since 2018/12/26
 **/
@Service
public class GroupServiceImpl implements IGroupService {

    @Autowired
    private GroupRepository groupRepository;

    @Autowired
    private ProjectRepository projectRepository;

    @Autowired
    private SectionRepository sectionRepository;


    @Override
    public RespVO add(Group group) {
        Date currentTime = new Date();
        group.setCreateTime(currentTime);
        group.setUpdateTime(currentTime);
        group.setValid(0);
        groupRepository.insert(group);
        return RespVOBuilder.success();
    }

    @Override
    public GroupVo modify(Group group) {
        group.setUpdateTime(new Date());
        group.setValid(0);
        Group insert = groupRepository.save(group);
        return GroupVo.builder().id(insert.getId()).name(insert.getName()).desc(insert.getDesc()).upperGroup(insert.getUpperGroup()).build();
    }

    @Override
    public GroupVo queryById(String id) {
        Group group = groupRepository.findGroupByIdAndValid(id, 0);
        return GroupVo.builder().name(group.getName())
                .id(group.getId())
                .desc(group.getDesc())
                .upperGroup(group.getUpperGroup())
                .address(group.getAddress())
                .internetSite(group.getInternetSite())
                .contactNumber(group.getContactNumber())
                .build();
    }

    @Override
    public Group queryByName(String name) {
        return groupRepository.findGroupByNameAndValid(name, 0);
    }

    @Override
    public void delete(String id) {
        Group group = groupRepository.findGroupByIdAndValid(id, 0);
        if(group!=null){
            group.setValid(1);
            groupRepository.save(group);
        }
        //TODO 更新关联表状态
    }

    @Override
    public PagedResult<GroupVo> list(int pageIndex, int pageSize) {
        PagedResult<GroupVo> pagedResult = new PagedResult<>();
        List<GroupVo> groupVos = new ArrayList<>();
        Pageable pageable = PageRequest.of(pageIndex - 1, pageSize, Sort.Direction.DESC, "createTime");
        Page<Group> groupPage = groupRepository.findAll(pageable);
        pagedResult.setPage(new com.survey.lib.common.page.Page(pageIndex, pageSize, 0, groupPage.getTotalElements(), groupPage.getTotalPages()));
        List<Group> groupList = groupPage.getContent();
        for (Group group : groupList) {
            groupVos.add(GroupVo.builder().id(group.getId())
                    .name(group.getName())
                    .desc(group.getDesc())
                    .address(group.getAddress())
                    .internetSite(group.getInternetSite())
                    .contactNumber(group.getContactNumber())
                    .upperGroup(group.getUpperGroup()).build());
        }
        pagedResult.setResultList(groupVos);
        return pagedResult;
    }

    @Override
    public RespDataVO<GroupVo> listCompleteGroup(String id) {
        List<GroupVo> groupVos = new ArrayList<>();
        Group upperGroup = groupRepository.findGroupByIdAndValid(id, 0);
        List<Group> groupList = groupRepository.findGroupByUpperGroupIdAndValidOrderByCreateTimeDesc(id, 0);
        if (groupList != null) {
            for (Group group : groupList) {
                groupVos.add(GroupVo.builder().name(group.getName())
                        .desc(group.getDesc())
                        .id(group.getId())
                        .contactNumber(group.getContactNumber())
                        .address(group.getAddress())
                        .internetSite(group.getInternetSite())
                        .upperGroup(UpperGroup.builder().id(upperGroup.getId())
                                .name(upperGroup.getName()).build()).build());
            }
        }
        return new RespDataVO<>(groupVos);
    }

    @Override
    public List<Group> findAll() {
        return groupRepository.findGroupsByValid(0);
    }

    @Override
    public List<GroupTreeVo> findTreeList() {
        List<Group> groupList = groupRepository.findGroupsByValid(0);
        List<GroupTreeVo> groupTreeVos=new ArrayList<>();
        for (Group group: groupList) {
            GroupTreeVo groupTreeVo=new GroupTreeVo();
            if (group.getUpperGroup() == null) {
                groupTreeVo.setId(group.getId());
                groupTreeVo.setName(group.getName());
                groupTreeVo.setChildGroup(getChildGroup(group.getId(), groupList));
                groupTreeVos.add(groupTreeVo);
            }
        }
        return groupTreeVos;
    }


    private List<GroupTreeVo> getChildGroup(String id, List<Group> groupList) {
        List<GroupTreeVo> groupTreeVos=new ArrayList<>();
        for (Group group : groupList) {
            GroupTreeVo groupTreeVo=new GroupTreeVo();
            UpperGroup upperGroup = group.getUpperGroup();
            if (upperGroup!=null && id.equals(upperGroup.getId())) {
                groupTreeVo.setId(group.getId());
                groupTreeVo.setName(group.getName());
                groupTreeVo.setChildGroup(getChildGroup(group.getId(), groupList));
                groupTreeVos.add(groupTreeVo);
            }
        }
        return groupTreeVos;
    }


}
