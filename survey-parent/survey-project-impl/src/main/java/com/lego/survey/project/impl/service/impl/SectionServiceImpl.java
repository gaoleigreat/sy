package com.lego.survey.project.impl.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.lego.survey.project.impl.repository.GroupRepository;
import com.lego.survey.project.impl.repository.ProjectRepository;
import com.lego.survey.project.impl.repository.SectionRepository;
import com.lego.survey.project.impl.service.ISectionService;
import com.lego.survey.project.model.entity.*;
import com.lego.survey.project.model.vo.*;
import com.lego.survey.user.feign.UserClient;
import com.lego.survey.user.model.vo.UserVo;
import com.survey.lib.common.consts.RespConsts;
import com.survey.lib.common.page.PagedResult;
import com.survey.lib.common.utils.UuidUtils;
import com.survey.lib.common.vo.RespDataVO;
import com.survey.lib.common.vo.RespVO;
import com.survey.lib.common.vo.RespVOBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author yanglf
 * @description
 * @since 2018/12/27
 **/
@Service
public class SectionServiceImpl implements ISectionService {

    @Autowired
    private SectionRepository sectionRepository;

    @Autowired
    private ProjectRepository projectRepository;

    @Autowired
    private GroupRepository groupRepository;

    @Autowired
    private UserClient userClient;

    @Autowired
    private MongoTemplate mongoTemplate;

    @Override
    public RespVO add(Section section) {
        OwnerProject ownerProject = section.getOwnerProject();
        if (ownerProject != null) {
            // 新增  project 关联 section 信息
            String id = ownerProject.getId();
            Project project = projectRepository.findProjectByIdAndValid(id, 0);
            List<OwnerSection> sections = project.getSections();
            if (sections == null) {
                sections = new ArrayList<>();
            }
            sections.add(OwnerSection.builder().id(section.getId()).name(section.getName()).build());
            project.setSections(sections);
            project.setUpdateTime(new Date());
            projectRepository.save(project);
        }
        // 新增标段信息
        List<OwnWorkspace> workspaceList = new ArrayList<>();
        List<OwnWorkspace> workSpace = section.getWorkSpace();
        if (workSpace != null) {
            for (OwnWorkspace ownWorkspace : workSpace) {
                ownWorkspace.setId(UuidUtils.generateShortUuid());
                workspaceList.add(ownWorkspace);
            }
        }
        section.setValid(0);
        section.setWorkSpace(workspaceList);
        sectionRepository.insert(section);
        return RespVOBuilder.success();
    }

    @Override
    public RespVO modify(Section section) {
        //修改标段信息
        section.setValid(0);
        sectionRepository.save(section);
        return RespVOBuilder.success();
    }

    @Override
    public RespVO delete(String id) {
        Section section = sectionRepository.findSectionByIdAndValid(id, 0);
        section.setValid(1);
        sectionRepository.save(section);
        // TODO 更新关联表状态
        return RespVOBuilder.success();
    }

    @Override
    public RespVO<SectionAddVo> queryById(String id) {
        Section section = sectionRepository.findSectionByIdAndValid(id, 0);
        if (section != null) {
            SectionAddVo addVo = SectionAddVo.builder().address(section.getAddress())
                    .code(section.getCode())
                    .desc(section.getDesc())
                    .id(section.getId())
                    .name(section.getName())
                    .property(section.getProperty())
                    .services(section.getService()).build();
            OwnerProject ownerProject = section.getOwnerProject();
            if (ownerProject != null) {
                addVo.setProjectId(ownerProject.getId());
            }
            UpperGroup ownerGroup = section.getOwnerGroup();
            if (ownerGroup != null) {
                addVo.setGroupId(ownerGroup.getId());
            }
            return RespVOBuilder.success(addVo);
        }
        return RespVOBuilder.success();
    }

    @Override
    public Section findSectionById(String id) {
        return sectionRepository.findSectionByIdAndValid(id,0);
    }

    @Override
    public RespVO<RespDataVO<Section>> queryByMaster(String masterId) {
        List<Section> sectionList = sectionRepository.findSectionsByMasterIdAndValidOrderByCreateTimeDesc(masterId, 0);
        return RespVOBuilder.success(sectionList);
    }

    @Override
    public RespVO<Section> queryByWorkspaceId(String workspaceId) {
        Section section = sectionRepository.findSectionByWorkSpaceIdAndValid(workspaceId, 0);
        return RespVOBuilder.success(section);
    }

    @Override
    public RespVO<Section> queryByWorkspaceCode(String workspaceCode) {
        Section section = sectionRepository.findSectionByWorkSpaceCodeAndValid(workspaceCode, 0);
        return RespVOBuilder.success(section);
    }

    @Override
    public Section queryByCode(String code) {
        return sectionRepository.findSectionByCodeAndValid(code, 0);
    }

    @Override
    public Section queryByName(String name) {
        return sectionRepository.findSectionByNameAndValid(name, 0);
    }

    @Override
    public RespVO<RespDataVO<SectionVo>> queryByProjectId(String projectId) {
        List<SectionVo> sectionVos = new ArrayList<>();
        Project project = projectRepository.findProjectByIdAndValid(projectId, 0);
        List<OwnerSection> sectionList = project.getSections();
        if (sectionList != null) {
            for (OwnerSection ownerSection : sectionList) {
                Section section = sectionRepository.findSectionByIdAndValid(ownerSection.getId(), 0);
                UpperGroup ownerGroup = section.getOwnerGroup();
                Group group = groupRepository.findGroupByIdAndValid(ownerGroup.getId(), 0);
                SectionVo sectionVo = SectionVo.builder().build();
                sectionVo.setId(section.getId());
                sectionVo.setName(section.getName());
                sectionVo.setCode(section.getCode());
                sectionVo.setAddress(section.getAddress());
                sectionVo.setDesc(section.getDesc());
                sectionVo.setGroup(GroupVo.builder().name(group.getName()).upperGroup(group.getUpperGroup())
                        .desc(group.getDesc())
                        .id(group.getId()).build());
                List<WorkspaceVo> workspaceVos = getWorkspaceVos(section);
                sectionVo.setWorkspace(workspaceVos);

                List<ColleaguesVo> colleaguesVos = getColleaguesVos(section);
                sectionVo.setColleagues(colleaguesVos);
                sectionVos.add(sectionVo);
            }
        }
        return RespVOBuilder.success(sectionVos);
    }

