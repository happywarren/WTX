package com.lt.business.core.dao.sms;

import com.lt.model.sms.SystemMessage;

/**   
* 项目名称：lt-business   
* 类名称：SmsDao   
* 类描述：信息存储类   
* 创建人：yuanxin   
* 创建时间：2016年11月29日 下午4:32:30      
*/
public interface SmsDao {
	
	/**
	 * 插入消息内容
	 * @param message
	 * @return    
	 * @return:       int    
	 * @throws 
	 * @author        yuanxin
	 * @Date          2016年11月29日 下午7:37:35
	 */
	public int saveSms(SystemMessage message);

	/**
	 * 查询员工最后一条验证数据 15分钟之内后台发送的数据
	 * @param code
	 * @param staffId
	 * @return
	 */
	public SystemMessage findLastCodeByStaffId(Integer staffId);
}
