package com.lt.tms.comm.netty.server.listener;

import io.netty.channel.ChannelHandlerContext;


public interface MessageListener {

    void onActive(ChannelHandlerContext ctx);

    void onInactive(ChannelHandlerContext ctx);

    void onException(ChannelHandlerContext ctx);

    void onPing(ChannelHandlerContext ctx,String message);

    void onMessage(ChannelHandlerContext ctx, String message);
}
