package com.lego.survey.auth.impl.utils;

import com.lego.survey.auth.impl.propery.JwtProperty;
import com.lego.survey.user.model.entity.User;
import com.survey.lib.common.consts.HttpConsts;
import com.survey.lib.common.vo.CurrentVo;
import com.survey.lib.common.vo.TokenVo;
import io.jsonwebtoken.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.crypto.spec.SecretKeySpec;
import javax.xml.bind.DatatypeConverter;
import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * @author yanglf
 * @description
 * @since 2019/7/31
 **/
@Component
@Slf4j
public class JwtTokenUtil {

    @Autowired
    private JwtProperty jwtProperty;

    /**
     * 根据负责生成JWT的token
     */
    private TokenVo generateToken(Map<String, Object> claims, String deviceType) {
        // 使用加密算法  HS256
        SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS512;
        //生成签名密钥
        byte[] apiKeySecretBytes = DatatypeConverter.parseBase64Binary(jwtProperty.getBase64Secret());
        Key signingKey = new SecretKeySpec(apiKeySecretBytes, signatureAlgorithm.getJcaName());
        Date expirationDate = generateExpirationDate(deviceType);
        String token = Jwts.builder()
                .setHeaderParam("type", "JWT")
                .setClaims(claims)
                // 发行者
                .setIssuer(jwtProperty.getClientId())
                // 设置 接收 jwt 的名称  受众
                .setAudience(jwtProperty.getName())
                //  设置  jwt 所面向的对象
                .setSubject(deviceType)
                // 过期时间
                .setExpiration(expirationDate)
                .setNotBefore(new Date())
                .signWith(signatureAlgorithm, signingKey)
                .compact();
        return TokenVo.builder()
                .token(token)
                .deviceType(deviceType)
                .expireTime(expirationDate)
                .build();
    }

    /**
     * 从token中获取JWT中的负载
     */
    private Claims getClaimsFromToken(String token) {
        Claims claims = null;
        try {
            claims = Jwts.parser()
                    .setSigningKey(DatatypeConverter.parseBase64Binary(jwtProperty.getBase64Secret()))
                    .parseClaimsJws(token)
                    .getBody();
        } catch (ExpiredJwtException e) {
            e.printStackTrace();
            log.warn("token失效:{}", token);
        } catch (SignatureException e) {
            e.printStackTrace();
            log.warn("解析失败:{}", token);
        } catch (Exception e) {
            e.printStackTrace();
            log.warn("Token格式错误:{}", token);
        }
        return claims;
    }

    /**
     * 生成token的过期时间
     *
     * @param deviceType
     */
    private Date generateExpirationDate(String deviceType) {
        long expires = 0;
        if (deviceType.equals(HttpConsts.DeviceType.DEVICE_ANDROID)) {
            expires = jwtProperty.getAppExpires();
        } else if (deviceType.equals(HttpConsts.DeviceType.DEVICE_WEB)) {
            expires = jwtProperty.getWebExpires();
        }
        return new Date(System.currentTimeMillis() + expires * 1000);
    }

    /**
     * 从token中获取登录用户名
     */
    private String getUserNameFromToken(String token) {
        String username;
        try {
            Claims claims = getClaimsFromToken(token);
            username = claims.get("userName", String.class);
        } catch (Exception e) {
            username = null;
        }
        return username;
    }


    public TokenVo getTokenVoFromToken(String token) {
        TokenVo tokenVo = null;
        try {
            tokenVo = new TokenVo();
            Claims claims = getClaimsFromToken(token);
            String subject = claims.getSubject();
            Date expiration = claims.getExpiration();
            String userName = claims.get("userName", String.class);
            String userId = claims.get("userId", String.class);
            tokenVo.setToken(token);
            tokenVo.setExpireTime(expiration);
            tokenVo.setDeviceType(subject);
            tokenVo.setUserName(userName);
            tokenVo.setUserId(userId);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return tokenVo;
    }


    /**
     * 验证token是否还有效
     *
     * @param token 客户端传入的token
     * @param user  从数据库中查询出来的用户信息
     */
    public boolean validateToken(String token, User user) {
        String username = getUserNameFromToken(token);
        return username.equals(user.getUserName()) && !isTokenExpired(token);
    }

    /**
     * 判断token是否已经失效
     */
    private boolean isTokenExpired(String token) {
        Date expiredDate = getExpiredDateFromToken(token);
        return expiredDate.before(new Date());
    }

    /**
     * 从token中获取过期时间
     */
    private Date getExpiredDateFromToken(String token) {
        Claims claims = getClaimsFromToken(token);
        return claims.getExpiration();
    }

    /**
     * 根据用户信息生成token
     */
    public TokenVo generateToken(CurrentVo currentVo, String deviceType) {
        Map<String, Object> claims = getClamsMap(currentVo);
        return generateToken(claims, deviceType);
    }

    /**
     * 判断token是否可以被刷新
     */
    public boolean canRefresh(String token) {
        return !isTokenExpired(token);
    }

    /**
     * 刷新token
     */
    public TokenVo refreshToken(CurrentVo currentVo, String deviceType) {
        Map<String, Object> claims = getClamsMap(currentVo);
        return generateToken(claims, deviceType);
    }


    private Map<String, Object> getClamsMap(CurrentVo currentVo) {
        Map<String, Object> claims = new HashMap<>(4);
        claims.put("userName", currentVo.getUserName());
        claims.put("userId", currentVo.getUserId());
        claims.put("userPhone", currentVo.getPhone());
        claims.put("createTime", System.currentTimeMillis());
        return claims;
    }
}
