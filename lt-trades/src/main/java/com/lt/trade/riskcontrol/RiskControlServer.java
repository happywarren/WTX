package com.lt.trade.riskcontrol;

import com.alibaba.fastjson.JSONObject;
import com.lt.enums.trade.PlateEnum;
import com.lt.model.dispatcher.enums.DispatcherRedisKey;
import com.lt.model.dispatcher.enums.RedisQuotaObject;
import com.lt.trade.ProductTimeCache;
import com.lt.trade.netty.OnStartupListener;
import com.lt.trade.riskcontrol.bean.MutableStopLossBean;
import com.lt.trade.riskcontrol.bean.RiskControlBean;
import com.lt.trade.tradeserver.BaseTrade;
import com.lt.trade.tradeserver.bean.FutureOrderBean;
import com.lt.trade.tradeserver.bean.ProductInfoBean;
import com.lt.trade.tradeserver.bean.ProductPriceBean;
import com.lt.trade.utils.TradeUtils;
import com.lt.tradeclient.tcp.client.NettyTcpClientStartup;
import com.lt.tradeclient.tcp.client.bean.QuotaServer;
import com.lt.util.utils.StringTools;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.BoundHashOperations;
import org.springframework.data.redis.core.RedisTemplate;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 风控服务
 * <p>
 * Created by sunch on 2016/11/10.
 */
public class RiskControlServer implements Runnable {

    private static Logger LOGGER = LoggerFactory.getLogger(RiskControlServer.class);

    private NettyTcpClientStartup nettyTcpClientStartup;

    private OnStartupListener startupListener_;

    private BaseTrade trade_;
    private ProductTimeCache productTimeCache;
    private String host;
    private String port;
    private final Map<String, RiskControlQueue> riskControlQueueMap_ = new ConcurrentHashMap<String, RiskControlQueue>();

    public RiskControlQueue getRiskControlQueue(String productName) {
        return riskControlQueueMap_.get(productName);
    }

    public RiskControlServer(BaseTrade trade, ProductTimeCache productTimeCache, String host, String port, OnStartupListener startupListener, Boolean quotaFlag) {
        this.trade_ = trade;
        this.productTimeCache = productTimeCache;
        this.host = host;
        this.port = port;
        this.startupListener_ = startupListener;
        //行情只连接一次
        if (!quotaFlag) {
            startQuota();
        }

    }

    //启动行情
    private void startQuota() {
        nettyTcpClientStartup = new NettyTcpClientStartup(new QuotaListener(productTimeCache));
        Set<QuotaServer> quotaServers = new HashSet<QuotaServer>();
        //TODO 抽取host port
        String[] hostArray = host.split(",");
        String[] portArray = port.split(",");
        for (int i = 0; i < hostArray.length; i++) {
            try {
                String ip = InetAddress.getByName(hostArray[i]).getHostAddress();
                quotaServers.add(new QuotaServer(ip, Integer.parseInt(portArray[i])));
            } catch (UnknownHostException e) {
                LOGGER.error("解析" + hostArray[i] + "未找到ip");
            }
        }
        try {
            nettyTcpClientStartup.start(quotaServers);
            startupListener_.onCompletion(true);
        } catch (Exception e) {
            e.printStackTrace();
            startupListener_.onCompletion(false);
        }
    }

    // 初始化风控队列
    public void initRiskControlQueue() {
        Map<String, ProductInfoBean> productInfoMap = productTimeCache.getProductInfoMap();
        Iterator<String> iterator = productInfoMap.keySet().iterator();
        LOGGER.info("初始化风控队列:productTimeCache={}", JSONObject.toJSON(productTimeCache));
        while (iterator.hasNext()) {
            String productName = iterator.next();
            riskControlQueueMap_.put(productName, new RiskControlQueue());
            initQuota(productName);
        }
    }

    private void initQuota(String productCode) {
        RedisTemplate redisTemplate = trade_.getRedisTemplate();
        String key = DispatcherRedisKey.QUOTA_NEW + productCode;
        BoundHashOperations<String, String, RedisQuotaObject> hashOps = redisTemplate.boundHashOps(key);
        RedisQuotaObject obj = hashOps.get(productCode);
        double lastPrice = 0D;
        double bidPrice = 0D;
        double askPrice = 0D;
        double changeValue = 0D;
        double changeRate = 0D;
        String quotaTime = "";
        if (StringTools.isNotEmpty(obj)) {
            lastPrice = StringTools.formatDouble(obj.getLastPrice(), 0D);// 当前价
            bidPrice = StringTools.formatDouble(obj.getBidPrice1(), 0D);// 买一价
            askPrice = StringTools.formatDouble(obj.getAskPrice1(), 0D);// 卖一价
            quotaTime = obj.getTimeStamp();//接收行情时间
            changeValue = StringTools.formatDouble(obj.getChangeValue(), 0.0d);//涨跌值
            changeRate = StringTools.formatDouble(obj.getChangeRate(), 0.0d);//涨跌幅
        }
        LOGGER.info("初始化商品: {} 行情 lastPrice: {} bidPrice: {} askPrice: {} quotaTime: {} ", productCode, lastPrice, bidPrice, askPrice, quotaTime);
        ProductPriceBean productPrice = new ProductPriceBean();
        productPrice.setProductName(productCode);
        productPrice.setLastPrice(lastPrice);
        productPrice.setBidPrice(bidPrice);
        productPrice.setAskPrice(askPrice);
        productPrice.setChangeValue(changeValue);
        productPrice.setChangeRate(changeRate);
        productPrice.setQuotaTime(quotaTime);
        QuotaOperator.getInstance().setQuotePriceMap(productCode, productPrice);
    }

