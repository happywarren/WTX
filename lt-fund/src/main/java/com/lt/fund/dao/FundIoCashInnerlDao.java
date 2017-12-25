package com.lt.fund.dao;

import com.lt.model.fund.FundIoCashInner;

public interface FundIoCashInnerlDao {

	Integer addFundIoCashInnerl(FundIoCashInner fundIoCashInnerl);
	
	Integer addFundIoScoreInnerl(FundIoCashInner fundIoCashInnerl);

	/**
	 * 根据id查询待审核的内部存取数据
	 * @param id
	 * @return    
	 * @return:       FundIoCashInner    
	 * @throws 
	 * @author        yuanxin
	 * @Date          2017年1月18日 下午5:52:13
	 */
	FundIoCashInner queryFundIoCashInnerlById(Long id);

	/**
	 * 修改内部存入
	 * @param info    
	 * @return:       void    
	 * @throws 
	 * @author        yuanxin
	 * @Date          2017年1月19日 上午9:56:12
	 */
	void updateFundIoCashInnerl(FundIoCashInner info);
	
	/**
	 * 根据id查询待审核的内部存取数据
	 * @param id
	 * @return    
	 * @return:       FundIoCashInner    
	 * @throws 
	 * @author        yuanxin
	 * @Date          2017年1月18日 下午5:52:13
	 */
	FundIoCashInner queryFundIoScoreInnerlById(Long id);

	/**
	 * 修改内部存入
	 * @param info    
	 * @return:       void    
	 * @throws 
	 * @author        yuanxin
	 * @Date          2017年1月19日 上午9:56:12
	 */
	void updateFundIoScoreInnerl(FundIoCashInner info);

}
