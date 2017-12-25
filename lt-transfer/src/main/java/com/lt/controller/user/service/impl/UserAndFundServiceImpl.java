package com.lt.controller.user.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.lt.controller.user.bean.UserOldBaseInfo;
import com.lt.controller.user.bean.UserFundMsgBean;
import com.lt.controller.user.bean.UserMainCash;
import com.lt.controller.user.dao.IUserDao;
import com.lt.controller.user.service.IUserAndFundService;
import com.lt.util.utils.DoubleTools;

/**   
* 项目名称：lt-transfer   
* 类名称：UserAndFundServiceImpl   
* 类描述：老系统用户资金操作服务   
* 创建人：yuanxin   
* 创建时间：2017年5月12日 上午9:58:04      
*/
@Service
public class UserAndFundServiceImpl implements IUserAndFundService {
	
	@Autowired
	private IUserDao userDao;
	
	@Override
	public List<UserFundMsgBean> getUserFundInfo(String content,Double rate){
		List<UserFundMsgBean> userFundList = new ArrayList<UserFundMsgBean>();
		userFundList = userDao.queryUserUsedAmt();
		
		if(userFundList == null ){
			userFundList = new ArrayList<UserFundMsgBean>();
		}
		
		if(CollectionUtils.isNotEmpty(userFundList)){
			for(UserFundMsgBean userFundMsgBean : userFundList){
				Double rmtAmt = userFundMsgBean.getRmbAmt();
				Double dollar = DoubleTools.rund(rmtAmt * rate, 2);
				userFundMsgBean.setContent(content.replace("(cny)", rmtAmt.toString()).replace("(usd)", dollar.toString()));
			}
		}
		
		return userFundList ;
	}

	@Override
	public List<UserOldBaseInfo> qryUserBaseInfo() {
		return userDao.queryUserBaseInfo();
	}

	
	@Override
	public UserMainCash qryUserMainCash(String userId) {
		UserMainCash userMainCash = userDao.queryUserMain(userId);
//		if(userMainCash != null ){
//			Double deferFee = userDao.querydeferFee(userId);
//			userMainCash.setTotal_interest_amount(deferFee == null ? 0.0 : deferFee);
//		}
		
		return userMainCash;
	}

	@Override
	public Integer qryUserOrderCount(String userId) {
		return userDao.queryUserOrder(userId);
	}
}
