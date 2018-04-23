package com.lt.quota.core.quota.third;

import com.lt.quota.core.quota.rf.RfClientBox;
import com.lt.quota.core.quota.rf.RfTcpClientThread;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Component
public class ThirdTcpClient {

    private Logger logger = LoggerFactory.getLogger(getClass());

    private static ThirdTcpClient instance;

    public static synchronized ThirdTcpClient getInstance() {
        if (instance == null) {
            instance = new ThirdTcpClient();
        }
        return instance;
    }

    public void start(String ip, int port) {
        logger.info("连接{} {} start", ip, port);
        StringBuilder key = new StringBuilder();
        key.append(ip).append(":").append(port);
        if (RfClientBox.getInstance().contains(key.toString())) {
            return;
        }
        ThirdTcpClientThread rfTcpClientThread = new ThirdTcpClientThread();
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
        ThirdBaseClient baseClient = ThirdClientBox.getInstance().getBaseClient(key.toString());
        if (null != baseClient) {
            baseClient.shutdown();
        }
        if (null != thread) {
            thread.interrupt();
        }
        ThirdClientBox.getInstance().removeClient(key.toString());
    }
}
