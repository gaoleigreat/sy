package com.lego.survey.settlement.impl.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.lego.survey.base.exception.ExceptionBuilder;
import com.lego.survey.settlement.impl.mapper.SurveyPointTypeMapper;
import com.lego.survey.settlement.impl.service.ISurveyPointTypeService;
import com.lego.survey.settlement.model.entity.SurveyPointType;
import com.lego.survey.settlement.model.vo.SurveyPointTypeVo;
import com.survey.lib.common.vo.RespDataVO;
import com.survey.lib.common.vo.RespVO;
import com.survey.lib.common.vo.RespVOBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author Wesley.Xia
 * @description
 * @since 2019/1/8 15:44
 **/
@Service
public class SurveyPointTypeServiceImpl implements ISurveyPointTypeService {

    @Autowired
    SurveyPointTypeMapper surveyPointTypeMapper;

    @Override
    public RespVO<RespDataVO<SurveyPointTypeVo>> list(String sectionCode, Integer status) {
        List<SurveyPointTypeVo> typeVos = new ArrayList<>();
        QueryWrapper<SurveyPointType> wrapper = new QueryWrapper<>();
        if (sectionCode != null) {
            wrapper.eq("section_code", sectionCode);
        }
        if (status != null) {
            wrapper.eq("status", status);
        }
        wrapper.orderByDesc("create_time");
        List<SurveyPointType> typeList = surveyPointTypeMapper.selectList(wrapper);
        if (typeList != null) {
            for (SurveyPointType type : typeList) {
                typeVos.add(SurveyPointTypeVo.builder().build().loadData(type));
            }
        }
        return RespVOBuilder.success(typeVos);
    }

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public RespVO create(SurveyPointTypeVo surveyPointType) {
        try {
            surveyPointType.setCreateTime(new Date());
            surveyPointType.setUpdateTime(new Date());
            if (surveyPointTypeMapper.insert(surveyPointType.getSurveyPointType()) > 0) {
                return RespVOBuilder.success();
            } else {
                return RespVOBuilder.failure("添加测点类型失败");
            }
        } catch (DuplicateKeyException ex) {
            ex.printStackTrace();
            ExceptionBuilder.duplicateKeyException("主键冲突", surveyPointType.getId());
        }
        return RespVOBuilder.failure("添加测点类型失败");

    }

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public RespVO delete(Long id) {
        if (surveyPointTypeMapper.deleteById(id) > 0) {
            return RespVOBuilder.success();
        } else {
            return RespVOBuilder.failure("delete survey point type failed");
        }
    }

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public RespVO modify(SurveyPointTypeVo surveyPointType) {
        surveyPointType.setUpdateTime(new Date());
        surveyPointTypeMapper.updateStatus(surveyPointType.getId());
        return create(surveyPointType);
    }

    @Override
    public RespVO<SurveyPointTypeVo> info(Long id) {
        SurveyPointType info = surveyPointTypeMapper.selectById(id);
        if (info != null) {
            return RespVOBuilder.success(SurveyPointTypeVo.builder().build().loadData(info));
        } else {
            return RespVOBuilder.failure("info survey point type failed");
        }
    }

    @Override
    public RespDataVO<SurveyPointTypeVo> querySurveyPointTypeBySectionCode(String sectioncode) {
        List<SurveyPointType> types = surveyPointTypeMapper.querySurveyPointTypeBySectionCode(sectioncode);

        if (types != null && types.size() > 0) {
            List<SurveyPointTypeVo> rets = new ArrayList<>();
            types.forEach(item -> rets.add(SurveyPointTypeVo.builder().build().loadData(item)));
            return new RespDataVO<>(rets);
        } else {
            return new RespDataVO<>();
        }
    }

    @Override
    public SurveyPointType queryTypeByNameOrCode(String name, String code, String sectionCode) {
        QueryWrapper<SurveyPointType> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("section_code", sectionCode).eq("status", 0);
        queryWrapper.eq("name", name).or().eq("code", code);
        List<SurveyPointType> typeList = surveyPointTypeMapper.selectList(queryWrapper);
        if (typeList != null && typeList.size() > 0) {
            return typeList.get(0);
        }
        return null;
    }

    @Override
    public SurveyPointType queryByName(String name, String sectionCode) {
        QueryWrapper<SurveyPointType> wrapper = new QueryWrapper<>();
        wrapper.eq("name", name).eq("status", 0);
        List<SurveyPointType> surveyPointTypes = surveyPointTypeMapper.selectList(wrapper);
        if (surveyPointTypes == null || surveyPointTypes.size() <= 0) {
            return null;
        }
        return surveyPointTypes.get(0);
    }
}
