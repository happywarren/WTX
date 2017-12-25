package com.lt.quota.core.quota.coin;

import com.lt.quota.core.constant.IConstants;
import com.lt.quota.core.quota.WarningUtil;
import com.lt.quota.core.quota.bean.QuotaBean;
import com.lt.quota.core.quota.clean.CleanInstance;
import com.lt.quota.core.quota.coin.bo.CoinQuotaBO;
import com.lt.quota.core.utils.DateUtils;
import com.lt.quota.core.utils.FastJsonUtils;
import com.lt.quota.core.utils.NumberUtil;
import com.lt.quota.core.utils.Utils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.*;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;

/**
 * 币行情计算
 *
 * @author mcsong
 * @create 2017-10-18 10:37
 */
@Component
public class CoinQuotaCalc {

    private static final Logger logger = LoggerFactory.getLogger(CoinQuotaCalc.class);

    private final ScheduledExecutorService executorService = new ScheduledThreadPoolExecutor(2);

    private static final Map<String, Map<String, CoinQuotaBO>> PRICE_MAP = new HashMap<>();

    /**
     * 价差历史
     */
    private static final String QUOTA_DIFF_HISTORY = "quota:diff:history:";

    /**
     * 价差平均值
     */
    private static final String QUOTA_DIFF_AVG = "quota:diff:avg:";


    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Autowired
    private RedisTemplate redisTemplate;

    @Autowired
    private SpreadSync spreadSync;

    @Autowired
    private VolumeCalc volumeCalc;

    @Autowired
    private CoinProductSync coinProductSync;

    public void calc(CoinQuotaBO param) {

        String source = param.getSource();
        String productName = param.getProductName();
        BigDecimal lastPrice = param.getLastPrice();
        if (NumberUtil.isLessOrEqual(lastPrice, new BigDecimal(0))) {
            return;
        }

        WarningUtil.warn(source);

        Map<String, CoinQuotaBO> dataMap = PRICE_MAP.get(productName);

        if (null == dataMap) {
            dataMap = new HashMap<>();
            CoinQuotaBO coinQuotaBO = new CoinQuotaBO();
            coinQuotaBO.setLastPrice(lastPrice);
            coinQuotaBO.setTimestamp(System.currentTimeMillis());
            dataMap.put(source, coinQuotaBO);
            PRICE_MAP.put(productName, dataMap);
        } else {
            CoinQuotaBO coinQuotaBO = dataMap.get(source);
            if (null == coinQuotaBO) {
                coinQuotaBO = new CoinQuotaBO();
            }

            //检测行情
            boolean validate = checkPrice(param);
            if (!validate) {
                return;
            }
            coinQuotaBO.setLastPrice(lastPrice);
            coinQuotaBO.setTimestamp(System.currentTimeMillis());
            dataMap.put(source, coinQuotaBO);
            PRICE_MAP.put(productName, dataMap);
        }

        int sourceSize = 0;
        //买一价
        BigDecimal bidPrice = new BigDecimal(0);
        for (Map.Entry<String, CoinQuotaBO> entry : dataMap.entrySet()) {
            String key = entry.getKey();
            CoinQuotaBO coinQuotaBO = entry.getValue();
            long timestamp = coinQuotaBO.getTimestamp();
            if (!isValidateTimestamp(timestamp)) {
                logger.info("商品: {} 源: {} 最后行情时间: {} 超时", productName, key, timestamp);
                continue;
            }
            sourceSize++;
            Double rate = spreadSync.getRate(key);
            BigDecimal sourcePrice = NumberUtil.mul(coinQuotaBO.getLastPrice(), new BigDecimal(rate));
            bidPrice = NumberUtil.add(bidPrice, sourcePrice);
        }

        //priceMap 入Redis


        QuotaBean quotaBean = new QuotaBean();
        quotaBean.setProductName(param.getProductName());
        quotaBean.setSource("system");


        //只有当前数据源可用
        if (sourceSize == 1) {
            //取价差平均值
            StringBuilder avgKey = new StringBuilder();
            avgKey.append(QUOTA_DIFF_AVG).append(productName);

            String avgPriceStr = (String) redisTemplate.opsForHash().get(avgKey.toString(), source);
            BigDecimal avgPrice = new BigDecimal(0);
            if (Utils.isNotEmpty(avgPriceStr)) {
                avgPrice = NumberUtil.formatBigDecimal(avgPriceStr, avgPrice);
            }
            bidPrice = NumberUtil.sub(lastPrice, avgPrice);
        }

        if (NumberUtil.isLessOrEqual(bidPrice, new BigDecimal(0))) {
            bidPrice = lastPrice;
        }

        quotaBean.setBidPrice1(NumberUtil.toStr(bidPrice));

        //卖一价 加点差
        //点差
        BigDecimal spread = spreadSync.getSpread(productName);
        quotaBean.setAskPrice1(NumberUtil.toStr(NumberUtil.add(bidPrice, spread)));
        quotaBean.setLastPrice(NumberUtil.toStr(bidPrice));
        quotaBean.setTimeStamp(DateUtils.formatDate2Str("yyyy-MM-dd HH:mm:ss.SSS"));
        quotaBean.setPlate(2);

        logger.info("CoinQuotaCalc calc product: {} lastPrice: {} spread: {} askPrice1: {} bidPrice: {} ", productName, lastPrice, spread, quotaBean.getAskPrice1(), quotaBean.getBidPrice1());

        fillPrice(quotaBean);

        WarningUtil.warnQuota(productName);

        CleanInstance.getInstance().setMarketDataQueue(quotaBean);

        executorService.execute(new Runnable() {
            @Override
            public void run() {
                calcDiffPrice(productName, source);
            }
        });
    }

