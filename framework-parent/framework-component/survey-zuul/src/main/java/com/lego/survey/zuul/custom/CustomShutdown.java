package com.lego.survey.zuul.custom;

import lombok.extern.slf4j.Slf4j;
import org.apache.catalina.connector.Connector;
import org.springframework.boot.web.embedded.tomcat.TomcatConnectorCustomizer;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextClosedEvent;

import java.util.concurrent.Executor;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @author yanglf
 * @description
 * @since 2019/7/25
 **/
@Slf4j
public class CustomShutdown implements TomcatConnectorCustomizer, ApplicationListener<ContextClosedEvent> {

    private volatile Connector connector;

    private static final int TIMEOUT = 30;

    @Override
    public void customize(Connector connector) {
        this.connector = connector;

    }

    @Override
    public void onApplicationEvent(ContextClosedEvent event) {
        log.info("关闭  web 应用");
        // 暂停处理请求
        this.connector.pause();
        // 获取到 Connector 对应的线程池
        Executor executor = this.connector.getProtocolHandler().getExecutor();
        if (executor instanceof ThreadPoolExecutor) {
            try {
                ThreadPoolExecutor poolExecutor = (ThreadPoolExecutor) executor;
                poolExecutor.shutdown();
                if(!poolExecutor.awaitTermination(TIMEOUT, TimeUnit.SECONDS)){
                    log.warn("web 等待关闭时间超过 "+TIMEOUT+"秒,将强制关闭");
                    poolExecutor.shutdownNow();
                    if(!poolExecutor.awaitTermination(TIMEOUT, TimeUnit.SECONDS)){
                        log.warn("关闭失败.....");
                    }

                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }


        }

    }
}
