package com.lt.quota.core.quota.clean.impl;

import com.lt.quota.core.quota.ProductTimeCache;
import com.lt.quota.core.quota.bean.QuotaBean;
import com.lt.quota.core.quota.clean.BaseCleanService;
import com.lt.quota.core.quota.clean.QuotaDataSend;
import com.lt.quota.core.utils.DateUtils;
import com.lt.quota.core.utils.NumberUtil;
import com.lt.quota.core.utils.Utils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.Date;

@Component
public class DefaultCleanService extends BaseCleanService {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private QuotaDataSend quotaDataSend;

    @Autowired
    private ProductTimeCache productTimeCache;

    @Override
    public void clean(QuotaBean quotaBean) {

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

        Date date = DateUtils.formatDate(quotaBean.getTimeStamp(), "yyyy-MM-dd HH:mm:ss.SSS");

        //第三方行情时间 大于 当前时间 30s
        long now = System.currentTimeMillis();
        if(date.getTime() - now > 3000){
           // logger.info("商品 {} 行情 {} 大于当前3S",productCode,quotaBean.getTimeStamp());
            logger.info("行情信息1：{}",quotaBean.toString());
        }
        if(date.getTime() - now > 5000){
            logger.info("商品 {} 行情 {} 大于当前5S",productCode,quotaBean.getTimeStamp());
        }
        if(date.getTime() - now > 8000){
            logger.info("商品 {} 行情 {} 大于当前8S",productCode,quotaBean.getTimeStamp());
        }
        if (date.getTime() - now > 30000){
            logger.info("商品 {} 行情 {} 大于当前30S",productCode,quotaBean.getTimeStamp());
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
            logger.info("商品 {} 行情时间不对,{},{}", productCode, timestamp,preTimestamp);
            return;
        }

        LAST_TIME_PRODUCT_MAP.put(productCode, timestamp);

        //分发数据
        quotaDataSend.send(quotaBean);
    }
}
