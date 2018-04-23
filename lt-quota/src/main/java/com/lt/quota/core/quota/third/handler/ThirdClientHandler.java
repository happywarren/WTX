package com.lt.quota.core.quota.third.handler;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.lt.quota.core.comm.netty.client.listener.OnMessageListener;
import com.lt.quota.core.quota.third.ThirdSanpShot;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.ByteOrder;

@ChannelHandler.Sharable
public class ThirdClientHandler extends SimpleChannelInboundHandler<byte[]> {

    private Logger logger = LoggerFactory.getLogger(ThirdClientHandler.class);

    private OnMessageListener messageListener;

    public ThirdClientHandler(OnMessageListener messageListener) {
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
    protected void channelRead0(ChannelHandlerContext ctx, byte[] msg) throws Exception {
        logger.debug("收到服务器 {} 消息 {}", ctx.channel().remoteAddress(), msg);

        if(msg.length%156 == 0){
            ByteBuf tmp = Unpooled.directBuffer();
            tmp.writeBytes(msg);
            while(tmp.isReadable()){

                ByteBuf quotaBuf = Unpooled.directBuffer(156);
                tmp.readBytes(quotaBuf,156);
                ThirdSanpShot sanpShot = ThirdSanpShot.parseByeBuf(quotaBuf);
                quotaBuf.release();
            }

            tmp.release();   //释放
        }

    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        logger.error("与服务器 {} 连接出现异常, 异常信息 {} ", ctx.channel().remoteAddress(), cause.getMessage());
        messageListener.onException(ctx);
    }
}
