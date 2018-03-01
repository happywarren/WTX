/*
 * PROJECT NAME: lt-api
 * PACKAGE NAME: com.lt.api.fund
 * FILE    NAME: IFundAccountApiService.java
 * COPYRIGHT: Copyright(c) 2017 LT All Rights Reserved.
 */ 
package com.lt.api.fund;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import com.lt.enums.fund.FundTypeEnum;
import com.lt.enums.fund.IFundOptCode;
import com.lt.model.fund.FundIoCashRecharge;
import com.lt.model.fund.FundIoCashWithdrawal;
import com.lt.model.fund.FundMainCash;
import com.lt.model.fund.FundMainScore;
import com.lt.model.fund.FundOptCode;
import com.lt.model.fund.FundPayAuthFlow;
import com.lt.model.fund.FundVo;
import com.lt.model.promote.CommisionIo;
import com.lt.model.user.UserBussinessInfo;
import com.lt.util.error.LTException;
import com.lt.vo.fund.FundFlowVo;
import com.lt.vo.fund.FundOrderVo;

/**
 * TODO 资金账户业务接口
 * @author XieZhibing
 * @date 2016年11月30日 下午3:42:26
 * @version <b>1.0.0</b>
 */
public interface IFundAccountApiService extends Serializable {
	
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
	 * TODO 查询用户积分主账户
	 * @author XieZhibing
	 * @date 2016年12月7日 下午9:52:42
	 * @param userId
	 * @return
	 */
	public FundMainScore queryFundMainScore(String userId);
	
	
	/**
	 * 
	 * TODO 初始化现金主账户
	 * @author XieZhibing
	 * @date 2016年11月30日 下午8:12:55
	 * @param userId 用户ID
	 * @return
	 */
	public boolean doInitFundCashAccount(String userId) throws LTException;
	
	/**
	 * 
	 * TODO 初始化资积分账户
	 * @author XieZhibing
	 * @date 2016年11月30日 下午8:12:55
	 * @param userId 用户ID
	 * @return
	 */
	public boolean doInitFundScoreAccount(String userId) throws LTException;
	
	/**
	 * TODO 开启/关闭递延
	 * @param flag
	 * @param fundOrderVo
	 * @throws LTException
	 */
	public void updateDefer(boolean flag,FundOrderVo fundOrderVo) throws LTException;
	
	
	/**
	 * 止盈止损修改
	 * FundOrderVo:{
	 * <ul>
	 * <li>productName:商品名称, </li>
	 * <li>orderId:订单ID </li>
	 * <li>userId:用户ID, </li>
	 * <li>holdFund:止损保证金 </li>
	 * <li>deferFund:递延保证金, </li>
	 * <li>platformFee:平台抽取手续费 </li>
	 * <li>investorId:券商ID, </li>
	 * <li>stopProfit:止盈保证金 </li>
	 * <li>investorFee:券商抽取手续费  </li>
	 * </ul>
	 * }
	 * @param fundOrderVo
	 * @throws LTException
	 */
	public void updateProfitLoss(FundOrderVo fundOrderVo) throws LTException;
	
	/**
	 * 止盈止损+递延
	 * @param flag
	 * @param fundOrderVo
	 * @throws LTException
	 */
	public void updateDeferAndProfitLoss(boolean flag,FundOrderVo fundOrderVo) throws LTException;

	/**
	 * 内部  存入/取出  申请
	 * @param userId
	 * @param freezeAmount
	 * @param FundCashOptCodeEnum
	 * @param fundType
	 */
	public void manualOutOrIn(String orderId,String userId, Double freezeAmount,
			IFundOptCode FundCashOptCodeEnum,FundTypeEnum fundType,String thirdOptCode,String remark,Double rmbAmount,Integer modifyUserId);
	
	/**
	 * 银生宝充值
	 * @param userToken
	 * @param amount
	 * @param responseUrl
	 * @return
	 * @throws Exception
	 */
	public Map<String,String> rechargeForUnspay(UserBussinessInfo info,String amount,String rmbAmt,String responseUrl,String bankCode,String thirdOptcode,Double rate) throws LTException;
	
