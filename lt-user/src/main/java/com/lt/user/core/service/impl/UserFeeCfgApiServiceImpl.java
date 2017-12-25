package com.lt.user.core.service.impl;


import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Random;

import com.lt.model.user.InvestorAccount;
import com.lt.util.utils.StringTools;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.alibaba.fastjson.JSONObject;
import com.lt.api.user.IInvestorFeeCfgApiService;
import com.lt.enums.trade.TradeDirectionEnum;
import com.lt.model.user.InvestorFeeCfg;
import com.lt.user.core.dao.sqldb.IUserFeeCfgDao;
import com.lt.user.core.dao.sqldb.IUserInvestorAccountDao;
import com.lt.util.error.LTException;
import com.lt.util.error.LTResponseCode;


@com.alibaba.dubbo.config.annotation.Service
public class UserFeeCfgApiServiceImpl implements IInvestorFeeCfgApiService {
	
	Logger logger = LoggerFactory.getLogger(getClass());
	
	@Autowired
	private IUserFeeCfgDao iUserFeeCfgDao;
	
	@Autowired
	private IUserInvestorAccountDao userInvestorAccountDao;
	
	@Override
	public InvestorFeeCfg getInvestorFeeCfg(String userId,Integer productId,Integer plateType,Integer direct) throws LTException {			
			if(productId == null){
				throw new LTException(LTResponseCode.PR00001);
			}
			
			InvestorFeeCfg feeCfgObj = new InvestorFeeCfg();
			if(direct != TradeDirectionEnum.DIRECTION_DOWN.getValue() && direct != TradeDirectionEnum.DIRECTION_UP.getValue()){
				throw new LTException("方向不正确");
			}
			logger.info("userId={},productId={},plateType={},direct={}",userId,productId,plateType,direct);
			List<InvestorFeeCfg> feeCfgObjList = iUserFeeCfgDao.selectInvestorFeeCfg(userId,productId,plateType,direct);
		
			if(!feeCfgObjList.isEmpty()){
				int totalWeight = 0; 
				//统计权重之和
				for(InvestorFeeCfg  feeCfgOb: feeCfgObjList){
					int weight = feeCfgOb.getWeight();
					totalWeight +=weight;
				}
				//排序：权重从小到大
				Collections.sort(feeCfgObjList, new Comparator<InvestorFeeCfg>(){
					public int compare(InvestorFeeCfg cfg1,InvestorFeeCfg cfg2) {
						return cfg1.getWeight().compareTo(cfg2.getWeight());
					}
				});
				
				if(totalWeight <= 0){
					//如果权重设置为为0，则不需要出票
					throw new LTException(LTResponseCode.USL2000);
				}
				
				//权重分配
				Random rand = new Random();
				
				int randWeight = rand.nextInt(totalWeight)+1;
				  
				for(InvestorFeeCfg  invaccobj: feeCfgObjList){
					int curWeight = invaccobj.getWeight();
					randWeight = randWeight - curWeight;
					if(randWeight <= 0){
						feeCfgObj = invaccobj;
						break; 
					}
				}
			}
		
		return feeCfgObj;
	}

	@Override
	public InvestorFeeCfg getInvestorFeeCfgScore(String investorId, Integer productId, Integer plateType, Integer supportDirect) throws LTException {
		if(productId == null){
			throw new LTException(LTResponseCode.PR00001);
		}

		if(supportDirect != TradeDirectionEnum.DIRECTION_DOWN.getValue() && supportDirect != TradeDirectionEnum.DIRECTION_UP.getValue()){
			throw new LTException("方向不正确");
		}

		return iUserFeeCfgDao.selectInvestorFeeCfgScore(investorId, productId, plateType, supportDirect);
	}

	@Override
	public InvestorFeeCfg getInvestorFeeCfgSecurityCode(String investorId, Integer productId, String securityCode, Integer supportDirect) throws LTException {
		if(productId == null || StringTools.isEmpty(securityCode)){
			throw new LTException(LTResponseCode.PR00001);
		}

		if(supportDirect != TradeDirectionEnum.DIRECTION_DOWN.getValue() && supportDirect != TradeDirectionEnum.DIRECTION_UP.getValue()){
			throw new LTException("方向不正确");
		}

		InvestorFeeCfg investorFeeCfg = iUserFeeCfgDao.selectInvestorFeeCfgBySecurityCode(investorId, productId, securityCode, supportDirect);

		if(investorFeeCfg == null){
			return new InvestorFeeCfg();
		}
		else{
			return investorFeeCfg;
		}
	}

	@Override
	public InvestorFeeCfg getInvestorFeeCfgByProId(Integer productId, String investorId) throws LTException {
		InvestorFeeCfg feeCfgObj = new InvestorFeeCfg();
			 feeCfgObj = iUserFeeCfgDao.selectInvestorFeeCfgByProId(productId, investorId);
			 logger.debug("返回的对象为：{}",JSONObject.toJSONString(feeCfgObj));
		     return feeCfgObj;
	}
	
	@Override
	public Integer getInvestorAccountId(String securityCode){
		return userInvestorAccountDao.selectInvestorAccountIdForCode(securityCode);
	}

	@Override
	public List<InvestorAccount> listInvestorAccountForServer() {
		return userInvestorAccountDao.listInvestorAccountForServer();
	}

	@Override
	public InvestorAccount selectInvestorAccountById(Integer id) {
		return userInvestorAccountDao.selectInvestorAccountById(id);
	}
}
