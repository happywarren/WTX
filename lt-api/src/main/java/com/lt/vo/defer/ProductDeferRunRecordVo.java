package com.lt.vo.defer;

import java.util.Date;


public class ProductDeferRunRecordVo implements java.io.Serializable{

    /**
     * 结算短类型
     */
    private String futureType;
    /**
     * 下一个交易时间段 格式为 ：品种1,品种2-时间;品种 - 时间 或 时间
     */
    private String nextOnePeriod;

    /**
     * 日期 yyyyMMdd
     */
    private String day;

    /**
     * 执行时间
     */
    private Date createTime;

    public ProductDeferRunRecordVo() {
    }

    public String getFutureType() {
        return futureType;
    }

    public void setFutureType(String futureType) {
        this.futureType = futureType;
    }

    public String getNextOnePeriod() {
        return nextOnePeriod;
    }

    public void setNextOnePeriod(String nextOnePeriod) {
        this.nextOnePeriod = nextOnePeriod;
    }

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }
}
