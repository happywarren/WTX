package com.lt.quota.core.quota.coin.gadx;

import com.lt.quota.core.quota.coin.CoinQuotaCalc;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.net.URI;

/**
 * @author mcsong
 * @create 2017-11-14 19:16
 */
@Component
public class GadxRunner {

    private Logger logger = LoggerFactory.getLogger(getClass());

    private String WEBSOCKET_URL = "wss://ws-feed.gdax.com/";

    @Autowired
    private CoinQuotaCalc coinQuotaCalc;

    private GadxNewClient gadxNewClient;

    public void start() {
        try {
            gadxNewClient = new GadxNewClient(new URI(WEBSOCKET_URL), coinQuotaCalc, this);
            logger.info("启动GadxNewClient");
            if (null != gadxNewClient) {
                gadxNewClient.connect();
            }
        } catch (Exception e) {
        }
    }
}
