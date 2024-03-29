package com.lego.survey.project.impl.service.impl;

import com.lego.survey.project.impl.repository.SectionRepository;
import com.lego.survey.project.impl.service.IWorkspaceService;
import com.lego.survey.project.model.entity.OwnWorkspace;
import com.lego.survey.project.model.entity.OwnerProject;
import com.lego.survey.project.model.entity.Section;
import com.lego.survey.project.model.entity.Workspace;
import com.survey.lib.common.page.PagedResult;
import com.survey.lib.common.vo.RespDataVO;
import com.survey.lib.common.vo.RespVO;
import com.survey.lib.common.vo.RespVOBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.CriteriaDefinition;
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
public class WorkspaceServiceImpl implements IWorkspaceService {

    @Autowired
    private SectionRepository sectionRepository;

    @Override
    public RespVO add(Workspace workSpace) {
        String sectionCode = workSpace.getSection();
        Section section = sectionRepository.findSectionByCodeAndValid(sectionCode, 0);
        List<OwnWorkspace> workspaceList = section.getWorkSpace();
        OwnWorkspace workspace = OwnWorkspace.builder().id(workSpace.getId()).code(workSpace.getCode())
                .name(workSpace.getName())
                .type(workSpace.getType())
                .desc(workSpace.getDesc())
                .valid(0)
                .surveyer(workSpace.getSurveyer())
                .build();
        workspaceList.add(workspace);
        section.setUpdateTime(new Date());
        sectionRepository.save(section);
        return RespVOBuilder.success();
    }

    @Override
    public RespVO modify(Workspace workSpace) {
        String sectionId = workSpace.getSection();
        Section section = sectionRepository.findSectionByIdAndValid(sectionId, 0);
        List<OwnWorkspace> workspaceList = section.getWorkSpace();
        workspaceList.forEach(x -> {
            if (x.getId().equals(workSpace.getId())) {
                x.setName(workSpace.getName());
                x.setCode(workSpace.getCode());
                x.setSurveyer(workSpace.getSurveyer());
                x.setDesc(workSpace.getDesc());
                x.setType(workSpace.getType());
            }
        });
        section.setUpdateTime(new Date());
        sectionRepository.save(section);
        return RespVOBuilder.success();
    }

    @Override
    public RespVO deleteWorkSpace(String code) {
        Section section = sectionRepository.findSectionByWorkSpaceCodeAndValid(code, 0);
        if (section != null) {
            List<OwnWorkspace> workspaceList = section.getWorkSpace();
            if (!CollectionUtils.isEmpty(workspaceList)) {
                workspaceList.forEach(x -> {
                    if (x.getCode().equals(code)) {
                        x.setValid(1);
                    }
                });
                section.setUpdateTime(new Date());
                sectionRepository.save(section);
            }
        }
        return RespVOBuilder.success();
    }

    @Override
    public RespVO<RespDataVO<Workspace>> queryBySectionCode(String sectionCode) {
        Section section = sectionRepository.findSectionByCodeAndValid(sectionCode, 0);
        List<Workspace> workspaces = new ArrayList<>();
        if (section != null) {
            List<OwnWorkspace> workSpaces = section.getWorkSpace();
            if (workSpaces != null) {
                for (OwnWorkspace workSpace : workSpaces) {
                    Integer valid = workSpace.getValid();
                    if (valid == null || valid != 0) {
                        continue;
                    }
                    Workspace workspace = Workspace.builder()
                            .code(workSpace.getCode())
                            .desc(workSpace.getDesc())
                            .id(workSpace.getId())
                            .name(workSpace.getName())
                            .type(workSpace.getType())
                            .section(sectionCode)
                            .surveyer(workSpace.getSurveyer())
                            .project(section.getOwnerProject().getCode())
                            .build();
                    workspaces.add(workspace);
                }
            }
        }
        return RespVOBuilder.success(workspaces);
    }

    @Override
    public Workspace queryById(String id) {
        Section section = sectionRepository.findSectionByWorkSpaceIdAndValid(id, 0);
        if (section != null) {
            List<Workspace> workspaces = section.loadWorkspace();
            if (!CollectionUtils.isEmpty(workspaces)) {
                for (Workspace workspace : workspaces) {
                    String workspaceId = workspace.getId();
                    if (workspaceId.equals(id)) {
                        return workspace;
                    }
                }
            }
        }
        return null;
    }

    @Override
    public List<OwnWorkspace> findAll(List<String> sectionCodes) {
        List<OwnWorkspace> ownWorkspaceList = new ArrayList<>();
        if (CollectionUtils.isEmpty(sectionCodes)) {
            List<Section> sectionList = sectionRepository.findSectionsByValid(0);
            if (!CollectionUtils.isEmpty(sectionList)) {
                sectionList.forEach(section -> {
                    List<OwnWorkspace> workSpace = section.getWorkSpace();
                    if (!CollectionUtils.isEmpty(workSpace)) {
                        ownWorkspaceList.addAll(workSpace);
                    }
                });
            }
        } else {
            for (String sectionCode : sectionCodes) {
                Section section = sectionRepository.findSectionByCodeAndValid(sectionCode, 0);
                if (section != null) {
                    List<OwnWorkspace> workSpace = section.getWorkSpace();
                    if (!CollectionUtils.isEmpty(workSpace)) {
                        ownWorkspaceList.addAll(workSpace);
                    }
                }
            }
        }

        return ownWorkspaceList;
    }

    @Override
    public Workspace queryByCode(String code) {
        Section section = sectionRepository.findSectionByWorkSpaceCodeAndValid(code, 0);
        if (section != null) {
            List<Workspace> workspaces = section.loadWorkspace();
            if (!CollectionUtils.isEmpty(workspaces)) {
                for (Workspace workspace : workspaces) {
                    String workspaceCode = workspace.getCode();
                    if (workspaceCode.equals(code)) {
                        return workspace;
                    }
                }
            }
        }
        return null;
    }

}
