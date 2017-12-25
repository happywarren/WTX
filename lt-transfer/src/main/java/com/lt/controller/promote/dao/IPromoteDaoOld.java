package com.lt.controller.promote.dao;

import java.util.List;


import com.lt.controller.promote.bean.CommisionMainOld;
import com.lt.controller.promote.bean.CommissionsMain;
import com.lt.controller.promote.bean.PromoterOld;
import com.lt.controller.promote.bean.PromoterUserMapperOld;
import com.lt.controller.promote.bean.UserPromoteDetailVo;


public interface IPromoteDaoOld {

	//查询推广员
	public List<PromoterOld> selectPromoterList();
	
	//查询下线关系
	public List<PromoterUserMapperOld> selectPromoterUserMapperList();
	
	//查询推广资金主表
	public List<CommisionMainOld> selectCommisionMainList();
	
	//查询推广员下线
	public List<String> getRegisterIdsByPromoteIdOfDate(String promoteId);
	
	//获取一层下线交易用户数
	public Integer getConsumerCountByUserIds(List<String> list);
	
	//获取一层下线交易手数
	public Integer getConsumerHandSumByUserIds(List<String> list);
	
	//获取二层下线
	public List<String> getSubRegisterIdsByPromoteIdOfDate(String promoteId);
	
	//查询老佣金
	public CommissionsMain getCommissionsMainByPromoteId(String promoteId);
	
	//查询下线累计充值金额，累计手续费
	public UserPromoteDetailVo getUserPromoteDetail(String promoteId);
	
	//获取充值用户数
	public Integer getStoreAmtCountByUserIds(List<String> list);
	
	//查询下线信息
	public UserPromoteDetailVo getUserPromoteDetails(String promoteId);
}
