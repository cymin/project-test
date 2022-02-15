package com.example.config;

import org.apache.catalina.connector.Connector;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.embedded.tomcat.TomcatConnectorCustomizer;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextClosedEvent;

import java.util.concurrent.Executor;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @ClassName GracefulShutdown  actuator停止服务的时候先关闭新的请求，然后等待已有连接释放
 * 也就是说只要还有请求没返回就会一直等待直到超过连接释放等待最长时间（通常50s），如果超过最长等待时间连接还未释放则强制停止服务
 * @Version V1.0
 **/
public class GracefulShutdown implements TomcatConnectorCustomizer, ApplicationListener<ContextClosedEvent> {
    @Value("${spring.lifecycle.timeout-per-shutdown-phase}")
    private String timeoutPerShutdownPhase;
    
    private volatile Connector connector;
    private static final Logger logger = LoggerFactory.getLogger(GracefulShutdown.class);
    
    @Override
    public void customize(Connector connector) {
        this.connector = connector;
    }

    @Override
    public void onApplicationEvent(ContextClosedEvent event) {
        logger.info("开始停止服务...准备关闭所有连接(包括注册中心)");
        Executor executor = this.connector.getProtocolHandler().getExecutor();
        if (executor instanceof ThreadPoolExecutor) {
            try {
                ThreadPoolExecutor threadPoolExecutor = (ThreadPoolExecutor) executor;
                threadPoolExecutor.shutdown();
                if (!threadPoolExecutor.awaitTermination(Long.parseLong(timeoutPerShutdownPhase.substring(0, timeoutPerShutdownPhase.length()-1)), TimeUnit.SECONDS)) {
                    logger.info("停止服务时,存在连接超过最大连接释放时间{},因未能释放连接，强制停止服务了，请相关人员检查异常接口，并做好补救措施", timeoutPerShutdownPhase);
                }
            } catch (InterruptedException ex) {
                Thread.currentThread().interrupt();
            }
        }
        logger.info("关闭所有连接成功...准备停止服务");
    }
}
