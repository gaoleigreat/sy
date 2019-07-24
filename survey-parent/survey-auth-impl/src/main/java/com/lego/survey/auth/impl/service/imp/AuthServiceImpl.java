package com.lego.survey.auth.impl.service.imp;

import com.alibaba.fastjson.JSONObject;
import com.lego.survey.auth.impl.propery.JwtProperty;
import com.lego.survey.auth.impl.service.IAuthService;
import com.lego.survey.auth.impl.service.IResourcesService;
import com.lego.survey.user.model.entity.OwnProject;
import com.lego.survey.user.model.entity.OwnSection;
import com.lego.survey.user.model.entity.User;
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


    @Value("${jwt.info.expiresSecond}")
    private int expiresSecond;

    private String prefix = "userToken:";

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Autowired
    private IResourcesService iResourcesService;


    @Override
    public TokenVo generateUserToken(User user, String deviceType) {
        if (user == null) {
            return null;
        }
        // 使用加密算法  HS256
        SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;

        long nowMillis = System.currentTimeMillis();
        Date nowDate = new Date(nowMillis);
        //生成签名密钥
        byte[] apiKeySecretBytes = DatatypeConverter.parseBase64Binary(jwtProperty.getBase64Secret());
        Key signingKey = new SecretKeySpec(apiKeySecretBytes, signatureAlgorithm.getJcaName());

        CurrentVo currentVo = generateCurrentVo(user,deviceType);

        JwtBuilder jwtBuilder = Jwts.builder().setHeaderParam("type", "JWT")
                .claim("current", currentVo)
                // 设置 jwt 的签发者
                .setIssuer(jwtProperty.getClientId())
                // 设置 接收 jwt 的名称
                .setAudience(jwtProperty.getName())
                //  设置  jwt 所面向的对象
                .setSubject(deviceType)
                .signWith(signatureAlgorithm, signingKey);
        AuthVo authVo = new AuthVo();
        authVo.setIssUer(jwtProperty.getClientId());
        authVo.setAudience(jwtProperty.getName());
        authVo.setSubject(deviceType);
        authVo.setCurrentVo(currentVo);
        //添加Token过期时间
        int expiresSecond = jwtProperty.getExpiresSecond();
        Date exp = new Date();
        if (expiresSecond >= 0) {
            long expMillis = nowMillis + (expiresSecond * 1000);
            exp = new Date(expMillis);
            // 设置  jwt  的过期时间
            jwtBuilder.setExpiration(exp)
                    // 如果当前时间在 nowDate 之前  token不生效
                    .setNotBefore(nowDate);
            authVo.setExpiration(exp);
            authVo.setNotBefore(nowDate);
        }
        //  头部(Header)、载荷(Payload)与签名(Signature)
        String token = jwtBuilder.compact();
        //  缓存  token 和  用户信息
        ValueOperations<String, String> ops = stringRedisTemplate.opsForValue();
        ops.set(prefix + currentVo.getUserId() + ":" + deviceType, token, expiresSecond, TimeUnit.SECONDS);
        ops.set(token, JSONObject.toJSONString(authVo), expiresSecond, TimeUnit.SECONDS);
        return TokenVo.builder().token(token).expireTime(exp).build();
    }

    private CurrentVo generateCurrentVo(User user,String deviceType) {
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
        currentVo.setProjectIds(projectCodes);
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
                userSectionVos.add(userSectionVo);
            });
        }
        currentVo.setUserSections(userSectionVos);

        currentVo.setDeviceType(deviceType);
        String role = user.getRole();
        if(role!=null){
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
            Boolean hasToken = stringRedisTemplate.hasKey(token);
            if (hasToken == null || !hasToken) {
                return null;
            }
            ValueOperations<String, String> ops = stringRedisTemplate.opsForValue();
            String authStr = ops.get(token);
            AuthVo authVo = JSONObject.parseObject(authStr, AuthVo.class);
            if (authVo == null) {
                return null;
            }
            CurrentVo currentVo = authVo.getCurrentVo();
            if (currentVo == null) {
                return null;
            }
            String userId = currentVo.getUserId();
            log.info("tokenKey:{}", prefix + userId + ":" + deviceType);
            Boolean hasValue = stringRedisTemplate.hasKey(prefix + userId + ":" + deviceType);
            if (hasValue == null || !hasValue) {
                return null;
            }
            String storeToken = ops.get(prefix + userId + ":" + deviceType);
            log.info("storeKey:{}", storeToken);
            if (!token.equals(storeToken)) {
                return null;
            }
            return authVo;
        } catch (Exception ex) {
            ex.printStackTrace();
            log.error("token 解析异常");
            return null;
        }
    }

    @Override
    public Boolean deleteUserToken(String userToken, String deviceType) {
        //  注销用户 token
        Boolean hasToken = stringRedisTemplate.hasKey(userToken);
        if (hasToken != null && hasToken) {
            ValueOperations<String, String> ops = stringRedisTemplate.opsForValue();
            String authStr = ops.get(userToken);
            AuthVo authVo = JSONObject.parseObject(authStr, AuthVo.class);
            if (authVo == null) {
                return true;
            }
            CurrentVo currentVo = authVo.getCurrentVo();
            if (currentVo == null) {
                return null;
            }
            stringRedisTemplate.delete(userToken);
            Boolean hasAuth = stringRedisTemplate.hasKey(prefix + currentVo.getUserId() + ":" + deviceType);
            if (hasAuth != null && hasAuth) {
                stringRedisTemplate.delete(prefix + currentVo.getUserId() + ":" + deviceType);
            }
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
    public Integer setUserToken(User user, String deviceType) {
        ValueOperations<String, String> ops = stringRedisTemplate.opsForValue();
        Boolean aBoolean = stringRedisTemplate.hasKey(prefix + user.getId() + ":" + deviceType);
        if (aBoolean == null || !aBoolean) {
            return 0;
        }

        String token = ops.get(prefix + user.getId() + ":" + deviceType);
        if (token == null) {
            return 0;
        }
        Boolean hasKey = stringRedisTemplate.hasKey(token);
        if (hasKey == null || !hasKey) {
            return 0;
        }
        String authVoStr = ops.get(token);
        AuthVo authVo = JSONObject.parseObject(authVoStr, AuthVo.class);
        if (authVo == null) {
            return 0;
        }
        CurrentVo currentVo = generateCurrentVo(user,deviceType);
        authVo.setCurrentVo(currentVo);
        Long expire = stringRedisTemplate.getExpire(token, TimeUnit.SECONDS);
        if (expire == null) {
            return 0;
        }
        ops.set(token, JSONObject.toJSONString(authVo), expire, TimeUnit.SECONDS);
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
                .claim("toService",toService)
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
        TokenVo tokenVo=new TokenVo();
        tokenVo.setToken(compact);
        tokenVo.setExpireTime(date);
        return tokenVo;
    }

    @Override
    public Boolean verifyServiceToken( String token) {
        try {
            Claims claims = Jwts.parser().setSigningKey(DatatypeConverter.parseBase64Binary(jwtProperty.getBase64Secret()))
                    .parseClaimsJws(token)
                    .getBody();
            return true;
        }catch (ExpiredJwtException ex){
            log.error("service  token  expire ");
        }catch (SignatureException | MalformedJwtException ex ){
            log.error(" service token invalid");
        }
        return null;
    }


}
