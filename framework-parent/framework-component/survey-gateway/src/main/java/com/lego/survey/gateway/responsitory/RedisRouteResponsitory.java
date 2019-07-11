package com.lego.survey.gateway.responsitory;
import com.alibaba.fastjson.JSON;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.route.RouteDefinition;
import org.springframework.cloud.gateway.route.RouteDefinitionRepository;
import org.springframework.cloud.gateway.support.NotFoundException;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import java.util.ArrayList;
import java.util.List;

/*
 * @author yanglf
 * @description   优化   使用map 保存，通过消息进行动态更新
 * @since 2018/12/28
 **/

@Component
public class RedisRouteResponsitory implements RouteDefinitionRepository {

    public static final String GATEWAY_ROUTES = "gateway_routes";

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Override
    public Flux<RouteDefinition> getRouteDefinitions() {
        List<RouteDefinition> routeDefinitions = new ArrayList<>();
        //   从  redis  中获取配置的  route规则
        stringRedisTemplate.opsForHash().values(GATEWAY_ROUTES).forEach(routeDefinition -> {
            routeDefinitions.add(JSON.parseObject(routeDefinition.toString(), RouteDefinition.class));
        });
        return Flux.fromIterable(routeDefinitions);
    }

    @Override
    public Mono<Void> save(Mono<RouteDefinition> route) {
        return route.flatMap(routeDefinition  -> {
            //  向   redis  中存储 路由信息
            //   key  GATEWAY_ROUTES   value   routeDefinition.getId()   JsonObject.toString(routeDefinition)
            stringRedisTemplate.opsForHash().put(GATEWAY_ROUTES, routeDefinition.getId(),
                    JSON.toJSONString(routeDefinition));
            return Mono.empty();
        });
    }

    @Override
    public Mono<Void> delete(Mono<String> routeId) {
        // 从  redis 中删除配置的路由规则
        return routeId.flatMap(id -> {
            // 如果  redis 中存在 规则 删除
            if (stringRedisTemplate.opsForHash().hasKey(GATEWAY_ROUTES, id)) {
                stringRedisTemplate.opsForHash().delete(GATEWAY_ROUTES, id);
                return Mono.empty();
            }
            return Mono.defer(() -> Mono.error(new NotFoundException("RouteDefinition not found: " + routeId)));
        });
    }
}
