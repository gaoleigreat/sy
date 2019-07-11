package com.lego.survey.settlement.impl.service.impl;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.lego.survey.base.exception.ExceptionBuilder;
import com.lego.survey.settlement.impl.mapper.SurveyPointExceptionMapper;
import com.lego.survey.settlement.impl.service.ISurveyPointExceptionService;
import com.lego.survey.settlement.model.entity.SurveyPointException;
import com.lego.survey.settlement.model.vo.SurveyPointExceptionVo;
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
 * @author yanglf
 * @description
 * @since 2019/1/9
 **/
@Service
public class SurveyPointExceptionServiceImpl implements ISurveyPointExceptionService {

    @Autowired
    private SurveyPointExceptionMapper surveyPointExceptionMapper;

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public RespVO create(SurveyPointException surveyPointException) {
        try {
            surveyPointException.setCreateTime(new Date());
            int insert = surveyPointExceptionMapper.insert(surveyPointException);
            if(insert<=0){
                return RespVOBuilder.failure("添加失败");
            }
            return RespVOBuilder.success();
        }catch (DuplicateKeyException ex){
            ex.printStackTrace();
            ExceptionBuilder.duplicateKeyException("主键冲突",surveyPointException.getId());
        }
        return RespVOBuilder.failure("添加失败");
    }

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public RespVO modify(SurveyPointException surveyPointException) {
        surveyPointException.setCreateTime(new Date());
        int update = surveyPointExceptionMapper.updateById(surveyPointException);
        if(update<=0){
            return RespVOBuilder.failure("修改失败");
        }
        return RespVOBuilder.success();
    }

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public RespVO delete(Long id) {
        int update = surveyPointExceptionMapper.deleteById(id);
        if(update<=0){
            return RespVOBuilder.failure("删除失败");
        }
        return RespVOBuilder.success();
    }

    @Override
    public RespVO<RespDataVO<SurveyPointExceptionVo>> list(int pageIndex, int pageSize, String sectionCode, String pointCode) {
        List<SurveyPointExceptionVo> surveyPointExceptionVos=new ArrayList<>();
        QueryWrapper<SurveyPointException> queryWrapper=new QueryWrapper<>();
        if(sectionCode!=null){
            queryWrapper.eq("section_code",sectionCode);
        }
        if(pointCode!=null){
            queryWrapper.eq("point_code",pointCode);
        }
        IPage<SurveyPointException> exceptionIPage = surveyPointExceptionMapper.selectPage(new Page<>(pageIndex, pageSize), queryWrapper);
        List<SurveyPointException> exceptionList = exceptionIPage.getRecords();
        if(exceptionList!=null){
            exceptionList.forEach(e -> surveyPointExceptionVos.add(SurveyPointExceptionVo.builder().build().loadData(e)));
        }
        return RespVOBuilder.success(surveyPointExceptionVos);
    }

    @Override
    public RespVO<SurveyPointException> queryById(Long id) {
        SurveyPointException pointException = surveyPointExceptionMapper.selectById(id);
        return RespVOBuilder.success(pointException);
    }

}
