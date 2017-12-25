package com.lt.user.charge.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.BoundHashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSONObject;
import com.lt.api.fund.IFundAccountApiService;
import com.lt.constant.redis.RedisUtil;
import com.lt.enums.fund.FundCashOptCodeEnum;
import com.lt.enums.fund.FundThirdOptCodeEnum;
import com.lt.model.fund.FundOptCode;
import com.lt.model.user.charge.FundAggpayRecharge;
import com.lt.model.user.charge.FundAlipayRecharge;
import com.lt.model.user.charge.FundIapppayRecharge;
import com.lt.model.user.charge.XDPayRecharge;
import com.lt.user.charge.dao.sqldb.UserCharge1010107Dao;
import com.lt.user.charge.dao.sqldb.UserRechargeAggpayDao;
import com.lt.user.charge.dao.sqldb.UserRechargeAlipayDao;
import com.lt.user.charge.dao.sqldb.UserRechargeDao;
import com.lt.user.charge.dao.sqldb.UserRechargeIapppayDao;
import com.lt.user.charge.service.UserRecharegeService;
import com.lt.util.error.LTException;
import com.lt.util.error.LTResponseCode;
import com.lt.util.utils.DateTools;
import com.lt.util.utils.HttpTools;
import com.lt.util.utils.StringTools;
import com.lt.util.utils.crypt.MD5Util;

import javolution.util.FastMap;

/**
 * 项目名称：lt-user 类名称：UserRechargeServiceImpl 类描述： 充值公共逻辑处理类 创建人：yuanxin
 * 创建时间：2017年7月18日 上午11:37:17
 */
@Service
public class UserCommonRechargeServiceImpl implements UserRecharegeService {

	@Autowired
	private IFundAccountApiService fundAccountServiceImpl;

	/*
	 * @Autowired private UserCharge1010101FDao userChargeAlipayFDao;
	 */

	@Autowired
	private UserRechargeAlipayDao alipayDao;

	@Autowired
	private UserCharge1010107Dao xdPayDao;

	@Autowired
	private UserRechargeDao rechargeDao;

	@Autowired
	private UserRechargeIapppayDao iapppayDao;

	@Autowired
	private UserRechargeAggpayDao aggpayDao;

	@Autowired
	private RedisTemplate<String, String> redisTemplate;

	private static final Logger logger = LoggerFactory.getLogger(UserCommonRechargeServiceImpl.class);

	@Override
	public FundAlipayRecharge getAliChargeByOrderId(String orderId) throws LTException {
		FundAlipayRecharge alipayRecharge = null;
		alipayRecharge = alipayDao.getAlipayRecharge(orderId);
		return alipayRecharge;
	}

	@Override
	@Transactional
	public Integer updateAlipayBackInfo(FundAlipayRecharge alipayRecharge) throws LTException {
		Integer num = alipayDao.updateAlipayBackInfo(alipayRecharge);
		return num;
	}

	@Override
	public List<String> getAlipayFWaiteDataIds(Map<String, String> paraMap) throws LTException {
		return alipayDao.getAlipayFWaitDataIds(paraMap);
	}

