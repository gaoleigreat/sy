package com.lego.survey.ribbon;

import com.netflix.client.config.IClientConfig;
import com.netflix.loadbalancer.AbstractLoadBalancerRule;
import com.netflix.loadbalancer.ILoadBalancer;
import com.netflix.loadbalancer.Server;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.ArrayUtils;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
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

    private String[] localhost = {"192.168.101.103"};

    private String[] blackInstances = {"192.168.101.103:48090"};

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
        List<Server> chooseServer = getChooseServer(upList, localhost, blackInstances,loadBalancer);
        total++;
        currentIndex++;
        if (currentIndex >= chooseServer.size()) {
            currentIndex = 0;
        }
        Server server = chooseServer.get(currentIndex);
        log.info("choose service :[{}]", server);
        return server;
    }

    private List<Server> getChooseServer(List<Server> upList, String[] localhost, String[] blackInstances, ILoadBalancer loadBalancer) {
        List<Server> availableServers = new ArrayList<>();
        if (!CollectionUtils.isEmpty(upList)) {
            for (Server server : upList) {
                String host = server.getHost();
               /* if(ArrayUtils.contains(blackInstances,host)){
                    loadBalancer.markServerDown(server);
                    continue;
                }*/
                if (!ArrayUtils.contains(localhost, host)) {
                    continue;
                }
                if (server.isAlive()) {
                    availableServers.add(server);
                }
            }
        }
        return availableServers;
    }
}
