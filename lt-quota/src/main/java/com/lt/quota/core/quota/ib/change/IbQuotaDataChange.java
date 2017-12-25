package com.lt.quota.core.quota.ib.change;

import com.lt.quota.core.quota.bean.QuotaBean;
import com.lt.quota.core.quota.ib.bean.IbQuotaData;
import com.lt.quota.core.utils.DateUtils;
import com.lt.quota.core.utils.FastJsonUtils;
import com.lt.quota.core.utils.Utils;

import java.text.DecimalFormat;
import java.util.Date;

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
        if (instance == null) {
            instance = new IbQuotaDataChange();
        }
        return instance;
    }


    public QuotaBean change(String message) {

        try {
            IbQuotaData ibQuotaData = FastJsonUtils.getJson(message, IbQuotaData.class);
            QuotaBean quotaBean = new QuotaBean();
            //时间戳
            long timestamp = ibQuotaData.getTimestamp();
            Date date = new Date(timestamp);
            quotaBean.setTimeStamp(DateUtils.formatDate2Str(date, "yyyy-MM-dd HH:mm:ss.SSS"));
            quotaBean.setProductName(ibQuotaData.getProductCode());

            quotaBean.setAskPrice1("0");
            quotaBean.setBidPrice1("0");
            quotaBean.setAskQty1("0");
            quotaBean.setBidQty1("0");
            if (Utils.isNotEmpty(ibQuotaData.getAskPrice1())) {
                quotaBean.setAskPrice1(format(ibQuotaData.getAskPrice1()));
            }
            if (Utils.isNotEmpty(ibQuotaData.getAskVolume1())) {
                quotaBean.setBidQty1(ibQuotaData.getAskVolume1());
            }
            if (Utils.isNotEmpty(ibQuotaData.getBidPrice1())) {
                quotaBean.setBidPrice1(format(ibQuotaData.getBidPrice1()));
            }
            if (Utils.isNotEmpty(ibQuotaData.getBidVolume1())) {
                quotaBean.setAskQty1(ibQuotaData.getBidVolume1());
            }

            quotaBean.setLastPrice("0");
            quotaBean.setHighPrice("0");
            quotaBean.setLowPrice("0");
            quotaBean.setOpenPrice("0");
            if (Utils.isNotEmpty(ibQuotaData.getLastPrice())) {
                quotaBean.setLastPrice(format(ibQuotaData.getLastPrice()));
            }
            if (Utils.isNotEmpty(ibQuotaData.getHighPrice())) {
                quotaBean.setHighPrice(format(ibQuotaData.getHighPrice()));
            }
            if (Utils.isNotEmpty(ibQuotaData.getLowPrice())) {
                quotaBean.setLowPrice(format(ibQuotaData.getLowPrice()));
            }
            if (Utils.isNotEmpty(ibQuotaData.getOpenPrice())) {
                quotaBean.setOpenPrice(format(ibQuotaData.getOpenPrice()));
            }

            quotaBean.setLimitUpPrice("0");
            quotaBean.setLimitDownPrice("0");
            if (Utils.isNotEmpty(ibQuotaData.getUpLimitPrice())) {
                quotaBean.setLimitUpPrice(format(ibQuotaData.getUpLimitPrice()));
            }
            if (Utils.isNotEmpty(ibQuotaData.getDownLimitPrice())) {
                quotaBean.setLimitDownPrice(format(ibQuotaData.getDownLimitPrice()));
            }

            quotaBean.setAveragePrice("0");
            if (Utils.isNotEmpty(ibQuotaData.getAvgPrice())) {
                quotaBean.setAveragePrice(format(ibQuotaData.getAvgPrice()));
            }

            quotaBean.setChangeValue("0");
            quotaBean.setChangeRate("0");
            if (Utils.isNotEmpty(ibQuotaData.getChangePrice())) {
                quotaBean.setChangeValue(format(ibQuotaData.getChangePrice()));
            }
            if (Utils.isNotEmpty(ibQuotaData.getChangeRate())) {
                quotaBean.setChangeRate(format(ibQuotaData.getChangeRate()));
            }

            quotaBean.setPositionQty("0");
            if (Utils.isNotEmpty(ibQuotaData.getPositionVolume())) {
                quotaBean.setPositionQty(ibQuotaData.getPositionVolume());
            }
            ibQuotaData.setSettlePrice("0");
            if (Utils.isNotEmpty(ibQuotaData.getSettlePrice())) {
                quotaBean.setSettlePrice(format(ibQuotaData.getSettlePrice()));
            }
            quotaBean.setPreClosePrice("0");
            quotaBean.setPreSettlePrice("0");
            if (Utils.isNotEmpty(ibQuotaData.getPreClosePrice())) {
                quotaBean.setPreClosePrice(format(ibQuotaData.getPreClosePrice()));
            }
            if (Utils.isNotEmpty(ibQuotaData.getPreSettlePrice())) {
                quotaBean.setPreSettlePrice(format(ibQuotaData.getPreSettlePrice()));
            }
            quotaBean.setTotalQty(ibQuotaData.getTradeVolume() == null ? 0 : Integer.parseInt(ibQuotaData.getTradeVolume()));

            quotaBean.setLow13Week("0");
            if (Utils.isNotEmpty(ibQuotaData.getLow13Week())) {
                quotaBean.setLow13Week(format(ibQuotaData.getLow13Week()));
            }
            quotaBean.setLow26Week("0");
            if (Utils.isNotEmpty(ibQuotaData.getLow26Week())) {
                quotaBean.setLow26Week(format(ibQuotaData.getLow26Week()));
            }
            quotaBean.setLow52Week("0");
            if (Utils.isNotEmpty(ibQuotaData.getLow52Week())) {
                quotaBean.setLow52Week(format(ibQuotaData.getLow52Week()));
            }
            quotaBean.setHigh13Week("0");
            if (Utils.isNotEmpty(ibQuotaData.getHigh13Week())) {
                quotaBean.setHigh13Week(format(ibQuotaData.getHigh13Week()));
            }
            quotaBean.setHigh26Week("0");
            if (Utils.isNotEmpty(ibQuotaData.getHigh26Week())) {
                quotaBean.setHigh26Week(format(ibQuotaData.getHigh26Week()));
            }
            quotaBean.setHigh52Week("0");
            if (Utils.isNotEmpty(ibQuotaData.getHigh52Week())) {
                quotaBean.setHigh52Week(format(ibQuotaData.getHigh52Week()));
            }

            quotaBean.setMarketCap("0");
            if (Utils.isNotEmpty(ibQuotaData.getMarketCap())) {
                quotaBean.setMarketCap(ibQuotaData.getMarketCap());
            }
            return quotaBean;
        } catch (Exception e) {
            return null;
        }
    }

    private String format(String value) {
        try {
            double doubleValue = Utils.formatDouble(value, 0d);
            DecimalFormat df = new DecimalFormat("#.000");
            return df.format(doubleValue);
        } catch (Exception e) {
        }
        return value;
    }
}