	/**
	 * 银生宝充值回调接口
	 * @param map
	 * @throws Exception
	 */
	public void callbackForUnspay(Map<String, Object> map) throws Exception;

	/**
	 * daddyPay充值回调接口
	 * @param map
	 * @throws Exception
	 */
	public Map<String,String> callbackForDaddyPay(Map<String, String> map);
	
	/**
	 * 提现申请
	 * @param userId
	 * @param outAmount
	 * @param sign 本次操作标记
	 * @return
	 * @throws Exception
	 */
	public Map<String,String> doWithdrawApply(String userId,Double outAmount,Double rate,
													String withdrawCode,String faxCode,boolean flag,String sign) throws LTException;
	
	/**
	 * 提现成功
	 * @param fio
	 * @throws Exception
	 */
	public void withdrawSuccess(FundIoCashWithdrawal fio,String thirdOptCode) throws Exception;
	
	/**
	 * 提现失败
	 * @param fio
	 * @throws Exception
	 */
	public void withdrawFail(FundIoCashWithdrawal fio) throws Exception;
	
	/**
	 * 获取用户积分现金可以余额，持仓保证金
	 * @param userId
	 * @return
	 */
	FundVo findUserFund(String userId)throws LTException;
	
	/**
	 * 提现处理 
	 * @param id 提现id
	 * @param modifyUserId 处理用户ID
	 * @param status 状态
	 * @param thirdOptCode （暂无）
	 * @param remark 备注
	 * @throws LTException    
	 * @return:       void    
	 * @throws 
	 * @author        yuanxin
	 * @Date          2017年1月10日 上午9:19:55
	 */
	public  void fundDoAudit(Long id,Integer modifyUserId, Integer status,
			IFundOptCode thirdOptCode, String remark) throws LTException;
	
	/**
	 * 提现处理(单条修改) 
	 * @param id 提现id
	 * @param modifyUserId 处理用户ID
	 * @param status 状态
	 * @param thirdOptCode （暂无）
	 * @param remark 备注
	 * @param amount 实际手续费
	 * @throws LTException    
	 * @return:       void    
	 * @throws 
	 * @author        yuanxin
	 * @Date          2017年1月10日 上午9:19:55
	 */
	public  void fundDoAuditSingle(Long id,Integer modifyUserId, Integer status,
			IFundOptCode thirdOptCode,Double amount, String remark) throws LTException;

	/**
	 * 	内部存取审核
	 * @param id
	 * @param userId
	 * @param modifyUserId
	 * @param status
	 * @param thirdOptCode
	 * @param remark
	 * @throws LTException    
	 * @return:       void    
	 * @throws 
	 * @author        yuanxin
	 * @Date          2017年1月11日 上午10:38:16
	 */
	public  void fundInnerDoAudit(Long id,Integer status, String remark,Integer modifyUserId) throws LTException;
	
	/**
	 * 	内部存取审核
	 * @param id
	 * @param userId
	 * @param modifyUserId
	 * @param status
	 * @param thirdOptCode
	 * @param remark
	 * @throws LTException    
	 * @return:       void    
	 * @throws 
	 * @author        yuanxin
	 * @Date          2017年1月11日 上午10:38:16
	 */
	public  void scoreInnerDoAudit(Long id,Integer status,String remark,Integer modifyUserId) throws LTException;

	/**
	 * 查询用户资金明细
	 * @param map
	 * @param fundType 
	 * @return
	 */
	public List<FundFlowVo> findFundFollowByUserId(Map<String, Object> map, Integer fundType);


	/**
	 * 查询订单资金明细
	 * @param map
	 * @param fundType
	 * @return
	 */
	public List<FundFlowVo> findFundFollowByOrder(Map<String, Object> map, Integer fundType);
	
