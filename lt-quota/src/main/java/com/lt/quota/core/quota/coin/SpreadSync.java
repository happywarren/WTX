package com.lt.quota.core.quota.coin;

import com.alibaba.fastjson.JSONObject;
import com.lt.quota.core.comm.redis.lock.DistributedLock;
import com.lt.quota.core.comm.redis.lock.RedisLock;
import com.lt.quota.core.constant.IConstants;
import com.lt.quota.core.quota.bean.QuotaBean;
import com.lt.quota.core.utils.FastJsonUtils;
import com.lt.quota.core.utils.NumberUtil;
import com.lt.quota.core.utils.Utils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.math.BigDecimal;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

/**
 * 点差同步
 *
 * @author mcsong
 * @create 2017-10-19 11:46
 */
@Component
public class SpreadSync {

    private Logger logger = LoggerFactory.getLogger(getClass());

    private static final String PRODUCT_INFO = "PRODUCT_INFO";

    private static final String SYS_CONFIG = "SYS_CONFIG";

    /**
     * 点差
     */
    private static final Map<String, BigDecimal> SPREAD_MAP = new HashMap<>();

    /**
     * 点差倍数
     */
    private static final Map<String, Double> SPREAD_MULTIPLE_MAP = new HashMap<>();
    private static final String SPREAD_MULTIPLE = "quota:spread:multiple";

    /**
     * 设置新的一天
     */
    private static final Map<String, Boolean> NEW_DAY_MAP = new HashMap<>();
    private static final String NEW_DAY_RUN = "quota:new:day:run";

    /**
     * 行情源 比例
     */
    private static final Map<String, Double> QUOTA_SOURCE_RATE_MAP = new HashMap<>();

    private static final String QUOTA_SOURCE_RATE = "quota:source:rate";

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Autowired
    private RedisTemplate redisTemplate;

    @Autowired
    private DistributedLock distributedLock;

  //  @PostConstruct
    private void initData() {
        /*
        Map<String, String> dataMap = redisTemplate.opsForHash().entries(PRODUCT_INFO);
        for (Map.Entry<String, String> entry : dataMap.entrySet()) {
            String productCode = entry.getKey().replace("\"", "");
            JSONObject jsonObject = JSONObject.parseObject(entry.getValue());
            logger.info("商品: {} 配置: {}", productCode, jsonObject.toJSONString());
            BigDecimal spread = jsonObject.getBigDecimal("spread");
            if (null != spread) {
                SPREAD_MAP.put(productCode, spread);
                NEW_DAY_MAP.put(productCode, false);
            }
        }
        syncSpreadMultiple();
        syncSourceRate();*/
    }

    public void syncSpreadMultiple() {
        Map<String, Object> dataMap = redisTemplate.opsForHash().entries(SPREAD_MULTIPLE);
        if (!Utils.isNotEmpty(dataMap)) {
            return;
        }

        for (Map.Entry<String, Object> entry : dataMap.entrySet()) {
            String key = entry.getKey();
            String multiple = (String) entry.getValue();
            SPREAD_MULTIPLE_MAP.put(key, Utils.formatDouble(multiple, 1D));
            logger.info("同步商品: {} 点差倍数: {}", key, multiple);
        }
    }

    public void syncSourceRate() {
        Map<String, Object> dataMap = redisTemplate.opsForHash().entries(QUOTA_SOURCE_RATE);
        if (!Utils.isNotEmpty(dataMap)) {
            return;
        }

        for (Map.Entry<String, Object> entry : dataMap.entrySet()) {
            String key = entry.getKey();
            String rate = (String) entry.getValue();
            QUOTA_SOURCE_RATE_MAP.put(key, Utils.formatDouble(rate, 0.5D));
            logger.info("同步行情源: {} : 比例 {}", key, rate);
        }
    }

    public void syncData(String productId) {
        StringBuilder hashKey = new StringBuilder();
        hashKey.append("\"").append(productId).append("\"");
        String value = (String) redisTemplate.opsForHash().get(PRODUCT_INFO, hashKey.toString());
        JSONObject jsonObject = JSONObject.parseObject(value);
        BigDecimal spread = jsonObject.getBigDecimal("spread");
        logger.info("同步商品: {} 点差: {}", productId, spread);
        if (null != spread) {
            SPREAD_MAP.put(productId, spread);
        }
    }

