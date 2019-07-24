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
        SectionAddVo queryByCode = iSectionService.queryByCode(sectionCode);
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
        String projectCode = sectionAddVo.getProjectCode();
        if (projectCode != null) {
            ProjectVo projectVo = iProjectService.queryByCode(projectCode);
            if (projectVo != null) {
                section.setOwnerProject(OwnerProject.builder().id(projectVo.getId())
                        .code(projectVo.getCode())
                        .name(projectVo.getName()).build());
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
            @ApiImplicitParam(name = "code", value = "标段code", dataType = "String", required = true, paramType = "query"),
    })
    @Operation(value = "query", desc = "查询标段信息")
    @RequestMapping(value = "/query", method = RequestMethod.GET)
    public RespVO<SectionAddVo> query(@RequestParam String code, HttpServletRequest request) {
        // TODO ID -> CODE
        HeaderVo headerVo = HeaderUtils.parseHeader(request);
        String userId = authClient.getAuthVo(headerVo).getUserId();
        SectionAddVo sectionAddVo = iSectionService.queryByCode(code);
        return RespVOBuilder.success(sectionAddVo);

    }

    @ApiOperation(value = "查询标段信息", notes = "查询标段信息", httpMethod = "GET")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "code", value = "标段code", dataType = "String", required = true, paramType = "path"),
    })
    @Operation(value = "query", desc = "查询标段信息")
    @RequestMapping(value = "/queryByCode/{code}", method = RequestMethod.GET)
    public SectionAddVo queryByCode(@PathVariable(value = "code") String code) {
        return iSectionService.queryByCode(code);
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


    /*@ApiOperation(value = "根据工区CODE获取标段信息", notes = "根据工区CODE获取标段信息", httpMethod = "GET")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "workspaceCode", value = "工区code", dataType = "String", required = true, paramType = "query"),
    })
    @Operation(value = "queryByWorkspaceCode", desc = "根据工区CODE获取标段信息")
    @RequestMapping(value = "/queryByWorkspaceCode", method = RequestMethod.GET)
    public RespVO<Section> queryByWorkspaceId(@RequestParam String workspaceCode, HttpServletRequest request) {
        // ID -> CODE
        HeaderVo headerVo = HeaderUtils.parseHeader(request);
        String userId = authClient.getAuthVo(headerVo).getUserId();
        return iSectionService.queryByWorkspaceCode(workspaceCode);
    }*/


    @ApiOperation(value = "根据工区编码获取标段信息", notes = "根据工区编码获取标段信息", httpMethod = "GET")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "workspaceCode", value = "工区编码", dataType = "String", required = true, paramType = "query"),
    })
    @Operation(value = "queryByWorkspaceCode", desc = "根据工区编码获取标段信息")
    @RequestMapping(value = "/queryByWorkspaceCode", method = RequestMethod.GET)
    public RespVO<Section> queryByWorkspaceCode(@RequestParam String workspaceCode, HttpServletRequest request) {
        // TODO ID -> CODE
        HeaderVo headerVo = HeaderUtils.parseHeader(request);
        String userId = authClient.getAuthVo(headerVo).getUserId();
        return iSectionService.queryByWorkspaceCode(workspaceCode);
    }


    @ApiOperation(value = "删除标段", notes = "删除标段", httpMethod = "DELETE")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "code", value = "标段code", dataType = "String", required = true, paramType = "query"),
    })
    @Operation(value = "delete", desc = "删除标段")
    @RequestMapping(value = "/delete", method = RequestMethod.DELETE)
    public RespVO delete(@RequestParam String code, HttpServletRequest request) {
        // TODO ID -> CODE
        HeaderVo headerVo = HeaderUtils.parseHeader(request);
        String userId = authClient.getAuthVo(headerVo).getUserId();
        RespVO respVO = iSectionService.delete(code);
        logSender.sendLogEvent(HttpUtils.getClientIp(request), userId, "删除标段信息:[" + code + "]");
        return respVO;
    }


    @ApiOperation(value = "查询标段列表", notes = "查询标段列表", httpMethod = "GET")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "projectCode", value = "工程code", dataType = "String", required = true, paramType = "query"),
            @ApiImplicitParam(name = "pageIndex", value = "当前页", dataType = "int", required = true, example = "1", paramType = "path"),
            @ApiImplicitParam(name = "pageSize", value = "每页大小", dataType = "int", defaultValue = "10", example = "10", paramType = "query"),
    })
    @Operation(value = "list", desc = "查询标段列表")
    @RequestMapping(value = "/list/{pageIndex}", method = RequestMethod.GET)
    public RespVO<PagedResult<SectionVo>> list(@PathVariable(value = "pageIndex") int pageIndex,
                                               @RequestParam(required = false, defaultValue = "10") int pageSize,
                                               @RequestParam(required = false) String projectCode,
                                               HttpServletRequest request) {
        // TODO ID -> CODE
        HeaderVo headerVo = HeaderUtils.parseHeader(request);
        String userId = authClient.getAuthVo(headerVo).getUserId();
        PagedResult<SectionVo> pagedResult = iSectionService.list(pageIndex, pageSize, projectCode);
        return RespVOBuilder.success(pagedResult);
    }


    @ApiOperation(value = "查询全部标段信息", notes = "查询全部标段信息", httpMethod = "GET")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "projectCodes", value = "工程CODE", dataType = "String", paramType = "query"),
    })
    @Operation(value = "findAll", desc = "查询全部标段信息")
    @RequestMapping(value = "/findAll", method = RequestMethod.GET)
    public RespVO<RespDataVO<Section>> findAll(@RequestParam(required = false) List<String> projectCodes,
                                               HttpServletRequest request) {
        // TODO ID -> CODE
        HeaderVo headerVo = HeaderUtils.parseHeader(request);
        String userId = authClient.getAuthVo(headerVo).getUserId();
        List<Section> sections = iSectionService.findAll(projectCodes);
        return RespVOBuilder.success(sections);
    }


    @ApiOperation(value = "查询标段列表", notes = "查询标段列表", httpMethod = "GET")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "projectCode", value = "工程code", dataType = "String", required = true, paramType = "query"),
    })
    @Operation(value = "queryByProjectCode", desc = "查询标段列表")
    @RequestMapping(value = "/queryByProjectCode", method = RequestMethod.GET)
    public RespVO<RespDataVO<SectionVo>> queryByProjectId(
            @RequestParam String projectCode,
            HttpServletRequest request) {
        // TODO ID -> CODE
        HeaderVo headerVo = HeaderUtils.parseHeader(request);
        String userId = authClient.getAuthVo(headerVo).getUserId();
        return iSectionService.queryByProjectCode(projectCode);
    }


    @ApiOperation(value = "查询标段管理员", notes = "查询标段管理员", httpMethod = "GET")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "sectionCode", value = "标段code", dataType = "String", required = true, paramType = "path"),
    })
    @Operation(value = "findBySectionMasterBySectionCode", desc = "查询标段管理员")
    @RequestMapping(value = "/findBySectionMasterBySectionCode/{sectionCode}", method = RequestMethod.GET)
    public List<Master> findBySectionMasterBySectionCode(@PathVariable String sectionCode) {
        // TODO ID -> CODE
        return iSectionService.findBySectionMasterBySectionCode(sectionCode);
    }


    @ApiOperation(value = "查询标段测量员", notes = "查询标段测量员", httpMethod = "GET")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "sectionCode", value = "标段code", dataType = "String", required = true, paramType = "path"),
    })
    @Operation(value = "findSurveyerBySectionCode", desc = "查询标段测量员")
    @RequestMapping(value = "/findSurveyerBySectionCode/{sectionCode}", method = RequestMethod.GET)
    public List<Surveyer> findSurveyerBySectionCode(@PathVariable String sectionCode) {
        // TODO ID -> CODE
        return iSectionService.findSurveyerBySectionCode(sectionCode);
    }


    @ApiOperation(value = "查询工区测量员", notes = "查询工区测量员", httpMethod = "GET")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "workspaceCode", value = "工区code", dataType = "String", required = true, paramType = "path"),
    })
    @Operation(value = "findSurveyerByWorkspaceCode", desc = "查询工区测量员")
    @RequestMapping(value = "/findSurveyerByWorkspaceCode/{workspaceCode}", method = RequestMethod.GET)
    public List<Surveyer> findSurveyerByWorkspaceCode(@PathVariable String workspaceCode) {
        // TODO ID -> CODE
        return iSectionService.findSurveyerByWorkspaceCode(workspaceCode);
    }


}
