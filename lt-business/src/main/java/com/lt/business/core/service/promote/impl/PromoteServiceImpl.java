package com.lt.business.core.service.promote.impl;


import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.lt.api.user.IUserApiBussinessService;
import com.lt.business.core.dao.promote.ICommisionMainDao;
import com.lt.business.core.dao.promote.IPromoterDao;
import com.lt.business.core.dao.promote.IPromoterLevelDao;
import com.lt.business.core.dao.promote.IPromoterUserMapperDao;
import com.lt.business.core.dao.statistic.IStatisticBrancherDayLogDao;
import com.lt.business.core.dao.statistic.IStatisticBrancherSummaryLogDao;
import com.lt.business.core.dao.statistic.IStatisticPromoterDayLogDao;
import com.lt.business.core.dao.statistic.IStatisticPromoterSummaryLogDao;
import com.lt.business.core.mongo.promote.IPromoteMongoDao;
import com.lt.business.core.service.promote.IPromoteService;
import com.lt.business.core.vo.PromoteLibrary;
import com.lt.business.core.vo.PromoteVo;
import com.lt.model.promote.CommisionMain;
import com.lt.model.promote.Promoter;
import com.lt.model.promote.PromoterLevel;
import com.lt.model.promote.PromoterUserMapper;
import com.lt.model.statistic.StatisticBrancherDayLog;
import com.lt.model.statistic.StatisticBrancherSummaryLog;
import com.lt.model.statistic.StatisticPromoterDayLog;
import com.lt.model.statistic.StatisticPromoterSummaryLog;
import com.lt.model.user.ServiceContant;
import com.lt.model.user.UserBaseInfo;
import com.lt.model.user.UserServiceMapper;
import com.lt.util.error.LTException;
import com.lt.util.error.LTResponseCode;
import com.lt.util.utils.DateTools;
import com.lt.util.utils.InvitecodeTools;
import com.lt.util.utils.StringTools;
import com.lt.util.utils.model.Response;
/**
 * 推广实现类
 * @author jingwb
 *
 */
@Service
public class PromoteServiceImpl implements IPromoteService{

	Logger logger = LoggerFactory.getLogger(getClass());
	
	@Autowired
	private IPromoterLevelDao promoterLevelDao;
	@Autowired
	private IPromoterDao promoterDao;
	@Autowired
	private ICommisionMainDao commisionMainDao;
	@Autowired
	private IPromoterUserMapperDao promoterUserMapperDao;
	@Autowired
	private IPromoteMongoDao promoteMongoDao;
	@Autowired
	private IStatisticPromoterSummaryLogDao statisticPromoterSummaryLogDao;
	@Autowired
	private IStatisticBrancherSummaryLogDao statisticBrancherSummaryLogDao;
	@Autowired
	private IUserApiBussinessService userApiBussinessService;
	@Autowired
	private IStatisticBrancherDayLogDao statisticBrancherDayLogDao;
	@Autowired
	private IStatisticPromoterDayLogDao statisticPromoterDayLogDao;
	
