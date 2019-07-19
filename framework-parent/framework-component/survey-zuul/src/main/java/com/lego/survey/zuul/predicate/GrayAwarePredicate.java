package com.lego.survey.zuul.predicate;

import com.google.common.base.Optional;
import com.netflix.loadbalancer.AbstractServerPredicate;
import com.netflix.loadbalancer.PredicateKey;
import com.netflix.loadbalancer.Server;
import com.netflix.loadbalancer.ZoneAvoidancePredicate;
import com.netflix.niws.loadbalancer.DiscoveryEnabledServer;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.util.CollectionUtils;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

/**
 * @author yanglf
 * @description
 * @see ZoneAvoidancePredicate
 * @see AbstractServerPredicate
 * @since 2019/7/12
 **/
@Slf4j
public class GrayAwarePredicate extends AbstractServerPredicate {

    private final StringRedisTemplate stringRedisTemplate;

    private AtomicInteger nextInteger = new AtomicInteger();

    //private String blackList [] ={"192.168.101.103:48070"};

    private String localhost="192.168.104.16";

    public GrayAwarePredicate(StringRedisTemplate stringRedisTemplate) {
        this.stringRedisTemplate = stringRedisTemplate;
    }


    @Override
    public boolean apply(@Nullable PredicateKey predicateKey) {
        // 是否 流量 接入  从 Redis 中获取需要灰度的实例
        Set<String> grays = stringRedisTemplate.opsForSet().members("gray");
        assert predicateKey != null;
        DiscoveryEnabledServer server = (DiscoveryEnabledServer) predicateKey.getServer();

        if (!CollectionUtils.isEmpty(grays)) {
            grays = grays.stream().map(String::toUpperCase).collect(Collectors.toSet());
            if (predicateKey.getServer() instanceof DiscoveryEnabledServer) {
                // 判断 获取到的实例  是否和请求中的一致  一致 就禁用流量接入
                return !(grays.contains(server.getInstanceInfo().getInstanceId().toUpperCase()));
            } else {
                return !(grays.contains(predicateKey.getServer().getId().toUpperCase()));
            }
        }


        return true;
    }


    @Override
    public Optional<Server> chooseRoundRobinAfterFiltering(List<Server> servers, Object loadBalancerKey) {
        List<Server> eligibleServers = this.getEligibleServers(servers, loadBalancerKey);
        String targetVersion = RibbonVersionHolder.getContext();
        RibbonVersionHolder.clearContext();
        if (CollectionUtils.isEmpty(eligibleServers)) {
            return Optional.absent();
        }
        List<DiscoveryEnabledServer> targetServers = getTargetServers(eligibleServers, targetVersion);

        try {
            if (!CollectionUtils.isEmpty(targetServers)) {
                return Optional.of(targetServers.get(this.incrementAndGetModule(targetServers.size())));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return Optional.of(eligibleServers.get(0));
    }


    private List<DiscoveryEnabledServer> getTargetServers(List<Server> eligibleServers, String targetVersion) {
       /* if (StringUtils.isBlank(targetVersion)) {
            log.debug("客户端未配置目标版本直接路由");
            return null;
        }*/
        List<DiscoveryEnabledServer> targetServers = new ArrayList<>();
        for (Server eligibleServer : eligibleServers) {
            if (eligibleServer instanceof DiscoveryEnabledServer) {
                DiscoveryEnabledServer server = (DiscoveryEnabledServer) eligibleServer;
                String host = server.getHost();
                if(!host.equals(localhost)){
                    continue;
                }
               /* Map<String, String> metadata = server.getInstanceInfo().getMetadata();
                if (StringUtils.isBlank(metadata.get("version"))) {
                    log.debug("服务未设置 version");
                    continue;
                }
                if (!metadata.get("version").equals(targetVersion)) {
                    log.debug("当前微服务{} 版本为{}，目标版本{} 匹配失败", server.getInstanceInfo().getAppName()
                            , metadata.get("version"), targetVersion);
                    continue;
                }*/
                targetServers.add(server);
            }

        }
        return targetServers;
    }


    private int incrementAndGetModule(int module) {
        int current;
        int next;
        do {
            current = this.nextInteger.get();
            next = (current + 1) % module;
        } while (!this.nextInteger.compareAndSet(current, next));
        return current;

    }


}
