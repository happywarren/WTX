package com.lt.trade.defer.dao;

import java.util.List;

import com.lt.trade.defer.bean.NextPeroidOrderInfo;

/**   
* 项目名称：lt-trade   
* 类名称：SettleDao   
* 类描述：结算查询类   
* 创建人：yuanxin   
* 创建时间：2017年3月16日 下午3:15:39      
*/
public interface DeferDao {
	

	/**
	 * 根据短类型查询所有的持仓中的积分订单数据
	 * @param list
	 * @return    
	 * @return:       List<NextPeroidOrderInfo>    
	 * @throws 
	 * @author        yuanxin
	 * @Date          2017年2月16日 下午7:49:07
	 */
	public List<NextPeroidOrderInfo> findAllCashOrdersByCode(List<String> list);
	


}
