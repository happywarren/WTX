package com.lt.business.core.dao.sys;

import com.lt.model.sys.RulesOfTransactions;

public interface IRulesOfTransactionsDao {
	
	RulesOfTransactions select(String shortCode);
}
