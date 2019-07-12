package com.lego.survey.settlement.impl.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.lego.survey.base.exception.ExceptionBuilder;
import com.lego.survey.settlement.impl.mapper.SurveyOriginalMapper;
import com.lego.survey.settlement.impl.service.ISurveyOriginalService;
import com.lego.survey.settlement.model.entity.SurveyOriginal;
import com.lego.survey.settlement.model.vo.SurveyOriginalVo;
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
 * @since 2019/1/8
 **/
@Service
public class SurveyOriginalServiceImpl implements ISurveyOriginalService {

    @Autowired
    private SurveyOriginalMapper surveyOriginalMapper;


    @Override
    public List<SurveyOriginalVo> list(int pageIndex, int pageSize, Long taskId, String tableName) {
        List<SurveyOriginalVo> surveyOriginalVos = new ArrayList<>();
        QueryWrapper<SurveyOriginal> queryWrapper = new QueryWrapper<>();
        if (taskId != null) {
            queryWrapper.eq("task_id", taskId);
        }
        IPage<SurveyOriginal> surveyOriginals = surveyOriginalMapper.queryList(new Page(pageIndex, pageSize), tableName, queryWrapper);
        if (surveyOriginals != null) {
            List<SurveyOriginal> records = surveyOriginals.getRecords();
            records.forEach(surveyOriginal -> surveyOriginalVos.add(SurveyOriginalVo.builder().build().loadSurveyOriginalVo(surveyOriginal)));
        }
        return surveyOriginalVos;
    }

    @Override
    public List<SurveyOriginalVo> list(Long taskId, String tableName) {
        List<SurveyOriginalVo> surveyOriginalVos = new ArrayList<>();
        QueryWrapper<SurveyOriginal> queryWrapper = new QueryWrapper<>();
        if (taskId != null && tableName !=null) {
            queryWrapper.eq("task_id", taskId);
        }
        List<SurveyOriginal> surveyOriginals = surveyOriginalMapper.queryAll(tableName,queryWrapper);

        if (surveyOriginals != null) {
            surveyOriginals.forEach(surveyOriginal -> surveyOriginalVos.add(SurveyOriginalVo.builder().build().loadSurveyOriginalVo(surveyOriginal)));
        }
        return surveyOriginalVos;
    }

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public RespVO create(SurveyOriginal surveyOriginal, String tableName) {
        try {
            surveyOriginal.setUploadTime(new Date());
            Integer save = surveyOriginalMapper.save(surveyOriginal, tableName);
            if (save <= 0) {
                return RespVOBuilder.failure("新增原始数据失败");
            }
            return RespVOBuilder.success();
        }catch (DuplicateKeyException ex){
            ex.printStackTrace();
            ExceptionBuilder.duplicateKeyException("主键冲突",surveyOriginal.getId());
        }
        return RespVOBuilder.failure("新增原始数据失败");

    }

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public RespVO modify(SurveyOriginal surveyOriginal, String tableName) {
        Integer modify = surveyOriginalMapper.modify(surveyOriginal, tableName);
        if (modify <= 0) {
            return RespVOBuilder.failure("修改原始数据失败");
        }
        return RespVOBuilder.success();
    }

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public RespVO delete(List<Long> ids, String tableName) {
        Integer delete = surveyOriginalMapper.deleteBatch(ids, tableName);
        if (delete <= 0) {
            return RespVOBuilder.failure("删除原始数据失败");
        }
        return RespVOBuilder.success();
    }

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public RespVO createBatch(List<SurveyOriginal> surveyOriginals, String tableName) {
        try {
            Integer result = surveyOriginalMapper.saveBatch(surveyOriginals, tableName);
            if (result <= 0) {
                return RespVOBuilder.failure("新增原始数据失败");
            }
            return RespVOBuilder.success();
        }catch (DuplicateKeyException ex){
            ex.printStackTrace();
            ExceptionBuilder.duplicateKeyException("主键冲突",0L);
        }
        return RespVOBuilder.failure("新增原始数据失败");

    }
}
