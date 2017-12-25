/*
 * PROJECT NAME: lt-trade
 * PACKAGE NAME: com.lt.trade.order.executor
 * FILE    NAME: AbstractPersistExecutor.java
 * COPYRIGHT: Copyright(c) 2017 LT All Rights Reserved.
 */ 
package com.lt.trade.order.executor;

import com.alibaba.fastjson.JSONObject;
import com.lt.model.trade.OrderCashEntrustInfo;
import com.lt.trade.order.cache.OrderCashEntrustInfoCache;
import com.lt.trade.order.service.ICashOrderPersistService;
import com.lt.trade.tradeserver.bean.BaseMatchBean;
import com.lt.trade.tradeserver.bean.FutureErrorBean;
import com.lt.trade.tradeserver.bean.FutureMatchBean;
import com.lt.trade.tradeserver.bean.FutureMatchWrapper;
import com.lt.util.error.LTException;
import com.lt.util.error.LTResponseCode;
import com.lt.util.utils.StringTools;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.*;

/**
 * TODO 数据持久化执行器
 * @author XieZhibing
 * @date 2016年12月11日 下午7:53:08
 * @version <b>1.0.0</b>
 */
public abstract class AbstractPersistExecutor implements IPersistExecutor {
	
	private static Logger logger = LoggerFactory.getLogger(AbstractPersistExecutor.class);
	
	/**
	 * TODO（用一句话描述这个变量表示什么）
	 */
	private static final long serialVersionUID = 8689415367090181536L;
		
	/**
	 * 回执处理进程
	 */
	private final ExecutorService executorService =  new ThreadPoolExecutor(8, 8,
			0L, TimeUnit.MILLISECONDS,
			new LinkedBlockingQueue<Runnable>());;

