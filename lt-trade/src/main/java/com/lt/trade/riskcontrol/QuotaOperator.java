package com.lt.trade.riskcontrol;

import com.lt.enums.trade.PlateEnum;
import com.lt.trade.tradeserver.bean.ProductPriceBean;
import com.lt.util.utils.DateTools;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Date;
import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.LinkedBlockingDeque;


public class QuotaOperator {

    private static final Logger logger = LoggerFactory.getLogger(QuotaOperator.class);

    private final Map<String, ProductPriceBean> quotePriceMap = new ConcurrentHashMap<String, ProductPriceBean>();
    private final BlockingQueue<ProductPriceBean> productPriceQueueOuter = new LinkedBlockingDeque<ProductPriceBean>();
    private final BlockingQueue<ProductPriceBean> productPriceQueueInner = new LinkedBlockingDeque<ProductPriceBean>();
    private final BlockingQueue<ProductPriceBean> productPriceQueueContract = new LinkedBlockingDeque<ProductPriceBean>();

    private static QuotaOperator instance;

    private QuotaOperator() {

    }

    public static synchronized QuotaOperator getInstance() {
        if (instance == null) {
            instance = new QuotaOperator();
        }
        return instance;
    }

    public void setQuotePriceMap(String key, ProductPriceBean productPriceBean) {
        try{
            long time = System.currentTimeMillis() - Long.parseLong(productPriceBean.getQuotaTime());
            if(time > 4000){
             //   logger.info("******************行情有延迟!{},{},{}",productPriceBean.getProductName(),DateTools.formatDate(new Date(),DateTools.FORMAT_FULL),DateTools.formatDate(new Date(Long.parseLong(productPriceBean.getQuotaTime())),DateTools.FORMAT_FULL));
            }
        }catch(Exception e){
            logger.info("error:{}",productPriceBean.getQuotaTime());
        }

        quotePriceMap.put(key, productPriceBean);
    }

    public void setProductPriceQueue(ProductPriceBean productPriceBean) {
        try {
            if(PlateEnum.INNER_PLATE.getValue().equals(productPriceBean.getPlate())){
                productPriceQueueInner.put(productPriceBean);
            }
            else if(PlateEnum.OUTER_PLATE.getValue().equals(productPriceBean.getPlate())){
                productPriceQueueOuter.put(productPriceBean);
            }
            else if(PlateEnum.CONTRACT_FOR_DIFFERENCE.getValue().equals(productPriceBean.getPlate())){
                productPriceQueueContract.put(productPriceBean);
            }else{
                productPriceQueueOuter.put(productPriceBean);
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public ProductPriceBean getQuotePrice(String product) {
        return quotePriceMap.get(product);
    }

    public BlockingQueue<ProductPriceBean> getProductPriceQueue(String plateName) {
        if(PlateEnum.INNER_PLATE.getName().equals(plateName)){
            return productPriceQueueInner;
        }
        else if(PlateEnum.OUTER_PLATE.getName().equals(plateName)){
            return productPriceQueueOuter;
        }
        else if(PlateEnum.CONTRACT_FOR_DIFFERENCE.getName().equals(plateName)){
            return productPriceQueueContract;
        }
        else{
            return null;
        }
    }
}
