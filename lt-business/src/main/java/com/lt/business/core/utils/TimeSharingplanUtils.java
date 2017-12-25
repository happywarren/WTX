package com.lt.business.core.utils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.lt.business.core.server.QuotaInitializeServer;
import com.lt.business.core.vo.TimeSharingplanBean;
import com.lt.model.dispatcher.enums.DispatcherRedisKey;
import com.lt.model.dispatcher.enums.RedisQuotaObject;
import com.lt.util.utils.DateTools;
import com.lt.util.utils.StringTools;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.BoundHashOperations;

import java.text.ParseException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;


/**
 * 分时图相关方法实现
 *
 * @author guodw
 */
public class TimeSharingplanUtils {

    static Logger logger = LoggerFactory.getLogger(TimeSharingplanUtils.class);

    /**
     * 本地内存 Map<商品,Map<时间,分时图对象>>
     */
    public static Map<String, Map<String, TimeSharingplanBean>> timeSharingplanMap = new ConcurrentHashMap<String, Map<String, TimeSharingplanBean>>();

    /**
     * 本地内存 Map<商品,最新行情数据>
     */
    public static Map<String, RedisQuotaObject> map = new ConcurrentHashMap<String, RedisQuotaObject>();

    /**
     * 每个商品的上一分钟分时图的总量
     */
    public static Map<String, Integer> publicVolume = new ConcurrentHashMap<String, Integer>();

    /**
     * 分时图生成逻辑
     *
     * @param product
     * @param obj
     */
    public static void createTimeSharingplan(String product, RedisQuotaObject obj, String data) {
        try {
            // -----------------------------分时图生成逻辑start-----------------
            if (!StringTools.isNotEmpty(obj.getTimeStamp())) {
                return;
            }
            Date date = new Date();
            date.setTime(new Long(obj.getTimeStamp()));
            String time = DateTools.parseToDefaultDateSecondString(date);

            Map<String, TimeSharingplanBean> timeSharingplanBeanMap = timeSharingplanMap.get(product);

            TimeSharingplanBean bean = null;
            if (timeSharingplanBeanMap != null) {
                bean = timeSharingplanBeanMap.get(time);
            }else {
                timeSharingplanBeanMap = new HashMap<>();
            }

            // 缓存中不存在这一分钟的数据 生成一条分时图
            if (null == bean) {
                timeSharingplanBeanMap.clear();
                bean = new TimeSharingplanBean(time, obj.getLastPrice(), obj.getTotalQty());
                // 内存
                timeSharingplanBeanMap.put(time, bean);

                timeSharingplanMap.put(product, timeSharingplanBeanMap);

                Integer v = publicVolume.get(product);

                v = StringTools.formatInt(v, 0);

                //分钟行情 交易量计算
                publicVolume.put(product, bean.getVolume());

                int volume = bean.getVolume() - v;
                bean.setVolume(volume);

                // 文件
                QuotaUtils.wirteFileFst(JSONObject.toJSONString(bean), product);
            }

            // -----------------------------分时图生成逻辑end-----------------
        } catch (Exception e) {
            logger.error("分时图生成逻辑异常", e);
        }

    }

    /**
     * redis中保存最新的行情数据
     *
     * @param product
     * @param obj
     */
    public static void saveNewQuotaDataForRedis(String product, RedisQuotaObject obj) {
        try {
            // redis缓存保存最新行情数据
            String key = DispatcherRedisKey.QUOTA_NEW + product;
            BoundHashOperations<String, String, RedisQuotaObject> hashOps = QuotaInitializeServer
                    .getRedisTemplate().boundHashOps(key);
            hashOps.put(product, obj);
        } catch (Exception e) {
            logger.error("redis中保存最新的行情数据异常", e);
            return;
        }


    }

    /**
     * json 转换 对象
     *
     * @param json
     * @return
     */
    public static RedisQuotaObject toJsonObject(String json) {
        try {
            JSONObject jsonObj = JSONObject.parseObject(json);
            JSON data = jsonObj.getJSONObject("DATA");
            RedisQuotaObject obj = JSONObject.toJavaObject(data,
                    RedisQuotaObject.class);
            return obj;
        } catch (Exception e) {
            logger.error("RedisMessageListenerService------->>RedisQuotaObject------->>json 转换 对象------->>", e);
            return null;
        } finally {
        }

    }

    /**
     * 清空内存缓存 redis缓存
     */
    public static void clear() {
        // 清空分时图
        timeSharingplanMap.clear();
    }

    /**
     * 启动时加载方法 把缓存中的数据加载到本地内存
     */
    public static void init() {
        // 查询所有商品
        for (String product : QuotaInitializeServer.productMap.keySet()) {
            // 最新行情加载
            String key = DispatcherRedisKey.QUOTA_NEW + product;
            BoundHashOperations<String, String, RedisQuotaObject> hashOps = QuotaInitializeServer.getRedisTemplate().boundHashOps(key);
            map.put(product, hashOps.get(key));
        }
    }

    /**
     * TODO 添加行情到内存
     *
     * @param redisQuotaObject
     * @author XieZhibing
     * @date 2017年2月16日 下午1:20:09
     */
    public static void add(RedisQuotaObject redisQuotaObject) {
        // 最新行情加载
        String productName = redisQuotaObject.getProductName();
        map.put(productName, redisQuotaObject);
    }

    public static void main(String[] args) throws ParseException {
        String timeStamp = "20161201 16:32:00.921448";
        QuotaTimeConfigUtils.DateformatForTimeSharingplan(timeStamp);
    }
}
