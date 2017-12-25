package com.lt.user.charge.dao.sqldb;

import java.util.List;
import java.util.Map;

import com.lt.model.user.charge.FundAlipayRecharge;

/**
 * 
 * 项目名称：lt-user 类名称：UserRechargeAlipayDao 类描述： 充值集中处理dao 创建人：yubei
 * 创建时间：2017年6月30日 下午6:22:16
 */

public interface UserRechargeAlipayDao {

	/**
	 * 插入支付宝()
	 * 
	 * @param alipaymRecharge
	 * @return: void
	 * @throws @author
	 *             yuanxin
	 * @Date 2017年7月11日 下午7:33:43
	 */
	public void insertAlipayApply(FundAlipayRecharge alipayRecharge);

	/**
	 * 通过id查询支付宝充值记录
	 * 
	 * @param orderId
	 * @return
	 * @return: FundAlipayRecharge
	 * @throws @author
	 *             yuanxin
	 * @Date 2017年7月18日 上午11:39:22
	 */
	public FundAlipayRecharge getAlipayRecharge(String orderId);

	/**
	 * 记录支付宝（聚合支付） 回调数据
	 * 
	 * @param alipayRecharge
	 * @return: void
	 * @throws @author
	 *             yuanxin
	 * @Date 2017年7月18日 下午1:58:30
	 */
	public Integer updateAlipayBackInfo(FundAlipayRecharge alipayRecharge);

	/***
	 * 根据条件查询支付宝订单 id
	 * 
	 * @param paraMap
	 *            包含条件 1， limit 条数 2， beginDate 起始时间 3， status 状态 4， merid 商户号
	 * @return
	 * @return: String
	 * @throws @author
	 *             yuanxin
	 * @Date 2017年7月18日 下午10:31:16
	 */
	public List<String> getAlipayFWaitDataIds(Map<String, String> paraMap);

	/**
	 * 
	 * @param payId
	 * @return
	 * @return: FundAlipayRecharge
	 * @throws @author
	 *             yuanxin
	 * @Date 2017年7月19日 下午5:15:13
	 */
	public FundAlipayRecharge getAlipayFByIdLock(String payId);
}
