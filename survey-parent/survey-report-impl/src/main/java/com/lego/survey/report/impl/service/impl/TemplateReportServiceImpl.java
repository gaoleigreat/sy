package com.lego.survey.report.impl.service.impl;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.lego.survey.report.model.entity.TemplateReport;
import com.lego.survey.report.impl.mapper.TemplateReportMapper;
import com.lego.survey.report.impl.service.ITemplateReportService;
import com.survey.lib.common.vo.RespDataVO;
import com.survey.lib.common.vo.RespVO;
import com.survey.lib.common.vo.RespVOBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

/**
 * @author yanglf
 * @description
 * @since 2019/1/7
 **/
@Service
public class TemplateReportServiceImpl implements ITemplateReportService {

    @Autowired
    private TemplateReportMapper templateReportMapper;


    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public RespVO create(TemplateReport templateReport) {
        Date currentTime = new Date();
        templateReport.setCreateTime(currentTime);
        templateReport.setUpdateTime(currentTime);
        int insert = templateReportMapper.insert(templateReport);
        if(insert<=0){
            return RespVOBuilder.failure("添加失败");
        }
        return RespVOBuilder.success();
    }

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public RespVO delete(Long id) {
        int delete = templateReportMapper.deleteById(id);
        if(delete<=0){
            return RespVOBuilder.failure("删除失败");
        }
        return RespVOBuilder.success();
    }

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public RespVO modify(TemplateReport templateReport) {
        templateReport.setUpdateTime(new Date());
        int update = templateReportMapper.updateById(templateReport);
        if(update<=0){
            return RespVOBuilder.failure("修改失败");
        }
        return RespVOBuilder.success();
    }

    @Override
    public RespVO<RespDataVO<TemplateReport>> list(int pageIndex, int pageSize,String sectionCode) {
        IPage<TemplateReport> page = new Page<>(pageIndex, pageSize);
        QueryWrapper<TemplateReport> wrapper = new QueryWrapper<>();
        if(sectionCode!=null){
            wrapper.eq("section_code",sectionCode);
        }
        wrapper.orderByDesc("create_time");
        IPage<TemplateReport> reportIPage = templateReportMapper.selectPage(page, wrapper);
        return RespVOBuilder.success(reportIPage.getRecords());
    }

    @Override
    public RespVO<TemplateReport> queryById(Long id) {
        templateReportMapper.selectById(id);
        return RespVOBuilder.success();
    }
}
