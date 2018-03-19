package com.lt.api.user.charge;

import java.util.Map;

import com.lt.util.error.LTException;

/**
 * 项目名称：lt-api 类名称：IUserApiRechargeService 类描述： 用户充值处理接口 创建人：yuanxin
 * 创建时间：2017年6月29日 下午3:09:42
 */
public interface IUserApiRechargeService {
	/**
	 * 公共充值接口 （包含返回code，及msg）
	 * 
	 * @param paraMap
	 * @return
	 * @throws LTException
	 * @return: Map<String,Object>
	 * @throws @author
	 *             yuanxin
	 * @Date 2017年6月29日 下午3:05:18
	 */
	public Map<String, Object> userChargeFund(Map<String, Object> paraMap) throws LTException;

	/**
	 * 支付宝（聚合支付） 回调接口处理逻辑
	 * 
	 * @param map
	 * @throws LTException
	 * @return: void
	 * @throws @author
	 *             yuanxin
	 * @Date 2017年7月18日 上午10:02:03
	 */
	public void userReviceZfbFResponse(Map<String, Object> map) throws LTException;

	/**
	 * 熙大支付宝回调处理
	 * 
	 * @param map
	 *            请求参数
	 * @throws LTException
	 */
	public void userReviceXDPayResponse(Map<String, Object> map) throws LTException;

	/**
	 * 钱通支付回调
	 * 
	 * @param map
	 * @throws LTException
	 */
	public void userReviceQTPayResponse(Map<String, Object> map) throws LTException;

	/**
	 * 爱贝充值回调处理
	 * 
	 * @param map
	 * @throws LTException
	 */
	public void userReviceIapppayResponse(Map<String, Object> map) throws LTException;
	
	/**
	 * 聚合支付回调处理
	 * @param map
	 * @throws LTException
	 */
	public void userReviceAggpayResponse(Map<String, Object> map) throws LTException;
	
	/**
	 * 钱通第二次支付确认接口
	 * 
	 * @param map
	 * @throws LTException
	 */
	public Map<String,Object> qianTongPayConfirm(String checkCode,String payOrderId) throws LTException;

	/**
	 * 支付宝h5支付回调处理
	 * @param map
	 * @throws LTException
	 */
	public void userReviceAlipayH5Response(Map<String, Object> map) throws LTException;

}
