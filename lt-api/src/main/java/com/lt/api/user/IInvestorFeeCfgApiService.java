package com.lt.api.user;

import com.lt.model.user.InvestorAccount;
import com.lt.model.user.InvestorFeeCfg;
import com.lt.util.error.LTException;

import java.util.List;

public interface IInvestorFeeCfgApiService {
	/**
	 * 根据用户ID、产品ID、内外盘、支持方向返回对应产品的证券账户、服务器IP、端口、密码和费用配置
	 * 
	 */
	public InvestorFeeCfg getInvestorFeeCfg(String userId,Integer productId,Integer plateType,Integer supportDirect) throws LTException;

	/**
	 * 查询积分专用账户相关信息
	 * @param productId 产品id
	 * @param plateType 内外盘标记
	 * @param supportDirect 多、空方向
	 * @return
	 * @throws LTException
	 */
	public InvestorFeeCfg getInvestorFeeCfgScore(String investorId, Integer productId,Integer plateType,Integer supportDirect) throws LTException;

	/**
	 * 查询积分专用账户相关信息
	 * @param investorId 券商id
	 * @param productId 产品id
	 * @param securityCode 证券帐号
	 * @param supportDirect 多、空方向
	 * @return
	 * @throws LTException
	 */
	public InvestorFeeCfg getInvestorFeeCfgSecurityCode(String investorId, Integer productId, String securityCode, Integer supportDirect) throws LTException;
	/**
	 * 
	 * TODO 查询券商的商品费用配置
	 * @date 2017年2月3日 下午5:49:53
	 * @param productId
	 * @param investorId
	 * @return
	 * @throws LTException
	 */
	public InvestorFeeCfg getInvestorFeeCfgByProId(Integer productId, String investorId) throws LTException;

	/**
	 * 查询券商ID
	 * @param securityCode
	 * @return
	 */
	Integer getInvestorAccountId(String securityCode);

	/**
	 * 列出所有c++服务器配置
	 * @param
	 * @return
	 */
	List<InvestorAccount> listInvestorAccountForServer();

	/**
	 * 根据id列出券商配置
	 * @param
	 * @return
	 */
	InvestorAccount selectInvestorAccountById(Integer id);
	
}
