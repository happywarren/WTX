package com.lt.business.core.handler;



import java.util.Date;

import com.alibaba.fastjson.JSONObject;
import com.lt.model.dispatcher.enums.RedisQuotaObject;
import com.lt.util.utils.DateTools;
import com.lt.util.utils.StringTools;

/**
 * averagePrice 均价
 * changeRate 涨幅
 * changeValue 涨跌值
 */

public class IbQuotaDataChange {


    private IbQuotaDataChange() {
    }

    private static IbQuotaDataChange instance;

    public static synchronized IbQuotaDataChange getInstance() {
        if (instance == null)
            instance = new IbQuotaDataChange();
        return instance;
    }


    public RedisQuotaObject change(String message) {

        try {
            IbQuotaData ibQuotaData = JSONObject.parseObject(message, IbQuotaData.class);
            RedisQuotaObject RedisQuotaObject = new RedisQuotaObject();
            //时间戳
            long timestamp = ibQuotaData.getTimestamp();
            Date date = new Date(timestamp);
            RedisQuotaObject.setTimeStamp(DateTools.formatDate(date, "yyyy-MM-dd HH:mm:ss.SSS"));
            RedisQuotaObject.setProductName(ibQuotaData.getProductCode());

            RedisQuotaObject.setAskPrice1("0");
            RedisQuotaObject.setAskQty1("0");
            if (StringTools.isNotEmpty(ibQuotaData.getAskPrice1())) {
                RedisQuotaObject.setAskPrice1(ibQuotaData.getAskPrice1());
            }
            if (StringTools.isNotEmpty(ibQuotaData.getAskVolume1())) {
                RedisQuotaObject.setAskQty1(ibQuotaData.getAskVolume1());
            }

            RedisQuotaObject.setBidPrice1("0");
            RedisQuotaObject.setBidQty1("0");
            if (StringTools.isNotEmpty(ibQuotaData.getBidPrice1())) {
                RedisQuotaObject.setBidPrice1(ibQuotaData.getBidPrice1());
            }
            if (StringTools.isNotEmpty(ibQuotaData.getBidVolume1())) {
                RedisQuotaObject.setBidQty1(ibQuotaData.getBidVolume1());
            }

            RedisQuotaObject.setLastPrice("0");
            RedisQuotaObject.setHighPrice("0");
            RedisQuotaObject.setLowPrice("0");
            RedisQuotaObject.setOpenPrice("0");
            if (StringTools.isNotEmpty(ibQuotaData.getLastPrice())){
                RedisQuotaObject.setLastPrice(ibQuotaData.getLastPrice());
            }
            if (StringTools.isNotEmpty(ibQuotaData.getHighPrice())){
                RedisQuotaObject.setHighPrice(ibQuotaData.getHighPrice());
            }
            if (StringTools.isNotEmpty(ibQuotaData.getLowPrice())){
                RedisQuotaObject.setLowPrice(ibQuotaData.getLowPrice());
            }
            if (StringTools.isNotEmpty(ibQuotaData.getOpenPrice())){
                RedisQuotaObject.setOpenPrice(ibQuotaData.getOpenPrice());
            }

            RedisQuotaObject.setLimitUpPrice("0");
            RedisQuotaObject.setLimitDownPrice("0");
            if (StringTools.isNotEmpty(ibQuotaData.getUpLimitPrice())) {
                RedisQuotaObject.setLimitUpPrice(ibQuotaData.getUpLimitPrice());
            }
            if (StringTools.isNotEmpty(ibQuotaData.getDownLimitPrice())) {
                RedisQuotaObject.setLimitDownPrice(ibQuotaData.getDownLimitPrice());
            }

            RedisQuotaObject.setAveragePrice("0");
            if (StringTools.isNotEmpty(ibQuotaData.getAvgPrice())) {
                RedisQuotaObject.setAveragePrice(ibQuotaData.getAvgPrice());
            }

            RedisQuotaObject.setChangeValue("0");
            RedisQuotaObject.setChangeRate("0");
            if (StringTools.isNotEmpty(ibQuotaData.getChangePrice())){
                RedisQuotaObject.setChangeValue(ibQuotaData.getChangePrice());
            }
            if (StringTools.isNotEmpty(ibQuotaData.getChangeRate())){
                RedisQuotaObject.setChangeRate(ibQuotaData.getChangeRate());
            }

            RedisQuotaObject.setPositionQty("0");
            if (StringTools.isNotEmpty(ibQuotaData.getPositionVolume())) {
                RedisQuotaObject.setPositionQty(ibQuotaData.getPositionVolume());
            }
            ibQuotaData.setSettlePrice("0");
            if (StringTools.isNotEmpty(ibQuotaData.getSettlePrice())) {
                RedisQuotaObject.setSettlePrice(ibQuotaData.getSettlePrice());
            }
            RedisQuotaObject.setPreClosePrice("0");
            RedisQuotaObject.setPreSettlePrice("0");
            if (StringTools.isNotEmpty(ibQuotaData.getPreClosePrice())){
                RedisQuotaObject.setPreClosePrice(ibQuotaData.getPreClosePrice());
            }
            if (StringTools.isNotEmpty(ibQuotaData.getPreSettlePrice())){
                RedisQuotaObject.setPreSettlePrice(ibQuotaData.getPreSettlePrice());
            }
            RedisQuotaObject.setTotalQty(ibQuotaData.getTradeVolume() == null ? 0 : Integer.parseInt(ibQuotaData.getTradeVolume()));
            return RedisQuotaObject;
        } catch (Exception e) {
            return null;
        }
    }
}
