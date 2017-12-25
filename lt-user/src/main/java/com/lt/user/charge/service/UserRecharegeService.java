package com.lt.user.charge.service;

import java.util.List;
import java.util.Map;

import com.lt.model.fund.FundOptCode;
import com.lt.model.user.charge.FundAggpayRecharge;
import com.lt.model.user.charge.FundAlipayRecharge;
import com.lt.model.user.charge.FundIapppayRecharge;
import com.lt.model.user.charge.XDPayRecharge;
import com.lt.util.error.LTException;

/**
 * 项目名称：lt-user 类名称：UserRecharegeService 类描述：用户充值逻辑处理包内使用（新） 创建人：yuanxin
 * 创建时间：2017年7月18日 上午11:23:39
 */
public interface UserRecharegeService {

	/**
	 * 获取支付宝充值订单数据
	 * 
	 * @param orderId
	 *            订单id
	 * @return
	 * @throws LTException
	 * @return: FundAlipayRecharge
	 * @throws @author
	 *             yuanxin
	 * @Date 2017年7月18日 上午11:43:31
	 */
	public FundAlipayRecharge getAliChargeByOrderId(String orderId) throws LTException;

	/**
	 * 修改支付宝充值（聚合支付） 返回值
	 * 
	 * @param alipayRecharge
	 * @throws LTException
	 * @return: void
	 * @throws @author
	 *             yuanxin
	 * @Date 2017年7月18日 下午1:57:11
	 */
	public Integer updateAlipayBackInfo(FundAlipayRecharge alipayRecharge) throws LTException;

	/***
	 * 查询支付宝（聚合支付） 待处理数据 查询条件包含 1， limit 条数 2， beginDate 起始时间 3， status 状态 4，
	 * merid 商户号
	 * 
	 * @param paraMap
	 * @return
	 * @throws LTException
	 * @return: List<String>
	 * @throws @author
	 *             yuanxin
	 * @Date 2017年7月19日 下午5:00:12
	 */
	public List<String> getAlipayFWaiteDataIds(Map<String, String> paraMap) throws LTException;

	/**
	 * 发送http请求查询支付宝（聚合支付） 充值结果
	 * 
	 * @param payId
	 *            订单号
	 * @param merid
	 *            商户id
	 * @param key
	 *            私钥
	 * @param noceStr
	 *            随机数
	 * @param qryAction
	 *            查询的链接地址
	 * @throws LTException
	 * @return: void
	 * @throws @author
	 *             yuanxin
	 * @Date 2017年7月19日 下午5:09:42
	 */
	public void queryAlipayFDataByHttp(String payId, String merid, String key, String noceStr, String qryAction) throws LTException;

	/**
	 * 查询支付宝入金结果
	 * 
	 * @param payId
	 * @throws LTException
	 */
	public void queryAipayResult(String payId) throws LTException;

	/**
	 * 查询熙大支付宝支付
	 * 
	 * @param payId
	 * @param merid
	 * @param key
	 * @param noceStr
	 * @param qryAction
	 * @throws LTException
	 */
	public void queryXDPayFDataByHttp(String payId, String merNo, String key, String qryAction) throws LTException;

	/**
	 * 
	 * @param orderId
	 * @return
	 * @throws LTException
	 */
	public XDPayRecharge queryXDPayRechargeByOrderId(String orderNo) throws LTException;

	/**
	 * 更新熙大支付宝处理信息
	 * 
	 * @param XDPayRecharge
	 */
	public Integer updateXDPayRecharge(XDPayRecharge recharge) throws LTException;

	/**
	 * 查询熙大支付宝订单列表
	 * 
	 * @param paraMap
	 * @return
	 * @throws LTException
	 */
	public List<String> getXDPayOrderNoList(Map<String, String> paraMap) throws LTException;

	/**
	 * 查询渠道组id
	 * 
	 * @param channelId
	 *            渠道id
	 * @return
	 */
	public String queryGroupIdByChannelId(String channelId) throws LTException;

	/**
	 * 查询爱贝充值信息
	 * 
	 * @param payId
	 * @return
	 */
	public FundIapppayRecharge getFundIapppayRechargeByPayId(String payId) throws LTException;

	/**
	 * 获取充值code
	 * 
	 * @param thirdOptCode
	 * @return
	 * @throws LTException
	 */
	public FundOptCode getFundOptCodeByThirdOptCode(String thirdOptCode) throws LTException;

	/**
	 * 更新爱贝入金信息
	 * 
	 * @param payId
	 * @return
	 * @throws LTException
	 */
	public void updateFundIapppayRechargeByPayId(FundIapppayRecharge iapppayRecharge) throws LTException;

	/**
	 * 查询支付渠道id
	 * 
	 * @param groupId
	 * @return
	 * @throws LTException
	 */
	public List<String> getChannelIdByGroupId(String groupId) throws LTException;

	/**
	 * 
	 * 查询聚合充值信息
	 * 
	 * @param orderId
	 * @return
	 * @throws LTException
	 */
	public FundAggpayRecharge getFundAggpayRechargeByPayId(String orderId) throws LTException;

	/**
	 * 查询聚合充值信息
	 * 
	 * @param orderId
	 * @return
	 * @throws LTException
	 */
	public FundAggpayRecharge getFundAggpayRechargeByPayIdForUpdate(String orderId) throws LTException;

	/**
	 * 更新聚合支付信息
	 * 
	 * @param aggpayRecharge
	 * @throws LTException
	 */
	public void updateFundAggpayRechargeByPayId(FundAggpayRecharge aggpayRecharge) throws LTException;
	
	
	/**
	 * 查询聚合支付支付列表
	 * @param condition
	 * @return
	 * @throws LTException
	 */
	public List<String> getFundAggpayRechargeListByCondition(Map<String, Object> condition) throws LTException;
	
	
	/**
	 * 查询聚合支付结果
	 * @param orderId
	 * @throws LTException
	 */
	public void queryAggpayResult(String orderId) throws LTException;
}
