package com.lt.trade;


import com.alibaba.fastjson.JSONObject;
import com.lt.constant.redis.RedisUtil;
import com.lt.enums.trade.PlateEnum;
import com.lt.trade.order.executor.TradeTimeOut;
import com.lt.trade.tradeserver.bean.ProductInfoBean;
import com.lt.util.utils.StringTools;
import com.lt.vo.product.ProductVo;
import com.lt.vo.product.TimeConfigVO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.BoundHashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.text.DecimalFormat;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

@Component
public class ProductTimeCache {

    private Logger LOGGER = LoggerFactory.getLogger(getClass());

    private final Map<String, Boolean> isExchangeTradingTimeMap = new ConcurrentHashMap<String, Boolean>();//存取每个品种交易状态，true 可交易；  false 不可交易
    private final Map<String, ProductInfoBean> productInfoMap = new ConcurrentHashMap<String, ProductInfoBean>();
    private final Map<String, List<String>> summaryClearTimeMap = new LinkedHashMap<String, List<String>>();

    private final ScheduledExecutorService marketStatusScheduler = new ScheduledThreadPoolExecutor(1);


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
        setExchangeTradingTime(true);
        marketStatusScheduler.scheduleAtFixedRate(new Runnable() {
            @Override
            public void run() {
                setExchangeTradingTime(false);
            }
        }, delayTime, period, TimeUnit.SECONDS);
    }


    // 设置交易时间
    public void setExchangeTradingTime(boolean summaryFlag) {
        LOGGER.info("重新加载商品行情交易清仓时间段");
        try {
            boolean isTradingTime = false;
            // 从redis读取是否周末及假期(周末或假期: beginTime/endTime均为00:00:00)
            BoundHashOperations<String, String, TimeConfigVO> productRedis = redisTemplate.boundHashOps(RedisUtil.PRODUCT_TIME_CONFIG);
            BoundHashOperations<String, String, ProductVo> productInfo = redisTemplate.boundHashOps(RedisUtil.PRODUCT_INFO);
            if (productRedis != null) {
                for (String productName : productRedis.keys()) {
                    TimeConfigVO productTime = productRedis.get(productName);
                    LOGGER.debug("productName = {}", productName);
                    LOGGER.debug("productTime = {}", JSONObject.toJSONString(productTime));
                    if (productTime != null) {
                        //行情时间
                        List<TimeConfigVO.TradeAndQuotaTime> quoteTimeList = productTime.getQuotaTimeList();
                        LOGGER.debug("quoteTimeList = {}", JSONObject.toJSONString(quoteTimeList));

                        if(quoteTimeList == null || quoteTimeList.size() == 0){
                            continue;
                        }
                        if (quoteTimeList.size() > 0) {
                            isTradingTime = !quoteTimeList.get(0).getBeginTime().equals(quoteTimeList.get(0).getEndTime());
                        }
                        if (!isTradingTime) {
                            LOGGER.info("{} redis读取{}行情时间: {} ~ {}...", this.getClass().getSimpleName(), productName, quoteTimeList.get(0).getBeginTime(), quoteTimeList.get(0).getEndTime());
                        }
                        isExchangeTradingTimeMap.put(productName, isTradingTime);
                        ProductVo product = productInfo.get(productName);
                        ProductInfoBean productInfoBean = new ProductInfoBean(productName);
                        //设置内外盘标记
                        if (product != null) {
                            productInfoBean.setPlate(product.getPlate());
                        }

                        //交易时间段
                        List<TimeConfigVO.TradeAndQuotaTime> tradeTimeList = productTime.getTradeTimeList();
                        for (TimeConfigVO.TradeAndQuotaTime tradeAndQuotaTime : tradeTimeList) {
                            // 填充交易时间
                            String startTradeTime = tradeAndQuotaTime.getBeginTime();
                            String endTradeTime = tradeAndQuotaTime.getEndTime();
                            if ((startTradeTime != null) && (endTradeTime != null)) {
                                int startHour = Integer.parseInt(startTradeTime.substring(0, 2));
                                int startMinute = Integer.parseInt(startTradeTime.substring(3, 5));
                                int endHour = Integer.parseInt(endTradeTime.substring(0, 2));
                                int endMinute = Integer.parseInt(endTradeTime.substring(3, 5));
                                if (startHour < endHour) {
                                    productInfoBean.addTradeTimeNode(startHour, startMinute, endHour, endMinute);
                                } else {
                                    productInfoBean.addTradeTimeNode(startHour, startMinute, 23, 59);
                                    productInfoBean.addTradeTimeNode(0, 0, endHour, endMinute);
                                }
                            }
                        }
                        //清仓时间段
                        List<TimeConfigVO.SysSaleTime> sysSaleTimeList = productTime.getSysSaleTimeList();
                        for (TimeConfigVO.SysSaleTime sysSaleTime : sysSaleTimeList) {
                            String clearTime = sysSaleTime.getTime();
                            if (clearTime != null) {
                                int clearHour = Integer.parseInt(clearTime.substring(0, 2));
                                int clearMinute = Integer.parseInt(clearTime.substring(3, 5));
                                productInfoBean.addClearTimeNode(clearHour, clearMinute);
                            }
                        }

                        productInfoMap.put(productName, productInfoBean);
                    } else {
                        LOGGER.error("{} redis读取数据异常, 没有读取到{}对应的时间段...", this.getClass().getSimpleName(), productName);
                    }
                }
            } else {
                LOGGER.error("{} redis读取数据异常, 返回结果为null...", this.getClass().getSimpleName());
            }

            LOGGER.info("假期节假日商品交易状态: {} ", isExchangeTradingTimeMap);

            if (summaryFlag) {
                summaryClearTime();
            }

            try {
                BoundHashOperations<String, String, Long> time = redisTemplate.boundHashOps("redis_refresh_early_warn_time");
                if (!StringTools.isNotEmpty(time.get("time"))) {
                    time.put("time", 10 * 1000L);
                }
                TradeTimeOut.map.put("time", time.get("time"));
                LOGGER.info("刷新订单超时时间成功： time:{}", JSONObject.toJSON(TradeTimeOut.map));
            } catch (Exception e) {
                LOGGER.error("刷新订单超时时间失败", e);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // 聚合清仓时间
    public void summaryClearTime() {
        Iterator<String> iterator;
        DecimalFormat df = new DecimalFormat("00");
        Map<String, List<ProductInfoBean>> tmpClearTimeMap = new TreeMap<>();

        // 自动排序
        iterator = productInfoMap.keySet().iterator();
        while (iterator.hasNext()) {
            String key = iterator.next();
            ProductInfoBean productInfoBean = productInfoMap.get(key);
            List<ProductInfoBean.ClearTimeBean> clearTimeList = productInfoBean.getClearTimeList();
            for (ProductInfoBean.ClearTimeBean timeBean : clearTimeList) {
                String strHour = df.format(timeBean.getHour());
                String strMinute = df.format(timeBean.getMinute());
                String timeKey = strHour + ":" + strMinute;
                if (tmpClearTimeMap.containsKey(timeKey)) {
                    List<ProductInfoBean> productList = tmpClearTimeMap.get(timeKey);
                    productList.add(productInfoBean);
                } else {
                    List<ProductInfoBean> productList = new ArrayList<>();
                    productList.add(productInfoBean);
                    tmpClearTimeMap.put(timeKey, productList);
                }
            }
        }

        //对象锁，防止两个定时线程同时修改
        synchronized (summaryClearTimeMap) {
            // 保持插入顺序
            summaryClearTimeMap.clear();
            iterator = tmpClearTimeMap.keySet().iterator();
            while (iterator.hasNext()) {
                String key = iterator.next();
                List<ProductInfoBean> productList = tmpClearTimeMap.get(key);
                //内盘商品队列
                List<String> productListInner = new ArrayList<>();
                //外盘商品队列
                List<String> productListOuter = new ArrayList<>();
                //差价合约商品队列
                List<String> productListContract = new ArrayList<>();
                for (ProductInfoBean product : productList) {
                    if (PlateEnum.OUTER_PLATE.getValue().equals(product.getPlate())) {
                        productListOuter.add(product.getProductName());
                    } else if (PlateEnum.INNER_PLATE.getValue().equals(product.getPlate())) {
                        productListInner.add(product.getProductName());
                    } else if (PlateEnum.CONTRACT_FOR_DIFFERENCE.getValue().equals(product.getPlate())) {
                        productListContract.add(product.getProductName());
                    }
                }

                if (StringTools.isNotEmpty(productListOuter)) {
                    summaryClearTimeMap.put(key + ":" + PlateEnum.OUTER_PLATE.getValue(), productListOuter);
                }
                if (StringTools.isNotEmpty(productListInner)) {
                    summaryClearTimeMap.put(key + ":" + PlateEnum.INNER_PLATE.getValue(), productListInner);
                }
                if (StringTools.isNotEmpty(productListContract)) {
                    summaryClearTimeMap.put(key + ":" + PlateEnum.CONTRACT_FOR_DIFFERENCE.getValue(), productListContract);
                }
            }
        }
        LOGGER.info("summaryClearTimeMap = {}", JSONObject.toJSONString(summaryClearTimeMap));
    }

    public Boolean getIsExchangeTradingTime(String productName) {
        //假期及节假日
        if (isExchangeTradingTimeMap.containsKey(productName) && !isExchangeTradingTimeMap.get(productName)) {
            LOGGER.info("商品 {} 处于假期或者节假日", productName);
            return false;
        }
        ProductInfoBean productBean = productInfoMap.get(productName);
        if (productBean == null) {
            LOGGER.error("==========商品:{},未配置==========", productName);
            return false;
        }
        Calendar calendar = Calendar.getInstance();
        //交易时间
        if (productBean.checkTradeTime(calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE))) {
            return true;
        }
        LOGGER.info("商品 {} 不处于交易时段", productName);
        return false;
    }

    public Map<String, List<String>> getSummaryClearTimeMap() {
        return Collections.unmodifiableMap(summaryClearTimeMap);
    }

    public List<String> getSummaryClearTime(String key) {
        if (summaryClearTimeMap.containsKey(key)) {
            return summaryClearTimeMap.get(key);
        }
        return new ArrayList<String>();
    }

    public void removeSummaryClearTime(String key) {
        summaryClearTimeMap.remove(key);
    }

    public void addSummaryClearTime(String key, List<String> list) {
        summaryClearTimeMap.put(key, list);
    }


    public Map<String, ProductInfoBean> getProductInfoMap() {
        return Collections.unmodifiableMap(productInfoMap);
    }

    public ProductInfoBean getProductInfoBean(String productName) {
        if (productInfoMap.containsKey(productName)) {
            return productInfoMap.get(productName);
        }
        return new ProductInfoBean();
    }
}
