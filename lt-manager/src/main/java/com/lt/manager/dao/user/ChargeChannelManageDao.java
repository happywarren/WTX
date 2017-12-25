package com.lt.manager.dao.user;

import java.util.List;
import java.util.Map;

import com.lt.manager.bean.user.ChargeChannelVo;
import com.lt.manager.bean.user.RechargeGroupVO;
import com.lt.manager.bean.user.RechargeInfo;
import com.lt.model.fund.FundOptCode;
import com.lt.model.user.charge.BankChargeMapper;
import com.lt.model.user.charge.UserChannelTrans;

/**
 * 
 * 充值渠道管理
 * @author yubei
 * @date 2017年6月15日
 *
 */
public interface ChargeChannelManageDao {
	
	/**
	 * 更新充值渠道信息
	 * @param chargeChannelVo
	 */
	public void updateChargeChannel(ChargeChannelVo chargeChannelVo);

	/**
	 * 更新充值渠道优先级
	 * @param chargeChannelVo
	 */
	public void updateChargeChannelPriority(ChargeChannelVo chargeChannelVo);
	
	/**
	 * 查询充值渠道信息
	 * @return
	 */
	public List<ChargeChannelVo> selectChargeChannelList();
	
	/**
	 * 查询可用的充值渠道
	 * @return
	 */
	public List<ChargeChannelVo> selectAviableChargeChannelList();
	
	/**
	 * 查询充值渠道
	 * @param channelId
	 * @return
	 */
	public ChargeChannelVo selectChargeChannel(String channelId);
	
	
	public List<ChargeChannelVo> selectChargeChannelPriorityList();
	
	/**
	 * 更新充值渠道优先级降序
	 * @param chargeChannelVo
	 */
	public void updateChargeChannelPriorityDesc(Map<String,Object> map);
	
	/**
	 * 更新充值渠道优先级升序
	 * @param chargeChannelVo
	 */
	public void updateChargeChannelPriorityAsc(Map<String,Object> map);
	
	
	/**
	 * 查询用户支持的渠道列表
	 * @param userId
	 * @return
	 */
	
	public List<ChargeChannelVo> selectUserChargeChannelList(String userId);
	
	/**
	 * 查询用户支持的渠道列表
	 * @param userId
	 * @return
	 */	
	public ChargeChannelVo selectUserChargeChannel(Map<String,Object> map);
	
	
	/**
	 * 查询用户不支持渠道
	 * @param userId
	 * @return
	 */
	public List<ChargeChannelVo> selectUserUnsupportChargeChannelList(String userId);
	
	/**
	 * 新增用户渠道关系
	 * @param map
	 */
	public void insertUserChargeMapper(Map<String, Object> map);
	
	
	/**
	 * 删除用户渠道关系
	 * @param map
	 */
	public void deleteUserChargeMapper(Map<String, Object> map);
	
	/**
	 * 查询用户渠道列表
	 * @param userChannelTrans
	 * @return
	 */
	public Integer selectUserChannelTransCount(UserChannelTrans userChannelTrans);
	
	
	/**
	 * 查询充值组列表
	 * @return
	 */
	public List<RechargeGroupVO> selectRechargeGroupList();
	
	
	/**
	 * 增加充值渠道
	 * @param chargeChannelVo
	 */
	public void insertChargeChannel(ChargeChannelVo chargeChannelVo);
	
	
	/**
	 * 查询最大的充值渠道编码
	 * @return
	 */
	public String selectMaxChannelId();
	
	/**
	 * 插入充值渠道编码
	 * @param fundOptCode
	 * @return
	 */
	public void insertFundOptcode(FundOptCode fundOptCode);
	
	
	/**批量增加渠道限额**/
	public void insertBankChargeMapperList(List<BankChargeMapper> bankChargeMappers);
	
	
	/**
	 * 删除支付渠道
	 * @param channelId
	 */
	public void deleteChargeChannelInfoByChannelId(String channelId);
	
	/**
	 * 删除支付渠道关联关系
	 * @param channelId
	 */
	public void delteBankChargeMapperByChannelId(String channelId);	
	
	/**
	 * 删除资金码
	 * @param thirdOptCode
	 */
	public void delteFundOptCodeByThirdOptCode(String thirdOptCode);	
	
	/**
	 * 查询充值列表信息
	 * @return
	 */
	public List<RechargeInfo> selectRechargeInfoList();
}
