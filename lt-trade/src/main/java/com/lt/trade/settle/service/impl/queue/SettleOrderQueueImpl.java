package com.lt.trade.settle.service.impl.queue;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingDeque;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.lt.api.settle.SettleMentApiService;
import com.lt.trade.settle.bean.SettleQueueBean;
import com.lt.trade.settle.service.SettleAbstractQueueService;

/**   
* 项目名称：lt-trade   
* 类名称：SettleOrderQueueImpl   
* 类描述：   
* 创建人：yuanxin   
* 创建时间：2017年4月12日 下午1:35:16      
*/
@Service
public class SettleOrderQueueImpl extends SettleAbstractQueueService implements InitializingBean {

	/** 订单清仓结算队列 */
	private final BlockingQueue<SettleQueueBean> orderEveningUpQueue = new LinkedBlockingDeque<SettleQueueBean>();
	
	/** 任务执行服务 */
	private final ExecutorService executorService = Executors.newFixedThreadPool(1);
	
	@Autowired
	private SettleMentApiService settleMentServiceImpl;
	
	@Override
	public void afterPropertiesSet() throws Exception {
		this.excute();
	}

	@Override
	public BlockingQueue<SettleQueueBean> getQueue() {
		return this.orderEveningUpQueue;
	}

	@Override
	public ExecutorService getExecutorService() {
		return this.executorService;
	}

	@Override
	public SettleMentApiService getSettleServiceImpl() {
		return this.settleMentServiceImpl;
	}

}