    private List<ColleaguesVo> getColleaguesVos(Section section) {
        List<Surveyer> surveyers = section.getSurveyer();
        List<ColleaguesVo> colleaguesVos = new ArrayList<>();
        if (surveyers != null) {
            for (Surveyer surveyer : surveyers) {
                RespVO<UserVo> user = userClient.queryUserById(surveyer.getId());
                if (user.getRet().equals(RespConsts.SUCCESS)) {
                    UserVo info = user.getInfo();
                    colleaguesVos.add(ColleaguesVo.builder().name(info.getName())
                            .id(info.getId())
                            .role(JSONObject.toJSONString(info.getRole()))
                            .phone(info.getPhone()).build());
                }
            }
        }
        return colleaguesVos;
    }

    private List<WorkspaceVo> getWorkspaceVos(Section section) {
        List<WorkspaceVo> workspaceVos = new ArrayList<>();
        List<OwnWorkspace> workSpace = section.getWorkSpace();
        if (workSpace != null) {
            for (OwnWorkspace ownWorkspace : workSpace) {
                workspaceVos.add(WorkspaceVo.builder()
                        .name(ownWorkspace.getName())
                        .type(ownWorkspace.getType())
                        .code(ownWorkspace.getCode())
                        .id(ownWorkspace.getId())
                        .build());
            }
        }
        return workspaceVos;
    }

    @Override
    public PagedResult<SectionVo> list(int pageIndex, int pageSize, String projectId) {
        List<SectionVo> sectionVos = new ArrayList<>();
        PagedResult<SectionVo> pagedResult = new PagedResult<>();

        Query query = new Query();
        query.with(PageRequest.of(pageIndex - 1, pageSize, Sort.Direction.DESC, "createTime"));
        Criteria c = new Criteria();
        c.and("valid").is(0);
        if (!StringUtils.isEmpty(projectId)) {
            c.and("ownerProject._id").is(projectId);
        }
        query.addCriteria(c);
        List<Section> sectionList = mongoTemplate.find(query, Section.class, "section");
        if (!CollectionUtils.isEmpty(sectionList)) {
            for (Section section : sectionList) {
                UpperGroup ownerGroup = section.getOwnerGroup();
                List<ColleaguesVo> colleaguesVos = getColleaguesVos(section);
                SectionVo sectionVo = SectionVo.builder().id(section.getId())
                        .code(section.getCode())
                        .name(section.getName())
                        .address(section.getAddress())
                        .desc(section.getDesc())
                        .colleagues(colleaguesVos).build();
                if (ownerGroup != null) {
                    Group group = groupRepository.findGroupByIdAndValid(ownerGroup.getId(), 0);
                    if (group != null) {
                        sectionVo.setGroup(GroupVo.builder().id(group.getId()).desc(group.getDesc()).upperGroup(group.getUpperGroup())
                                .name(group.getName()).build());
                    }
                }
                sectionVos.add(sectionVo);
            }
        }
        pagedResult.setResultList(sectionVos);

        return getJsonObjectPagedResult(pageIndex, pageSize, query, sectionVos, mongoTemplate, "section");
    }


    private PagedResult<SectionVo> getJsonObjectPagedResult(int pageIndex, int pageSize, Query query, List<SectionVo> objectList, MongoTemplate mongoTemplate, String tableName) {
        PagedResult<SectionVo> pagedResult = new PagedResult<>();
        Long count = mongoTemplate.count(query, Long.class, tableName);
        pagedResult.setResultList(objectList);
        int totalPage = count.intValue() % pageSize == 0 ? count.intValue() / pageSize : count.intValue() / pageSize + 1;
        pagedResult.setPage(new com.survey.lib.common.page.Page(pageIndex, pageSize, 0, count, totalPage));
        return pagedResult;
    }


    @Override
    public List<Section> findAll(String projectId) {
        if (!StringUtils.isEmpty(projectId)) {
            return sectionRepository.findSectionsByOwnerProjectIdAndValidOrderByCreateTimeDesc(projectId, 0);
        }
        return sectionRepository.findAll();
    }

    @Override
    public List<Master> findBySectionMasterBySectionId(String sectionId) {
        Section section = sectionRepository.findSectionByIdAndValid(sectionId, 0);
        if (section != null) {
            return section.getMaster();
        }
        return null;
    }

    @Override
    public List<Surveyer> findSectionSurveyerBySectionId(String sectionId) {
        Section section = sectionRepository.findSectionByIdAndValid(sectionId, 0);
        if (section != null) {
            return section.getSurveyer();
        }
        return null;
    }

    @Override
    public List<Surveyer> findSurveyerByWorkspaceId(String workspaceId) {
        Section section = sectionRepository.findSectionByWorkSpaceIdAndValid(workspaceId, 0);
        if (section != null) {
            List<OwnWorkspace> workspaceList = section.getWorkSpace();
            for (OwnWorkspace ownWorkspace : workspaceList) {
                String id = ownWorkspace.getId();
                if (id.equals(workspaceId)) {
                    return ownWorkspace.getSurveyer();
                }
            }
        }
        return null;
    }
}