    private void fillPrice(QuotaBean param) {
        String productName = param.getProductName();
        BigDecimal lastPrice = NumberUtil.formatBigDecimal(param.getBidPrice1(), new BigDecimal(0));

        String key = IConstants.REDIS_PRODUCT_LAST_QUOTA + productName;
        String quotaMessage = stringRedisTemplate.opsForValue().get(key);
        QuotaBean quotaBean = FastJsonUtils.getJson(quotaMessage, QuotaBean.class);

        BigDecimal cacheLastPrice = lastPrice;
        BigDecimal lowPrice = new BigDecimal(999999999);
        BigDecimal highPrice = new BigDecimal(0);

        BigDecimal openPrice = lastPrice;
        BigDecimal closePrice = lastPrice;

        if (Utils.isNotEmpty(quotaBean)) {
            cacheLastPrice = NumberUtil.formatBigDecimal(quotaBean.getLastPrice(), lastPrice);
            lowPrice = NumberUtil.formatBigDecimal(quotaBean.getLowPrice(), new BigDecimal(999999999));
            highPrice = NumberUtil.formatBigDecimal(quotaBean.getHighPrice(), new BigDecimal(0));

            openPrice = NumberUtil.formatBigDecimal(quotaBean.getOpenPrice(), lastPrice);
            closePrice = NumberUtil.formatBigDecimal(quotaBean.getPreClosePrice(), lastPrice);
        }

        param.setOpenPrice(NumberUtil.toStr(openPrice));
        param.setPreClosePrice(NumberUtil.toStr(closePrice));

        param.setLowPrice(lowPrice(lastPrice, lowPrice));
        param.setHighPrice(highPrice(lastPrice, highPrice));

        BigDecimal changeValue = NumberUtil.sub(lastPrice, closePrice);
        param.setChangeValue(NumberUtil.toStr(changeValue));
        param.setChangeRate(NumberUtil.toStr(NumberUtil.div(NumberUtil.mul(changeValue, new BigDecimal(100)), closePrice)));
        Integer askQty = Utils.intRandom(11);
        Integer bidQty = Utils.intRandom(11);
        if (askQty <= 0){
            askQty = 1;
        }
        if (bidQty <= 0){
            bidQty = 1;
        }
        param.setAskQty1(String.valueOf(askQty));
        param.setBidQty1(String.valueOf(bidQty));
        param.setAveragePrice("0");
        param.setLimitDownPrice("0");
        param.setLimitUpPrice("0");
        param.setPositionQty("0");
        param.setPreSettlePrice("0");
        param.setSettlePrice("0");
        param.setMarketCap("0");
        param.setLow13Week("0");
        param.setHigh13Week("0");
        param.setLow26Week("0");
        param.setHigh26Week("0");
        param.setLow52Week("0");
        param.setHigh52Week("0");

        param.setTotalQty(volumeCalc.getVolume(productName));

        if (spreadSync.isNewDay(productName)) {
            param.setOpenPrice(NumberUtil.toStr(lastPrice));
            param.setPreClosePrice(NumberUtil.toStr(cacheLastPrice));
            param.setLowPrice(NumberUtil.toStr(lastPrice));
            param.setHighPrice(NumberUtil.toStr(lastPrice));
            param.setChangeValue("0");
            param.setChangeRate("0.0");

            logger.info("商品: {} 昨收: {} 今开: {} ", productName, param.getPreClosePrice(), param.getOpenPrice());
            //今开 昨收
            if (!NumberUtil.equals(lastPrice, cacheLastPrice)) {
                stringRedisTemplate.opsForValue().set(IConstants.REDIS_PRODUCT_LAST_QUOTA + productName, FastJsonUtils.toJson(param));
                spreadSync.setNewDay(productName, false);
            }
        }
    }

    /**
     * 最低
     */
    private String lowPrice(BigDecimal lastPrice, BigDecimal cachePrice) {
        if (NumberUtil.isLess(lastPrice, cachePrice)) {
            return NumberUtil.toStr(lastPrice);
        }
        return NumberUtil.toStr(cachePrice);
    }

    /**
     * 最高
     */
    private String highPrice(BigDecimal lastPrice, BigDecimal cachePrice) {
        if (NumberUtil.isGreater(lastPrice, cachePrice)) {
            return NumberUtil.toStr(lastPrice);
        }
        return NumberUtil.toStr(cachePrice);
    }

    /**
     * 超过2分钟
     *
     * @param timestamp
     * @return
     */
    private boolean isValidateTimestamp(Long timestamp) {
        long curTimestamp = System.currentTimeMillis();
        if (curTimestamp - timestamp > 120 * 1000) {
            return false;
        }
        return true;
    }

