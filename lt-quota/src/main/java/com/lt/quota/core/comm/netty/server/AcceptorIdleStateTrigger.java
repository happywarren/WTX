package com.lt.quota.core.comm.netty.server;

import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@ChannelHandler.Sharable
public class AcceptorIdleStateTrigger extends ChannelInboundHandlerAdapter {

    private Logger logger = LoggerFactory.getLogger(getClass());

    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        if (evt instanceof IdleStateEvent) {
            IdleState state = ((IdleStateEvent) evt).state();
            if (state == IdleState.READER_IDLE) {
                logger.info("客户端 {} 读超时，关闭连接 ", ctx.channel().remoteAddress());
                ctx.close();
            } else if (state == IdleState.WRITER_IDLE) {
                logger.info("客户端 {} 写超时，关闭连接 ", ctx.channel().remoteAddress());
                ctx.close();
            } else if (state == IdleState.ALL_IDLE) {
                logger.info("客户端 {} 读写超时，关闭连接 ", ctx.channel().remoteAddress());
                ctx.close();
            }
        } else {
            logger.info("AcceptorIdleStateTrigger userEventTriggered {} ", ctx.channel().remoteAddress());
            super.userEventTriggered(ctx, evt);
        }
    }
}