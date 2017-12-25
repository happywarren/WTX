package com.lt.tms.comm.netty.client.listener;

import io.netty.channel.ChannelHandlerContext;


public interface MessageListener {

    void onMessage(ChannelHandlerContext ctx, String msg);

    void onActive(ChannelHandlerContext ctx);

    void onInactive(ChannelHandlerContext ctx);

    void onException(ChannelHandlerContext ctx);
}
