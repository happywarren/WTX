package com.lt.trade.order.service;

import com.lt.model.trade.OrderScoreInfo;
import com.lt.util.error.LTException;
import com.lt.vo.trade.*;

import java.util.List;
import java.util.Map;

/**
 * 订单业务接口
 * @author guodw
 *
 */
public interface IOrderBusinessService {

	/**
	 * 查询用户持仓订单
	 * @param userId
	 * @param fundType   现金 、积分
	 * @return
	 */
	List<PositionOrderVo> findPositionOrderByUserId(String userId, int fundType);


	/**
	 * 查询用户持仓订单
	 * @param userId
	 * @param fundType   现金 、积分
	 * @return
	 */
	List<PositionOrderVo> findPositionOrderByUserAndProduct(String userId, String productCode, int fundType);


	/**
	 *  查询所有内/外盘积分持仓订单
	 * @param plate
	 * @return
	 */
	public List<OrderScoreInfo> queryAllPositionScoreOrders(Integer plate);


	/**
	 * 积分总盈亏
	 * @param userId
	 * @return
	 * @throws LTException
	 */
	Double findFloatScoreAmtByUserId(OrderScoreInfo orderinfo) throws LTException;



	/**
	 * 查询持仓数量
	 * @param userId
	 * @param fundType
	 * @return
	 */
	Map<String, Map<String, Object>> findPosiOrderCount(String userId,
                                                        Integer fundType);

	/**
	 * 查询用户结算订单信息
	 * @param map
	 * @return
	 */
	public List<OrderBalanceVo> findBalanceRecord(Map<String, Object> map);
	
	/**
	 * 查询用户结算订单信息
	 * @param map
	 * @return
	 */
	public List<OrderEntrustVo> findEntrustRecord(Map<String, Object> map);

	OrderScoreInfo findOrderScoreInfoByOrderId(String orderId);

}
