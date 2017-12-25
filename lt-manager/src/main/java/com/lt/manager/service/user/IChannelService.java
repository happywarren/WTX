package com.lt.manager.service.user;

import java.util.List;

import com.github.pagehelper.Page;
import com.lt.manager.bean.user.Channel;
import com.lt.util.error.LTException;

/**
 * 
 * <br>
 * <b>功能：</b>ChannelService<br>
 */
public interface IChannelService{

/**
	 * 查询Channel--用于分页
	 * @param param
	 * @return
	 * @throws Exception
	 */
	
	public Page<Channel> queryChannelPage(Channel param) throws Exception;

/**
	 * 查询Channel
	 * @param param
	 * @return
	 * @throws Exception
	 */

	public List<Channel> queryChannel(Channel param) throws Exception;

	/**
	 * Channel--数量用于分页
	 * @param param
	 * @return
	 * @throws Exception 
	 */
	
	public Integer queryChannelCount(Channel param) throws Exception;
	
	/**Channel--用于编辑
	 * @param param
	 * @return
	 * @throws Exception 
	 */
	
	public void editChannel(Channel param) throws Exception;
	
	/**Channel--用于添加
	 * @param param
	 * @return
	 * @throws Exception 
	 */
	
	public void addChannel(Channel param) throws Exception;
	
	/**
	 *Channel--用于移除
	 * @param param
	 * @return
	 * @throws Exception 
	 */
	
	public void removeChannel(Integer id) throws Exception;
	/**
	 *Channel--用于获得对象
	 * @param param
	 * @return
	 * @throws Exception 
	 */
	
	public Channel getChannel(Integer id) throws Exception;
	
	/**
	 * @param param
	 * @return
	 * @throws Exception 
	 */
	public List<Channel> findChannel(Channel param) throws Exception;
	
	
	/**
	 * 新增支付渠道管理
	 * @param channelId
	 * @param payChannels
	 * @throws LTException
	 */
	public void addPayChannelRelation(String channelId,String payChannels) throws LTException;
	
	
	/**
	 * 修改支付渠道关联关系
	 * @param channelId
	 * @param payChannels
	 * @throws LTException
	 */
	public void editPayChannelRelation(String channelId,String payChannels) throws LTException;
	
	
	/**
	 * 删除支付渠道关联关系
	 * @param channelId
	 * @param payChannels
	 * @throws LTException
	 */	
	public void removePayChannelRelation(String channelId) throws LTException;

}
