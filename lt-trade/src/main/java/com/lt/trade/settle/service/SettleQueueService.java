package com.lt.trade.settle.service;

import com.lt.trade.settle.bean.SettleQueueBean;

/**   
* 项目名称：lt-trade   
* 类名称：SettleQueueService   
* 类描述：   
* 创建人：yuanxin   
* 创建时间：2017年4月12日 下午1:51:37      
*/
public interface SettleQueueService {
	
	/**
	 * 放入队列
	 * @param settleQueue
	 * @return    
	 * @return:       boolean    
	 * @throws 
	 * @author        yuanxin
	 * @Date          2017年4月12日 下午1:51:55
	 */
	public boolean put(SettleQueueBean settleQueue);
	
	/**
	 * 执行方法
	 *     
	 * @return:       void    
	 * @throws 
	 * @author        yuanxin
	 * @Date          2017年4月12日 下午1:52:22
	 */
	public void excute();
}
