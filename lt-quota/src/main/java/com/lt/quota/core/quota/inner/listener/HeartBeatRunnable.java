package com.lt.quota.core.quota.inner.listener;

import com.alibaba.fastjson.JSON;
import io.netty.channel.ChannelHandlerContext;

import java.util.HashMap;
import java.util.Map;

// 心跳包
public class HeartBeatRunnable implements Runnable {

    private ChannelHandlerContext ctx;

    public HeartBeatRunnable(final ChannelHandlerContext ctx) {
        this.ctx = ctx;
    }

    @Override
    public void run() {
        ctx.writeAndFlush(makeHeartBeatPacket());
    }

    private String makeHeartBeatPacket() {
        Map<String, Object> data = new HashMap<String, Object>();
        data.put("timeStamp", System.currentTimeMillis());
        Map<String, Object> requestData = new HashMap<String, Object>();
        requestData.put("CMDID", 1001);
        requestData.put("DATA", data);
        return JSON.toJSONString(requestData);
    }
}