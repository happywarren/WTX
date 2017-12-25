package com.lt.manager.service.impl.sys;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.lt.manager.dao.sys.RulesOfTransactionsDao;
import com.lt.manager.service.sys.IRulesOfTransactionsService;
import com.lt.model.sys.RulesOfTransactions;
import com.lt.util.error.LTException;
import com.lt.util.error.LTResponseCode;
import com.lt.util.utils.StringTools;

@Service
public class RulesOfTransactionsServiceImpl implements
		IRulesOfTransactionsService {

	@Autowired
	private RulesOfTransactionsDao rulesOfTransactionsDao;
	
	@Override
	public void saveRulesOfTransactions(RulesOfTransactions rulesOfTransactions)
			throws LTException {
		this.saveOrUpdate(rulesOfTransactions);

	}

	@Override
	public int removeById(Integer id) throws LTException {
		return rulesOfTransactionsDao.delete(id);
	}

	@Override
	public void editRulesOfTransactions(RulesOfTransactions rulesOfTransactions)
			throws LTException {
		this.saveOrUpdate(rulesOfTransactions);
		
	}

	private void saveOrUpdate(RulesOfTransactions rulesOfTransactions) {
		if(!StringTools.isNotEmpty(rulesOfTransactions)){
			throw new LTException(LTResponseCode.FU00003);
		}
		if(StringTools.isNotEmpty(rulesOfTransactions.getId())&&rulesOfTransactions.getId()>0){
			rulesOfTransactionsDao.update(rulesOfTransactions);
		}else{
			RulesOfTransactions rot = new RulesOfTransactions();
			rot.setShortCode(rulesOfTransactions.getShortCode());
			List<RulesOfTransactions> list = rulesOfTransactionsDao.selectByParam(rot);
			if(StringTools.isNotEmpty(list)&&list.size()>0){
				throw new LTException(LTResponseCode.NAY00001);
			}
			rulesOfTransactionsDao.add(rulesOfTransactions);
		}
	}

	@Override
	public List<RulesOfTransactions> selectRulesOfTransactions(
			RulesOfTransactions rulesOfTransactions) throws LTException {
		// TODO Auto-generated method stub
		return rulesOfTransactionsDao.selectByParam(rulesOfTransactions);
	}

}
