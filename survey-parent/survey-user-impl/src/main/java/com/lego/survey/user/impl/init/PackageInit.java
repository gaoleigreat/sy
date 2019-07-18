package com.lego.survey.user.impl.init;
import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

@Component
public class PackageInit implements InitializingBean {

    public static final Logger log = LoggerFactory.getLogger(PackageInit.class);

    private byte[] appApk;

    @Value("${package.packageDir}")
    private String apkPath;

    @Value("${package.versionPath}")
    private String versionPath;

    private Map<String, Object> appVersion;

    @Override
    public void afterPropertiesSet() throws Exception {
        refresh();
    }

    private void initApkVersion(){
        Properties properties = new Properties();
        try {
            properties.load(new FileInputStream(versionPath));
            String version = properties.getProperty("version");
            String forcedUpgrade = properties.getProperty("forcedUpgrade");
            appVersion = new HashMap<>();
            appVersion.put("version",version);
            appVersion.put("forcedUpgrade",Boolean.valueOf(forcedUpgrade));
            appVersion.put("apkLength", appApk.length);
        } catch (IOException e) {
            log.error("init apk error", e);
        }
    }

    private void initApk(){
        File f = new File(apkPath);
        byte[] datas = null;
        if (f.exists() && f.isFile()){
            try {
                datas  = FileUtils.readFileToByteArray(f);
            } catch (IOException e) {
                log.error("read file exception", e);
            }
        }

        appApk = datas;
    }

    public void refresh(){
        initApk();
        initApkVersion();
    }

    public byte[] getAppApk() {
        return appApk;
    }

    public Map<String, Object> getAppVersion() {
        return appVersion;
    }
}
