package com.lt.tms.comm.exception;

/**
 * 作者: 邓玉明
 * 时间: 2017/5/9 上午11:22
 * email:cndym@163.com
 */
public interface ITmsErrCode {

    //系统级
    String ERR_0000 = "0000";//成功

    //参数错误
    String ERR_0001 = "0001";

    String ERR_9999 = "9999";//系统错误

    //行情类
    String ERR_1000 = "1000";//合约id为空
    String ERR_1001 = "1001";//商品代码为空
    String ERR_1002 = "1002";//证券类型为空
    String ERR_1003 = "1003";//币种为空
    String ERR_1004 = "1004";//交易所代码为空
    String ERR_1005 = "1005";//symbol为空
    String ERR_1006 = "1006";//商品行情订阅失败
    String ERR_1007 = "1007";//取消商品行情订阅失败


    //订单接收
    String ERR_2000 = "2000";//交易id为空
    String ERR_2001 = "2001";//商品不可交易
    String ERR_2002 = "2002";//交易数量不正确



    //商品类
    String ERR_3000="3000";//商品不存在



}
