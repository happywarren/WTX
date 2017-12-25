package com.lt.manager.service.user;

import java.util.List;

import com.github.pagehelper.Page;
import com.lt.manager.bean.user.InvestorAccount;
/**
 * 用户管理service
 * @author licy
 *
 */
public interface IInvestorAccountManageService {

	/**
	 * 添加券商信息
	 * @param param
	 * @return
	 * @throws Exception
	 */
	public void addInvestorAccount(InvestorAccount param) throws Exception;
	
	/**
	 * 编辑券商信息
	 * @param param
	 * @return
	 * @throws Exception
	 */
	public void editInvestorAccount(InvestorAccount param) throws Exception;

	/**
	 * 删除券商信息
	 * @param param
	 * @return
	 * @throws Exception
	 */
	public void removeInvestorAccount(InvestorAccount param) throws Exception;
	
	/**
	 * 查询券商信息
	 * @param param
	 * @return
	 * @throws Exception
	 */
	public List<InvestorAccount> findInvestorAccount(InvestorAccount param) throws Exception;
	
	
	/**
	 * 获取券商信息
	 * @param param
	 * @return
	 * @throws Exception
	 */
	public InvestorAccount getInvestorAccountObj(String id) throws Exception;
	/**
	 * 获取券商信息
	 * @param param
	 * @return
	 * @throws Exception
	 */
	public InvestorAccount getInvestorAccountObjBySecurityCode(String securityCode) throws Exception;
	
	/**
	 * 查询券商信息---分页
	 * @param param
	 * @return
	 */
	public Page<InvestorAccount> findInvestorAccountPage(InvestorAccount param);
	
	/**
	 * 查询券商账户列表
	 * @param serviceCode
	 * @return
	 */
	public List<InvestorAccount> findInvestorAccountList(String serviceCode);
}
