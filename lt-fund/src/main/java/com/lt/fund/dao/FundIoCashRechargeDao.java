package com.lt.fund.dao;

import java.util.List;
import java.util.Map;

import com.lt.model.fund.FundIoCashRecharge;
import com.lt.model.user.charge.BankChargeDetail;
import com.lt.model.user.charge.UserChannelTrans;

/**
 * 充值iodao
 * @author jingwb
 *
 */
public interface FundIoCashRechargeDao {

	/**
	 * 插入充值流水
	 * @param ficr
	 */
	public int insertFundIoRecharge(FundIoCashRecharge fio);
	
	/**
	 * 根据外部订单号查询充值记录
	 * @param payId
	 * @return
	 */
	public FundIoCashRecharge selectFundIoRechargeOne(String payId);


	/**
	 * 根据外部订单号查询充值记录
	 * @param payId
	 * @return
	 */
	public FundIoCashRecharge selectFundIoRechargeForUpdate(String payId);
	
	/** 根据订单号查询充值记录*/
	public FundIoCashRecharge selectRechargeIoById(String id);
	
	/**
	 * 查询充值流水
	 * @param fio
	 * @return
	 */
	public List<FundIoCashRecharge> selectFundIoRecharge(FundIoCashRecharge fio);

	/**
	 * 查询充值流水
	 * @param fio
	 * @return
	 */
	public List<FundIoCashRecharge> selectFundIoRechargeList(Map<String, Object> queryMap);
	
	/**
	 * 修改充值流水信息
	 * @param fio
	 */
	public void updateFundIoRecharge(FundIoCashRecharge fio);
	
	/**
	 * 查询该用户银生宝充值成功的充值标识
	 * @param userId
	 * @return
	 */
	public String selectRechargeIdentification(String userId,String thirdOptcode);
	
	/**
	 * 修改充值流水信息（支付id）
	 * @param fio    
	 * @return:       void    
	 * @throws 
	 * @author        yuanxin
	 * @Date          2017年4月5日 下午3:23:07
	 */
	public void updateFundIoRechargeByPayId(FundIoCashRecharge fio);
	
	/**
	 * 插入银行充值渠道统计列表
	 * @param bankChargeDetail    
	 * @return:       void    
	 * @throws 
	 * @author        yuanxin
	 * @Date          2017年6月13日 下午3:47:40
	 */
	public void insertBankChargeDetail(BankChargeDetail bankChargeDetail);
	
	/**
	 * 根据条件查询用户银行渠道下充值数据   用户ID，银行卡号，创建时间，渠道id
	 * @param bankChargeDetail    
	 * @return:       void    
	 * @throws 
	 * @author        yuanxin
	 * @Date          2017年6月13日 下午4:40:28
	 */
	public BankChargeDetail qryBankChargeDetailByCondition(BankChargeDetail bankChargeDetail);
	
	/**
	 * 根据条件累加用户银行渠道下充值金额 用户id，银行卡号，创建时间，渠道id
	 * @param bankChargeDetail
	 * @return    
	 * @return:       int    
	 * @throws 
	 * @author        yuanxin
	 * @Date          2017年6月13日 下午5:05:11
	 */
	public int updateBankChargeAmountByCondition(BankChargeDetail bankChargeDetail);
	
	/**
	 * 保存用户充值渠道使用
	 * @Date 2017年7月27日
	 * @param yubei
	 */
	public void insertUserChannelTrans(UserChannelTrans userChannelTrans);

	/**
	 * 分页查询充值记录
	 * @param params
	 * @return
	 */
	public List<FundIoCashRecharge> findRechargeByUserId(Map<String,Object> params);
}
