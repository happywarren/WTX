package com.lt.quota.core.server;

import com.lt.quota.core.utils.FastJsonUtils;
import com.lt.tradeclient.mdata.model.QuotaBean;
import io.netty.channel.Channel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.*;

public class NewClientOperator {

    private Logger logger  = LoggerFactory.getLogger(NewClientOperator.class);

    private static NewClientOperator instance = null;

    /*
     * 通道ip:port和channel对应
     */
    private java.util.Map<String,Channel> channelMap = new ConcurrentHashMap<String,Channel>();

    /**
     * ip:port和订阅的产品代码
     */
    private java.util.Map<String,Set<String>> channelProduct= new ConcurrentHashMap<String,Set<String>>();

    private ExecutorService executorService = Executors.newFixedThreadPool(5);

    private NewClientOperator(){
    }

    public static synchronized  NewClientOperator getInstance(){
        if(instance == null){
            instance = new NewClientOperator();
        }
        return instance;
    }


    public void addChannel(String hostPort,Channel channel){
        if(!channelMap.containsKey(hostPort)){
            channelMap.put(hostPort,channel);
            channelProduct.put(hostPort,new HashSet<>());
        }

    }

    public void removeChannel(String hostPort){
        if(!channelMap.containsKey(hostPort)){
            channelMap.remove(hostPort);
            channelProduct.remove(hostPort);
        }
    }

    public void subscribeProductCode(String hostPort,Set<String> productCodes){
        if(channelMap.containsKey(hostPort)){
            Set<String> pcs =  channelProduct.get(hostPort);
            if(pcs == null){
                pcs = new HashSet<String>();
            }
            pcs.addAll(productCodes);
            channelProduct.put(hostPort,pcs);
        }
    }

    public void sendQuota(QuotaBean quotaBean){
        executorService.execute(new Runnable() {
            @Override
            public void run() {
                for(String key : channelMap.keySet()){
                    Channel channel = channelMap.get(key);
                    Set<String> pcs = channelProduct.get(key);
                    if(pcs == null || pcs.size() == 0){
                        channel.writeAndFlush(FastJsonUtils.toJson(quotaBean));
                    }else{
                        if(pcs.contains(quotaBean.getProductName())){
                            channel.writeAndFlush(FastJsonUtils.toJson(quotaBean));
                        }
                    }
                }
            }
        });
    }








}