	@Override
	@Transactional
	public void queryAlipayFDataByHttp(String payId, String merid, String key, String noceStr, String action) throws LTException {
		logger.info("进入到执行事务方法");
		FundAlipayRecharge alipayRecharge = alipayDao.getAlipayFByIdLock(payId);

		// 存在数据，且支付状态为等待 才执行后续查询
		if (alipayRecharge != null && alipayRecharge.getPayResult() == 2) {
			if (StringTools.isNotEmpty(alipayRecharge.getMerid())) {
				merid = alipayRecharge.getMerid();
				key = alipayRecharge.getSendMsg().split("&")[6];
			}
			// 渠道代码
			String bizCode = alipayRecharge.getBizCode();
			Map<String, String> params = new HashMap<String, String>();

			params.put("merchantOutOrderNo", payId);
			params.put("merid", merid);
			params.put("noncestr", noceStr);

			String org_param = "merchantOutOrderNo=" + payId + "&merid=" + merid + "&noncestr=" + noceStr + "&" + key;
			// +"&key="+key;
			String sign = MD5Util.md5(org_param).toLowerCase();
			params.put("sign", sign);
			logger.info("签名{}" + sign);
			JSONObject json = HttpTools.doPost(action, params);

			if (json == null) {
				logger.info("支付宝（聚合支付） 后台无数据返回 ");
				return;
			}
			logger.info("支付宝（聚合支付）返回参数为 json:{}", json.toString());
			String resultCode = json.getString("code");
			String resultMsg = json.getString("msg");
			if (resultCode != null && !"0000".equals(resultCode)) {
				logger.info("查询失败!" + resultMsg);
				return;
			}
			// 支付宝平台订单号
			String orderNo = json.getString("orderNo");
			// 支付结果
			String payResult = json.getString("payResult");
			// 订单金额
			String orderMoney = json.getString("orderMoney");
			// 支付完成时间
			String payTime = json.getString("payTime");

			// 1 为成功 0 为失败或未支付，状态重复，不作处理
			if ("0".equals(payResult)) {
				logger.info("【支付宝查询结果】支付失败或者未支付");
			}
			if ("1".equals(payResult)) {
				FundAlipayRecharge fundAlipayRecharge = null;
				try {
					fundAlipayRecharge = new FundAlipayRecharge(payId, Integer.valueOf(payResult), orderNo, Double.valueOf(orderMoney), json.toString(), DateTools.toUtilDate(payTime, DateTools.TIME_STAMP_2));
				} catch (Exception e) {
					e.printStackTrace();
					fundAlipayRecharge = new FundAlipayRecharge(payId, Integer.valueOf(payResult), orderNo, Double.valueOf(orderMoney), json.toString(), null);
				}

				Integer num = updateAlipayBackInfo(fundAlipayRecharge);
				if (num != 1) {
					logger.info("支付宝充值记录修改失败或已被操作处理");
					throw new LTException(LTResponseCode.FUY00011);
				}

				/** 修改资金数据 beg */
				FundOptCode code = new FundOptCode();
				code.setFirstOptCode(FundCashOptCodeEnum.RECHARGE.getFirstLevelCode());
				code.setSecondOptCode(FundThirdOptCodeEnum.ZFBCZ.getSecondCode());
				if (StringTools.isNotEmpty(bizCode)) {
					code.setThirdOptCode(bizCode);
				} else {
					code.setThirdOptCode(FundThirdOptCodeEnum.ZFBCZ.getThirdLevelCode());
				}

				fundAccountServiceImpl.doUserRecharge(payId, orderNo, code, Double.valueOf(orderMoney));
			}
		}
	}