	/**
	 * 委托单处理中标记
	 */
	protected Set<Object> set = new HashSet<Object>();
	/**
	 * 
	 * 
	 * @author XieZhibing
	 * @date 2016年12月11日 下午8:33:46
	 * @see com.lt.trade.order.executor.IPersistExecutor#put(com.lt.trade.tradeserver.bean.FutureMatchWrapper)
	 * @param futureMatchWrapper
	 * @return
	 */
	@Override
	public boolean put(FutureMatchWrapper futureMatchWrapper) {
		try {
			logger.info("--------加入put:"+JSONObject.toJSONString(futureMatchWrapper));
			getQueue().put(futureMatchWrapper);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
		return true;
	}
	
	/**
	 * 
	 * 
	 * @author XieZhibing
	 * @date 2016年12月11日 下午8:33:49
	 * @see com.lt.trade.order.executor.IPersistExecutor#execute()
	 */
	@Override
	public void execute(){
		executorService.submit(new Runnable() {
			@Override
			public void run() {
				while(true){
					try {					
						persist();						
					} catch (Exception e) {
						logger.error(e.getMessage());
						e.printStackTrace();
					}
				}
			}
		});
	}
	
	/**
	 * 
	 * TODO 持久化数据
	 * @author XieZhibing
	 * @date 2016年12月12日 上午9:43:54
	 */
	protected void persist(){
		
		FutureMatchWrapper matchWrapper = null;
		FutureMatchBean matchBean = null;
		FutureErrorBean errorBean =null;
		try {
			matchWrapper = getQueue().take();
			if(matchWrapper == null){
				return;
			}
			int messageId = matchWrapper.getMessageId();
			BaseMatchBean baseMatchBean = matchWrapper.getBaseMatchBean();
			if(baseMatchBean == null) {
				return;
			}
			
			
			logger.info("------------处理收到回执消息messageId={},回执信息baseMatchBean={}----------------",messageId,JSONObject.toJSONString(baseMatchBean));
			
			switch (messageId) {
			case 2012:
				errorBean = (FutureErrorBean)baseMatchBean;
				final FutureErrorBean bean_2012 = errorBean;
				if(set.contains(errorBean.getPlatformId())){
					OrderCashEntrustInfo orderCashEntrustInfo = OrderCashEntrustInfoCache.get(errorBean.getPlatformId()+"");	
					if(StringTools.isNotEmpty(orderCashEntrustInfo)){
						getQueue().put(matchWrapper);
					}
					break;
				}
				set.add(bean_2012.getPlatformId());
				executorService.execute(new Runnable() {
					@Override
					public void run() {
						try {
							if(bean_2012.getFundType() != 1){
								getCashOrderPersistService().doPersist2012(bean_2012);
							}
						} catch (Exception e) {
							logger.error("baseMatchBean:{}",JSONObject.toJSON(bean_2012));
							logger.error("处理2012回执消息异常：e:{}",e);
						}finally{
							set.remove(bean_2012.getPlatformId());
							logger.info("bean_2012.getPlatformId():{}",bean_2012.getPlatformId());
						}
					}
				});
				
				break;
			case 2002:
				errorBean = (FutureErrorBean)baseMatchBean;
				final FutureErrorBean bean_2002 = errorBean;
				if(set.contains(errorBean.getPlatformId())){
					OrderCashEntrustInfo orderCashEntrustInfo = OrderCashEntrustInfoCache.get(errorBean.getPlatformId()+"");	
					if(StringTools.isNotEmpty(orderCashEntrustInfo)){
						getQueue().put(matchWrapper);
					}
					break;
				}
				
				set.add(bean_2002.getPlatformId());
				executorService.execute(new Runnable() {
					@Override
					public void run() {
						try {
								getCashOrderPersistService().doPersist2002(bean_2002);
						} catch (Exception e) {
							logger.error("baseMatchBean:{}",JSONObject.toJSON(bean_2002));
							logger.error("处理2002回执消息异常：e:{}",e);
						}finally{
							set.remove(bean_2002.getPlatformId());
						}
					}
				});
				
				break;
			case 2003:
				matchBean = (FutureMatchBean)baseMatchBean;
				final FutureMatchBean bean_2003 = matchBean;
				if(set.contains(matchBean.getPlatformId())){
					OrderCashEntrustInfo orderCashEntrustInfo = OrderCashEntrustInfoCache.get(matchBean.getPlatformId()+"");	
					if(StringTools.isNotEmpty(orderCashEntrustInfo)){
						getQueue().put(matchWrapper);
					}
					break;
				}
				set.add(bean_2003.getPlatformId());
				executorService.execute(new Runnable() {
					@Override
					public void run() {
						try {
							if(bean_2003.getFundType() != 1){
								getCashOrderPersistService().doPersist2003(bean_2003);
							}
						} catch (Exception e) {
							logger.error("baseMatchBean:{}",JSONObject.toJSON(bean_2003));
							logger.error("处理2003回执消息异常：e:{}",e);
						}finally{
							set.remove(bean_2003.getPlatformId());
						}
					}
				});
				
				break;
			case 2004:
				matchBean = (FutureMatchBean)baseMatchBean;
				final FutureMatchBean bean_2004 = matchBean;
				logger.info("bean_2004:{}", JSONObject.toJSON(bean_2004));
				if(set.contains(matchBean.getPlatformId())){
					OrderCashEntrustInfo orderCashEntrustInfo = OrderCashEntrustInfoCache.get(matchBean.getPlatformId()+"");	
					if(StringTools.isNotEmpty(orderCashEntrustInfo)){
						getQueue().put(matchWrapper);
					}
					break;
				}
				set.add(bean_2004.getPlatformId());
				executorService.execute(new Runnable() {
					@Override
					public void run() {
						try {
							if(bean_2004.getFundType()!=1){
								getCashOrderPersistService().doPersist2004(bean_2004);
							}
						} catch (LTException e) {
							logger.error("baseMatchBean:{}",JSONObject.toJSON(bean_2004));
							logger.error("处理2004回执消息异常：e:{}",e);
							if(e.getMessage().equals(LTResponseCode.ER400)){
								logger.error("2003还没处理完就在执行2004，此时强制抛出异常，并加入队列尾，等2003全部完成后再执行2004");
								FutureMatchWrapper bean = new FutureMatchWrapper();
								bean.setMessageId(2004);
								bean.setBaseMatchBean(bean_2004);
								try {
									getQueue().put(bean);
								} catch (InterruptedException e1) {
									e1.printStackTrace();
								}
							}
							
						}finally{
							set.remove(bean_2004.getPlatformId());
						}
					}
				});
				
				break;				
			default:
				break;
			}	
		} catch (Exception e) {
			logger.error(e.getMessage());
			e.printStackTrace();			
		}
		
	}

	/** 
	 * 获取 C++返回的消息队列 
	 * @return queue 
	 */
	public abstract BlockingQueue<FutureMatchWrapper> getQueue();

	/** 
	 * 获取 现金订单持久化服务 
	 * @return cashOrderPersistService 
	 */
	public abstract ICashOrderPersistService getCashOrderPersistService() ;
	
}
