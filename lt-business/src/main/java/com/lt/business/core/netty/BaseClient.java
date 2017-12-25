package com.lt.business.core.netty;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.codec.LengthFieldPrepender;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import io.netty.util.CharsetUtil;
import io.netty.util.concurrent.Future;
import io.netty.util.concurrent.GenericFutureListener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 发送/接收信息(心跳/行情/订单状态/成交信息等)
 *
 * Created by sunch on 2016/11/8.
 */
public class BaseClient implements Runnable {

    private static Logger LOGGER = LoggerFactory.getLogger(BaseClient.class);

    private String host_;
    private int port_;
    private int reConnectCount_ = 0;
    private ClientHandler clientHandler_;
    private OnMessageListener messageListener_;
    private OnStartupListener startupListener_;
    private volatile boolean isChannelPrepared_;

    private final static int MAX_MESSAGE_LENGTH = 8192;

    public BaseClient(String host, int port, OnMessageListener messageListener, OnStartupListener startupListener) {
        host_ = host;
        port_ = port;
        messageListener_ = messageListener;
        startupListener_ = startupListener;
    }

    @Override
    public void run() {
        connect(host_, port_);
    }

    // 发送消息
    public void sendMessage(String msg, ResultListener<String> listener) {
        if (isChannelPrepared_) {
            clientHandler_.sendMessage(msg, listener);
        } else {
            listener.onFailure(msg);
            LOGGER.error("连接还未建立, 无法发送数据...");
        }
    }

    // 建立连接
    private void connect(String host, int port) {
        EventLoopGroup group = new NioEventLoopGroup();

        try {
            Bootstrap b = new Bootstrap();
            b.group(group);
            b.channel(NioSocketChannel.class);
            b.option(ChannelOption.TCP_NODELAY, true);
            b.handler(new ChannelInitializer<SocketChannel>() {
                @Override
                protected void initChannel(SocketChannel ch) throws Exception {
                    ChannelPipeline p = ch.pipeline();

                    p.addLast(new LengthFieldBasedFrameDecoder(MAX_MESSAGE_LENGTH, 0, 4, 0 ,4));
                    p.addLast(new LengthFieldPrepender(4));
                    p.addLast(new StringDecoder(CharsetUtil.UTF_8));
                    p.addLast(new StringEncoder(CharsetUtil.UTF_8));

                    clientHandler_ = new ClientHandler(messageListener_);
                    p.addLast(clientHandler_);
                }
            });

            ChannelFuture f = b.connect(host, port).sync();
            f.addListener(new GenericFutureListener<Future<? super Void>>() {
                @Override
                public void operationComplete(Future<? super Void> future) throws Exception {
                    if (future.isSuccess()) {
                        reConnectCount_ = 0;
                        isChannelPrepared_ = true;
                        startupListener_.onCompletion(true);
                        LOGGER.info("与服务器{}:{}连接建立成功...", host_, port_);
                    } else {
                        isChannelPrepared_ = false;
                        startupListener_.onCompletion(false);
                        LOGGER.info("与服务器{}:{}连接建立失败...", host_, port_);
                    }
                }
            });

            f.channel().closeFuture().sync();
        } catch (Exception e) {
            isChannelPrepared_ = false;
            LOGGER.error("与服务器{}:{}连接出现异常...", host_, port_);
        } finally {
            isChannelPrepared_ = false;
            group.shutdownGracefully();
            reConnect(host, port);
        }
    }

    // 断线重连
    private void reConnect(String host, int port) {
        // fixme: 重连显式退出?
        try {
            isChannelPrepared_ = false;
            int delay = ++reConnectCount_ * 5;
            reConnectCount_ = reConnectCount_ > 23 ? 23 : reConnectCount_;
            LOGGER.error("与服务器{}:{}连接已断开, {}秒后重连...", host_, port_, delay);

            Thread.sleep(delay * 1000);
            connect(host, port);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
