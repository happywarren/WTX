package com.lt.api.trade;

import com.lt.model.trade.OrderCashInfo;
import com.lt.model.trade.OrderScoreInfo;
import com.lt.model.user.InvestorAccount;
import com.lt.model.user.log.OrderLossProfitDefLog;
import com.lt.util.error.LTException;
import com.lt.vo.trade.OrderBalanceVo;
import com.lt.vo.trade.OrderVo;
import com.lt.vo.trade.PositionOrderVo;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * @Author jwb
 * @Description:
 * @Date: Create in 15:42 2017/10/16
 */
public interface IOrderScoreApiService extends Serializable {

    /**
     *
     * TODO 开仓买入
     * @author XieZhibing
     * @date 2016年12月8日 下午5:38:43
     * @param orderVo
     * @throws LTException
     */
    public void buy(OrderVo orderVo) throws LTException;

    /**
     *
     * TODO 平仓卖出
     * @author XieZhibing
     * @date 2016年12月8日 下午5:42:56
     * @param orderVo
     * @throws LTException
     */
    public void sell(OrderVo orderVo) throws LTException;

    /**
     *
     * TODO 批量平仓
     * @author XieZhibing
     * @date 2017年1月4日 上午10:20:19
     * @param fundType	资金类型: 0 现金; 1 积分
     * @param userId	用户ID
     * @param productCode	产品编码:CL1702
     * @throws LTException
     */
    public void sellAll(Integer fundType, String userId, String productCode) throws LTException;


    /**
     * 查询用户现金积分浮动盈亏
     * @param userId
     * @return json {floatCashAmt:0.0d, floatScoreAmt:0.0d}
     */
    public Map<String, Double> findFloatAmtByUserId(String userId);

    /**
     * 用户修改止盈止损，开启关闭递延(积分)
     * @param log 操作日志，存储user_id，ip
     * @param orderId 订单id
     * @param deferStatu 0，开启递延， 1关闭递延
     * @param stopLoss 单笔订单的止损金额
     * @param stopfit 单笔订单的止盈金额
     * @throws LTException
     * @return:       void
     * @throws
     * @author        yuanxin
     * @Date          2017年2月17日 下午1:41:33
     */
    public void doUpdateScoreDefAndLossProfit(OrderLossProfitDefLog log,String orderId,String deferStatu,String stopLoss,String stopfit,String trailStopLoss) throws LTException;

    /**
     * 查询用户结算订单信息
     * @param map
     * @return
     */
    public List<OrderBalanceVo> findBalanceRecord(Map<String, Object> map);

    /**
     * 查询持仓订单数
     * @param userId
     * @param fundType
     * @return
     */
    public Map<String, Map<String, Object>> findPosiOrderCount(String userId,Integer fundType);

    /**
     * 查询持仓订单
     * @param userId
     * @param fundType
     * @return
     */
    List<PositionOrderVo> findPositionOrderByUserId(String userId, Integer fundType);

    /**
     * 查询持仓订单
     * @param userId
     * @param productCode
     * @param fundType
     * @return
     */
    List<PositionOrderVo> findPositionOrderByUserAndProduct(String userId,String productCode, Integer fundType);

    /**
     * 获取移动止损价
     * @param orderId
     * @return
     */
    public Double getMoveStopLoss(String orderId,String proCode,Integer palte);

    /**
     * 移除风控--后台接口
     * @param
     */
    public void removeRiskControlOrder(OrderScoreInfo scoreOrdersInfo);

    /**
     * 添加或删除内存--积分
     * @param orderScoreInfo
     */
    public void addOrRemoveOrderInfo(OrderScoreInfo orderScoreInfo,String flag);

    /**
     * 结算--后台接口

     */
    public void fundBalance(OrderScoreInfo scoreOrdersInfo, Integer sellCount);

    /**
     * 加入风控--后台接口
     * @param
     */
    public void fillRiskControlQueue(OrderScoreInfo scoreOrdersInfo);

    /**
     * 更改递延配置选项
     *
     * @return:       void
     * @throws
     * @author        yuanxin
     * @Date          2017年5月5日 上午11:00:31
     */
    public void updateDeferTimeConfig();

    public boolean manageProducts(Integer plate,String newProductName, String oldProductName);

    /**
     * 修改风控订单清仓时间
     * @param productCodes
     */
    public String updateRiskOrderClearTime(String ...productCodes);

    public void callOrderClearTime();

    /**
     * 修改券商交易服务器配置
     * @param investorAccount 券商账户配置信息
     *
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
