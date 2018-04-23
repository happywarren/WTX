package com.lt.quota.core.quota.third;

import com.lt.quota.core.comm.netty.client.BaseClient;
import com.lt.quota.core.quota.rf.RfClientBox;
import com.lt.quota.core.quota.rf.handler.RfClientHandler;
import com.lt.quota.core.quota.rf.listener.RfClientListener;
import com.lt.quota.core.quota.rf.listener.RfClientStartupListener;
import com.lt.quota.core.quota.third.handler.ThirdClientHandler;
import com.lt.quota.core.quota.third.listener.ThirdClientListener;
import com.lt.quota.core.quota.third.listener.ThirdClientStartupListener;

public class ThirdTcpClientThread implements Runnable {

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
        ThirdBaseClient baseClient = new ThirdBaseClient(host, port, new ThirdClientHandler(new ThirdClientListener()), new ThirdClientStartupListener());
        Thread thread = new Thread(baseClient);
        ThirdClientBox.getInstance().setClient(host + ":" + port, baseClient, thread);
        thread.start();
    }
}
