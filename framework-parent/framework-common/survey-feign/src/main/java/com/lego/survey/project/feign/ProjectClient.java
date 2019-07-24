package com.lego.survey.project.feign;

import com.lego.survey.project.model.entity.Project;
import com.lego.survey.project.model.vo.ProjectVo;
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
 * @since 2018/12/21
 **/
@FeignClient(value = DictConstant.Service.PROJECT, path = DictConstant.Path.PROJECT,fallback = ProjectClientFallback.class)
public interface ProjectClient {

    /**
     * 新增工程项目
     *
     * @param project
     * @return
     */
    @RequestMapping(value = "/create", method = RequestMethod.POST)
    RespVO create(@RequestBody Project project);


    /**
     * 修改工程项目
     *
     * @param project
     * @return
     */
    @RequestMapping(value = "/modify", method = RequestMethod.POST)
    RespVO modify(@RequestBody Project project);


    /**
     * 查询工程项目
     *
     * @param code
     * @return
     */
    @RequestMapping(value = "/info", method = RequestMethod.GET)
    RespVO<ProjectVo> query(@RequestParam("code") String code);

    /**
     * 查询工程列表
     * @param pageIndex
     * @param pageSize
     * @return
     */
    @RequestMapping(value = "/list/{pageIndex}", method = RequestMethod.GET)
    RespVO<RespDataVO<ProjectVo>> list(@PathVariable(value = "pageIndex") int pageIndex,
                                       @RequestParam("pageSize") int pageSize);


    /**
     * 删除工程
     * @param code
     * @return
     */
    @RequestMapping(value = "/delete", method = RequestMethod.DELETE)
    RespVO delete(@RequestParam("code") String code);

}

@Component
class  ProjectClientFallback implements ProjectClient{

    @Override
    public RespVO create(Project project) {
        return RespVOBuilder.failure(RespConsts.ERROR_SERVER_CODE,"project服务不可用");
    }

    @Override
    public RespVO modify(Project project) {
        return RespVOBuilder.failure(RespConsts.ERROR_SERVER_CODE,"project服务不可用");
    }

    @Override
    public RespVO<ProjectVo> query(String code) {
        return RespVOBuilder.failure(RespConsts.ERROR_SERVER_CODE,"project服务不可用");
    }

    @Override
    public RespVO<RespDataVO<ProjectVo>> list(int pageIndex, int pageSize) {
        return RespVOBuilder.failure(RespConsts.ERROR_SERVER_CODE,"project服务不可用");
    }

    @Override
    public RespVO delete(String code) {
        return RespVOBuilder.failure(RespConsts.ERROR_SERVER_CODE,"project服务不可用");
    }
}
