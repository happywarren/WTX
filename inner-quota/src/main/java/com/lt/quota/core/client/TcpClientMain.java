package com.lt.quota.core.client;

import com.lt.quota.core.comm.config.SysConfig;
import com.lt.quota.core.comm.netty.client.BaseClient;
import com.lt.quota.core.quota.ProductTimeCache;
import com.lt.tradeclient.tcp.client.NettyTcpClientStartup;
import com.lt.tradeclient.tcp.client.bean.QuotaServer;
import com.lt.util.utils.netty.Client;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.HashSet;
import java.util.Set;

@Component
public class TcpClientMain implements InitializingBean {
    private Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private SysConfig  sysConfig;

    @Autowired
    private ProductTimeCache productTimeCache;

    @Override
    public void afterPropertiesSet() {
        startQuota();
    }

    public void startQuota(){
        /*

        System.out.println("productTimeCache="+productTimeCache);
        String quotaServerStr =   sysConfig.getQuotaServer();
        Set<QuotaServer> quotaServerSet = new HashSet<QuotaServer>();
        String [] quotaServers = quotaServerStr.split("_");
        for(int i=0;i<quotaServers.length;i++){
            String [] hostPort = quotaServers[i].split(":");
            String host = hostPort[0];
            String port = hostPort[1];
            String key = host+":"+port;
            if(ClientBox.getInstance().contains(key)){
                continue;
            }
            QuotaListener quotaListener = new QuotaListener();
            quotaListener.setProductTimeCache(productTimeCache);
            BaseClient baseClient = new BaseClient(host,Integer.parseInt(port),new QuotaClientHandler(quotaListener),new QuotaStarupListener());
            Thread t = new Thread(baseClient);
            ClientBox.getInstance().setClient(key,baseClient,t);
            t.start();
        }*/
        QuotaListener quotaListener = new QuotaListener();
        BaseClient baseClient = new BaseClient("106.15.181.168",8068,new QuotaClientHandler(quotaListener),new QuotaStarupListener());
        Thread t = new Thread(baseClient);
        //ClientBox.getInstance().setClient(key,baseClient,t);
        t.start();


    }

    public static void main(String [] args){
        TcpClientMain t = new TcpClientMain();
        t.startQuota();
    }

}
