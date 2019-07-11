package com.lego.survey.project.impl.controller;

import com.lego.survey.auth.feign.AuthClient;
import com.lego.survey.event.project.SectionSource;
import com.lego.survey.base.annotation.Operation;
import com.lego.survey.base.annotation.Resource;
import com.lego.survey.project.impl.service.IGroupService;
import com.lego.survey.project.impl.service.IProjectService;
import com.lego.survey.project.impl.service.ISectionService;
import com.lego.survey.project.model.entity.*;
import com.lego.survey.project.model.vo.GroupVo;
import com.lego.survey.project.model.vo.ProjectVo;
import com.lego.survey.project.model.vo.SectionAddVo;
import com.lego.survey.project.model.vo.SectionVo;
import com.lego.survey.event.user.LogSender;
import com.lego.survey.user.model.entity.OwnProject;
import com.survey.lib.common.consts.DictConstant;
import com.survey.lib.common.consts.RespConsts;
import com.survey.lib.common.page.PagedResult;
import com.survey.lib.common.utils.HeaderUtils;
import com.survey.lib.common.utils.HttpUtils;
import com.survey.lib.common.utils.UuidUtils;
import com.survey.lib.common.vo.HeaderVo;
import com.survey.lib.common.vo.RespDataVO;
import com.survey.lib.common.vo.RespVO;
import com.survey.lib.common.vo.RespVOBuilder;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.integration.support.MessageBuilder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * @author yanglf
 * @description
 * @since 2018/12/21
 **/
@Validated
@RestController
@RequestMapping(DictConstant.Path.SECTION)
@Api(value = "SectionController", description = "标段管理")
@Resource(value = "section", desc = "标段管理")
public class SectionController {

    @Autowired
    private ISectionService iSectionService;

    @Autowired
    private AuthClient authClient;

    @Autowired
    private LogSender logSender;

    @Autowired
    private SectionSource sectionSource;

    @Autowired
    private IProjectService iProjectService;

    @Autowired
    private IGroupService iGroupService;

    @ApiOperation(value = "新增标段", notes = "新增标段", httpMethod = "POST")
    @ApiImplicitParams({

    })
    @Operation(value = "create", desc = "新增标段")
    @RequestMapping(value = "/create", method = RequestMethod.POST)
    public RespVO create(@Validated @RequestBody SectionAddVo sectionAddVo, HttpServletRequest request) {
        String sectionName = sectionAddVo.getName();
        Section queryByName = iSectionService.queryByName(sectionName);
        if (queryByName != null) {
            return RespVOBuilder.failure(RespConsts.FAIL_RESULT_CODE, "标段名称已经存在");
        }
        String sectionCode = sectionAddVo.getCode();
        Section queryByCode = iSectionService.queryByCode(sectionCode);
        if (queryByCode != null) {
            return RespVOBuilder.failure(RespConsts.FAIL_RESULT_CODE, "标段编号已经存在");
        }
        HeaderVo headerVo = HeaderUtils.parseHeader(request);
        String userId = authClient.getAuthVo(headerVo).getUserId();
        //TODO 权限校验
        Section section = getSection(sectionAddVo);
        RespVO respVO = iSectionService.add(section);
        if (respVO.getRetCode() == RespConsts.SUCCESS_RESULT_CODE) {

            List<String> service = sectionAddVo.getServices();
            if (service != null && service.size() > 0) {
                sectionSource.createSection().send(MessageBuilder.withPayload(section).build());
            }
            logSender.sendLogEvent(HttpUtils.getClientIp(request), userId, "新增标段信息:[" + section.getId() + "]");
        }
        return respVO;
    }

    private Section getSection(SectionAddVo sectionAddVo) {
        Section section = sectionAddVo.loadSection();
        String projectId = sectionAddVo.getProjectId();
        if (projectId != null) {
            RespVO<ProjectVo> projectVoRespVO = iProjectService.queryById(projectId);
            if (projectVoRespVO.getRetCode() == RespConsts.SUCCESS_RESULT_CODE) {
                ProjectVo projectVo = projectVoRespVO.getInfo();
                if (projectVo != null) {
                    section.setOwnerProject(OwnerProject.builder().id(projectVo.getId())
                            .code(projectVo.getCode())
                            .name(projectVo.getName()).build());
                }
            }
        }
        String groupId = sectionAddVo.getGroupId();
        if (groupId != null) {
            GroupVo groupVo = iGroupService.queryById(groupId);
            if (groupVo != null) {
                section.setOwnerGroup(UpperGroup.builder()
                        .id(groupVo.getId())
                        .name(groupVo.getName())
                        .build());
            }
        }
        return section;
    }


