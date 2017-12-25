package com.lt.quota.core.quota.coin.gadx;

import com.lt.quota.core.constant.IConstants;
import com.lt.quota.core.quota.coin.CoinQuotaCalc;
import com.lt.quota.core.quota.coin.bo.CoinQuotaBO;
import com.lt.quota.core.utils.FastJsonUtils;
import com.lt.quota.core.utils.NumberUtil;
import com.lt.quota.core.utils.Utils;
import com.lt.websocket.client.WebSocketClient;
import com.lt.websocket.handshake.ServerHandshake;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;

import java.math.BigDecimal;
import java.net.URI;
import java.util.*;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;


public class GadxNewClient extends WebSocketClient {

    private Logger logger = LoggerFactory.getLogger(getClass());

    private static final String SOURCE = "gadx";

    private final ScheduledExecutorService executorService = new ScheduledThreadPoolExecutor(1);

    private CoinQuotaCalc coinQuotaCalc;

    private GadxRunner gadxRunner;

    public GadxNewClient(URI serverUri) {
        super(serverUri);
    }

    public GadxNewClient(URI serverUri, CoinQuotaCalc coinQuotaCalc, GadxRunner gadxRunner) {
        super(serverUri);
        this.coinQuotaCalc = coinQuotaCalc;
        this.gadxRunner = gadxRunner;
    }

    @Override
    public void onOpen(ServerHandshake handshakedata) {
        logger.info("GadxClient onOpen {} {}", handshakedata.getHttpStatusMessage(), handshakedata.getHttpStatus());
        String requestMessage = subscribe();

        send(requestMessage);

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
        logger.info("GadxClient onClose code: {} message: {}", code, reason);
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        gadxRunner.start();
    }

    @Override
    public void onError(Exception ex) {
        logger.info("GadxClient onError ");
    }

    private String ping() {
        Map<String, Object> requestMap = new HashMap<>();
        requestMap.put("event", "ping");
        return FastJsonUtils.toJson(requestMap);
    }

    public String subscribe() {
        Map<String, Object> requestMap = new HashMap<>();
        requestMap.put("type", "subscribe");
        List<String> productIdList = new ArrayList<>();
        for (Map.Entry<String, String> entry : GadxTradePair.getTradePair().entrySet()) {
            productIdList.add(entry.getValue());
        }
        requestMap.put("product_ids", productIdList);
        List<String> channelList = new ArrayList<>();
        channelList.add("heartbeat");
        channelList.add("ticker");
        requestMap.put("channels", channelList);

        String requestMessage = FastJsonUtils.toJson(requestMap);
        logger.info("GadxClient subscribe message: {} ", requestMessage);
        return requestMessage;
    }


    private void handleData(String message) {
        try {
            Map<String, Object> dataMap = FastJsonUtils.getMap(message);
            //{sequence=4218976729, price=5522.00000000, product_id=BTC-USD, best_bid=5521.99, volume_24h=9412.53873494,
            // low_24h=5522.00000000, high_24h=5687.00000000, volume_30d=307854.04582131, best_ask=5522, type=ticker, open_24h=5669.93000000}
            String productId = (String) dataMap.get("product_id");
            String type = (String) dataMap.get("type");
            if (!"ticker" .equals(type)) {
                return;
            }
            logger.info("GadxClient handleData message: {} ", dataMap);
            String productName = GadxTradePair.getTradePair(productId);
            if (Utils.isEmpty(productName)) {
                return;
            }

            String price = (String) dataMap.get("price");

            CoinQuotaBO coinQuotaBO = new CoinQuotaBO();
            coinQuotaBO.setSource(SOURCE);
            coinQuotaBO.setProductName(productName);
            coinQuotaBO.setLastPrice(NumberUtil.formatBigDecimal(price, new BigDecimal(0)));
            coinQuotaBO.setTimestamp(System.currentTimeMillis());

            if (productName.equals("BTCUSD")) {
                Set<String> btcProductList = coinQuotaCalc.getProductList(IConstants.BTC);
                for (String product : btcProductList) {
                    CoinQuotaBO miniCoinQuotaBO = new CoinQuotaBO();
                    BeanUtils.copyProperties(coinQuotaBO, miniCoinQuotaBO);
                    miniCoinQuotaBO.setProductName(product);
                    coinQuotaCalc.calc(miniCoinQuotaBO);
                }
            }


            if (productName.equals("ETHUSD")) {
                Set<String> ethProductList = coinQuotaCalc.getProductList(IConstants.ETH);
                for (String product : ethProductList) {
                    CoinQuotaBO coinQuotaBO10 = new CoinQuotaBO();
                    BeanUtils.copyProperties(coinQuotaBO, coinQuotaBO10);
                    coinQuotaBO10.setProductName(product);
                    coinQuotaCalc.calc(coinQuotaBO10);
                }
            }

        } catch (Exception e) {

        }
    }
}
