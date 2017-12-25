package com.lt.fund.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.lt.model.fund.FundOptCode;

public interface FundOptCodeDao {
	/**
	 * 查询资金流转方式
	 * 
	 * @param firstOptCode
	 * @param secondOptCode
	 * @param thirdOptCode
	 * @return
	 */
	public FundOptCode queryFundOptCodeByCode(@Param("first")String firstOptCode,@Param("second")String secondOptCode,@Param("third")String thirdOptCode);
	
	
	public List<FundOptCode> selectFundOptCodes(FundOptCode fundOptCode);
}
