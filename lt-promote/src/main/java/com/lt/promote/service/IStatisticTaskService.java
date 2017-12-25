package com.lt.promote.service;


/**
 * 推广定时任务相关接口
 * @author jingwb
 *
 */
public interface IStatisticTaskService {

	/**
	 * 手动执行佣金结算入口
	 * （更新推广员汇总数据，更新佣金余额，生成结算流水）
	 * @param day 格式如：2017-02-12 表示生成这一天的数据
	 * @throws Exception
	 */
	public void balanceCommisionTask(String day) throws Exception;
	
	/**
	 * 每天0点佣金结算（更新推广员汇总数据，更新佣金余额，生成结算流水）
	 * @throws Exception
	 */	
	public void balanceCommisionTask() throws Exception;
	
	/**
	 * 每天中午12:00生成 明天的推广日报和下线日报
	 * @throws Exception
	 */
	public void initStatisticDayLog() throws Exception;
	
	/**
	 * 手动执行入口
	 * @param day
	 * @throws Exception
	 */
	public void initStatisticDayLog(String day) throws Exception;
}
