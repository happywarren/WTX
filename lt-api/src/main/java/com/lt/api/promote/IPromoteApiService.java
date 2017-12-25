package com.lt.api.promote;

import java.io.Serializable;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import com.lt.model.statistic.StatisticBrancherDayLog;
import com.lt.model.statistic.StatisticPromoterDayLog;
import com.lt.util.error.LTException;
import com.lt.util.utils.model.Response;

/**
 * 推广外部接口
 * @author jingwb
 *
 */
public interface IPromoteApiService extends Serializable{

	/**
	 * 激活成为推广员
	 * @param userId
	 */
	public void activatePromoter(String userId) throws Exception;
	
	/**
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
	 * 佣金提现申请
	 * @param userId
	 * @param amount
	 * @throws Exception
	 */
	public void commisionWidthdrawApply(String userId,Double amount) throws Exception;
	
	/**
	 * 佣金提现审核通过
	 * @param ioId
	 * @throws Exception
	 */
	public void commisionWidthdrawPass(String ioId) throws Exception;
	
	/**
	 * 佣金提现审核拒绝
	 * @param ioId
	 * @throws Exception
	 */
	public void commisionWidthdrawNoPass(String ioId) throws Exception;
	
	/**
	 * 佣金结算定时任务，手动执行入口
	 * @param day
	 * @throws Exception
	 */
	public void balanceCommisionTask(String day) throws Exception;
	
	/**
	 * 判断该用户是否为推广员
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
	 * 统计今天推官员信息
	 * @return
	 */
	public Map<String,StatisticPromoterDayLog> getPromoteInfoForToday(String userId);
	
	/**
	 * 统计今天下线信息
	 * @return
	 */
	public Map<String,StatisticBrancherDayLog> getBrancherInfoForToday(String userId);
	
	/**
	 * 手动执行入口
	 * @param day
	 * @throws Exception
	 */
	public void initStatisticDayLog(String day) throws Exception;
	
	/**
	 * 查询推广日报
	 * @return
	 */
	public Map<String, StatisticPromoterDayLog> getPromoteInfoForToday();
}
