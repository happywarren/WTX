package com.lt.manager.mogodb.user;

import java.util.Date;

import com.lt.model.logs.LogInfo;
import com.lt.model.user.log.UserOperateLog;

/**   
* 项目名称：lt-manager   
* 类名称：UserLoggerDao   
* 类描述： 用户日志操作类  
* 创建人：yuanxin   
* 创建时间：2017年1月13日 下午2:11:46      
*/
public interface UserLoggerDao {
	
	/**
	 * 根据条件查询用户操作日志（返回单条，根据日期倒序排列）
	 * @param operateLog
	 * @return    
	 * @return:       UserOperateLog    
	 * @throws 
	 * @author        yuanxin
	 * @Date          2017年1月13日 下午2:13:51
	 */
	public UserOperateLog queryUserOPerateLog(UserOperateLog operateLog);

}
