package com.lego.survey.project.impl.service.impl;
import com.lego.survey.project.impl.repository.SectionRepository;
import com.lego.survey.project.impl.service.IWorkspaceService;
import com.lego.survey.project.model.entity.OwnWorkspace;
import com.lego.survey.project.model.entity.OwnerProject;
import com.lego.survey.project.model.entity.Section;
import com.lego.survey.project.model.entity.Workspace;
import com.survey.lib.common.vo.RespDataVO;
import com.survey.lib.common.vo.RespVO;
import com.survey.lib.common.vo.RespVOBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

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
        String sectionId = workSpace.getSection();
        Section section = sectionRepository.findSectionByIdAndValid(sectionId, 0);
        List<OwnWorkspace> workspaceList = section.getWorkSpace();
        OwnWorkspace workspace = OwnWorkspace.builder().id(workSpace.getId()).code(workSpace.getCode())
                .name(workSpace.getName())
                .type(workSpace.getType())
                .desc(workSpace.getDesc())
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
    public RespVO deleteWorkSpace(String workSpaceId) {
        Section section = sectionRepository.findSectionByWorkSpaceIdAndValid(workSpaceId,0);
        List<OwnWorkspace> workspaceList = section.getWorkSpace();
        workspaceList.forEach(x -> {
            if (x.getId().equals(workSpaceId)) {
                x.setValid(1);
            }
        });
        section.setUpdateTime(new Date());
        sectionRepository.save(section);
        return RespVOBuilder.success();
    }

    @Override
    public RespVO<RespDataVO<Workspace>> queryByProjectIdAndSectionId(String projectId, String sectionId) {
        Section section = sectionRepository.findSectionByIdAndValid(sectionId, 0);
        List<Workspace> workspaces = new ArrayList<>();
        if (section != null) {
            List<OwnWorkspace> workSpaces = section.getWorkSpace();
            if (workSpaces != null) {
                workSpaces.forEach(workSpace -> {
                    Workspace workspace = Workspace.builder()
                            .code(workSpace.getCode())
                            .desc(workSpace.getDesc())
                            .id(workSpace.getId())
                            .name(workSpace.getName())
                            .type(workSpace.getType())
                            .surveyer(workSpace.getSurveyer())
                            .project(projectId)
                            .build();
                    workspaces.add(workspace);
                });
            }
        }
        return RespVOBuilder.success(workspaces);
    }

    @Override
    public Workspace queryById(String id) {
        Section section = sectionRepository.findSectionByWorkSpaceIdAndValid(id, 0);
        if(section!=null){
            OwnerProject ownerProject = section.getOwnerProject();
            List<OwnWorkspace> workSpaces = section.getWorkSpace();
            if(!CollectionUtils.isEmpty(workSpaces)){
                OwnWorkspace ownWorkspace = workSpaces.get(0);
                Workspace workspace=new Workspace();
                workspace.setCode(ownWorkspace.getCode());
                workspace.setId(ownWorkspace.getId());
                workspace.setDesc(ownWorkspace.getDesc());
                workspace.setName(ownWorkspace.getName());
                workspace.setSurveyer(ownWorkspace.getSurveyer());
                workspace.setType(ownWorkspace.getType());
                if(ownerProject!=null){
                    String projectId = ownerProject.getId();
                    workspace.setProject(projectId);
                }
                return workspace;
            }
        }
        return null;
    }

}
