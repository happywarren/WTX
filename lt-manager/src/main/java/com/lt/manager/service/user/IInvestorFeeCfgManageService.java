package com.lt.manager.service.user;

import java.util.List;

import com.github.pagehelper.Page;
import com.lt.manager.bean.user.InvestorFeeCfg;
/**
 * 用户管理service
 * @author licy
 *
 */
public interface IInvestorFeeCfgManageService {

	/**
	 * 添加券商费用配置信息
	 * @param param
	 * @return
	 * @throws Exception
	 */
	public void addInvestorFeeCfg(InvestorFeeCfg param) throws Exception;
	
	/**
	 * 编辑券商费用配置信息
	 * @param param
	 * @return
	 * @throws Exception
	 */
	public void editInvestorFeeCfg(InvestorFeeCfg param) throws Exception;

	/**
	 * 删除券商费用配置信息
	 * @param param
	 * @return
	 * @throws Exception
	 */
	public void removeInvestorFeeCfg(Integer id) throws Exception;
	
	/**
	 * 查询券商费用配置信息
	 * @param param
	 * @return
	 * @throws Exception
	 */
	public Page<InvestorFeeCfg> findInvestorFeeCfg(InvestorFeeCfg param) throws Exception;
	
	
	/**
	 * 后获得某个券商费用配置信息
	 * @param param
	 * @return
	 * @throws Exception
	 */
	public InvestorFeeCfg getInvestorFeeCfg(InvestorFeeCfg param) throws Exception;
	
	/**
	 * 后获得某个券商费用配置信息
	 * @param param
	 * @return
	 * @throws Exception
	 */
	public InvestorFeeCfg getInvestorFeeCfgByModel(Integer productId) throws Exception;
	
	
	/**
	 * 查询券商费用配置信息
	 * @param param
	 * @return
	 * @throws Exception
	 */
	public List<InvestorFeeCfg> findInvestorFeeCfgList(InvestorFeeCfg param) throws Exception;
	
	public List<InvestorFeeCfg> findInvestorFeeCfgForProduct(InvestorFeeCfg param) throws Exception;
	
	
	

}
