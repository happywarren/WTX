package com.lt.trade.order.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.lt.api.trade.IOrderApiService;
import com.lt.util.error.LTException;
import com.lt.util.error.LTResponseCode;
import com.lt.util.utils.TokenTools;
import com.lt.util.utils.model.Response;
import com.lt.util.utils.model.Token;
import com.lt.vo.trade.OrderBalanceVo;
import com.lt.vo.trade.OrderEntrustVo;

/**   
* 项目名称：lt-controller   
* 类名称：OrderController   
* 类描述： 订单处理类 
* 创建人：yuanxin   
* 创建时间：2016年12月15日 下午3:29:15      
*/

@Controller
@RequestMapping("/orderBusiness")
public class OrderBusinessController {
	
	Logger logger = LoggerFactory.getLogger(getClass());
	
	@Autowired
	private IOrderApiService orderApiService;

	/**
	 * 查询用户委托记录
	 * @param token
	 * @return
	 */
	@RequestMapping("findEntrustRecord")
	@ResponseBody
	public Response findEntrustRecord(String token,Integer fundType,Integer page,Integer rows){
		if(page == null || page <= 0){
			page = 1;
		}
		if(null == rows || rows <= 0){
			rows = 10;
		}
		List<OrderEntrustVo> data = null;
		try {
			Token  tokens = TokenTools.parseToken(token);
			if(tokens == null || tokens.getUserId().isEmpty()||Integer.valueOf(tokens.getUserId()) <= 0){
				throw new LTException(LTResponseCode.OR10001);
			}
			if(fundType == null ){
				throw new LTException(LTResponseCode.OR10002);
			}
			Integer userId = Integer.valueOf(tokens.getUserId());
			Map<String,Object> map =new  HashMap<String, Object>();
			map.put("limit1", (page-1) * rows);
			map.put("limit2", rows);
			map.put("userId", userId);
			map.put("fundType", fundType);
			data = orderApiService.findEntrustRecord(map);
			
		} catch (LTException e) {
			return LTResponseCode.getCode(e.getMessage(),data);
		}
		return LTResponseCode.getCode(LTResponseCode.SUCCESS,data);
	}
	
	/**
	 * 查询用户结算记录
	 * @param token
	 * @return
	 */
	@RequestMapping("findBalanceRecord")
	@ResponseBody
	public Response findBalanceRecord(String token,Integer fundType,Integer page,Integer rows){
		if(page == null || page <= 0){
			page = 1;
		}
		if(null == rows || rows <= 0){
			rows = 10;
		}
		List<OrderBalanceVo> data = null;
		try {
			Token  tokens = TokenTools.parseToken(token);
			if(tokens == null || tokens.getUserId().isEmpty()||Integer.valueOf(tokens.getUserId()) <= 0){
				throw new LTException(LTResponseCode.OR10001);
			}
			if(fundType == null ){
				throw new LTException(LTResponseCode.OR10002);
			}
			Integer userId = Integer.valueOf(tokens.getUserId());
			Map<String,Object> map =new  HashMap<String, Object>();
			map.put("limit1", (page-1) * rows);
			map.put("limit2", page * rows);
			map.put("userId", userId);
			map.put("fundType", fundType);
			data = orderApiService.findBalanceRecord(map);
			
		} catch (LTException e) {
			return LTResponseCode.getCode(e.getMessage(),data);
		}
		return LTResponseCode.getCode(LTResponseCode.SUCCESS,data);
	}
}
