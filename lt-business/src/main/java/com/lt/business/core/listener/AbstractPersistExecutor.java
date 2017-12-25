package com.lt.business.core.listener;



import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSONObject;
import com.lt.business.core.service.product.IProductTaskService;
import com.lt.business.core.utils.KLine;
//import com.lt.business.core.utils.KLineUtils;
import com.lt.business.core.utils.QuotaTimeConfigUtils;
import com.lt.business.core.utils.QuotaUtils;
import com.lt.business.core.utils.TimeSharingplanUtils;
import com.lt.model.dispatcher.enums.RedisQuotaObject;
import com.lt.util.utils.StringTools;


/**
 * TODO 数据持久化执行器
 */
public abstract class AbstractPersistExecutor implements IPersistExecutor {
	
	private static Logger logger = LoggerFactory.getLogger(AbstractPersistExecutor.class);
	
	/**
	 * TODO（用一句话描述这个变量表示什么）
	 */
	private static final long serialVersionUID = 8689415367090181536L;
		
	public boolean put(RedisQuotaObject obj) {
		try {
			getQueue().put(obj);
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
	public void execute(){
		getExecutorService().submit(new Runnable() {			
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
	
	
	/*private void exec(){
		RedisQuotaObject obj = null;
		try {
			obj = getQueue().take();
		} catch (InterruptedException e1) {
			e1.printStackTrace();
			return;
		}
		if(obj == null){
			return;
		}
		//组装K线对象
		double askprice = Double.valueOf(obj.getAskPrice1()); 
		double bidprice = Double.valueOf(obj.getBidPrice1()); 
		if(askprice <= 0 || bidprice <= 0){
			return;
		}
		//获取行情商品名称
		String  product = obj.getProductName();
		logger.debug("redis消息接收 productName:{}", product);
		if(null == product){
			return;
		}
		//获取当前品种是否在开市时间
		if(!QuotaTimeConfigUtils.startFlag(product)){
			return;
		}
		logger.debug("obj.getChangeRate():"+obj.getChangeRate());
		try {
			//涨跌幅刷新市场状态逻辑
			getProductTaskService().changeProductStatus(product, obj.getChangeRate());
		} catch (Exception e) {
			logger.error("刷新市场状态逻辑异常",e);
			return;
		}
		createKLine(obj);
	}*/
	
	/**
	 * 处理K线数据
	 * @param obj
	 */
	private void createKLine(RedisQuotaObject obj){
		//1分钟
//		KLine.addKLineBean(obj, 1);
//		KLine.addKLineBean(obj, 5);
//		KLine.addKLineBean(obj, 15);
//		KLine.addKLineBean(obj, 30);
//		KLine.addKLineBean(obj, 60);
//		KLine.addKLineBean(obj, 1440);
//		KLine.addKLineBean(obj, 1440*5);
//		KLine.addKLineBean(obj, 1440*22);
	}
	
	/**
	 * 
	 * TODO 持久化数据
	 */
	private void persist(){
		
		RedisQuotaObject obj = null;
		
		try {
			obj = getQueue().take();
			if(obj == null){
				return;
			}
			//组装K线对象
			double askprice = Double.valueOf(obj.getAskPrice1()); 
			double bidprice = Double.valueOf(obj.getBidPrice1()); 
			if(askprice <= 0 || bidprice <= 0){
				return;
			}
			//获取行情商品名称
			String  product = obj.getProductName();
			if(null == product){
				return;
			}
			//获取当前品种是否在开市时间
			if(!QuotaTimeConfigUtils.startFlag(product)){
				logger.error("商品{}不在交易时间段内 obj = {}",product,JSONObject.toJSONString(obj));
				return;
			}
			String data = JSONObject.toJSONString(obj);
			//内存保存最新的行情数据
			TimeSharingplanUtils.map.put(product, obj);
			//内存保存每一条数据
//			KLineUtils.saveAllQuotaData(product, obj);
			
			//写入.log行情日志
			QuotaUtils.wirteFileLog(data, obj.getProductName());
			
			if(StringTools.isNotEmpty(obj.getTimeStamp())){
				//保存最新的行情数据
				TimeSharingplanUtils.saveNewQuotaDataForRedis(product, obj);
				//生成分时图日志 及 保存到缓存中
				TimeSharingplanUtils.createTimeSharingplan(product, obj, data);
			
			}
			
		} catch (Exception e) {
			logger.error(e.getMessage(),e);
			e.printStackTrace();			
		}
		
	}

	/** 
	 * 获取 任务执行服务 
	 * @return executorService 
	 */
	public abstract ExecutorService getExecutorService();

	/** 
	 * @return queue 
	 */
	public abstract BlockingQueue<RedisQuotaObject> getQueue();
	
	
	public abstract IProductTaskService getProductTaskService();
	
}
