package com.lt.quota.core.quota.coin.bitstamp;

import com.lt.quota.core.constant.IConstants;
import com.lt.quota.core.quota.coin.CoinQuotaCalc;
import com.lt.quota.core.quota.coin.WebsocketPingRunnable;
import com.lt.quota.core.quota.coin.bo.CoinQuotaBO;
import com.lt.quota.core.utils.FastJsonUtils;
import com.lt.quota.core.utils.Utils;
import com.pusher.client.Pusher;
import com.pusher.client.channel.Channel;
import com.pusher.client.channel.ChannelEventListener;
import com.pusher.client.connection.ConnectionEventListener;
import com.pusher.client.connection.ConnectionStateChange;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @author mcsong
 * @create 2017-10-17 17:37
 */
@Component
public class BitstampClient {

    private Logger logger = LoggerFactory.getLogger(getClass());

    private Pusher pusher;

    private static final String SOURCE = "bitstamp";

    @Autowired
    private CoinQuotaCalc coinQuotaCalc;

    private void BitstampClient() {

    }

    public void connect() {
        pusher = new Pusher("de504dc5763aeef9ff52");
        pusher.connect(new ConnectionEventListener() {
            @Override
            public void onConnectionStateChange(ConnectionStateChange change) {
                logger.info("BitstampClient onConnectionStateChange currentState {} previousState {} ", change.getCurrentState(), change.getPreviousState());
                if ("CONNECTED".equals(change.getCurrentState().name())) {
                    subscribe();
                }
            }

            @Override
            public void onError(String message, String code, Exception e) {
                logger.info("BitstampClient onError code {} message {}", code, message);
            }
        });
    }

    public void subscribe() {
        for (Map.Entry<String, String> entry : BitstampTradePair.getTradePair().entrySet()) {
            Channel channel = pusher.subscribe(entry.getValue());
            channel.bind("trade", new ChannelEventListener() {
                @Override
                public void onEvent(String channel, String event, String data) {
                    logger.info("BitstampClient onEvent channel: {}  event: {} data: {} ", channel, event, data);
                    handleData(channel, data);
                }

                @Override
                public void onSubscriptionSucceeded(String s) {
                    logger.info("BitstampClient onSubscriptionSucceeded: {} ", s);
                }
            });
        }
    }

    private void handleData(String channel, String message) {
        try {
            Map<String, Object> dataMap = FastJsonUtils.getMap(message);
            logger.info("BitstampClient handleData channel: {} type: {} amount: {} price: {}", channel, dataMap.get("type"), dataMap.get("amount"), dataMap.get("price_str"));
            String productName= BitstampTradePair.getTradePair(channel);
            if (Utils.isEmpty(productName)){
                return;
            }
            CoinQuotaBO coinQuotaBO = new CoinQuotaBO();
            String price = (String) dataMap.get("price_str");
            coinQuotaBO.setSource(SOURCE);
            coinQuotaBO.setProductName(productName);
            coinQuotaBO.setLastPrice(new BigDecimal(price));
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

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}