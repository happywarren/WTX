package com.lt.manager.service.user;

import java.util.List;
import java.util.Map;

import com.lt.manager.bean.user.ChargeChannelVo;
import com.lt.manager.bean.user.RechargeGroupVO;
import com.lt.util.error.LTException;

/**
 * 充值渠道服务
 * 
 * @author yubei
 * @date 2017年6月15日
 *
 */
public interface IChargeChannelManageService {

	/**
	 * 查询充值渠道信息列表
	 * 
	 * @return
	 */
	public List<ChargeChannelVo> queryChargeChannelList();

	/**
	 * 更新充值渠道优先级
	 * 
	 * @param chargeChannelVo
	 */
	public void modifyChargeChannelPriority(ChargeChannelVo chargeChannelVo);

	/**
	 * 更新充值渠道信息
	 * 
	 * @param chargeChannelVo
	 */
	public void modifyChargeChannel(ChargeChannelVo chargeChannelVo);

	/**
	 * 查询充值渠道信息列表
	 * 
	 * @return
	 */
	public List<ChargeChannelVo> queryChargeChannelPriorityList();

	/**
	 * 查询用户渠道
	 * 
	 * @param userId
	 * @return
	 */
	public Map<String, Object> getUserChargeChannelInfo(String userId);

	/**
	 * 修改用户充值渠道信息
	 * 
	 * @param userId
	 * @return
	 */
	public Map<String, Object> modifyUserChargeChannelInfo(String userId, String supportChannels, String unsupportChannels);

	/**
	 * 查詢充值組列表
	 * 
	 * @return
	 */
	public List<RechargeGroupVO> queryRechargeGroupList() throws LTException;

	/** 增加充值渠道 **/

	public void addChargeChannel(ChargeChannelVo chargeChannelVo) throws LTException;

	/**
	 * 删除充值渠道
	 * 
	 * @param channelId
	 */
	public void deleteChargeChannelInfo(String channelId) throws LTException;
	
	
	/**
	 * 刷新配置
	 * @throws LTException
	 */
	public void refreshConfig() throws LTException;
}
