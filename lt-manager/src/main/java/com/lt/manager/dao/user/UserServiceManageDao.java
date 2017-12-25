package com.lt.manager.dao.user;

import java.util.List;
import java.util.Map;

import com.github.pagehelper.Page;
import com.lt.manager.bean.fund.FundIoCashRechargeVO;
import com.lt.manager.bean.user.UserService;
import com.lt.model.user.charge.BankChargeMapper;

/**
 * 商品类型管理dao
 * @author licy
 *
 */
public interface UserServiceManageDao {
	
	/**
	 * 添加用户服务
	 * @param param
	 */
	public void insertUserService(UserService param);
	
	/**
	 * 修改用户服务
	 * @param param
	 */
	public void updateUserService(UserService param);
	
	/***
	 * 删除用户服务
	 * @param param
	 */
	public void deleteUserService(Integer id);
	
	/***
	 * 根据Code查询用户服务
	 * @param param
	 */
	public Page<UserService>  selectUserServiceById(String id);
	
	public void updateUserDailyTopAmt(Map<String,Object> paraMap);
	/***
	 * 获取用户单日充值金额
	 * @param userId
	 * @return    
	 * @return:       Double    
	 * @throws 
	 * @author        yuanxin
	 * @Date          2017年6月16日 下午6:14:52
	 */
	public Double getUserDailyTopAmt(String userId);
	/**
	 * 获取用户充值总金额
	 * @param userId
	 * @return    
	 * @return:       FundIoCashRechargeVO    
	 * @throws 
	 * @author        yuanxin
	 * @Date          2017年6月16日 下午6:14:30
	 */
	public FundIoCashRechargeVO getUserRechargeAmt(String userId);
	
	/**
	 * 获取用户充值渠道列表
	 * @param userId
	 * @return    
	 * @return:       List<String>    
	 * @throws 
	 * @author        yuanxin
	 * @Date          2017年6月16日 下午6:15:13
	 */
	public List<String> getUserChannel(String userId);
	
	/**
	 * 查询用户支持渠道的银行限额信息
	 * @param channelList
	 * @return    
	 * @return:       List<BankChannelVo>    
	 * @throws 
	 * @author        yuanxin
	 * @Date          2017年6月16日 下午6:19:05
	 */
	public BankChargeMapper getUserBankChannelAmt(BankChargeMapper bankChargeMapper);
}
