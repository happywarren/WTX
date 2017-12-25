/*
 * PROJECT NAME: lt-trade
 * PACKAGE NAME: com.lt.trade.order.dao
 * FILE    NAME: ScoreOrderDao.java
 * COPYRIGHT: Copyright(c) 2017 LT All Rights Reserved.
 */
package com.lt.trade.order.dao;

import com.lt.model.trade.OrderScoreInfo;
import com.lt.vo.trade.*;
import org.apache.ibatis.annotations.Param;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 现金订单数据接口
 *
 * @author XieZhibing
 * @version <b>1.0.0</b>
 * @date 2016年12月8日 下午9:04:16
 */
public interface OrderScoreInfoDao extends Serializable {

    /**
     * 添加现金订单
     *
     * @param ScoreOrdersInfo
     * @author XieZhibing
     * @date 2016年12月12日 下午12:04:01
     */
    public void add(OrderScoreInfo ScoreOrdersInfo);

    /**
     * 更新现金订单
     *
     * @param ScoreOrdersInfo
     * @author XieZhibing
     * @date 2016年12月12日 下午12:04:10
     */
    public int update(OrderScoreInfo ScoreOrdersInfo);

    /**
     * 根据ID查询现金订单
     *
     * @param id
     * @return
     * @author XieZhibing
     * @date 2016年12月12日 下午12:02:51
     */
    public OrderScoreInfo queryById(String id);

    /**
     * 根据订单显示ID查询现金订单
     *
     * @param displayId
     * @return
     * @author XieZhibing
     * @date 2016年12月12日 下午12:02:47
     */
    public OrderScoreInfo queryByOrderId(String orderId);

    /**
     * 用户持仓订单
     *
     * @return
     */
    public List<PositionOrderVo> findScorePositionList(String userId);


    public List<PositionOrderVo> findScorePositionListByUserAndProduct(@Param("userId") String userId, @Param("productCode") String productCode);

    /**
     * 查询所有内/外盘现金持仓订单
     *
     * @param plate
     * @return
     * @author XieZhibing
     * @date 2016年12月16日 下午3:12:08
     */
    public List<OrderScoreInfo> queryAllPositionScoreOrders(Integer plate);

    /**
     * 计算用户持仓订单盈亏使用的对象
     *
     * @param userId
     * @return
     */
    public List<FloatAmtOrderVo> findScoreFloatAmtOrderList(OrderScoreInfo orderinfo);

    /**
     * 单品持仓订单
     *
     * @param userId
     * @param productCode
     * @return
     */
    public List<SinglePositionOrderVo> findSinglePositionScoreOrderList(
            @Param("userId") String userId, @Param("productCode") String productCode);

    /**
     * 查询用户可售的现金订单
     *
     * @param userId
     * @param productCode
     * @return
     * @author XieZhibing
     * @date 2017年1月3日 下午7:13:58
     */
    public List<OrderScoreInfo> queryUserVendibleScoreOrderList(
            @Param("userId") String userId, @Param("productCode") String productCode);

    /**
     * 查询用户委托中订单个数
     *
     * @param userId
     * @return
     */
    public int selectEntrustTheOrdersCount(String userId);

    /**
     * 查询用户委托中订单
     *
     * @param userId
     * @return
     */
    public List<EntrustVo> selectEntrustTheOrdersList(String userId);

    /**
     * 查询用户下单数
     *
     * @param userId
     * @return
     */
    public Integer selectScoreOrderCount(Integer userId);

    /**
     * 查询结算订单信息
     *
     * @param map
     * @return
     */
    public List<OrderBalanceVo> selectOrderBalanceVo(Map<String, Object> map);

    /**
     * 查询委托订单记录信息
     *
     * @param map
     * @return
     */
    public List<OrderEntrustVo> selectOrderEntrustVo(Map<String, Object> map);

    /**
     * 查询委托和成交记录
     *
     * @param orderId
     * @return
     */
    public List<Map<String, Object>> selectEntrustAndSuccess(String orderId);

    /**
     * 更新非递延单系统清仓时间
     *
     * @param orderId
     * @return
     */
    public int updateSysSellDate(@Param("orderId") String orderId, @Param("sysSetSellDate") Date sysSetSellDate);

}
