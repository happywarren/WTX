package com.lt.api.user;

import java.util.List;

import com.github.pagehelper.Page;
import com.lt.model.user.log.NotificationLog;
import com.lt.model.user.log.OrderLossProfitDefLog;
import com.lt.model.user.log.UserOperateLog;

/**   
* 项目名称：lt-api   
* 类名称：IUserApiLogService   
* 类描述：用户日志操作统一入口服务   
* 创建人：yuanxin   
* 创建时间：2017年5月8日 下午1:44:50      
*/
public interface IUserApiLogService {
	
	/**
	 * 插入用户订单操作日志（修改止盈止损，递延）
	 * @param defLog    
	 * @return:       void    
	 * @throws 
	 * @author        yuanxin
	 * @Date          2017年5月8日 下午3:09:39
	 */
	public void insertUserOrderLossProfitDeferLog(OrderLossProfitDefLog defLog);
	
	/**
	 * 根据订单id 查询用户订单操作日志(分页)（修改止盈止损，递延）
	 * @param orderId
	 * @return    
	 * @return:       List<OrderLossProfitDefLog>    
	 * @throws 
	 * @author        yuanxin
	 * @Date          2017年5月8日 下午3:10:11
	 */
	public Page<OrderLossProfitDefLog> qryUserOrderLossProfitDeferLog(String orderId,int page,int rows);
	
	/**
	 * 插入用户在线数据  登录 退出
	 * @param userLog
	 */
	public void insertUserClientLog(UserOperateLog userLog);

	/**
	 * 插入用户下线数据  计算在线时间
	 * @param userLog
	 */
	public void insertUserClientOffLine(UserOperateLog userLog);
	
	/**
	 * 分页查询消息日志
	 * @param userId
	 * @param page
	 * @param rows
	 * @return
	 */
	public List<NotificationLog> getNotificationLogPage(String userId,
			int page, int rows);
}
