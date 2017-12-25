package com.lt.business.core.dao.statistic;

import java.util.List;
import java.util.Map;

import com.lt.model.statistic.StatisticBrancherDayLog;

/**
 * 统计用户日报表信息dao
 * @author jingwb
 *
 */
public interface IStatisticBrancherDayLogDao {

	/**
	 * 查询一层下线用户交易信息
	 * @param param
	 * @return
	 */
	public List<Map<String,Object>> selectBrancherTradeInfo(Map<String,Object> param);
	
	/**
	 * 查询二层下线用户交易信息
	 * @param param
	 * @return
	 */
	public List<Map<String,Object>> selectSecondBrancherTradeInfo(Map<String,Object> param);
	
	/**
	 * 查询用户充值信息
	 * @param param
	 * @return
	 */
	public List<Map<String,Object>> selectBrancherRechargeInfo(Map<String,Object> param);
	
	/**
	 * 批量插入下线日统计数据
	 * @param list
	 */
	public void insertBrancherDayLogs(List<StatisticBrancherDayLog> list);
}
