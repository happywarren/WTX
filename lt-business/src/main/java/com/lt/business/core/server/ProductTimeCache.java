package com.lt.business.core.server;


import com.alibaba.fastjson.JSONObject;
import com.lt.constant.redis.RedisUtil;
import com.lt.vo.product.TimeConfigVO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.BoundHashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import com.lt.business.core.server.bean.ProductInfoBean;
@Component
public class ProductTimeCache {

    private Logger LOGGER = LoggerFactory.getLogger(ProductTimeCache.class);

    private final Map<String, Boolean> isExchangeTradingTimeMap = new ConcurrentHashMap<String, Boolean>();//存取每个品种交易状态，true 可交易；  false 不可交易

    private final ScheduledExecutorService marketStatusScheduler_ = new ScheduledThreadPoolExecutor(1);

    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    @PostConstruct
    private void ProductTimeCache() {
        startupMarketStatusScheduler();
    }

    // 启动交易时间刷新定时器(10秒刷新)
    private void startupMarketStatusScheduler() {
        int delayTime = 60;
        int period = 60;
        setExchangeTradingTime();
        marketStatusScheduler_.scheduleAtFixedRate(new Runnable() {
            @Override
            public void run() {
                setExchangeTradingTime();
            }
        }, delayTime, period, TimeUnit.SECONDS);
    }


    // 设置交易时间
    private void setExchangeTradingTime() {
        LOGGER.info("重新加载商品行情交易清仓时间段");
        try {
            boolean isTradingTime = false;
            // 从redis读取是否周末及假期(周末或假期: beginTime/endTime均为00:00:00)
            BoundHashOperations<String, String, TimeConfigVO> productRedis = redisTemplate.boundHashOps(RedisUtil.PRODUCT_TIME_CONFIG);
            if (productRedis != null) {
                for (String productName : productRedis.keys()) {
                    TimeConfigVO productTime = productRedis.get(productName);
                    LOGGER.debug("productName = {}", productName);
                    LOGGER.debug("productTime = {}", JSONObject.toJSONString(productTime));
                    if (productTime != null) {
                        //行情时间
                        List<TimeConfigVO.TradeAndQuotaTime> quoteTimeList = productTime.getQuotaTimeList();
                        LOGGER.debug("quoteTimeList = {}", JSONObject.toJSONString(quoteTimeList));
                        if (quoteTimeList.size() > 0) {
                            isTradingTime = !quoteTimeList.get(0).getBeginTime().equals(quoteTimeList.get(0).getEndTime());
                        }
                        if (!isTradingTime) {
                            LOGGER.info("{} redis读取{}行情时间: {} ~ {}...", this.getClass().getSimpleName(), productName, quoteTimeList.get(0).getBeginTime(), quoteTimeList.get(0).getEndTime());
                            //break;
                        }
                        isExchangeTradingTimeMap.put(productName, isTradingTime);

                    } else {
                        LOGGER.error("{} redis读取数据异常, 没有读取到{}对应的时间段...", this.getClass().getSimpleName(), productName);
                    }
                }
            } else {
                LOGGER.error("{} redis读取数据异常, 返回结果为null...", this.getClass().getSimpleName());
            }
            LOGGER.info("{} 交易时间刷新成功, 是否可交易Map: {}", this.getClass().getSimpleName(), JSONObject.toJSON(isExchangeTradingTimeMap));

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public Boolean getIsExchangeTradingTime(String productName) {
        if (isExchangeTradingTimeMap.containsKey(productName)) {
            return isExchangeTradingTimeMap.get(productName);
        }
        return false;
    }
}
