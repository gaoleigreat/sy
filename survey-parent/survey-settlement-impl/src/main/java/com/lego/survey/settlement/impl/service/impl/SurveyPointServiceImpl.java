package com.lego.survey.settlement.impl.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.lego.survey.base.exception.ExceptionBuilder;
import com.lego.survey.settlement.impl.mapper.SurveyPointMapper;
import com.lego.survey.settlement.impl.mapper.SurveyPointTypeMapper;
import com.lego.survey.settlement.impl.service.ISurveyPointService;
import com.lego.survey.settlement.model.entity.SurveyPoint;
import com.lego.survey.settlement.model.entity.SurveyPointType;
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
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import javax.validation.constraints.NotNull;
import java.util.*;

/**
 * @author yanglf
 * @description
 * @since 2019/1/8
 **/
@Service
public class SurveyPointServiceImpl implements ISurveyPointService {

    @Autowired
    private SurveyPointMapper surveyPointMapper;

    @Autowired
    private SurveyPointTypeMapper typeMapper;

    @Override
    public List<SurveyPointVo> list(int pageIndex, int pageSize, String deviceType, String workspaceCode, String tableName, Date startDate, Date endDate) {
        List<SurveyPointVo> surveyPointVos = new ArrayList<>();
        QueryWrapper<SurveyPoint> queryWrapper = new QueryWrapper<>();
        if (startDate != null) {
            queryWrapper.gt("create_time", startDate).lt("create_time", endDate);
        }
        if (workspaceCode != null) {
            queryWrapper.eq("workspace_code", workspaceCode);
        }
        List<SurveyPoint> list = null;
        if (deviceType.equals(HttpConsts.DeviceType.DEVICE_WEB)) {
            Page<SurveyPoint> surveyPointPage = surveyPointMapper.queryList(new Page(pageIndex, pageSize), tableName, queryWrapper);
            list = surveyPointPage.getRecords();
        } else if (deviceType.equals(HttpConsts.DeviceType.DEVICE_ANDROID)) {
            list = surveyPointMapper.queryList(tableName, queryWrapper);
        }
        Map<Long, String> typeMap = getTypeMap();
        if (list != null) {
            list.forEach(surveyPoint -> {
                String s = typeMap.get(surveyPoint.getType());
                SurveyPointVo surveyPointVo = SurveyPointVo.builder().build().loadData(surveyPoint, s);
                surveyPointVos.add(surveyPointVo);
            });
        }
        return surveyPointVos;
    }

    private Map<Long, String> getTypeMap() {
        Map<Long, String> map = new HashMap<>(16);
        List<SurveyPointType> selectList = typeMapper.selectList(null);
        if (!CollectionUtils.isEmpty(selectList)) {
            for (SurveyPointType surveyPointType : selectList) {
                map.put(surveyPointType.getId(), surveyPointType.getName() + "$" + surveyPointType.getLimits());
            }
        }
        return map;
    }

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public RespVO create(SurveyPointVo surveyPointVo, String tableName) {
        try {
            Date currentTime = new Date();
            surveyPointVo.setCreateTime(currentTime);
            surveyPointVo.setUpdateTime(currentTime);
            String code = surveyPointVo.getCode();
            if (StringUtils.isEmpty(code)) {
                surveyPointVo.setCode(surveyPointVo.getName());
            }
            try {
                Long type = surveyPointVo.getType();
                if (type != null) {
                    SurveyPointType surveyPointType = typeMapper.selectById(type);
                    String limits = surveyPointType.getLimits();
                    String[] split = limits.split(",");
                    if (split.length == 6) {
                        surveyPointVo.setOnceLowerLimit(Double.valueOf(split[0]));
                        surveyPointVo.setOnceUpperLimit(Double.valueOf(split[1]));
                        surveyPointVo.setTotalLowerLimit(Double.valueOf(split[2]));
                        surveyPointVo.setTotalUpperLimit(Double.valueOf(split[3]));
                        surveyPointVo.setSpeedLowerLimit(Double.valueOf(split[4]));
                        surveyPointVo.setSpeedUpperLimit(Double.valueOf(split[5]));
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            Integer save = surveyPointMapper.save(surveyPointVo.getSurveyPoint(), tableName);
            if (save <= 0) {
                return RespVOBuilder.failure("添加测点失败");
            }
            return RespVOBuilder.success();
        } catch (DuplicateKeyException ex) {
            ex.printStackTrace();
            ExceptionBuilder.duplicateKeyException("主键冲突", surveyPointVo.getId());
        }
        return RespVOBuilder.failure("添加测点失败");

    }

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public RespVO delete(List<String> codes, String tableName) {
        Integer integer = surveyPointMapper.deleteBatch(codes, tableName);
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
                String code = surveyPointVo.getCode();
                if (StringUtils.isEmpty(code)) {
                    surveyPointVo.setCode(surveyPointVo.getName());
                }
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
        } catch (DuplicateKeyException ex) {
            ex.printStackTrace();
            ExceptionBuilder.duplicateKeyException("主键冲突", 0L);
        }
        return RespVOBuilder.failure("批量新增测点失败");

    }

    @Override
    public SurveyPointVo querySurveyPointByNameOrCode(String name, String code, String tableName) {
        QueryWrapper<SurveyPoint> wrapper = new QueryWrapper<>();
        wrapper.eq("code", code).or().eq("name", name);
        List<SurveyPoint> surveyPoints = surveyPointMapper.queryByNameOrCode(wrapper, tableName);
        Map<Long, String> typeMap = getTypeMap();
        if (!CollectionUtils.isEmpty(surveyPoints)) {
            SurveyPoint surveyPoint = surveyPoints.get(0);
            String s = typeMap.get(surveyPoint.getType());
            return SurveyPointVo.builder().build().loadData(surveyPoint, s);
        }
        return null;
    }

    @Override
    public SurveyPointVo querySurveyPointByCode(String pointCode, String tableName) {
        QueryWrapper<SurveyPoint> wrapper = new QueryWrapper<>();
        wrapper.eq("code", pointCode);
        List<SurveyPoint> surveyPoints = surveyPointMapper.queryByName(wrapper, tableName);
        if (!CollectionUtils.isEmpty(surveyPoints)) {
            SurveyPoint surveyPoint = surveyPoints.get(0);
            String s = getTypeMap().get(surveyPoint.getType());
            return SurveyPointVo.builder().build().loadData(surveyPoint, s);
        }
        return null;
    }

    @Override
    public SurveyPointVo queryInitPoint(String tableName) {
        return surveyPointMapper.queryInitPoint(tableName);
    }


}