	@Override
	@Transactional
	public void activatePromoter(String userId) throws Exception {
		logger.info("----------用户userId={},激活推广员----",userId);
		//判断该用户是否已是推广员
		Promoter p = new Promoter();
		p.setUserId(userId);
		p.setFlag(1);
		Integer count = promoterDao.selectPromoterCount(p);
		if(count > 0){
			throw new LTException(LTResponseCode.PROMJ0012);
		}
		//获取一级推广等级的 id
		PromoterLevel levelVo = new PromoterLevel();
		levelVo.setLevel(1);
		List<PromoterLevel> levels = promoterLevelDao.selectPromoterLevels(levelVo);
		if(levels == null || levels.size() == 0){
			throw new LTException(LTResponseCode.PROMJ0001);
		}
		
		//判断该用户的推广员信息是否删除过，如果是，则修改状态即可
		p.setFlag(0);
		Integer count1 = promoterDao.selectPromoterCount(p);
		if(count1 != 0){//是
			//修改
			Promoter vo = new Promoter();
			vo.setUserId(userId);
			vo.setModifyUserId(0);
			vo.setFlag(1);
			promoterDao.updatePromoter(vo);
		}else{
			//添加推广员信息
			Promoter promoter = new Promoter();
			promoter.setLevelId(levels.get(0).getId());
			promoter.setUserId(userId);
			promoterDao.insertPromoter(promoter);
		}		
		
		//判断佣金账户是否已存在
		CommisionMain main = commisionMainDao.selectCommisionMain(userId);
		if(main == null){//不存在
			//初始化佣金数据
			commisionMainDao.initCommisionMain(userId);	
		}
		//判断汇总数据是否已存在
		Integer scount = statisticPromoterSummaryLogDao.selectSummaryCount(userId);
		if(scount == 0){
			//初始化推广员汇总数据（包括一层注册数、二层注册数）
			//查询用户一层、二层下线数
			Integer fc = promoterUserMapperDao.selectFirstPromoterCount(userId);
			Integer sc = promoterUserMapperDao.selectSecondPromoterCount(userId);
			StatisticPromoterSummaryLog log = new StatisticPromoterSummaryLog();
			log.setUserId(userId);
			log.setFirstRegisterCount(fc);
			log.setSecondRegisterCount(sc);
			statisticPromoterSummaryLogDao.initPromoterSummaryLog(log);
		}
		
		/*//初始化统计下线汇总数据
		//查询未生成下线汇总的下线用户
		List<PromoterUserMapper> list = promoterUserMapperDao.selectNoSummaryBranchers(userId);
		List<StatisticBrancherSummaryLog> logs = new ArrayList<StatisticBrancherSummaryLog>();
		for(PromoterUserMapper pum:list){
			StatisticBrancherSummaryLog log = new StatisticBrancherSummaryLog();
			log.setUserId(pum.getUserId());
			log.setPromoterUserId(userId);
			logs.add(log);
		}
		if(logs.size() > 0){
			statisticBrancherSummaryLogDao.initBrancherSummaryLogs(logs);
		}*/
		
		
		//激活用户推广员服务
		UserServiceMapper userServiceMapper = new UserServiceMapper();
		userServiceMapper.setUserId(userId);
		userServiceMapper.setServiceCode(ServiceContant.PROMOTER_SERVICE_CODE);
		userApiBussinessService.activeUserService(userServiceMapper);
	}

	@Override
	public void visitCount(Integer count, String userId) {
		promoteMongoDao.visitCount(count, userId);
	}

	@Override
	public void addPromoteLibrary(String userId, String ip, String tele) {
		promoteMongoDao.addPromoteLibrary(userId, ip, tele);
	}

	@Override
	public int getVisitCount(String userId) {
		return promoteMongoDao.getVisitCount(userId);
	}

	@Override
	@Transactional
	public void addPromoteUserMapper(String userId, String ip, String tele)
			throws LTException {
		
		logger.info("-------将userId与推广员建立关系--------");
		if(StringTools.isEmpty(ip) && StringTools.isEmpty(tele)){
			logger.info("------PROMJ0002--------参数缺失,ip={},tele={}-----",ip,tele);
			return;
		}
		
		PromoteLibrary promoteLibrary = null;
		
		//优先通过手机号获取推广员信息，建立用户关系
		if(!StringTools.isEmpty(tele)){
			promoteLibrary = promoteMongoDao.getPromoteLibraryByTele(tele);
		}
		
		
		if(promoteLibrary == null){			
			promoteLibrary = promoteMongoDao.getPromoteLibraryByIP(ip);			
		}
		
		if(promoteLibrary == null){
			logger.info("------PROMJ0003------未获取到推广员信息-----");
			return;
		}
		
		PromoterUserMapper mapper = new PromoterUserMapper();
		mapper.setPromoterId(promoteLibrary.getUserId());
		mapper.setUserId(userId);
		promoterUserMapperDao.insertPromoterUserMapper(mapper);
		
		//初始化统计下线汇总数据
		StatisticBrancherSummaryLog log = new StatisticBrancherSummaryLog();
		log.setUserId(userId);
		log.setPromoterUserId(promoteLibrary.getUserId());
		statisticBrancherSummaryLogDao.initBrancherSummaryLog(log);
	}

	@Override
	public Response isPromoter(String userId) throws Exception {
		Response response = null;
		
		UserBaseInfo user = userApiBussinessService.queryUserBuyId(userId);
		Map<String,Object> map = new HashMap<String,Object>();
		String code = InvitecodeTools.getInvitecodeByUserId(Integer.valueOf(user.getId()));		
		map.put("code", code);
		
		Promoter p = new Promoter();
		p.setUserId(userId);
		p.setFlag(1);
		Integer count = promoterDao.selectPromoterCount(p);
		if(count == 1){
			map.put("isPromoter", 1);
			response = LTResponseCode.getCode(LTResponseCode.SUCCESS,map);
		}else{
			map.put("isPromoter", 0);
			response = LTResponseCode.getCode(LTResponseCode.SUCCESS,map);
		}
		return response;
	}

