package com.lt.quota.core.appServer.handler;

import com.alibaba.fastjson.JSONObject;
import com.lt.quota.core.comm.netty.server.listener.OnMessageListener;
import com.lt.quota.core.constant.IConstants;
import com.lt.quota.core.utils.Utils;
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
    private OnMessageListener nettyServerListener;

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
        if (Utils.isEmpty(msg)) {
            return;
        }
        JSONObject myJson = JSONObject.parseObject(msg);
        String cmd = "";
        if (myJson.getString("CMDID") != null) {
            cmd = myJson.getString("CMDID");
        }

        if (myJson.getString("CMD") != null) {
            cmd = myJson.getString("CMD");
        }
        if (Utils.isEmpty(cmd)) {
            return;
        }
        //心跳
        if (cmd.equals(IConstants.REQ_PING)) {
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
