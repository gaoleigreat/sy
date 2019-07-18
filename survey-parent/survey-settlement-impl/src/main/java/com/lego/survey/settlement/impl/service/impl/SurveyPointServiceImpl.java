package com.lego.survey.settlement.impl.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.lego.survey.base.exception.ExceptionBuilder;
import com.lego.survey.settlement.impl.mapper.SurveyPointMapper;
import com.lego.survey.settlement.impl.service.ISurveyPointService;
import com.lego.survey.settlement.model.entity.SurveyPoint;
import com.lego.survey.settlement.model.vo.SurveyPointVo;
import com.survey.lib.common.consts.HttpConsts;
import com.survey.lib.common.utils.HeaderUtils;
import com.survey.lib.common.utils.SnowflakeIdUtils;
import com.survey.lib.common.vo.HeaderVo;
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
public class SurveyPointServiceImpl implements ISurveyPointService {

    @Autowired
    private SurveyPointMapper surveyPointMapper;

    @Override
    public List<SurveyPointVo> list(int pageIndex, int pageSize, String deviceType, String workspaceCode, String tableName, Date startDate, Date endDate) {
        List<SurveyPointVo> surveyPointVos = new ArrayList<>();
        QueryWrapper<SurveyPoint> queryWrapper = new QueryWrapper<>();
        if (startDate != null) {
            queryWrapper.gt("create_time", startDate).lt("create_time", endDate);
        }
        if(workspaceCode!=null){
            queryWrapper.eq("workspace_code",workspaceCode);
        }
        List<SurveyPoint> list = null;
        if (deviceType.equals(HttpConsts.DeviceType.DEVICE_WEB)) {
            Page<SurveyPoint> surveyPointPage = surveyPointMapper.queryList(new Page(pageIndex, pageSize), tableName, queryWrapper);
            list=surveyPointPage.getRecords();
        } else if (deviceType.equals(HttpConsts.DeviceType.DEVICE_ANDROID)) {
            list = surveyPointMapper.queryList(tableName, queryWrapper);
        }
        if (list != null) {
            list.forEach(surveyPoint -> surveyPointVos.add(SurveyPointVo.builder().build().loadData(surveyPoint)));
        }
        return surveyPointVos;
    }

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public RespVO create(SurveyPointVo surveyPointVo, String tableName) {
        try {
            Date currentTime = new Date();
            surveyPointVo.setCreateTime(currentTime);
            surveyPointVo.setUpdateTime(currentTime);
            Integer save = surveyPointMapper.save(surveyPointVo.getSurveyPoint(), tableName);
            if (save <= 0) {
                return RespVOBuilder.failure("添加测点失败");
            }
            return RespVOBuilder.success();
        }catch (DuplicateKeyException ex){
            ex.printStackTrace();
            ExceptionBuilder.duplicateKeyException("主键冲突",surveyPointVo.getId());
        }
        return RespVOBuilder.failure("添加测点失败");

    }

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public RespVO delete(List<Long> ids, String tableName) {
        Integer integer = surveyPointMapper.deleteBatch(ids, tableName);
        if (integer <= 0) {
            ExceptionBuilder.operateFailException("删除测点失败");
        }
        return RespVOBuilder.success();
    }

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public RespVO modify(SurveyPoint surveyPoint, String tableName) {
        surveyPoint.setUpdateTime(new Date());
        Integer modify = surveyPointMapper.modify(surveyPoint, tableName);
        if (modify <= 0) {
            return RespVOBuilder.failure("修改测点失败");
        }
        return RespVOBuilder.success();
    }

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public RespVO createBatch(List<SurveyPointVo> surveyPointVos, String tableName) {
        try {
            List<SurveyPoint> surveyPoints = new ArrayList<>();

            surveyPointVos.forEach(surveyPointVo -> {
                surveyPointVo.setId(SnowflakeIdUtils.createId());
                Date currentTime = new Date();
                surveyPointVo.setCreateTime(currentTime);
                surveyPointVo.setUpdateTime(currentTime);
                surveyPoints.add(surveyPointVo.getSurveyPoint());
            });

            Integer save = surveyPointMapper.saveBatch(surveyPoints, tableName);
            if (save <= 0) {
                ExceptionBuilder.operateFailException("批量新增测点失败");
            }
            return RespVOBuilder.success();
        }catch (DuplicateKeyException ex){
            ex.printStackTrace();
            ExceptionBuilder.duplicateKeyException("主键冲突",0L);
        }
        return RespVOBuilder.failure("批量新增测点失败");

    }

    @Override
    public SurveyPointVo querySurveyPointByNameOrCode(String name, String code,String tableName) {
        QueryWrapper<SurveyPoint> wrapper = new QueryWrapper<>();
        wrapper.eq("code", code).or().eq("name", name);
        List<SurveyPoint> surveyPoints = surveyPointMapper.queryByNameOrCode(wrapper,tableName);
        if (surveyPoints != null && surveyPoints.size() > 0) {
            return SurveyPointVo.builder().build().loadData(surveyPoints.get(0));
        }
        return null;
    }

    @Override
    public SurveyPointVo querySurveyPointByCode(String pointCode,String tableName) {
        QueryWrapper<SurveyPoint> wrapper=new QueryWrapper<>();
        wrapper.eq("code",pointCode);
        List<SurveyPoint> surveyPoints=surveyPointMapper.queryByName(wrapper,tableName);
        if (surveyPoints != null && surveyPoints.size() > 0) {
            return SurveyPointVo.builder().build().loadData(surveyPoints.get(0));
        }
        return null;
    }

    @Override
    public SurveyPointVo queryInitPoint(String tableName) {
        return surveyPointMapper.queryInitPoint(tableName);
    }


}
