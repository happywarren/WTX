package com.lt.business.core.listener;



import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingDeque;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.lt.business.core.service.product.IProductTaskService;
import com.lt.model.dispatcher.enums.RedisQuotaObject;

/**
 * TODO 更新数据持久化执行器
 */
@Component
public class PersistExecutor extends AbstractPersistExecutor implements InitializingBean {
	
	/**
	 * TODO（用一句话描述这个变量表示什么）
	 */
	private static final long serialVersionUID = -2120356500907934133L;
	
	/** 任务执行服务 */
	private final ExecutorService executorService = Executors.newFixedThreadPool(1);
	/** 更新数据队列 */
	private final BlockingQueue<RedisQuotaObject> queue = new LinkedBlockingDeque<RedisQuotaObject>();
		
	@Autowired
	private IProductTaskService productTaskService;
	
	@Override
	public void afterPropertiesSet() throws Exception {
		// TODO Auto-generated method stub
		this.execute();
	}

	@Override
	public ExecutorService getExecutorService() {
		// TODO Auto-generated method stub
		return this.executorService;
	}

	@Override
	public BlockingQueue<RedisQuotaObject> getQueue() {
		// TODO Auto-generated method stub
		return this.queue;
	}

	@Override
	public IProductTaskService getProductTaskService() {
		// TODO Auto-generated method stub
		return this.productTaskService;
	}



}
