package com.lego.survey.project.feign;

import com.lego.survey.project.model.entity.Group;
import com.lego.survey.project.model.vo.GroupVo;
import com.survey.lib.common.consts.DictConstant;
import com.survey.lib.common.consts.RespConsts;
import com.survey.lib.common.vo.RespDataVO;
import com.survey.lib.common.vo.RespVO;
import com.survey.lib.common.vo.RespVOBuilder;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.*;

/**
 * The interface Group client.
 *
 * @author yanglf
 * @description
 * @since 2018 /12/21
 */
@FeignClient(value = DictConstant.Service.PROJECT, path = DictConstant.Path.GROUP, fallback = GroupClientFallback.class)
public interface GroupClient {

    /**
     * 新增单位信息
     *
     * @param group the group
     * @return resp vo
     */
    @RequestMapping(value = "/create", method = RequestMethod.POST)
    RespVO create(@RequestBody Group group);


    /**
     * 修改单位信息
     *
     * @param group the group
     * @return resp vo
     */
    @RequestMapping(value = "/modify", method = RequestMethod.PUT)
    RespVO modify(@RequestBody Group group);


    /**
     * 查询单位信息
     *
     * @param id the id
     * @return resp vo
     */
    @RequestMapping(value = "/query", method = RequestMethod.GET)
    RespVO<GroupVo> query(@RequestParam("id") String id);


    /**
     * 删除单位信息
     *
     * @param id the id
     * @return the resp vo
     */
    @RequestMapping(value = "/delete", method = RequestMethod.DELETE)
    RespVO delete(@RequestParam("id") String id);

    /**
     * 获取所有公司.
     *
     * @return the resp vo
     */
    @RequestMapping(value = "/list/{pageIndex}", method = RequestMethod.GET)
    RespVO<RespDataVO<GroupVo>> list(@PathVariable(value = "pageIndex") int pageIndex, @RequestParam("pageSize") int pageSize);

    /**
     * 查询完整的集团公司信息.
     *
     * @param id the id
     * @return the resp vo
     */
    @RequestMapping(value = "/queryFull", method = RequestMethod.GET)
    RespVO<RespDataVO<GroupVo>> queryFullGroup(@RequestParam("id") String id);

}


@Component
class GroupClientFallback implements GroupClient {

    @Override
    public RespVO create(Group group) {
        return RespVOBuilder.failure(RespConsts.ERROR_SERVER_CODE, "project服务不可用");
    }

    @Override
    public RespVO modify(Group group) {
        return RespVOBuilder.failure(RespConsts.ERROR_SERVER_CODE, "project服务不可用");
    }

    @Override
    public RespVO<GroupVo> query(String id) {
        return RespVOBuilder.failure(RespConsts.ERROR_SERVER_CODE, "project服务不可用");
    }

    @Override
    public RespVO delete(String id) {
        return RespVOBuilder.failure(RespConsts.ERROR_SERVER_CODE, "project服务不可用");
    }

    @Override
    public RespVO<RespDataVO<GroupVo>> list(int pageIndex, int pageSize) {
        return RespVOBuilder.failure(RespConsts.ERROR_SERVER_CODE, "project服务不可用");
    }

    @Override
    public RespVO<RespDataVO<GroupVo>> queryFullGroup(String id) {
        return RespVOBuilder.failure(RespConsts.ERROR_SERVER_CODE, "project服务不可用");
    }
}
