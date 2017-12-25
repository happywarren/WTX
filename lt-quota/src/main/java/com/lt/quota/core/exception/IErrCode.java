package com.lt.quota.core.exception;

/**
 * 作者: 邓玉明
 * 时间: 2017/5/9 上午11:22
 * email:cndym@163.com
 */
public interface IErrCode {

    //系统级
    String ERR_0000 = "0000";//成功
    String ERR_9999 = "9999";//系统错误

    //行情类
    String ERR_1000 = "1000";//行情最后时间超时
    String ERR_1001 = "1001";//行情价小于等于0
    String ERR_1002 = "1002";//无法获取行情

    //订单接收
    String ERR_2000 = "2000";//文件存储失败


    //商品类
    String ERR_3000 = "3000";//商品不存在


}
