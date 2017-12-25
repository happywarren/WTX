package com.lt.manager.dao.user;


import com.lt.manager.bean.user.Channel;
import com.lt.manager.bean.user.ChargeChannelVo;
import com.lt.manager.bean.user.PayAndChannelRelation;
import com.lt.model.user.charge.UserChargeMapper;

import java.util.List;
/**
 * 
 * <br>
 * <b>功能：</b>ChannelDao<br>
 */
public interface ChannelDao {

	/**
	 * Channel查询接口
	 * @param param
	 * @return
	 */
	public List<Channel> selectChannelPage(Channel param);
	
	/**
	 * Channel查询根据数量分页
	 * @param param
	 * @return
	 */
	public Integer selectChannelCount(Channel param);
	
	
	/**
	 * Channel查询根据数量分页
	 * @param param
	 * @return
	 */
	public void insertChannel(Channel param);
	
	/**
	 *Channel信息更改
	 * @param param
	 * @return
	 */
	public void updateChannel(Channel param);
	
	/**
	 * Channel信息删除
	 * @param param
	 * @return
	 */
	public void deleteChannel(Integer id);
	
	/**
	 * 根据ID获得Channel对象
	 */
	public Channel selectChannelById(Integer id);
	
	
	/**
	 * 根据ID获得Channel对象
	 */
	public List<Channel> selectChannel(Channel param);
	
	
	/**
	 * 查询注册渠道关联的支付渠道
	 * @param id
	 * @return
	 */
	public List<ChargeChannelVo> selectPayChannelRelation(String channelId);
	
	
	/**
	 * 新增支付渠道关系
	 * @param channelRelations
	 */
	public void insertPayChannelRelation(List<PayAndChannelRelation> channelRelations);
	
	/**
	 * 删除支付渠道关联关系
	 * @param channelId
	 */
	public void deletePayChannelRelation(String channelId);
	
	
	/**
	 * 查询注册人数
	 * @return
	 */
	public Integer selectRegisterCount(String code,String brandId);
	
	
	/**
	 * 统计入金人数和金额
	 * @param code
	 * @return
	 */
	public Channel selectRechargeCount(String code);
	
	
	public Integer selectTraderCount(String code);
	
	Double selectRechargeAmount(String code);
	
	/**
	 * 查询当前用户id
	 * @param status
	 * @return
	 */
	public List<String> selectUserIds(String regSource);
	
	/**
	 * 查询用户渠道关系
	 * @param regSource
	 * @return
	 */
	public List<UserChargeMapper> selectUserChargeMappers(String regSource);
	
	
	/**
	 * 批量保存用户渠道关系
	 * @param userChargeMappers
	 */
	public void insertUserChargeMapperMutil(List<UserChargeMapper> userChargeMappers);

	/**
	 *  根据品牌 id 查询渠道
	 * @param brandId 品牌 id
	 */
	public List<Channel> findChannelByBrandId(String brandId);

}
