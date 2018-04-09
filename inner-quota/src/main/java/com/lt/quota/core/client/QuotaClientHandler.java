package com.lt.quota.core.client;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.lt.quota.core.comm.netty.client.listener.OnMessageListener;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.EventLoop;
import io.netty.channel.SimpleChannelInboundHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

@ChannelHandler.Sharable
public class QuotaClientHandler extends SimpleChannelInboundHandler<String> {

    private Logger logger = LoggerFactory.getLogger(QuotaClientHandler.class);

    private OnMessageListener onMessageListener;

    public QuotaClientHandler(OnMessageListener onMessageListener){
        this.onMessageListener = onMessageListener;
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        logger.error("与服务器 {} 断线", ctx.channel().remoteAddress());
        onMessageListener.onInactive(ctx);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        logger.error("与服务器 {} 连接出现异常, 异常信息 {} ", ctx.channel().remoteAddress(), cause.getMessage());
        onMessageListener.onException(ctx);
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, String msg) throws Exception {
        logger.debug("收到服务器 {} 消息 {}", ctx.channel().remoteAddress(), msg);
        logger.info(msg);
        /*
        JSONObject jsonObject = JSON.parseObject(msg);
        String cmd =  jsonObject.getString("CMD");
        JSONObject data = jsonObject.getJSONObject("DATA");
        if(cmd.equals("1001")){   //响应
            long lastTime =  data.getLongValue("timeStamp");
            if (System.currentTimeMillis() - lastTime > 10000L) {
                //断开链接
                ctx.close();
                logger.info("与服务器:{}失去连接",ctx.channel().remoteAddress());
            }
        }else{
            onMessageListener.onMessage(ctx,msg);
        }*/

    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        logger.error("与服务器 {} 连接成功", ctx.channel().remoteAddress());
        onMessageListener.onActive(ctx);
    }
}
