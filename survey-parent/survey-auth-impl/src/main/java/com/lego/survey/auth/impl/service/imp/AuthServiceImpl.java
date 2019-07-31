package com.lego.survey.auth.impl.service.imp;

import com.alibaba.fastjson.JSONObject;
import com.lego.survey.auth.impl.propery.JwtProperty;
import com.lego.survey.auth.impl.service.IAuthService;
import com.lego.survey.auth.impl.service.IResourcesService;
import com.lego.survey.auth.impl.utils.JwtTokenUtil;
import com.lego.survey.user.model.entity.OwnProject;
import com.lego.survey.user.model.entity.OwnSection;
import com.lego.survey.user.model.entity.User;
import com.survey.lib.common.consts.HttpConsts;
import com.survey.lib.common.vo.AuthVo;
import com.survey.lib.common.vo.CurrentVo;
import com.survey.lib.common.vo.TokenVo;
import com.survey.lib.common.vo.UserSectionVo;
import io.jsonwebtoken.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import javax.crypto.spec.SecretKeySpec;
import javax.xml.bind.DatatypeConverter;
import java.security.Key;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * @author yanglf
 * @descript
 * @since 2018/12/20
 **/
@Service
@Slf4j
public class AuthServiceImpl implements IAuthService {

    @Autowired
    private JwtProperty jwtProperty;

    private String prefix = "survey:loginToken:";

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Autowired
    private IResourcesService iResourcesService;

    @Autowired
    private JwtTokenUtil jwtTokenUtil;


    @Override
    public TokenVo generateUserToken(User user, String deviceType) {
        if (user == null) {
            return null;
        }
        CurrentVo currentVo = generateCurrentVo(user, deviceType);
        AuthVo authVo = new AuthVo();
        authVo.setIssUer(jwtProperty.getClientId());
        authVo.setAudience(jwtProperty.getName());
        authVo.setSubject(deviceType);
        authVo.setCurrentVo(currentVo);
        authVo.setNotBefore(new Date());
        TokenVo tokenVo = jwtTokenUtil.generateToken(currentVo, deviceType);
        //  缓存  token 和  用户信息
        ValueOperations<String, String> ops = stringRedisTemplate.opsForValue();
        long expiresSecond = tokenVo.getExpireTime().getTime() / 1000;
        String token = tokenVo.getToken();
        authVo.setToken(token);
        authVo.setExpiration(tokenVo.getExpireTime());
        ops.set(prefix + currentVo.getUserId() + ":" + deviceType, JSONObject.toJSONString(authVo), expiresSecond, TimeUnit.SECONDS);
        tokenVo.setUserName(user.getUserName());
        tokenVo.setCardId(user.getCardId());
        tokenVo.setPermissions(user.getPermission());
        tokenVo.setRole(user.getRole());
        tokenVo.setUserId(user.getId());
        return tokenVo;
    }

    private CurrentVo generateCurrentVo(User user, String deviceType) {
        CurrentVo currentVo = new CurrentVo();
        currentVo.setGroupId(user.getGroup().getId());
        currentVo.setGroupName(user.getGroup().getName());
        currentVo.setName(user.getName());
        currentVo.setPermissions(user.getPermission());
        currentVo.setPhone(user.getPhone());
        List<OwnProject> ownProjects = user.getOwnProjects();
        List<String> projectCodes = new ArrayList<>();
        if (!CollectionUtils.isEmpty(ownProjects)) {
            ownProjects.forEach(ownProject -> projectCodes.add(ownProject.getCode()));
        }
        currentVo.setProjects(projectCodes);
        currentVo.setRole(user.getRole());
        currentVo.setUserId(user.getId());
        currentVo.setUserName(user.getUserName());
        List<UserSectionVo> userSectionVos = new ArrayList<>();
        List<OwnSection> ownSections = user.getOwnSections();
        if (!CollectionUtils.isEmpty(ownSections)) {
            ownSections.forEach(ownSection -> {
                UserSectionVo userSectionVo = new UserSectionVo();
                userSectionVo.setId(ownSection.getId());
                userSectionVo.setRole(ownSection.getName());
                userSectionVo.setCode(ownSection.getCode());
                userSectionVos.add(userSectionVo);
            });
        }
        currentVo.setUserSections(userSectionVos);

        currentVo.setDeviceType(deviceType);
        String role = user.getRole();
        if (role != null) {
            List<String> resource = iResourcesService.queryRoleResource(role);
            currentVo.setResourcesScopes(resource);
        }

        return currentVo;
    }

