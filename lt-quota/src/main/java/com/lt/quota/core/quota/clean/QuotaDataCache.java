package com.lt.quota.core.quota.clean;

import com.lt.quota.core.constant.IConstants;
import com.lt.quota.core.quota.KLineUtils;
import com.lt.quota.core.quota.bean.QuotaBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * 行情数据写入文件
 */

@Component
public class QuotaDataCache {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    private final BlockingQueue<QuotaBean> quotaDataQueue = new LinkedBlockingQueue<QuotaBean>();

    private final BlockingQueue<QuotaBean> quotaDataMinuteQueue = new LinkedBlockingQueue<QuotaBean>();

    private final ExecutorService executorService = Executors.newCachedThreadPool();

    @Value("${sys.quota.data.base.dir}")
    private String baseDir;

    @Autowired
    private KLineUtils kLineUtils;

    @PostConstruct
    public void init() {
        startupCacheMinuteMarketDataThread();
        startupCacheMarketDataThread();
    }

    public void setQuotaDataQueue(QuotaBean quotaBean) {
        try {
            quotaDataMinuteQueue.put(quotaBean);
            quotaDataQueue.put(quotaBean);
        } catch (Exception e) {
        }
    }

    private void startupCacheMinuteMarketDataThread() {
        executorService.execute(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    try {
                        final QuotaBean quotaBean = quotaDataMinuteQueue.take();
                        mergeMarketData(quotaBean);
                    } catch (Exception e) {
                        e.printStackTrace();
                        logger.error("行情数据发送出错, 异常信息: " + e.getMessage());
                    }
                }
            }
        });
    }

    private void startupCacheMarketDataThread() {
        executorService.execute(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    try {
                        final QuotaBean quotaBean = quotaDataQueue.take();
                        logger.debug("实时行情时间: {} ", quotaBean.getTimeStamp());
                        kLineUtils.create(quotaBean);
                    } catch (Exception e) {
                        e.printStackTrace();
                        logger.error("行情数据发送出错, 异常信息: " + e.getMessage());
                    }
                }
            }
        });
    }


    private void mergeMarketData(QuotaBean quotaBean) {
        logger.debug("分钟行情时间: {} ", quotaBean.getTimeStamp());
        String productCode = quotaBean.getProductName();
        synchronized (getLock(productCode)) {
            kLineUtils.addKLineBean(quotaBean, IConstants.DATA_TYPE_1);
            kLineUtils.addKLineBean(quotaBean, IConstants.DATA_TYPE_5);
            kLineUtils.addKLineBean(quotaBean, IConstants.DATA_TYPE_15);
            kLineUtils.addKLineBean(quotaBean, IConstants.DATA_TYPE_30);
            kLineUtils.addKLineBean(quotaBean, IConstants.DATA_TYPE_60);
            kLineUtils.addKLineBean(quotaBean, IConstants.DATA_TYPE_DAY);
            kLineUtils.addKLineBean(quotaBean, IConstants.DATA_TYPE_WEEK);
            kLineUtils.addKLineBean(quotaBean, IConstants.DATA_TYPE_MONTH);
        }
    }

    private String getLock(String productCode) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(productCode);
        return stringBuilder.toString().intern();
    }
}
