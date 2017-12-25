package com.lt.tms.tcp.server;

import com.lt.tms.tcp.server.config.NettyConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;


@Component
public class NettyTcpServerStartUp implements InitializingBean {

    private Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private NettyTcpServer nettyTcpServer;
    @Autowired
    private NettyConfig nettyConfig;

    @Override
    public void afterPropertiesSet() throws Exception {
        if (!nettyConfig.isStartup()) {
            logger.info("netty server not startup");
            return;
        }
        ScheduledExecutorService service = Executors.newSingleThreadScheduledExecutor();
        service.schedule(nettyTcpServer, 5, TimeUnit.SECONDS);
    }
}
