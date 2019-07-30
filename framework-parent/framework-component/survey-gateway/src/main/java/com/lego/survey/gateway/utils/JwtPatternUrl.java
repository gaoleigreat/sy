package com.lego.survey.gateway.utils;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author yanglf
 * @descript
 * @since 2018/12/20
 **/
@ConfigurationProperties(prefix = "jwt.exclude")
@Component
@Data
public class JwtPatternUrl {
    private List<String> urlPatterns;
}
