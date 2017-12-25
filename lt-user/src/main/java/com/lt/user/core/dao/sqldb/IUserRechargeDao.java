package com.lt.user.core.dao.sqldb;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.lt.model.user.UserchargeBankDetailInfo;
import com.lt.model.user.charge.BankChargeDetail;
import com.lt.model.user.charge.BankChargeMapper;
import com.lt.model.user.charge.ChargeChannelInfo;
import com.lt.model.user.charge.UserChannelTrans;
import com.lt.model.user.charge.UserChargeMapper;

/**   
* 项目名称：lt-user   
* 类名称：IUserRechargeDao   
* 类描述：   
* 创建人：yuanxin   
* 创建时间：2017年6月12日 下午4:49:15      
*/
public interface IUserRechargeDao {
	
	/**
	 * 通过用户id查询关联的渠道
	 * @param userId
	 * @return    
	 * @return:       List<ChargeChannelInfo>    
	 * @throws 
	 * @author        yuanxin
	 * @Date          2017年6月12日 下午4:58:49
	 */
	public List<ChargeChannelInfo> getUserChargeMapperChannel(String userId);
	
	/**
	 * 查询银行编号下的单日入金限额和 单笔限额 的值
	 * @param bankCode 银行编号
	 * @param channelList 渠道列表
	 * @return    
	 * @return:       List<BankChargeMapper>    
	 * @throws 
	 * @author        yuanxin
	 * @Date          2017年6月12日 下午5:37:16
	 */
	public List<BankChargeMapper> getUserBankChargeChannel(@Param("bankCode")String bankCode,@Param("channelList")List<String> channelList);
	
	/**
	 * 查询银行卡id下当日的总充值金额
	 * @param bankId
	 * @param channelList
	 * @return    
	 * @return:       List<BankChargeDetail>    
	 * @throws 
	 * @author        yuanxin
	 * @Date          2017年6月12日 下午5:58:30
	 */
	public List<BankChargeDetail> getBankChargeDetail(@Param("userId")String userId,@Param("bankcardCode")String bankcardCode,@Param("createTime")String createTime,@Param("channelList")List<String> channelList);
	
	/**
	 * 查询用户充值记录
	 * @param userId
	 * @return    
	 * @return:       UserchargeBankDetailInfo    
	 * @throws 
	 * @author        yuanxin
	 * @Date          2017年6月14日 上午11:19:20
	 */
	public UserchargeBankDetailInfo getUserChargeBankInfo(String userId);
	
	/**
	 * 查询用户可充值的银行卡列表
	 * @param userId
	 * @return    
	 * @return:       List<UserchargeBankDetailInfo>    
	 * @throws 
	 * @author        yuanxin
	 * @Date          2017年6月14日 下午2:16:25
	 */
	public List<UserchargeBankDetailInfo> getUserChargeBankList(String userId);
	
	/**
	 * 获取配置的默认开启的渠道
	 * @param isDefault
	 * @return    
	 * @return:       List<ChargeChannelInfo>    
	 * @throws 
	 * @author        yuanxin
	 * @Date          2017年6月16日 下午2:04:05
	 */
	public List<ChargeChannelInfo> getDefaultChargeChannelList(Integer isDefault);
	
	/**
	 * 插入用户渠道关联关系
	 * @param userChargeMapper    
	 * @return:       void    
	 * @throws 
	 * @author        yuanxin
	 * @Date          2017年6月16日 下午2:29:17
	 */
	public void insertUserChargeMapperMutil(List<UserChargeMapper> userChargeMapper);
	
	/**
	 * 查询用户驱动使用次数
	 * @param userChannelTrans
	 * @return
	 * @author yubei
	 * @Date 2017年7月28日
	 */
	public Integer selectUserChannelTransCount(UserChannelTrans userChannelTrans);
	
	
	/**
	 * 查询用户渠道列表
	 * @param channelId 注册渠道
	 * @return
	 */
	public List<String> selectUserChannelIds(String channelId);
	
	
}
