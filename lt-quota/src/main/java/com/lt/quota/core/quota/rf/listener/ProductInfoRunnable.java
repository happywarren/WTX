package com.lt.quota.core.quota.rf.listener;

import com.lt.quota.core.quota.rf.operator.OuterProductOperator;
import com.lt.quota.core.utils.Utils;
import io.netty.channel.ChannelHandlerContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

//合约信息发送
public class ProductInfoRunnable implements Runnable {

    private Logger logger = LoggerFactory.getLogger(getClass());

    private ChannelHandlerContext ctx;

    public ProductInfoRunnable(final ChannelHandlerContext ctx) {
        this.ctx = ctx;
    }

    @Override
    public void run() {

        String preMessage = OuterProductOperator.getInstance().preProductPacket();
        if (Utils.isNotEmpty(preMessage)) {
            logger.info("通知服务器 {} 订阅行情 {} ", ctx.channel().remoteAddress(), preMessage);
            ctx.writeAndFlush(preMessage);
        }

        String expireMessage = OuterProductOperator.getInstance().expireProductPacket();
        if (Utils.isNotEmpty(expireMessage)) {
            logger.info("通知服务器 {} 退订行情 {} ", ctx.channel().remoteAddress(), expireMessage);
            ctx.writeAndFlush(expireMessage);
        }
    }


}