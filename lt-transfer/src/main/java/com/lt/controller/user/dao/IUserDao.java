package com.lt.controller.user.dao;


import java.util.List;

import com.lt.controller.user.bean.UserOldBaseInfo;
import com.lt.controller.user.bean.UserFundMsgBean;
import com.lt.controller.user.bean.UserMainCash;

public interface IUserDao {
	
	/**
	 * 查询用户
	 * @param id
	 * @return    
	 * @return:       UserFundMsgBean    
	 * @throws 
	 * @author        yuanxin
	 * @Date          2017年5月12日 上午10:30:28
	 */
	public List<UserFundMsgBean> queryUserUsedAmt();
	
	/**
	 * 
	 * @return    
	 * @return:       List<UserBaseInfo>    
	 * @throws 
	 * @author        yuanxin
	 * @Date          2017年5月16日 下午4:39:51
	 */
	public List<UserOldBaseInfo> queryUserBaseInfo();
	
	/***
	 * 根据用户ID查询用户账户信息
	 * @param userId
	 * @return    
	 * @return:       List<UserMainCash>    
	 * @throws 
	 * @author        yuanxin
	 * @Date          2017年5月16日 下午8:37:03
	 */
	
	public UserMainCash queryUserMain(String userId);
	
	/**
	 * 根据用户ID查询汇总递延费
	 * @param userId
	 * @return    
	 * @return:       Double    
	 * @throws 
	 * @author        yuanxin
	 * @Date          2017年5月16日 下午8:42:59
	 */
	public Double querydeferFee(String userId);
	
	/**
	 * 查询用户是否有订单数据
	 * @param userId
	 * @return    
	 * @return:       Double    
	 * @throws 
	 * @author        yuanxin
	 * @Date          2017年5月19日 下午4:06:47
	 */
	public Integer queryUserOrder(String userId);

}
