package com.lt.user.core.dao.mogodb.user;

import java.util.Date;
import java.util.List;

import com.lt.model.user.log.OrderLossProfitDefLog;
import com.lt.model.user.log.UserCheckRegcode;
import com.lt.model.user.log.UserOperateLog;
import com.lt.model.user.log.UserToken;
import com.lt.model.user.log.UserUpdateInfoLog;

/**   
* 项目名称：lt-user   
* 类名称：LoggerDao   
* 类描述：存储用户模块mobodb的日志内容的处理类   
* 创建人：yuanxin   
* 创建时间：2016年11月29日 下午1:48:31      
*/
public interface LoggerDao {
	
	/**
	 * 存储用户操作日志
	 * @param operateLog    
	 * @return:       void    
	 * @throws 
	 * @author        yuanxin
	 * @Date          2016年12月1日 上午10:12:47
	 */
	public void saveUserOperateLog(UserOperateLog operateLog);
	
	/**
	 * 查询用户token
	 * @param id
	 * @param token
	 * @return    
	 * @return:       UserToken    
	 * @throws 
	 * @author        yuanxin
	 * @Date          2016年12月1日 上午10:51:35
	 */
	public UserToken selectByIdAndToken(String id);	
	
	/**
	 * 插入用户token
	 * @param idToken    
	 * @return:       boolean true:成功
	 * 							false ：失败    
	 * @throws 
	 * @author        yuanxin
	 * @Date          2016年12月1日 上午10:59:33
	 */
	public void saveToken(UserToken idToken);
	
	/**
	 * 删除token
	 * @param id    
	 * @return:       boolean true:成功
	 * 							false ：失败  
	 * @throws 
	 * @author        yuanxin
	 * @Date          2016年12月1日 上午10:59:43
	 */
	public void delToken(String id);
	
	/**
	 * 查询用户最近的几次操作记录（当天）
	 * @param userId 用户id
	 * @param type 日志类型
	 * @param isSuccessd 是否成功
	 * @param createTime 创建时间
	 * @param pageSize 查询的数量
	 * @return    
	 * @return:       List<UserOperateLog>    
	 * @throws 
	 * @author        yuanxin
	 * @Date          2016年12月1日 下午3:38:06
	 */
	public List<UserOperateLog> getLastUserOperateLog(String userId,String type,Boolean isSuccessd,Date createTime,int pageSize);
	
	/**
	 * 获取今天用户验证验证码非成功的记录
	 * @param tele 电话
	 * @param createTime 创建时间
	 * @return    
	 * @return:       List<UserCheckRegcode>    
	 * @throws 
	 * @author        yuanxin
	 * @Date          2016年12月5日 下午9:03:05
	 */
	public List<UserCheckRegcode> getUserCheckRegCode(String tele,Date createTime);
	
	/**
	 * 保存电话校验 短信验证码的操作记录
	 * @param checkRegcode    
	 * @return:       boolean true:成功
	 * 							false ：失败      
	 * @throws 
	 * @author        yuanxin
	 * @Date          2016年12月5日 下午9:08:45
	 */
	public void saveUserCheckRegCode(UserCheckRegcode checkRegcode);
	
	/**
	 * 获取用户修改信息日志
	 * @param infoLog
	 * @return    
	 * @return:       List<UserUpdateInfoLog>    
	 * @throws 
	 * @author        yuanxin
	 * @Date          2016年12月8日 上午10:43:24
	 */
	public List<UserUpdateInfoLog> getUserUpdateInfoLog(UserUpdateInfoLog infoLog);
	
	/**
	 * 根据时间获取用户修改日志
	 * @param infoLog
	 * @return
	 */
	public List<UserUpdateInfoLog> getUserUpdateInfoLogByTime(UserUpdateInfoLog infoLog,Date updateTime);
	
	/**
	 * 保存用户修改信息日志
	 * @param infoLog
	 * @return    
	 * @return:       boolean    
	 * @throws 
	 * @author        yuanxin
	 * @Date          2016年12月8日 下午9:01:01
	 */
	public void saveUserUpdateInfoLog(UserUpdateInfoLog infoLog);
	
	/**
	 * 保存用户修改止盈止损递延扣除的日志
	 * @param defLog    
	 * @return:       void    
	 * @throws 
	 * @author        yuanxin
	 * @Date          2017年5月8日 下午1:49:59
	 */
	public void insertUserOrderLossProfitDefLog(OrderLossProfitDefLog defLog);
	
	/**
	 * 根据订单id 查询用户的订单操作日志
	 * @param orderId
	 * @return    
	 * @return:       List<OrderLossProfitDefLog>    
	 * @throws 
	 * @author        yuanxin
	 * @Date          2017年5月8日 下午3:11:39
	 */
	public List<OrderLossProfitDefLog> qryUserOrderLossProfitDefLogByOrderId(String orderId,int page,int rows);
	
	/**
	 *  根据订单id 查询用户的订单操作日志 总数
	 * @param orderId
	 * @return    
	 * @return:       Integer    
	 * @throws 
	 * @author        yuanxin
	 * @Date          2017年5月8日 下午3:16:15
	 */
	public Integer qryUserOrderLossProfitDefLogSizeByOrderId(String orderId);
	
	/**
	 * 根据token查询数据
	 * @param token
	 * @return
	 */
	public UserToken selectToken(String token);

	public void saveUserOperateLogForOffLine(UserOperateLog userLog);	
}
