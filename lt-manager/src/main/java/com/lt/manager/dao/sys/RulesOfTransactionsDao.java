package com.lt.manager.dao.sys;

import java.util.List;

import com.lt.model.sys.RulesOfTransactions;

public interface RulesOfTransactionsDao {

	List<RulesOfTransactions> select(RulesOfTransactions rulesOfTransactions);

	List<RulesOfTransactions> selectByParam(RulesOfTransactions rulesOfTransactions);

	void add(RulesOfTransactions rulesOfTransactions);

	void update(RulesOfTransactions rulesOfTransactions);

	int delete(Integer id);

}
