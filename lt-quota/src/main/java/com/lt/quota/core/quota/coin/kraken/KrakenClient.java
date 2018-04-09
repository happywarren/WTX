package com.lt.quota.core.quota.coin.kraken;

import com.alibaba.fastjson.JSONArray;
import com.lt.quota.core.quota.coin.VolumeCalc;
import com.lt.quota.core.utils.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;

/**
 * @author mcsong
 * @create 2017-10-19 15:17
 */
@Component
public class KrakenClient {

    private static final Logger logger = LoggerFactory.getLogger(KrakenClient.class);

    private static final String BASE_URL = "https://api.kraken.com/0/public/Ticker?pair=";

    private final ScheduledExecutorService executorService = new ScheduledThreadPoolExecutor(2);

    @Autowired
    private VolumeCalc volumeCalc;

    //@Scheduled(cron = "0/10 * * * * ?")
    @Async
    public void getData() {
        for (Map.Entry<String, String> entry : KrakenTradePair.TRADE_PAIR.entrySet()) {
            execute(BASE_URL, entry.getValue());
        }
    }

    private void execute(String url, String pair) {
        executorService.execute(new Runnable() {
            @Override
            public void run() {
                String result = HttpClientUtil.get(url + pair);
                handleData(result, pair);
            }
        });
    }

    private void handleData(String result, String pair) {
        try {
            logger.info("KrakenClient pair: {} result: {}", pair, result);
            Map<String, Object> dataMap = FastJsonUtils.getMap(result);
            Map<String, Object> resultMap = (Map<String, Object>) dataMap.get("result");
            if (!Utils.isNotEmpty(resultMap)) {
                return;
            }
            Map<String, Object> tickerMap = null;
            if ("XBTUSD".equals(pair)) {
                tickerMap = (Map<String, Object>) resultMap.get("XXBTZUSD");
            }else if ("ETHUSD".equals(pair)) {
                tickerMap = (Map<String, Object>) resultMap.get("XETHZUSD");
            }

            if (!Utils.isNotEmpty(tickerMap)) {
                return;
            }

            JSONArray jsonArray = (JSONArray) tickerMap.get("v");
            String volumeTodayStr = "";
            if (jsonArray.size() == 2) {
                volumeTodayStr = jsonArray.getString(0);
            }
            if (Utils.isEmpty(volumeTodayStr)) {
                return;
            }

            String productCode = KrakenTradePair.getTradePair(pair);
            if (Utils.isEmpty(productCode)) {
                return;
            }
            BigDecimal volume = NumberUtil.formatBigDecimal(new BigDecimal(volumeTodayStr), 2);
            volumeCalc.calc(productCode, volume);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}