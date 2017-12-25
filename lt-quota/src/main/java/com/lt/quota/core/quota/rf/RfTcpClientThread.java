package com.lt.quota.core.quota.rf;

import com.lt.quota.core.comm.netty.client.BaseClient;
import com.lt.quota.core.quota.rf.handler.RfClientHandler;
import com.lt.quota.core.quota.rf.listener.RfClientListener;
import com.lt.quota.core.quota.rf.listener.RfClientStartupListener;

public class RfTcpClientThread implements Runnable {

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
        BaseClient baseClient = new BaseClient(host, port, new RfClientHandler(new RfClientListener()), new RfClientStartupListener());
        Thread thread = new Thread(baseClient);
        RfClientBox.getInstance().setClient(host + ":" + port, baseClient, thread);
        thread.start();
    }
}
