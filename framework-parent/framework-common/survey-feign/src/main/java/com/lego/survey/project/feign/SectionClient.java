package com.lego.survey.project.feign;
import com.lego.survey.project.model.entity.Master;
import com.lego.survey.project.model.entity.Section;
import com.lego.survey.project.model.entity.Surveyer;
import com.lego.survey.project.model.vo.SectionVo;
import com.survey.lib.common.consts.DictConstant;
import com.survey.lib.common.consts.RespConsts;
import com.survey.lib.common.vo.RespDataVO;
import com.survey.lib.common.vo.RespVO;
import com.survey.lib.common.vo.RespVOBuilder;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author yanglf
 * @description
 * @since 2018/12/27
 **/
@FeignClient(value = DictConstant.Service.PROJECT,path = DictConstant.Path.SECTION,fallback = SectionClientFallback.class)
public interface SectionClient {

    /**
     * 新增标段信息
     * @param section
     * @return
     */
    @RequestMapping(value = "/create", method = RequestMethod.POST)
    RespVO create(@RequestBody Section section);


    /**
     * 修改标段信息
     * @param section
     * @return
     */
    @RequestMapping(value = "/modify", method = RequestMethod.POST)
    RespVO modify(@RequestBody Section section);


    /**
     * 根据id查询标段信息
     * @param id
     * @return
     */
    @RequestMapping(value = "/query", method = RequestMethod.GET)
    RespVO<SectionVo> query(@RequestParam("id") String id);


    /**
     * 删除标段信息
     * @param id
     * @return
     */
    @RequestMapping(value = "/delete", method = RequestMethod.DELETE)
    RespVO delete(@RequestParam("id") String id);


    /**
     * 查询标段列表
     * @param pageIndex
     * @param pageSize
     * @return
     */
    @RequestMapping(value = "/list/{pageIndex}", method = RequestMethod.GET)
    RespVO<RespDataVO<SectionVo>> list(@PathVariable(value = "pageIndex") int pageIndex,
                                       @RequestParam("pageSize") int pageSize);


    /**
     * 根据负责人id获取标段信息
     * @param masterId
     * @return
     */
    @RequestMapping(value = "/queryByMasterId", method = RequestMethod.GET)
    RespVO<RespDataVO<Section>> queryByMasterId(@RequestParam("masterId") String masterId);


    /**
     * 根据工区id获取标段信息
     * @param workspaceId
     * @return
     */
    @RequestMapping(value = "/queryByWorkspaceId", method = RequestMethod.GET)
    RespVO<Section> queryByWorkspaceId(@RequestParam("workspaceId") String workspaceId);


    /**
     * 根据工区编码获取标段信息
     * @param workspaceId
     * @return
     */
    @RequestMapping(value = "/queryByWorkspaceCode", method = RequestMethod.GET)
    RespVO<Section> queryByWorkspaceCode(@RequestParam("workspaceId") String workspaceId);


    /**
     * 根据工区ID获取工区测量员
     * @param workspaceId
     * @return
     */
    @RequestMapping(value = "/findSurveyerByWorkspaceId/{workspaceId}", method = RequestMethod.GET)
    List<Surveyer> findSurveyerByWorkspaceId(@PathVariable(value = "workspaceId") String workspaceId);


    /**
     * 查询标段测量员
     * @param sectionId
     * @return
     */
    @RequestMapping(value = "/findSectionSurveyerBySectionId/{sectionId}", method = RequestMethod.GET)
    List<Surveyer> findSectionSurveyerBySectionId(@PathVariable(value = "sectionId") String sectionId);


    /**
     * 查询标段管理员
     * @param sectionId
     * @return
     */
    @RequestMapping(value = "/findBySectionMasterBySectionId/{sectionId}", method = RequestMethod.GET)
    List<Master> findBySectionMasterBySectionId(@PathVariable(value = "sectionId") String sectionId);

}


@Component
class SectionClientFallback implements SectionClient{

    @Override
    public RespVO create(Section section) {
        return RespVOBuilder.failure(RespConsts.ERROR_SERVER_CODE,"project服务不可用");
    }

    @Override
    public RespVO modify(Section section) {
        return RespVOBuilder.failure(RespConsts.ERROR_SERVER_CODE,"project服务不可用");
    }

    @Override
    public RespVO<SectionVo> query(String id) {
        return RespVOBuilder.failure(RespConsts.ERROR_SERVER_CODE,"project服务不可用");
    }

    @Override
    public RespVO delete(String id) {
        return RespVOBuilder.failure(RespConsts.ERROR_SERVER_CODE,"project服务不可用");
    }

    @Override
    public RespVO<RespDataVO<SectionVo>> list(int pageIndex, int pageSize) {
        return RespVOBuilder.failure(RespConsts.ERROR_SERVER_CODE,"project服务不可用");
    }

    @Override
    public RespVO<RespDataVO<Section>> queryByMasterId(String masterId) {
        return RespVOBuilder.failure(RespConsts.ERROR_SERVER_CODE,"project服务不可用");
    }

    @Override
    public RespVO<Section> queryByWorkspaceId(String workspaceId) {
        return RespVOBuilder.failure(RespConsts.ERROR_SERVER_CODE,"project服务不可用");
    }

    @Override
    public RespVO<Section> queryByWorkspaceCode(String workspaceId) {
        return RespVOBuilder.failure(RespConsts.ERROR_SERVER_CODE,"project服务不可用");
    }

    @Override
    public List<Surveyer> findSurveyerByWorkspaceId(String workspaceId) {
        return null;
    }

    @Override
    public List<Surveyer> findSectionSurveyerBySectionId(String sectionId) {
        return null;
    }

    @Override
    public List<Master> findBySectionMasterBySectionId(String sectionId) {
        return null;
    }
}
