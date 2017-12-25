package com.lt.quota.core.server;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.lt.quota.core.comm.bean.response.PingResponse;
import com.lt.quota.core.comm.netty.client.listener.OnMessageListener;
import com.lt.quota.core.constant.IConstants;
import com.lt.quota.core.utils.FastJsonUtils;
import com.lt.quota.core.utils.IpUtils;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.net.InetSocketAddress;
import java.util.HashMap;
import java.util.Map;


@Component
public class NettyServerListener implements OnMessageListener {

    private Logger logger  = LoggerFactory.getLogger(NettyServerListener.class);

    @Override
    public void onMessage(ChannelHandlerContext ctx, String msg) {

    }

    @Override
    public void onActive(ChannelHandlerContext ctx) {
        Channel channel = ctx.channel();
        InetSocketAddress socketAddress = (InetSocketAddress) channel.remoteAddress();
        String hostAddress = socketAddress.getAddress().getHostAddress();
        int port = socketAddress.getPort();
        NewClientOperator.getInstance().addChannel(hostAddress+":"+port,channel);
        logger.info("[client] - {} : {} 连入 {}", hostAddress, port, IpUtils.getRealIp());
    }

    @Override
    public void onInactive(ChannelHandlerContext ctx) {
        Channel channel = ctx.channel();
        InetSocketAddress socketAddress = (InetSocketAddress) channel.remoteAddress();
        String hostAddress = socketAddress.getAddress().getHostAddress();
        int port = socketAddress.getPort();
        NewClientOperator.getInstance().removeChannel(hostAddress+":"+port);
        logger.info("与{}:{} 断开连接",hostAddress,port);
    }

    @Override
    public void onException(ChannelHandlerContext ctx) {
        Channel channel = ctx.channel();
        InetSocketAddress socketAddress = (InetSocketAddress) channel.remoteAddress();
        String hostAddress = socketAddress.getAddress().getHostAddress();
        int port = socketAddress.getPort();
        logger.info("[client] - {} : {} 异常 {}", hostAddress, port, ctx.channel().remoteAddress());
        //newClientOperator.exceptionQuit(channel);
        ctx.close();
    }

    @Override
    public void onPing(ChannelHandlerContext ctx, String msg) {
        PingResponse pingResponse = new PingResponse();
        JSONObject myJson = JSONObject.parseObject(msg);
        String DATA = myJson.getString("DATA");
        myJson = JSON.parseObject(DATA);
        long lastTime = myJson.getLong("timeStamp");
        pingResponse.setTimeStamp(lastTime);
        Map<String, Object> map = new HashMap<>();
        map.put("CMD", IConstants.RES_PING);
        map.put("DATA", pingResponse);
        String responseMessage = FastJsonUtils.toJson(map);
        Channel channel = ctx.channel();
        channel.writeAndFlush(responseMessage);
    }
}
