/*
 * PROJECT NAME: lt-api
 * PACKAGE NAME: com.lt.api.fund
 * FILE    NAME: IFundCashService.java
 * COPYRIGHT: Copyright(c) 2017 LT All Rights Reserved.
 */ 
package com.lt.fund.service;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.Map;

import com.lt.model.fund.FundFlow;
import com.lt.model.fund.FundIoCashRecharge;
import com.lt.model.fund.FundMainCash;
import com.lt.model.fund.FundOptCode;
import com.lt.model.fund.FundPayAuthFlow;
import com.lt.model.promote.CommisionIo;
import com.lt.model.user.UserBussinessInfo;
import com.lt.model.user.charge.UserChannelTrans;
import com.lt.util.error.LTException;

/**
 * TODO 现金账户业务接口
 * @author XieZhibing
 * @date 2016年11月30日 下午3:42:26
 * @version <b>1.0.0</b>
 */
public interface IFundCashService extends Serializable {

	/**
	 * 
	 * TODO 查询现金订单余额是否充足
	 * @author XieZhibing
	 * @date 2016年12月9日 下午3:02:05
	 * @param userId
	 * @param amount
	 * @return
	 */
	public boolean isCashBalanceEnough(String userId, double amount);
	/**
	 * 
	 * TODO 查询用户现金主账户
	 * @author XieZhibing
	 * @date 2016年12月7日 下午9:52:42
	 * @param userId
	 * @return
	 */
	public FundMainCash queryFundMainCash(String userId);
	
	/**
	 * 
	 * TODO 初始化现金主账户
	 * @author XieZhibing
	 * @date 2016年11月30日 下午8:12:55
	 * @param userId
	 * @return
	 */
	public boolean doInitFundCashAccount(String userId) throws LTException;
	
	/**
	 * 
	 * TODO 查询现金主账余额是否充足
	 * @author XieZhibing
	 * @date 2016年11月30日 下午9:55:36
	 * @param userId	用户ID
	 * @param amount	购买时总费用 = (保证金 + 手续费 + 递延保证金 + 递延费) * 手数
	 * @return true 充足, false 不足
	 */
	public boolean doCheckFundMainBalance(String userId, double amount);
	
	/**
	 * 
	 * TODO 开仓扣款
	 * @author XieZhibing
	 * @date 2016年12月6日 下午1:34:00
	 * @param productName	商品名称
	 * @param orderId	订单ID
	 * @param userId	用户ID
	 * @param holdFund	止损保证金
	 * @param deferFund	递延保证金
	 * @param actualCounterFee	总手续费
	 * @return  
	 */
	public boolean doBuy(String productName, String orderId, String userId,
			double holdFund, double deferFund, double actualCounterFee) throws LTException;
	
	/**
	 * <p>
	 * TODO 平仓结算更新现金账户: </br>
	 * 1. 当用户盈利时, 用户总利润 = 用户实际利润 + 券商利润抽成; </br>
	 * userBenefit == 用户实际利润; investorBenefit == 券商利润抽成; </br>
	 * 2. 当用户亏损时, userBenefit == 用户总亏损, investorBenefit == 0)
	 * </p>
	 * @author XieZhibing
	 * @date 2016年12月7日 下午2:27:47
	 * @param productName	商品名称
	 * @param orderId	订单ID
	 * @param userId	用户ID
	 * @param holdFund	止损保证金
	 * @param deferFund	递延保证金
	 * @param userBenefit	用户净利润
	 * @return
	 * @throws LTException
	 */
	public void doBalance(String productName, String orderId, String userId,
			double holdFund, double deferFund, double userBenefit) throws LTException;
	
	/**
	 * 
	 * TODO 开仓失败退款
	 * @author XieZhibing
	 * @date 2016年12月6日 下午1:34:00
	 * @param productName	商品名称
	 * @param orderId	订单ID
	 * @param userId	用户ID
	 * @param holdFund	止损保证金
	 * @param deferFund	递延保证金
	 * @param actualCounterFee	总手续费
	 * @return  
	 */
	public void doRefund(String productName, String orderId, String userId,
			double holdFund, double deferFund, double actualCounterFee) throws LTException;

