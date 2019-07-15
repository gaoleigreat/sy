package com.lego.survey.eureka.config;

import com.netflix.appinfo.InstanceInfo;
import com.netflix.discovery.BackupRegistry;
import com.netflix.discovery.shared.Application;
import com.netflix.discovery.shared.Applications;

/**
 * @author yanglf
 * @description    全部服务不可用时  加载默认的地址
 * @since 2019/7/15
 **/

public class BackupServerRegistry implements BackupRegistry {

    private Applications loadRegionApps = new Applications();


    public BackupServerRegistry() {
        Application app = new Application("org");
        InstanceInfo instance1=InstanceInfo.Builder.newBuilder()
                .setAppName("")

                .build();

        app.addInstance(instance1);
        loadRegionApps.addApplication(app);
    }

    @Override
    public Applications fetchRegistry() {
        return loadRegionApps;
    }

    @Override
    public Applications fetchRegistry(String[] strings) {
        return null;
    }
}
