package com.lt.business.core.service.sys.lmpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.lt.business.core.dao.sys.IRulesOfTransactionsDao;
import com.lt.business.core.service.sys.IRulesOfTransactionsService;
import com.lt.model.sys.RulesOfTransactions;


@Service
public class RulesOfTransactionsServiceImpl implements
		IRulesOfTransactionsService {

	@Autowired
	private IRulesOfTransactionsDao rulesOfTransactionsDao;

	@Override
	public RulesOfTransactions select(String shortCode) {
		return rulesOfTransactionsDao.select(shortCode);
	}
	
	
}