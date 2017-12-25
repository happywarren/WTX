package com.lt.business.core.handler;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.lt.business.core.server.ProductTimeCache;
import com.lt.business.core.utils.KLine;
import com.lt.business.core.utils.QuotaUtils;
import com.lt.business.core.utils.TimeSharingplanUtils;
import com.lt.model.dispatcher.enums.RedisQuotaObject;
import com.lt.tradeclient.cmd.factory.TmsCmdFactory;
import com.lt.tradeclient.listener.MarketDataListener;
import com.lt.util.utils.StringTools;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class QuotaListener implements MarketDataListener {

    Logger LOGGER = LoggerFactory.getLogger(getClass());

    private ProductTimeCache productTimeCache;

    public QuotaListener(ProductTimeCache productTimeCache) {
        this.productTimeCache = productTimeCache;
    }

    @Override
    public void onMarketData(String message) {
        JSONObject jsonData = JSON.parseObject(message);
        if (jsonData.getDouble("lastPrice") == null || jsonData.getDouble("bidPrice1") == null
                || jsonData.getDouble("askPrice1") == null) {
            LOGGER.info(" 收到行情数据时 卖一价 买一价 当前价 有一个为null  msg = {} ", message);
            return;
        }
        if (jsonData.getDouble("lastPrice").equals(0.0) || jsonData.getDouble("bidPrice1").equals(0.0)
                || jsonData.getDouble("askPrice1").equals(0.0)) {
            LOGGER.info(" 收到行情数据时 卖一价 买一价 当前价 有一个为0.0  msg = {} ", message);
            return;
        }
        String productName = jsonData.getString("productName");

        boolean isExchangeTradingTime = productTimeCache.getIsExchangeTradingTime(productName);
        if (isExchangeTradingTime) {
            String data = message;
            RedisQuotaObject obj = JSONObject.toJavaObject(jsonData, RedisQuotaObject.class);
            String product = obj.getProductName();
            //内存保存最新的行情数据
            TimeSharingplanUtils.map.put(product, obj);

            //写入.log行情日志
            try {
                QuotaUtils.wirteFileLog(data, obj.getProductName());
            } catch (Exception e) {
                e.printStackTrace();
                LOGGER.error("行情写入log日志异常  msg:{}", data);
            }

            if (StringTools.isNotEmpty(obj.getTimeStamp())) {
                //保存最新的行情数据
                TimeSharingplanUtils.saveNewQuotaDataForRedis(product, obj);
                //生成分时图日志 及 保存到缓存中
                TimeSharingplanUtils.createTimeSharingplan(product, obj, data);
            }
            createKLine(obj);
        }
    }

    /**
     * 处理K线数据
     * @param obj
     */
    private static void createKLine(RedisQuotaObject obj){
        KLine.addKLineBean(obj,1);
        KLine.addKLineBean(obj, 5);
        KLine.addKLineBean(obj, 15);
        KLine.addKLineBean(obj, 30);
        KLine.addKLineBean(obj, 60);
        KLine.addKLineBean(obj, 1440);
        KLine.addKLineBean(obj, 1440*5);
        KLine.addKLineBean(obj, 1440*22);
    }

    @Override
    public void onActive(String message) {
        LOGGER.info("已连接java行情服务器");
        TmsCmdFactory.subAllMarketDataCmd().execute();
    }

    @Override
    public void onError(String message) {

    }

    @Override
    public void onCmd(String message) {

    }

    @Override
    public void onInactive(String s) {

    }


}
