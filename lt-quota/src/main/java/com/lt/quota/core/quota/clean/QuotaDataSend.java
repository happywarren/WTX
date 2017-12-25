package com.lt.quota.core.quota.clean;

import com.alibaba.fastjson.JSON;
import com.lt.quota.core.appServer.newer.NewClientOperator;
import com.lt.quota.core.constant.IConstants;
import com.lt.quota.core.constant.SystemDataCount;
import com.lt.quota.core.quota.bean.QuotaBean;
import com.lt.quota.core.utils.FastJsonUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;

/**
 * 行情实时数据分发
 */
@Component
public class QuotaDataSend {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    private final ScheduledExecutorService executorService = new ScheduledThreadPoolExecutor(5);

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Autowired
    private NewClientOperator newClientOperator;

    public void send(QuotaBean quotaBean) {
        executorService.execute(new Runnable() {
            @Override
            public void run() {
                newClientOperator.sendQuotaData(quotaBean);
                //存入到Redis
                stringRedisTemplate.opsForValue().set(IConstants.REDIS_PRODUCT_LAST_QUOTA + quotaBean.getProductName(), FastJsonUtils.toJson(quotaBean));
            }
        });
    }
}