	/**
	 * 
	 * TODO 查询止损保证金流水汇总
	 * @author XieZhibing
	 * @date 2017年2月9日 下午3:22:05
	 * @param orderId
	 * @param userId
	 * @return
	 */
	public Double queryHoldFundFlowSum(String orderId, String userId);
	
	/**
	 * 
	 * TODO 查询递延保证金流水汇总
	 * @author XieZhibing
	 * @date 2017年2月9日 下午3:22:36
	 * @param orderId
	 * @param userId
	 * @return
	 */
	public Double queryDeferFundFlowSum(String orderId, String userId);
	
	/**
	 * 
	 * TODO 查询手续费流水汇总
	 * @author XieZhibing
	 * @date 2017年2月9日 下午3:22:43
	 * @param orderId
	 * @param userId
	 * @return
	 */
	public Double queryActualCounterFeeFlowSum(String orderId, String userId);	
//	
//	/**
//	 * 计算账户余额与生成用户止损保证金流水
//	 * @param flowList
//	 * @param userBalance
//	 * @param productName
//	 * @param orderId
//	 * @param userId
//	 * @param holdFund
//	 * @param currentTime
//	 */
//	public void unfreezeOrFreezeHoldfund(List<FundFlow> flowList, double userBalance,
//			String productName, String orderId, String userId,
//			double holdFund,Date currentTime);
//	/**
//	 * 解冻和冻结递延保证金流水
//	 * @param flowList
//	 * @param userBalance
//	 * @param productName
//	 * @param orderId
//	 * @param userId
//	 * @param deferFund
//	 * @param currentTime
//	 */
//	public void unfreezeOrFreezeDeferfund(List<FundFlow> flowList, double userBalance,
//			String productName, String orderId, String userId,
//			 double deferFund, 	Date currentTime);
//	/**
//	 * 用户退回与扣除手续费及流水
//	 * @param flowList
//	 * @param userBalance
//	 * @param productName
//	 * @param orderId
//	 * @param userId
//	 * @param platformFee
//	 * @param investorFee
//	 * @param currentTime
//	 */
//	public void refundFee(List<FundFlow> flowList, double userBalance,
//			String productName, String orderId, String userId,	double platformFee,
//			double investorFee,Date currentTime);
////	
//	/**
//	 * 用户主账户计算
//	 * 退回：参数是负数,扣除：参数是正数 
//	 * @param userFundMainCash
//	 * @param holdFund 止损保证金                 
//	 * @param deferFund 递延保证金                 
//	 * @param platformFee 平台抽取手续费   
//	 * @param investorFee 券商抽取手续费   
//	 */
//	public void userAccountCalculate(FundMainCash userFundMainCash,double holdFund,double deferFund,
//			double platformFee,double investorFee);
//	
	/**
	 * 冻结or解冻券商止盈保证金流水
	 * @param flowList
	 * @param investorBalance
	 * @param stopProfit
	 * @param productName
	 * @param investorId
	 * @param orderId
	 * @param currentTime
	 */
	public void freezeInvestorStopProfit(List<FundFlow> flowList ,double investorBalance,double stopProfit,String productName,String investorId,String orderId,Date currentTime);
	
	/**
	 * 退回or扣款券商抽成手续费
	 * @param flowList
	 * @param investorBalance 券商账号余额
	 * @param investorFee 券商抽成手续费
	 * @param productName
	 * @param investorId
	 * @param orderId
	 * @param currentTime
	 */
	public void refundProrata(List<FundFlow> flowList ,double investorBalance,double investorFee,String productName,String investorId,String orderId,Date currentTime);
	
	/**
	 * 券商主账户计算
	 * 扣款时 主账户 减少金额   所以  investorFee：正数     stopProfit ：负数 //券商余额变动 = （-保证金）     +   手续费抽成  
	 * 退款时 主账户 增加金额   所以  stopProfit：正数   investorFee：负数     //券商余额变动 = 保证金 + （-手续费抽成）   
	 * @param investorFundMainCash
	 * @param stopProfit 保证金
	 * @param investorFee 手续费抽成
	 */
	public void addInvestorAccountDetails(FundMainCash investorFundMainCash,double stopProfit,double investorFee);
	
	/**
	 * 流水及用户、券商主账户更新 
	 * @param flowList
	 * @param userFundMainCash
	 * @param investorFundMainCash
	 */
	public void saveOrUpdateAccount(List<FundFlow> flowList,FundMainCash userFundMainCash,FundMainCash investorFundMainCash);
	
