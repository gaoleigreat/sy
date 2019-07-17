package com.lego.survey.settlement.impl.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.lego.survey.base.exception.ExceptionBuilder;
import com.lego.survey.settlement.impl.mapper.SurveyPointExceptionMapper;
import com.lego.survey.settlement.impl.mapper.SurveyPointMapper;
import com.lego.survey.settlement.impl.mapper.SurveyPointTypeMapper;
import com.lego.survey.settlement.impl.mapper.SurveyResultMapper;
import com.lego.survey.settlement.impl.service.ISurveyResultService;
import com.lego.survey.settlement.model.entity.SurveyPoint;
import com.lego.survey.settlement.model.entity.SurveyPointException;
import com.lego.survey.settlement.model.entity.SurveyPointType;
import com.lego.survey.settlement.model.entity.SurveyResult;
import com.lego.survey.settlement.model.vo.OverrunListVo;
import com.lego.survey.settlement.model.vo.SurveyResultVo;
import com.survey.lib.common.consts.DictConstant;
import com.survey.lib.common.consts.HttpConsts;
import com.survey.lib.common.page.PagedResult;
import com.survey.lib.common.vo.RespVO;
import com.survey.lib.common.vo.RespVOBuilder;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.*;

/**
 * @author yanglf
 * @description
 * @since 2019/1/8
 **/
@Service
@Slf4j(topic = "service")
public class SurveyResultServiceImpl implements ISurveyResultService {

    @Autowired
    private SurveyResultMapper surveyResultMapper;

    @Autowired
    private SurveyPointExceptionMapper surveyPointExceptionMapper;

    @Autowired
    private SurveyPointMapper surveyPointMapper;

    @Autowired
    private SurveyPointTypeMapper surveyPointTypeMapper;

    @Override
    public RespVO list(int pageIndex, int pageSize, String workspaceCode, Date startDate, Date endDate, String deviceType, String tableName) {
        PagedResult<SurveyResultVo> pagedResult = new PagedResult<>();
        List<SurveyResultVo> surveyResultVos = new ArrayList<>();
        QueryWrapper<SurveyResult> wrapper = new QueryWrapper<>();
        wrapper.eq("workspace_code", workspaceCode);
        if (startDate != null) {
            wrapper.gt("survey_time", startDate).lt("survey_time", endDate);
        }
        if (deviceType.equals(HttpConsts.DeviceType.DEVICE_WEB)) {
            Page<SurveyResult> surveyResultPage = surveyResultMapper.queryList(new Page(pageIndex, pageSize), tableName, wrapper);
            surveyResultPage.getRecords().forEach(surveyResult -> surveyResultVos.add(SurveyResultVo.builder().build().loadSurveyResultVo(surveyResult)));
            pagedResult.setResultList(surveyResultVos);
            pagedResult.setPage(new com.survey.lib.common.page.Page(pageIndex, pageSize, 0, surveyResultPage.getTotal(), (int) surveyResultPage.getPages()));
            return RespVOBuilder.success(surveyResultPage);
        } else if (deviceType.equals(HttpConsts.DeviceType.DEVICE_ANDROID)) {
            List<SurveyResult> surveyResults = surveyResultMapper.queryList(tableName, wrapper);
            surveyResults.forEach(surveyResult -> surveyResultVos.add(SurveyResultVo.builder().build().loadSurveyResultVo(surveyResult)));
            return RespVOBuilder.success(surveyResultVos);
        } else {
            return null;
        }
    }

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public RespVO create(SurveyResult surveyResult, String tableName) {
        try {
            Date currentTime = new Date();
            // TODO 测试  setSurveyTime
            //surveyResult.setSurveyTime(currentTime);
            surveyResult.setUploadTime(currentTime);
            Integer save = surveyResultMapper.save(surveyResult, tableName);
            if (save <= 0) {
                return RespVOBuilder.failure("新增成果数据失败");
            }
            return RespVOBuilder.success();
        } catch (DuplicateKeyException ex) {
            ex.printStackTrace();
            ExceptionBuilder.duplicateKeyException("主键冲突", surveyResult.getId());
        }
        return RespVOBuilder.failure("新增成果数据失败");

    }


    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public RespVO modify(SurveyResult surveyResult, String tableName) {
        Integer modify = surveyResultMapper.modify(surveyResult, tableName);
        if (modify <= 0) {
            return RespVOBuilder.failure("修改成果数据失败");
        }
        return RespVOBuilder.success();
    }

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public RespVO delete(List<Long> ids, String tableName) {
        Integer deleteResult = surveyResultMapper.deleteBatch(ids, tableName);
        if (deleteResult <= 0) {
            ExceptionBuilder.operateFailException("删除成果数据失败");
        }
        return RespVOBuilder.success();
    }

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public RespVO createBatch(List<SurveyResult> surveyResults, String tableName) {
        try {
            Integer save = surveyResultMapper.saveBatch(surveyResults, tableName);
            if (save <= 0) {
                ExceptionBuilder.operateFailException("批量添加成果数据失败");
            }
            return RespVOBuilder.success();
        } catch (DuplicateKeyException ex) {
            ex.printStackTrace();
            ExceptionBuilder.duplicateKeyException("主键冲突", 0L);
        }
        return RespVOBuilder.failure("批量添加成果数据失败");

    }

