package com.lt.tms.tcp.server.handler;

import com.alibaba.fastjson.JSONObject;
import com.lt.tms.comm.constant.IConstants;
import com.lt.tms.comm.netty.server.listener.MessageListener;
import com.lt.tms.utils.Utils;
import io.netty.channel.ChannelHandler.Sharable;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Sharable
@Component
public class NettyServerHandler extends SimpleChannelInboundHandler<String> {

    private Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private MessageListener nettyServerListener;

    @Override
    public void channelRegistered(ChannelHandlerContext ctx) throws Exception {
        super.channelRegistered(ctx);
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        nettyServerListener.onActive(ctx);
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        nettyServerListener.onInactive(ctx);
    }

    @Override
    public void channelRead0(ChannelHandlerContext ctx, String msg) throws Exception {
        logger.info("服务器 {} 收到[client] {} 数据: {} ", IConstants.SERVER_IP, ctx.channel().remoteAddress(), msg);
        JSONObject myJson = JSONObject.parseObject(msg);
        String cmd = myJson.getString("cmd");
        if (Utils.isEmpty(cmd)){
            cmd = myJson.getString("CMDID");
        }
        //心跳
        if (cmd.equals(IConstants.REQ_PING) || cmd.equals(IConstants.RES_PING)) {
            nettyServerListener.onPing(ctx, msg);
        } else {
            nettyServerListener.onMessage(ctx, msg);
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        cause.printStackTrace();//捕捉异常信息
        nettyServerListener.onException(ctx);
    }
}
