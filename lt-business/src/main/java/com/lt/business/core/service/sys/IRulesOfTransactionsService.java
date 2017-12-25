package com.lt.business.core.service.sys;

import com.lt.model.sys.RulesOfTransactions;

public interface IRulesOfTransactionsService {
	
	RulesOfTransactions select(String shortCode);
}
