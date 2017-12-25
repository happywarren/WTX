package com.lt.tms.trade.constant;


public interface TradeConstants {



    // 报单基本参数
    public static final int TRADE_DIRECT_BUY = 66;// 买: 'B'(看多)
    public static final int TRADE_DIRECT_SELL = 83;// 卖: 'S'(看空)

    public static final int TRADE_OFFSET_OPEN = 79;// 开仓: 'O' 79
    public static final int TRADE_OFFSET_CLOSE = 67;// 平仓: 'C' 67

    public static final int TRADE_ORDER_TYPE_LIMIT = 49;// 限价: '1' 49
    public static final int TRADE_ORDER_TYPE_MARKET = 50;// 市价: '2' 50

}
