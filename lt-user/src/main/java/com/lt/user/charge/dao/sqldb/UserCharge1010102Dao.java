package com.lt.user.charge.dao.sqldb;

import com.lt.model.user.charge.FundKqRecharge;

/**
 * 
* 项目名称：lt-user   
* 类名称：UserChargeDao   
* 类描述：  充值集中处理dao
* 创建人：yuanxin   
* 创建时间：2017年6月30日 下午6:22:16
 */

public interface UserCharge1010102Dao {
	
	/**
	 * 插入快钱充值记录(发送验证码记录)
	 * @param alipaymRecharge    
	 * @return:       void    
	 * @throws 
	 * @author        yuanxin
	 * @Date          2017年7月11日 下午7:33:43
	 */
	public void insertKQrecordValid(FundKqRecharge fundKqRecharge) ; 
	
	/**
	 * 添加验证码返回数据（修改数据操作）
	 * @param fundKqRecharge    
	 * @return:       void    
	 * @throws 
	 * @author        yuanxin
	 * @Date          2017年7月12日 上午10:46:08
	 */
	public void addKQrecordValid(FundKqRecharge fundKqRecharge) ; 
	
	/**
	 * 订单请求数据存储
	 * @param fundKqRecharge    
	 * @return:       void    
	 * @throws 
	 * @author        yuanxin
	 * @Date          2017年7月12日 上午10:51:00
	 */
	public  void addKQrecordPay(FundKqRecharge fundKqRecharge);
	
	/**
	 * 订单回复数据修改
	 * @param fundKqRecharge    
	 * @return:       void    
	 * @throws 
	 * @author        yuanxin
	 * @Date          2017年7月12日 上午11:10:44
	 */
	public void addKQrecordResponsePay(FundKqRecharge fundKqRecharge);
	
	/**
	 * 根据订单id 查询发送验证码时存储的数据(需在完成验证码之后调用该方法)
	 * @param payOrderId 订单id
	 * @return    
	 * @return:       FundKqRecharge    
	 * @throws 
	 * @author        yuanxin
	 * @Date          2017年7月12日 下午4:41:15
	 */
	public FundKqRecharge getFundKqRechargeInValid(String payOrderId);
}