	/**
	 * 递延结息
	 * @param userId
	 * @param productName
	 * @param orderId
	 * @param interest
	 */
	public void deductInterest(String userId, String productName, String orderId,
			double interest);
	
	/**
	 * 银生宝充值
	 * @param userToken
	 * @param amount
	 * @param responseUrl
	 * @return
	 * @throws Exception
	 */
	public Map<String,String> rechargeForUnspay(UserBussinessInfo info, String amount,String rmbAmt,String responseUrl,String bankCode,String thirdOptcode,Double rate) throws LTException;
	
	/**
	 * 生成充值流水
	 * @param payId
	 * @param userId
	 * @param amount
	 * @param transferNumber
	 * @param foc
	 * @return
	 */
	public int addFundIoForRecharge(String payId,String userId,Double amount,Double rmbAmt,String transferNumber,String bankCode,
							FundOptCode foc,String rechargeIdentification,Double rate);
	
	/**
	 * 查询用户资金信息 for update
	 * @param userId
	 * @return
	 */
	public FundMainCash queryFundMainCashForUpdate(String userId);
	
	/**
	 * 银生宝充值回调接口
	 * @param map
	 * @throws Exception
	 */
	public void callbackForUnspay(Map<String, Object> map) throws Exception;

	/**
	 * daddypay充值回调接口
	 * @param map
	 * @throws Exception
	 */
	public Map<String,String> callbackForDaddyPay(Map<String, String> map);
	
	/**
	 * 充值方法（以人民币转美元存储） 适用于人工补单
	 * @param userId
	 * @param rmbAmount 人民币金额
	 * @param rate 利率 人民币对美元
	 * @param fio
	 * @param foc
	 * @return
	 * @throws Exception    
	 * @return:       int    
	 * @throws 
	 * @author        yuanxin
	 * @Date          2017年5月27日 上午9:33:37
	 */
	public int doReCharge(Double amt,Double rmbAmount,FundIoCashRecharge fio,FundOptCode foc) throws Exception;
	
	/**
	 * 充值方法 适用于回调接口
	 * @return    
	 * @return:       int    
	 * @throws 
	 * @author        yuanxin
	 * @Date          2017年5月27日 下午1:47:40
	 */
	public int doRecharge(FundIoCashRecharge fio,FundOptCode foc) throws Exception;

	public int doRecharge(String payId,FundOptCode foc) throws Exception;

	
	public int doDinpayRecharge(FundIoCashRecharge fio,FundOptCode foc) throws Exception;
	/**
	 * 定时任务查询银生宝充值接口
	 * @throws Exception
	 */
	public void queryOrderStatusForUnspay();
	
	/**
	 * 佣金转现
	 * @param io
	 * @throws Exception
	 */
	public void doCommisionWithdraw(CommisionIo io) throws Exception;
	
	/**
	 * 获取出金到期日
	 * @param userId
	 * @param ioId
	 * @return
	 * @throws LTException
	 */
	public Map<String,Object> getWithdrawDoneDate(String userId,Long ioId) throws LTException;
	
	/**
	 * 获取出金记录
	 * @param userId
	 * @return
	 * @throws LTException
	 */
	public List<Map<String,Object>> getWithdrawHistory(Map<String,Object> param) throws LTException;
	
	/**
	 * 获取出金明细
	 * @param userId
	 * @param ioId
	 * @return
	 * @throws LTException
	 */
	public Map<String,Object> getWithdrawHistoryDetail(String userId,Long ioId) throws LTException;
	
	/**
	 * 支付宝充值调用接口
	 * @param userId
	 * @param amt (美元)
	 * @param url
	 * @return
	 * @throws LTException    
	 * @return:       Map<String,Object>    
	 * @throws 
	 * @author        yuanxin
	 * @Date          2017年3月29日 下午5:02:12
	 */
	public Map<String,String> RequestchargeByZfb(String userId,Double amt,String url,Double rate) throws LTException;
	
	/**
	 * 处理支付宝回调接口
	 * @param paraMap
	 * @throws LTException    
	 * @return:       void    
	 * @throws 
	 * @author        yuanxin
	 * @Date          2017年3月30日 上午11:07:12
	 */
	public  void aplipayCallBack(Map<String,Object> paraMap) throws LTException;
	