	@Override
	@Transactional
	public void queryXDPayFDataByHttp(String payId, String merNo, String key, String qryAction) throws LTException {
		try {
			logger.info("【熙大支付宝查询】订单号：{}" + payId);
			/** 锁定该笔订单号 **/
			XDPayRecharge recharge = this.xdPayDao.selectXDPayRechargeForUpdate(payId);
			if (recharge == null) {
				logger.info("【熙大支付宝查询】当前订单不存在,订单号:{}" + payId);
				return;
			}
			if ("SUCCESS".equals(recharge.getStatus())) {
				logger.info("【熙大支付宝查询】当前订单已处理成功,订单号:{}" + payId);
				return;
			}

			/** 签名 **/
			StringBuffer signParam = new StringBuffer();
			signParam.append("merchant_no=" + merNo);
			signParam.append("&order_no=" + payId);
			signParam.append(key);
			String sign = MD5Util.md5(signParam.toString()).toLowerCase();
			logger.info("【熙大支付宝查询】生成签名:{}" + sign);

			/** 组装参数 **/
			Map<String, String> param = FastMap.newInstance();
			param.put("merchant_no", merNo);
			param.put("order_no", payId);
			param.put("sign", sign);
			JSONObject json = HttpTools.doPost(qryAction, param);

			/** 查询结果处理 **/
			if (json == null) {
				logger.info("【熙大支付宝单笔交易查询】后台无数据返回 ");
				return;
			}
			logger.info("【熙大支付宝单笔交易查询】返回参数为 json:{}", json.toString());
			String queryStatus = json.getString("query_status");

			if (StringTools.isEmpty(queryStatus)) {
				logger.info("【熙大支付宝单笔交易查询】查询状态为空!");
				return;
			}
			if (!"SUCCESS".equals(queryStatus)) {
				logger.info("【熙大支付宝单笔交易查询】查询失败!query_status:{}" + queryStatus);
				return;
			}
			String orderStatus = json.getString("order_status");
			if (StringTools.isEmpty(orderStatus)) {
				logger.info("【熙大支付宝单笔交易查询】订单状态为空!");
				return;
			} else if ("INIT".equals(orderStatus)) {
				logger.info("【熙大支付宝单笔交易查询】正在初始化,订单号:{}" + payId);
				return;
			} else if ("UNPAY".equals(orderStatus)) {
				logger.info("【熙大支付宝单笔交易查询】未支付,订单号:{}" + payId);
				return;
			} else if ("FAIL".equals(orderStatus)) {
				logger.info("【熙大支付宝单笔交易查询】交易失败,订单号:{}" + payId);
				return;
			} else if ("CLOSED".equals(orderStatus)) {
				logger.info("【熙大支付宝单笔交易查询】已关闭,订单号:{}" + payId);
				return;
			} else if ("REFUND".equals(orderStatus)) {
				logger.info("【熙大支付宝单笔交易查询】已退款,订单号:{}" + payId);
				recharge.setStatus(orderStatus);
				this.xdPayDao.updateXDPayRecharge(recharge);
				return;
			} else if ("UNKNOW".equals(orderStatus)) {
				logger.info("【熙大支付宝单笔交易查询】未知,订单号:{}" + payId);
				return;
			} else if ("SUCCESS".equals(orderStatus)) {
				logger.info("【熙大支付宝单笔交易查询】交易成功,订单号:{}" + payId);
				String orgAmount = json.getString("org_amount");
				String hostOrderNo = json.getString("host_order_no");
				String orderTime = json.getString("order_time");
				recharge.setStatus(orderStatus);
				recharge.setHostOrderNo(hostOrderNo);
				recharge.setTransAmount(Double.valueOf(orgAmount));
				if (StringTools.isNotEmpty(orderTime)) {
					recharge.setOrderTime(DateTools.parseDate(orderTime, "yyyyMMddHHmmss"));
				}
				this.xdPayDao.updateXDPayRecharge(recharge);

				/** 修改资金数据 beg */
				FundOptCode code = new FundOptCode();
				code.setFirstOptCode(FundCashOptCodeEnum.RECHARGE.getFirstLevelCode());
				code.setSecondOptCode(FundThirdOptCodeEnum.XDPAY.getSecondCode());
				code.setThirdOptCode(FundThirdOptCodeEnum.XDPAY.getThirdLevelCode());
				fundAccountServiceImpl.doUserRecharge(payId, hostOrderNo, code, Double.valueOf(StringTools.getAmountToYuan(orgAmount)));
				return;
			} else {
				logger.info("【熙大支付宝单笔交易查询】未知,订单号:{}" + payId);
			}

		} catch (Exception e) {
			e.printStackTrace();
			throw new LTException("【熙大支付宝查询异常】");
		}

	}

	@Override
	public XDPayRecharge queryXDPayRechargeByOrderId(String orderNo) throws LTException {
		return this.xdPayDao.selectXDPayRechargeForUpdate(orderNo);
	}

	@Override
	public Integer updateXDPayRecharge(XDPayRecharge recharge) throws LTException {
		return this.xdPayDao.updateXDPayRecharge(recharge);
	}

	@Override
	public List<String> getXDPayOrderNoList(Map<String, String> paraMap) throws LTException {
		return this.xdPayDao.selectXDPayRechargeList(paraMap);
	}

	@Override
	public String queryGroupIdByChannelId(String channelId) {
		try {
			String groupId = this.rechargeDao.selectGroupIdByChannelId(channelId);
			if (StringTools.isEmpty(groupId)) {
				throw new LTException("查询渠道组为空!");
			}
			return groupId;
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("【查询渠道组异常】" + e.getMessage());
			throw new LTException("查询渠道组异常");
		}
	}

	@Override
	public FundIapppayRecharge getFundIapppayRechargeByPayId(String cporderid) {
		try {
			return this.iapppayDao.selectFundIapppayRechargeByCporderid(cporderid);
		} catch (Exception e) {
			e.printStackTrace();
			throw new LTException("查询爱贝充值异常");
		}
	}

