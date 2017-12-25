package com.lt.manager.dao.user;

import java.util.List;
import java.util.Map;

import com.lt.manager.bean.user.InvestorFeeCfg;

/**
 *券商信息管理dao
 * @author licy
 *
 */
public interface InvestorFeeCfgManageDao {

	
	/**
	 * 添加投资者费用配偶信息
	 * @param param
	 */
	public void insertInvestorFeeCfg(InvestorFeeCfg param);
	
	/**
	 * 修改投资者费用信息
	 * @param param
	 */
	public void updateInvestorFeeCfg(InvestorFeeCfg param);
	
	/***
	 * 删除投资者费用信息
	 * @param param
	 */
	public void deleteInvestorFeeCfg(Integer id);
	
	/***
	 * 查询投资费用信息
	 * @param param
	 */
	public List<InvestorFeeCfg>  selectInvestorFeeCfgPage(InvestorFeeCfg param);
	
	
	/***
	 * 查询投资费用信息符合条件数
	 * @param param
	 */
	public Integer selectInvestorFeeCfgCount(InvestorFeeCfg param);
	
	/***
	 * 根据用户ID查询投资费用信息
	 * @param param
	 */
	
	public InvestorFeeCfg selectInvestorFeeCfg(InvestorFeeCfg param);
	
	
	/***
	 * 根据用户ID查询投资费用信息
	 * @param param
	 */
	
	public InvestorFeeCfg selectInvestorFeeCfgVOByModel(Integer productId);
	
	/**
	 * 通过商品id修改券商配置递延状态
	 * @param map
	 */
	public void updateDeferSattusByProducts(Map<String,Object> map);
	
}
