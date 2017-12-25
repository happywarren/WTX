package com.lt.quota.core.appServer.listener;

import com.lt.quota.core.comm.netty.server.listener.OnStartupListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class NettyServerStartupListener implements OnStartupListener {

    private Logger logger = LoggerFactory.getLogger(getClass());

    @Override
    public void onCompletion(boolean result) {

    }
}