	@Override
	public FundOptCode getFundOptCodeByThirdOptCode(String thirdOptCode) throws LTException {
		return this.rechargeDao.selectFundOptCodebyThirdOptCode(thirdOptCode);
	}

	@Override
	public void updateFundIapppayRechargeByPayId(FundIapppayRecharge iapppayRecharge) throws LTException {
		try {
			this.iapppayDao.updateFundIapppayRecharge(iapppayRecharge);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("更新爱贝信息异常");
			throw new LTException("更新爱贝信息异常");
		}

	}

	@Override
	public void queryAipayResult(String payId) throws LTException {
		logger.info("开始查询支付宝结果,订单号：{}", payId);

		FundAlipayRecharge alipayRecharge = alipayDao.getAlipayFByIdLock(payId);

		// 存在数据，且支付状态为等待 才执行后续查询
		if (alipayRecharge != null && alipayRecharge.getPayResult() == 2) {
			// 渠道代码
			String bizCode = alipayRecharge.getBizCode();
			/** 查询配置信息 **/
			BoundHashOperations<String, String, String> rechargeConfig = redisTemplate.boundHashOps(RedisUtil.FUND_RECHARGE_CONFIG);
			String config = rechargeConfig.get(bizCode);
			logger.info("支付宝渠道配置:{}", config);
			JSONObject jsonObject = JSONObject.parseObject(config);
			String merid = jsonObject.getString("mchId");
			String key = jsonObject.getString("secretKey");
			String action = jsonObject.getString("queryUrl");
			String noceStr = StringTools.getUUID().replaceAll("-", "").substring(0, 16);

			/** 签名 **/
			Map<String, String> params = new HashMap<String, String>();
			params.put("merchantOutOrderNo", payId);
			params.put("merid", merid);
			params.put("noncestr", noceStr);

			String org_param = "merchantOutOrderNo=" + payId + "&merid=" + merid + "&noncestr=" + noceStr + "&" + key;
			// +"&key="+key;
			String sign = MD5Util.md5(org_param).toLowerCase();
			params.put("sign", sign);
			logger.info("签名{}" + sign);
			JSONObject json = HttpTools.doPost(action, params);

			if (json == null) {
				logger.info("支付宝后台无数据返回 ");
				return;
			}
			logger.info("支付宝（聚合支付）返回参数为 json:{}", json.toString());
			String resultCode = json.getString("code");
			String resultMsg = json.getString("msg");
			if (resultCode != null && !"0000".equals(resultCode)) {
				logger.info("查询失败!" + resultMsg);
				return;
			}
			// 支付宝平台订单号
			String orderNo = json.getString("orderNo");
			// 支付结果
			String payResult = json.getString("payResult");
			// 订单金额
			String orderMoney = json.getString("orderMoney");
			// 支付完成时间
			String payTime = json.getString("payTime");

			// 1 为成功 0 为失败或未支付，状态重复，不作处理
			if ("0".equals(payResult)) {
				logger.info("【支付宝查询结果】支付失败或者未支付");
			}
			if ("1".equals(payResult)) {
				FundAlipayRecharge fundAlipayRecharge = null;
				try {
					fundAlipayRecharge = new FundAlipayRecharge(payId, Integer.valueOf(payResult), orderNo, Double.valueOf(orderMoney), json.toString(), DateTools.toUtilDate(payTime, DateTools.TIME_STAMP_2));
				} catch (Exception e) {
					e.printStackTrace();
					fundAlipayRecharge = new FundAlipayRecharge(payId, Integer.valueOf(payResult), orderNo, Double.valueOf(orderMoney), json.toString(), null);
				}

				Integer num = updateAlipayBackInfo(fundAlipayRecharge);
				if (num != 1) {
					logger.info("支付宝充值记录修改失败或已被操作处理");
					throw new LTException(LTResponseCode.FUY00011);
				}

				/** 更新资金数据 */
				FundOptCode code = this.rechargeDao.selectFundOptCodebyThirdOptCode(bizCode);
				fundAccountServiceImpl.doUserRecharge(payId, orderNo, code, Double.valueOf(orderMoney));
			}
		}
	}

	@Override
	public List<String> getChannelIdByGroupId(String groupId) throws LTException {
		try {
			return this.rechargeDao.selectChannelIdByGroupId(groupId);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("查询支付渠道id异常");
		}
		return null;
	}

