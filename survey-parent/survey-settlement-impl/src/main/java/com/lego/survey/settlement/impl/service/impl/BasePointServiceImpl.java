package com.lego.survey.settlement.impl.service.impl;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.lego.survey.base.exception.ExceptionBuilder;
import com.lego.survey.settlement.impl.mapper.BasePointMapper;
import com.lego.survey.settlement.impl.service.IBasePointService;
import com.lego.survey.settlement.model.entity.BasePoint;
import com.lego.survey.settlement.model.vo.BasePointVo;
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
 * @since 2019/1/7
 **/
@Service
public class BasePointServiceImpl implements IBasePointService {

    @Autowired
    private BasePointMapper basePointMapper;

    @Override
    public List<BasePointVo> list(String sectionCode, String name, Date startDate, Date endDate) {
        List<BasePointVo> basePointVos = new ArrayList<>();
        QueryWrapper<BasePoint> wrapper = new QueryWrapper<>();
        wrapper.eq("valid", 0);
        if (sectionCode != null) {
            wrapper.eq("section_code", sectionCode);
        }
        if (name != null) {
            wrapper.eq("name", name);
        }
        if (startDate != null && endDate != null) {
            wrapper.gt("create_time", startDate).lt("create_time", endDate);
        }
        wrapper.orderByDesc("create_time");
        List<BasePoint> basePoints = basePointMapper.selectList(wrapper);
        if (basePoints != null) {
            for (BasePoint basePoint : basePoints) {
                basePointVos.add(BasePointVo.builder().code(basePoint.getCode()).name(basePoint.getName())
                        .time(basePoint.getCreateTime())
                        .gridX(basePoint.getGridX())
                        .gridY(basePoint.getGridY())
                        .value(basePoint.getElevation())
                        .type(basePoint.getType())
                        .version(basePoint.getVersion()).build());
            }
        }
        return basePointVos;
    }

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public RespVO create(BasePoint basePoint) {
        try {
            basePoint.setCreateTime(new Date());
            basePoint.setUpdateTime(new Date());
            Integer version = basePointMapper.queryLastVersionBySectionCode(basePoint.getSectionCode());
            if (version == null) {
                version = 0;
            }
            basePoint.setVersion(version + 1);
            int insert = basePointMapper.insert(basePoint);
            if (insert <= 0) {
                return RespVOBuilder.failure("添加失败");
            }
            return RespVOBuilder.success();
        } catch (DuplicateKeyException ex) {
            ex.printStackTrace();
            ExceptionBuilder.duplicateKeyException("主键冲突",basePoint.getId());
        }
        return RespVOBuilder.failure("添加失败");
    }

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public RespVO delete(List<Long> ids) {
        for (Long id : ids) {
            BasePoint basePoint = basePointMapper.selectById(id);
            basePoint.setValid(1);
            basePoint.setUpdateTime(new Date());
            int update = basePointMapper.updateById(basePoint);
            if (update <= 0) {
                ExceptionBuilder.operateFailException("修改失败");
            }
        }
        return RespVOBuilder.success();
    }

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public RespVO modify(BasePoint basePoint) {
        int update = basePointMapper.updateById(basePoint);
        if (update <= 0) {
            return RespVOBuilder.failure("修改失败");
        }
        return RespVOBuilder.success();
    }

    @Override
    public RespVO<BasePoint> info(Long id) {
        BasePoint basePoint = basePointMapper.selectById(id);

        return RespVOBuilder.success(basePoint);
    }

    @Override
    public int queryLastVersionBySectionCode(String sectionCode) {
        return basePointMapper.queryLastVersionBySectionCode(sectionCode);
    }

    @Override
    public BasePoint queryByCodeOrName(String code, String name, String sectionCode) {
        QueryWrapper<BasePoint> wrapper = new QueryWrapper<>();
        wrapper.eq("section_code", sectionCode);
        wrapper.eq("code", name).or().eq("name", sectionCode);
        List<BasePoint> basePoints = basePointMapper.selectList(wrapper);
        if (basePoints != null && basePoints.size() > 0) {
            return basePoints.get(0);
        }
        return null;
    }
}
