package com.lt.promote.service;



import java.util.Map;

import com.lt.model.statistic.StatisticBrancherDayLog;
import com.lt.model.statistic.StatisticPromoterDayLog;
import com.lt.util.error.LTException;
import com.lt.util.utils.model.Response;
import com.lt.vo.promote.BrancherVo;

/**
 * 推广接口
 * @author jingwb
 *
 */
public interface IPromoteService {

	/**
	 * 激活推广员（成为推广员）
	 * @param userId
	 * @throws Exception
	 */
	public void activatePromoter(String userId) throws Exception;
	
	/***
	 * 统计点击次数
	 * @param count
	 * @param userId
	 */
	public void visitCount(Integer count, String userId);
	
	/**
	 * 将推广员id，ip，tele加入mongo中
	 * @param userId
	 * @param ip
	 * @param tele
	 */
	public void addPromoteLibrary(String userId, String ip,String tele);
	
	/**
	 * 获取用户点击次数
	 * @param userId
	 * @return
	 */
	public int getVisitCount(String userId);
	
	/**
	 * 将userId与推广员建立关系(从tele和ip中获取推广员信息)
	 * @param userId
	 * @param ip
	 * @param tele
	 * @throws Exception
	 */
	public void addPromoteUserMapper(String userId, String ip, String tele) throws LTException;	
	
	/**
	 * 获取当前用户是否为推广员
	 * @param userId
	 * @return
	 * @throws Exception
	 */
	public Response isPromoter(String userId) throws Exception;
	
	/**
	 * 获取推广员信息
	 * @param userId
	 * @return
	 * @throws Exception
	 */
	public Response getPromoterInfo(String userId) throws Exception;
	
	/**
	 * 获取下线详情
	 * @param userId
	 * @return
	 * @throws Exception
	 */
	public Response getBrancherInfos(String userId) throws Exception;
	
	/**
	 * 获取推广链接
	 * @param userId
	 * @return
	 */
	public Response getPromoterUrl(String userId);
	
	
	/**
	 * 处理数据
	 * @param param
	 */
	public void dealData(BrancherVo param);
	
	/**
	 * 查询推广日报
	 * @return
	 */
	public Map<String, StatisticPromoterDayLog> getPromoteInfoForToday();
	
	public Map<String, StatisticPromoterDayLog> getPromoteInfoForToday(String userId);
	
	/**
	 * 查询下线日报
	 * @return
	 */
	public Map<String, StatisticBrancherDayLog> getBrancherInfoForToday(String userId);
}
