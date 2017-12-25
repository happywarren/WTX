package com.lt.promote.dao.statistic;

import java.util.List;

import com.lt.model.statistic.StatisticPromoterDayLog;
import com.lt.model.statistic.StatisticPromoterSummaryLog;
import com.lt.promote.vo.PromoteVo;

/**
 * 推广员佣金汇总dao
 * @author jingwb
 *
 */
public interface IStatisticPromoterSummaryLogDao {
	
	/**
	 * 初始化推广员汇总数据
	 * @param userId
	 */
	public void initPromoterSummaryLog(StatisticPromoterSummaryLog log);
	
	/**
	 * 批量更新推广员汇总信息
	 * @param list
	 */
	public void updatePromoterSummaryLogs(List<StatisticPromoterDayLog> list);
	
	/**
	 * 查询推广员汇总数据
	 * @param userId
	 * @return
	 */
	public PromoteVo selectPromoterSummaryInfo(String userId);
	/**
	 * 查询汇总数
	 * @param userId
	 * @return
	 */
	public Integer selectSummaryCount(String userId);
}
