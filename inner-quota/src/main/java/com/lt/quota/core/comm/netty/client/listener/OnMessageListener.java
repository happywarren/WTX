package com.lt.quota.core.comm.netty.client.listener;

import io.netty.channel.ChannelHandlerContext;


public interface OnMessageListener {

    void onMessage(ChannelHandlerContext ctx, String msg);

    void onActive(ChannelHandlerContext ctx);

    void onInactive(ChannelHandlerContext ctx);

    void onException(ChannelHandlerContext ctx);

    void onPing(ChannelHandlerContext ctx, String msg);
}
