package com.lt.quota.core.quota.coin.bitmex;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.lt.quota.core.quota.coin.CoinQuotaCalc;
import com.lt.quota.core.quota.coin.WebsocketPingRunnable;
import com.lt.quota.core.quota.coin.WebsocketPongHandle;
import com.lt.quota.core.quota.coin.bo.CoinQuotaBO;
import com.lt.quota.core.utils.FastJsonUtils;
import com.lt.quota.core.utils.Utils;
import com.lt.websocket.client.WebSocketClient;
import com.lt.websocket.handshake.ServerHandshake;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.net.URI;
import java.util.Map;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @author mcsong
 * @create 2017-10-20 17:16
 */
@Component
public class BitmexClient {

    private Logger logger = LoggerFactory.getLogger(getClass());

    private static final String SOURCE = "bitmex";

    private static final String WEBSOCKET_URL = "wss://www.bitmex.com/realtime?subscribe=instrument";

    private final ScheduledExecutorService executorService = new ScheduledThreadPoolExecutor(1);

    private WebSocketClient webSocketClient;

    @Autowired
    private CoinQuotaCalc coinQuotaCalc;

    @Autowired
    private WebsocketPongHandle websocketPongHandle;

    public void connect() {
        try {
            webSocketClient = new WebSocketClient(new URI(WEBSOCKET_URL)) {
                @Override
                public void onOpen(ServerHandshake serverHandshake) {
                    logger.info("BitmexClient onOpen {} {}", serverHandshake.getHttpStatusMessage(), serverHandshake.getHttpStatus());
                    executorService.scheduleAtFixedRate(new WebsocketPingRunnable(webSocketClient), 5, 5, TimeUnit.SECONDS);
                }

                @Override
                public void onMessage(String message) {
                    logger.info("BitmexClient onMessage {} ", message);
                    //处理pong
                    if ("pong".equals(message)) {
                        websocketPongHandle.pongHandle(webSocketClient);
                        return;
                    }
                    handleData(message);
                }

                @Override
                public void onClose(int code, String message, boolean remote) {
                    logger.info("BitmexClient onClose code {} message {} remote {}", code, message, remote);
                    try {
                        Thread.sleep(5000);
                    }catch (Exception e){
                    }
                    BitmexClient.this.connect();
                }

                @Override
                public void onError(Exception e) {
                    logger.info("BitmexClient onError {} ", e.getMessage());
                }
            };
        } catch (Exception e) {
            e.printStackTrace();
        }
        webSocketClient.setTcpNoDelay(true);
        webSocketClient.connect();
    }

    private void handleData(String message) {
        try {
            Map<String, Object> dataMap = FastJsonUtils.getMap(message);
            String table = (String) dataMap.get("table");
            if (!"quote".equals(table)) {
                return;
            }
            JSONArray jsonArray = (JSONArray) dataMap.get("data");
            JSONObject jsonObject = (JSONObject) jsonArray.get(0);
            String symbol = jsonObject.getString("symbol");
            String productId = BitmexTradePair.getTradePair(symbol);
            if (Utils.isEmpty(productId)) {
                return;
            }
            BigDecimal price = jsonObject.getBigDecimal("bidPrice");
            CoinQuotaBO coinQuotaBO = new CoinQuotaBO();
            coinQuotaBO.setSource(SOURCE);
            coinQuotaBO.setProductName(productId);
            coinQuotaBO.setLastPrice(price);
            coinQuotaBO.setTimestamp(System.currentTimeMillis());
            coinQuotaCalc.calc(coinQuotaBO);
        } catch (Exception e) {

        }
    }
}
