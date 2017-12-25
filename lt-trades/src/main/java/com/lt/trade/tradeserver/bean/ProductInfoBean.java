package com.lt.trade.tradeserver.bean;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * 封装商品信息
 *
 * Created by sunch on 2016/11/11.
 */
public class ProductInfoBean {

    private String productName;
    private Integer plate;
    private List<TradeTimeBean> tradeTimeList = new ArrayList<TradeTimeBean>();
    private List<ClearTimeBean> clearTimeList = new ArrayList<ClearTimeBean>();

    public ProductInfoBean() {
    }

    public ProductInfoBean(String productName) {
        this.productName = productName;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public Integer getPlate() {
        return plate;
    }

    public void setPlate(Integer plate) {
        this.plate = plate;
    }

    public List<TradeTimeBean> getTradeTimeList() {
        return tradeTimeList;
    }

    public void setTradeTimeList(List<TradeTimeBean> tradeTimeList) {
        this.tradeTimeList = tradeTimeList;
    }

    public List<ClearTimeBean> getClearTimeList() {
        return clearTimeList;
    }

    public void setClearTimeList(List<ClearTimeBean> clearTimeList) {
        this.clearTimeList = clearTimeList;
    }

    // 添加交易时间
    public void addTradeTimeNode(int startHour, int startMinute, int endHour, int endMinute) {
        tradeTimeList.add(new TradeTimeBean(startHour, startMinute, endHour, endMinute));
    }

    // 检查交易时间
    public boolean checkTradeTime(int currentHour, int currentMinute) {
        for (TradeTimeBean time : tradeTimeList) {
            int ret = currentHour*60 + currentMinute;
            if (ret <= (time.endHour*60 + time.endMinute) && ret >= (time.startHour*60 + time.startMinute)) {
                return true;
            }
        }

        return false;
    }

    // 添加清仓时间
    public void addClearTimeNode(int hour, int minute) {
        DecimalFormat df = new DecimalFormat("00");
        String strHour = df.format(hour);
        String strMinute = df.format(minute);
        String timeKey = strHour + ":" + strMinute;
        for (ClearTimeBean timeBean : clearTimeList) {
            if (timeBean.getTimeKey().equals(timeKey)) {
                return;
            }
        }

        clearTimeList.add(new ClearTimeBean(hour, minute));
    }

    // 交易时间
    private class TradeTimeBean {

        private int startHour;
        private int startMinute;
        private int endHour;
        private int endMinute;

        private TradeTimeBean(int startHour, int startMinute, int endHour, int endMinute) {
            this.startHour = startHour;
            this.startMinute = startMinute;
            this.endHour = endHour;
            this.endMinute = endMinute;
        }

    }

    // 清仓时间
    public class ClearTimeBean {

        private int hour;
        private int minute;

        private ClearTimeBean(int hour, int minute) {
            this.hour = hour;
            this.minute = minute;
        }

        public String getTimeKey() {
            DecimalFormat df = new DecimalFormat("00");
            String strHour = df.format(hour);
            String strMinute = df.format(minute);
            return strHour + ":" + strMinute;
        }

        public int getHour() {
            return hour;
        }

        public void setHour(int hour) {
            this.hour = hour;
        }

        public int getMinute() {
            return minute;
        }

        public void setMinute(int minute) {
            this.minute = minute;
        }
    }

}
