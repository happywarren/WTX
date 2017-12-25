package com.lt.trade.order.service;

import java.util.List;
import java.util.Map;

import com.lt.model.trade.OrderCashInfo;
import com.lt.util.error.LTException;
import com.lt.vo.trade.EntrustVo;
import com.lt.vo.trade.OrderBalanceVo;
import com.lt.vo.trade.OrderEntrustVo;
import com.lt.vo.trade.PositionOrderVo;
import com.lt.vo.trade.SinglePositionOrderVo;

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
	List<PositionOrderVo> findPositionOrderByUserAndProduct(String userId, String productCode,int fundType);

	/**
	 *  查询所有内/外盘现金持仓订单
	 * @param plate
	 * @return
	 */
	public List<OrderCashInfo> queryAllPositionCashOrders(Integer plate);
	

	/**
	 * 现金总盈亏
	 * @return
	 * @throws LTException
	 */
	Double findFloatCashAmtByUserId(OrderCashInfo orderinfo) throws LTException;


	/**
	 * 用户单品持仓订单
	 * @param userId   
	 * @param productCode 商品合约
	 * @param fundType   现金 /积分
	 * @return
	 */
	List<SinglePositionOrderVo> findSinglePositionList(String userId,
			String productCode, Integer fundType)throws LTException;

	
	
	
	
	/**
	 * TODO 查询用户委托中订单个数
	 * @param userId
	 * @return
	 */
	int findEntrustTheOrdersCount(String userId);

	/**
	 * TODO 查询用户委托中订单
	 * @param userId
	 * @return
	 */
	List<EntrustVo> findEntrustTheOrdersList(String userId);
	
	/**
	 * 查询用户下单数
	 * @param userId
	 * @return
	 */
	public Integer queryOrdersCounts(String userId);

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
	
	/**
	 * 查询成交记录加上委托记录
	 * @return
	 */
	public  List<Map<String,Object>> findEntrustAndSuccRecord(String orderId);
	
	OrderCashInfo findOrderCashInfoByOrderId(String orderId);


}
