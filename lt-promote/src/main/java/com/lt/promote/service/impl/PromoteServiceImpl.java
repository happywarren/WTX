package com.lt.promote.service.impl;


import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSONObject;
import com.lt.api.user.IUserApiBussinessService;
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
import com.lt.promote.dao.promote.ICommisionMainDao;
import com.lt.promote.dao.promote.IPromoterDao;
import com.lt.promote.dao.promote.IPromoterLevelDao;
import com.lt.promote.dao.promote.IPromoterUserMapperDao;
import com.lt.promote.dao.statistic.IStatisticBrancherDayLogDao;
import com.lt.promote.dao.statistic.IStatisticBrancherSummaryLogDao;
import com.lt.promote.dao.statistic.IStatisticPromoterDayLogDao;
import com.lt.promote.dao.statistic.IStatisticPromoterSummaryLogDao;
import com.lt.promote.mongo.promote.IPromoteMongoDao;
import com.lt.promote.service.IPromoteService;
import com.lt.promote.vo.PromoteLibrary;
import com.lt.promote.vo.PromoteVo;
import com.lt.util.error.LTException;
import com.lt.util.error.LTResponseCode;
import com.lt.util.utils.DateTools;
import com.lt.util.utils.InvitecodeTools;
import com.lt.util.utils.StringTools;
import com.lt.util.utils.model.Response;
import com.lt.vo.promote.BrancherVo;
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
		
		//判断推广日报数据是否存在
		Calendar c = Calendar.getInstance();
		Date day = c.getTime();
		String dayStr = DateTools.parseToDefaultDateString(day);
		StatisticPromoterDayLog daylog = new StatisticPromoterDayLog();
		daylog.setUserId(userId);
		daylog.setStatisticTime(dayStr);
		Integer lcount = statisticPromoterDayLogDao.selectPromoterDayLogCount(daylog);
		if(lcount == 0){
			//初始化推广员汇总数据（包括一层注册数、二层注册数）
			//查询用户一层、二层下线数
			Integer fc = promoterUserMapperDao.selectFirstPromoterCount(userId);
			Integer sc = promoterUserMapperDao.selectSecondPromoterCount(userId);
			daylog.setFirstRegisterCount(fc);
			daylog.setSecondRegisterCount(sc);
			//初始化推广日报信息
			statisticPromoterDayLogDao.insertPromoterDayLog(daylog);
		}
		
		//判断汇总数据是否已存在
		Integer scount = statisticPromoterSummaryLogDao.selectSummaryCount(userId);
		if(scount == 0){
			StatisticPromoterSummaryLog log = new StatisticPromoterSummaryLog();
			log.setUserId(userId);
			statisticPromoterSummaryLogDao.initPromoterSummaryLog(log);
		}
		
		//初始化统计下线汇总数据
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
		}
		
		//初始化下线日报数据
		//查询当天未生成下线日报的下线用户
		List<PromoterUserMapper> dayBranchers = promoterUserMapperDao.selectNoDayLogBranchers(userId);
		List<StatisticBrancherDayLog> brancherDayLogs = new ArrayList<StatisticBrancherDayLog>();
		for(PromoterUserMapper pum : dayBranchers){
			StatisticBrancherDayLog dayLog = new StatisticBrancherDayLog();
			dayLog.setUserId(pum.getUserId());
			dayLog.setPromoterUserId(userId);
			dayLog.setStatisticTime(dayStr);
			brancherDayLogs.add(dayLog);
		}
		if(brancherDayLogs.size() > 0){
			statisticBrancherDayLogDao.insertBrancherDayLogs(brancherDayLogs);
		}
		
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
		
		//判断该用户的上线是否为推广员
		Integer count = promoterUserMapperDao.selectPromoterCount(userId);
		if(count > 0){//是
			//初始化下线日报数据
			Calendar c = Calendar.getInstance();
			Date day = c.getTime();
			String dayStr = DateTools.parseToDefaultDateString(day);
			StatisticBrancherDayLog dayLog = new StatisticBrancherDayLog();
			dayLog.setUserId(userId);
			dayLog.setPromoterUserId(promoteLibrary.getUserId());
			dayLog.setStatisticTime(dayStr);
			statisticBrancherDayLogDao.insertBrancherDayLog(dayLog);
			
			//初始化统计下线汇总数据
			StatisticBrancherSummaryLog log = new StatisticBrancherSummaryLog();
			log.setUserId(userId);
			log.setPromoterUserId(promoteLibrary.getUserId());
			statisticBrancherSummaryLogDao.initBrancherSummaryLog(log);
		}
		
		//注册数累加
		BrancherVo vo = new BrancherVo();
		vo.setRegister(true);
		vo.setUserId(userId);
		dealData(vo);
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
		StatisticPromoterDayLog statisticPromoterDayLog = statisticPromoterDayLogDao.selectPromoterDayLogByUserId(userId);
		if(statisticPromoterDayLog != null){
			promoter.setFirstRegisterCount((promoter.getFirstRegisterCount()==null?0:promoter.getFirstRegisterCount())+statisticPromoterDayLog.getFirstRegisterCount());
			promoter.setFirstTraderCount((promoter.getFirstTraderCount()==null?0:promoter.getFirstTraderCount())+statisticPromoterDayLog.getFirstTraderCount());
			promoter.setFirstHandCount((promoter.getFirstHandCount()==null?0:promoter.getFirstHandCount())+statisticPromoterDayLog.getFirstHandCount());
		}	
		return LTResponseCode.getCode(LTResponseCode.SUCCESS,promoter);
	}

	@Override
	public Response getBrancherInfos(String userId) throws Exception {
		//查询下线汇总数据
		List<PromoteVo> list = statisticBrancherSummaryLogDao.selectBrancherInfos(userId);
		if(list == null || list.size() == 0){
			return LTResponseCode.getCode(LTResponseCode.SUCCESS,list);
		}
		//查询今日下线数据
		List<PromoteVo> dayLogs = statisticBrancherDayLogDao.selectBrancherDayLogInfoList(list);
		Map<String,PromoteVo> map = new HashMap<String,PromoteVo>();
		for(PromoteVo vo : dayLogs){
			map.put(vo.getUserId(), vo);
		}
		
		if(map != null){
			for(PromoteVo vo : list){
				PromoteVo log = map.get(vo.getUserId());
				if(log == null){
					log = new PromoteVo();
					log.setHandCount(0);
					log.setFirstHandCount(0);
				}
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
	@Transactional
	public void dealData(BrancherVo param) {
		logger.info("============================处理推广数据,param={}=====================",JSONObject.toJSONString(param));
		boolean isTrade_=true;//是否交易过（不含本次）
		boolean isRecharge_=true;//是否充值过（不含本次）
		
		//处理该用户一层上线数据
		StatisticPromoterDayLog firstPromoterDayLog = statisticPromoterDayLogDao.selectFirstPromoterDayLog(param.getUserId());
		if(firstPromoterDayLog != null){
			StatisticPromoterDayLog statisticPromoterDayLog = new StatisticPromoterDayLog();
			statisticPromoterDayLog.setId(firstPromoterDayLog.getId());
			if(param.isRegister()){//注册
				statisticPromoterDayLog.setFirstRegisterCount(1);
				
			}
			if(param.getTradeHandCount() != null){//交易
				statisticPromoterDayLog.setFirstHandCount(param.getTradeHandCount());
				Integer count = statisticPromoterDayLogDao.selectOrderCountByUserId(param);//交易过的单数（不包含本次交易）				
				if(count == 0){//从未交易过,交易用户数+1
					isTrade_=false;
					statisticPromoterDayLog.setFirstTraderCount(1);
				}
				statisticPromoterDayLog.setFirstTradeAmount(param.getTradeAmount());
			}
			if(param.getRechargeAmount() != null){//充值
				statisticPromoterDayLog.setFirstRechargeAmount(param.getRechargeAmount());
				Integer count = statisticPromoterDayLogDao.selectRechargeCountByUserId(param);//充值次数（不包含本次）
				if(count == 0){
					isRecharge_=false;
					statisticPromoterDayLog.setFirstRechargerCount(1);
				}
			}
			//计算一层下线佣金
			if(param.getOrderId() != null){
				Double firstCommission = statisticPromoterDayLogDao.selectFirstCommissionByOrderId(param.getOrderId());
				statisticPromoterDayLog.setFirstCommision(firstCommission);
				statisticPromoterDayLog.setBalanceCommision(firstCommission);//结算佣金
			}
			
			//更新推广员日报
			logger.info("===========更新推广员日报firstPromoterDayLog={}=====",JSONObject.toJSONString(statisticPromoterDayLog));
			
			statisticPromoterDayLogDao.updatePromoterDayLog(statisticPromoterDayLog);
		}
		
		//处理该用户二层上线数据
		StatisticPromoterDayLog secondPromoterDayLog = statisticPromoterDayLogDao.selectSecondPromoterDayLog(param.getUserId());
		if(secondPromoterDayLog != null){
			StatisticPromoterDayLog statisticPromoterDayLog = new StatisticPromoterDayLog();
			statisticPromoterDayLog.setId(secondPromoterDayLog.getId());
			if(param.isRegister()){//注册
				statisticPromoterDayLog.setSecondRegisterCount(1);
			}
			if(param.getTradeHandCount() != null){//交易
				statisticPromoterDayLog.setSecondHandCount(param.getTradeHandCount());
				if(!isTrade_){
					statisticPromoterDayLog.setSecondTraderCount(1);
				}
				statisticPromoterDayLog.setSecondTradeAmount(param.getTradeAmount());
			}
			if(param.getRechargeAmount() != null){//充值
				statisticPromoterDayLog.setSecondRechargeAmount(param.getRechargeAmount());
				if(!isRecharge_){
					statisticPromoterDayLog.setSecondRechargerCount(1);
				}
			}
			//计算二层下线佣金
			if(param.getOrderId() != null){
				Double secondCommission = statisticPromoterDayLogDao.selectSecondCommissionByOrderId(param.getOrderId());
				statisticPromoterDayLog.setSecondCommision(secondCommission);
				statisticPromoterDayLog.setBalanceCommision(secondCommission);
			}
			
			//更新推广员日报
			logger.info("===========更新推广员日报secondPromoterDayLog={}=====",JSONObject.toJSONString(statisticPromoterDayLog));
			statisticPromoterDayLogDao.updatePromoterDayLog(statisticPromoterDayLog);
		}
		
		//处理下线数据
		StatisticBrancherDayLog brancherDayLog = statisticBrancherDayLogDao.selectBrancherDayLog(param.getUserId());
		if(brancherDayLog != null){
			StatisticBrancherDayLog statisticBrancherDayLog = new StatisticBrancherDayLog();
			statisticBrancherDayLog.setId(brancherDayLog.getId());
			statisticBrancherDayLog.setRechargeAmount(param.getRechargeAmount());
			statisticBrancherDayLog.setHandCount(param.getTradeHandCount());
			statisticBrancherDayLog.setTradeAmount(param.getTradeAmount());
			
			//更新下线日报
			logger.info("===========更新下线日报brancherDayLog={}=====",JSONObject.toJSONString(statisticBrancherDayLog));
			statisticBrancherDayLogDao.updateBrancherDayLog(statisticBrancherDayLog);
		}
		
		logger.info("===============================处理推广数据结束============================");
	}

	@Override
	public Map<String, StatisticPromoterDayLog> getPromoteInfoForToday() {
		Map<String, StatisticPromoterDayLog> map = new HashMap<String, StatisticPromoterDayLog>();
		Calendar c = Calendar.getInstance();
		Date day = c.getTime();
		String dayStr = DateTools.parseToDefaultDateString(day);
		Map<String,String> pmap = new HashMap<String,String>();
		pmap.put("statisticTime", dayStr);
		List<StatisticPromoterDayLog> list = statisticPromoterDayLogDao.selectPromoterDayLogList(pmap);
		for(StatisticPromoterDayLog log : list){
			map.put(log.getUserId(), log);
		}
		return map;
	}

	@Override
	public Map<String, StatisticBrancherDayLog> getBrancherInfoForToday(String userId) {
		Map<String,String> pmap = new HashMap<String,String>();
		Map<String, StatisticBrancherDayLog> map = new HashMap<String, StatisticBrancherDayLog>();
		Calendar c = Calendar.getInstance();
		Date day = c.getTime();
		String dayStr = DateTools.parseToDefaultDateString(day);
		pmap.put("statisticTime", dayStr);
		pmap.put("userId", userId);
		List<StatisticBrancherDayLog> list = statisticBrancherDayLogDao.selectBrancherDayLogListByMap(pmap);
		for(StatisticBrancherDayLog log : list){
			map.put(log.getUserId(), log);
		}
		return map;
	}

	@Override
	public Map<String, StatisticPromoterDayLog> getPromoteInfoForToday(
			String userId) {
		Map<String, StatisticPromoterDayLog> map = new HashMap<String, StatisticPromoterDayLog>();
		Calendar c = Calendar.getInstance();
		Date day = c.getTime();
		String dayStr = DateTools.parseToDefaultDateString(day);
		Map<String,String> pmap = new HashMap<String,String>();
		pmap.put("statisticTime", dayStr);
		pmap.put("userId", userId);
		List<StatisticPromoterDayLog> list = statisticPromoterDayLogDao.selectPromoterDayLogList(pmap);
		for(StatisticPromoterDayLog log : list){
			map.put(log.getUserId(), log);
		}
		return map;
	}
}
