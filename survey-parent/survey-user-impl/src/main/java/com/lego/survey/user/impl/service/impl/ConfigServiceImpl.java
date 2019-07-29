package com.lego.survey.user.impl.service.impl;
import com.lego.survey.user.impl.repository.ConfigRepository;
import com.lego.survey.user.impl.service.IConfigService;
import com.lego.survey.user.model.entity.Config;
import com.lego.survey.user.model.vo.ConfigOptionVo;
import com.survey.lib.common.consts.DictConstant;
import com.survey.lib.common.page.PagedResult;
import com.survey.lib.common.vo.CurrentVo;
import com.survey.lib.common.vo.RespVO;
import com.survey.lib.common.vo.RespVOBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author yanglf
 * @description
 * @since 2019/1/3
 **/
@Service
public class ConfigServiceImpl implements IConfigService {

    @Autowired
    private ConfigRepository configRepository;

    @Override
    public RespVO addConfig(Config config) {
        config.setValid(0);
        config.setUpdateTime(new Date());
        configRepository.save(config);
        return RespVOBuilder.success();
    }

    @Override
    public RespVO delConfig(String id) {
        Config config = findByOptionId(id);
        if(config==null){
            return RespVOBuilder.failure();
        }
        List<ConfigOptionVo> options = config.getOption();
        if(CollectionUtils.isEmpty(options)){
            return RespVOBuilder.failure();
        }
        List<ConfigOptionVo> newOptions=new ArrayList<>();
        for (ConfigOptionVo option : options) {
            String optionId = option.getId();
            if(!id.equals(optionId)){
                newOptions.add(option);
            }
        }
        config.setOption(newOptions);
        configRepository.save(config);
        return RespVOBuilder.success();

    }

    @Override
    public Config queryById(String id) {
        return configRepository.findConfigByIdAndValid(id,0);
    }

    @Override
    public PagedResult<Config> queryList(int pageIndex, int pageSize) {
        PagedResult<Config> pagedResult=new PagedResult<>();
        Pageable pageable= PageRequest.of(pageIndex-1,pageSize, Sort.Direction.DESC,"updateTime");
        Page<Config> configPage = configRepository.findAll(pageable);
        pagedResult.setPage(new com.survey.lib.common.page.Page(pageIndex,pageSize,0,configPage.getTotalElements(),configPage.getTotalPages()));
        pagedResult.setResultList(configPage.getContent());
        return pagedResult;
    }

    @Override
    public Config queryByName(String name, CurrentVo authVo) {
        Config config = configRepository.findConfigByNameAndValidOrderByUpdateTimeDesc(name, 0);
        if(!name.equals("角色配置")){
            return config;
        }
        String role = authVo.getRole();
        List<ConfigOptionVo> optionVos;
        if(role.equalsIgnoreCase(DictConstant.Role.ADMIN)){
            optionVos = config.getOption().stream().filter(opt -> opt.getName().equalsIgnoreCase(DictConstant.Role.MASTER)
                    || opt.getName().equalsIgnoreCase(DictConstant.Role.SECTION)).collect(Collectors.toList());
        }else if(role.equalsIgnoreCase(DictConstant.Role.SECTION)){
             optionVos = config.getOption().stream().filter(opt -> opt.getName().equalsIgnoreCase(DictConstant.Role.SURVEYER)
                  ).collect(Collectors.toList());
        }else {
            return null;
        }
        config.setOption(optionVos);
        return config;
    }

    @Override
    public Config queryByName(String name) {
        return configRepository.findConfigByNameAndValidOrderByUpdateTimeDesc(name, 0);
    }

    @Override
    public RespVO modify(Config config) {
        config.setValid(0);
        config.setUpdateTime(new Date());
         configRepository.save(config);
         return RespVOBuilder.success();
    }

    @Override
    public Config findByOptionId(String id) {
        return configRepository.findConfigByOptionId(id);
    }
}
