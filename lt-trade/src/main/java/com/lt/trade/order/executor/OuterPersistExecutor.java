/*
 * PROJECT NAME: lt-trade
 * PACKAGE NAME: com.lt.trade.order.executor
 * FILE    NAME: OuterPersistExecutor.java
 * COPYRIGHT: Copyright(c) 2017 LT All Rights Reserved.
 */ 
package com.lt.trade.order.executor;

import com.lt.trade.order.service.ICashOrderPersistService;
import com.lt.trade.tradeserver.bean.FutureMatchWrapper;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * TODO 外盘订单委托、成交回执结果数据持久化执行器
 * @author XieZhibing
 * @date 2016年12月11日 下午7:53:08
 * @version <b>1.0.0</b>
 */
@Service
public class OuterPersistExecutor extends AbstractPersistExecutor implements InitializingBean {
	
	/**
	 * TODO（用一句话描述这个变量表示什么）
	 */
	private static final long serialVersionUID = 4055658553011319857L;

	/** C++返回的消息队列 */
	private final BlockingQueue<FutureMatchWrapper> queue = new LinkedBlockingQueue<FutureMatchWrapper>();
	/** 现金外盘订单持久化服务 */
	@Autowired
	private ICashOrderPersistService cashOrderOuterPlatePersistServiceImpl;

	/** 
	 * 
	 * @author XieZhibing
	 * @date 2016年12月29日 下午3:24:51
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
	 * @date 2016年12月12日 上午9:59:23
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
	 * @date 2016年12月12日 上午9:59:23
	 * @return
	 */
	@Override
	public ICashOrderPersistService getCashOrderPersistService() {
		// TODO Auto-generated method stub
		return this.cashOrderOuterPlatePersistServiceImpl;
	}

}
