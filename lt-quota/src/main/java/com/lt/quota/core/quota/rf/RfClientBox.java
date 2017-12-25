package com.lt.quota.core.quota.rf;


import com.lt.quota.core.comm.netty.client.BaseClient;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class RfClientBox {

    private final Map<String, RfClientBean> CLIENT_MAP = new ConcurrentHashMap<String, RfClientBean>();

    private static RfClientBox instance;

    public static synchronized RfClientBox getInstance() {
        if (instance == null) {
            instance = new RfClientBox();
        }
        return instance;
    }


    public void setClient(String ip, BaseClient baseClient, Thread thread) {
        RfClientBean rfClientBean = new RfClientBean(baseClient, thread);
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


    private class RfClientBean {
        private BaseClient baseClient;
        private Thread thread;

        public RfClientBean(BaseClient baseClient, Thread thread) {
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
