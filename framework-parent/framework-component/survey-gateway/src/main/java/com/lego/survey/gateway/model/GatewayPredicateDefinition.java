package com.lego.survey.gateway.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @author yanglf
 * @description  predicate info
 * @since 2019/1/11
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
public class GatewayPredicateDefinition {
    //断言对应的Name
    private String name;
    //配置的断言规则
    private Map<String, String> args = new LinkedHashMap<>();
}
