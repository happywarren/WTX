package com.lt.model.dispatcher.enums;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 简化的行情数据
 * <p>
 * Created by sunch on 2017/1/17.
 */
public class SimpleQuoteDataArray implements Serializable {

    private List<SimpleQuoteData> quoteDataList = new ArrayList<SimpleQuoteData>();

    public SimpleQuoteDataArray() {
    }

    public void add(RedisQuotaObject rawQuoteData) {
        quoteDataList.add(new SimpleQuoteData(
                rawQuoteData.getProductName(), rawQuoteData.getTimeStamp(), rawQuoteData.getLastPrice(),
                rawQuoteData.getBidPrice1(), rawQuoteData.getBidQty1(), rawQuoteData.getAskPrice1(), rawQuoteData.getAskQty1(),
                rawQuoteData.getChangeRate(), rawQuoteData.getChangeValue()));
    }

    public List<SimpleQuoteData> getQuoteDataList() {
        return quoteDataList;
    }

    public void setQuoteDataList(List<SimpleQuoteData> quoteDataList) {
        this.quoteDataList = quoteDataList;
    }

    private class SimpleQuoteData {

        private String productCode;// 商品
        private String timeStamp;// 时间戳
        private String lastPrice;// 最新价
        private String bidPrice1;// 买价1
        private String bidQty1;// 买量1
        private String askPrice1;// 卖价1
        private String askQty1;// 卖量1
        private String changeRate;// 涨幅
        private String changeValue;// 涨跌值

        public SimpleQuoteData(String productCode, String timeStamp, String lastPrice,
                               String bidPrice1, String bidQty1, String askPrice1, String askQty1,
                               String changeRate, String changeValue) {
            this.productCode = productCode;
            this.timeStamp = timeStamp;
            this.lastPrice = lastPrice;
            this.bidPrice1 = bidPrice1;
            this.bidQty1 = bidQty1;
            this.askPrice1 = askPrice1;
            this.askQty1 = askQty1;
            this.changeRate = changeRate;
            this.changeValue = changeValue;
        }

        public String getProductCode() {
            return productCode;
        }

        public void setProductCode(String productCode) {
            this.productCode = productCode;
        }

        public String getTimeStamp() {
            return timeStamp;
        }

        public void setTimeStamp(String timeStamp) {
            this.timeStamp = timeStamp;
        }

        public String getLastPrice() {
            return lastPrice;
        }

        public void setLastPrice(String lastPrice) {
            this.lastPrice = lastPrice;
        }

        public String getBidPrice1() {
            return bidPrice1;
        }

        public void setBidPrice1(String bidPrice1) {
            this.bidPrice1 = bidPrice1;
        }

        public String getBidQty1() {
            return bidQty1;
        }

        public void setBidQty1(String bidQty1) {
            this.bidQty1 = bidQty1;
        }

        public String getAskPrice1() {
            return askPrice1;
        }

        public void setAskPrice1(String askPrice1) {
            this.askPrice1 = askPrice1;
        }

        public String getAskQty1() {
            return askQty1;
        }

        public void setAskQty1(String askQty1) {
            this.askQty1 = askQty1;
        }

        public String getChangeRate() {
            return changeRate;
        }

        public void setChangeRate(String changeRate) {
            this.changeRate = changeRate;
        }

        public String getChangeValue() {
            return changeValue;
        }

        public void setChangeValue(String changeValue) {
            this.changeValue = changeValue;
        }

    }

}
