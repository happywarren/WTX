package com.lt.business.core.dao.statistic;

import java.util.List;
import java.util.Map;

import com.lt.model.statistic.StatisticPromoterDayLog;

/**
 * 统计推广员日报表数据dao
 * @author jingwb
 *
 */
public interface IStatisticPromoterDayLogDao {

	/**
	 * 查询推广员的一层佣金
	 * @param param
	 * @return
	 */
	public List<Map<String,Object>> selectPromoterFirstCommission(Map<String,Object> param);
	
	/**
	 * 查询推广员的二层佣金
	 */
	public List<Map<String,Object>> selectPromoterSecondCommission(Map<String,Object> param);
	
	/**
	 * 查询一层下线交易数据（包含所有品种，而非推广配置的品种，，，需求如此）
	 * @param param
	 * @return
	 */
	public List<Map<String,Object>> selectPromoterFirstTradeInfo(Map<String,Object> param);
	
	/**
	 *查询二层下线交易数据（包含所有品种，而非推广配置的品种，，，需求如此）
	 * @param param
	 * @return
	 */
	public List<Map<String,Object>> selectPromoterSecondTradeInfo(Map<String,Object> param);
	
	/**
	 * 查询所有推广员一层下线充值金额 
	 * @param param
	 * @return
	 */
	public List<Map<String,Object>> selectPromoterFirstRechargeAmount(Map<String,Object> param);
	
	/**
	 * 查询所有推广员二层下线充值金额
	 * @param param
	 * @return
	 */
	public List<Map<String,Object>> selectPromoterSecondRechargeAmount(Map<String,Object> param);
	
	/**
	 *  查询推广员一层下线交易用户数（包含所有品种，而非推广配置的品种，，，需求如此）
	 * @param param
	 * @return
	 */
	public List<Map<String,Object>> selectPromoterFirstTraderCount(Map<String,Object> param);
	
	/**
	 * 查询推广员二层下线交易用户数（包含所有品种，而非推广配置的品种，，，需求如此）
	 * @param param
	 * @return
	 */
	public List<Map<String,Object>> selectPromoterSecondTraderCount(Map<String,Object> param);
	
	/**
	 * 查询推广员一层下线充值用户数
	 * @param param
	 * @return
	 */
	public List<Map<String,Object>> selectPromoterFirstRechargerCount(Map<String,Object> param);
	
	/**
	 * 查询推广员二层下线充值用户数
	 * @param param
	 * @return
	 */
	public List<Map<String,Object>> selectPromoterSecondRechargerCount(Map<String,Object> param);
	
	/**
	 * 查询推广员一层下线注册用户数
	 * @param param
	 * @return
	 */
	public List<Map<String,Object>> selectPromoterFirstRegisterCount(Map<String,Object> param);
	
	/**
	 * 查询推广员二层下线注册用户数
	 * @param param
	 * @return
	 */
	public List<Map<String,Object>> selectPromoterSecondRegisterCount(Map<String,Object> param);
	
	
	/**
	 * 批量添加推广日报表数据
	 * @param list
	 */
	public void insertPromoterDayLogs(List<StatisticPromoterDayLog> list);
}
