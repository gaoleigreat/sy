package com.lego.survey.project.feign;

import com.lego.survey.project.model.entity.Workspace;
import com.survey.lib.common.consts.DictConstant;
import com.survey.lib.common.consts.RespConsts;
import com.survey.lib.common.vo.RespDataVO;
import com.survey.lib.common.vo.RespVO;
import com.survey.lib.common.vo.RespVOBuilder;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.*;

/**
 * @author yanglf
 * @description
 * @since 2018/12/27
 **/
@FeignClient(value = DictConstant.Service.PROJECT, path = DictConstant.Path.WORKSPACE, fallback = WorkspaceClientFallback.class)
public interface WorkspaceClient {

    /**
     * 新增工区信息
     *
     * @param workspace
     * @return
     */
    @RequestMapping(value = "/create", method = RequestMethod.POST)
    RespVO create(@RequestBody Workspace workspace);

    /**
     * 修改工区信息
     *
     * @return
     */
    @RequestMapping(value = "/modify", method = RequestMethod.POST)
    RespVO modify(@RequestBody Workspace workspace);

    /**
     * 删除工区信息
     *
     * @param code
     * @return
     */
    @RequestMapping(value = "/delete", method = RequestMethod.DELETE)
    RespVO delete(@RequestParam("code") String code);


    /**
     * 根据标段id获取工区信息
     *
     * @param sectionCode
     * @return
     */
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    RespVO<RespDataVO<Workspace>> list(@RequestParam(value = "sectionCode") String sectionCode);

    /**
     * 获取工区信息
     *
     * @param code
     * @return
     */
    @RequestMapping(value = "/info/{code}", method = RequestMethod.GET)
    RespVO<Workspace> info(@PathVariable(value = "code") String code);

}


@Component
class WorkspaceClientFallback implements WorkspaceClient {

    @Override
    public RespVO create(Workspace workspace) {
        return RespVOBuilder.failure(RespConsts.ERROR_SERVER_CODE, "project服务不可用");
    }

    @Override
    public RespVO modify(Workspace workspace) {
        return RespVOBuilder.failure(RespConsts.ERROR_SERVER_CODE, "project服务不可用");
    }

    @Override
    public RespVO delete(String code) {
        return RespVOBuilder.failure(RespConsts.ERROR_SERVER_CODE, "project服务不可用");
    }

    @Override
    public RespVO<RespDataVO<Workspace>> list(String sectionCode) {
        return RespVOBuilder.failure(RespConsts.ERROR_SERVER_CODE, "project服务不可用");
    }

    @Override
    public RespVO<Workspace> info(String code) {
        return RespVOBuilder.failure(RespConsts.ERROR_SERVER_CODE, "project服务不可用");
    }
}
