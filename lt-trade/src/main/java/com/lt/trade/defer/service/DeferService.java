package com.lt.trade.defer.service;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import com.lt.trade.defer.bean.NextPeroidOrderInfo;
import com.lt.vo.defer.PeroidOrderHolidayVo;
import com.lt.vo.defer.ProNextTradePeriodVo;

/**   
* 项目名称：lt-trade   
* 类名称：DeferService   
* 类描述：  递延处理服务类
* 创建人：yuanxin   
* 创建时间：2017年4月24日 下午3:41:26      
*/
public interface DeferService {
	
	/**
	 * 获取商品的列表map key ：时间 00:00:00
	 * 				value ：品种 CL ,GC
	 * @return    
	 * @return:       LinkedHashMap<String,String>    
	 * @throws 
	 * @author        yuanxin
	 * @Date          2017年4月24日 下午3:54:25
	 */
	public LinkedHashMap<String,Set<String>> getDeferTimeProduct();
	
	/**
	 * 获取品种对应的递延执行时间列表
	 * @return    
	 * @return:       Map<String,TreeSet<String>>    
	 * @throws 
	 * @author        yuanxin
	 * @Date          2017年5月5日 上午6:01:47
	 */
	public Map<String, TreeSet<String>> getProductClearTime();
	
	/**
	 * 根据短类型查询所有的持仓中的现金订单
	 * @param code 短类型（CU，AU）
	 * @return    
	 * @return:       List<CashOrdersInfo>    
	 * @throws 
	 * @author        yuanxin
	 * @Date          2017年2月8日 上午11:15:29
	 */
	public List<NextPeroidOrderInfo> findAllCashOrderByCode(List<String> code);
	

	/**
	 * 查询对应品种的节假日
	 * @param code
	 * @return    
	 * @return:       List<PeroidOrderHoliday>    
	 * @throws 
	 * @author        yuanxin
	 * @Date          2017年2月8日 下午7:45:09
	 */
	public List<PeroidOrderHolidayVo> findCodeHoliday(List<String> code);
	
	/**
	 * 返回对应的隔天清仓时间段的品种及最大隔天时间和最小隔天时间
	 * @param code
	 * @return    
	 * @return:       Map<String,ProNextTradePeriod>   
	 * @throws 
	 * @author        yuanxin
	 * @Date          2017年2月9日 上午10:26:53
	 */
	public Map<Integer,ProNextTradePeriodVo> findCodeNextDayPeriod(List<String> code);
	
	/**
	 * 获取递延费
	 * @param plate
	 * @param tradeDirection
	 * @param investorId
	 * @return
	 */
	public double getOrderDeferredFee(Integer plate, Integer tradeDirection, String investorId,double deferInterest,String productCode);
	
}
