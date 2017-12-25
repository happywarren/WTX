package com.lt.manager.service.impl.user;

import java.util.List;

import com.lt.api.trade.IOrderApiService;
import com.lt.api.trade.IOrderScoreApiService;
import com.lt.manager.bean.user.InvestorAccount;
import com.lt.manager.dao.user.ProductAccountMapperDao;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.github.pagehelper.Page;
import com.lt.api.user.IUserApiBussinessService;
import com.lt.manager.dao.user.InvestorAccountManageDao;
import com.lt.manager.service.user.IInvestorAccountManageService;
import com.lt.model.user.UserServiceMapper;
import com.lt.util.error.LTException;
import com.lt.util.error.LTResponseCode;
import com.lt.util.utils.StringTools;

/**
 * 银行卡管理实现类
 * @author licy
 *
 */
@Service
public class InvestorAccountManageServiceImpl implements IInvestorAccountManageService{

	@Autowired
	private InvestorAccountManageDao investorAccountManageDao;
	@Autowired
	private ProductAccountMapperDao productAccountMapperDao;
	@Autowired
	private IUserApiBussinessService userApiBussinessService;
	@Autowired
	private IOrderApiService orderApiService;
	@Autowired
	private IOrderScoreApiService orderScoreApiService;

	@Override
	@Transactional
	public void addInvestorAccount(InvestorAccount param) throws Exception {
/*		InvestorAccount account = investorAccountManageDao.selectInvestorAccountObjBySecurityCode(param.getSecurityCode());
		if(StringTools.isNotEmpty(account)){
			throw new LTException(LTResponseCode.US04102);
		}*/
		//新增券商信息
		investorAccountManageDao.insertInvestorAccount(param);
		//新增券商服务
		UserServiceMapper userServiceMapper = new UserServiceMapper();
		userServiceMapper.setServiceCode("1002");
		userServiceMapper.setUserId(param.getUserId());
		userServiceMapper.setStatus("1");
		try {
			userApiBussinessService.activeUserService(userServiceMapper);
		} catch (LTException e) {
			if(!e.getMessage().equals(LTResponseCode.US04102)){
				throw new LTException(e.getMessage());
			}
		}
		finally {
			//实时连接c++交易服务器
			if(param.getIsOpen() != null
					&& param.getIsOpen() == 1){
				com.lt.model.user.InvestorAccount investorAccount = new com.lt.model.user.InvestorAccount();
				investorAccount.setServerIp(param.getServerIp());
				investorAccount.setServerPort(param.getServerPort());
				investorAccount.setSecurityCode(param.getSecurityCode());
				investorAccount.setPlateType(param.getPlateType());
				orderApiService.updateInvestorAccountServer(investorAccount);
				orderScoreApiService.updateInvestorAccountServer(investorAccount);
			}
		}
	}

	@Override
	public void editInvestorAccount(InvestorAccount param) throws Exception {
		InvestorAccount account = investorAccountManageDao.selectInvestorAccountObj(Integer.parseInt(param.getId()));
		investorAccountManageDao.updateInvestorAccount(param);
		//如果修改证券帐号则需要同时修改关联表中的帐号
		if(StringUtils.isNotBlank(param.getSecurityCode())
				&& !StringUtils.equals(account.getSecurityCode(), param.getSecurityCode())){
			productAccountMapperDao.updateProductAccountMapperBySecurityCode(param.getUserId(), account.getSecurityCode(), param.getSecurityCode());
		}

		//实时连接c++交易服务器
		if(param.getIsOpen() != null
				&& param.getIsOpen() == 1){
			if(account != null){
				com.lt.model.user.InvestorAccount investorAccount = new com.lt.model.user.InvestorAccount();
				investorAccount.setServerIp(account.getServerIp());
				investorAccount.setServerPort(account.getServerPort());
				investorAccount.setSecurityCode(account.getSecurityCode());
				investorAccount.setPlateType(account.getPlateType());
				orderApiService.updateInvestorAccountServer(investorAccount);
                orderScoreApiService.updateInvestorAccountServer(investorAccount);
			}

		}

	}

	@Override
	@Transactional
	public void removeInvestorAccount(InvestorAccount param) throws Exception {
		InvestorAccount investorAccount = investorAccountManageDao.selectInvestorAccountObj(Integer.parseInt(param.getId()));
		investorAccountManageDao.deleteInvestorAccount(param);
		if(investorAccount != null){
			productAccountMapperDao.deleteMapperBySecurityCodeAndInvestorId(investorAccount.getSecurityCode(), investorAccount.getUserId());
		}
	}
	
	@Override
	public List<InvestorAccount> findInvestorAccount(InvestorAccount param) throws Exception {
		// TODO
		return  investorAccountManageDao.selectInvestorAccount(param);
		
	}
	@Override
	public InvestorAccount getInvestorAccountObj(String id) throws Exception{
		return investorAccountManageDao.selectInvestorAccountObj(Integer.valueOf(id));
	}
	@Override
	public InvestorAccount getInvestorAccountObjBySecurityCode(String securityCode) throws Exception{
		return investorAccountManageDao.selectInvestorAccountObjBySecurityCode(securityCode);
	}

	@Override
	public Page<InvestorAccount> findInvestorAccountPage(InvestorAccount param) {
		Page<InvestorAccount> page = new Page<InvestorAccount>();
		page.setPageNum(param.getPage());
		page.setPageSize(param.getRows());
		page.addAll(investorAccountManageDao.selectInvestorAccountPage(param));
		page.setTotal(investorAccountManageDao.selectInvestorAccountCount(param));
		return page;
	}

	@Override
	public List<InvestorAccount> findInvestorAccountList(String serviceCode) {
		return investorAccountManageDao.selectInvestorAccountList(serviceCode);
	}
	
}
