package com.lt.api.settle;

import java.util.Map;

import com.lt.model.trade.OrderCashInfo;
import com.lt.util.utils.model.Response;

/**   
* 项目名称：lt-api   
* 类名称：SettleMentServiceInter   
* 类描述：结算对外入口   
* 创建人：yuanxin   
* 创建时间：2017年3月15日 上午10:05:05      
*/
public interface SettleMentApiService {
	
	/**
	 * 对外接口处理类
	 * @param paraMap “service” ： 调用的服务名称
	 * 				  Object : object传递的参数服务对象
	 * @return    
	 * @return:       Response    
	 * @throws 
	 * @author        yuanxin
	 * @Date          2017年3月19日 下午10:43:32
	 */
	public Response execute(Map<String,Object> paraMap) ;
}
