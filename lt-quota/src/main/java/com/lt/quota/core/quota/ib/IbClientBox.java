package com.lt.quota.core.quota.ib;


import com.lt.quota.core.comm.netty.client.BaseClient;
import io.netty.channel.Channel;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class IbClientBox {

    private final Map<String, IbClientBean> CLIENT_MAP = new ConcurrentHashMap<String, IbClientBean>();

    private final Map<String, Channel> CHANNEL_MAP = new ConcurrentHashMap<String, Channel>();

    private static IbClientBox instance;

    public static synchronized IbClientBox getInstance() {
        if (instance == null) {
            instance = new IbClientBox();
        }
        return instance;
    }


    public void setClient(String ip, BaseClient baseClient, Thread thread) {
        IbClientBean rfClientBean = new IbClientBean(baseClient, thread);
        CLIENT_MAP.put(ip, rfClientBean);
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

    private class IbClientBean {
        private BaseClient baseClient;
        private Thread thread;

        public IbClientBean(BaseClient baseClient, Thread thread) {
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
