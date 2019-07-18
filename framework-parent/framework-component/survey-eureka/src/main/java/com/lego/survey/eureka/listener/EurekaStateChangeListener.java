package com.lego.survey.eureka.listener;

import com.netflix.appinfo.InstanceInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.cloud.netflix.eureka.server.event.*;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.List;

/**
 * @author yanglf
 * @description
 * @since 2019/6/13
 **/
@Slf4j
@Component
public class EurekaStateChangeListener {

    @Autowired
    private DiscoveryClient discoveryClient;

    @EventListener(condition = "#event.replication==false")
    public void listen(EurekaInstanceCanceledEvent event) {
        String appName = event.getAppName();
        String serviceId = event.getServerId();
        log.info("服务下线:--------------------:[{}]----------serviceId:[{}]",appName,serviceId);

        List<String> services = discoveryClient.getServices();
        if (!CollectionUtils.isEmpty(services)) {
            services.forEach(s -> {
                        log.info("服务:[{}]--------------------------------", s);
                        List<ServiceInstance> instances = discoveryClient.getInstances(s);
                        log.info("-------------实例数:[{}],实例信息:[{}]", instances.size(), instances);
                    }
            );
        }


        log.info("当前注册服务[{}],实例信息[{}]", appName, serviceId);
    }

    @EventListener(condition = "#event.replication==false")
    public void listen(EurekaInstanceRegisteredEvent event) {
        InstanceInfo instanceInfo = event.getInstanceInfo();
        String appName = instanceInfo.getAppName();
        String instanceId = instanceInfo.getInstanceId();
        log.info("服务上线:--------------------:[{}]----------serviceId:[{}]",appName,instanceId);

        List<String> services = discoveryClient.getServices();
        if (!CollectionUtils.isEmpty(services)) {
            services.forEach(s -> {
                        log.info("服务:[{}]--------------------------------", s);
                        List<ServiceInstance> instances = discoveryClient.getInstances(s);
                        log.info("-------------实例数:[{}],实例信息:[{}]", instances.size(), instances);
                    }
            );
        }
    }

    @EventListener(condition = "#event.replication==false")
    public void listen(EurekaInstanceRenewedEvent event) {
        // System.err.println(event.getServerId() + "\t" + event.getAppName() + " 服务进行续约");
    }

    @EventListener
    public void listen(EurekaRegistryAvailableEvent event) {
        // System.err.println("注册中心 启动");
    }

    @EventListener
    public void listen(EurekaServerStartedEvent event) {
        //System.err.println("Eureka Server 启动");
    }


}
