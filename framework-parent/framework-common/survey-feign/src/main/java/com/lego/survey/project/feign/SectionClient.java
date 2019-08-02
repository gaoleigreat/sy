package com.lego.survey.project.feign;

import com.lego.survey.project.model.entity.Master;
import com.lego.survey.project.model.entity.Section;
import com.lego.survey.project.model.entity.Surveyer;
import com.lego.survey.project.model.vo.SectionAddVo;
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
@FeignClient(value = DictConstant.Service.PROJECT, path = DictConstant.Path.SECTION, fallback = SectionClientFallback.class)
public interface SectionClient {

    /**
     * 新增标段信息
     *
     * @param section
     * @return
     */
    @RequestMapping(value = "/create", method = RequestMethod.POST)
    RespVO create(@RequestBody Section section);


    /**
     * 修改标段信息
     *
     * @param section
     * @return
     */
    @RequestMapping(value = "/modify", method = RequestMethod.POST)
    RespVO modify(@RequestBody Section section);


    /**
     * 根据id查询标段信息
     *
     * @param code
     * @return
     */
    @RequestMapping(value = "/query", method = RequestMethod.GET)
    RespVO<SectionVo> query(@RequestParam("code") String code);


    /**
     * 删除标段信息
     *
     * @param code
     * @return
     */
    @RequestMapping(value = "/delete", method = RequestMethod.DELETE)
    RespVO delete(@RequestParam("code") String code);


    /**
     * 查询标段列表
     *
     * @param pageIndex
     * @param pageSize
     * @return
     */
    @RequestMapping(value = "/list/{pageIndex}", method = RequestMethod.GET)
    RespVO<RespDataVO<SectionVo>> list(@PathVariable(value = "pageIndex") int pageIndex,
                                       @RequestParam("pageSize") int pageSize);


    /**
     * 根据负责人id获取标段信息
     *
     * @param masterId
     * @return
     */
    @RequestMapping(value = "/queryByMasterId", method = RequestMethod.GET)
    RespVO<RespDataVO<Section>> queryByMasterId(@RequestParam("masterId") String masterId);


    /**
     * 根据工区id获取标段信息
     *
     * @param workspaceId
     * @return
     */
    @RequestMapping(value = "/queryByWorkspaceId", method = RequestMethod.GET)
    RespVO<Section> queryByWorkspaceId(@RequestParam("workspaceId") String workspaceId);


    /**
     * 根据工区编码获取标段信息
     *
     * @param workspaceCode
     * @return
     */
    @RequestMapping(value = "/queryByWorkspaceCode", method = RequestMethod.GET)
    RespVO<Section> queryByWorkspaceCode(@RequestParam("workspaceCode") String workspaceCode);


    /**
     * 根据工区ID获取工区测量员
     *
     * @param workspaceCode
     * @return
     */
    @RequestMapping(value = "/findSurveyerByWorkspaceCode/{workspaceCode}", method = RequestMethod.GET)
    List<Surveyer> findSurveyerByWorkspaceCode(@PathVariable(value = "workspaceCode") String workspaceCode);


    /**
     * 查询标段测量员
     *
     * @param sectionCode
     * @return
     */
    @RequestMapping(value = "/findSurveyerBySectionCode/{sectionCode}", method = RequestMethod.GET)
    List<Surveyer> findSurveyerBySectionCode(@PathVariable(value = "sectionCode") String sectionCode);


    /**
     * 查询标段管理员
     *
     * @param sectionCode
     * @return
     */
    @RequestMapping(value = "/findBySectionMasterBySectionCode/{sectionCode}", method = RequestMethod.GET)
    List<Master> findBySectionMasterBySectionCode(@PathVariable(value = "sectionCode") String sectionCode);

    /**
     * @param code
     * @return
     */
    @RequestMapping(value = "/queryByCode/{code}", method = RequestMethod.GET)
    SectionAddVo queryByCode(@PathVariable(value = "code") String code);
}


@Component
class SectionClientFallback implements SectionClient {

    @Override
    public RespVO create(Section section) {
        return RespVOBuilder.failure(RespConsts.ERROR_SERVER_CODE, "project服务不可用");
    }

    @Override
    public RespVO modify(Section section) {
        return RespVOBuilder.failure(RespConsts.ERROR_SERVER_CODE, "project服务不可用");
    }

    @Override
    public RespVO<SectionVo> query(String code) {
        return RespVOBuilder.failure(RespConsts.ERROR_SERVER_CODE, "project服务不可用");
    }

    @Override
    public RespVO delete(String code) {
        return RespVOBuilder.failure(RespConsts.ERROR_SERVER_CODE, "project服务不可用");
    }

    @Override
    public RespVO<RespDataVO<SectionVo>> list(int pageIndex, int pageSize) {
        return RespVOBuilder.failure(RespConsts.ERROR_SERVER_CODE, "project服务不可用");
    }

    @Override
    public RespVO<RespDataVO<Section>> queryByMasterId(String masterId) {
        return RespVOBuilder.failure(RespConsts.ERROR_SERVER_CODE, "project服务不可用");
    }

    @Override
    public RespVO<Section> queryByWorkspaceId(String workspaceCode) {
        return RespVOBuilder.failure(RespConsts.ERROR_SERVER_CODE, "project服务不可用");
    }

    @Override
    public RespVO<Section> queryByWorkspaceCode(String workspaceCode) {
        return RespVOBuilder.failure(RespConsts.ERROR_SERVER_CODE, "project服务不可用");
    }

    @Override
    public List<Surveyer> findSurveyerByWorkspaceCode(String workspaceCode) {
        return null;
    }

    @Override
    public List<Surveyer> findSurveyerBySectionCode(String sectionCode) {
        return null;
    }

    @Override
    public List<Master> findBySectionMasterBySectionCode(String sectionCode) {
        return null;
    }

    @Override
    public SectionAddVo queryByCode(String code) {
        return null;
    }
}
