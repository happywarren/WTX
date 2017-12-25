package com.lt.promote.dao.statistic;

import java.util.List;
import java.util.Map;

import com.lt.model.statistic.StatisticPromoterDayLog;
import com.lt.vo.promote.BrancherVo;

/**
 * 统计推广员日报表数据dao
 * @author jingwb
 *
 */
public interface IStatisticPromoterDayLogDao {
	
	
	/**
	 * 批量添加推广日报表数据
	 * @param list
	 */
	public void insertPromoterDayLogs(List<StatisticPromoterDayLog> list);
	
	/**
	 * 查询该用户一层推广员日报信息
	 * @param userId 下线用户id
	 * @return
	 */
	public StatisticPromoterDayLog selectFirstPromoterDayLog(String userId);
	
	/**
	 * 查询该用户二层推广员日报信息
	 * @param userId 下线用户id
	 * @return
	 */
	public StatisticPromoterDayLog selectSecondPromoterDayLog(String userId);
	
	/***
	 * 判断用户是否是初次交易
	 * @param param
	 * @return
	 */
	public Integer selectOrderCountByUserId(BrancherVo param);
	
	/**
	 * 查询用户是否是初次充值
	 * @param param
	 * @return
	 */
	public Integer selectRechargeCountByUserId(BrancherVo param);
	
	/**
	 * 查询该笔订单的一层佣金 
	 * @param orderId
	 * @return
	 */
	public Double selectFirstCommissionByOrderId(String orderId);
	
	/**
	 * 查询该笔订单的二层佣金
	 * @param orderId
	 * @return
	 */
	public Double selectSecondCommissionByOrderId(String orderId);
	
	/**
	 * 更新推广日报
	 * @param log
	 * @return
	 */
	public Integer updatePromoterDayLog(StatisticPromoterDayLog log);
	
	/**
	 * 查询统计日期的所有推广员日报信息
	 * @param statisticTime
	 * @return
	 */
	public List<StatisticPromoterDayLog> selectPromoterDayLogList(Map<String,String> map);
	
	/**
	 * 
	 * @param log
	 * @return
	 */
	public Integer selectPromoterDayLogCount(StatisticPromoterDayLog log);
	
	/**
	 * 初始化推广日报信息
	 * @param log
	 */
	public void insertPromoterDayLog(StatisticPromoterDayLog log);
	
	/**
	 * 根据用户id查询该用户推广日报信息
	 * @param userId
	 * @return
	 */
	public StatisticPromoterDayLog selectPromoterDayLogByUserId(String userId);
	
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
	
}
