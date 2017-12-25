package com.lt.trade.order.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.lt.constant.redis.RedisUtil;
import com.lt.enums.fund.FundTypeEnum;
import com.lt.enums.trade.TradeDirectionEnum;
import com.lt.model.dispatcher.enums.DispatcherRedisKey;
import com.lt.model.trade.OrderCashInfo;
import com.lt.trade.order.dao.OrderCashInfoDao;
import com.lt.trade.order.service.IOrderBusinessService;
import com.lt.util.error.LTException;
import com.lt.util.error.LTResponseCode;
import com.lt.util.utils.DoubleUtils;
import com.lt.util.utils.StringTools;
import com.lt.vo.product.ProductVo;
import com.lt.vo.trade.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.BoundHashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class OrderBusinessServiceImpl implements IOrderBusinessService {

    Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    @Autowired
    private OrderCashInfoDao orderCashInfoDao;


    @Autowired
    private RedisTemplate<String, JSONObject> jsonRedisTemplate;

    /**
     * TODO
     */
    @Override
    public List<PositionOrderVo> findPositionOrderByUserId(String userId, int fundType) throws LTException {

        if (fundType == FundTypeEnum.CASH.getValue()) {
            return cashFind(userId);
        }
        throw new LTException("资金类型异常。错误参数 fundType:" + fundType);
    }

    @Override
    public List<PositionOrderVo> findPositionOrderByUserAndProduct(String userId, String productCode, int fundType) throws LTException {

        if (fundType == FundTypeEnum.CASH.getValue()) {
            return cashFind(userId, productCode);
        }
        throw new LTException("资金类型异常。错误参数 fundType:" + fundType);
    }

    /**
     * 现金持仓订单
     *
     * @param userId
     * @return
     */
    private List<PositionOrderVo> cashFind(String userId) {
        List<PositionOrderVo> list = orderCashInfoDao.findCashPositionList(userId);
        if (list == null || list.size() == 0) {
            logger.error("用户" + userId + "现金持仓订单不存在");
        }
        return list;
    }

    /**
     * 现金持仓订单
     *
     * @param userId
     * @return
     */
    private List<PositionOrderVo> cashFind(String userId, String productCode) {
        List<PositionOrderVo> list = orderCashInfoDao.findCashPositionListByUserAndProduct(userId, productCode);
        if (list == null || list.size() == 0) {
            logger.error("用户" + userId + "现金持仓订单不存在");
        }
        return list;
    }

    /**
     * 查询所有内/外盘现金持仓订单
     *
     * @param plate
     * @return
     * @author XieZhibing
     * @date 2017年2月8日 下午1:09:53
     * @see com.lt.trade.order.service.IOrderBusinessService#queryAllPositionCashOrders(java.lang.Integer)
     */
    @Override
    public List<OrderCashInfo> queryAllPositionCashOrders(Integer plate) {
        return orderCashInfoDao.queryAllPositionCashOrders(plate);
    }


    private Double calculate(List<FloatAmtOrderVo> list) {
        logger.info(JSONObject.toJSONString(list));
        try {
            Double num = 0.0d;
            Map<String, JSONObject> map = new ConcurrentHashMap<String, JSONObject>();
            Map<String, ProductVo> productTradeConfigMap = new ConcurrentHashMap<String, ProductVo>();
            for (FloatAmtOrderVo orderVo : list) {
                if (orderVo == null) {
                    continue;
                }
                if (!map.containsKey(orderVo.getProductCode())) {
                    JSONObject jsonObject = jsonRedisTemplate.opsForValue().get(DispatcherRedisKey.REDIS_PRODUCT_LAST_QUOTA + orderVo.getProductCode());
                    if (jsonObject == null) {
                        logger.error("redis中没有查询到最新行情 key:" + orderVo.getProductCode());
                        continue;
                    }
                    map.put(orderVo.getProductCode(), jsonObject);
                    //一个点位的价格
                    BoundHashOperations<String, Integer, ProductVo> proTradeCfgRedisHash = redisTemplate.boundHashOps(RedisUtil.PRODUCT_INFO);
                    ProductVo config = proTradeCfgRedisHash.get(orderVo.getProductCode());
                    if (config == null) {
                        logger.error("redis中没有查询到商品配置信息   key:" + RedisUtil.PRODUCT_INFO + "，productCode:" + orderVo.getProductCode());
                        continue;
                    }
                    productTradeConfigMap.put(orderVo.getProductCode(), config);
                }
                //获取最新行情
                JSONObject jsonObject = map.get(orderVo.getProductCode());
                double bidPrice = StringTools.formatDouble(jsonObject.getString("bidPrice1"), 0d);
                double askPrice = StringTools.formatDouble(jsonObject.getString("askPrice1"), 0d);

                //买入均价
                double d = orderVo.getBuyAvgPrice();

                ProductVo config = productTradeConfigMap.get(orderVo.getProductCode());
                logger.info("config = {}", config);
                double jumpPrice = config.getJumpPrice();
                double jumpValue = config.getJumpValue();
                logger.info("========bidPrice={},askPrice={},d={},jumpPrice={},jumpValue={}========", bidPrice, askPrice, d, jumpPrice, jumpValue);
                if (orderVo.getTradeDirection() == TradeDirectionEnum.DIRECTION_UP.getValue()) {
                    //多方向
                    //盈亏  = (当前卖出价  （买一价） - 买入均价)  /   商品一次跳动点位  * 一个点位的价格
                    logger.info("多方向 num={},bidPrice={},d={},jumpValue={},jumpPrice={}", num, bidPrice, d, jumpValue, jumpPrice);
                    num += DoubleUtils.div(DoubleUtils.mul(DoubleUtils.sub(bidPrice, d), jumpPrice), jumpValue);
                }
                if (orderVo.getTradeDirection() == TradeDirectionEnum.DIRECTION_DOWN.getValue()) {
                    //空方向
                    //盈亏  = (买入均价 - 当前买入价 （卖一价）)  /  商品一次跳动点位  * 一个点位的价格
                    logger.info("空方向 num={},askPrice={},d={},jumpValue={},jumpPrice={}", num, askPrice, d, jumpValue, jumpPrice);
                    num += DoubleUtils.div(DoubleUtils.mul(DoubleUtils.sub(d, askPrice), jumpPrice), jumpValue);
                }
            }
            map.clear();
            productTradeConfigMap.clear();
            return num;
        } catch (Exception e) {
            logger.error("计算盈亏异常", e);
            throw new LTException("计算盈亏异常", e);
        }

    }

    /**
     * TODO 现金总盈亏
     *
     * @return
     * @throws LTException
     */
    @Override
    public Double findFloatCashAmtByUserId(OrderCashInfo orderInfo) throws LTException {
        List<FloatAmtOrderVo> list = orderCashInfoDao.findCashFloatAmtOrderList(orderInfo);
        if (list == null || list.size() == 0) {
            return null;
        }
        return calculate(list);
    }

    @Override
    public List<SinglePositionOrderVo> findSinglePositionList(String userId, String productCode, Integer fundType) throws LTException {
        if (FundTypeEnum.CASH.getValue() == fundType) {
            return orderCashInfoDao.findSinglePositionCashOrderList(userId, productCode);
        }
        logger.debug("入参  userId = {}, productCode = {},fundType = {}", userId, productCode, fundType);
        throw new LTException("查不到单品持仓数据");
    }

    /**
     * TODO 查询用户委托中订单个数
     *
     * @param userId
     * @return
     */
    @Override
    public int findEntrustTheOrdersCount(String userId) {
        return orderCashInfoDao.selectEntrustTheOrdersCount(userId);

    }

    /**
     * TODO 查询用户委托中订单
     *
     * @param userId
     * @return
     */
    @Override
    public List<EntrustVo> findEntrustTheOrdersList(String userId) {
        return orderCashInfoDao.selectEntrustTheOrdersList(userId);
    }

    @Override
    public Integer queryOrdersCounts(String userId) {
        return orderCashInfoDao.selectCashOrderCount(userId);
    }

    /**
     * 查询订单持仓数量
     *
     * @param userId
     * @param fundType 1积分、 0现金
     * @return
     */
    @Override
    public Map<String, Map<String, Object>> findPosiOrderCount(String userId, Integer fundType) {
        try {
            Map<String, Map<String, Object>> orderPosiCountMap = new HashMap<String, Map<String, Object>>();
            // 通过找出用户的持仓订单
            List<PositionOrderVo> list = null;
            if (fundType == FundTypeEnum.CASH.getValue()) {
                list = orderCashInfoDao.findCashPositionList(userId);
            }
            if (list != null && list.size() > 0) {
                for (PositionOrderVo fo : list) {
                    String code = fo.getProductCode();
                    Map<String, Object> futuresPosiCountDetailMap = orderPosiCountMap.get(code);
                    if (null == futuresPosiCountDetailMap) {
                        futuresPosiCountDetailMap = new HashMap<String, Object>();
                        futuresPosiCountDetailMap.put("count", 0);
                        futuresPosiCountDetailMap.put("productCode", code);
                    }
                    Integer count = (Integer) futuresPosiCountDetailMap.get("count") + 1;
                    futuresPosiCountDetailMap.put("count", count);
                    orderPosiCountMap.put(code, futuresPosiCountDetailMap);
                }
            }
            return orderPosiCountMap;
        } catch (Exception e) {
            logger.error("查询用户持仓订单数量异常", e);
            throw new LTException(LTResponseCode.OR10003);
        }
    }

    @Override
    public List<OrderBalanceVo> findBalanceRecord(Map<String, Object> map) {
        logger.debug("======>> 查询结算记录, 入参打印:{}", JSONObject.toJSONString(map));
        Integer fundType = (Integer) map.get("fundType");
        if (fundType == FundTypeEnum.CASH.getValue()) {
            return orderCashInfoDao.selectOrderBalanceVo(map);
        }
        throw new LTException("获取结算记录异常");
    }

    @Override
    public List<OrderEntrustVo> findEntrustRecord(Map<String, Object> map) {
        logger.debug("======>> 查询委托记录, 入参打印:{}", JSONObject.toJSONString(map));
        Integer fundType = (Integer) map.get("fundType");
        if (fundType == FundTypeEnum.CASH.getValue()) {
            return orderCashInfoDao.selectOrderEntrustVo(map);
        }
        throw new LTException("获取委托记录异常");
    }

    @Override
    public List<Map<String, Object>> findEntrustAndSuccRecord(String orderId) {
        return orderCashInfoDao.selectEntrustAndSuccess(orderId);
    }

    @Override
    public OrderCashInfo findOrderCashInfoByOrderId(String orderId) {
        return orderCashInfoDao.queryByOrderId(orderId);
    }

}
