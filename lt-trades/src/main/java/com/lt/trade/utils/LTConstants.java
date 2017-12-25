package com.lt.trade.utils;

/**
 * 工程相关常量
 *
 * Created by sunch on 2016/11/10.
 */
public class LTConstants {

    // 数据帧类型
    public static final int FRAME_TYPE_QUOTE = 1000;// 行情
    public static final int FRAME_TYPE_HEART_BEAT = 1001;// 心跳
    public static final int FRAME_TYPE_ERROR = 2002;// 异常
    public static final int FRAME_TYPE_ORDER_STATUS = 2003;// 状态变更
    public static final int FRAME_TYPE_ORDER_MATCH  = 2004;// 成交信息
    public static final int FRAME_TYPE_ORDER_INSERT_REQ = 2001;// 报单请求
    public static final int FRAME_TYPE_ORDER_INSERT_RSP = 2012;// 报单响应(委托成功)

    // 报单基本参数
 	/** 买: 'B'(看多) 66 */
    public static final int TRADE_DIRECT_BUY  = 66;
    /** 卖: 'S'(看空) 83 */
    public static final int TRADE_DIRECT_SELL = 83;

    /** 开仓: 'O' 79 */ 
    public static final int TRADE_OFFSET_OPEN  = 79;
    /** 平仓: 'C' 67 */
    public static final int TRADE_OFFSET_CLOSE = 67;
    /** 平今: 'T' 84 */
    public static final int TRADE_OFFSET_CLOSE_TODAY = 84;

    /** 限价: '1' 49 */
    public static final int TRADE_ORDER_TYPE_LIMIT = 49;
    /** 市价: '2' 50 */
    public static final int TRADE_ORDER_TYPE_MARKET  = 50;

    // 其他参数
    public static final int MUTABLE_STOPLOSS_OPEN  = 1;// 移动止损开

    public static final int FUND_TYPE_SCORE = 1;// 积分

}