    @ApiOperation(value = "修改标段信息", notes = "修改标段信息", httpMethod = "PUT")
    @ApiImplicitParams({

    })
    @Operation(value = "modify", desc = "修改标段信息")
    @RequestMapping(value = "/modify", method = RequestMethod.PUT)
    public RespVO modify(@Validated @RequestBody SectionAddVo sectionAddVo, HttpServletRequest request) {
        HeaderVo headerVo = HeaderUtils.parseHeader(request);
        String userId = authClient.getAuthVo(headerVo).getUserId();
        //TODO 权限校验
        Section sectionById = iSectionService.findSectionById(sectionAddVo.getId());
        if (sectionById == null) {
            return RespVOBuilder.failure(RespConsts.FAIL_RESULT_CODE, "标段信息不存在");
        }
        Section section = sectionAddVo.modifySection(sectionById);
        RespVO respVO = iSectionService.modify(section);
        logSender.sendLogEvent(HttpUtils.getClientIp(request), userId, "修改标段信息:[" + section.getId() + "]");
       /* List<String> list = ListUtils.getDiffrentList(services, service);
        if(list.size()>0){
          // 是否发送事件
        }*/
        return respVO;
    }


    @ApiOperation(value = "查询标段信息", notes = "查询标段信息", httpMethod = "GET")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "标段id", dataType = "String", required = true, paramType = "query"),
    })
    @Operation(value = "query", desc = "查询标段信息")
    @RequestMapping(value = "/query", method = RequestMethod.GET)
    public RespVO<SectionAddVo> query(@RequestParam String id, HttpServletRequest request) {
        HeaderVo headerVo = HeaderUtils.parseHeader(request);
        String userId = authClient.getAuthVo(headerVo).getUserId();
        return iSectionService.queryById(id);

    }


    @ApiOperation(value = "根据负责人id获取标段信息", notes = "根据负责人id获取标段信息", httpMethod = "GET")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "masterId", value = "标段负责人id", dataType = "String", required = true, paramType = "query"),
    })
    @Operation(value = "queryByMasterId", desc = "根据负责人id获取标段信息")
    @RequestMapping(value = "/queryByMasterId", method = RequestMethod.GET)
    public RespVO<RespDataVO<Section>> queryByMasterId(@RequestParam String masterId, HttpServletRequest request) {
        HeaderVo headerVo = HeaderUtils.parseHeader(request);
        String userId = authClient.getAuthVo(headerVo).getUserId();
        return iSectionService.queryByMaster(masterId);
    }


    @ApiOperation(value = "根据工区id获取标段信息", notes = "根据工区id获取标段信息", httpMethod = "GET")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "workspaceId", value = "工区id", dataType = "String", required = true, paramType = "query"),
    })
    @Operation(value = "queryByWorkspaceId", desc = "根据工区id获取标段信息")
    @RequestMapping(value = "/queryByWorkspaceId", method = RequestMethod.GET)
    public RespVO<Section> queryByWorkspaceId(@RequestParam String workspaceId, HttpServletRequest request) {
        HeaderVo headerVo = HeaderUtils.parseHeader(request);
        String userId = authClient.getAuthVo(headerVo).getUserId();
        return iSectionService.queryByWorkspaceId(workspaceId);
    }


    @ApiOperation(value = "根据工区编码获取标段信息", notes = "根据工区编码获取标段信息", httpMethod = "GET")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "workspaceCode", value = "工区编码", dataType = "String", required = true, paramType = "query"),
    })
    @Operation(value = "queryByWorkspaceCode", desc = "根据工区编码获取标段信息")
    @RequestMapping(value = "/queryByWorkspaceCode", method = RequestMethod.GET)
    public RespVO<Section> queryByWorkspaceCode(@RequestParam String workspaceId, HttpServletRequest request) {
        HeaderVo headerVo = HeaderUtils.parseHeader(request);
        String userId = authClient.getAuthVo(headerVo).getUserId();
        return iSectionService.queryByWorkspaceCode(workspaceId);
    }


    @ApiOperation(value = "删除标段", notes = "删除标段", httpMethod = "DELETE")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "标段id", dataType = "String", required = true, paramType = "query"),
    })
    @Operation(value = "delete", desc = "删除标段")
    @RequestMapping(value = "/delete", method = RequestMethod.DELETE)
    public RespVO delete(@RequestParam String id, HttpServletRequest request) {
        HeaderVo headerVo = HeaderUtils.parseHeader(request);
        String userId = authClient.getAuthVo(headerVo).getUserId();
        // TODO 权限校验

        RespVO respVO = iSectionService.delete(id);
        logSender.sendLogEvent(HttpUtils.getClientIp(request), userId, "删除标段信息:[" + id + "]");
        return respVO;
    }


    @ApiOperation(value = "查询标段列表", notes = "查询标段列表", httpMethod = "GET")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "projectId", value = "工程ID", dataType = "String", required = true, paramType = "query"),
            @ApiImplicitParam(name = "pageIndex", value = "当前页", dataType = "int", required = true, example = "1", paramType = "path"),
            @ApiImplicitParam(name = "pageSize", value = "每页大小", dataType = "int", defaultValue = "10", example = "10", paramType = "query"),
    })
    @Operation(value = "list", desc = "查询标段列表")
    @RequestMapping(value = "/list/{pageIndex}", method = RequestMethod.GET)
    public RespVO<PagedResult<SectionVo>> list(@PathVariable(value = "pageIndex") int pageIndex,
                                               @RequestParam(required = false, defaultValue = "10") int pageSize,
                                               @RequestParam(required = false) String projectId,
                                               HttpServletRequest request) {
        HeaderVo headerVo = HeaderUtils.parseHeader(request);
        String userId = authClient.getAuthVo(headerVo).getUserId();
        PagedResult<SectionVo> pagedResult = iSectionService.list(pageIndex, pageSize, projectId);
        return RespVOBuilder.success(pagedResult);
    }


    @ApiOperation(value = "查询全部标段信息", notes = "查询全部标段信息", httpMethod = "GET")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "projectId", value = "工程ID", dataType = "String", paramType = "query"),
    })
    @Operation(value = "findAll", desc = "查询全部标段信息")
    @RequestMapping(value = "/findAll", method = RequestMethod.GET)
    public RespVO<RespDataVO<Section>> findAll(@RequestParam(required = false) String projectId,
                                               HttpServletRequest request) {
        HeaderVo headerVo = HeaderUtils.parseHeader(request);
        String userId = authClient.getAuthVo(headerVo).getUserId();
        List<Section> sections = iSectionService.findAll(projectId);
        return RespVOBuilder.success(sections);
    }


    @ApiOperation(value = "查询标段列表", notes = "查询标段列表", httpMethod = "GET")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "projectId", value = "工程ID", dataType = "String", required = true, paramType = "query"),
    })
    @Operation(value = "queryByProjectId", desc = "查询标段列表")
    @RequestMapping(value = "/queryByProjectId", method = RequestMethod.GET)
    public RespVO<RespDataVO<SectionVo>> queryByProjectId(
            @RequestParam String projectId,
            HttpServletRequest request) {
        HeaderVo headerVo = HeaderUtils.parseHeader(request);
        String userId = authClient.getAuthVo(headerVo).getUserId();
        return iSectionService.queryByProjectId(projectId);
    }


    @ApiOperation(value = "查询标段管理员", notes = "查询标段管理员", httpMethod = "GET")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "sectionId", value = "标段ID", dataType = "String", required = true, paramType = "path"),
    })
    @Operation(value = "findBySectionMasterBySectionId", desc = "查询标段管理员")
    @RequestMapping(value = "/findBySectionMasterBySectionId/{sectionId}", method = RequestMethod.GET)
    public List<Master> findBySectionMasterBySectionId(@PathVariable String sectionId) {
        return iSectionService.findBySectionMasterBySectionId(sectionId);
    }


    @ApiOperation(value = "查询标段测量员", notes = "查询标段测量员", httpMethod = "GET")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "sectionId", value = "标段ID", dataType = "String", required = true, paramType = "path"),
    })
    @Operation(value = "findSectionSurveyerBySectionId", desc = "查询标段测量员")
    @RequestMapping(value = "/findSectionSurveyerBySectionId/{sectionId}", method = RequestMethod.GET)
    public List<Surveyer> findSectionSurveyerBySectionId(@PathVariable String sectionId) {
        return iSectionService.findSectionSurveyerBySectionId(sectionId);
    }


    @ApiOperation(value = "查询工区测量员", notes = "查询工区测量员", httpMethod = "GET")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "workspaceId", value = "工区ID", dataType = "String", required = true, paramType = "path"),
    })
    @Operation(value = "findSurveyerByWorkspaceId", desc = "查询工区测量员")
    @RequestMapping(value = "/findSurveyerByWorkspaceId/{workspaceId}", method = RequestMethod.GET)
    public List<Surveyer> findSurveyerByWorkspaceId(@PathVariable String workspaceId) {
        return iSectionService.findSurveyerByWorkspaceId(workspaceId);
    }


}