	@Override
	public FundAggpayRecharge getFundAggpayRechargeByPayId(String orderId) throws LTException {
		return this.aggpayDao.getAggRecharge(orderId);
	}

	@Override
	public FundAggpayRecharge getFundAggpayRechargeByPayIdForUpdate(String orderId) throws LTException {
		return this.aggpayDao.getAggpayRechargeForUpdate(orderId);
	}

	@Override
	public void updateFundAggpayRechargeByPayId(FundAggpayRecharge aggpayRecharge) throws LTException {
		this.aggpayDao.updateAggpayRecharge(aggpayRecharge);
	}

	@Override
	public List<String> getFundAggpayRechargeListByCondition(Map<String, Object> condition) throws LTException {
		return this.aggpayDao.getAggpayListByCondition(condition);
	}

	@Override
	@Transactional
	public void queryAggpayResult(String orderId) throws LTException {
		logger.info("开始查询聚合支付结果,订单号：{}", orderId);

		/** 锁定改支付信息 **/
		FundAggpayRecharge aggpayRecharge = this.aggpayDao.getAggpayRechargeForUpdate(orderId);
		if ("0".equals(aggpayRecharge.getDealStatus())) {
			logger.info("此订单已处理,订单号:{}", orderId);
			return;
		}
		/** 查询配置信息 **/
		BoundHashOperations<String, String, String> rechargeConfig = redisTemplate.boundHashOps(RedisUtil.FUND_RECHARGE_CONFIG);
		String config = rechargeConfig.get(aggpayRecharge.getBizCode());
		logger.info("聚合渠道配置:{}", config);
		JSONObject jsonObject = JSONObject.parseObject(config);
		String mchId = jsonObject.getString("mchId");
		String key = jsonObject.getString("secretKey");
		String queryUrl = jsonObject.getString("queryUrl");

		/** 签名 **/
		StringBuilder buff = new StringBuilder();
		buff.append("MerchantID=" + mchId);
		buff.append("&OrderID=" + orderId);
		buff.append("&TradeType=bis.trade.query");
		buff.append("&Key=" + key);
		String sign = MD5Util.md5(buff.toString()).toUpperCase();
		logger.info("签名:{}" + sign);

		/** 发送查询 **/
		Map<String, Object> param = new TreeMap<String, Object>();
		param.put("TradeType", "bis.trade.query");// 交易类型
		param.put("MerchantID", mchId);
		param.put("OrderID", orderId);
		param.put("Sign", sign);
		String result = HttpTools.sendPost(queryUrl, param);

		if (StringTools.isNotEmpty(result)) {
			logger.info("聚合支付无数据返回 ");
			return;
		}
		logger.info("聚合支付返回参数为:{}", result);
		// 查询状态0表示查询成功，1或者其他表示未查询到或者查询失败
		Map<String, Object> resultMap = FastMap.newInstance();
		String data[] = result.split("&");
		for (String s : data) {
			resultMap.put(s.split("=")[0], s.split("=")[1]);
		}
		
		String retStatus = (String)resultMap.get("RetStatus");
		if (!"0".equals(retStatus)) {
			logger.info("查询失败!");
			return;
		}
		// 支付结果
		String dealStatus = (String)resultMap.get("DealStatus");
		// 订单金额
		String orderMoney = (String)resultMap.get("Amt");

		// 0已支付，1未支付，其他失败
		if (!"0".equals(dealStatus)) {
			logger.info("【聚合查询结果】支付失败或者未支付");
		}
		if ("0".equals(dealStatus)) {
			aggpayRecharge.setDealStatus(dealStatus);
			try {
				this.aggpayDao.updateAggpayRecharge(aggpayRecharge);
			} catch (Exception e) {
				throw new LTException("更新信息失败");
			}
			try {
				/** 更新资金数据 */
				FundOptCode code = this.rechargeDao.selectFundOptCodebyThirdOptCode(aggpayRecharge.getBizCode());
				fundAccountServiceImpl.doUserRecharge(orderId, null, code, Double.valueOf(StringTools.getAmountToYuan(orderMoney)));
			} catch (Exception e) {
				logger.error("更新资金异常");
				return;
			}
		}
	}

}
