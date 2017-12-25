package com.lt.fund.dao;

import java.util.List;

import com.lt.model.fund.FundPayAuthFlow;

public interface FundPayAuthFlowDao {
	
	public void addFinancyPayAuthFlow(FundPayAuthFlow financyPayAuthFlow);

	public List<FundPayAuthFlow> getFinancyPayAuthFlow(FundPayAuthFlow financyPayAuthFlow);
	
	public Integer updateFinancyPayAuthFlow(FundPayAuthFlow financyPayAuthFlow);
}
