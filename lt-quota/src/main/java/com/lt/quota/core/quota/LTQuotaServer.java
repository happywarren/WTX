package com.lt.quota.core.quota;

import com.lt.tradeclient.tcp.client.NettyTcpClientStartup;
import com.lt.tradeclient.tcp.client.bean.QuotaServer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;


import javax.annotation.PostConstruct;
import java.util.HashSet;
import java.util.Set;

/**
 * 行情模块
 */
@Component
public class LTQuotaServer {

    private static Logger LOGGER = LoggerFactory.getLogger(QuotaServer.class);


    @PostConstruct
    public void start() throws Exception {
        startQuota();
    }

    //启动行情
    private void startQuota() {

        NettyTcpClientStartup nettyTcpClientStartup = new NettyTcpClientStartup(new QuotaListener());
        Set<com.lt.tradeclient.tcp.client.bean.QuotaServer> quotaServers = new HashSet<com.lt.tradeclient.tcp.client.bean.QuotaServer>();

        String[] hostArray = {"39.108.160.13","120.79.172.237"};
        String[] portArray = {"50000","50000"};
        for(int i=0; i< hostArray.length; i++){
            quotaServers.add(new QuotaServer(hostArray[i],Integer.parseInt(portArray[i])));
        }

        try {
            nettyTcpClientStartup.start(quotaServers);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
