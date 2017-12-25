package com.lt.tms.quota.ib;

import com.lt.tms.quota.ib.api.config.IbQuotaConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@Component
public class IbQuotaMain {

    private Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private IbQuotaStart ibQuotaStart;

    @Autowired
    private IbQuotaConfig ibQuotaConfig;

    private final ScheduledExecutorService service = Executors.newSingleThreadScheduledExecutor();

    @PostConstruct
    public void init() {
        if (!ibQuotaConfig.isStartup()) {
            logger.info("盈透行情不启动");
            return;
        }
        ibQuotaStart.start();

        service.scheduleAtFixedRate(new Runnable() {
            @Override
            public void run() {
                ibQuotaStart.checkConnected();
            }
        }, 20, 20, TimeUnit.SECONDS);

    }
}
