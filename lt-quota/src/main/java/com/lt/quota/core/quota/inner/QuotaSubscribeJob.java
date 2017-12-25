package com.lt.quota.core.quota.inner;

import com.lt.quota.core.quota.inner.operator.InnerProductOperator;
import io.netty.channel.Channel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * @author mcsong
 * @create 2017-10-24 9:58
 */
@Component
public class QuotaSubscribeJob {

    private Logger logger = LoggerFactory.getLogger(getClass());

    @Scheduled(cron = "0 0 0 * * ?")
    public void subscribe0() {
        subscribe();
    }

    @Scheduled(cron = "0 0 9 * * ?")
    public void subscribe9() {
        subscribe();
    }

    @Scheduled(cron = "0 30 10 * * ?")
    public void subscribe10() {
        subscribe();
    }

    @Scheduled(cron = "0 30 13 * * ?")
    public void subscribe13() {
        subscribe();
    }

    @Scheduled(cron = "0 0 21 * * ?")
    public void subscribe21() {
        subscribe();
    }

    private void subscribe(){
        String message = InnerProductOperator.getInstance().productPacket();
        logger.info("订阅内盘行情: {} ", message);
        Map<String, Channel> channelMap = InnerClientBox.getInstance().getChannelMap();
        for (Map.Entry<String, Channel> entry : channelMap.entrySet()) {
            Channel channel = entry.getValue();
            channel.writeAndFlush(message);
        }
    }
}
