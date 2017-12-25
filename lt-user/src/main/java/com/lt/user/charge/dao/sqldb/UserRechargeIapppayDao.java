package com.lt.user.charge.dao.sqldb;

import java.util.List;
import java.util.Map;

import com.lt.model.user.charge.FundIapppayRecharge;

/**
 * 爱贝充值
 * 
 * @author yubei
 *
 */
public interface UserRechargeIapppayDao {

	/**
	 * 登记爱贝充值
	 * 
	 * @param iapppayRecharge
	 */
	public void insertFundIapppayRecharge(FundIapppayRecharge iapppayRecharge);

	/**
	 * 更新爱贝充值
	 * 
	 * @param iapppayRecharge
	 */
	public void updateFundIapppayRecharge(FundIapppayRecharge iapppayRecharge);
	
	/**
	 * 锁爱贝充值
	 * @return
	 */
	public FundIapppayRecharge selectFundIapppayRechargeByCporderidForUpdate(String cporderid);
	
	/**
	 * 查询爱贝充值
	 * @param iapppayRecharge
	 * @return
	 */
	public FundIapppayRecharge selectFundIapppayRechargeByCporderid(String cporderid);
	
	
	/**
	 * 查询爱贝列表
	 * @param param
	 * @return
	 */
	public List<FundIapppayRecharge> selectFundIapppayRechargeList(Map<String, Object> param);
}
