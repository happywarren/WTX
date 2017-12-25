package com.lt.api.user.charge;

import java.util.List;
import java.util.Map;

import com.lt.model.user.UserchargeBankDetailInfo;
import com.lt.util.error.LTException;

/**   
* 项目名称：lt-api   
* 接口名称：IUserApiAutoRechargeService   
* 接口描述：系统自动选择充值渠道   
* 创建人：yuanxin   
* 创建时间：2017年6月12日 下午3:36:05      
*/
public interface IUserApiAutoRechargeService {
	
	/**
	 * 查询用户银行卡下最大的充值金额
	 * @param bankCardId 银行卡
	 * @param userId 用户id 
	 * @param bankCode 银行编号
	 * @return    
	 * @return:       Map<String,Object>  存储数额及通知类型 
	 * @throws 
	 * @author        yuanxin
	 * @Date          2017年6月12日 下午3:59:00
	 */
	public Map<String,Object> qryBankMaxRechargeAmount(String bankCardId,String userId,String bankCode) throws LTException;
	
	/**
	 * 根据金额返回充值方式
	 * @param bankCardId
	 * @param userId
	 * @param bankCode
	 * @param amount
	 * @return    
	 * @return:       Map<String,String>    
	 * @throws 
	 * @author        yuanxin
	 * @Date          2017年6月12日 下午8:28:39
	 */
	public Map<String,String> qryChargeChannel(String bankCardId,String userId,String bankCode,Double amount) throws LTException;
	
	/**
	 * 根据用户id查询银行渠道充值金额
	 * @param userId 用户id
	 * @return
	 * @throws LTException    
	 * @return:       List<UserchargeBankDetailInfo>    
	 * @throws 
	 * @author        yuanxin
	 * @Date          2017年6月14日 下午2:06:53
	 */
	public List<UserchargeBankDetailInfo> qryBankChargeAmountList(String userId) throws LTException;
}