	/**
	 * 获取快钱的鉴权记录
	 * @param financyPayAuthFlow
	 * @return
	 * @throws LTException    
	 * @return:       List<FundPayAuthFlow>    
	 * @throws 
	 * @author        yuanxin
	 * @Date          2017年4月1日 下午1:23:28
	 */
	public List<FundPayAuthFlow> getFundPayAuthFlow(FundPayAuthFlow financyPayAuthFlow) throws LTException;
	
	/**
	 * 插入充值记录
	 * @param userId 用户id
	 * @param orderId 订单id
	 * @param amt 金额（美元）
	 * @param thirdLevelCode 三级业务码
	 * @throws LTException    
	 * @return:       void    
	 * @throws 
	 * @author        yuanxin
	 * @Date          2017年4月1日 下午2:48:47
	 */
	public void insertRechargeIo(String userId,String orderId,Double amt,Double rmbAmt,Double rate,String transferNum,String bankCode,String thirdLevelCode) throws LTException;
	
	/**
	 * 插入充值记录
	 * @param userId 用户id
	 * @param orderId 订单id
	 * @param amt 金额
	 * @param thirdLevelCode 三级业务码
	 * @throws LTException    
	 * @return:       void    
	 * @throws 
	 * @author        yuanxin
	 * @Date          2017年4月1日 下午2:48:47
	 */		
	public void insertAlipayRechargeIo(String userId,String orderId,Double amt,Double rmbAmt,Double rate,String transferNum,String alipayNum,String bankCode,String thirdLevelCode) throws LTException;
	/**
	 * 修改快钱 签权 记录
	 * @param financyPayAuthFlow
	 * @return
	 * @throws LTException    
	 * @return:       Integer    
	 * @throws 
	 * @author        yuanxin
	 * @Date          2017年4月5日 下午2:04:54
	 */
	public Integer updateFundPayAuthFlow(FundPayAuthFlow financyPayAuthFlow) throws LTException;
	
	/**
	 * 快钱 增加签权记录
	 * @param financyPayAuthFlow
	 * @throws LTException    
	 * @return:       void    
	 * @throws 
	 * @author        yuanxin
	 * @Date          2017年4月5日 下午2:09:59
	 */
	public void addFundPayAuthFlow(FundPayAuthFlow financyPayAuthFlow) throws LTException;
	
	/**
	 * 根据充值id 查询充值记录
	 * @param payId
	 * @return
	 * @throws LTException    
	 * @return:       FundIoCashRecharge    
	 * @throws 
	 * @author        yuanxin
	 * @Date          2017年4月5日 下午2:27:50
	 */
	public FundIoCashRecharge qryFundIoCashRechargeByPayId(String payId) throws LTException;
	
	/**
	 * 设置充值记录失败
	 * @param cashRecharge
	 * @throws LTException    
	 * @return:       void    
	 * @throws 
	 * @author        yuanxin
	 * @Date          2017年4月5日 下午3:20:28
	 */
	public void setFundRechargeIoFail(FundIoCashRecharge cashRecharge) throws LTException;

	/**
	 * 智付支付結果接收
	 * @param map
	 * @throws Exception
	 * @author yubei
	 * @date 2017年6月2日 上午11:18:54
	 */
	public void dinpayAccept(Map<String, Object> map) throws LTException;

	/**
	 * 定时任务查询智付支付結果
	 * @throws Exception
	 */
	public void queryDinpayResult();
	
	/**
	 * 智付登记
	 * @param paramMap
	 * @return
	 * @throws LTException
	 */
	public Map<String, Object> dinpayCreate(Map<String, Object> paramMap) throws LTException;
	
	
	/**
	 * 保存用户渠道充值记录
	 * @param userChannelTrans
	 * @throws LTException
	 */
	public void saveUserChannelTrans(UserChannelTrans userChannelTrans) throws LTException;

	/**
	 * 威富通支付回调
	 * @param map
	 * @throws LTException
	 */
	public void swiftPassCallback(Map<String,String> map)throws  LTException;

	/**
	 * 易宝回调
	 * @param map
	 * @throws LTException
	 */
	public void yibaoCallback(Map<String,String> map)throws LTException;
}
