package com.lego.survey.project.impl.service.impl;

import com.lego.survey.project.impl.repository.GroupRepository;
import com.lego.survey.project.impl.repository.ProjectRepository;
import com.lego.survey.project.impl.repository.SectionRepository;
import com.lego.survey.project.impl.service.IProjectService;
import com.lego.survey.project.model.entity.*;
import com.lego.survey.project.model.vo.*;
import com.survey.lib.common.page.PagedResult;
import com.survey.lib.common.vo.CurrentVo;
import com.survey.lib.common.vo.RespDataVO;
import com.survey.lib.common.vo.RespVO;
import com.survey.lib.common.vo.RespVOBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author yanglf
 * @description
 * @since 2018/12/24
 **/
@Service
public class ProjectServiceImpl implements IProjectService {

    @Autowired
    private ProjectRepository projectRepository;

    @Autowired
    private SectionRepository sectionRepository;

    @Autowired
    private GroupRepository groupRepository;

    @Override
    public RespVO add(Project project) {
        projectRepository.save(project);
        return RespVOBuilder.success();
    }

    @Override
    public RespVO modify(Project project) {
        project.setUpdateTime(new Date());
        project.setValid(0);
        projectRepository.save(project);
        List<Section> sectionList = sectionRepository.findSectionsByOwnerProjectIdAndValidOrderByCreateTimeDesc(project.getId(), 0);
        if (sectionList != null) {
            sectionList.forEach(section -> {
                OwnerProject ownerProject = section.getOwnerProject();
                if (ownerProject != null) {
                    ownerProject.setName(project.getName());
                    ownerProject.setCode(project.getCode());
                    sectionRepository.save(section);
                }
            });
        }
        return RespVOBuilder.success();
    }

    @Override
    public RespVO<RespDataVO<ProjectVo>> queryByUserId(String userId) {
        List<ProjectVo> projectVos = new ArrayList<>();
        List<SectionVo> sectionVos = new ArrayList<>();
        // 根据用户id查询 workSpace 信息  (关联 master/surveyer id)
        List<Section> sectionList = sectionRepository.findSectionsByMasterIdAndValidOrderByCreateTimeDesc(userId, 0);
        for (Section section : sectionList) {
            ProjectVo projectVo = ProjectVo.builder().build();
            SectionVo sectionVo = SectionVo.builder().
                    code(section.getCode()).id(section.getId())
                    .address(section.getAddress())
                    .desc(section.getDesc())
                    .name(section.getName())
                    .build();
            OwnerProject ownerProject = section.getOwnerProject();
            if (ownerProject != null) {
                // 根据标段id 在 project 表查询 工程信息
                String id = ownerProject.getId();

                Project project = projectRepository.findProjectByIdAndValid(id, 0);
                String projectGroup = project.getGroup();
                projectVo.setCode(project.getCode());
                projectVo.setDesc(project.getDesc());
                projectVo.setName(project.getName());
                projectVo.setAddress(project.getAddress());
                projectVo.setId(project.getId());
                projectVo.setGroup(projectGroup);

                UpperGroup ownerGroup = section.getOwnerGroup();
                Group group = groupRepository.findGroupByIdAndValid(ownerGroup.getId(), 0);
                if (group != null) {
                    sectionVo.setGroup(GroupVo.builder().id(group.getId()).name(group.getName()).desc(group.getDesc()).upperGroup(group.getUpperGroup()).build());
                }
                List<OwnWorkspace> ownWorkspaces = section.getWorkSpace();
                List<Double> property = section.getProperty();
                if (property.size() >= 2) {
                    sectionVo.setStartingMileage(section.getProperty().get(0));
                    sectionVo.setEndMileage(section.getProperty().get(1));
                }
                List<WorkspaceVo> workAreaVos = new ArrayList<>();
                sectionVo.setWorkspace(workAreaVos);
                if (!CollectionUtils.isEmpty(ownWorkspaces)) {
                    for (OwnWorkspace ownWorkspace : ownWorkspaces) {
                        WorkspaceVo workspaceVo = WorkspaceVo.builder().id(ownWorkspace.getId()).name(ownWorkspace.getName()).type(ownWorkspace.getType()).code(ownWorkspace.getCode()).build();
                        workAreaVos.add(workspaceVo);
                        List<Surveyer> surveyers = ownWorkspace.getSurveyer();
                        List<ColleaguesVo> colleagues = new ArrayList<>();
                        sectionVo.setColleagues(colleagues);
                        for (Surveyer surveyer : surveyers) {
                            colleagues.add(ColleaguesVo.builder().id(surveyer.getId()).name(surveyer.getName()).build());
                        }
                    }
                }
            }
            sectionVos.add(sectionVo);
            projectVo.setSections(sectionVos);
            projectVos.add(projectVo);
        }
        return RespVOBuilder.success(projectVos);
    }

    @Override
    public Project queryByName(String name) {
        return projectRepository.findProjectByNameAndValid(name, 0);
    }

    @Override
    public Project queryByCode(String code) {
        return projectRepository.findProjectByCodeAndValid(code, 0);
    }

