/*
 * PROJECT NAME: lt-api
 * PACKAGE NAME: com.lt.vo.trade
 * FILE    NAME: TradeDirectVo.java
 * COPYRIGHT: Copyright(c) 2017 LT All Rights Reserved.
 */ 
package com.lt.vo.trade;

import java.io.Serializable;

import com.lt.enums.trade.TradeDirectionEnum;
import com.lt.enums.trade.TradeTypeEnum;

/**
 * TODO 交易方向
 * @author XieZhibing
 * @date 2016年12月12日 下午9:43:27
 * @version <b>1.0.0</b>
 */
public class TradeDirectVo implements Serializable{

	private static final long serialVersionUID = -4884350800476269894L;
	// 报单基本参数
 	/** 'B'(看多) 66 */
    public static final int TRADE_DIRECT_BUY  = 66;
    /** 'S'(看空) 83 */
    public static final int TRADE_DIRECT_SELL = 83; 

    /** 开仓: 'O' 79 */ 
    public static final int TRADE_OFFSET_OPEN  = 79;
    /** 平仓: 'C' 67 */
    public static final int TRADE_OFFSET_CLOSE = 67; 
    /** 市价: '1' 49 */
    public static final int TRADE_ORDER_TYPE_MARKET = 49; 
    /** 限价: '2' 50 */
    public static final int TRADE_ORDER_TYPE_LIMIT  = 50; 
    
	/** 交易方向(转换后): 买: 'B' 66; 卖: 'S' 83 */
	private int tradeDirect;
	/** 开平标志(转换后): 平仓: 'C' 67; 开仓: 'O' 79 */
	private int tradeOffset;
	
	/**
	 * 
	 * @param tradeDirection交易方向
	 * @param tradeType交易类型
	 */
	public TradeDirectVo(int tradeDirection,int tradeType) {
		//买多处理: 买入开仓 (BO)
		if(tradeDirection == TradeDirectionEnum.DIRECTION_UP.getValue() && tradeType == TradeTypeEnum.BUY.getValue()){
			//买入
			this.tradeDirect = TRADE_DIRECT_BUY;
			//开仓
			this.tradeOffset = TRADE_OFFSET_OPEN;
		}
		// 卖多: 卖出平仓 (SC)
		else if(tradeDirection == TradeDirectionEnum.DIRECTION_UP.getValue() && tradeType == TradeTypeEnum.SELL.getValue()){
			//卖出
			this.tradeDirect = TRADE_DIRECT_SELL;
			//平仓
			this.tradeOffset = TRADE_OFFSET_CLOSE;
		}
		//买空处理: 卖出开仓 (SO) 
		else if(tradeDirection == TradeDirectionEnum.DIRECTION_DOWN.getValue() && tradeType == TradeTypeEnum.BUY.getValue()){
			//卖出
			this.tradeDirect = TRADE_DIRECT_SELL;
			//开仓
			this.tradeOffset = TRADE_OFFSET_OPEN;
		}
		//卖空: 买入平仓 (BC) 	
		else if(tradeDirection == TradeDirectionEnum.DIRECTION_DOWN.getValue() && tradeType == TradeTypeEnum.SELL.getValue()){
			//买入
			this.tradeDirect = TRADE_DIRECT_BUY;
			//平仓
			this.tradeOffset = TRADE_OFFSET_CLOSE;
		}
				
	}
	
	/** 
	 * 获取 交易方向(转换后): 买: 'B' 66; 卖: 'S' 83 
	 * @return tradeDirect 
	 */
	public int getTradeDirect() {
		return tradeDirect;
	}
	/** 
	 * 设置 交易方向(转换后): 买: 'B' 66; 卖: 'S' 83 
	 * @param tradeDirect 交易方向(转换后): 买: 'B' 66; 卖: 'S' 83 
	 */
	public void setTradeDirect(int tradeDirect) {
		this.tradeDirect = tradeDirect;
	}
	/** 
	 * 获取 开平标志(转换后): 平仓: 'C' 67; 开仓: 'O' 79 
	 * @return tradeOffset 
	 */
	public int getTradeOffset() {
		return tradeOffset;
	}
	/** 
	 * 设置 开平标志(转换后): 平仓: 'C' 67; 开仓: 'O' 79 
	 * @param tradeOffset 开平标志(转换后): 平仓: 'C' 67; 开仓: 'O' 79 
	 */
	public void setTradeOffset(int tradeOffset) {
		this.tradeOffset = tradeOffset;
	}
	
	
}
