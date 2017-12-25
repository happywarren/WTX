package com.lt.quota.core.appServer;

import com.lt.quota.core.constant.IConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * 作者: 邓玉明
 * 时间: 2017/5/5 上午9:57
 * email:cndym@163.com
 */
@Component
public class AppServerStartup implements InitializingBean {

    private Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private AppServer appServer;

    @Autowired
    private RedisTemplate redisTemplate;

    @Override
    public void afterPropertiesSet() throws Exception {
        redisTemplate.delete(IConstants.REDIS_USER_ONLINE_LIST);
        ScheduledExecutorService service = Executors.newSingleThreadScheduledExecutor();
        // 第二个参数为首次执行的延时时间，第三个参数为定时执行的间隔时间
        service.schedule(appServer, 5, TimeUnit.SECONDS);
    }
}