	@Override
	public Response getPromoterInfo(String userId) throws Exception {
		logger.info("=========查询推广信息，userId={}=======",userId);
		//查询推广员汇总数据
		PromoteVo promoter = statisticPromoterSummaryLogDao.selectPromoterSummaryInfo(userId);
		//查询今日推广信息
		Map<String, StatisticPromoterDayLog> map = this.getPromoteInfoForToday(userId);
		if(map != null){
			StatisticPromoterDayLog statisticPromoterDayLog = map.get(userId);
			if(statisticPromoterDayLog != null){
				promoter.setFirstRegisterCount(promoter.getFirstRegisterCount()+(statisticPromoterDayLog.getFirstRegisterCount()==null?0:statisticPromoterDayLog.getFirstRegisterCount()));
				promoter.setFirstTraderCount(promoter.getFirstTraderCount()+(statisticPromoterDayLog.getFirstTraderCount()==null?0:statisticPromoterDayLog.getFirstTraderCount()));
				promoter.setFirstHandCount(promoter.getFirstHandCount()+(statisticPromoterDayLog.getFirstHandCount()==null?0:statisticPromoterDayLog.getFirstHandCount()));
			}
		}		
		return LTResponseCode.getCode(LTResponseCode.SUCCESS,promoter);
	}

	@Override
	public Response getBrancherInfos(String userId) throws Exception {
		//查询下线汇总数据
		List<PromoteVo> list = statisticBrancherSummaryLogDao.selectBrancherInfos(userId);
		//查询今日下线数据
		Map<String, StatisticBrancherDayLog> map = this.getBrancherInfoForToday(userId);
		if(map != null){
			for(PromoteVo vo : list){
				StatisticBrancherDayLog log = map.get(vo.getUserId());
				vo.setHandCount(vo.getHandCount()+(log.getHandCount()==null?0:log.getHandCount()));
				vo.setFirstHandCount((vo.getFirstHandCount()==null?0:vo.getFirstHandCount())+(log.getFirstHandCount()==null?0:log.getFirstHandCount()));
			}
		}
		
		return LTResponseCode.getCode(LTResponseCode.SUCCESS,list);
	}

	@Override
	public Response getPromoterUrl(String userId) {
		UserBaseInfo user = userApiBussinessService.queryUserBuyId(userId);
		String code = InvitecodeTools.getInvitecodeByUserId(Integer.valueOf(user.getId()));
		return LTResponseCode.getCode(LTResponseCode.SUCCESS,code);
	}

