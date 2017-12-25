package com.lt.quota.core.quota.inner;


import com.lt.quota.core.comm.netty.client.BaseClient;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import org.omg.CORBA.PUBLIC_MEMBER;

import java.net.SocketAddress;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class InnerClientBox {

    private final Map<String, InnerClientBean> CLIENT_MAP = new ConcurrentHashMap<String, InnerClientBean>();

    private final Map<String, Channel> CHANNEL_MAP = new ConcurrentHashMap<String, Channel>();

    private static InnerClientBox instance;

    public static synchronized InnerClientBox getInstance() {
        if (instance == null) {
            instance = new InnerClientBox();
        }
        return instance;
    }



    public void setClient(String ip, BaseClient baseClient, Thread thread) {
        InnerClientBean innerClientBean = new InnerClientBean(baseClient, thread);
        CLIENT_MAP.put(ip, innerClientBean);
    }

    public BaseClient getBaseClient(String key) {
        if (CLIENT_MAP.containsKey(key)) {
            return CLIENT_MAP.get(key).getBaseClient();
        }
        return null;
    }

    public Thread getThread(String key) {
        if (CLIENT_MAP.containsKey(key)) {
            return CLIENT_MAP.get(key).getThread();
        }
        return null;
    }

    public void removeClient(String key) {
        if (CLIENT_MAP.containsKey(key)) {
            CLIENT_MAP.remove(key);
        }
    }

    public boolean contains(String key) {
        if (CLIENT_MAP.containsKey(key)) {
            return true;
        }
        return false;
    }


    public void addChannel(ChannelHandlerContext ctx) {
        Channel channel = ctx.channel();
        SocketAddress socketAddress = channel.remoteAddress();
        String key = socketAddress.toString().replace("/", "");
        CHANNEL_MAP.put(key, channel);
    }

    public void removeChannel(ChannelHandlerContext ctx) {
        Channel channel = ctx.channel();
        SocketAddress socketAddress = channel.remoteAddress();
        String key = socketAddress.toString().replace("/", "");
        CHANNEL_MAP.remove(key);
    }

    public Channel getChannel(String key){
        return CHANNEL_MAP.get(key);
    }

    public Map<String,Channel> getChannelMap(){
        return CHANNEL_MAP;
    }

    private class InnerClientBean {
        private BaseClient baseClient;
        private Thread thread;

        public InnerClientBean(BaseClient baseClient, Thread thread) {
            this.baseClient = baseClient;
            this.thread = thread;
        }

        public BaseClient getBaseClient() {
            return baseClient;
        }

        public void setBaseClient(BaseClient baseClient) {
            this.baseClient = baseClient;
        }

        public Thread getThread() {
            return thread;
        }

        public void setThread(Thread thread) {
            this.thread = thread;
        }
    }
}
