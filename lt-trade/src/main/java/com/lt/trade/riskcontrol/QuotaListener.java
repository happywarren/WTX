package com.lt.trade.riskcontrol;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.lt.trade.ProductTimeCache;
import com.lt.trade.tradeserver.bean.ProductPriceBean;
import com.lt.tradeclient.cmd.factory.TmsCmdFactory;
import com.lt.tradeclient.listener.MarketDataListener;
import com.lt.util.utils.StringTools;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class QuotaListener implements MarketDataListener {

    private static Logger LOGGER = LoggerFactory.getLogger(RiskControlServer.class);

    private ProductTimeCache productTimeCache;

    public QuotaListener(ProductTimeCache productTimeCache) {
        this.productTimeCache = productTimeCache;
    }

    @Override
    public void onMarketData(String message) {
        //LOGGER.info("收到行情: {} ", message);
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
        double lastPrice = StringTools.formatDouble(jsonData.getDouble("lastPrice"), 0.0d);// 当前价
        double bidPrice = StringTools.formatDouble(jsonData.getDouble("bidPrice1"), 0.0d);// 买一价
        double askPrice = StringTools.formatDouble(jsonData.getDouble("askPrice1"), 0.0d);// 卖一价
        double changeValue = StringTools.formatDouble(jsonData.getDouble("changeValue"), 0.0d);//涨跌值
        double changeRate = StringTools.formatDouble(jsonData.getDouble("changeRate"), 0.0d);//涨跌幅
        Integer plate = jsonData.getInteger("plate");
        String timeStamp = jsonData.getString("timeStamp");//接收行情时间
        String productName = jsonData.getString("productName");


        boolean isExchangeTradingTime = productTimeCache.getIsExchangeTradingTime(productName);
        if (isExchangeTradingTime) {
            ProductPriceBean productPrice = new ProductPriceBean();
            productPrice.setProductName(productName);
            productPrice.setLastPrice(lastPrice);
            productPrice.setBidPrice(bidPrice);
            productPrice.setAskPrice(askPrice);
            productPrice.setChangeValue(changeValue);
            productPrice.setChangeRate(changeRate);
            productPrice.setQuotaTime(timeStamp);
            productPrice.setPlate(plate);
            try {
                QuotaOperator.getInstance().setQuotePriceMap(productName, productPrice);
                QuotaOperator.getInstance().setProductPriceQueue(productPrice);
            } catch (Exception e) {
                LOGGER.error("添加行情信息出错, 异常信息: " + e.getMessage());
            }
        } else {
            LOGGER.error("商品: {} 不是交易时间: {} ", productName, isExchangeTradingTime);
        }
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