	@Override
	public Map<String, StatisticPromoterDayLog> getPromoteInfoForToday(String userId) {
		Map<String,StatisticPromoterDayLog> dataMap = new HashMap<String,StatisticPromoterDayLog>();
		List<Promoter> promoters = null;
		//获取推广员信息
		if(StringTools.isEmpty(userId)){//未传userId，表示查所有的推广员信息
			promoters = promoterDao.selectPromoters();
		}else{
			Promoter promoter = promoterDao.selectPromoterByUserId(userId);
			if(promoter == null){				
				return null;
			}
			promoters = new ArrayList<Promoter>();
			promoters.add(promoter);
		}
		if(promoters == null || promoters.size() == 0){				
			return null;
		}
		
		for(Promoter promoter : promoters){
			StatisticPromoterDayLog statisticPromoterDayLog = new StatisticPromoterDayLog();
			statisticPromoterDayLog.setCommisionBalance(promoter.getCommisionBalance());
			statisticPromoterDayLog.setUserId(promoter.getUserId());
			dataMap.put(promoter.getUserId(), statisticPromoterDayLog);
		}		
		
		Map<String,Object> param = new HashMap<String,Object>();
		param.put("beginTime", DateTools.parseToDefaultDateString(new Date())+" 00:00:00");
		param.put("endTime", DateTools.parseToDefaultDateString(new Date())+" 23:59:59");
		param.put("userId", userId);
		
		//统计推广员的一层下线交易数据，二层下线交易数据
		logger.info("============统计推广员的一层下线交易数据，二层下线交易数据=========");
		List<Map<String, Object>> promoterFirstTradeInfo = statisticPromoterDayLogDao.selectPromoterFirstTradeInfo(param);
		for(Map<String, Object> firstTradeInfoMap : promoterFirstTradeInfo){
			StatisticPromoterDayLog log = dataMap.get(firstTradeInfoMap.get("promoter_id"));
			
			log.setFirstHandCount(Integer.valueOf((firstTradeInfoMap.get("first_hand_count")==null?0:firstTradeInfoMap.get("first_hand_count")).toString().replace(".0", "")));
			log.setFirstTradeAmount((Double)firstTradeInfoMap.get("first_trade_amount"));
		}
		List<Map<String, Object>> promoterSecondTradeInfo = statisticPromoterDayLogDao.selectPromoterSecondTradeInfo(param);
		for(Map<String, Object> secondTradeInfoMap : promoterSecondTradeInfo){
			StatisticPromoterDayLog log = dataMap.get(secondTradeInfoMap.get("promoter_id"));
			log.setSecondHandCount(Integer.valueOf((secondTradeInfoMap.get("second_hand_count")==null?0:secondTradeInfoMap.get("second_hand_count")).toString().replace(".0", "")));
			log.setSecondTradeAmount((Double)secondTradeInfoMap.get("second_trade_amount"));
		}
		//统计推广员一层下线，二层下线充值金额
		logger.info("============统计推广员一层下线，二层下线充值金额=========");
		List<Map<String, Object>> promoterFirstRechargeAmount = statisticPromoterDayLogDao.selectPromoterFirstRechargeAmount(param);			
		for(Map<String, Object> firstRechargeAmountMap : promoterFirstRechargeAmount){
			StatisticPromoterDayLog log = dataMap.get(firstRechargeAmountMap.get("promoter_id"));
			log.setFirstRechargeAmount((Double)firstRechargeAmountMap.get("first_recharge_amount"));
		}
		List<Map<String, Object>> promoterSecondRechargeAmount = statisticPromoterDayLogDao.selectPromoterSecondRechargeAmount(param);
		for(Map<String, Object> secondRechargeAmountMap : promoterSecondRechargeAmount){
			StatisticPromoterDayLog log = dataMap.get(secondRechargeAmountMap.get("promoter_id"));
			log.setSecondRechargeAmount((Double)secondRechargeAmountMap.get("second_recharge_amount"));
		}
		
		//统计一层下线，二层下线交易用户数
		logger.info("============统计一层下线，二层下线交易用户数=========");
		List<Map<String, Object>> promoterFirstTraderCount = statisticPromoterDayLogDao.selectPromoterFirstTraderCount(param);
		for(Map<String, Object> promoterFirstTraderCountMap : promoterFirstTraderCount){
			StatisticPromoterDayLog log = dataMap.get(promoterFirstTraderCountMap.get("promoter_id"));
			log.setFirstTraderCount(Integer.valueOf((promoterFirstTraderCountMap.get("first_trader_count")==null?0:promoterFirstTraderCountMap.get("first_trader_count")).toString().replace(".0", "")));
		}
		List<Map<String, Object>> promoterSecondTraderCount = statisticPromoterDayLogDao.selectPromoterSecondTraderCount(param);
		for(Map<String, Object> promoterSecondTraderCountMap : promoterSecondTraderCount){
			StatisticPromoterDayLog log = dataMap.get(promoterSecondTraderCountMap.get("promoter_id"));
			log.setSecondTraderCount(Integer.valueOf((promoterSecondTraderCountMap.get("second_trader_count")==null?0:promoterSecondTraderCountMap.get("second_trader_count")).toString().replace(".0", "")));
		}
		//统计一层，二层下线充值用户数
		logger.info("============统计一层，二层下线充值用户数=========");
		List<Map<String, Object>> promoterFirstRechargerCount = statisticPromoterDayLogDao.selectPromoterFirstRechargerCount(param);
		for(Map<String, Object> promoterFirstRechargerCountMap : promoterFirstRechargerCount){
			StatisticPromoterDayLog log = dataMap.get(promoterFirstRechargerCountMap.get("promoter_id"));
			log.setFirstRechargerCount(Integer.valueOf((promoterFirstRechargerCountMap.get("first_recharger_count")==null?0:promoterFirstRechargerCountMap.get("first_recharger_count")).toString().replace(".0", "")));
		}
		List<Map<String, Object>> promoterSecondRechargerCount = statisticPromoterDayLogDao.selectPromoterSecondRechargerCount(param);
		for(Map<String, Object> promoterSecondRechargerCountMap : promoterSecondRechargerCount){
			StatisticPromoterDayLog log = dataMap.get(promoterSecondRechargerCountMap.get("promoter_id"));
			log.setSecondRegisterCount(Integer.valueOf((promoterSecondRechargerCountMap.get("second_recharger_count")==null?0:promoterSecondRechargerCountMap.get("second_recharger_count")).toString().replace(".0", "")));
		}
		
		//统计一层，二层下线注册用户数
		logger.info("============统计一层，二层下线注册用户数=========");
		List<Map<String, Object>> promoterFirstRegisterCount = statisticPromoterDayLogDao.selectPromoterFirstRegisterCount(param);
		for(Map<String, Object> promoterFirstRegisterCountMap : promoterFirstRegisterCount){
			StatisticPromoterDayLog log = dataMap.get(promoterFirstRegisterCountMap.get("promoter_id"));
			log.setFirstRegisterCount(Integer.valueOf((promoterFirstRegisterCountMap.get("first_register_count")==null?0:promoterFirstRegisterCountMap.get("first_register_count")).toString().replace(".0", "")));
		}
		List<Map<String, Object>> promoterSecondRegisterCount = statisticPromoterDayLogDao.selectPromoterSecondRegisterCount(param);
		for(Map<String, Object> promoterSecondRegisterCountMap : promoterSecondRegisterCount){
			StatisticPromoterDayLog log = dataMap.get(promoterSecondRegisterCountMap.get("promoter_id"));
			log.setSecondRegisterCount(Integer.valueOf((promoterSecondRegisterCountMap.get("second_register_count")==null?0:promoterSecondRegisterCountMap.get("second_register_count")).toString().replace(".0", "")));
		}
		logger.info("======dataMap={}========",dataMap);
		return dataMap;
	}

