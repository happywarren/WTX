package com.lt.quota.core.server;

import com.lt.quota.core.comm.config.SysConfig;
import com.lt.quota.core.comm.netty.server.BaseServer;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@Component
public class AppServerStartup implements InitializingBean {

    @Autowired
    private SysConfig sysConfig;

    @Autowired
    private NettyServerHandler nettyServerHandler;

    @Autowired
    private NettyServerListener nettyServerListener;

    @Autowired
    private NettyServerStartupListener nettyServerStartupListener;



    @Override
    public void afterPropertiesSet() throws Exception {

        ScheduledExecutorService service = Executors.newSingleThreadScheduledExecutor();
        service.schedule(new Runnable() {
            @Override
            public void run() {
                BaseServer baseServer = new BaseServer(sysConfig.getServerPort(), nettyServerHandler, nettyServerStartupListener);

                baseServer.setReadTimeout(sysConfig.getReadTimeout());
                baseServer.setWriteTimeout(sysConfig.getWriteTimeout());
                baseServer.setAllTimeout(sysConfig.getAllTimeout());
                new Thread(baseServer).start();
            }
        },5, TimeUnit.SECONDS);

    }
}
