package com.lt.quota.core.quota.inner.listener;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.lt.quota.core.comm.netty.client.listener.OnMessageListener;
import com.lt.quota.core.quota.bean.QuotaBean;
import com.lt.quota.core.quota.clean.CleanInstance;
import com.lt.quota.core.quota.inner.InnerClientBox;
import com.lt.quota.core.quota.inner.operator.InnerProductOperator;
import com.lt.quota.core.utils.DateUtils;
import com.lt.quota.core.utils.NumberUtil;
import com.lt.quota.core.utils.Utils;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.EventLoop;
import io.netty.util.concurrent.ScheduledFuture;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.net.SocketAddress;
import java.util.Date;
import java.util.concurrent.TimeUnit;

public class InnerClientListener implements OnMessageListener {

    private Logger logger = LoggerFactory.getLogger(getClass());

    private volatile ScheduledFuture<?> heartBeatScheduler;

    private volatile ScheduledFuture<?> productScheduler;

    private volatile ChannelHandlerContext handlerContext;

    private static final int HEART_BEAT_PERIOD = 5;

    @Override
    public void onMessage(ChannelHandlerContext ctx, String msg) {
        SocketAddress socketAddress = ctx.channel().remoteAddress();
        logger.debug("收到Inner服务器 {} message: {} ", ctx.channel().remoteAddress(), msg);
        JSONObject jsonData = JSON.parseObject(msg);
        String cmd = jsonData.getString("CMDID");
        //处理行情数据
        if ("1100".equals(cmd)) {
            String data = jsonData.getString("DATA");
            QuotaBean quotaBean = JSON.parseObject(data, QuotaBean.class);
            String askQty1 = quotaBean.getAskQty1();
            String bidQty1 = quotaBean.getBidQty1();

            quotaBean.setProductName(quotaBean.getProductName());
            quotaBean.setBidQty1(askQty1);
            quotaBean.setAskQty1(bidQty1);
            quotaBean.setSource("INNER" + socketAddress.toString());
            String changeRateStr = quotaBean.getChangeRate();
            BigDecimal changeRate = NumberUtil.formatBigDecimal(new BigDecimal(changeRateStr), 2);
            quotaBean.setChangeRate(NumberUtil.toStr(changeRate));
            quotaBean.setPlate(0);
            Date timestamp = DateUtils.formatDate(quotaBean.getTimeStamp(), "yyyyMMdd HH:mm:ss.SSS");
            quotaBean.setTimeStamp(DateUtils.formatDate2Str(timestamp, "yyyy-MM-dd HH:mm:ss.SSS"));
            CleanInstance.getInstance().setMarketDataQueue(quotaBean);
        } else if ("1111".equals(cmd)) {
            Channel channel = ctx.channel();
            String message = InnerProductOperator.getInstance().productPacket();
            if (Utils.isNotEmpty(message)) {
                logger.info("通知内盘服务器 {} 订阅行情 {} ", channel.remoteAddress(), message);
                channel.writeAndFlush(message);
            }
            String preMessage = InnerProductOperator.getInstance().preProductPacket();
            if (Utils.isNotEmpty(preMessage)) {
                logger.info("通知内盘服务器 {} 预定行情 {} ", channel.remoteAddress(), preMessage);
                channel.writeAndFlush(preMessage);
            }
        }
    }

    @Override
    public void onActive(ChannelHandlerContext ctx) {
        handlerContext = ctx;
        InnerClientBox.getInstance().addChannel(ctx);
        final EventLoop loop = ctx.channel().eventLoop();
        heartBeatScheduler = loop.scheduleAtFixedRate(new HeartBeatRunnable(handlerContext), 0, HEART_BEAT_PERIOD, TimeUnit.SECONDS);
        //定时发送 订阅 退订合约信息
        productScheduler = loop.scheduleAtFixedRate(new ProductInfoRunnable(handlerContext), 0, 1, TimeUnit.HOURS);
        try {
            Thread.sleep(10000);
            //立即订阅
            String message = InnerProductOperator.getInstance().productPacket();
            logger.info("发送立即订阅信息: {} ", message);
            if (Utils.isNotEmpty(message)) {
                ctx.writeAndFlush(message);
            }
        } catch (Exception e) {
        }

    }

    @Override
    public void onInactive(ChannelHandlerContext ctx) {
        handlerContext = ctx;
        InnerClientBox.getInstance().removeChannel(ctx);
        ctx.close();
    }

    @Override
    public void onException(ChannelHandlerContext ctx) {
        ctx.close();
    }
}