    @Override
    public RespVO<ProjectVo> queryById(String id) {
        Project project = projectRepository.findProjectByIdAndValid(id, 0);
        String group = project.getGroup();
        return RespVOBuilder.success(ProjectVo.builder()
                .code(project.getCode())
                .desc(project.getDesc())
                .name(project.getName())
                .id(project.getId())
                .group(group)
                .address(project.getAddress())
                .build());
    }

    @Override
    public RespVO deleteProject(String projectId) {
        Project project = projectRepository.findProjectByIdAndValid(projectId, 0);
        project.setValid(1);
        projectRepository.save(project);
        //TODO 更新关联表数据状态

        return RespVOBuilder.success();
    }


    @Override
    public PagedResult<ProjectVo> list(int pageSize, int pageIndex) {
        List<ProjectVo> projectVos = new ArrayList<>();
        PagedResult<ProjectVo> pagedResult = new PagedResult<>();
        Pageable pageable = PageRequest.of(pageIndex - 1, pageSize, Sort.Direction.DESC, "createTime");
        Page<Project> projects = projectRepository.findProjectsByValid(0, pageable);
        List<Project> projectList = projects.getContent();
        pagedResult.setPage(new com.survey.lib.common.page.Page(pageIndex, pageSize, 0, projects.getTotalElements(), projects.getTotalPages()));
        for (Project project : projectList) {
            String group = project.getGroup();
            ProjectVo projectVo = ProjectVo.builder()
                    .code(project.getCode())
                    .desc(project.getDesc())
                    .address(project.getAddress())
                    .group(group)
                    .id(project.getId())
                    .name(project.getName()).build();
            projectVos.add(projectVo);
        }
        pagedResult.setResultList(projectVos);
        return pagedResult;
    }

    @Override
    public List<OwnerProject> findAll(String role, String userId) {
        List<OwnerProject> ownerProjects = new ArrayList<>();
        if (StringUtils.isEmpty(role)) {
            return null;
        }
        List<Section> sectionList;
        if (role.equals("master")) {
            // 标段管理员
            sectionList = sectionRepository.findSectionsByMasterIdAndValid(userId, 0);
        } else if (role.equals("admin")) {
            // 超級管理員
            List<Project> projects = projectRepository.findAll();
            if (!CollectionUtils.isEmpty(projects)) {
                projects.forEach(project -> ownerProjects.add(new OwnerProject(project.getId(), project.getName(), project.getCode())));
            }
            return ownerProjects;
        } else {
            return ownerProjects;
        }
        return sectionList.stream().map(Section::getOwnerProject).collect(Collectors.toList());
    }

    @Override
    public List<Project> findByCurrent(CurrentVo currentVo) {
        String role = currentVo.getRole();
        if (role.equalsIgnoreCase("admin")) {
            return projectRepository.findAll();
        }

        List<Project> projects = new ArrayList<>();
        List<String> projectIds = currentVo.getProjectIds();
        if (!CollectionUtils.isEmpty(projectIds)) {
            for (String projectId : projectIds) {
                Project project = projectRepository.findProjectByIdAndValid(projectId, 0);
                if (project != null) {
                    projects.add(project);
                }
            }
        }
        return projects;
    }

    @Override
    public List<TreeVo> findTreeByCurrent(CurrentVo currentVo) {
        List<Project> projects = findByCurrent(currentVo);
        return getProjectTreeVos(projects);
    }

    private List<TreeVo> getProjectTreeVos(List<Project> projects) {
        List<TreeVo> projectTreeVos = new ArrayList<>();
        if (!CollectionUtils.isEmpty(projects)) {
            for (Project project : projects) {
                TreeVo projectTreeVo = new TreeVo();
                projectTreeVo.setId(project.getId());
                projectTreeVo.setCode(project.getCode());
                projectTreeVo.setName(project.getName());
                projectTreeVos.add(projectTreeVo);
                List<OwnerSection> sections = project.getSections();
                if (!CollectionUtils.isEmpty(sections)) {
                    List<TreeVo> sectionTreeVos = new ArrayList<>();
                    for (OwnerSection ownerSection : sections) {
                        String id = ownerSection.getId();
                        Section section = sectionRepository.findSectionByIdAndValid(id, 0);
                        if (section != null) {
                            TreeVo treeVo = TreeVo.builder()
                                    .id(section.getId())
                                    .name(section.getName())
                                    .code(section.getCode()).build();
                            sectionTreeVos.add(treeVo);
                            List<OwnWorkspace> workSpaces = section.getWorkSpace();
                            if (!CollectionUtils.isEmpty(workSpaces)) {
                                List<TreeVo> workSpaceTreeVos = new ArrayList<>();
                                for (OwnWorkspace workSpace : workSpaces) {
                                    workSpaceTreeVos.add(TreeVo.builder()
                                            .id(workSpace.getId())
                                            .code(workSpace.getCode())
                                            .name(workSpace.getName()).build());
                                }
                                treeVo.setTreeVos(workSpaceTreeVos);
                            }
                        }
                    }
                    projectTreeVo.setTreeVos(sectionTreeVos);
                }
            }
        }
        return projectTreeVos;
    }
}
