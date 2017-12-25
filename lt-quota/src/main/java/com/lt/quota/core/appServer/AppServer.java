package com.lt.quota.core.appServer;

import com.lt.quota.core.appServer.handler.NettyServerHandler;
import com.lt.quota.core.comm.config.SysConfig;
import com.lt.quota.core.comm.netty.server.BaseServer;
import com.lt.quota.core.comm.netty.server.listener.OnStartupListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 作者: 邓玉明
 * 时间: 2017/5/5 下午1:33
 * email:cndym@163.com
 */
@Component
public class AppServer implements Runnable {


    @Autowired
    private SysConfig sysConfig;

    @Autowired
    private NettyServerHandler nettyServerHandler;
    @Autowired
    private OnStartupListener nettyServerStartupListener;

    @Override
    public void run() {
        BaseServer baseServer = new BaseServer(sysConfig.getServerPort(), nettyServerHandler, nettyServerStartupListener);

        baseServer.setReadTimeout(sysConfig.getReadTimeout());
        baseServer.setWriteTimeout(sysConfig.getWriteTimeout());
        baseServer.setAllTimeout(sysConfig.getAllTimeout());
        new Thread(baseServer).start();
    }
}
