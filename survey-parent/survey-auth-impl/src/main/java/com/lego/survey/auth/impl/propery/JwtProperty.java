package com.lego.survey.auth.impl.propery;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @author yanglf
 * @descript
 * @since 2018/12/20
 **/
@ConfigurationProperties(prefix = "jwt.info")
@Component
@Data
public class JwtProperty {
    private String clientId;
    /**
     * 密钥
     */
    private String base64Secret;
    private String name;
    /**
     * APP 过期时间(秒)
     */
    private long appExpires;

    /**
     * WEB 过期时间 (秒)
     */
    private long webExpires;
}