    /**
     * 检测价格 并保存价差历史
     *
     * @param param
     * @return
     */
    private boolean checkPrice(CoinQuotaBO param) {
        boolean validate = true;
        String source = param.getSource();
        String productName = param.getProductName();
        BigDecimal lastPrice = param.getLastPrice();

        try {
            StringBuilder historyKey = new StringBuilder();
            historyKey.append(QUOTA_DIFF_HISTORY).append(productName).append(":").append(source);

            StringBuilder avgKey = new StringBuilder();
            avgKey.append(QUOTA_DIFF_AVG).append(productName);

            Map<String, CoinQuotaBO> dataMap = PRICE_MAP.get(productName);

            //同源 价格相同 抛弃
//            CoinQuotaBO sourceCoinQuotaBO = dataMap.get(source);
//            if (Utils.isNotEmpty(sourceCoinQuotaBO)) {
//                if (NumberUtil.equals(lastPrice, sourceCoinQuotaBO.getLastPrice())) {
//                    logger.info("CoinQuotaCalc lastSource: {} lastPrice: {} 相同", source, lastPrice);
//                    return false;
//                }
//            }

            //价差平均值
            String avgPriceStr = (String) redisTemplate.opsForHash().get(avgKey.toString(), source);
            BigDecimal avgPrice = NumberUtil.formatBigDecimal(avgPriceStr, new BigDecimal(0)).abs();
            for (Map.Entry<String, CoinQuotaBO> entry : dataMap.entrySet()) {
                String key = entry.getKey();
                CoinQuotaBO coinQuotaBO = entry.getValue();
                if (source.equals(key)) {
                    continue;
                }
                String hashKey = String.valueOf(System.currentTimeMillis());

                if (NumberUtil.isGreater(lastPrice, new BigDecimal(0))
                        && NumberUtil.isGreater(coinQuotaBO.getLastPrice(), new BigDecimal(0))) {

                    //计算askPrice差
                    BigDecimal priceDiff = NumberUtil.sub(lastPrice, coinQuotaBO.getLastPrice());

                    BigDecimal absPriceDiff = priceDiff.abs();

                    BigDecimal avgPrice10 = NumberUtil.mul(avgPrice, new BigDecimal(10));

                    BigDecimal maxPriceDiff = avgPrice10;
                    if (NumberUtil.isLess(maxPriceDiff, new BigDecimal(100))) {
                        maxPriceDiff = new BigDecimal(100);
                    }

                    if (NumberUtil.isGreater(absPriceDiff, maxPriceDiff)) {
                        logger.error("CoinQuotaCalc lastSource: {} lastPrice: {} targetSource: {} targetPrice: {} 价差大于最大价差: {} ", source, lastPrice, key, coinQuotaBO.getLastPrice(), maxPriceDiff);
                        StringBuilder content = new StringBuilder();
                        content.append("行情源 ").append(source).append(" 最新价 ").append(lastPrice);
                        content.append("与行情源 ").append(key).append(" 最新价 ").append(coinQuotaBO.getLastPrice()).append(" 价差大于最大价差 ").append(maxPriceDiff);
                        WarningUtil.warnQuotaPrice(content.toString());
                        validate = false;
                        break;
                    }
                    redisTemplate.opsForHash().put(historyKey.toString(), hashKey, NumberUtil.toStr(priceDiff));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return validate;
    }

    /**
     * 计算价差平均值
     */
    private void calcDiffPrice(String productName, String source) {
        try {
            StringBuilder historyKey = new StringBuilder();
            historyKey.append(QUOTA_DIFF_HISTORY).append(productName).append(":").append(source);

            StringBuilder avgKey = new StringBuilder();
            avgKey.append(QUOTA_DIFF_AVG).append(productName);

            //取askPrice
            Map<String, String> priceMap = redisTemplate.opsForHash().entries(historyKey.toString());

            int priceSize = 0;
            BigDecimal totalPrice = new BigDecimal(0);
            List<String> expireList = new ArrayList<>();
            long curTimestamp = System.currentTimeMillis();
            for (Map.Entry<String, String> entry : priceMap.entrySet()) {
                String key = entry.getKey();
                BigDecimal price = new BigDecimal(entry.getValue());
                long timestamp = Utils.formatLong(key, 0);
                if (curTimestamp - timestamp > 60 * 60 * 1000) {
                    expireList.add(key);
                    continue;
                }
                priceSize++;
                totalPrice = NumberUtil.add(totalPrice, price);
            }

            if (priceSize != 0) {
                //计算价差平均值
                BigDecimal price = NumberUtil.div(totalPrice, new BigDecimal(priceSize), 2);

                redisTemplate.opsForHash().put(avgKey.toString(), source, NumberUtil.toStr(price));
            }

            if (expireList.size() > 0) {
                for (String hashKey : expireList) {
                    redisTemplate.opsForHash().delete(historyKey.toString(), hashKey);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public Set<String> getProductList(String key){
        return coinProductSync.getProductList(key);
    }
}