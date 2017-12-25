package com.lt.quota.core.quota;

import com.alibaba.fastjson.JSON;
import com.lt.quota.core.comm.redis.lock.DistributedLock;
import com.lt.quota.core.comm.redis.lock.RedisLock;
import com.lt.quota.core.constant.IConstants;
import com.lt.quota.core.model.KLineBean;
import com.lt.quota.core.model.TimeSharingBean;
import com.lt.quota.core.quota.bean.QuotaBean;
import com.lt.quota.core.utils.DateUtils;
import com.lt.quota.core.utils.FileUtils;
import com.lt.quota.core.utils.Utils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.File;
import java.util.Date;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * K线工具类
 */
@Component
public class KLineUtils {

    private Logger logger = LoggerFactory.getLogger(getClass());

    /**
     * K线文件后缀
     */
    private final String K_SUFFIX = ".k";

    /**
     * 分时图
     */
    private final String FST_SUFFIX = ".fst";

    /**
     * 实时行情
     */
    private final String REAL_SUFFIX = ".log";

    @Value("${sys.quota.data.base.dir}")
    private String baseDir;

    @Autowired
    private RedisTemplate redisTemplate;

    @Autowired
    private DistributedLock distributedLock;

    private final Map<String, Long> LAST_QUOTA_MAP = new ConcurrentHashMap<String, Long>();

    private final Map<Integer, Map<String, KLineBean>> KLINE_MAP = new ConcurrentHashMap<Integer, Map<String, KLineBean>>();

    private ValueOperations<String, Map<Integer, Map<String, KLineBean>>> valueOperations;


    @PostConstruct
    private void init() {
        valueOperations = redisTemplate.opsForValue();
    }

    public void create(KLineBean kLineBean) {
        String productName = kLineBean.getProductName();
        String timestamp = kLineBean.getTimeStamp();
        int type = kLineBean.getType();

        RedisLock redisLock = new RedisLock(productName + type);
        if (distributedLock.tryLock(redisLock)) {
            Date date = DateUtils.formatDate(timestamp, "yyyyMMddHHmm");
            if (check(productName, type, date.getTime())) {
                //写K线文件
                StringBuilder fileDir = new StringBuilder();
                fileDir.append(baseDir).append(File.separator).append(productName);

                FileUtils.creatDir(fileDir.toString());

                StringBuilder fileName = new StringBuilder();
                fileName.append(fileDir).append(File.separator).append(productName).append("_");
                if (type == IConstants.DATA_TYPE_DAY) {
                    fileName.append("day");
                } else if (type == IConstants.DATA_TYPE_WEEK) {
                    fileName.append("week");
                } else if (type == IConstants.DATA_TYPE_MONTH) {
                    fileName.append("month");
                } else {
                    fileName.append(DateUtils.formatDate2Str(date, "yyyy-MM-dd")).append("_");
                    fileName.append(kLineBean.getType());
                }
                fileName.append(K_SUFFIX);
                File file = new File(fileName.toString());
                kLineBean.setTimeStamp(DateUtils.formatDate2Str(date, "yyyy-MM-dd HH:mm:ss.SSS"));
                if (file.exists()) {
                    FileUtils.writeFile("," + JSON.toJSONString(kLineBean), fileName.toString(), true);
                } else {
                    FileUtils.writeFile(JSON.toJSONString(kLineBean), fileName.toString(), true);
                }
            }
            distributedLock.releaseLock(redisLock);
        }
    }

    public void create(TimeSharingBean timeSharingBean, String productName) {
        String timestamp = timeSharingBean.getTimeStamp();
        int type = IConstants.DATA_TYPE_FST;
        RedisLock redisLock = new RedisLock(productName + type);
        if (distributedLock.tryLock(redisLock)) {
            Date date = DateUtils.formatDate(timestamp, "yyyyMMddHHmm");
            if (check(productName, type, date.getTime())) {
                //写K线文件
                StringBuilder fileDir = new StringBuilder();
                fileDir.append(baseDir).append(File.separator).append(productName);

                FileUtils.creatDir(fileDir.toString());

                StringBuilder fileName = new StringBuilder();
                fileName.append(fileDir).append(File.separator).append(productName).append("_");
                fileName.append(DateUtils.formatDate2Str(date, "yyyy-MM-dd"));
                fileName.append(FST_SUFFIX);
                File file = new File(fileName.toString());
                timeSharingBean.setTimeStamp(DateUtils.formatDate2Str(date, "yyyy-MM-dd HH:mm:ss"));
                if (file.exists()) {
                    FileUtils.writeFile("," + JSON.toJSONString(timeSharingBean), fileName.toString(), true);
                } else {
                    FileUtils.writeFile(JSON.toJSONString(timeSharingBean), fileName.toString(), true);
                }
            }
            distributedLock.releaseLock(redisLock);
        }
    }