	/**
	 * 补单确认
	 * @param userId 用户id
	 * @param actAmount 补单金额（人民币）
	 * @param fio 充值对象
	 * @param foc 枚举类
	 * @throws LTException    
	 * @return:       void    
	 * @throws 
	 * @author        yuanxin
	 * @Date          2017年1月16日 下午4:20:31
	 */
	public void repairFinancyIo(Double actAmount,Double rmbAmt, FundIoCashRecharge fio, FundOptCode foc)  throws LTException;

	
	/**
	 * 佣金转现
	 * @param io
	 * @throws Exception
	 */
	public void commisionWithdraw(CommisionIo io) throws Exception;

	
	/**
	 * 根据递延费金额修改用户余额
	 * @param userId 用户id
	 * @param productName 品种名称
	 * @param orderId  订单id（订单显示id）
	 * @param fee 费用
	 * @throws LTException    
	 * @return:       void    
	 * @throws 
	 * @author        yuanxin
	 * @Date          2017年2月9日 下午1:47:56
	 */
	public void updateFundByPeriodFee(String userId,String productName,String orderId, Double fee) throws LTException;
	
	/**
	 * 根据递延费金额修改用户积分余额
	 * @param userId
	 * @param productName
	 * @param orderId
	 * @param fee
	 * @throws LTException    
	 * @return:       void    
	 * @throws 
	 * @author        yuanxin
	 * @Date          2017年2月16日 下午8:11:48
	 */
	public void updateScoreByPeriodFee(String userId,String productName,String orderId, Double fee) throws LTException;
	
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
	 * 提现撤销
	 * @param userId
	 * @param ioId
	 * @throws LTException
	 */
	public void WithdrawCancel(String userId,Long ioId) throws LTException;
	
	/**
	 * 支付宝充值产生充值链接及回调接口
	 * @param userId
	 * @param amt (前台传值单位 美元)
	 * @param url
	 * @return
	 * @throws LTException    
	 * @return:       Map<String,String>    
	 * @throws 
	 * @author        yuanxin
	 * @Date          2017年3月29日 下午4:32:42
	 */
	public Map<String,String> createAlipayUrl(String userId,Double amt,String url,Double rate) throws LTException;
	
	/**
	 * 处理支付宝的回传接口
	 * @param paraMap
	 * @throws LTException    
	 * @return:       void    
	 * @throws 
	 * @author        yuanxin
	 * @Date          2017年3月30日 上午10:32:29
	 */
	public void reviceZfbResponse(Map<String,Object> paraMap) throws LTException;
	
	/**
	 * 支付宝转账接口
	 * @param paraMap
	 * @throws LTException    
	 * @return:       void    
	 * @throws 
	 * @author        yuanxin
	 * @Date          2017年4月1日 下午5:06:23
	 */
	public void zfbTransfer(Map<String,Object> paraMap) throws LTException;
	
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
	public void insertRechargeIo(String userId,String orderId,Double amt,Double rmbAmt,String transferNum,String bankCode,String thirdLevelCode,Double rate) throws LTException;
	
	/**
	 * 支付宝充值
	 * @param userId 用户id
	 * @param orderId 订单id
	 * @param amt 金额
	 * @param transferNum 转账银行卡号
	 * @param alipayNum 支付宝账号
	 * @param thirdLevelCode 三级业务码
	 * @param rate 利率
	 * @throws LTException    
	 * @return:       void    
	 * @throws 
	 * @author        yuanxin
	 * @Date          2017年6月13日 下午2:07:08
	 */
	public void insertAlipayRechargeIo(String userId,String orderId,Double amt,Double rmbAmt,String transferNum,String alipayNum,String bankCode,String thirdLevelCode,Double rate) throws LTException;
	/**
	 * 查看是否存在快钱签权记录
	 * @param financyPayAuthFlow
	 * @return
	 * @throws LTException    
	 * @return:       Boolean    
	 * @throws 
	 * @author        yuanxin
	 * @Date          2017年4月5日 上午9:39:52
	 */
	public Boolean isExistPayAuthFlow(FundPayAuthFlow financyPayAuthFlow) throws LTException;
	
	/**
	 * 查询快钱签权记录
	 * @param queryPayAuthFlowList
	 * @return
	 * @throws LTException
	 */
	public List<FundPayAuthFlow> queryFundPayAuthFlowList(FundPayAuthFlow financyPayAuthFlow) throws LTException;	
	/**
	 * 快钱 修改签权记录
	 * @param financyPayAuthFlow
	 * @return
	 * @throws LTException    
	 * @return:       Integer    
	 * @throws 
	 * @author        yuanxin
	 * @Date          2017年4月5日 下午2:07:52
	 */
	public Integer updateFundPayAuthFlow(FundPayAuthFlow financyPayAuthFlow) throws LTException;
	
