package com.lt.tms.tcp.server;

import com.lt.tms.comm.netty.server.BaseServer;
import com.lt.tms.comm.netty.server.listener.MessageListener;
import com.lt.tms.comm.netty.server.listener.StartupListener;
import com.lt.tms.tcp.server.config.NettyConfig;
import com.lt.tms.tcp.server.handler.NettyServerHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class NettyTcpServer implements Runnable {

    private Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private StartupListener nettyServerStartupListener;

    @Autowired
    private NettyConfig nettyConfig;
    @Autowired
    private NettyServerHandler nettyServerHandler;

    @Override
    public void run() {
        if (nettyConfig.isStartup()) {
            logger.info("启动netty服务端监听");
            BaseServer baseServer = new BaseServer(nettyConfig.getPort(), nettyServerStartupListener, nettyServerHandler);
            baseServer.setConnectTimeout(nettyConfig.getSoTimeout());
            new Thread(baseServer).start();
        }
    }
}