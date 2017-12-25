package com.lt.business.core.netty;

import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.EventLoop;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.util.concurrent.Future;
import io.netty.util.concurrent.GenericFutureListener;
import io.netty.util.concurrent.ScheduledFuture;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.lt.util.utils.StringTools;

/**
 * Created by sunch on 2016/11/8.
 */
public class ClientHandler extends SimpleChannelInboundHandler<String> {

    public static final int FRAME_TYPE_QUOTE = 1000;// 行情
    public static final int FRAME_TYPE_HEART_BEAT = 1001;// 心跳
	
    private OnMessageListener messageListener_;
    private ScheduledFuture<?> heartBeatScheduler_;
    private ChannelHandlerContext handlerContext_;

    private static Logger LOGGER = LoggerFactory.getLogger(ClientHandler.class);
    private static final int HEART_BEAT_PERIOD = 2*60;

    public ClientHandler(OnMessageListener messageListener) {
        messageListener_ = messageListener;
    }

    // 发送消息
    public void sendMessage(final String msg, final ResultListener<String> listener) {
    	LOGGER.info("发送消息  : {}",msg);
        ChannelFuture future = handlerContext_.writeAndFlush(msg);
        future.addListener(new GenericFutureListener<Future<? super Void>>() {
            @Override
            public void operationComplete(Future<? super Void> future) throws Exception {
                if (future.isSuccess()) {
                    listener.onSuccess("向服务器" + handlerContext_.channel().remoteAddress() + "/发送信息成功..." + msg);
                } else {
                    listener.onFailure(msg);
                    LOGGER.error("向服务器" + handlerContext_.channel().remoteAddress() + "/发送信息失败..." + msg);
                    if (future.cause() != null) {
                        LOGGER.error("异常信息: " + future.cause().getMessage());
                    }
                }
            }
        });
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        handlerContext_ = ctx;

        // 连接建立成功, 主动发送心跳消息
        final EventLoop loop = ctx.channel().eventLoop();
        heartBeatScheduler_ = loop.scheduleAtFixedRate(new HeartBeatRunnable(ctx), 0, HEART_BEAT_PERIOD, TimeUnit.SECONDS);
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        cancelHeartBeatScheduler();
        ctx.close();
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, String msg) throws Exception {
//    	LOGGER.info("收到信息 = {}",msg);
        JSONObject jsonData = JSON.parseObject(msg);
        Integer frameType = jsonData.getInteger("CMDID");
        if (!StringTools.isNotEmpty(frameType)){
        	 readCmd(ctx,msg);
        	 return;
        }
        if (frameType != FRAME_TYPE_HEART_BEAT) {
            messageListener_.onMessage(msg);
        } else {
            String DATA = jsonData.getString("DATA");
            jsonData = JSON.parseObject(DATA);
            long lastTime = jsonData.getLong("timeStamp");
            if (System.currentTimeMillis() - lastTime > 10 * 1000) {
                ctx.close();
                LOGGER.error("与服务器 {} 连接网络拥塞, 主动断线重连...", ctx.channel().remoteAddress());
            }

            LOGGER.info("收到心跳包: " + ctx.channel().remoteAddress());
        }
    }
    
    private void readCmd(ChannelHandlerContext ctx, String msg){
    	JSONObject jsonData = JSON.parseObject(msg);
    	Integer frameType = jsonData.getInteger("cmd");
    	if (frameType == 1005) {
            messageListener_.onMessage(msg);
        } else {
            String DATA = jsonData.getString("data");
            jsonData = JSON.parseObject(DATA);
            long lastTime = jsonData.getLong("timeStamp");
            if (System.currentTimeMillis() - lastTime > 10 * 1000) {
                ctx.close();
                LOGGER.error("与赢透服务器 {} 连接网络拥塞, 主动断线重连...", ctx.channel().remoteAddress());
            }

            LOGGER.info("收到赢透心跳包: " + ctx.channel().remoteAddress());
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
    	cause.printStackTrace();
        ctx.close();
        LOGGER.error("与服务器 {} 连接出现异常, 异常信息: " + cause.getMessage(), ctx.channel().remoteAddress());
    }

    // 停止发送心跳包
    private void cancelHeartBeatScheduler() {
        if (heartBeatScheduler_ != null) {
            heartBeatScheduler_.cancel(true);
            heartBeatScheduler_ = null;
        }
    }

    // 心跳包
    private class HeartBeatRunnable implements Runnable {

        private ChannelHandlerContext ctx_;
        private HeartBeatRunnable(final ChannelHandlerContext ctx) {
            this.ctx_ = ctx;
        }

        @Override
        public void run() {
            ctx_.writeAndFlush(makeHeartBeatPacket());
        }

        private String makeHeartBeatPacket() {
            Map<String, Object> data = new HashMap<String, Object>();
            data.put("timeStamp", System.currentTimeMillis());

            Map<String, Object> pkt = new HashMap<String, Object>();
            pkt.put("CMDID", FRAME_TYPE_HEART_BEAT);
            pkt.put("DATA", data);

            return JSON.toJSONString(pkt);
        }
    }

}
