package com.lt.quota.core.jms.consumer;

import com.lt.quota.core.constant.IConstants;
import com.lt.quota.core.jms.bo.SpreadBO;
import com.lt.quota.core.quota.coin.SpreadSync;
import com.lt.quota.core.utils.FastJsonUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

/**
 * 点差
 */
@Component
public class ConfigConsumer {

    private Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private SpreadSync spreadSync;

    @JmsListener(containerFactory = "topicFactory", destination = IConstants.TOPIC_CONFIG_CHANGE)
    public void receive(String msg) {
        logger.info("收到Topic {} 消息：{}", IConstants.TOPIC_CONFIG_CHANGE, msg);
        SpreadBO spreadBO = FastJsonUtils.getJson(msg, SpreadBO.class);
        String cmd = spreadBO.getCmd();
        //点差
        if ("spread".equals(cmd)) {
            String productCode = spreadBO.getProductCode();
            spreadSync.syncData(productCode);
        }
    }
}