    public void create(QuotaBean quotaBean) {
        String productName = quotaBean.getProductName();
        RedisLock redisLock = new RedisLock(productName + IConstants.DATA_TYPE_REAL);
        if (distributedLock.tryLock(redisLock)) {
            Date date = DateUtils.formatDate(quotaBean.getTimeStamp(), "yyyy-MM-dd HH:mm:ss.SSS");
            long timestamp = date.getTime();
            if (check(productName, IConstants.DATA_TYPE_REAL, timestamp)) {
                StringBuilder fileDir = new StringBuilder();
                fileDir.append(baseDir).append(File.separator).append(productName);

                FileUtils.creatDir(fileDir.toString());

                StringBuilder fileName = new StringBuilder();
                fileName.append(fileDir).append(File.separator).append(productName).append("_");
                fileName.append(DateUtils.formatDate2Str(date, "yyyy-MM-dd"));
                fileName.append(REAL_SUFFIX);
                FileUtils.writeLine(fileName.toString(), JSON.toJSONString(quotaBean));
            }
            distributedLock.releaseLock(redisLock);
        }
    }

    /**
     * 是否是新数据
     *
     * @param productName
     * @param type
     * @param timestamp
     * @return
     */
    private boolean check(String productName, int type, Long timestamp) {
        String key = buildKey(productName, type);
        try {
            long localTimestamp = 0L;
            if (LAST_QUOTA_MAP.containsKey(key)) {
                localTimestamp = LAST_QUOTA_MAP.get(key);
            }
            if (localTimestamp >= timestamp.longValue()) {
                return false;
            }
            String cacheTimestamp = (String) redisTemplate.opsForHash().get(IConstants.QUOTA_KLINE_LAST, key);
            if (Utils.isNotEmpty(cacheTimestamp) && Long.parseLong(cacheTimestamp) >= timestamp.longValue()) {
                return false;
            }
            LAST_QUOTA_MAP.put(key, timestamp);
            redisTemplate.opsForHash().put(IConstants.QUOTA_KLINE_LAST, key, String.valueOf(timestamp));
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    private String buildKey(String productName, Integer type) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(productName).append("_").append(type);
        return stringBuilder.toString();
    }

    public void addKLineBean(QuotaBean obj, Integer type) {
        try {
            if (null == KLINE_MAP || KLINE_MAP.size() == 0) {
                Map<Integer, Map<String, KLineBean>> map = valueOperations.get(IConstants.QUOTA_KINE_LAST_DATA);
                KLINE_MAP.putAll(map);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            Map<String, KLineBean> beanMap = KLINE_MAP.get(type);
            if (beanMap == null || beanMap.isEmpty()) {
                beanMap = new ConcurrentHashMap<String, KLineBean>();
                KLINE_MAP.put(type, beanMap);
            }
            KLineBean bean = beanMap.get(obj.getProductName());
            if (!Utils.isNotEmpty(bean)) {
                bean = new KLineBean();
                bean.setClosePrice(obj.getLastPrice());
                bean.setHighPrice(obj.getLastPrice());
                bean.setLowPrice(obj.getLastPrice());
                bean.setOpenPrice(obj.getLastPrice());
                bean.setProductName(obj.getProductName());
                bean.setTimeStamp(obj.getTimeStamp());
                bean.setType(type);
                bean.setVolume(obj.getTotalQty());
                bean.setTotalQty(obj.getTotalQty());
            } else {
                double lastPrice = Utils.formatDouble(obj.getLastPrice(), 0.0);
                double highPrice = Utils.formatDouble(bean.getHighPrice(), 0.0);
                double lowPrice = Utils.formatDouble(bean.getLowPrice(), 0.0);
                int volume = bean.getTotalQty();
                int totalQty = obj.getTotalQty();
                if (lastPrice > highPrice) {
                    bean.setHighPrice(obj.getLastPrice());
                }
                if (lastPrice < lowPrice) {
                    bean.setLowPrice(obj.getLastPrice());
                }
                bean.setClosePrice(obj.getLastPrice());
                bean.setTimeStamp(obj.getTimeStamp());
                bean.setVolume(totalQty - volume < 0 ? 0 : totalQty - volume);
            }
            beanMap.put(obj.getProductName(), bean);
            valueOperations.set(IConstants.QUOTA_KINE_LAST_DATA, KLINE_MAP);
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("保存K线数据error: {} ", e.getMessage());
        }
    }

    public Map<String, KLineBean> getKLineBean(Integer dataType) {
        return KLINE_MAP.get(dataType);
    }

    public void resetKLineBean(Integer dataType, String productName) {
        KLINE_MAP.get(dataType).remove(productName);
    }
}