    @Override
    public AuthVo verifyUserToken(String token, String deviceType) {
        try {
            if (StringUtils.isEmpty(token)) {
                return null;
            }
            TokenVo tokenVo = jwtTokenUtil.getTokenVoFromToken(token);
            if (tokenVo == null) {
                return null;
            }
            String userId = tokenVo.getUserId();
            ValueOperations<String, String> ops = stringRedisTemplate.opsForValue();
            Boolean hasValue = stringRedisTemplate.hasKey(prefix + userId + ":" + deviceType);
            if (hasValue == null || !hasValue) {
                return null;
            }
            String authStr = ops.get(prefix + userId + ":" + deviceType);
            return JSONObject.parseObject(authStr, AuthVo.class);
        } catch (Exception ex) {
            ex.printStackTrace();
            log.error("token 解析异常");
            return null;
        }
    }

    @Override
    public Boolean deleteUserToken(String userToken, String deviceType) {
        //  注销用户 token
        TokenVo tokenVo = jwtTokenUtil.getTokenVoFromToken(userToken);
        if (tokenVo == null) {
            return null;
        }
        stringRedisTemplate.delete(userToken);
        Boolean hasAuth = stringRedisTemplate.hasKey(prefix + tokenVo.getUserId() + ":" + deviceType);
        if (hasAuth != null && hasAuth) {
            stringRedisTemplate.delete(prefix + tokenVo.getUserId() + ":" + deviceType);
        }
        return true;
    }

    @Override
    public String hasLogin(String userId, String deviceType) {
        Boolean hasKey = stringRedisTemplate.hasKey(prefix + userId + ":" + deviceType);
        if (hasKey != null && hasKey) {
            ValueOperations<String, String> ops = stringRedisTemplate.opsForValue();
            return ops.get(prefix + userId + ":" + deviceType);
        }
        return null;
    }

    @Override
    public Integer setUserToken(User user, String deviceType, String token) {
        ValueOperations<String, String> ops = stringRedisTemplate.opsForValue();

        String authStr = ops.get(prefix + user.getId() + ":" + deviceType);
        if (authStr == null) {
            return 0;
        }
        AuthVo authVo = JSONObject.parseObject(authStr, AuthVo.class);
        if (authVo == null) {
            return 0;
        }
        CurrentVo currentVo = generateCurrentVo(user, deviceType);
        authVo.setCurrentVo(currentVo);
        TokenVo tokenVo = jwtTokenUtil.getTokenVoFromToken(token);
        if (tokenVo == null || tokenVo.getExpireTime() == null) {
            return 0;
        }
        long expire = tokenVo.getExpireTime().getTime() / 1000;
        ops.set(prefix + user.getId() + ":" + deviceType, JSONObject.toJSONString(authVo), expire, TimeUnit.SECONDS);
        return 1;
    }

    @Override
    public TokenVo generateServiceToken(String fromService, String toService) {
        // 使用加密算法  HS256
        SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;

        long nowMillis = System.currentTimeMillis();
        Date nowDate = new Date(nowMillis);
        //生成签名密钥
        byte[] apiKeySecretBytes = DatatypeConverter.parseBase64Binary(jwtProperty.getBase64Secret());
        Key signingKey = new SecretKeySpec(apiKeySecretBytes, signatureAlgorithm.getJcaName());
        JwtBuilder jwtBuilder = Jwts.builder().setHeaderParam("type", "JWT")
                .claim("fromService", fromService)
                .claim("toService", toService)
                // 设置 jwt 的签发者
                .setIssuer(jwtProperty.getClientId())
                // 设置 接收 jwt 的名称
                .setAudience(jwtProperty.getName())
                //  设置  jwt 所面向的对象
                .setSubject("service")
                .signWith(signatureAlgorithm, signingKey);
        Date date = new Date(nowMillis + 1000 * 10);
        jwtBuilder.setExpiration(date)
                // 如果当前时间在 nowDate 之前  token不生效
                .setNotBefore(nowDate);
        String compact = jwtBuilder.compact();
        TokenVo tokenVo = new TokenVo();
        tokenVo.setToken(compact);
        tokenVo.setExpireTime(date);
        return tokenVo;
    }

    @Override
    public Boolean verifyServiceToken(String token) {
        try {
            Claims claims = Jwts.parser().setSigningKey(DatatypeConverter.parseBase64Binary(jwtProperty.getBase64Secret()))
                    .parseClaimsJws(token)
                    .getBody();
            return true;
        } catch (ExpiredJwtException ex) {
            log.error("service  token  expire ");
        } catch (SignatureException | MalformedJwtException ex) {
            log.error(" service token invalid");
        }
        return null;
    }


}
