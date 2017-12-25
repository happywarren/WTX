package com.lt.user.charge.service.impl;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.BoundHashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import com.lt.api.sys.IThreadLockService;
import com.lt.constant.redis.RedisUtil;
import com.lt.enums.sys.SysThreadLockEnum;
import com.lt.user.charge.service.UserRecharegeService;
import com.lt.util.error.LTException;
import com.lt.util.utils.DateTools;
import com.lt.util.utils.StringTools;

/**
 * 项目名称：lt-user 类名称：UserChargeTimeQryServiceImpl 类描述： 创建人：yuanxin
 * 创建时间：2017年7月19日 上午9:08:28
 */
@Service
public class UserChargeTimeQryServiceImpl {

	@Autowired
	private RedisTemplate<String, String> redisTemplate;
	@Autowired
	private UserRecharegeService recharegeService;

	@Autowired
	private IThreadLockService threadLockService;

	private static final Logger logger = LoggerFactory.getLogger(UserCommonRechargeServiceImpl.class);

	public void qryAlipayFResult() {

		logger.info("-------------------开始执行支付宝定时查询接口 --------------------------");

		/** 拼接公共的查询数据 beg */
		BoundHashOperations<String, String, String> sysCfgRedis = redisTemplate.boundHashOps(RedisUtil.SYS_CONFIG);
		// 查询间隔
		String query = sysCfgRedis.get("query_second");

		Map<String, String> paraMap = new HashMap<String, String>();

		/*
		 * StringBuilder bizCode = new StringBuilder(); bizCode.append("(");
		 * bizCode.append("'" + FundThirdOptCodeEnum.ZFBCZ.getThirdLevelCode() +
		 * "',"); bizCode.append("'" +
		 * FundThirdOptCodeEnum.XLPAY1.getThirdLevelCode() + "',");
		 * bizCode.append("'" + FundThirdOptCodeEnum.XLPAY2.getThirdLevelCode()
		 * + "',"); bizCode.append("'" +
		 * FundThirdOptCodeEnum.XLPAY3.getThirdLevelCode() + "',");
		 * bizCode.append("'" + FundThirdOptCodeEnum.XLPAY4.getThirdLevelCode()
		 * + "'"); bizCode.append("'" +
		 * FundThirdOptCodeEnum.XLPAY5.getThirdLevelCode() + "'");
		 * bizCode.append("'" + FundThirdOptCodeEnum.XLPAY6.getThirdLevelCode()
		 * + "'"); bizCode.append("'" +
		 * FundThirdOptCodeEnum.XLPAY7.getThirdLevelCode() + "'");
		 * bizCode.append(")");
		 */
		paraMap.put("status", "2");
		paraMap.put("limit", "100");
		// paraMap.put("bizCode", bizCode.toString());

		paraMap.put("beginDate", DateTools.formatDate(new Date(System.currentTimeMillis() - Long.valueOf(query) * 1000), DateTools.DEFAULT_DATE_TIME));

		List<String> idList = recharegeService.getAlipayFWaiteDataIds(paraMap);
		/** 拼接公共的查询数据 end */

		/** 查询逻辑 beg */
		if (CollectionUtils.isNotEmpty(idList)) {
			for (String id : idList) {
				// logger.info("开发每单发送请求：id:{},merid:{},key:{},noceString:{},action:{}",
				// id, merid, key, noceStr, action);
				// recharegeService.queryAlipayFDataByHttp(id, merid, key,
				// noceStr, action);
				this.recharegeService.queryAipayResult(id);
			}
		}
		/** 查询逻辑 end */
	}

	/**
	 * 熙大支付宝定时查询任务
	 * 
	 * @throws LTException
	 */
	public void queryXDPayResult() throws LTException {
		try {
			/** 检查当前是否已经存在查询任务 **/
			if (!threadLockService.lock(SysThreadLockEnum.XDPPAY_QUERY_TASK.getCode())) {
				return;
			}
			logger.info("【熙大支付宝定时查询任务】start");
			BoundHashOperations<String, String, String> sysCfgRedis = redisTemplate.boundHashOps(RedisUtil.SYS_CONFIG);
			String key = sysCfgRedis.get("xdpay_private_key");
			String queryAction = sysCfgRedis.get("xdpay_query_url");
			String merchantNo = sysCfgRedis.get("xdpay_merchant_no");
			Map<String, String> paraMap = new HashMap<String, String>();
			paraMap.put("status", "WAIT");
			paraMap.put("limit", "100");
			paraMap.put("beginDate", DateTools.formatDate(DateTools.addMinute(new Date(), -120), DateTools.DEFAULT_DATE_TIME));
			List<String> idList = recharegeService.getXDPayOrderNoList(paraMap);
			if (CollectionUtils.isNotEmpty(idList)) {
				for (String orderNo : idList) {
					logger.info("开发每单发送请求：orderNo:{},merchantNo:{},key:{},queryAction:{}", orderNo, merchantNo, key, queryAction);
					recharegeService.queryXDPayFDataByHttp(orderNo, merchantNo, key, queryAction);
				}
			}
			logger.info("【熙大支付宝定时查询任务】end");
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("【熙大支付宝定时查询异常】");
		} finally {
			// 解锁
			threadLockService.unlock(SysThreadLockEnum.XDPPAY_QUERY_TASK.getCode());
		}
	}
	
	/**
	 * 查询聚合支付定时任务
	 * @throws LTException
	 */
	public void queryAggpayResult() throws LTException {
		
		logger.info("【聚合定时查询任务】start");
		BoundHashOperations<String, String, String> sysCfgRedis = redisTemplate.boundHashOps(RedisUtil.SYS_CONFIG);
		// 查询间隔
		String query = sysCfgRedis.get("query_second");
		if(StringTools.isEmail(query)){
			query = "7200";
		}
		Map<String, Object> condition = new HashMap<String, Object>();
		condition.put("dealStatus", "1");
		condition.put("limit", "100");
		condition.put("beginDate", DateTools.formatDate(new Date(System.currentTimeMillis() - Long.valueOf(query) * 1000), DateTools.DEFAULT_DATE_TIME));
		
		List<String> orderIds = this.recharegeService.getFundAggpayRechargeListByCondition(condition);
		if (CollectionUtils.isNotEmpty(orderIds)) {
			for (String orderId : orderIds) {
				this.recharegeService.queryAggpayResult(orderId);
			}
		}
		logger.info("【聚合定时查询任务】end");
	}
}