	/**
	 * 快钱 增加签权记录
	 * @param financyPayAuthFlow
	 * @throws LTException    
	 * @return:       void    
	 * @throws 
	 * @author        yuanxin
	 * @Date          2017年4月5日 下午2:09:16
	 */
	public void addFinancyPayAuthFlow(FundPayAuthFlow financyPayAuthFlow) throws LTException;
	
	/**
	 * 快钱充值方法
	 * @param payId 订单id
	 * @param rmbAmt 人民币金额
	 * @throws LTException    
	 * @return:       void    
	 * @throws 
	 * @author        yuanxin
	 * @Date          2017年5月27日 下午1:39:09
	 */
	public void doUserKQrecharge(String payId,Double rmbAmt) throws LTException;
	
	/***
	 * 用户充值回调处理方法
	 * @param payId 支付订单id
	 * @param code 业务码
	 * @param rmbAmt 人民币金额
	 * @throws LTException    
	 * @return:       void    
	 * @throws 
	 * @author        yuanxin
	 * @Date          2017年7月18日 下午2:09:41
	 */
	public void doUserRecharge(String payId,String externalNo, FundOptCode code, Double rmbAmt) throws LTException;
	/**
	 * 充值失败
	 * @param payId
	 * @param failReason
	 * @throws LTException    
	 * @return:       void    
	 * @throws 
	 * @author        yuanxin
	 * @Date          2017年4月5日 下午3:19:04
	 */
	public void setfailRechargeIo(String payId,String failReason) throws LTException;
	
	/**
	 * 获取用户待日汇总的
	 * @param userId
	 * @return
	 * @throws LTException    
	 * @return:       Double    
	 * @throws 
	 * @author        yuanxin
	 * @Date          2017年4月10日 上午11:32:27
	 */
	public Double getDailyUserRechargeTotalAmt(String userId) throws LTException;
	
	/**
	 * 获取平安银行充值方式用户一年剩余可充值金额
	 * @param userId
	 * @return
	 * @throws LTException    
	 * @return:       Double    
	 * @throws 
	 * @author        yuanxin
	 * @Date          2017年5月8日 下午7:12:01
	 */
	public Double getPingAnRechargeAmtInYear(String userId) throws LTException;
	
	/**
	 * 根据支付id 查询充值记录
	 * @param payId
	 * @return
	 * @throws LTException    
	 * @return:       FundIoCashRecharge    
	 * @throws 
	 * @author        yuanxin
	 * @Date          2017年4月20日 下午5:35:37
	 */
	public FundIoCashRecharge qryFundIoCashRechargeByPayId(String payId) throws LTException;
	
	/**
	 * 平安充值
	 * @param paraMap
	 * @throws LTException    
	 * @return:       void    
	 * @throws 
	 * @author        yuanxin
	 * @Date          2017年5月8日 下午7:53:28
	 */
	public void pingAnTransfer(Map<String, Object> paraMap) throws LTException;
	
	/**
	 * 智付支付結果接收
	 * @param map
	 * @throws LTException
	 * @author yubei
	 * @dete 2017年六月2日 上午11:07
	 */
	public void dinPayAccept(Map<String, Object> map) throws LTException;	
	
	
	/**
	 * 登记智付信息
	 * @param info
	 * @param amount
	 * @param responseUrl
	 * @param thirdOptcode
	 * @param rate
	 * @throws Exception
	 */
	public Map<String, Object> dinPayCreate(Map<String, Object> paramMap) throws LTException;
	
	/**
	 * 查询现金充值流水记录列表
	 * @param fundIoCashRecharge
	 * @return
	 * @throws LTException
	 * @author yubei
	 */
	public List<FundIoCashRecharge> queryFundIoCashRechargeList(FundIoCashRecharge fundIoCashRecharge) throws LTException;

	/**
	 * 威富通支付回调
	 * @param map
	 * @throws LTException
	 */
	public void swiftPassCallback(Map<String,String> map)throws LTException;

	/**
	 *
	 * 易宝提现回调
	 * @param map
	 * @throws LTException
	 */
	public void callbackForYiBao(Map<String,String> map)throws LTException;
}
