package com.lt.business.core.server;

import com.lt.business.core.handler.QuotaListener;
import com.lt.tradeclient.tcp.client.NettyTcpClientStartup;
import com.lt.util.utils.prop.CustomerPropertiesConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.HashSet;
import java.util.Set;

/**
 * 行情模块
 */
@Service
public class QuotaServer implements InitializingBean {

    private static Logger LOGGER = LoggerFactory.getLogger(QuotaServer.class);

    @Autowired
    private CustomerPropertiesConfig propertyConfig_;

    @Autowired
    private ProductTimeCache productTimeCache;

    @Override
    public void afterPropertiesSet() throws Exception {
        startQuota();
    }


    //启动行情
    private void startQuota() {
        NettyTcpClientStartup nettyTcpClientStartup = new NettyTcpClientStartup(new QuotaListener(productTimeCache));
        Set<com.lt.tradeclient.tcp.client.bean.QuotaServer> quotaServers = new HashSet<com.lt.tradeclient.tcp.client.bean.QuotaServer>();

        String host = propertyConfig_.getProperty("outer_quota_host").trim();
        String port = propertyConfig_.getProperty("outer_quota_port").trim();

        String[] hostArray = host.split(",");
        String[] portArray = port.split(",");
        for (int i = 0; i < hostArray.length; i++) {
            try {
                String ip = InetAddress.getByName(hostArray[i]).getHostAddress();
                quotaServers.add(new com.lt.tradeclient.tcp.client.bean.QuotaServer(ip, Integer.parseInt(portArray[i])));
            } catch (UnknownHostException e) {
                LOGGER.error("解析" + hostArray[i] + "未找到ip");
            }
        }
        try {
            nettyTcpClientStartup.start(quotaServers);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
