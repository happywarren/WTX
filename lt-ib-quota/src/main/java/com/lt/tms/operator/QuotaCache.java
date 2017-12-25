package com.lt.tms.operator;


import com.lt.tms.quota.bean.QuotaDataBean;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class QuotaCache {

    private static QuotaCache instance;

    private final Map<Integer, QuotaDataBean> QUOTA_DATA_MAP = new ConcurrentHashMap<Integer, QuotaDataBean>();

    private final Map<Integer, Integer> QUOTA_SUBSCRIBE_MAP = new ConcurrentHashMap<Integer, Integer>();

    private QuotaCache() {

    }

    public static synchronized QuotaCache getInstance() {
        if (instance == null)
            instance = new QuotaCache();
        return instance;
    }

    public void setQuotaDataMap(Integer tickerId, QuotaDataBean quotaDataBean) {
        QUOTA_DATA_MAP.put(tickerId, quotaDataBean);
    }

    public void setQuotaDataBean(Integer tickerId, QuotaDataBean quotaDataBean) {
        QUOTA_DATA_MAP.put(tickerId,quotaDataBean);
    }

    public void removeQuotaDataBean(Integer tickerId) {
        QUOTA_DATA_MAP.remove(tickerId);
        QUOTA_SUBSCRIBE_MAP.remove(tickerId);
    }

    public QuotaDataBean getQuotaDataBean(Integer tickerId) {
        return QUOTA_DATA_MAP.get(tickerId);
    }

    public void clearSubscribe(){
        QUOTA_SUBSCRIBE_MAP.clear();
    }

    public boolean subscribe(Integer tickerId) {
        if (QUOTA_SUBSCRIBE_MAP.containsKey(tickerId)) {
            return true;
        }
        QUOTA_SUBSCRIBE_MAP.put(tickerId, 1);
        return false;
    }
}
