package com.lt.quota.core.quota;

import com.alibaba.fastjson.JSONObject;
import com.lt.enums.trade.PlateEnum;
import com.lt.quota.core.quota.bean.QuotaBean;
import com.lt.quota.core.quota.clean.CleanInstance;
import com.lt.tradeclient.cmd.factory.TmsCmdFactory;
import com.lt.tradeclient.listener.MarketDataListener;
import com.lt.tradeclient.tcp.client.bean.QuotaServer;
import com.lt.util.utils.DateTools;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class QuotaListener implements MarketDataListener{

    private static Logger logger = LoggerFactory.getLogger(QuotaServer.class);
    private String [] innerList = {"ni","SR","au","ag","rb"};
    private String [] outerList = {"CL","HSI","DAX","GC","MHI","NQ","SI","HG","YM","ES","BP","AD","EC","JY","ZL","ZM","NG","S"};

    @Override
    public void onMarketData(String msg) {

        JSONObject jsonObject =  (JSONObject) JSONObject.parse(msg);

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
        String productName = quotaBean.getProductName();


        boolean isInnerPlate = false;
        for(String inner:innerList){
            if(productName.contains(inner)){
                isInnerPlate = true;
                break;
            }
        }

        if(isInnerPlate){
            quotaBean.setPlate(PlateEnum.INNER_PLATE.getValue());
        }

        boolean isOuterPlate = false;
        for(String outer : outerList){
            if(productName.contains(outer)){
                isOuterPlate = true;
                break;
            }
        }
        if(isOuterPlate){
            quotaBean.setPlate(PlateEnum.OUTER_PLATE.getValue());
        }
        /*
        if(productName.contains("DAX") ||quotaBean.getPlate() == PlateEnum.INNER_PLATE.getValue()){
            CleanInstance.getInstance().setMarketDataQueue(quotaBean);
        }*/

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
