package com.lt.quota.core.quota.ib;

import com.lt.quota.core.comm.netty.client.BaseClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Component
public class IbTcpClient {

    private Logger logger = LoggerFactory.getLogger(getClass());

    public void start(String ip, int port) {
        logger.info("连接{} {} start", ip, port);
        StringBuilder key = new StringBuilder();
        key.append(ip).append(":").append(port);
        if (IbClientBox.getInstance().contains(key.toString())) {
            return;
        }
        IbTcpClientThread ibTcpClientThread = new IbTcpClientThread();
        ibTcpClientThread.setHost(ip);
        ibTcpClientThread.setPort(port);
        ExecutorService executorService = Executors.newSingleThreadExecutor();
        executorService.execute(ibTcpClientThread);
    }

    public void shutdown(String ip, int port) {
        logger.info("关闭{} {} start", ip, port);
        StringBuilder key = new StringBuilder();
        key.append(ip).append(":").append(port);
        if (!IbClientBox.getInstance().contains(key.toString())) {
            return;
        }
        Thread thread = IbClientBox.getInstance().getThread(key.toString());
        BaseClient baseClient = IbClientBox.getInstance().getBaseClient(key.toString());
        if (null != baseClient) {
            baseClient.shutdown();
        }
        if (null != thread) {
            thread.interrupt();
        }
        IbClientBox.getInstance().removeClient(key.toString());
    }
}
