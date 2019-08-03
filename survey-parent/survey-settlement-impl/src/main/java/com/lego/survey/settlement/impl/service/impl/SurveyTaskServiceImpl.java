package com.lego.survey.settlement.impl.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.lego.survey.base.exception.ExceptionBuilder;
import com.lego.survey.settlement.impl.mapper.SurveyOriginalMapper;
import com.lego.survey.settlement.impl.mapper.SurveyTaskMapper;
import com.lego.survey.settlement.impl.service.ISurveyTaskService;
import com.lego.survey.settlement.model.entity.SurveyOriginal;
import com.lego.survey.settlement.model.entity.SurveyTask;
import com.lego.survey.settlement.model.vo.SurveyTaskVo;
import com.survey.lib.common.consts.DictConstant;
import com.survey.lib.common.page.PagedResult;
import com.survey.lib.common.vo.RespVO;
import com.survey.lib.common.vo.RespVOBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.ArrayList;
import java.util.List;

/**
 * @author yanglf
 * @description
 * @since 2019/1/8
 **/
@Service
public class SurveyTaskServiceImpl implements ISurveyTaskService {

    @Autowired
    private SurveyTaskMapper surveyTaskMapper;

    @Autowired
    private SurveyOriginalMapper surveyOriginalMapper;

    @Override
    public PagedResult<SurveyTaskVo> list(int pageIndex, int pageSize, String tableName) {
        PagedResult<SurveyTaskVo> pagedResult=new PagedResult<>();
        List<SurveyTaskVo> surveyTaskVos = new ArrayList<>();
        Page<SurveyTask> surveyTaskPage = surveyTaskMapper.queryList(new Page(pageIndex, pageSize), tableName, new QueryWrapper());
        List<SurveyTask> records = surveyTaskPage.getRecords();
        if (records != null) {
            records.forEach(surveyTask -> surveyTaskVos.add(SurveyTaskVo.builder().build().loadSurveyTaskVo(surveyTask)));
        }
        pagedResult.setResultList(surveyTaskVos);
        pagedResult.setPage(new com.survey.lib.common.page.Page(pageIndex,pageSize,0,(int)surveyTaskPage.getTotal(),(int)surveyTaskPage.getPages()));
        return pagedResult;
    }

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public RespVO create(SurveyTaskVo surveyTaskVo, String sectionCode) {
        try {
            SurveyTask surveyTask = surveyTaskVo.getSurveyTask();
            // 添加   task
            SurveyTask storeTask=surveyTaskMapper.queryById(surveyTask.getId(),DictConstant.TableNamePrefix.SURVEY_TASK+sectionCode);
            if(storeTask==null){
                Integer save = surveyTaskMapper.save(surveyTask, DictConstant.TableNamePrefix.SURVEY_TASK+sectionCode);
                if (save <= 0) {
                    return RespVOBuilder.failure("添加任务失败");
                }
            }
            // 添加   原始数据
            Integer saveBatch;
            List<SurveyOriginal> surveyOriginals = surveyTaskVo.getSurveyOriginal();
            if (surveyOriginals != null) {
                saveBatch = surveyOriginalMapper.saveBatch(surveyOriginals, DictConstant.TableNamePrefix.SURVEY_ORIGINAL+sectionCode);
                if(saveBatch<=0){
                    ExceptionBuilder.operateFailException("添加任务失败");
                }
            }
            return RespVOBuilder.success();
        }catch (DuplicateKeyException ex){
            ex.printStackTrace();
            ExceptionBuilder.duplicateKeyException("主键冲突",surveyTaskVo.getId());
        }
        return RespVOBuilder.failure("添加任务失败");
    }

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public RespVO modify(SurveyTask surveyTask, String tableName) {
        Integer modify = surveyTaskMapper.modify(surveyTask, tableName);
        if (modify <= 0) {
            return RespVOBuilder.failure("修改任务失败");
        }
        return RespVOBuilder.success();
    }

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public RespVO delete(List<Long> ids, String tableName) {
        Integer deleteTask = surveyTaskMapper.deleteBatch(ids, tableName);
        if (deleteTask <= 0) {
            ExceptionBuilder.operateFailException("删除任务失败");
        }
        return RespVOBuilder.success();
    }
}
