/*
 * PROJECT NAME: lt-trade
 * PACKAGE NAME: com.lt.trade.order.executor
 * FILE    NAME: InnerPersistExecutor.java
 * COPYRIGHT: Copyright(c) 2017 LT All Rights Reserved.
 */ 
package com.lt.trade.order.executor;

import com.lt.trade.order.service.ICashOrderPersistService;
import com.lt.trade.tradeserver.bean.FutureMatchWrapper;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingDeque;

/**
 * TODO 内盘订单委托、成交回执结果数据持久化执行器
 * @author XieZhibing
 * @date 2016年12月12日 上午10:02:16
 * @version <b>1.0.0</b>
 */
@Service
public class InnerPersistExecutor extends AbstractPersistExecutor implements InitializingBean {
	
	/**
	 * TODO（用一句话描述这个变量表示什么）
	 */
	private static final long serialVersionUID = -2120356500907934133L;
	/** C++返回的消息队列 */
	private final BlockingQueue<FutureMatchWrapper> queue = new LinkedBlockingDeque<FutureMatchWrapper>();
	/** 现金内盘订单持久化服务 */
	@Autowired
	private ICashOrderPersistService cashOrderInnerPlatePersistServiceImpl;

	/**
	 * 
	 * @author XieZhibing
	 * @date 2016年12月29日 下午3:25:44
	 * @see org.springframework.beans.factory.InitializingBean#afterPropertiesSet()
	 * @throws Exception
	 */
	@Override
	public void afterPropertiesSet() throws Exception {
		// TODO Auto-generated method stub
		this.execute();
	}

	/** 
	 * 
	 * @author XieZhibing
	 * @date 2016年12月12日 上午10:02:16
	 * @return
	 */
	@Override
	public BlockingQueue<FutureMatchWrapper> getQueue() {
		// TODO Auto-generated method stub
		return this.queue;
	}

	/** 
	 * 
	 * @author XieZhibing
	 * @date 2016年12月12日 上午10:02:16
	 * @return
	 */
	@Override
	public ICashOrderPersistService getCashOrderPersistService() {
		// TODO Auto-generated method stub
		return this.cashOrderInnerPlatePersistServiceImpl;
	}

}
