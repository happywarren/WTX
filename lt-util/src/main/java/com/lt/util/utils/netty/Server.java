/*
 * Copyright (c) 2015 www.caniu.com - 版权所有
 * 
 * This software is the confidential and proprietary information of
 * luckin Group. You shall not disclose such confidential information 
 * and shall use it only in accordance with the terms of the license 
 * agreement you entered into with www.cainiu.com
 */
package com.lt.util.utils.netty;

import java.nio.ByteBuffer;
import java.util.Date;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;

/**
 *
 * 描述:
 *
 * @author  boyce
 * @created 2015年7月23日 下午2:01:09
 * @since   v1.0.0
 */
public class Server {

	
	public static void start(NettyHandler handler,int workers,int port) throws Exception {
		ExecutorService pool=Executors.newFixedThreadPool(Math.max(workers, 1));
		EventLoopGroup bossGroup = new NioEventLoopGroup(1);
		EventLoopGroup workerGroup = new NioEventLoopGroup();
		try {
			ServerBootstrap b = new ServerBootstrap();
			b.group(bossGroup, workerGroup).channel(NioServerSocketChannel.class).childHandler(new InitHandler(handler,pool));
			b.bind(port).sync().channel().closeFuture().sync();
		} finally {
			bossGroup.shutdownGracefully();
			workerGroup.shutdownGracefully();
		}
	}

	public static void main(String[] args) throws Exception {
		Server.start(new NettyHandler() {
			AtomicInteger ai=new AtomicInteger();
			@Override
			public void handleMsg(Channel channel, String json) {
				System.out.println(ai.incrementAndGet()+" "+System.currentTimeMillis()+" "+json);
//				channel.writeAndFlush("server");
			}
			
			@Override
			public void channelRemoved(Channel channel) {
//				System.out.println("remove");
			}
			
			@Override
			public void channelRegistered(Channel channel) {
//				channel.writeAndFlush("server");
//				System.out.println("add");
			}
		}, 16, 13502);
	}
}
