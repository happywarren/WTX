package com.lt.trade.settle.service;

import com.lt.model.settle.SettleTypeInfo;

/**   
* 项目名称：lt-trade   
* 类名称：SettleMentAdapter   
* 类描述：结算主功能类   
* 创建人：yuanxin   
* 创建时间：2017年3月19日 下午10:38:53      
*/
public interface SettleMentAdapter {
	
	/**
	 * 结算服务处理类
	 * @param settleTypeInfo 配置的处理内容
	 * @param object    
	 * @return:       void    
	 * @throws 
	 * @author        yuanxin
	 * @Date          2017年3月19日 下午10:49:25
	 */
	public void excute(SettleTypeInfo settleTypeInfo,Object object);
}
