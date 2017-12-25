/*
 * PROJECT NAME: lt-api
 * PACKAGE NAME: com.lt.api.fund
 * FILE    NAME: IFundAccountApiService.java
 * COPYRIGHT: Copyright(c) 2017 LT All Rights Reserved.
 */ 
package com.lt.api.fund;

import java.io.Serializable;

import com.lt.util.error.LTException;
import com.lt.vo.fund.FundOrderVo;

/**
 * TODO 资金扣款、结算、退款业务接口
 * @author XieZhibing
 * @date 2016年11月30日 下午3:42:26
 * @version <b>1.0.0</b>
 */
public interface IFundTradeApiService extends Serializable {
	
	/**
	 * 
	 * TODO 开仓扣款, 需要参数：</br>
	 * FundOrderVo:{
	 * <ul>
	 * <li>productName:商品名称, </li>
	 * <li>orderId:订单ID </li>
	 * <li>userId:用户ID, </li>
	 * <li>holdFund:止损保证金 </li>
	 * <li>deferFund:递延保证金, </li>
	 * <li>actualCounterFee:总手续费 </li>
	 * 
	 * </ul>
	 * }
	 * 
	 * @author XieZhibing
	 * @date 2016年12月7日 下午7:50:52
	 * @param fundOrderVo 开仓扣款参数
	 * @return
	 * @throws LTException
	 */
	public boolean doBuy(FundOrderVo fundOrderVo) throws LTException;	
	
	/**
	 * 
	 * TODO 平仓结算, 需要参数：</br>
	 * FundOrderVo:{
	 * <ul>
	 * <li>productName:商品名称, </li>
	 * <li>orderId: 订单ID </li>
	 * <li>userId: 用户ID, </li>
	 * <li>holdFund: 止损保证金 </li>
	 * <li>deferFund: 递延保证金, </li>
	 * <li>userBenefit: 用户净利润 </li>
	 * </ul>
	 * }
	 * @author XieZhibing
	 * @date 2016年12月7日 下午7:50:52
	 * @param fundOrderVo 平仓结算参数
	 * @return
	 * @throws LTException
	 */
	public void doBalance(FundOrderVo fundOrderVo) throws LTException;
	
	/**
	 * 
	 * TODO 开仓失败退款, 需要参数：</br>
	 * FundOrderVo:{
	 * <ul>
	 * <li>productName:商品名称, </li>
	 * <li>orderId:订单ID </li>
	 * <li>userId:用户ID, </li>
	 * <li>holdFund:止损保证金 </li>
	 * <li>deferFund:递延保证金, </li>
	 * <li>actualCounterFee:总手续费 </li>
	 * </ul>
	 * }
	 * @author XieZhibing
	 * @date 2016年12月7日 下午7:50:52
	 * @param fundOrderVo 开仓失败退款参数
	 * @return
	 * @throws LTException
	 */
	public void doRefund(FundOrderVo fundOrderVo) throws LTException;
	
	/**
	 * 根据订单id查询冻结保障金
	 * @param orderId
	 * @return
	 */
	public Double getHoldFundByOrderId(String orderId,Integer fundType);

}
