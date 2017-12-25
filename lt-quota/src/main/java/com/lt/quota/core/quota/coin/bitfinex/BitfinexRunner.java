package com.lt.quota.core.quota.coin.bitfinex;

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
public class BitfinexRunner {

    private Logger logger = LoggerFactory.getLogger(getClass());

    private static final String WEBSOCKET_URL = "wss://api.bitfinex.com/ws/";

    private BitfinexNewClient bitfinexNewClient;

    @Autowired
    private CoinQuotaCalc coinQuotaCalc;

    public void start() {
        try {
            bitfinexNewClient = new BitfinexNewClient(new URI(WEBSOCKET_URL), coinQuotaCalc, this);
            logger.info("启动BitfinexNewClient");
            if (null != bitfinexNewClient) {
                bitfinexNewClient.connect();
            }
        } catch (Exception e) {
        }
    }
}
