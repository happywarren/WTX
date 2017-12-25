package com.lt.controller.promote.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.lt.controller.promote.bean.CommisionMainOld;
import com.lt.controller.promote.bean.CommissionsMain;
import com.lt.controller.promote.bean.PromoterOld;
import com.lt.controller.promote.bean.PromoterUserMapperOld;
import com.lt.controller.promote.bean.StatisticBrancherSummaryLogOld;
import com.lt.controller.promote.bean.StatisticPromoterSummaryLogOld;
import com.lt.controller.promote.bean.UserPromoteDetailVo;
import com.lt.controller.promote.bean.UserServiceMapperOld;
import com.lt.controller.promote.dao.IPromoteDaoOld;
import com.lt.controller.promote.service.IPromoteServiceOld;
import com.lt.controller.tools.CommonTools;

@Service
public class PromoteServiceImplOld implements IPromoteServiceOld{

	@Autowired
	private IPromoteDaoOld promoteDao;
	
	
	@Override
	public void promote() throws Exception{
		//查询推广员
		List<PromoterOld> promoterList = promoteDao.selectPromoterList();
		//查询推广关系
		List<PromoterUserMapperOld> promoterUserMapperList = promoteDao.selectPromoterUserMapperList();
		//查询推广资金主表
		List<CommisionMainOld> commisionMainList = promoteDao.selectCommisionMainList();
		//查询推广员统计信息
		List<StatisticPromoterSummaryLogOld> statisticPromoterSummaryLogList = new ArrayList<StatisticPromoterSummaryLogOld>();
		//处理推广服务关系
		List<UserServiceMapperOld> userServiceMapperOldList = new ArrayList<UserServiceMapperOld>();
		for(PromoterOld promoter : promoterList){
			StatisticPromoterSummaryLogOld log = new StatisticPromoterSummaryLogOld();
			String promoterId = promoter.getUser_id();
			log.setUser_id(promoterId);
			List<String> userIds = promoteDao.getRegisterIdsByPromoteIdOfDate(promoterId);
			Integer registerCount = 0;//一层注册数
			Integer consumerCount = 0;//一层交易用户数
			Integer consumerHand = 0;//一层交易手数
			Integer subHand = 0;//二层交易手数
			if (userIds == null || userIds.size() == 0) {
				log.setFirst_register_count(registerCount);
				log.setFirst_trader_count(consumerCount);
				log.setFirst_hand_count(consumerHand);
				log.setSecond_hand_count(subHand);
			}else{
				log.setFirst_register_count(userIds.size());
				consumerCount = promoteDao.getConsumerCountByUserIds(userIds);
				log.setFirst_trader_count(consumerCount==null?0:consumerCount);
				consumerHand = promoteDao.getConsumerHandSumByUserIds(userIds);
				log.setFirst_hand_count(consumerHand==null?0:consumerHand);
				
				List<String> subIds = promoteDao.getSubRegisterIdsByPromoteIdOfDate(promoterId);
				if (subIds != null && !subIds.isEmpty()) {
					subHand = promoteDao.getConsumerHandSumByUserIds(subIds);
					log.setSecond_hand_count(subHand==null?0:subHand);
					log.setSecond_register_count(subIds.size());
					Integer ss = promoteDao.getConsumerCountByUserIds(subIds);
					log.setSecond_trader_count(ss==null?0:ss);
				}else{
					log.setSecond_hand_count(0);
					log.setSecond_register_count(0);
					log.setSecond_trader_count(0);
				}
			}
			
			CommissionsMain cm = promoteDao.getCommissionsMainByPromoteId(promoterId);
			Double totleComm = 0.0;//已结算佣金
			Double levelOneComm = 0.0;//一层佣金
			Double levelTwoComm = 0.0;//二层佣金
			if (cm != null) {
				totleComm = cm.getTotal_commissions()==null?0.0:cm.getTotal_commissions();
				levelOneComm = cm.getTotal_level_one_commissions()==null?0.0:cm.getTotal_level_one_commissions();
				levelTwoComm = cm.getTotal_level_two_commissions()==null?0.0:cm.getTotal_level_two_commissions();
			}
			log.setFirst_commision(levelOneComm);
			log.setSecond_commision(levelTwoComm);
			log.setBalance_commision(totleComm);
			
			//查询下线累计充值，累计手续费
			UserPromoteDetailVo userPromoteDetailVo = promoteDao.getUserPromoteDetail(promoterId);
			Double storeAmt =0.0;//累计充值
			Double counterFee = 0.0;//累计交易金额
			if(userPromoteDetailVo != null){
				storeAmt = userPromoteDetailVo.getStore_amt();
				counterFee = userPromoteDetailVo.getCounter_fee();
			}
			log.setFirst_recharge_amount(storeAmt==null?0.0:storeAmt);
			log.setFirst_trade_amount(counterFee==null?0.0:counterFee);
			
			//入金用户数
			Integer storeAmtCount = 0;
			if(userIds != null && userIds.size() > 0){
				storeAmtCount = promoteDao.getStoreAmtCountByUserIds(userIds);
			}
			log.setFirst_recharger_count(storeAmtCount==null?0:storeAmtCount);
			log.setSecond_recharge_amount(0.0);
			log.setSecond_recharger_count(0);
			log.setSecond_trade_amount(0.0);
			log.setCreate_time(promoter.getCreate_time());
			log.setModify_time(promoter.getCreate_time());
			statisticPromoterSummaryLogList.add(log);
			
			//服务
			UserServiceMapperOld service = new UserServiceMapperOld();
			service.setUser_id(promoterId);
			service.setService_code("1003");
			service.setStatus("1");
			service.setOpen_time(promoter.getCreate_time());
			service.setCreate_date(promoter.getCreate_time());
			userServiceMapperOldList.add(service);
		}
		
		//查询下线统计信息
		List<StatisticBrancherSummaryLogOld> statisticBrancherSummaryLogList = new ArrayList<StatisticBrancherSummaryLogOld>();
		for(PromoterUserMapperOld promoterUserMapper : promoterUserMapperList){
			StatisticBrancherSummaryLogOld log = new StatisticBrancherSummaryLogOld();
			String userId = promoterUserMapper.getUser_id();
			log.setUser_id(userId);
			log.setPromoter_user_id(promoterUserMapper.getPromoter_id());
			UserPromoteDetailVo vo = promoteDao.getUserPromoteDetails(userId);
			if(vo == null){
				log.setRecharge_amount(0.0);
				log.setTrade_amount(0.0);
			}else{
				log.setRecharge_amount(vo.getStore_amt()==null?0.0:vo.getStore_amt());
				log.setTrade_amount(vo.getCounter_fee()==null?0.0:vo.getCounter_fee());
			}
			
			List<String> userIds = new ArrayList<>();
			userIds.add(userId);
			Integer hand = promoteDao.getConsumerHandSumByUserIds(userIds);
			log.setHand_count(hand==null?0:hand);
			log.setCreate_time(promoterUserMapper.getCreate_time());
			log.setModify_time(promoterUserMapper.getModify_time());
			log.setFlag(1);
			statisticBrancherSummaryLogList.add(log);
			
		}
		
		
		
		
		
		//生成推广员信息脚本
		CommonTools.createFile("7_promoter", CommonTools.createInsertSql("promoter", promoterList)) ;
		//生成推广关系脚本
		CommonTools.createFile("8_promoter_user_mapper", CommonTools.createInsertSql("promoter_user_mapper", promoterUserMapperList)) ;
		//生成佣金主脚本
		CommonTools.createFile("9_commision_main", CommonTools.createInsertSql("commision_main", commisionMainList)) ;
		//生成推广统计脚本
		CommonTools.createFile("10_statistic_promoter_summary_log", CommonTools.createInsertSql("statistic_promoter_summary_log", statisticPromoterSummaryLogList)) ;
		//生成下线统计脚本
		CommonTools.createFile("11_statistic_brancher_summary_log", CommonTools.createInsertSql("statistic_brancher_summary_log", statisticBrancherSummaryLogList)) ;
		//生成推广服务关系脚本
		CommonTools.createFile("12_user_service_mapper", CommonTools.createInsertSql("user_service_mapper", userServiceMapperOldList)) ;
		//将生成的推广等级转换成id脚本
		String sql = "UPDATE promoter a INNER JOIN promoter_level b ON(a.level_id=b.`level`) set a.level_id=b.id";
		CommonTools.createFile("13_update_promoter", sql);
	}

}
