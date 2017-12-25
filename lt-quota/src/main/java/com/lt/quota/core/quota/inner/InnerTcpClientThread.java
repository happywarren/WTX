package com.lt.quota.core.quota.inner;

import com.lt.quota.core.comm.netty.client.BaseClient;
import com.lt.quota.core.quota.inner.handler.InnerClientHandler;
import com.lt.quota.core.quota.inner.listener.InnerClientListener;
import com.lt.quota.core.quota.inner.listener.InnerClientStartupListener;

public class InnerTcpClientThread implements Runnable {

    private String host;

    private int port;

    public void setHost(String host) {
        this.host = host;
    }

    public void setPort(int port) {
        this.port = port;
    }


    @Override
    public void run() {
        connect();
    }

    public void connect() {
        BaseClient baseClient = new BaseClient(host, port, new InnerClientHandler(new InnerClientListener()), new InnerClientStartupListener());
        Thread thread = new Thread(baseClient);
        InnerClientBox.getInstance().setClient(host + ":" + port, baseClient, thread);
        thread.start();
    }
}
