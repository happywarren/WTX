package com.lt.manager.service.sys;

import java.util.List;

import com.lt.model.sys.RulesOfTransactions;
import com.lt.util.error.LTException;

public interface IRulesOfTransactionsService {

	void saveRulesOfTransactions(RulesOfTransactions rulesOfTransactions)throws LTException;

	int removeById(Integer id)throws LTException;

	void editRulesOfTransactions(RulesOfTransactions rulesOfTransactions)throws LTException;

	List<RulesOfTransactions> selectRulesOfTransactions(
			RulesOfTransactions rulesOfTransactions)throws LTException;

}
