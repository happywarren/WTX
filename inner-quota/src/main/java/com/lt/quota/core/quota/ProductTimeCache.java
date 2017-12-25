package com.lt.quota.core.quota;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.lt.constant.redis.RedisUtil;
import com.lt.quota.core.utils.DateUtils;
import com.lt.quota.core.utils.FastJsonUtils;
import com.lt.quota.core.utils.Utils;
import com.lt.vo.product.ProductVo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.BoundHashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.Date;
import java.util.Map;
import java.util.concurrent.*;

@Component
public class ProductTimeCache {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    private final String PRODUCT_TIME_CONFIG = "PRODUCT_TIME_CONFIG";

    private final String PRODUCT_INFO = "PRODUCT_INFO";

    private long timestamp = 0L;

    /**
     * 商品行情时间
     */
    public final Map<String, Map<String, String>> PRODUCT_QUOTA_MAP = new ConcurrentHashMap<String, Map<String, String>>();
    public final Map<String,ProductVo> PRODUCT_INFO_MAP = new ConcurrentHashMap<String,ProductVo>();

    ExecutorService executorService = Executors.newSingleThreadExecutor();

    @Autowired
    private RedisTemplate<String,String> redisTemplate;

    private ScheduledExecutorService productInfoScheduler = new ScheduledThreadPoolExecutor(1);


    @PostConstruct
    public void ProductCache() {
        startupProductInfoScheduler();
    }


    private void startupProductInfoScheduler(){
        int delayTime = 1;
        int period = 60;
        productInfoScheduler.scheduleAtFixedRate(new Runnable() {
            @Override
            public void run() {
                init();
            }
        },delayTime, period, TimeUnit.SECONDS);
    }

    private void init() {
        /***
         * 只加载内盘数据
         */
        logger.info("从Redis加载商品信息");
        Map<Object, Object> dataMap = redisTemplate.opsForHash().entries(PRODUCT_TIME_CONFIG);
        BoundHashOperations<String, String, String> productInfo = redisTemplate.boundHashOps(RedisUtil.PRODUCT_INFO);
        Map<String, String> entries = productInfo.entries();
        for (String productCode : entries.keySet()){
            String value = productInfo.get(productCode);
            ProductVo productVo = (ProductVo) JSON.parse(value);
            //System.out.println(productVo.getPlate());

            /*
            ProductVo productVo =  entries.get(productCode);*/
            if(productVo.getPlate() == 0){
                //内盘
                //System.out.println(productVo.getProductCode());
                PRODUCT_INFO_MAP.put(productCode,productVo);
            }
        }

        for (Map.Entry<Object, Object> entry : dataMap.entrySet()) {
            String productCode = entry.getKey().toString().replace("\"", "");
            if(!PRODUCT_INFO_MAP.containsKey(productCode)){
                continue;
            }
            JSONObject jsonObject = JSONObject.parseObject(entry.getValue().toString());
            JSONArray jsonArray = jsonObject.getJSONArray("quotaTimeList");
            Map<String, String> quotaMap = new ConcurrentHashMap<String, String>();
            for (Object object : jsonArray) {
                JSONObject subObject = (JSONObject) object;
                String beginTime = subObject.getString("beginTime");
                String endTime = subObject.getString("endTime");
                quotaMap.put(beginTime, endTime);
            }
            PRODUCT_QUOTA_MAP.put(productCode, quotaMap);
        }

    }


    /**
     * 是否在行情时段
     *
     * @param productCode
     * @return
     */
    public boolean isDispatch(String productCode, Date date) {
        long currTime = System.currentTimeMillis();
        //1分钟
        if (currTime - timestamp > 60000) {
            timestamp = currTime;
            executorService.execute(new Runnable() {
                @Override
                public void run() {
                    init();
                }
            });
        }
        Map<String, String> map = PRODUCT_QUOTA_MAP.get(productCode);
        if (!Utils.isNotEmpty(map)) {
            return true;
        }
        for (Map.Entry<String, String> entry : map.entrySet()) {
            try {
                //节假日(beginTime endTime 相等)
                if (entry.getKey().equals(entry.getValue())) {
                    return false;
                }
                Date beginTime = DateUtils.formatDate(entry.getKey(), "HH:mm:ss");
                Date endTime = DateUtils.formatDate(entry.getValue(), "HH:mm:ss");
                Date quotaTime = DateUtils.formatDate(DateUtils.formatDate2Str(date, "HH:mm:ss"), "HH:mm:ss");
                if (isBetween(quotaTime, beginTime, endTime)) {
                    return true;
                }
            } catch (Exception e) {
                e.printStackTrace();
                logger.error("获取当前品种是否在开市时间异常", e);
                return false;
            }
        }
        return false;
    }

    private boolean isBetween(Date date, Date from, Date to) {
        return from.getTime() <= date.getTime() && to.getTime() >= date.getTime();
    }
}
