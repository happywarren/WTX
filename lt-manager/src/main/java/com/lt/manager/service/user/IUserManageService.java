package com.lt.manager.service.user;

import java.util.List;
import java.util.Map;

import com.github.pagehelper.Page;
import com.lt.manager.bean.user.BankChannelVo;
import com.lt.manager.bean.user.UserBase;
import com.lt.manager.bean.user.UserBaseInfoQueryVO;
/**
 * 用户管理service
 * @author licy
 *
 */
import com.lt.model.user.log.UserOperateLog;
import com.lt.util.error.LTException;
public interface IUserManageService {

	/**
	 * 查询用户--用于分页
	 * @param param
	 * @return
	 * @throws Exception
	 */
	
	public Page<UserBase> queryUserInfoPage(UserBaseInfoQueryVO param) throws Exception;
	
	/**
	 * 查询券商列表
	 * @param param
	 * @return
	 * @throws Exception
	 */
	public Page<UserBase> queryInvestorList(UserBaseInfoQueryVO param) throws Exception;
	
	/**
	 * 查询黑名单用户--用于分页
	 * @param param
	 * @return
	 * @throws Exception
	 */
	
	public Page<UserBase> queryUserBlackListPage(UserBaseInfoQueryVO param) throws Exception;
	
	/**
	 * 查询实名审核用户--用于分页
	 * @param param
	 * @return
	 * @throws Exception
	 */
	
	public Page<UserBase> queryUserRealNameListPage(UserBaseInfoQueryVO param) throws Exception;
	/**
	 * 
	 * @param param
	 * @return
	 * @throws Exception 
	 */
	
	public Integer queryUserInfoCount(UserBaseInfoQueryVO param) throws Exception;
	
	/**
	 * 更新用户信息
	 * @param param
	 * @return
	 * @throws Exception 
	 */
	
	public void updateUserBase(UserBaseInfoQueryVO param) throws Exception;


	/**
	 * 用户所属券商修改
	 * @return
	 */
	public void updateUserInvestor(String userId,String investorAccountId);
	
	/**
	 * 更新用户信息
	 * @param param
	 * @return
	 * @throws Exception 
	 */
	
	public void removeUserBase(String id) throws Exception;
	/**
	 * 更新用户信息
	 * @param param
	 * @return
	 * @throws Exception 
	 */
	
	public UserBase getUserBase(String id) throws Exception;
	

	/**
	 * 更新用户信息
	 * @param param
	 * @return
	 * @throws Exception 
	 */
	
	public UserBase getUserPic(String id) throws Exception;
	
	/**
	 * 查询用户操作日志
	 * @param userId
	 * @return
	 * @throws Exception    
	 * @return:       UserOperateLog    
	 * @throws 
	 * @author        yuanxin
	 * @Date          2017年1月13日 下午2:26:59
	 */
	public UserOperateLog getUserLastLoginLog(UserOperateLog log) throws Exception; 
	
	/**
	 * 获取用户的每日充值金额
	 * @param userId
	 * @return
	 * @throws LTException    
	 * @return:       Map<String,Double>    
	 * @throws 
	 * @author        yuanxin
	 * @Date          2017年6月16日 下午5:26:09
	 */
	public Map<String,Double> getUserDailyAmt(String userId,Double rate) throws LTException;
	
	/***
	 * 根据用户查询对应的银行卡及渠道信息
	 * @param userId
	 * @return
	 * @throws LTException    
	 * @return:       List<BankChannelVo>    
	 * @throws 
	 * @author        yuanxin
	 * @Date          2017年6月16日 下午6:12:51
	 */
	public List<Map<String, Object>> getBankChannelVoList(String userId) throws LTException;
	
	
	/**
	 * 查询用户开户步骤
	 * @param userId
	 * @return
	 * @throws LTException
	 * @author yubei
	 */
	public int getUserOpenAccount(String userId) throws LTException;

	/**
	 * 吧手机号码  身份证 改成 123*******777 模式
	 * @param id
	 * @return
	 * @throws Exception
	 */
	UserBase getUserBaseFuzzy(String id) throws Exception;
	
	/**
	 * 查询用户身份信息
	 * @param userId
	 * @return
	 * @throws LTException
	 */
	public Map<String, Object> getUserInfo(String userId) throws LTException;
 }
