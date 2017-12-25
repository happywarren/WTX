package com.lt.quota.core.constant;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 统计数据
 */
public class SystemDataCount {

    private Logger logger = LoggerFactory.getLogger(getClass());

    /**
     * 商品当前秒行情数
     * product_当前秒
     */
    private final Map<String, SecondQuota> PRODUCT_QUOTA_COUNT = new ConcurrentHashMap<String, SecondQuota>();

    /**
     * 行情统计
     */
    private final Map<String, Long> SOURCE_QUOTA_COUNT = new ConcurrentHashMap<String, Long>();

    /**
     * 商品下发行情数(行情数量 * 下发人数)
     */
    private final Map<String, Long> PRODUCT_QUOTA_SUM = new ConcurrentHashMap<String, Long>();

    /**
     * 商品成功下发行情数
     */
    private final Map<String, Long> PRODUCT_QUOTA_SUCCESS_SUM = new ConcurrentHashMap<String, Long>();

    private static SystemDataCount instance;

    private SystemDataCount() {
    }

    public static synchronized SystemDataCount getInstance() {
        if (instance == null) {
            instance = new SystemDataCount();
        }
        return instance;
    }

    public void sourceQuotaCount(String source) {
        if (SOURCE_QUOTA_COUNT.containsKey(source)) {
            long count = SOURCE_QUOTA_COUNT.get(source);
            SOURCE_QUOTA_COUNT.put(source, count + 1);
        } else {
            SOURCE_QUOTA_COUNT.put(source, 1L);
        }
    }

    public Map<String, Long> getSourceQuotaCount() {
        return SOURCE_QUOTA_COUNT;
    }

    public void productQuotaCount(String product, String dateString) {
        String secondString = dateString.substring(0, dateString.indexOf("."));
        if (PRODUCT_QUOTA_COUNT.containsKey(product)) {
            SecondQuota secondQuota = PRODUCT_QUOTA_COUNT.get(product);
            if (secondQuota.getSecond().equals(secondString)) {
                secondQuota.setCount(secondQuota.getCount() + 1);
            } else {
                secondQuota.setSecond(secondString);
                secondQuota.setCount(1L);
            }
            PRODUCT_QUOTA_COUNT.put(product, secondQuota);
        } else {
            PRODUCT_QUOTA_COUNT.put(product, new SecondQuota(secondString));
        }
    }

    public Map<String, SecondQuota> getProductQuotaCount() {
        return PRODUCT_QUOTA_COUNT;
    }

    public void countQuota(String product) {
        if (PRODUCT_QUOTA_SUM.containsKey(product)) {
            long sum = PRODUCT_QUOTA_SUM.get(product);
            PRODUCT_QUOTA_SUM.put(product, sum + 1);
        } else {
            PRODUCT_QUOTA_SUM.put(product, 1L);
        }
    }

    public void countSuccessQuota(String product) {
        if (PRODUCT_QUOTA_SUCCESS_SUM.containsKey(product)) {
            long sum = PRODUCT_QUOTA_SUCCESS_SUM.get(product);
            PRODUCT_QUOTA_SUCCESS_SUM.put(product, sum + 1);
        } else {
            PRODUCT_QUOTA_SUCCESS_SUM.put(product, 1L);
        }
    }

    public Map<String, Long> quotaSendSum() {
        return PRODUCT_QUOTA_SUM;
    }

    public Map<String, Long> quotaSuccessSum() {
        return PRODUCT_QUOTA_SUCCESS_SUM;
    }

    public class SecondQuota {

        private String second;

        private Long count;

        public SecondQuota(String second) {
            this.second = second;
            this.count = 1L;
        }

        public String getSecond() {
            return second;
        }

        public void setSecond(String second) {
            this.second = second;
        }

        public Long getCount() {
            return count;
        }

        public void setCount(Long count) {
            this.count = count;
        }

    }
}
