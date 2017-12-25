package com.lt.quota.core.quota.rf;

import com.lt.quota.core.comm.netty.client.BaseClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Component
public class RfTcpClient {

    private Logger logger = LoggerFactory.getLogger(getClass());

    public void start(String ip, int port) {
        logger.info("连接{} {} start", ip, port);
        StringBuilder key = new StringBuilder();
        key.append(ip).append(":").append(port);
        if (RfClientBox.getInstance().contains(key.toString())) {
            return;
        }
        RfTcpClientThread rfTcpClientThread = new RfTcpClientThread();
        rfTcpClientThread.setHost(ip);
        rfTcpClientThread.setPort(port);
        ExecutorService executorService = Executors.newSingleThreadExecutor();
        executorService.execute(rfTcpClientThread);
    }

    public void shutdown(String ip, int port) {
        logger.info("关闭{} {} start", ip, port);
        StringBuilder key = new StringBuilder();
        key.append(ip).append(":").append(port);
        if (!RfClientBox.getInstance().contains(key.toString())) {
            return;
        }
        Thread thread = RfClientBox.getInstance().getThread(key.toString());
        BaseClient baseClient = RfClientBox.getInstance().getBaseClient(key.toString());
        if (null != baseClient) {
            baseClient.shutdown();
        }
        if (null != thread) {
            thread.interrupt();
        }
        RfClientBox.getInstance().removeClient(key.toString());
    }
}
