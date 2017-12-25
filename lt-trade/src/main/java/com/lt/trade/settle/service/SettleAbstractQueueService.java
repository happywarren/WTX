package com.lt.trade.settle.service;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.lt.api.settle.SettleMentApiService;
import com.lt.trade.settle.bean.SettleQueueBean;
import com.lt.util.error.LTResponseCode;
import com.lt.util.utils.model.Response;

/**   
* 项目名称：lt-trade   
* 类名称：SettleAbstractQueueService   
* 类描述：   
* 创建人：yuanxin   
* 创建时间：2017年4月12日 上午11:40:58      
*/
public abstract class SettleAbstractQueueService implements SettleQueueService {
	
	private static Logger logger = LoggerFactory.getLogger(SettleAbstractQueueService.class);
	
	/**
	 * 添加队列
	 * @param settleQueue
	 * @return    
	 * @return:       boolean    
	 * @throws 
	 * @author        yuanxin
	 * @Date          2017年4月12日 上午11:46:35
	 */
	public boolean put(SettleQueueBean settleQueue) {
		try {
			getQueue().put(settleQueue);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
		return true;
	}
	
	public void excute(){
		getExecutorService().submit(new Runnable() {
			@Override
			public void run() {
				for (;;) {
					try {
						SettleQueueBean settleQueue = getQueue().take();
						if(settleQueue != null){
							Object object = settleQueue.getObject();
							Integer service = settleQueue.getService();
							
							Map<String, Object> paraMap = new HashMap<String, Object>();
							paraMap.put("service", service);
							paraMap.put("object", object);
							Response response = getSettleServiceImpl().execute(paraMap);

							if (!response.getCode().equals(LTResponseCode.SUCCESS)) {
								put(settleQueue);
							}
						}
					} catch (Exception e) {
						e.printStackTrace();
						logger.error("执行清仓结算异常, 异常信息: " + e.getMessage());
					}
				}
			}
		});
	}
	
	/**
	 * 返回队列
	 * @return    
	 * @return:       BlockingQueue<SettleQueueBean>    
	 * @throws 
	 * @author        yuanxin
	 * @Date          2017年4月12日 上午11:45:05
	 */
	public abstract BlockingQueue<SettleQueueBean> getQueue();
	
	/** 
	 * 获取 任务执行服务
	 * @return executorService 
	 */
	public abstract ExecutorService getExecutorService();

	/**
	 * @return the settleServiceImpl
	 */
	public abstract SettleMentApiService getSettleServiceImpl();
	
}
