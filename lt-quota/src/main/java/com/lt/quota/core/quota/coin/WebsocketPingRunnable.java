package com.lt.quota.core.quota.coin;

import com.lt.quota.core.utils.Utils;
import com.lt.websocket.client.WebSocketClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

// 心跳包
public class WebsocketPingRunnable implements Runnable {

    private Logger logger = LoggerFactory.getLogger(getClass());

    private WebSocketClient webSocketClient;

    private String message;

    public WebsocketPingRunnable(final WebSocketClient webSocketClient, String message) {
        this.webSocketClient = webSocketClient;
        this.message = message;
    }

    public WebsocketPingRunnable(final WebSocketClient webSocketClient) {
        this.webSocketClient = webSocketClient;
    }

    @Override
    public void run() {
        /*
        if (Utils.isEmpty(message)) {
            message = "ping";
        }
        logger.info("WebsocketPingRunnable send {} ping {}", webSocketClient.getURI().getHost(), message);
        if (webSocketClient.isOpen()) {
            webSocketClient.send(message);
        }*/
    }
}