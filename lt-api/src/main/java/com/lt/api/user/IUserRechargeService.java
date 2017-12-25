package com.lt.api.user;

import java.util.List;
import java.util.Map;

import com.lt.model.sys.SysConfig;
import com.lt.util.error.LTException;

/**   
* 项目名称：lt-user   
* 类名称：IUserRechargeService   
* 类描述：用户充值接口   
* 创建人：yuanxin   
* 创建时间：2017年4月5日 上午9:27:20      
*/
public interface IUserRechargeService {
	
	/**
	 * 快钱充值 获取动态验证码
	 * @throws LTException    
	 * @return:       void    
	 * @throws 
	 * @author        yuanxin
	 * @Date          2017年4月5日 上午9:28:42
	 */
	public Map<String, String> getKQDynamicNum(Map<String, Object> paraMap) throws LTException;
	
	/**
	 * 快钱充值 用户提交充值
	 * @param paraMap
	 * @throws LTException    
	 * @return:       void    
	 * @throws 
	 * @author        yuanxin
	 * @Date          2017年4月5日 下午1:38:15
	 */
	public Map<String,String> userKQPayOrder(Map<String, Object> paraMap) throws LTException;
	
	/**
	 * 快钱充值 查询用户快钱充值记录
	 * @param paraMap
	 * @throws LTException    
	 * @return:       void    
	 * @throws 
	 * @author        yuanxin
	 * @Date          2017年4月5日 下午2:55:28
	 */
	public Map<String,Object> qryKQuserPayOrder(Map<String, Object> paraMap) throws LTException;
	
	/**
	 * 处理快钱返回接口
	 * @param paraMap
	 * @throws LTException    
	 * @return:       void    
	 * @throws 
	 * @author        yuanxin
	 * @Date          2017年4月20日 下午5:13:05
	 */
	public void reviceKQResponse(Map<String, Object> paraMap) throws LTException;

	/**
	 * 客户端充值回调配置
	 * @return
	 */
	public List<SysConfig> rechargeConfigList();
}
