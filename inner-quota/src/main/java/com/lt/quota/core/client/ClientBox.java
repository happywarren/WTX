package com.lt.quota.core.client;

import com.lt.quota.core.comm.netty.client.BaseClient;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class ClientBox {

    private static Map<String,ClientBoxBean> CLIENT_MAP = new ConcurrentHashMap<String,ClientBoxBean>();


    private static ClientBox instance;

    public static synchronized  ClientBox getInstance(){
        if(instance == null){
            instance = new ClientBox();
        }
        return instance;
    }

    public void setClient(String ip,BaseClient baseClient,Thread thread){
        ClientBoxBean clientBoxBean =new ClientBoxBean(baseClient,thread);
        CLIENT_MAP.put(ip,clientBoxBean);
    }

    public BaseClient getBaseClient(String key){
        if(CLIENT_MAP.containsKey(key)){
            return CLIENT_MAP.get(key).getBaseClient();
        }
        return null;
    }

    public Thread getThread(String key ){
        if(CLIENT_MAP.containsKey(key)){
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

    private class ClientBoxBean{
        private BaseClient baseClient;
        private Thread thread;

        public ClientBoxBean(BaseClient baseClient, Thread thread) {
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
