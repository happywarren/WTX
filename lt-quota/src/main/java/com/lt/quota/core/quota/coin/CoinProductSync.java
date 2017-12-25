package com.lt.quota.core.quota.coin;

import com.lt.quota.core.constant.IConstants;
import com.lt.quota.core.service.IQuotaCoreConfigService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.*;

/**
 * @author mcsong
 * @create 2017-11-20 17:25
 */
@Component
public class CoinProductSync {

    @Autowired
    private IQuotaCoreConfigService quotaCoreConfigService;

    private final Map<String, Set<String>> coinMap = new HashMap<>();

    @PostConstruct
    private void init() {
        coinMap.put(IConstants.BTC, new HashSet<>());
        coinMap.put(IConstants.ETH, new HashSet<>());
    }

    @Scheduled(cron = "* * 0/1 * * ?")
    public void getData() {
        List<Map<String, Object>> coinProductList = quotaCoreConfigService.getCoinProductList();
        for (Map<String, Object> dataMap : coinProductList) {
            String productCode = dataMap.get("productCode") + "";
            if (productCode.startsWith(IConstants.BTC)) {
                Set<String> productList = coinMap.get(IConstants.BTC);
                productList.add(productCode);
                coinMap.put(IConstants.BTC, productList);
            }
            if (productCode.startsWith(IConstants.ETH)) {
                Set<String> productList = coinMap.get(IConstants.ETH);
                productList.add(productCode);
                coinMap.put(IConstants.ETH, productList);
            }
        }
    }



    public Set<String> getProductList(String key){
        return coinMap.get(key);
    }
}
