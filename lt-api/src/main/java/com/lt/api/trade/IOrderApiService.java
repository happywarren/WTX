/*
 * PROJECT NAME: lt-api
 * PACKAGE NAME: com.lt.api.trade
 * FILE    NAME: IOrderApiService.java
 * COPYRIGHT: Copyright(c) 2017 LT All Rights Reserved.
 */
package com.lt.api.trade;

import com.lt.model.trade.OrderCashInfo;
import com.lt.model.user.InvestorAccount;
import com.lt.model.user.log.OrderLossProfitDefLog;
import com.lt.util.error.LTException;
import com.lt.vo.trade.*;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * TODO 订单分布式业务接口
 *
 * @author XieZhibing
 * @version <b>1.0.0</b>
 * @date 2016年12月8日 下午4:10:20
 */
public interface IOrderApiService extends Serializable {

    /**
     * TODO 开仓买入
     *
     * @param orderVo
     * @throws LTException
     * @author XieZhibing
     * @date 2016年12月8日 下午5:38:43
     */
    public void buy(OrderVo orderVo) throws LTException;

    /**
     * TODO 平仓卖出
     *
     * @param orderVo
     * @throws LTException
     * @author XieZhibing
     * @date 2016年12月8日 下午5:42:56
     */
    public void sell(OrderVo orderVo) throws LTException;

    /**
     * TODO 批量平仓
     *
     * @param fundType    资金类型: 0 现金; 1 积分
     * @param userId      用户ID
     * @param productCode 产品编码:CL1702
     * @throws LTException
     * @author XieZhibing
     * @date 2017年1月4日 上午10:20:19
     */
    public void sellAll(Integer fundType, String userId, String productCode) throws LTException;

    /**
     * 用户修改止盈止损，开启关闭递延(现金)
     *
     * @param log        操作日志，存储user_id，ip
     * @param orderId    订单id
     * @param deferStatu 0，开启递延， 1关闭递延
     * @param stopLoss   单笔订单的止损金额
     * @param stopfit    单笔订单的止盈金额
     * @throws LTException
     * @throws
     * @return: void
     * @author yuanxin
     * @Date 2016年12月15日 下午7:58:08
     */
    public void doUpdateCashDefAndLossProfit(OrderLossProfitDefLog log, String orderId, String deferStatu, String stopLoss, String stopfit, String trailStopLoss) throws LTException;


    /**
     * 查询用户现金积分浮动盈亏
     *
     * @param userId
     * @return json {floatCashAmt:0.0d, floatScoreAmt:0.0d}
     */
    public Map<String, Double> findFloatAmtByUserId(String userId);

    /**
     * 查询持仓订单
     *
     * @param userId
     * @param fundType
     * @return
     */
    List<PositionOrderVo> findPositionOrderByUserId(String userId, Integer fundType);

    /**
     * 查询持仓订单
     *
     * @param userId
     * @param productCode
     * @param fundType
     * @return
     */
    List<PositionOrderVo> findPositionOrderByUserAndProduct(String userId, String productCode, Integer fundType);

    /**
     * 查询单品持仓订单数据
     *
     * @param userId
     * @param productCode
     * @param fundType
     * @return
     * @throws LTException
     */
    List<SinglePositionOrderVo> findSinglePositionList(String userId,
                                                       String productCode, Integer fundType) throws LTException;

    /**
     * TODO 查询用户委托中订单个数
     *
     * @param userId
     * @return
     */
    int findEntrustTheOrdersCount(String userId);

    /**
     * TODO 查询用户委托中订单
     *
     * @param userId
     * @return
     */
    List<EntrustVo> findEntrustTheOrdersList(String userId);

    /**
     * 查询用户历史订单数
     *
     * @param userId
     * @return
     */
    public Integer queryOrdersCounts(String userId);

    /**
     * 查询持仓订单数
     *
     * @param userId
     * @param fundType
     * @return
     */
    public Map<String, Map<String, Object>> findPosiOrderCount(String userId,
                                                               Integer fundType);

    // 管理商品信息(当进行新增商品/更换合约等操作时，供管理后台调用)
    // 新增商品时: newProductName为商品名称, 如CL1706或au1706, oldProductName为null
    // 删除商品时: newProductName为null, oldProductName为商品名称, 如CL1706或au1706
    // 更换合约时: newProductName为新合约名称, 如CL1706或au1706, oldProductName为原合约名称, 如CL1702或au1702
    // 如果newProductName为null, oldProductName为null, 则仅从数据库重新加载数据, 不影响风控队列
    public boolean manageOuterProducts(String newProductName, String oldProductName);

    public boolean manageInnerProducts(String newProductName, String oldProductName);

    public boolean manageProducts(Integer plate, String newProductName, String oldProductName);

    /**
     * 查询用户结算订单信息
     *
     * @param map
     * @return
     */
    public List<OrderBalanceVo> findBalanceRecord(Map<String, Object> map);

    /**
     * 查询成交记录加上委托记录
     *
     * @return
     */
    public List<Map<String, Object>> findEntrustAndSuccRecord(String orderId);

    /**
     * 查询用户结算订单信息
     *
     * @param map
     * @return
     */
    public List<OrderEntrustVo> findEntrustRecord(Map<String, Object> map);

    /**
     * 加入风控--后台接口
     *
     * @param cashOrdersInfo
     */
    public void fillRiskControlQueue(OrderCashInfo cashOrdersInfo);

    /**
     * 移除风控--后台接口
     *
     * @param cashOrdersInfo
     */
    public void removeRiskControlOrder(OrderCashInfo cashOrdersInfo);


    /**
     * 结算--后台接口
     *
     * @param cashOrdersInfo
     * @param sellCount
     */
    public void fundBalance(OrderCashInfo cashOrdersInfo, Integer sellCount);


    /**
     * 获取移动止损价
     *
     * @param orderId
     * @return
     */
    public Double getMoveStopLoss(String orderId, String proCode, Integer palte);

    /**
     * 更改递延配置选项
     *
     * @throws
     * @return: void
     * @author yuanxin
     * @Date 2017年5月5日 上午11:00:31
     */
    public void updateDeferTimeConfig();

    /**
     * 添加或删除内存--现金
     *
     * @param orderCashInfo
     */
    public void addOrRemoveOrderInfo(OrderCashInfo orderCashInfo, String flag);


    /**
     * 修改风控订单清仓时间
     *
     * @param productCodes
     */
    public String updateRiskOrderClearTime(String... productCodes);

    public void callOrderClearTime();

    /**
     * 修改券商交易服务器配置
     *
     * @param investorAccount 券商账户配置信息
     */
    void updateInvestorAccountServer(InvestorAccount investorAccount);
    
    /**
     * 查询订单递延费用
     * @param plate
     * @param tradeDirection
     * @param inverstorId
     * @return
     */
    double getOrderDeferredFee(Integer plate,Integer tradeDirection,String investorId,double deferInterest,String productCode);
}
