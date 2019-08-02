package com.lego.survey.user.feign;

import com.lego.survey.user.model.entity.Config;
import com.survey.lib.common.consts.DictConstant;
import com.survey.lib.common.consts.RespConsts;
import com.survey.lib.common.vo.RespDataVO;
import com.survey.lib.common.vo.RespVO;
import com.survey.lib.common.vo.RespVOBuilder;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * @author yanglf
 * @description
 * @since 2019/1/3
 **/
@FeignClient(value = DictConstant.Service.USER, path = DictConstant.Path.CONFIG, fallback = ConfigClientFallback.class)
public interface ConfigClient {

    /**
     * 查询配置列表
     *
     * @param pageIndex
     * @param pageSize
     * @return
     */
    @RequestMapping(value = "/list/{pageIndex}", method = RequestMethod.GET)
    RespVO<RespDataVO<Config>> list(@PathVariable(value = "pageIndex") int pageIndex,
                                    @RequestParam("pageSize") int pageSize);


    /**
     * 新增配置
     *
     * @param config
     * @return
     */
    @RequestMapping(value = "/create", method = RequestMethod.POST)
    RespVO create(@RequestBody Config config);


    /**
     * 修改配置
     *
     * @param config
     * @return
     */
    @RequestMapping(value = "/modify", method = RequestMethod.PUT)
    RespVO modify(@Validated @RequestBody Config config);


    /**
     * 删除配置信息
     *
     * @param id
     * @return
     */
    @RequestMapping(value = "/delete", method = RequestMethod.DELETE)
    RespVO delete(@RequestParam("id") String id);


    /**
     * 删除配置信息
     *
     * @param id
     * @return
     */
    @RequestMapping(value = "/info", method = RequestMethod.GET)
    RespVO<Config> info(@RequestParam("id") String id);


    /**
     * 根据名称获取配置信息
     *
     * @param name
     * @return
     */
    @RequestMapping(value = "/queryByName", method = RequestMethod.GET)
    RespVO<Config> queryByName(@RequestParam("name") String name);


}


@Component
class ConfigClientFallback implements ConfigClient {

    @Override
    public RespVO<RespDataVO<Config>> list(int pageIndex, int pageSize) {
        return RespVOBuilder.failure(RespConsts.ERROR_SERVER_CODE, "user服务不可用");
    }

    @Override
    public RespVO create(Config config) {
        return RespVOBuilder.failure(RespConsts.ERROR_SERVER_CODE, "user服务不可用");
    }

    @Override
    public RespVO modify(Config config) {
        return RespVOBuilder.failure(RespConsts.ERROR_SERVER_CODE, "user服务不可用");
    }

    @Override
    public RespVO delete(String id) {
        return RespVOBuilder.failure(RespConsts.ERROR_SERVER_CODE, "user服务不可用");
    }

    @Override
    public RespVO<Config> info(String id) {
        return RespVOBuilder.failure(RespConsts.ERROR_SERVER_CODE, "user服务不可用");
    }

    @Override
    public RespVO<Config> queryByName(String name) {
        return RespVOBuilder.failure(RespConsts.ERROR_SERVER_CODE, "user服务不可用");
    }
}
