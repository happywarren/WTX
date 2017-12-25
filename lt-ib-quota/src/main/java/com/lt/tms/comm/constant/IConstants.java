package com.lt.tms.comm.constant;


import com.lt.tms.utils.IpUtils;

public interface IConstants {

    /**
     * 心跳
     */
    String REQ_PING = "1000";
    String RES_PING = "1001";

    /**
     * 行情订阅
     */
    String REQ_QUOTA_DATA = "1004";
    String RES_QUOTA_DATA = "1005";

    /**
     * 取消行情订阅
     */
    String REQ_CANCEL_MKT_DATA = "1006";
    String RES_CANCEL_MKT_DATA = "1007";

    /**
     * 交易
     */
    String REQ_TRADE = "1008";
    String RES_TRADE = "1009";

    /**
     * 异常
     */
    String RES_ERROR = "2000";

    final static String SERVER_IP = IpUtils.getRealIp();

    final String QUOTA_IB_SUBSCRIBE_LIST = "quota:ib:subscribe:list:";
    final String QUOTA_IB_SUBSCRIBE = "quota:ib:subscribe:";

    final String QUOTA_PRODUCT_LAST = "redis:product:last:quota:";

}
