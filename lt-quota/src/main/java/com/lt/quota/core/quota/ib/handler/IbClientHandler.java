package com.lt.quota.core.quota.ib.handler;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.lt.quota.core.comm.netty.client.listener.OnMessageListener;
import com.lt.quota.core.utils.Utils;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@ChannelHandler.Sharable
public class IbClientHandler extends SimpleChannelInboundHandler<String> {

    private Logger logger = LoggerFactory.getLogger(IbClientHandler.class);

    private OnMessageListener messageListener;

    public IbClientHandler(OnMessageListener messageListener) {
        this.messageListener = messageListener;
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        messageListener.onActive(ctx);
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        logger.error("与服务器 {} 断线", ctx.channel().remoteAddress());
        messageListener.onInactive(ctx);
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, String msg) throws Exception {
        logger.debug("收到IB服务器 {} 消息 {}", ctx.channel().remoteAddress(), msg);
        JSONObject jsonData = JSON.parseObject(msg);
        String cmd = jsonData.getString("cmd");
        if (Utils.isEmpty(cmd)) {
            return;
        }
        if ("1001".equals(cmd)) {
            String DATA = jsonData.getString("data");
            jsonData = JSON.parseObject(DATA);
            long lastTime = jsonData.getLong("timeStamp");
            if (System.currentTimeMillis() - lastTime > 10 * 1000) {
                ctx.close();
                logger.error("与服务器 {} 连接网络拥塞, 主动断线重连...", ctx.channel().remoteAddress());
            }
        } else {
            messageListener.onMessage(ctx, msg);
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        logger.error("与服务器 {} 连接出现异常, 异常信息 {} ", ctx.channel().remoteAddress(), cause.getMessage());
        messageListener.onException(ctx);
    }
}
