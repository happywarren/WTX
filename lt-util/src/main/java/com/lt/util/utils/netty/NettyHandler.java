/*
 * Copyright (c) 2015 www.caniu.com - 版权所有
 * 
 * This software is the confidential and proprietary information of
 * luckin Group. You shall not disclose such confidential information 
 * and shall use it only in accordance with the terms of the license 
 * agreement you entered into with www.cainiu.com
 */
package com.lt.util.utils.netty;

import io.netty.channel.Channel;

/**
 *
 * 描述:业务处理接口，屏蔽netty对普通开发人员的影响
 *
 * @author  boyce
 * @created 2015年7月23日 下午5:05:14
 * @since   v1.0.0
 */
public interface NettyHandler {
	public void channelRegistered(Channel channel);
	public void handleMsg(Channel channel,String json);
	public void channelRemoved(Channel channel);
}
