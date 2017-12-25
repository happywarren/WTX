package com.lt.user.core.dao.sqldb;


import com.lt.model.user.InvestorAccount;

import java.util.List;

public interface IUserInvestorAccountDao {
	

	/**
	 * 根据证券账户查询id
	 * @param code
	 * @return
	 */
	public Integer selectInvestorAccountIdForCode(String code);
	/**
	 * 列出所有c++服务器配置
	 * @param
	 * @return
	 */
	List<InvestorAccount> listInvestorAccountForServer();
	/**
	 * 根据id列出券商帐号配置
	 * @param
	 * @return
	 */
	InvestorAccount selectInvestorAccountById(Integer id);
}
