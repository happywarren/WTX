package com.lt.tms.utils;

/**
 * 作者: 邓玉明
 * 时间: 2017/5/25 下午5:01
 * email:cndym@163.com
 */
public class IdBuild {

    public static String orderId() {
        return new StringBuilder(DateUtils.formatDate2Str("yyyyMMddHHmmssSSS")).append(Utils.random(5)).toString();
    }

    //AA 行情请求唯一 ID
    public static String AAreqId() {
        return "AA" + DateUtils.formatDate2Str("yyyyMMddHHmmss") + Utils.random(5);
    }

    //TORRENT 行情请求唯一 ID
    public static String TTreqId() {
        return Utils.random(3);
    }
}
