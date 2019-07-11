package com.lego.survey.base.service;

import com.survey.lib.common.vo.CurrentVo;

public interface IAuthCheckService {

    /**
     * 获取缓存数据
     * @param token
     * @return
     */
    CurrentVo getData(String token,String deviceType);

}