	@Override
	public Map<String,StatisticBrancherDayLog> getBrancherInfoForToday(String userId) {
		Map<String,StatisticBrancherDayLog> dataMap = new HashMap<String,StatisticBrancherDayLog>();
		//查询下线人员
		List<PromoterUserMapper> brancherList = promoterUserMapperDao.selectPromoterUserMapperByUserId(userId);
		logger.info("------获取的下线信息集合brancherList.size={}-------",brancherList.size());
		if(brancherList == null || brancherList.size() == 0){
			return null;
		}
		for(PromoterUserMapper promoterUserMapper:brancherList){
			StatisticBrancherDayLog statisticBrancherDayLog = new StatisticBrancherDayLog();
			statisticBrancherDayLog.setUserId(promoterUserMapper.getUserId());
			statisticBrancherDayLog.setPromoterUserId(promoterUserMapper.getPromoterId());
			dataMap.put(promoterUserMapper.getUserId(),statisticBrancherDayLog);
		}
		//查询用户交易信息
		Map<String,Object> param = new HashMap<String,Object>();
		param.put("beginTime", DateTools.parseToDefaultDateString(new Date())+" 00:00:00");
		param.put("endTime", DateTools.parseToDefaultDateString(new Date())+" 23:59:59");
		param.put("userId", userId);
		//查询一层下线交易信息
		List<Map<String, Object>> userTradeInfos = statisticBrancherDayLogDao.selectBrancherTradeInfo(param);
		for(Map<String, Object> userTradeInfosMap:userTradeInfos){
			StatisticBrancherDayLog statisticBrancherDayLog = dataMap.get(userTradeInfosMap.get("user_id"));
			statisticBrancherDayLog.setTradeAmount((Double)userTradeInfosMap.get("trade_amount"));
			statisticBrancherDayLog.setHandCount(Integer.valueOf((userTradeInfosMap.get("hand_count")==null?0:userTradeInfosMap.get("hand_count")).toString().replace(".0", "")));				
		}
		
		List<Map<String, Object>> secondUserTradeInfos = statisticBrancherDayLogDao.selectSecondBrancherTradeInfo(param);
		for(Map<String, Object> userTradeInfosMap:secondUserTradeInfos){
			StatisticBrancherDayLog statisticBrancherDayLog = dataMap.get(userTradeInfosMap.get("user_id"));
			statisticBrancherDayLog.setFirstTradeAmount((Double)userTradeInfosMap.get("trade_amount"));
			statisticBrancherDayLog.setFirstHandCount(Integer.valueOf((userTradeInfosMap.get("hand_count")==null?0:userTradeInfosMap.get("hand_count")).toString().replace(".0", "")));				
		}
		
		//查询用户充值信息
		List<Map<String, Object>> userRechargeInfos = statisticBrancherDayLogDao.selectBrancherRechargeInfo(param);
		for(Map<String, Object> userRechargeInfosMap:userRechargeInfos){
			StatisticBrancherDayLog statisticBrancherDayLog = dataMap.get(userRechargeInfosMap.get("user_id"));
			statisticBrancherDayLog.setRechargeAmount((Double)userRechargeInfosMap.get("recharge_amount"));
		}
		logger.info("------获取的下线信息dataMap={}-------",dataMap);
		return dataMap;
	}
}
