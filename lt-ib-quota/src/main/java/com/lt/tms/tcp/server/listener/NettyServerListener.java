package com.lt.tms.tcp.server.listener;


import com.alibaba.fastjson.JSONObject;
import com.lt.tms.comm.constant.IConstants;
import com.lt.tms.comm.json.FastJsonUtils;
import com.lt.tms.comm.netty.server.listener.MessageListener;
import com.lt.tms.comm.spring.SpringUtils;
import com.lt.tms.operator.IOperator;
import com.lt.tms.tcp.server.TcpClient;
import com.lt.tms.tcp.server.response.BaseResponse;
import com.lt.tms.tcp.server.response.PingResponse;
import com.lt.tms.utils.IpUtils;
import com.lt.tms.utils.Utils;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.util.concurrent.GlobalEventExecutor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.net.InetSocketAddress;

@Component
public class NettyServerListener implements MessageListener {

    private Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private TcpClient tcpClient;

    @Override
    public void onActive(ChannelHandlerContext ctx) {
        Channel channel = ctx.channel();
        InetSocketAddress socketAddress = (InetSocketAddress) channel.remoteAddress();
        String hostAddress = socketAddress.getAddress().getHostAddress();
        int port = socketAddress.getPort();
        logger.info("[client] - {} : {} 连入 {}", hostAddress, port, IpUtils.getRealIp());
        tcpClient.addChannel(channel);
    }

    @Override
    public void onInactive(ChannelHandlerContext ctx) {
        Channel channel = ctx.channel();
        InetSocketAddress socketAddress = (InetSocketAddress) channel.remoteAddress();
        String hostAddress = socketAddress.getAddress().getHostAddress();
        int port = socketAddress.getPort();
        logger.info("[client] - {} : {} 离开 {}", hostAddress, port, IConstants.SERVER_IP);
        tcpClient.removeChannel(channel);
        ctx.close();
    }

    @Override
    public void onException(ChannelHandlerContext ctx) {
        Channel channel = ctx.channel();
        InetSocketAddress socketAddress = (InetSocketAddress) channel.remoteAddress();
        String hostAddress = socketAddress.getAddress().getHostAddress();
        int port = socketAddress.getPort();
        logger.info("[client] - {} : {} 异常 {}", hostAddress, port, IConstants.SERVER_IP);
        tcpClient.removeChannel(channel);
        channel.close();
    }

    @Override
    public void onPing(ChannelHandlerContext ctx, String msg) {
        PingResponse pingResponse = new PingResponse();
        BaseResponse<PingResponse> baseResponse = new BaseResponse<PingResponse>();
        baseResponse.setCmd(IConstants.RES_PING);
        baseResponse.setData(pingResponse);
        String responseMessage = FastJsonUtils.toJson(baseResponse);
        Channel channel = ctx.channel();
        channel.writeAndFlush(responseMessage);
    }

    @Override
    public void onMessage(ChannelHandlerContext ctx, String msg) {
        JSONObject myJson = JSONObject.parseObject(msg);
        String cmd = myJson.getString("cmd");
        if (Utils.isEmpty(cmd)) {
            return;
        }
        IOperator operator = SpringUtils.getBean("operator" + cmd, IOperator.class);
        operator.operator(ctx,myJson.getJSONArray("data").toJSONString());
    }
}
