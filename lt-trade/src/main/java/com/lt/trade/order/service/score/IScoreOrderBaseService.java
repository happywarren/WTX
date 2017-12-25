/*
 * PROJECT NAME: lt-trade
 * PACKAGE NAME: com.lt.trade.order.service
 * FILE    NAME: IScoreOrderService.java
 * COPYRIGHT: Copyright(c) 2017 LT All Rights Reserved.
 */ 
package com.lt.trade.order.service.score;

import com.lt.model.trade.OrderScoreEntrustInfo;
import com.lt.trade.tradeserver.bean.FutureMatchBean;

/**
 * TODO 现金订单基础业务服务接口
 * @author XieZhibing
 * @date 2016年12月12日 下午1:40:37
 * @version <b>1.0.0</b>
 */
public interface IScoreOrderBaseService {

	/**
	 * 
	 * TODO 委托失败，更新失败原因
	 * @author XieZhibing
	 * @date 2016年12月12日 下午7:03:43
	 * @param entrustRecord
	 * @param errorId 错误码
	 */
	public void doPersist2002(OrderScoreEntrustInfo entrustRecord, int errorId);
	
	/**
	 * 
	 * TODO 保存现金订单基本数据、现金订单持仓数量数据、委托单数据
	 * @author XieZhibing
	 * @date 2016年12月12日 下午2:18:31
	 * @param entrustRecord
	 */
	public void doPersist2012(OrderScoreEntrustInfo entrustRecord);
	
	/**
	 * 
	 * TODO 更新持仓数量、更新交易所委托单号、添加成交记录
	 * @author XieZhibing
	 * @date 2016年12月12日 下午5:54:09
	 * @param matchDateTime 成交时间
	 * @param matchVol 本次成交数量
	 * @param matchPrice 成交价
	 * @param matchTotal 已成交数量
	 * @param orderTotal 委托数量
	 * @param sysMatchId 期货公司成交ID
	 * @param entrustRecord 委托单
	 */
	/*public void doPersist2003(String matchDateTime, Integer matchVol,
			Double matchPrice, Integer matchTotal, Integer orderTotal,
			String sysMatchId, OrderScoreEntrustInfo entrustRecord);*/
	
	/**
	 * 接收成功过程数据
	 * @param matchBean
	 * @param entrustRecord
	 */
	public void doPersist2003(FutureMatchBean matchBean, OrderScoreEntrustInfo entrustRecord);

	/**
	 * 
	 * TODO 更新持仓数量、买入(卖出)成功数量、买入(卖出)失败数量、买入(卖出)均价、买入(卖出)时间、添加成交记录
	 * @author XieZhibing
	 * @date 2016年12月12日 下午5:13:38
	 * @param matchDateTime 成交时间
	 * @param matchVol 本次成交数量
	 * @param matchPrice 成交价
	 * @param matchTotal 已成交数量
	 * @param orderTotal 委托数量
	 * @param sysMatchId 期货公司成交ID
	 * @param entrustRecord 买入(卖出)委托单
	 */
	public void doPersist2004(FutureMatchBean matchBean, OrderScoreEntrustInfo entrustRecord);

	public void doPersist(FutureMatchBean matchBean,
			OrderScoreEntrustInfo scoreEntrustRecord);

}