    // 新增风控商品(如: 更换合约)
    public void addRiskControlProduct(String productName) {
        riskControlQueueMap_.put(productName, new RiskControlQueue());
        ProductPriceBean productPriceBean = QuotaOperator.getInstance().getQuotePrice(productName);
        if (null == productPriceBean) {
            initQuota(productName);
        }
    }

    // 移除风控商品
    public void removeRiskControlProduct(String productName) {
        riskControlQueueMap_.remove(productName);
    }

    // 填充风控队列
    public void fillRiskControlQueue(RiskControlBean riskBean) {
        LOGGER.info("填充风控队列商品：{}",riskBean.getProductName());
        RiskControlQueue riskControlQueue = riskControlQueueMap_.get(riskBean.getProductName());
        riskControlQueue.fillRiskControlQueue(riskBean);
    }

    // 移除风控订单
    public boolean removeRiskControlOrder(RiskControlBean riskBean) {
        RiskControlQueue riskControlQueue = riskControlQueueMap_.get(riskBean.getProductName());
        return riskControlQueue.removeRiskControlOrder(riskBean);
    }

    // 更新风控订单(修改止盈止损)
    public boolean updateRiskControlOrder(RiskControlBean riskBean) {
        RiskControlQueue riskControlQueue = riskControlQueueMap_.get(riskBean.getProductName());
        return riskControlQueue.updateRiskControlOrder(riskBean);
    }

    // 开启/关闭移动止损
    public void switchMutableStopLossOrder(RiskControlBean riskBean) {
        RiskControlQueue riskControlQueue = riskControlQueueMap_.get(riskBean.getProductName());
        riskControlQueue.switchMutableStopLossOrder(riskBean);
    }

    // 更新递延订单
    public boolean updateDeferredOrder(RiskControlBean riskBean) {
        RiskControlQueue riskControlQueue = riskControlQueueMap_.get(riskBean.getProductName());
        return riskControlQueue.updateDeferredOrder(riskBean.getUniqueOrderId(), riskBean.getDeferredOrderTimeStamp());
    }

    // 获取风控订单(移动止损)
    public MutableStopLossBean getMutableStopLossOrder(String productName, String uniqueOrderId) {
        RiskControlQueue riskControlQueue = riskControlQueueMap_.get(productName);
        return riskControlQueue.getMutableStopLossOrder(uniqueOrderId);
    }

    // 获取清仓订单
    public List<FutureOrderBean> getClearOrders(String productName) {
        RiskControlQueue riskControlQueue = riskControlQueueMap_.get(productName);
        return riskControlQueue.getClearOrders();
    }

    // 获取行情数据
    public ProductPriceBean getQuotePrice(String productName) {
        return QuotaOperator.getInstance().getQuotePrice(productName);
    }

    @Override
    public void run() {
        // fixme: 循环显式退出?
        for (; ; ) {
            try {
                // 有行情数据过来就执行一次风控
                final ProductPriceBean productPrice = QuotaOperator.getInstance().getProductPriceQueue(trade_.getPlateName()).take();
                final String productName = productPrice.getProductName();
                final RiskControlQueue riskControlQueue = riskControlQueueMap_.get(productName);

                if (riskControlQueue == null) {
                    LOGGER.error("商品不存在:{} ", productName);
                    continue;
                }
                TradeUtils.cachedThreadPool.execute(new Runnable() {
                    @Override
                    public void run() {
                        List<FutureOrderBean> orders = riskControlQueue.risk(productPrice);
                        if (orders != null) {
                            LOGGER.info("------------------行情信息:{}", JSONObject.toJSONString(productPrice));
                            LOGGER.info("productName {}  风控处理订单详情:{} ", productName, JSONObject.toJSON(orders));
                            trade_.closeOrders(orders);
                        }
                    }
                });
                TradeUtils.cachedThreadPool.execute(new Runnable() {
                    @Override
                    public void run() {
                        List<FutureOrderBean> orders = riskControlQueue.riskMutableStopLoss(productPrice);
                        if (orders != null) {
                            LOGGER.info("------------------行情信息:{}", JSONObject.toJSONString(productPrice));
                            LOGGER.info("productName {}  移动风控处理订单详情:{} ", productName, JSONObject.toJSON(orders));
                            trade_.closeOrders(orders);
                        }
                    }
                });


            } catch (Exception e) {
                LOGGER.error("风控服务出错, 异常信息: " + e.getMessage());
            }
        }
    }
}
