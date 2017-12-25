package com.lt.quota.core.quota;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.lt.quota.core.comm.spring.SpringUtils;
import com.lt.quota.core.server.NewClientOperator;
import com.lt.quota.core.utils.DateUtils;
import com.lt.quota.core.utils.FastJsonUtils;
import com.lt.quota.core.utils.NumberUtil;
import com.lt.quota.core.utils.Utils;
import com.lt.tradeclient.mdata.model.QuotaBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.util.Date;
import java.util.Map;
import java.util.concurrent.*;

public class CleanInstance {

    private Logger logger = LoggerFactory.getLogger(getClass());

    private static CleanInstance instance = null;

    private final BlockingQueue<QuotaBean> innerMarketQueue = new LinkedBlockingQueue<QuotaBean>();  //内盘队列

    private final ScheduledExecutorService executorService = new ScheduledThreadPoolExecutor(5);

    protected final Map<String, Long> LAST_TIME_PRODUCT_MAP = new ConcurrentHashMap<String, Long>();

    private ProductTimeCache productTimeCache;

    private CleanInstance(){
        productTimeCache = SpringUtils.getBean(ProductTimeCache.class);
        startupMarketDataThread();
    }

    public static synchronized CleanInstance getInstance(){
        if(instance  == null){
            instance = new CleanInstance();
        }
        return instance;
    }

    /**设置内盘行情**/
    public void setInnerMarketQueue(QuotaBean quotaBean){
        try{
            innerMarketQueue.put(quotaBean);
        }catch(Exception e){
        }
    }

    public void startupMarketDataThread(){
        executorService.execute(new Runnable() {
            @Override
            public void run() {
                while (true){
                    try{
                        QuotaBean quotaBean = innerMarketQueue.take();
                        clean(quotaBean);
                    }catch(Exception e){
                        e.printStackTrace();
                        logger.error("处理行情出错");
                    }
                }
            }
        });
    }

    public void clean(QuotaBean quotaBean){
        String productCode = quotaBean.getProductName();
        if (Utils.isEmpty(productCode)) {
            return;
        }
        BigDecimal lastPrice = NumberUtil.formatBigDecimal(quotaBean.getLastPrice(), new BigDecimal(0));
        BigDecimal bidPrice = NumberUtil.formatBigDecimal(quotaBean.getBidPrice1(), new BigDecimal(0));
        BigDecimal askPrice = NumberUtil.formatBigDecimal(quotaBean.getAskPrice1(), new BigDecimal(0));
        BigDecimal askQty = NumberUtil.formatBigDecimal(quotaBean.getAskQty1(), new BigDecimal(0));
        BigDecimal bidQty = NumberUtil.formatBigDecimal(quotaBean.getBidQty1(), new BigDecimal(0));

        if (NumberUtil.isLess(lastPrice, new BigDecimal(0))
                || NumberUtil.isLess(bidPrice, new BigDecimal(0))
                || NumberUtil.isLess(askPrice, new BigDecimal(0))
                || NumberUtil.isLess(bidQty, new BigDecimal(0))
                || NumberUtil.isLess(askQty, new BigDecimal(0))) {
            return;
        }

        //Date date = DateUtils.formatDate(quotaBean.getTimeStamp(), "yyyy-MM-dd HH:mm:ss.SSS");

        Date date = new Date(quotaBean.getTimeStamp());

        //第三方行情时间 大于 当前时间 30s
        long now = System.currentTimeMillis();
        if (date.getTime() - now > 30000){
            return;
        }

        //判断行情时间
        if (!productTimeCache.isDispatch(productCode, date)) {
            logger.info("商品 {} 行情 {} 不在行情时段", productCode, quotaBean.getTimeStamp());
            return;
        }

        long timestamp = date.getTime();
        long preTimestamp = 0L;
        if (LAST_TIME_PRODUCT_MAP.containsKey(productCode)) {
            preTimestamp = LAST_TIME_PRODUCT_MAP.get(productCode);
        }

        if (preTimestamp >= timestamp) {
            return;
        }

        LAST_TIME_PRODUCT_MAP.put(productCode, timestamp);

        String json =  JSON.toJSONString(quotaBean);


        NewClientOperator.getInstance().sendQuota(quotaBean);
        //分发数据
      //  quotaDataSend.send(quotaBean);
    }




}
