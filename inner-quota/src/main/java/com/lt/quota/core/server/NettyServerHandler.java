package com.lt.quota.core.server;

import com.alibaba.fastjson.JSONObject;
import com.lt.quota.core.comm.netty.client.listener.OnMessageListener;
import com.lt.quota.core.constant.IConstants;
import com.lt.quota.core.utils.Utils;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
@ChannelHandler.Sharable
public class NettyServerHandler extends SimpleChannelInboundHandler<String> {

    private Logger logger = LoggerFactory.getLogger(NettyServerHandler.class);



    @Autowired
    private OnMessageListener nettyServerListener;


    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        nettyServerListener.onActive(ctx);
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, String msg) throws Exception {
        if(Utils.isEmpty(msg))
            return;
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
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        nettyServerListener.onException(ctx);
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        nettyServerListener.onInactive(ctx);
    }
}
