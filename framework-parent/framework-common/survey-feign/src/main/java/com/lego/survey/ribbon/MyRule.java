package com.lego.survey.ribbon;

import com.netflix.client.config.IClientConfig;
import com.netflix.loadbalancer.AbstractLoadBalancerRule;
import com.netflix.loadbalancer.ILoadBalancer;
import com.netflix.loadbalancer.Server;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

/**
 * @author yanglf
 * @description
 * @since 2019/7/17
 **/
@Slf4j
public class MyRule extends AbstractLoadBalancerRule {

    private int currentIndex = 0;
    private int total = 0;

    @Override
    public void initWithNiwsConfig(IClientConfig iClientConfig) {

    }

    @Override
    public Server choose(Object key) {
        return choose(getLoadBalancer(), key);
    }

    private Server choose(ILoadBalancer loadBalancer, Object key) {
        if (loadBalancer == null) {
            return null;
        }
        // 获取可用的服务
        List<Server> upList = loadBalancer.getReachableServers();
        // 获取全部服务
        List<Server> allServers = loadBalancer.getAllServers();
        // 非法服务剔除
        total++;
        currentIndex++;
        if (currentIndex >= upList.size()) {
            currentIndex = 0;
        }
        Server server = upList.get(currentIndex);
        if (!server.isAlive()) {
            choose(loadBalancer, key);
        }
        log.info("choose service :[{}]",server);
        return server;
    }
}
