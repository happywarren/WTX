package com.lt.quota.core.comm.netty.client;

import com.lt.quota.core.comm.netty.client.listener.OnStartupListener;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.codec.LengthFieldPrepender;
import io.netty.handler.codec.MessageToMessageDecoder;
import io.netty.handler.codec.MessageToMessageEncoder;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import io.netty.util.CharsetUtil;
import io.netty.util.concurrent.Future;
import io.netty.util.concurrent.GenericFutureListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 发送/接收信息
 * <p>
 * Created by sunch on 2016/11/8.
 */
public class BaseClient implements Runnable {

    private static Logger logger = LoggerFactory.getLogger(BaseClient.class);

    private String host;
    private int port;

    private int reConnectCount = 0;
    private EventLoopGroup eventLoopGroup;
    private OnStartupListener startupListener;
    private MessageToMessageEncoder encoder;
    private MessageToMessageDecoder decoder;
    private ChannelHandler channelHandler;

    private boolean isChannelPrepared;

    public BaseClient(String host, int port, OnStartupListener startupListener) {
        this.host = host;
        this.port = port;
        this.startupListener = startupListener;
    }

    public BaseClient(String host, int port, ChannelHandler channelHandler, OnStartupListener startupListener) {
        this.host = host;
        this.port = port;
        this.channelHandler = channelHandler;
        this.startupListener = startupListener;
    }

    public void setEncoder(MessageToMessageEncoder encoder) {
        this.encoder = encoder;
    }

    public void setDecoder(MessageToMessageDecoder decoder) {
        this.decoder = decoder;
    }


    @Override
    public void run() {
        connect(host, port);
    }

    // 建立连接
    private void connect(String host, int port) {
        if (encoder == null) {
            encoder = new StringEncoder(CharsetUtil.UTF_8);
        }
        if (decoder == null) {
            decoder = new StringDecoder(CharsetUtil.UTF_8);
        }

        eventLoopGroup = new NioEventLoopGroup();
        try {
            Bootstrap b = new Bootstrap();
            b.group(eventLoopGroup);
            b.channel(NioSocketChannel.class);
            b.option(ChannelOption.TCP_NODELAY, true);

            b.handler(new ChannelInitializer<SocketChannel>() {
                @Override
                protected void initChannel(SocketChannel ch) throws Exception {
                    ChannelPipeline p = ch.pipeline();

                    p.addLast(new LengthFieldBasedFrameDecoder(8192, 0, 4, 0, 4));
                    p.addLast(new LengthFieldPrepender(4));
                    p.addLast(encoder);
                    p.addLast(decoder);
                    p.addLast(channelHandler);
                }
            });

            ChannelFuture f = b.connect(host, port).sync();
            f.addListener(new GenericFutureListener<Future<? super Void>>() {
                @Override
                public void operationComplete(Future<? super Void> future) throws Exception {
                    if (future.isSuccess()) {
                        reConnectCount = 0;
                        isChannelPrepared = true;
                        logger.info("连接服务器{}:{}成功...", host, port);
                    } else {
                        isChannelPrepared = false;
                        logger.info("连接服务器{}:{}失败...", host, port);
                    }
                }
            });
            f.channel().closeFuture().sync();
        } catch (Exception e) {
            e.printStackTrace();
            isChannelPrepared = false;
            logger.error("连接服务器{}:{}异常...", this.host, this.port);
        } finally {
            isChannelPrepared = false;
            shutdown();
        }

        if (!isChannelPrepared) {
            reConnect(host, port);
        }
    }

    public void shutdown() {
        if (null != eventLoopGroup) {
            eventLoopGroup.shutdownGracefully().awaitUninterruptibly();
        }
    }

    // 断线重连
    private void reConnect(String host, int port) {
        // fixme: 重连显式退出?
        try {
            isChannelPrepared = false;
            int delay = ++reConnectCount * 5;
            reConnectCount = reConnectCount > 23 ? 23 : reConnectCount;
            logger.error("与服务器{}:{}连接已断开, {}秒后重连...", this.host, this.port, delay);
            if (reConnectCount >= 3) {
                startupListener.onCompletion(false, host, port);
            }
            Thread.sleep(delay * 1000);
            connect(host, port);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
