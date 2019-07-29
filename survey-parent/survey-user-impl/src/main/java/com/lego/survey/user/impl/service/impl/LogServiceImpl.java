package com.lego.survey.user.impl.service.impl;

import com.lego.survey.user.impl.repository.LogRepository;
import com.lego.survey.user.impl.repository.UserRepository;
import com.lego.survey.user.impl.service.ILogService;
import com.lego.survey.user.model.entity.Log;
import com.lego.survey.user.model.entity.User;
import com.lego.survey.user.model.vo.LogVo;
import com.survey.lib.common.page.PagedResult;
import com.survey.lib.common.vo.RespVO;
import com.survey.lib.common.vo.RespVOBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * @author yanglf
 * @description
 * @since 2018/12/28
 **/
@Service
public class LogServiceImpl implements ILogService {

    @Autowired
    private LogRepository logRepository;

    @Autowired
    private UserRepository userRepository;

    @Override
    public RespVO add(Log log) {
        logRepository.insert(log);
        return RespVOBuilder.success();
    }

    @Override
    public RespVO<PagedResult<LogVo>> list(int pageIndex, int pageSize) {
        PagedResult<LogVo> pagedResult = new PagedResult<>();
        List<LogVo> logVos = new ArrayList<>();
        Pageable pageable = PageRequest.of(pageIndex - 1, pageSize, Sort.Direction.DESC, "time");
        Page<Log> logPage = logRepository.findAll(pageable);
        List<Log> logList = logPage.getContent();
        for (Log log : logList) {
            User user = userRepository.findUserByIdAndValid(log.getUserId(), 0);
            LogVo logVo = LogVo.builder()
                    .ip(log.getIp())
                    .time(log.getTime())
                    .desc(log.getDesc())
                    .build();
            if (user != null) {
                logVo.setNickName(user.getName());
            }
            logVos.add(logVo);
        }
        pagedResult.setPage(new com.survey.lib.common.page.Page(pageIndex, pageSize, 0, logPage.getTotalElements(), logPage.getTotalPages()));
        pagedResult.setResultList(logVos);
        return RespVOBuilder.success(pagedResult);
    }

    @Override
    public Log findLastLoginLogByUserId(String userId) {
        List<Log> logList = logRepository.findLogByUserIdAndDescOrderByTimeDesc(userId, "用户登录");
        return !CollectionUtils.isEmpty(logList) ? logList.get(0) : null;
    }
}
