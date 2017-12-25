package com.lt.tms.tcp.server.listener;

import com.lt.tms.comm.netty.server.listener.StartupListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class NettyServerStartupListener implements StartupListener {

    private Logger logger = LoggerFactory.getLogger(getClass());

    @Override
    public void onCompletion(boolean result) {

    }
}
