package com.lt.quota.core.quota.clean;


import com.lt.quota.core.quota.bean.QuotaBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class BaseCleanService implements ICleanService {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    protected final Map<String, Long> LAST_TIME_PRODUCT_MAP = new ConcurrentHashMap<String, Long>();

    @Override
    public void clean(QuotaBean quotaBean) {

    }
}