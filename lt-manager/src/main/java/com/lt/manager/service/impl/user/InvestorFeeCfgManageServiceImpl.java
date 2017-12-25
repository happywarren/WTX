package com.lt.manager.service.impl.user;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.github.pagehelper.Page;
import com.lt.manager.bean.user.InvestorFeeCfg;
import com.lt.manager.dao.user.InvestorFeeCfgManageDao;
import com.lt.manager.service.user.IInvestorFeeCfgManageService;

/**
 * 银行卡管理实现类
 * @author licy
 *
 */
@Service
public class InvestorFeeCfgManageServiceImpl implements IInvestorFeeCfgManageService{

	@Autowired
	private InvestorFeeCfgManageDao investorFeeCfgManageDao;
	
	@Override
	public void addInvestorFeeCfg(InvestorFeeCfg param) throws Exception {
		investorFeeCfgManageDao.insertInvestorFeeCfg(param);
	}

	@Override
	public void editInvestorFeeCfg(InvestorFeeCfg param) throws Exception {
		investorFeeCfgManageDao.updateInvestorFeeCfg(param);
	}

	@Override
	public void removeInvestorFeeCfg(Integer id) throws Exception {
		// TODO
		investorFeeCfgManageDao.deleteInvestorFeeCfg(id);
	}
	
	@Override
	public Page<InvestorFeeCfg> findInvestorFeeCfg(InvestorFeeCfg param) throws Exception {
		// TODO
		Page<InvestorFeeCfg> page = new Page<InvestorFeeCfg>();
		page.setPageNum(param.getPage());
		page.setPageSize(param.getRows());
		page.addAll(investorFeeCfgManageDao.selectInvestorFeeCfgPage(param));
		page.setTotal(investorFeeCfgManageDao.selectInvestorFeeCfgCount(param));
		return page;
	}
	
	public List<InvestorFeeCfg> findInvestorFeeCfgForProduct(InvestorFeeCfg param) throws Exception {
		
		List<InvestorFeeCfg> list = investorFeeCfgManageDao.selectInvestorFeeCfgPage(param);
		
		return list;
	}
	
	@Override
	public InvestorFeeCfg getInvestorFeeCfg(InvestorFeeCfg param) throws Exception {
		return investorFeeCfgManageDao.selectInvestorFeeCfg(param);
	}
	
	
	@Override
	public InvestorFeeCfg getInvestorFeeCfgByModel(Integer productId) throws Exception {
		return investorFeeCfgManageDao.selectInvestorFeeCfgVOByModel(productId);
	}
	
	@Override
	public List findInvestorFeeCfgList(InvestorFeeCfg param) throws Exception {
		return investorFeeCfgManageDao.selectInvestorFeeCfgPage(param);
	}
	
}
