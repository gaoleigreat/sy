package com.lego.survey.base.service;

import java.util.List;
import java.util.Map;

public interface IProgramCheckService {

    /**
     * 查询用户数据范围
     * @param dimensionCode
     * @param resourceValue
     * @param operationValue
     * @return
     */
    List<String> findProgramItems(String dimensionCode, String resourceValue, String operationValue);

    /**
     * 查询用户区域数据范围
     * @param resourceValue
     * @param operationValue
     * @return
     */
    Map<String, List<String>> findProgramByArea(String resourceValue, String operationValue);
}