    public BigDecimal getSpread(String productId) {
        if (SPREAD_MULTIPLE_MAP.size() == 0) {
            syncSpreadMultiple();
        }
        BigDecimal addPrice = new BigDecimal(5);
        if (SPREAD_MAP.containsKey(productId)) {
            addPrice = SPREAD_MAP.get(productId);
        }
        Double multiple = SPREAD_MULTIPLE_MAP.get(productId);
        if (null == multiple) {
            multiple = 1D;
        }
        BigDecimal totalSpread = NumberUtil.mul(addPrice, new BigDecimal(multiple));
        return totalSpread;
    }

    /**
     * 点差倍数
     */
    @Scheduled(cron = "30 0 5,6 * * ?")
    private void calcMultiple() {
        StringBuilder hashKey = new StringBuilder();
        hashKey.append("\"").append("winter_summer_change").append("\"");
        String value = (String) redisTemplate.opsForHash().get(SYS_CONFIG, hashKey.toString());
        value = value.replace("\"", "");
        Calendar calendar = Calendar.getInstance();
        int hour = calendar.get(Calendar.HOUR_OF_DAY);//当前时间

        if ("1".equals(value) && hour == 5) {
            logger.info("夏令时5点执行");
            execute();
        } else if ("2".equals(value) && hour == 6) {
            logger.info("冬令时6点执行");
            execute();
        }
    }

    private void execute() {
        SPREAD_MULTIPLE_MAP.clear();
        RedisLock redisLock = new RedisLock(NEW_DAY_RUN, "run");
        if (distributedLock.tryLock(redisLock, 100, 60000)) {
            logger.info("定时计算点差倍数");
            for (Map.Entry<String, BigDecimal> entry : SPREAD_MAP.entrySet()) {
                String productName = entry.getKey();
                calcMultiple(productName);
                NEW_DAY_MAP.put(productName, true);
            }
        }
    }

    private void calcMultiple(String productName) {
        String key = IConstants.REDIS_PRODUCT_LAST_QUOTA + productName;
        String quotaMessage = stringRedisTemplate.opsForValue().get(key);
        Double multiple = 1D;
        if (Utils.isNotEmpty(quotaMessage)) {
            QuotaBean quotaBean = FastJsonUtils.getJson(quotaMessage, QuotaBean.class);

            BigDecimal openPrice = NumberUtil.formatBigDecimal(quotaBean.getOpenPrice(), new BigDecimal(0));
            BigDecimal lowPrice = NumberUtil.formatBigDecimal(quotaBean.getLowPrice(), new BigDecimal(0));
            BigDecimal highPrice = NumberUtil.formatBigDecimal(quotaBean.getHighPrice(), new BigDecimal(0));

            BigDecimal diffPrice = NumberUtil.sub(highPrice, lowPrice);
            BigDecimal rate = NumberUtil.div(diffPrice, openPrice, 3);
            logger.info("商品: {} 最高价: {} 最低价: {} 开盘价: {} 波动: {}", productName, highPrice, lowPrice, openPrice, rate);

            if (NumberUtil.isGreater(rate, new BigDecimal(0.2))) {
                multiple = 3D;
            } else if (NumberUtil.isGreater(rate, new BigDecimal(0.1))) {
                multiple = 2.3;
            } else if (NumberUtil.isGreater(rate, new BigDecimal(0.05))) {
                multiple = 1.6;
            }
        }
        SPREAD_MULTIPLE_MAP.put(productName, multiple);
        redisTemplate.opsForHash().put(SPREAD_MULTIPLE, productName, String.valueOf(multiple));
        logger.info("定时计算商品: {} 点差倍数: {}", productName, multiple);
    }

    public boolean isNewDay(String productName) {
        if (NEW_DAY_MAP.containsKey(productName)) {
            return NEW_DAY_MAP.get(productName);
        }
        return false;
    }

    public void setNewDay(String productName, boolean newDay) {
        NEW_DAY_MAP.put(productName, newDay);
    }

    public Double getRate(String source) {
        if (QUOTA_SOURCE_RATE_MAP.containsKey(source)) {
            return QUOTA_SOURCE_RATE_MAP.get(source);
        }
        return 0.5D;
    }
}
