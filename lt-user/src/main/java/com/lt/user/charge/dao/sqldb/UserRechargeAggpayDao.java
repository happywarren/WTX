package com.lt.user.charge.dao.sqldb;

import java.util.List;
import java.util.Map;

import com.lt.model.user.charge.FundAggpayRecharge;

/**
 * 
 * @项目名称：lt-user 
 * @类名称：UserRechargeAggpayDao
 * @author yubei
 * @date创建时间：2017年6月30日 下午6:22:16
 */

public interface UserRechargeAggpayDao {

	/**
	 * 
	 * 登记聚合支付
	 * 
	 * @param FundAggpayRecharge
	 * @return: void
	 * @throws @author
	 *             yubei
	 * @Date 2017年7月11日 下午7:33:43
	 */
	public void insertAggpayRecharge(FundAggpayRecharge aggRecharge);

	/**
	 * 通过id查询支付宝充值记录
	 * 
	 * @param orderId
	 * @return
	 * @return: FundAlipayRecharge
	 * @throws @author
	 *             yubei
	 * @Date 2017年7月18日 上午11:39:22
	 */
	public FundAggpayRecharge getAggRecharge(String orderId);

	/**
	 * 
	 * 更新聚合支付信息
	 * @param alipayRecharge
	 * @return: void
	 * @throws @author
	 *             yuanxin
	 * @Date 2017年7月18日 下午1:58:30
	 */
	public Integer updateAggpayRecharge(FundAggpayRecharge aggpayRecharge);

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
	public List<String> getAggpayListByCondition(Map<String, Object> condition);

	/**
	 * 
	 * @param payId
	 * @return
	 * @return: FundAlipayRecharge
	 * @throws @author
	 *             yuanxin
	 * @Date 2017年7月19日 下午5:15:13
	 */
	public FundAggpayRecharge getAggpayRechargeForUpdate(String orderId);
}
