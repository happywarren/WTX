package com.lt.quota.core.quota.ib.listener;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.lt.quota.core.comm.netty.client.listener.OnMessageListener;
import com.lt.quota.core.quota.bean.QuotaBean;
import com.lt.quota.core.quota.clean.CleanInstance;
import com.lt.quota.core.quota.ib.change.IbQuotaDataChange;
import com.lt.quota.core.utils.Utils;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.EventLoop;
import io.netty.util.concurrent.ScheduledFuture;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.SocketAddress;
import java.util.concurrent.TimeUnit;

public class IbClientListener implements OnMessageListener {

    private Logger logger = LoggerFactory.getLogger(getClass());

    private volatile ScheduledFuture<?> heartBeatScheduler;

    private volatile ChannelHandlerContext handlerContext;

    private static final int HEART_BEAT_PERIOD = 5;

    @Override
    public void onMessage(ChannelHandlerContext ctx, String msg) {
        SocketAddress socketAddress = ctx.channel().remoteAddress();
        logger.debug("收到IB服务器 {} 行情: {} ", socketAddress, msg);
        JSONObject jsonData = JSON.parseObject(msg);
        String cmd = jsonData.getString("cmd");
        //处理行情数据
        if ("1005".equals(cmd)) {
            String data = jsonData.getString("data");
            QuotaBean quotaBean = IbQuotaDataChange.getInstance().change(data);
            if (!Utils.isNotEmpty(quotaBean)) {
                return;
            }
            quotaBean.setSource("IB" + socketAddress.toString());
            quotaBean.setPlate(1);
            //转换
            CleanInstance.getInstance().setMarketDataQueue(quotaBean);
        }
    }

    @Override
    public void onActive(ChannelHandlerContext ctx) {
        handlerContext = ctx;
        // 连接建立成功, 主动发送心跳消息
        final EventLoop loop = ctx.channel().eventLoop();
        heartBeatScheduler = loop.scheduleAtFixedRate(new HeartBeatRunnable(handlerContext), 0, HEART_BEAT_PERIOD, TimeUnit.SECONDS);
    }

    @Override
    public void onInactive(ChannelHandlerContext ctx) {
        handlerContext = ctx;
        ctx.close();
    }

    @Override
    public void onException(ChannelHandlerContext ctx) {
        ctx.close();
    }
}
