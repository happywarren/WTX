package com.lt.promote.dao.statistic;



import java.util.List;

import com.lt.model.statistic.StatisticBrancherDayLog;
import com.lt.model.statistic.StatisticBrancherSummaryLog;
import com.lt.promote.vo.PromoteVo;

/**
 * 统计下线汇总数据dao
 * @author jingwb
 *
 */
public interface IStatisticBrancherSummaryLogDao {

	/**
	 * 初始化下线汇总数据
	 * @param userId
	 */
	public void initBrancherSummaryLog(StatisticBrancherSummaryLog log);
	
	/**
	 * 批量初始化下线汇总数据
	 * @param list
	 */
	public void initBrancherSummaryLogs(List<StatisticBrancherSummaryLog> list);
	
	
	/**
	 * 修改下线汇总数据
	 * @param list
	 */
	public void updateBrancherSummaryLogs(List<StatisticBrancherDayLog> list);
	
	/**
	 * 查询下线详情
	 * @param userId
	 * @return
	 */
	public List<PromoteVo> selectBrancherInfos(String userId);
}
