package com.lt.manager.dao.user;

import java.util.List;

import com.lt.manager.bean.user.InvestorAccount;

/**
 *券商信息管理dao
 * @author licy
 *
 */
public interface InvestorAccountManageDao {

	
	/**
	 * 添加投资者信息
	 * @param param
	 */
	public void insertInvestorAccount(InvestorAccount param);
	
	/**
	 * 修改投资者信息
	 * @param param
	 */
	public void updateInvestorAccount(InvestorAccount param);
	
	/***
	 * 删除投资者信息
	 * @param param
	 */
	public void deleteInvestorAccount(InvestorAccount param);
	
	/***
	 * 查询投资用户
	 * @param param
	 */
	public List<InvestorAccount>  selectInvestorAccount(InvestorAccount param);
	
	
	/***
	 * 查询投资用户数
	 * @param param
	 */
	public Integer selectInvestorAccountCount(InvestorAccount param);
	
	/**
	 * 查询券商--分页
	 * @param param
	 * @return
	 */
	public List<InvestorAccount> selectInvestorAccountPage(InvestorAccount param);
	
	
	/***
	 * 查询投资用户数
	 * @param param
	 */
	public InvestorAccount selectInvestorAccountObj(Integer id);
	
	
	/***
	 * 查询投资用户数
	 * @param param
	 */
	public InvestorAccount selectInvestorAccountObjBySecurityCode(String securityCode);
	
	/**
	 *  查询券商账户列表*
	 * @param securityCode
	 * @return
	 */
	public List<InvestorAccount> selectInvestorAccountList(String securityCode);
}
