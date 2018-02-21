package com.lt.quota.core.quota;

import com.alibaba.fastjson.JSONObject;
import com.lt.quota.core.quota.bean.QuotaBean;
import com.lt.quota.core.quota.clean.CleanInstance;
import com.lt.tradeclient.cmd.factory.TmsCmdFactory;
import com.lt.tradeclient.listener.MarketDataListener;
import com.lt.tradeclient.tcp.client.bean.QuotaServer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.SimpleDateFormat;
import java.util.Date;

public class QuotaListener implements MarketDataListener{

    private static Logger logger = LoggerFactory.getLogger(QuotaServer.class);

    @Override
    public void onMarketData(String msg) {

        System.out.println("msg:"+msg);

        JSONObject jsonObject =  (JSONObject) JSONObject.parse(msg);
        logger.info("msg"+jsonObject);
        QuotaBean quotaBean = new QuotaBean();
        quotaBean.setAskPrice1(jsonObject.getString("askPrice1"));
        quotaBean.setAskQty1(jsonObject.getString("askQty1"));
        quotaBean.setAveragePrice(jsonObject.getString("averagePrice"));
        quotaBean.setBidPrice1(jsonObject.getString("bidPrice1"));
        quotaBean.setBidQty1(jsonObject.getString("bidQty1"));
        quotaBean.setChangeRate(jsonObject.getString("changeRate"));
        quotaBean.setChangeValue(jsonObject.getString("changeValue"));
        quotaBean.setHighPrice(jsonObject.getString("highPrice"));
        quotaBean.setLastPrice(jsonObject.getString("lastPrice"));
        quotaBean.setLimitDownPrice(jsonObject.getString("limitDownPrice"));
        quotaBean.setLimitUpPrice(jsonObject.getString("limitUpPrice"));
        quotaBean.setLowPrice(jsonObject.getString("lowPrice"));
        quotaBean.setOpenPrice(jsonObject.getString("openPrice"));
        quotaBean.setPositionQty(jsonObject.getString("positionQty"));
        quotaBean.setPreClosePrice(jsonObject.getString("preClosePrice"));
        quotaBean.setPreSettlePrice(jsonObject.getString("preSettlePrice"));
        quotaBean.setProductName(jsonObject.getString("productName"));
        quotaBean.setSettlePrice(jsonObject.getString("settlePrice"));
        long timeStamp = jsonObject.getLong("timeStamp");
        Date d = new Date(timeStamp);
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
        String time = format.format(d);
        quotaBean.setTimeStamp(time);
        quotaBean.setTotalQty(jsonObject.getInteger("totalQty"));
        quotaBean.setSource(jsonObject.getString("source"));
        quotaBean.setPlate(jsonObject.getInteger("plate"));
        quotaBean.setLow13Week(jsonObject.getString("low13Week"));
        quotaBean.setHigh13Week(jsonObject.getString("high13Week"));
        quotaBean.setLow26Week(jsonObject.getString("low26Week"));
        quotaBean.setHigh26Week(jsonObject.getString("high26Week"));
        quotaBean.setLow52Week(jsonObject.getString("low52Week"));
        quotaBean.setHigh52Week(jsonObject.getString("high52Week"));
        CleanInstance.getInstance().setMarketDataQueue(quotaBean);
    }

    @Override
    public void onActive(String s) {
        logger.info("已连接java行情服务器");
        TmsCmdFactory.subAllMarketDataCmd().execute();
    }

    @Override
    public void onError(String s) {

    }

    @Override
    public void onCmd(String s) {

    }

    @Override
    public void onInactive(String s) {

    }
}
