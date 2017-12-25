package com.lt.quota.core.appServer.listener;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.lt.quota.core.appServer.bean.response.PingResponse;
import com.lt.quota.core.appServer.newer.NewChannelManger;
import com.lt.quota.core.appServer.newer.NewClientOperator;
import com.lt.quota.core.comm.netty.server.listener.OnMessageListener;
import com.lt.quota.core.constant.IConstants;
import com.lt.quota.core.utils.FastJsonUtils;
import com.lt.quota.core.utils.IpUtils;
import com.lt.quota.core.utils.Utils;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.net.InetSocketAddress;
import java.util.HashMap;
import java.util.Map;

@Component
public class NettyServerListener implements OnMessageListener {

    @Autowired
    private NewClientOperator newClientOperator;

    private Logger logger = LoggerFactory.getLogger(getClass());

    @Override
    public void onActive(ChannelHandlerContext ctx) {
        Channel channel = ctx.channel();
        InetSocketAddress socketAddress = (InetSocketAddress) channel.remoteAddress();
        String hostAddress = socketAddress.getAddress().getHostAddress();
        int port = socketAddress.getPort();

        logger.info("[client] - {} : {} 连入 {}", hostAddress, port, IpUtils.getRealIp());
    }

    @Override
    public void onInactive(ChannelHandlerContext ctx) {
        Channel channel = ctx.channel();
        InetSocketAddress socketAddress = (InetSocketAddress) channel.remoteAddress();
        String hostAddress = socketAddress.getAddress().getHostAddress();
        int port = socketAddress.getPort();
        logger.info("[client] - {} : {} 离开 {}", hostAddress, port, IConstants.SERVER_IP);
        newClientOperator.exceptionQuit(channel);
        ctx.close();
    }

    @Override
    public void onPing(ChannelHandlerContext ctx, String message) {
        PingResponse pingResponse = new PingResponse();
        JSONObject myJson = JSONObject.parseObject(message);
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

    @Override
    public void onException(ChannelHandlerContext ctx) {
        Channel channel = ctx.channel();
        InetSocketAddress socketAddress = (InetSocketAddress) channel.remoteAddress();
        String hostAddress = socketAddress.getAddress().getHostAddress();
        int port = socketAddress.getPort();
        logger.info("[client] - {} : {} 异常 {}", hostAddress, port, IConstants.SERVER_IP);
        newClientOperator.exceptionQuit(channel);
        ctx.close();
    }

    @Override
    public void onMessage(ChannelHandlerContext ctx, String msg) {
        Channel channel = ctx.channel();
        InetSocketAddress socketAddress = (InetSocketAddress) channel.remoteAddress();
        String hostAddress = socketAddress.getAddress().getHostAddress();
        int port = socketAddress.getPort();
        logger.info("[client] - {} : {} 消息 {}", hostAddress, port, msg);
        try {
            JSONObject myJson = JSONObject.parseObject(msg);
            String cmdId = "";
            String token = null;
            if (myJson.getString("CMDID") != null) {
                cmdId = myJson.getString("CMDID");
            }

            if (myJson.getString("CMD") != null) {
                cmdId = myJson.getString("CMD");
            }

            if (!Utils.isNotEmpty(cmdId)) {
                return;
            }

            if (myJson.getString("token") != null) {
                token = myJson.getString("token");
            }

            if (!Utils.isNotEmpty(token)
                    && !cmdId.equals(IConstants.REQ_MKT_ALL)) {
                return;
            }

            String data = myJson.getString("DATA");

            boolean flag = false;

            switch (cmdId) {
                case IConstants.REQ_AUTH:
                    flag = newClientOperator.login(token, data, channel);
                    break;
                case IConstants.REQ_MKT_DATA:
                    flag = newClientOperator.subscribe(token, data, channel);
                    break;
                case IConstants.REQ_CANCEL_MKT_DATA:
                    flag = newClientOperator.cancelSubscribe(token, data);
                    break;
                case IConstants.REQ_LOGOUT:
                    flag = newClientOperator.quit(token, data, channel);
                    break;
                case IConstants.REQ_MKT_ALL:
                    NewChannelManger.getInstance().setChannelGroup(channel);
                    flag = true;
                    break;
               default:
                    break;
            }

            logger.info("onMessage operator flag: {} ", flag);

            if (!flag) {
                channel.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void close(ChannelHandlerContext ctx) {
        ctx.close();
    }
}
