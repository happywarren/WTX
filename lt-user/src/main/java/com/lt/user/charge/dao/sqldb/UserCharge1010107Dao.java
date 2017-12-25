package com.lt.user.charge.dao.sqldb;

import java.util.List;
import java.util.Map;

import com.lt.model.user.charge.XDPayRecharge;

/**
 * 
* 项目名称：lt-user   
* 类名称：UserChargeDao   
* 类描述：  充值集中处理dao
* 创建人：yubei
* 创建时间：2017年8月7日 
 */

public interface UserCharge1010107Dao {
	
	/**
	 * 插入熙大支付宝()
	 * @param XdPayRecharge    
	 * @return:       void    
	 * @throws 
	 * @author        yubei
	 * @Date          2017年8月7日
	 */
	public void insertXDPayRecharge(XDPayRecharge recharge) ; 
	
	/**
	 * 通过id查询熙大支付宝充值记录
	 * @param orderId
	 * @return    
	 * @return:       XdPayRecharge    
	 * @throws 
	 * @author        yuanxin
	 * @Date          2017年8月7日
	 */
	public XDPayRecharge selectXDPayRecharge(String orderNo);
	
	/**
	 * 更新熙大支付宝信息
	 * @param XdPayRecharge    
	 * @return:       void    
	 * @throws 
	 * @author        yubei
	 * @Date         2017年8月7日
	 */
	public Integer updateXDPayRecharge(XDPayRecharge recharge);
	
	/***
	 * 根据条件查询支付宝订单 id
	 * @param paraMap 包含条件  1， limit 条数   
	 * 						2， beginDate 起始时间
	 * 						3， status 状态
	 *                      4， merid 商户号
	 * @return    	
	 * @return:       String    
	 * @throws 
	 * @author        yubei
	 * @Date          2017年8月7日
	 */
	public List<String> selectXDPayRechargeList(Map<String,String> paraMap) ;
	
	/**
	 * 
	 * @param payId
	 * @return    
	 * @return:       XdPayRecharge    
	 * @throws 
	 * @author        yubei
	 * @Date          2017年8月7日
	 */
	public XDPayRecharge selectXDPayRechargeForUpdate(String orderNo);
}
