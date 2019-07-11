package com.lego.survey.auth.impl.mapper;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.lego.survey.auth.model.entity.RoleResources;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author yanglf
 * @description
 * @since 2019/7/10
 **/
@Repository
public interface RoleResourcesMapper extends BaseMapper<RoleResources> {


    List<RoleResources> findByRole(@Param(value = "role") String role);
}
