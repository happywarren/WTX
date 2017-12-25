package com.lt.quota.core.quota.ib;

import com.lt.quota.core.comm.netty.client.BaseClient;
import com.lt.quota.core.quota.ib.handler.IbClientHandler;
import com.lt.quota.core.quota.ib.listener.IbClientListener;
import com.lt.quota.core.quota.ib.listener.IbClientStartupListener;

public class IbTcpClientThread implements Runnable {

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
        BaseClient baseClient = new BaseClient(host, port, new IbClientHandler(new IbClientListener()), new IbClientStartupListener());
        Thread thread = new Thread(baseClient);
        IbClientBox.getInstance().setClient(host + ":" + port, baseClient, thread);
        thread.start();
    }
}
