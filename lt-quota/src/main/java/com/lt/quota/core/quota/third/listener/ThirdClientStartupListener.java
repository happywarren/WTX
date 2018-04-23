package com.lt.quota.core.quota.third.listener;

import com.lt.quota.core.comm.netty.client.listener.OnStartupListener;
import com.lt.quota.core.quota.WarningUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class ThirdClientStartupListener implements OnStartupListener {

    private Logger logger = LoggerFactory.getLogger(getClass());

    @Override
    public void onCompletion(boolean result, String host, Integer port) {
        try {
            if (!result) {
                WarningUtil.warn(host, port);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