    @Override
    public List<SurveyResult> queryPreResult(Date surveyTime, String tableName, int count, String pointCodes) {
        QueryWrapper<SurveyResult> wrapper = new QueryWrapper<>();
        if (surveyTime != null) {
            wrapper.lt("survey_time", surveyTime);
        }
        if (StringUtils.isNotBlank(pointCodes)) {
            wrapper.eq("point_code", pointCodes);
        }

        return surveyResultMapper.queryPreResult(wrapper, tableName, count);
    }

    @Override
    public SurveyResult queryInitResult(String tableName) {
        QueryWrapper<SurveyResult> wrapper = new QueryWrapper<>();
        return surveyResultMapper.queryInitResult(wrapper, tableName);
    }

    @Override
    public PagedResult<OverrunListVo> queryOverrunList(int pageIndex, Integer pageSize, String sectionId, Integer type) {
        PagedResult<OverrunListVo> voPagedResult = new PagedResult<>();
        IPage<SurveyResult> iPage = new Page<>(pageIndex, pageSize);
        List<OverrunListVo> overrunListVos = new ArrayList<>();
        QueryWrapper<SurveyResult> wrapper = new QueryWrapper<>();
        Page<SurveyResult> surveyResultPage = surveyResultMapper.queryList(iPage, DictConstant.TableNamePrefix.SURVEY_RESULT + sectionId, wrapper);
        List<SurveyResult> records = surveyResultPage.getRecords();
        Map<String, SurveyPoint> pointMap = new HashMap<>();
        List<SurveyPoint> surveyPoints = surveyPointMapper.queryByName(new QueryWrapper<>(), DictConstant.TableNamePrefix.SURVEY_POINT + sectionId);
        if (!CollectionUtils.isEmpty(surveyPoints)) {
            surveyPoints.forEach(surveyPoint -> pointMap.put(surveyPoint.getCode(), surveyPoint));
        }
        Map<String, String> typeMap = new HashMap<>();
        List<SurveyPointType> typeList = surveyPointTypeMapper.selectList(null);
        if (!CollectionUtils.isEmpty(typeList)) {
            typeList.forEach(tp -> typeMap.put(tp.getCode(), tp.getName()));
        }

        if (!CollectionUtils.isEmpty(records)) {
            for (SurveyResult record : records) {
                QueryWrapper<SurveyPointException> exceptionWrapper = new QueryWrapper<>();
                exceptionWrapper.eq("result_id", record.getId());
                List<SurveyPointException> exceptionList = surveyPointExceptionMapper.selectList(exceptionWrapper);
                if (type != 0) {
                    if (CollectionUtils.isEmpty(exceptionList)) {
                        continue;
                    }
                }
                QueryWrapper<SurveyResult> wp = new QueryWrapper<>();
                wp.le("survey_time", record.getSurveyTime());
                List<SurveyResult> preResult = surveyResultMapper.queryPreResult(wp, DictConstant.TableNamePrefix.SURVEY_RESULT + sectionId, 1);
                SurveyPoint surveyPoint = pointMap.get(record.getPointCode());
                OverrunListVo overrun = new OverrunListVo();
                overrun.setCumulativeSettlement(record.getCumulativeSettlement());
                overrun.setCurSettlement(record.getSingleSettlement());
                overrun.setCurValue(record.getElevation());
                overrun.setInitValue(surveyPoint.getElevation());
                overrun.setPointName(record.getPointName());
                overrun.setPreValue(!CollectionUtils.isEmpty(preResult) ? preResult.get(0).getElevation() : 0);
                overrun.setRemark(surveyPoint.getComment());
                overrun.setSettlingRate(record.getSettlingRate());
                overrun.setSurveyTime(record.getSurveyTime());
                String sp = surveyPoint.getType();
                overrun.setType(typeMap.get(sp) != null ? sp : sp);
                if (!CollectionUtils.isEmpty(exceptionList)) {
                    overrun.setIsException(true);
                } else {
                    overrun.setIsException(false);
                }

                overrunListVos.add(overrun);
            }
        }
        voPagedResult.setPage(new com.survey.lib.common.page.Page(pageIndex, pageSize, 0, surveyResultPage.getTotal(), (int) surveyResultPage.getPages()));
        voPagedResult.setResultList(overrunListVos);
        return voPagedResult;
    }

    @Override
    public List<SurveyResult> queryResult(String sectionId, List<Long> originalIds) {

        List<SurveyResult> surveyResults = surveyResultMapper.queryResult(DictConstant.TableNamePrefix.SURVEY_RESULT + sectionId, originalIds);
        //
        return surveyResults;
    }

    @Override
    public List<SurveyResultVo> queryPontResult(String sectionId, String ponitCode) {
        List<SurveyResultVo> surveyResultVos = new ArrayList<>();
        QueryWrapper<SurveyResult> wrapper = new QueryWrapper<>();
        wrapper.eq("point_code",ponitCode);;
        List<SurveyResult> surveyResults = surveyResultMapper.queryList(sectionId,wrapper);
        surveyResults.forEach(surveyResult -> surveyResultVos.add(SurveyResultVo.builder().build().loadSurveyResultVo(surveyResult)));
        return null;
    }

}
