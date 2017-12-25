package com.lt.quota.core.quota.coin.bitfinex;

import com.lt.quota.core.constant.IConstants;
import com.lt.quota.core.quota.coin.CoinQuotaCalc;
import com.lt.quota.core.quota.coin.bo.CoinQuotaBO;
import com.lt.quota.core.utils.FastJsonUtils;
import com.lt.websocket.client.WebSocketClient;
import com.lt.websocket.handshake.ServerHandshake;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;

import java.math.BigDecimal;
import java.net.URI;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * 取成交量
 *
 * @author mcsong
 * @create 2017-10-20 17:16
 */

public class BitfinexNewClient extends WebSocketClient {

    private Logger logger = LoggerFactory.getLogger(getClass());

    private static final String SOURCE = "bitfinex";

    private final ScheduledExecutorService executorService = new ScheduledThreadPoolExecutor(1);

    private CoinQuotaCalc coinQuotaCalc;

    private BitfinexRunner bitfinexRunner;

    public BitfinexNewClient(URI serverUri) {
        super(serverUri);
    }

    public BitfinexNewClient(URI serverUri, CoinQuotaCalc coinQuotaCalc, BitfinexRunner bitfinexRunner) {
        super(serverUri);
        this.coinQuotaCalc = coinQuotaCalc;
        this.bitfinexRunner = bitfinexRunner;
    }

    @Override
    public void onOpen(ServerHandshake handshakedata) {
        logger.info("BitfinexClient onOpen {} {}", handshakedata.getHttpStatusMessage(), handshakedata.getHttpStatus());
        for (Map.Entry<String, String> entry : BitfinexTradePair.getTradePair().entrySet()) {
            Map<String, Object> requestMap = new HashMap<>();
            requestMap.put("event", "subscribe");
            requestMap.put("channel", "trades");
            requestMap.put("pair", entry.getValue());
            String requestMessage = FastJsonUtils.toJson(requestMap);
            logger.info("BitfinexClient subscribe trades message: {} ", requestMessage);
            send(requestMessage);
        }

        executorService.scheduleAtFixedRate(new Runnable() {
            final String ping = ping();

            @Override
            public void run() {
                send(ping);
            }
        }, 0, 5, TimeUnit.SECONDS);
    }

    @Override
    public void onMessage(String message) {
        handleData(message);
    }

    @Override
    public void onClose(int code, String reason, boolean remote) {
        logger.info("BitfinexClient onClose code: {} message: {}", code, reason);
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        bitfinexRunner.start();
    }

    @Override
    public void onError(Exception ex) {
        logger.info("BitfinexClient onError ");
    }

    private String ping() {
        Map<String, Object> requestMap = new HashMap<>();
        requestMap.put("event", "ping");
        return FastJsonUtils.toJson(requestMap);
    }

    private void handleData(String message) {
        try {
            if (!message.contains("event")) {
                if (message.contains("tu") || message.contains("te")) {
                    handlePriceData(message);
                }
            }
        } catch (Exception e) {
        }
    }

    /**
     * 交易
     *
     * @param message
     */
    private void handlePriceData(String message) {
        try {
            logger.info("BitfinexClient onMessage {} ", message);
            List<Object> dataList = FastJsonUtils.getArrayJson(message);
            String type = (String) dataList.get(1);
            String pairObj = (String) dataList.get(2);
            BigDecimal price = new BigDecimal(0);
            if ("te" .equals(type)) {
                if (dataList.size() != 6) {
                    return;
                }
                price = new BigDecimal(dataList.get(4) + "");
            } else if ("tu" .equals(type)) {
                if (dataList.size() != 7) {
                    return;
                }
                price = new BigDecimal(dataList.get(5) + "");

            }
            //1104231-BTCUSD
            String[] pairArray = pairObj.split("-");
            if (pairArray.length != 2) {
                return;
            }
            String pair = pairArray[1];
            String productId = BitfinexTradePair.getTradePair(pair);

            CoinQuotaBO coinQuotaBO = new CoinQuotaBO();
            coinQuotaBO.setSource(SOURCE);
            coinQuotaBO.setProductName(productId);
            coinQuotaBO.setLastPrice(price);
            coinQuotaBO.setTimestamp(System.currentTimeMillis());

            if (productId.equals("ETHUSD")) {
                Set<String> ethProductList = coinQuotaCalc.getProductList(IConstants.ETH);
                for (String product : ethProductList) {
                    CoinQuotaBO coinQuotaBO10 = new CoinQuotaBO();
                    BeanUtils.copyProperties(coinQuotaBO, coinQuotaBO10);
                    coinQuotaBO10.setProductName(product);
                    coinQuotaCalc.calc(coinQuotaBO10);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
