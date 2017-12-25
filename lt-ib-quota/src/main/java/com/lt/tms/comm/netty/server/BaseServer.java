package com.lt.tms.comm.netty.server;

import com.lt.tms.comm.constant.IConstants;
import com.lt.tms.comm.netty.server.listener.MessageListener;
import com.lt.tms.comm.netty.server.listener.StartupListener;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.PooledByteBufAllocator;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
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

import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * 作者: 邓玉明
 * 时间: 2017/5/3 下午3:58
 * email:cndym@163.com
 */
public class BaseServer implements Runnable {

    private Logger logger = LoggerFactory.getLogger(getClass());

    private int port;
    private boolean soKeepAlive = true;
    private boolean tcpNodelay = true;
    private int connectTimeout = 3000;

    private volatile ServerBootstrap serverBootstrap;
    private volatile EventLoopGroup bossGroup;
    private volatile EventLoopGroup workerGroup;
    private volatile boolean serverRun;
    private volatile boolean closed = false;

    private StartupListener startupListener;

    private MessageToMessageEncoder encoder;
    private MessageToMessageDecoder decoder;
    private ChannelHandler channelHandler;


    public BaseServer(int port, StartupListener startupListener, ChannelHandler channelHandler) {
        this.port = port;
        this.startupListener = startupListener;
        this.channelHandler = channelHandler;
    }


    public boolean isSoKeepAlive() {
        return soKeepAlive;
    }

    public void setSoKeepAlive(boolean soKeepAlive) {
        this.soKeepAlive = soKeepAlive;
    }

    public boolean isTcpNodelay() {
        return tcpNodelay;
    }

    public void setTcpNodelay(boolean tcpNodelay) {
        this.tcpNodelay = tcpNodelay;
    }

    public int getConnectTimeout() {
        return connectTimeout;
    }

    public void setConnectTimeout(int connectTimeout) {
        this.connectTimeout = connectTimeout;
    }

    public void setServerRun(boolean serverRun) {
        this.serverRun = serverRun;
    }

    public MessageToMessageEncoder getEncoder() {
        return encoder;
    }

    public void setEncoder(MessageToMessageEncoder encoder) {
        this.encoder = encoder;
    }

    public MessageToMessageDecoder getDecoder() {
        return decoder;
    }

    public void setDecoder(MessageToMessageDecoder decoder) {
        this.decoder = decoder;
    }


    public void run() {
        if (encoder == null) {
            encoder = new StringEncoder(CharsetUtil.UTF_8);
        }
        if (decoder == null) {
            decoder = new StringDecoder(CharsetUtil.UTF_8);
        }
        logger.info("netty server start");
        bossGroup = new NioEventLoopGroup(1);
        workerGroup = new NioEventLoopGroup(4);
        try {
            serverBootstrap = new ServerBootstrap();
            serverBootstrap.group(bossGroup, workerGroup);

            serverBootstrap.channel(NioServerSocketChannel.class);
            serverBootstrap.option(ChannelOption.SO_BACKLOG, 1024);
            serverBootstrap.option(ChannelOption.CONNECT_TIMEOUT_MILLIS, connectTimeout);
            //允许重复使用本地地址和端口
            serverBootstrap.option(ChannelOption.SO_REUSEADDR, true);

            //长连接
            serverBootstrap.childOption(ChannelOption.SO_KEEPALIVE, soKeepAlive);
            //接收缓冲区和发送缓冲区的大小
            serverBootstrap.childOption(ChannelOption.SO_SNDBUF, 1024 * 64);
            serverBootstrap.childOption(ChannelOption.SO_RCVBUF, 1024 * 64);
            // 使用内存池的缓冲区重用机制
            serverBootstrap.childOption(ChannelOption.ALLOCATOR, PooledByteBufAllocator.DEFAULT);
            serverBootstrap.childOption(ChannelOption.RCVBUF_ALLOCATOR, new AdaptiveRecvByteBufAllocator());

            //通过NoDelay禁用Nagle,使消息立即发出去，不用等待到一定的数据量才发出去
            serverBootstrap.childOption(ChannelOption.TCP_NODELAY, tcpNodelay);
            serverBootstrap.childHandler(new ChannelInitializer<SocketChannel>() {
                @Override
                protected void initChannel(SocketChannel ch) throws Exception {
                    ChannelPipeline p = ch.pipeline();
                    p.addLast(new LengthFieldBasedFrameDecoder(8192, 0, 4, 0, 4));
                    p.addLast(new LengthFieldPrepender(4));
                    p.addLast(decoder);
                    p.addLast(encoder);
                    if (null != channelHandler) {
                        p.addLast(channelHandler);
                    }
                }
            });
            bind();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            close();
        }
    }

    private void bind() {
        if (closed) {
            return;
        }
        try {
            // 服务器绑定端口监听
            ChannelFuture channelFuture = serverBootstrap.bind(port).sync();
            channelFuture.addListener(new GenericFutureListener<Future<? super Void>>() {
                @Override
                public void operationComplete(Future<? super Void> future) throws Exception {
                    if (future.isSuccess()) {
                        serverRun = true;
                        startupListener.onCompletion(true);
                        logger.info("与服务器{}:{}连接建立成功...", IConstants.SERVER_IP, port);
                    } else {
                        serverRun = false;
                        channelFuture.channel().eventLoop().schedule(() -> bind(), 1, TimeUnit.SECONDS);
                        startupListener.onCompletion(false);
                        logger.info("与服务器{}:{}连接建立失败...", IConstants.SERVER_IP, port);
                    }
                }
            });
            //应用程序会一直等待，直到channel关闭
            channelFuture.channel().closeFuture().sync();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            close();
        }
    }

    public void close() {
        closed = true;
        if (bossGroup != null) {
            bossGroup.shutdownGracefully();
        }
        if (workerGroup != null) {
            workerGroup.shutdownGracefully();
        }
    }

    public boolean isServerRun() {
        return serverRun;
    }
}
