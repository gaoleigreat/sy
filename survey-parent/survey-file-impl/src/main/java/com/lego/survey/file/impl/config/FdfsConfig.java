package com.lego.survey.file.impl.config;

import org.csource.common.MyException;
import org.csource.fastdfs.ClientGlobal;
import org.csource.fastdfs.TrackerClient;
import org.csource.fastdfs.TrackerGroup;
import org.csource.fastdfs.TrackerServer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

import java.io.File;
import java.io.IOException;

/**
 * fdfs 配置
 * weihao 2018-08-09
 */
@Configuration
public class FdfsConfig {

    private static final Logger log = LoggerFactory.getLogger(FdfsConfig.class);

    @Autowired
    private Environment env;

    @Bean
    public TrackerGroup trackerGroup(){
        TrackerGroup tg = null;
        try {
            String envFlag = env.getProperty("spring.profiles.active");
            if(null != envFlag && envFlag.indexOf("test") > -1){
                ClientGlobal.init("fdfs_client-"+envFlag+".conf");
            }else if(null != envFlag && "pro".equals(envFlag)){
                ClientGlobal.init("fdfs_client-pro.conf");
            }else{
                ClientGlobal.init("fdfs_client.conf");
            }
            tg = ClientGlobal.getG_tracker_group();
        } catch (IOException e) {
            log.error("");
        } catch (MyException e) {
            e.printStackTrace();
        }
        return tg;
    }
}
