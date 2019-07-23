package com.lego.survey.settlement.impl.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.lego.survey.base.exception.ExceptionBuilder;
import com.lego.survey.project.feign.WorkspaceClient;
import com.lego.survey.project.model.entity.Workspace;
import com.lego.survey.settlement.impl.mapper.*;
import com.lego.survey.settlement.impl.service.ISurveyPointService;
import com.lego.survey.settlement.impl.service.ISurveyResultService;
import com.lego.survey.settlement.model.entity.*;
import com.lego.survey.settlement.model.vo.OverrunListVo;
import com.lego.survey.settlement.model.vo.SurveyPointVo;
import com.lego.survey.settlement.model.vo.SurveyPontResultVo;
import com.lego.survey.settlement.model.vo.SurveyResultVo;
import com.survey.lib.common.consts.DictConstant;
import com.survey.lib.common.consts.HttpConsts;
import com.survey.lib.common.consts.RespConsts;
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
    private WorkspaceClient workspaceClient;

    @Autowired
    private SurveyPointTypeMapper surveyPointTypeMapper;
    @Autowired
    private ISurveyPointService surveyPointSevice;

    @Autowired
    private SurveyOriginalMapper surveyOriginalMapper;

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
            Map<String, Object> map = new HashMap<>();
            map.put("workspaceCode", workspaceCode);
            if (startDate != null) {
                map.put("startDate", startDate);
                map.put("endDate", endDate);
            }

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
    public PagedResult<OverrunListVo> queryOverrunList(int pageIndex, Integer pageSize, String sectionId, String workspaceId, Integer type) {
        PagedResult<OverrunListVo> voPagedResult = new PagedResult<>();
        IPage<SurveyResult> iPage = new Page<>(pageIndex, pageSize);
        List<OverrunListVo> overrunListVos = new ArrayList<>();
        QueryWrapper<SurveyPoint> wrapper = new QueryWrapper<>();
        if (!StringUtils.isBlank(workspaceId)) {
            RespVO<Workspace> respVO = workspaceClient.info(workspaceId);
            if (respVO.getRetCode() == RespConsts.SUCCESS_RESULT_CODE) {
                Workspace info = respVO.getInfo();
                if (info != null) {
                    wrapper.eq("workspace_code", info.getCode());
                }
            }
        }
        Page<SurveyPoint> surveyPoints = surveyPointMapper.queryList(iPage, DictConstant.TableNamePrefix.SURVEY_POINT + sectionId, wrapper);
        List<SurveyPoint> records = surveyPoints.getRecords();
        Map<String, String> typeMap = getTypeMap();

        if (!CollectionUtils.isEmpty(records)) {
            for (SurveyPoint record : records) {
                QueryWrapper<SurveyPointException> exceptionWrapper = new QueryWrapper<>();
                exceptionWrapper.eq("result_id", record.getId());
                List<SurveyPointException> exceptionList = surveyPointExceptionMapper.selectList(exceptionWrapper);
                if (type != 0) {
                    if (CollectionUtils.isEmpty(exceptionList)) {
                        continue;
                    }
                }
                OverrunListVo overrun = new OverrunListVo();
                overrun.setPointCode(record.getCode());
                overrun.setPointName(record.getName());
                overrun.setInitValue(record.getElevation());
                overrun.setRemark(record.getComment());
                String sp = record.getType();
                overrun.setType(typeMap.get(sp) != null ? sp : sp);

                List<SurveyResult> surveyResults = surveyResultMapper.queryPreResult(null, DictConstant.TableNamePrefix.SURVEY_RESULT + sectionId, 1);
                if (!CollectionUtils.isEmpty(surveyResults)) {
                    SurveyResult surveyResult = surveyResults.get(0);
                    QueryWrapper<SurveyResult> wp = new QueryWrapper<>();
                    wp.le("survey_time", surveyResult.getSurveyTime());
                    List<SurveyResult> preResult = surveyResultMapper.queryPreResult(wp, DictConstant.TableNamePrefix.SURVEY_RESULT + sectionId, 1);
                    overrun.setPreValue(!CollectionUtils.isEmpty(preResult) ? preResult.get(0).getElevation() : 0);
                    overrun.setCumulativeSettlement(surveyResult.getCumulativeSettlement());
                    overrun.setCurSettlement(surveyResult.getSingleSettlement());
                    overrun.setSettlingRate(surveyResult.getSettlingRate());
                    overrun.setSurveyTime(surveyResult.getSurveyTime());
                    overrun.setCurValue(surveyResult.getElevation());
                    overrun.setSurveyer(surveyResult.getSurveyer());
                    SurveyOriginal surveyOriginal = surveyOriginalMapper.selectById(surveyResult.getOriginalId());
                    if(surveyOriginal!=null){
                        overrun.setTaskId(surveyOriginal.getTaskId());
                    }
                }

                if (!CollectionUtils.isEmpty(exceptionList)) {
                    SurveyPointException surveyPointException = exceptionList.get(0);
                    overrun.setIsException(true);
                    overrun.setExceptionType(surveyPointException.getType());
                } else {
                    overrun.setIsException(false);
                }

                overrunListVos.add(overrun);
            }
        }
        voPagedResult.setPage(new com.survey.lib.common.page.Page(pageIndex, pageSize, 0, surveyPoints.getTotal(), (int) surveyPoints.getPages()));
        voPagedResult.setResultList(overrunListVos);
        return voPagedResult;
    }

    private Map<String, String> getTypeMap() {
        Map<String, String> typeMap = new HashMap<>();
        List<SurveyPointType> typeList = surveyPointTypeMapper.selectList(null);
        if (!CollectionUtils.isEmpty(typeList)) {
            typeList.forEach(tp -> typeMap.put(tp.getName(), tp.getName()));
        }
        return typeMap;
    }

    @Override
    public List<SurveyResult> queryResult(String sectionId, List<Long> originalIds) {
        return surveyResultMapper.queryResult(DictConstant.TableNamePrefix.SURVEY_RESULT + sectionId, originalIds);
    }

    @Override
    public List<SurveyPontResultVo> queryPontResult(String sectionId, String ponitCode) {
        List<SurveyPontResultVo> surveyPontResultVos = new ArrayList<>();
        QueryWrapper<SurveyResult> wrapper = new QueryWrapper<>();
        wrapper.eq("point_code", ponitCode);
        List<SurveyResult> surveyResults = surveyResultMapper.queryList(DictConstant.TableNamePrefix.SURVEY_RESULT + sectionId, wrapper);
        SurveyPointVo surveyPointVo = surveyPointSevice.querySurveyPointByNameOrCode("", ponitCode, DictConstant.TableNamePrefix.SURVEY_POINT + sectionId);
        surveyResults.forEach(surveyResult -> surveyPontResultVos.add(SurveyPontResultVo.builder().build().loadSurveyResult(surveyResult, surveyPointVo)));
        return surveyPontResultVos;
    }

    @Override
    public PagedResult<OverrunListVo> queryOverrunDetails(int pageIndex, Integer pageSize, String sectionId, String pointCode, String workspaceId, Integer type) {
        PagedResult<OverrunListVo> voPagedResult = new PagedResult<>();
        IPage<SurveyResult> resultPage = new Page<>(pageIndex, pageSize);
        QueryWrapper<SurveyResult> resultWrapper = new QueryWrapper<>();
        resultWrapper.eq("point_code",pointCode);
        IPage<SurveyResult> surveyResultPage = surveyResultMapper.queryList(resultPage, DictConstant.TableNamePrefix.SURVEY_RESULT + sectionId, resultWrapper);
        List<SurveyResult> records = surveyResultPage.getRecords();
        List<OverrunListVo> overrunListVos = new ArrayList<>();
        if (!CollectionUtils.isEmpty(records)) {
            Map<String, String> typeMap = getTypeMap();
            for (SurveyResult record : records) {
                QueryWrapper<SurveyPointException> exceptionWrapper = new QueryWrapper<>();
                exceptionWrapper.eq("result_id", record.getId());
                List<SurveyPointException> exceptionList = surveyPointExceptionMapper.selectList(exceptionWrapper);
                if (CollectionUtils.isEmpty(exceptionList)) {
                    continue;
                }
                QueryWrapper<SurveyPoint> pointWrapper = new QueryWrapper<>();
                pointWrapper.eq("code", record.getPointCode());
                List<SurveyPoint> pointList = surveyPointMapper.queryByNameOrCode(pointWrapper, DictConstant.TableNamePrefix.SURVEY_POINT + sectionId);
                if (CollectionUtils.isEmpty(pointList)) {
                    continue;
                }
                SurveyPoint surveyPoint = pointList.get(0);
                OverrunListVo overrun = new OverrunListVo();
                overrun.setPointCode(surveyPoint.getCode());
                overrun.setPointName(surveyPoint.getName());
                overrun.setInitValue(surveyPoint.getElevation());
                String sp = surveyPoint.getType();
                overrun.setType(typeMap.get(sp) != null ? sp : sp);
                overrun.setPointStatus(surveyPoint.getStatus());
                overrun.setSurveyer(record.getSurveyer());
                SurveyOriginal surveyOriginal = surveyOriginalMapper.selectById(record.getOriginalId());
                if(surveyOriginal!=null){
                    overrun.setTaskId(surveyOriginal.getTaskId());
                }

                QueryWrapper<SurveyResult> wp = new QueryWrapper<>();
                wp.le("survey_time", record.getSurveyTime());
                List<SurveyResult> preResult = surveyResultMapper.queryPreResult(wp, DictConstant.TableNamePrefix.SURVEY_RESULT + sectionId, 1);

                overrun.setPreValue(!CollectionUtils.isEmpty(preResult) ? preResult.get(0).getElevation() : 0);
                overrun.setCumulativeSettlement(record.getCumulativeSettlement());
                overrun.setCurSettlement(record.getSingleSettlement());
                overrun.setSettlingRate(record.getSettlingRate());
                overrun.setSurveyTime(record.getSurveyTime());
                overrun.setCurValue(record.getElevation());


                SurveyPointException surveyPointException = exceptionList.get(0);
                overrun.setIsException(surveyPointException != null);
                if (surveyPointException != null) {
                    overrun.setRemark(surveyPointException.getMark());
                    overrun.setExceptionType(surveyPointException.getType());
                    overrun.setStatus(surveyPointException.getStatus());
                    overrun.setCloseTime(surveyPointException.getCloseTime());
                    overrun.setCloseUser(surveyPointException.getCloseUser());
                }
                overrunListVos.add(overrun);
            }
        }
        voPagedResult.setPage(new com.survey.lib.common.page.Page(pageIndex, pageSize, 0, surveyResultPage.getTotal(), (int) surveyResultPage.getPages()));
        voPagedResult.setResultList(overrunListVos);
        return voPagedResult;
    }

}
