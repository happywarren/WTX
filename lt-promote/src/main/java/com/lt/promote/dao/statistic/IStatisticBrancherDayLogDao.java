package com.lt.promote.dao.statistic;

import java.util.List;
import java.util.Map;

import com.lt.model.statistic.StatisticBrancherDayLog;
import com.lt.promote.vo.PromoteVo;

/**
 * 统计用户日报表信息dao
 * @author jingwb
 *
 */
public interface IStatisticBrancherDayLogDao {	
	
	/**
	 * 批量插入下线日统计数据
	 * @param list
	 */
	public void insertBrancherDayLogs(List<StatisticBrancherDayLog> list);
	
	/**
	 * 插入下线日报信息
	 * @param log
	 */
	public void insertBrancherDayLog(StatisticBrancherDayLog log);
	
	/**
	 * 查询下线信息
	 * @param userId
	 * @return
	 */
	public StatisticBrancherDayLog selectBrancherDayLog(String userId);
	
	/**
	 * 更新下线日报
	 * @param log
	 * @return
	 */
	public Integer updateBrancherDayLog(StatisticBrancherDayLog log);
	
	/**
	 * 查询该日期的所有下线数据
	 * @param statisticTime
	 * @return
	 */
	public List<StatisticBrancherDayLog> selectBrancherDayLogList(String statisticTime);
	
	public List<StatisticBrancherDayLog> selectBrancherDayLogListByMap(Map<String,String> map);
	
	public List<PromoteVo> selectBrancherDayLogInfoList(List<PromoteVo> list);
	
	/**
	 * 查询一层下线用户交易信息
	 * @param param
	 * @return
	 */
	public List<Map<String,Object>> selectBrancherTradeInfo(Map<String,Object> param);
	
	/**
	 * 查询用户充值信息
	 * @param param
	 * @return
	 */
	public List<Map<String,Object>> selectBrancherRechargeInfo(Map<String,Object> param);
}
