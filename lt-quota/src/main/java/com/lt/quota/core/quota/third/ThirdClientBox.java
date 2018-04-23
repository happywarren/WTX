package com.lt.quota.core.quota.third;


import com.lt.quota.core.comm.netty.client.BaseClient;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class ThirdClientBox {

    private final Map<String, ThirdClientBean> CLIENT_MAP = new ConcurrentHashMap<String, ThirdClientBean>();

    private static ThirdClientBox instance;

    public static synchronized ThirdClientBox getInstance() {
        if (instance == null) {
            instance = new ThirdClientBox();
        }
        return instance;
    }


    public void setClient(String ip, ThirdBaseClient baseClient, Thread thread) {
        ThirdClientBean thirdClientBean = new ThirdClientBean(baseClient, thread);
        CLIENT_MAP.put(ip, thirdClientBean);
    }

    public ThirdBaseClient getBaseClient(String key) {
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


    private class ThirdClientBean {
        private ThirdBaseClient baseClient;
        private Thread thread;

        public ThirdClientBean(ThirdBaseClient baseClient, Thread thread) {
            this.baseClient = baseClient;
            this.thread = thread;
        }

        public ThirdBaseClient getBaseClient() {
            return baseClient;
        }

        public void setBaseClient(ThirdBaseClient baseClient) {
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
