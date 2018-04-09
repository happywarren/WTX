package com.lt.quota.core.quota.coin;

import com.lt.websocket.client.WebSocketClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

// 心跳包
@Component
public class WebsocketPongHandle {

    private Logger logger = LoggerFactory.getLogger(getClass());

    private final Map<String, Long> PONG_MAP = new ConcurrentHashMap<>();

    public void pongHandle(WebSocketClient webSocketClient) {
        /*
        long timestamp = System.currentTimeMillis();
        String host = webSocketClient.getURI().getHost();
        long lastTimestamp = 0L;
        if (PONG_MAP.containsKey(host)) {
            lastTimestamp = PONG_MAP.get(host);
            if (timestamp - lastTimestamp > 20 * 1000) {
                logger.info("websocket: {} 收到pong 超过: {}秒 断开重连", host, 10);
                webSocketClient.close();
            }
        }
        PONG_MAP.put(host, timestamp);*/
    }
}