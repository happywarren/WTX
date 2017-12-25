package com.lt.user.core.service;

import java.util.List;
import java.util.Map;

import com.lt.model.user.UserchargeBankDetailInfo;
import com.lt.model.user.charge.BankChargeMapper;
import com.lt.model.user.charge.ChargeChannelInfo;
import com.lt.model.user.charge.UserChannelTrans;
import com.lt.util.error.LTException;

/**   
* 项目名称：lt-user   
* 类名称：IUserAutoRechageService   
* 类描述：  系统自动选择充值渠道 
* 创建人：yuanxin   
* 创建时间：2017年6月12日 下午4:01:52      
*/
public interface IUserAutoRechageService {

	/**
	 * 通过用户id查询用户支持的充值渠道
	 * @param userId 用户id
	 * @return    
	 * @return:       List<ChargeChannelInfo>    
	 * @throws 
	 * @author        yuanxin
	 * @Date          2017年6月12日 下午3:51:37
	 */
	public List<ChargeChannelInfo> qryUserChargeChannel(String userId) throws LTException;
	
	/**
	 * 通过银行编号，及渠道列表查询渠道下的单笔限额及单日限额
	 * @param bankCode 银行编号
	 * @param channelIds 渠道列表
	 * @return    
	 * @return:       List<BankChargeMapper>    
	 * @throws 
	 * @author        yuanxin
	 * @Date          2017年6月12日 下午3:55:09
	 */
	public List<BankChargeMapper> qryBankChargeInfo(String bankCode,List<String> channelIds) throws LTException;
	
	/**
	 * 通过银行卡id查询对应渠道下的单日充值金额
	 * @param bankCardId 银行卡ID
	 * @param channelIds 渠道列表
	 * @return    
	 * @return:       Map<String,Double>    
	 * @throws 
	 * @author        yuanxin
	 * @Date          2017年6月12日 下午4:19:43
	 */
	public Map<String,Double> qryBankIdChannelDailyAmt(String bankCardId,String userId,List<String> channelIds) throws LTException;
	
	/**
	 * 查询用户最近的充值记录
	 * @param userId
	 * @return
	 * @throws LTException    
	 * @return:       BankChargeDetail    
	 * @throws 
	 * @author        yuanxin
	 * @Date          2017年6月14日 上午11:01:54
	 */
	public UserchargeBankDetailInfo qryLastUserChargeBankInfo(String userId) throws LTException ;
	
	/**
	 * 查询用户下可用的银行
	 * @param userId
	 * @return
	 * @throws LTException    
	 * @return:       List<UserchargeBankDetailInfo>    
	 * @throws 
	 * @author        yuanxin
	 * @Date          2017年6月14日 下午2:14:53
	 */
	public List<UserchargeBankDetailInfo> qryUserChargeBankList(String userId) throws LTException ;
	
	/**
	 * 用户注册初始化关联充值渠道
	 * @param userId 用户id
	 * @throws LTException    
	 * @return:       void    
	 * @throws 
	 * @author        yuanxin
	 * @Date          2017年6月16日 下午2:01:40
	 */
	public void initUserChargeMapper(String userId,String regSource) throws LTException ;
	
	/**
	 * 根据用户id，银行编号，银行卡号 查询用户是否存在改银行卡
	 * @param userId 用户id
	 * @param bankCode 银行编号
	 * @param bankCardNum 银行卡
	 * @return
	 * @throws LTException    
	 * @return:       boolean    
	 * @throws 
	 * @author        yuanxin
	 * @Date          2017年6月26日 上午9:49:14
	 */
	public boolean isExitBankCard(String userId,String bankCode,String bankCardNum) throws LTException;
	
	/**
	 * 查询用户当渠道使用次数
	 * @param userChannelTrans
	 * @return
	 * @author yubei
	 * @throws LTException
	 */
	public Integer getUserChannelTransCount(UserChannelTrans userChannelTrans) throws LTException; 
}
