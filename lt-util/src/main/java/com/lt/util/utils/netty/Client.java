/*
 * Copyright (c) 2015 www.caniu.com - 版权所有
 * 
 * This software is the confidential and proprietary information of
 * luckin Group. You shall not disclose such confidential information 
 * and shall use it only in accordance with the terms of the license 
 * agreement you entered into with www.cainiu.com
 */
package com.lt.util.utils.netty;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 *
 * 描述:
 *
 * @author  boyce
 * @created 2015年7月23日 下午2:01:09
 * @since   v1.0.0
 */
public class Client {

	public static void connect(NettyHandler handler, int workers, String host, int port) throws Exception {
		ExecutorService pool=Executors.newFixedThreadPool(Math.max(workers, 1));
		EventLoopGroup workerGroup = new NioEventLoopGroup(1);
		try {
			Bootstrap b = new Bootstrap();
			b.group(workerGroup).channel(NioSocketChannel.class).handler(new InitHandler(handler, pool));

			b.connect(host, port).sync().channel().closeFuture().sync();
		} finally {
			workerGroup.shutdownGracefully();
			pool.shutdown();
		}
	}

	public static void main(String[] args) throws Exception {
//		for (int i = 0; i < 10; i++) {
			new Thread(){
				public void run() {
					try {
						Client.connect(new NettyHandler() {
							@Override
							public void handleMsg(Channel channel, String json) {
								System.out.println("客户端消息接收："+json);
//							channel.writeAndFlush("clientclientclientclientclientclientclientclientclientclientclientclientclientclientclientclientclientclientclientclientclientclientclientclientclientclientclientclient");
							}
							@Override
							public void channelRemoved(Channel channel) {
//								System.out.println("remove");
							}
							@Override
							public void channelRegistered(Channel channel) {
								System.out.println("add");
								String msg = "{\"token\":\"f9bb33a0a531974393530e60e5efb6a6fe51706450a8530d7dd05c1483589b908c11aad91ae4577ede02fb2f1969ba233742ccde63a13c0c787d6d9067aa0b79\"}";
								channel.writeAndFlush(msg);
							}
						}, 16, "127.0.0.1", 10000);
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}//192.168.1.20
				};
			}.start();//1437998421709  1437998412356
//		}
	}
	

}
