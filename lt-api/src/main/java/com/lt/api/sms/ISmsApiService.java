package com.lt.api.sms;


import com.lt.model.sms.SystemMessage;

/**   
* 项目名称：lt-api   
* 类名称：ISmsApiService   
* 类描述：短信或其他信息对外接口   
* 创建人：yuanxin   
* 创建时间：2016年11月29日 下午7:45:31      
*/
public interface ISmsApiService {
	/**
	 * 发送注册短信
	 * @param announce 信息对象
	 * @return    
	 * @return:       boolean    
	 * @throws 
	 * @author        yuanxin
	 * @Date          2016年11月29日 下午7:08:28
	 */
	public boolean sendImmediateMsg(SystemMessage announce) throws Exception;
	
	/**
	 * 发送递延通知短信
	 * @param announce
	 * @return
	 * @throws Exception    
	 * @return:       boolean    
	 * @throws 
	 * @author        yuanxin
	 * @Date          2017年2月9日 下午3:44:29
	 */
	public boolean sendExpirationMsg(SystemMessage announce) throws Exception;
	
	/**
	 * 发送递延通知短信
	 * @param announce
	 * @return
	 * @throws Exception    
	 * @return:       boolean    
	 * @throws 
	 * @author        yuanxin
	 * @Date          2017年2月9日 下午3:44:29
	 */
	public boolean sendUserFundMsg(SystemMessage announce) throws Exception;

	/**
	 * 查询根据员工ID和验证码查询短信完整信息
	 * @param code
	 * @param staffId
	 * @return
	 */
	public SystemMessage findLastCodeByStaffId( Integer staffId);

}
