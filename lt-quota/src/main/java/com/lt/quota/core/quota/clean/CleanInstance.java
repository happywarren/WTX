package com.lt.quota.core.quota.clean;


import com.lt.quota.core.comm.spring.SpringUtils;
import com.lt.quota.core.quota.bean.QuotaBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.*;

public class CleanInstance {

    private Logger logger = LoggerFactory.getLogger(getClass());

    private final BlockingQueue<QuotaBean> marketDataQueue = new LinkedBlockingQueue<QuotaBean>();

    private final ScheduledExecutorService executorService = new ScheduledThreadPoolExecutor(5);

    private static ICleanService defaultCleanService;

    private static CleanInstance instance;

    private CleanInstance() {
        defaultCleanService = SpringUtils.getBean("defaultCleanService", ICleanService.class);
        startupMarketDataThread();
    }


    public static synchronized CleanInstance getInstance() {
        if (instance == null) {
            instance = new CleanInstance();
        }
        return instance;
    }

    public void setMarketDataQueue(QuotaBean quotaBean) {
        try {
            marketDataQueue.put(quotaBean);
        } catch (Exception e) {
        }
    }

    private void startupMarketDataThread() {
        executorService.execute(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    try {
                        QuotaBean quotaBean = marketDataQueue.take();
                        clean(quotaBean);
                    } catch (Exception e) {
                        e.printStackTrace();
                        logger.error("行情数据处理出错, 异常信息: " + e.getMessage());
                    }
                }
            }
        });
    }


    private void clean(QuotaBean quotaBean) {
        try {
            defaultCleanService.clean(quotaBean);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
