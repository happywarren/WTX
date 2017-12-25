package com.lt.quota.core.quota.coin.bitmex;

import java.util.HashMap;
import java.util.Map;

/**
 * @author mcsong
 * @create 2017-10-18 10:08
 */
public class BitmexTradePair {

    public static final Map<String, String> TRADE_PAIR = new HashMap<>();

    static {
        TRADE_PAIR.put("BTCUSD", "XBTCUSD");
    }

    public static String getBitfinexChannel(String systemTradePair) {
        if (TRADE_PAIR.containsKey(systemTradePair)) {
            return TRADE_PAIR.get(systemTradePair);
        }
        return "";
    }

    public static String getTradePair(String bitstampChannel) {
        for (Map.Entry<String, String> entry : TRADE_PAIR.entrySet()) {
            if (entry.getValue().equals(bitstampChannel)) {
                return entry.getKey();
            }
        }
        return "";
    }
}
