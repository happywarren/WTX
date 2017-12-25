package com.lt.quota.core.quota.coin;

import com.lt.quota.core.quota.coin.bitfinex.BitfinexRunner;
import com.lt.quota.core.quota.coin.bitstamp.BitstampClient;
import com.lt.quota.core.quota.coin.gadx.GadxRunner;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

/**
 * @author mcsong
 * @create 2017-10-18 9:49
 */
@Component
public class BtcMain {

    @Autowired
    private BitstampClient bitstampClient;

    @Autowired
    private BitfinexRunner bitfinexRunner;

    @Autowired
    private GadxRunner gadxRunner;

    @PostConstruct
    private void BtcMain() {

        bitstampClient.connect();

        bitfinexRunner.start();

        gadxRunner.start();
    }

}
