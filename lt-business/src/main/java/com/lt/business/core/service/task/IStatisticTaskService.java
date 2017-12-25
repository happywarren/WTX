package com.lt.business.core.service.task;


/**
 * 推广定时任务相关接口
 * @author jingwb
 *
 */
public interface IStatisticTaskService {

	/**
	 * 每天0点10分处理下线日报表数据（统计每个用户的前一天数据，并初始化当天数据）
	 * day  日期，如：2017-02-06，表示对这天的数据做统计，
	 * 注：如果day为空 ，则默认统计前一天的数据,代表定时任务触发
	 * 	    如果day不为空，表示人为触发接口
	 * @throws Exception
	 */
	public void dealBrancherDayLog(String day) throws Exception;
	
	/**
	 * 手动执行佣金结算入口
	 * （生成当天推广员日报表数据，更新推广员汇总数据，更新佣金余额，生成结算流水）
	 * @param day 格式如：2017-02-12 表示生成这一天的数据
	 * @throws Exception
	 */
	public void balanceCommisionTask(String day) throws Exception;
	
	/**
	 * 每天0点10分佣金结算（生成前一天推广员日报表数据，更新推广员汇总数据，更新佣金余额，生成结算流水）
	 * @throws Exception
	 */	
	public void balanceCommisionTask() throws Exception;
}
