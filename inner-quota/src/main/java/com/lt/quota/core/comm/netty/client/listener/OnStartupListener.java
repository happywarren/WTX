package com.lt.quota.core.comm.netty.client.listener;

/**
 * Created by sunch on 2016/12/9.
 */
public interface OnStartupListener {

    void onCompletion(boolean result, String host, Integer port);

}
