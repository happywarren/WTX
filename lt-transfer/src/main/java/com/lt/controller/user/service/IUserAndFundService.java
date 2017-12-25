package com.lt.controller.user.service;

import java.util.List;

import com.lt.controller.user.bean.UserOldBaseInfo;
import com.lt.controller.user.bean.UserFundMsgBean;
import com.lt.controller.user.bean.UserMainCash;

/**   
* 项目名称：lt-transfer   
* 类名称：IUserAndFundService   
* 类描述：用户和资金处理类   
* 创建人：yuanxin   
* 创建时间：2017年5月12日 上午9:56:50      
*/
public interface IUserAndFundService {
	
	/**
	 * 获取老用户资金账户信息
	 * @param content
	 * @param rate 利率
	 * @return    
	 * @return:       List<UserFundMsgBean>    
	 * @throws 
	 * @author        yuanxin
	 * @Date          2017年5月12日 上午10:43:04
	 */
	public List<UserFundMsgBean> getUserFundInfo(String content,Double rate);
	
	/**
	 * 查询老系统用户信息
	 * @return    
	 * @return:       List<UserBaseInfo>    
	 * @throws 
	 * @author        yuanxin
	 * @Date          2017年5月16日 下午7:20:10
	 */
	public List<UserOldBaseInfo> qryUserBaseInfo();
	
	/**
	 * 查询用户资金信息
	 * @param userId
	 * @return    
	 * @return:       UserMainCash    
	 * @throws 
	 * @author        yuanxin
	 * @Date          2017年5月16日 下午8:40:16
	 */
	public UserMainCash qryUserMainCash(String userId);
	
	/**
	 * 查询用户订单数量（无状态限制）
	 * @param userId
	 * @return    
	 * @return:       Double    
	 * @throws 
	 * @author        yuanxin
	 * @Date          2017年5月19日 下午4:09:48
	 */
	public Integer qryUserOrderCount(String userId);
}
