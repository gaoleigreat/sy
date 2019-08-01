package com.lego.survey.gateway.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @author yanglf
 * @description  route filter info
 * @since 2019/1/11
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
public class GatewayFilterDefinition {
    //Filter Name
    private String name;
    //对应的路由规则
    private Map<String, String> args = new LinkedHashMap<>();
}
