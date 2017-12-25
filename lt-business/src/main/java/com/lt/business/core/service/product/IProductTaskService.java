package com.lt.business.core.service.product;


public interface IProductTaskService {

	/**
	 * 改变商品状态
	 * 1.涨跌幅开仓限制主要功能：当目前行情价格达到开仓限制价格（快接近于交易所规定涨跌幅限制价格）触发定时任务更改商品市场状态（由可买可卖状态更改为不可买卖）；
	 * 2.当商品状态已被系统维护更改，涨跌幅维护则不可更改商品市场状态
	 * 3.开市期间，读取行情价格，当行情价格达到开仓限制价格则触发定时任务更改商品市场状态为不可买卖；
	 * 4.开市期间，读取行情价格，当行情价格未达到涨跌幅开仓限制价格则触发定时任务更改商品市场状态为可买可卖；
	 * @param product
	 * @param changeRate 
	 */
	void changeProductStatus(String product, String changeRate);
	
	/**
	 * 每天早上6点将商品时间配置信息加载到redis中
	 */
	public void loadProTimeCfgToRedis() throws Exception;
	
	/**
	 * 定时任务执行方法
	 * @throws Exception
	 */
	public void loadProTimeCfgToRedisSchedule() throws Exception;
	
	/**
	 * 每分钟刷新市场状态到缓存
	 * @throws Exception
	 */
	public void refreshProMarketStatusForRedis() throws Exception;

	
}
