package com.lt.trade.settle.service;

import java.util.List;

import com.lt.model.settle.SettleInnerBean;

/**   
* 项目名称：lt-trade   
* 类名称：SettleMentTypeFunc   
* 类描述：  结算类型处理接口 
* 创建人：yuanxin   
* 创建时间：2017年3月15日 下午2:19:19      
*/
public interface SettleMentTypeFunc {
	
	/**
	 * 
	 * @param amt 结算的总金额
	 * @param innerBean 内置结算对象   
	 * @return:       void    
	 * @throws 
	 * @author        yuanxin
	 * @Date          2017年3月16日 下午1:21:27
	 */
	public void settle(Double amt,List<SettleInnerBean> innerBean);
}
