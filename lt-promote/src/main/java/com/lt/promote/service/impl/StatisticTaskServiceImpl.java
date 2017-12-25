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

import com.lt.api.sys.IThreadLockService;
import com.lt.enums.sys.SysThreadLockEnum;
import com.lt.model.promote.Promoter;
import com.lt.model.promote.PromoterUserMapper;
import com.lt.model.statistic.StatisticBrancherDayLog;
import com.lt.model.statistic.StatisticPromoterDayLog;
import com.lt.promote.dao.promote.IPromoterDao;
import com.lt.promote.dao.promote.IPromoterUserMapperDao;
import com.lt.promote.dao.statistic.IStatisticBrancherDayLogDao;
import com.lt.promote.dao.statistic.IStatisticBrancherSummaryLogDao;
import com.lt.promote.dao.statistic.IStatisticPromoterDayLogDao;
import com.lt.promote.dao.statistic.IStatisticPromoterSummaryLogDao;
import com.lt.promote.service.ICommisionService;
import com.lt.promote.service.IStatisticTaskService;
import com.lt.util.utils.DateTools;
import com.lt.util.utils.DoubleUtils;

/**
 * 推广相关定时任务实现类
 * @author jingwb
 *
 */
@Service
public class StatisticTaskServiceImpl implements IStatisticTaskService{

	Logger logger = LoggerFactory.getLogger(getClass());
	@Autowired
	private IStatisticBrancherDayLogDao statisticBrancherDayLogDao;
	@Autowired
	private IStatisticPromoterDayLogDao statisticPromoterDayLogDao;
	@Autowired
	private IPromoterDao promoterDao;
	@Autowired
	private IStatisticPromoterSummaryLogDao statisticPromoterSummaryLogDao;
	@Autowired
	private IStatisticBrancherSummaryLogDao statisticBrancherSummaryLogDao;
	@Autowired
	private ICommisionService commisionService;
	@Autowired
	private IPromoterUserMapperDao promoterUserMapperDao;
	@Autowired
	private IThreadLockService threadLockServiceImpl;


	/**
	 * 手动执行
	 */
	@Override
	public void balanceCommisionTask(String day) throws Exception {
		try{
			//锁校验
			if(!threadLockServiceImpl.lock(SysThreadLockEnum.BALANCE_COMMISION_TASK.getCode())){
				return;
			}
			balanceCommision(day);
		}finally{
			Thread.currentThread().sleep(10000);//等待10秒后再解锁
			//解锁
			threadLockServiceImpl.unlock(SysThreadLockEnum.BALANCE_COMMISION_TASK.getCode());
		}
	}

	/**
	 * 定时任务执行
	 */
	@Override
	public void balanceCommisionTask() throws Exception {
		try{
			//锁校验
			if(!threadLockServiceImpl.lock(SysThreadLockEnum.BALANCE_COMMISION_TASK.getCode())){
				return;
			}
			balanceCommision();
			
		}finally{
			Thread.currentThread().sleep(10000);//等待10秒后再解锁
			//解锁
			threadLockServiceImpl.unlock(SysThreadLockEnum.BALANCE_COMMISION_TASK.getCode());
		}
	}
	
	@Transactional
	public void balanceCommision(String day) throws Exception {
		String beginTime="00:00:00";//统计开始时间
		String endTime="23:59:59";//统计结束时间
		logger.info("---------人工触发执行，佣金结算,结算日期={}-----开始---------",day);
			
		//处理下线数据
		dealBrancherData(beginTime, endTime, day);
		//处理推广员数据
		dealPromoterData(beginTime, endTime, day);
			
		logger.info("--------人工触发执行，佣金结算----结束----");
		
	}
	
	@Transactional
	public void balanceCommision() throws Exception {
		String beginTime="00:00:00";//统计开始时间
		String endTime="23:59:59";//统计结束时间
		//获取前一天日期
		Calendar c = Calendar.getInstance();
		c.add(Calendar.DAY_OF_MONTH, -1);
		//昨天日期
		Date yesterday = c.getTime();
		String yesterdayStr = DateTools.parseToDefaultDateString(yesterday);
		logger.info("--------定时任务执行，佣金结算,结算日期={}----开始----",yesterdayStr);

		//处理下线数据
		dealBrancherData(beginTime, endTime, yesterdayStr);
		//处理推广员数据
		dealPromoterData(beginTime, endTime, yesterdayStr);
			
		logger.info("--------定时任务执行，佣金结算----结束----");
		
	}
	
	
	/**
	 * 处理推广员数据
	 * @param beginTime
	 * @param endTime
	 * @param yesterdayStr
	 * @throws Exception
	 */
	/*private void dealPromoterData(String statisticTime) throws Exception {
		//查询推广日报集合
		Map<String,String> pmap = new HashMap<String,String>();
		pmap.put("statisticTime", statisticTime);
		List<StatisticPromoterDayLog> promoterDayLogList = statisticPromoterDayLogDao.selectPromoterDayLogList(pmap);

		//更新推广汇总数据
		statisticPromoterSummaryLogDao.updatePromoterSummaryLogs(promoterDayLogList);
		logger.info("------------更新推广汇总数据---完成----------");
		//生成佣金结算流水，更新佣金账户
		commisionService.balanceCommision(promoterDayLogList);
		logger.info("------------生成佣金结算流水，更新佣金账户----完成---------");
	}*/
	
	/**
	 * 处理下线数据
	 * @param beginTime
	 * @param endTime
	 * @param yesterdayStr
	 */
	/*private void dealBrancherData(String statisticTime) {
		List<StatisticBrancherDayLog> brancherDayLogList = statisticBrancherDayLogDao.selectBrancherDayLogList(statisticTime);
		//更新下线汇总数据
		statisticBrancherSummaryLogDao.updateBrancherSummaryLogs(brancherDayLogList);
	}*/

	
	/**
	 * 处理下线数据
	 * @param beginTime
	 * @param endTime
	 * @param yesterdayStr
	 */
	private void dealBrancherData(String beginTime, String endTime,
			String yesterdayStr) {
		Map<String,StatisticBrancherDayLog> dataMap = new HashMap<String,StatisticBrancherDayLog>();
		//查询推广员的下线人员
		List<PromoterUserMapper> brancherList = promoterUserMapperDao.selectPromoterBranchers();
		logger.info("------获取的下线信息集合brancherList.size={}-------",brancherList.size());
		if(brancherList == null || brancherList.size() == 0){
			return;
		}
		for(PromoterUserMapper promoterUserMapper:brancherList){
			StatisticBrancherDayLog statisticBrancherDayLog = new StatisticBrancherDayLog();
			statisticBrancherDayLog.setUserId(promoterUserMapper.getUserId());
			statisticBrancherDayLog.setPromoterUserId(promoterUserMapper.getPromoterId());
			dataMap.put(promoterUserMapper.getUserId(),statisticBrancherDayLog);
		}
		//查询用户交易信息
		Map<String,Object> param = new HashMap<String,Object>();
		param.put("beginTime", yesterdayStr+" "+beginTime);
		param.put("endTime", yesterdayStr+" "+endTime);
		List<Map<String, Object>> userTradeInfos = statisticBrancherDayLogDao.selectBrancherTradeInfo(param);
		for(Map<String, Object> userTradeInfosMap:userTradeInfos){
			StatisticBrancherDayLog statisticBrancherDayLog = dataMap.get(userTradeInfosMap.get("user_id"));
			statisticBrancherDayLog.setTradeAmount((Double)userTradeInfosMap.get("trade_amount"));
			statisticBrancherDayLog.setHandCount(Integer.valueOf((userTradeInfosMap.get("hand_count")==null?0:userTradeInfosMap.get("hand_count")).toString().replace(".0", "")));				
		}
		//查询用户充值信息
		List<Map<String, Object>> userRechargeInfos = statisticBrancherDayLogDao.selectBrancherRechargeInfo(param);
		for(Map<String, Object> userRechargeInfosMap:userRechargeInfos){
			StatisticBrancherDayLog statisticBrancherDayLog = dataMap.get(userRechargeInfosMap.get("user_id"));
			statisticBrancherDayLog.setRechargeAmount((Double)userRechargeInfosMap.get("recharge_amount"));
		}
		
		List<StatisticBrancherDayLog> list = new ArrayList<StatisticBrancherDayLog>();
		for(PromoterUserMapper promoterUserMapper : brancherList){				
			StatisticBrancherDayLog log = dataMap.get(promoterUserMapper.getUserId());
			log.setStatisticTime(yesterdayStr);
			list.add(log);
		}
		//更新下线汇总数据
		statisticBrancherSummaryLogDao.updateBrancherSummaryLogs(list);
	}
	
	/**
	 * 处理推广员数据
	 * @param beginTime
	 * @param endTime
	 * @param yesterdayStr
	 * @throws Exception
	 */
	private void dealPromoterData(String beginTime, String endTime,
			String yesterdayStr) throws Exception {
		Map<String,StatisticPromoterDayLog> dataMap = new HashMap<String,StatisticPromoterDayLog>();
		//获取推广员信息
		List<Promoter> promoters = promoterDao.selectPromoters();
		logger.info("------获取的推广员信息集合promoters.size={}-------",promoters.size());
		if(promoters == null || promoters.size() == 0){				
			return;
		}
		
		for(Promoter promoter : promoters){
			StatisticPromoterDayLog statisticPromoterDayLog = new StatisticPromoterDayLog();
			statisticPromoterDayLog.setCommisionBalance(promoter.getCommisionBalance());
			statisticPromoterDayLog.setUserId(promoter.getUserId());
			dataMap.put(promoter.getUserId(), statisticPromoterDayLog);
		}
		
		Map<String,Object> param = new HashMap<String,Object>();
		param.put("beginTime", yesterdayStr+" "+beginTime);
		param.put("endTime", yesterdayStr+" "+endTime);
		//统计推广员的一层佣金，二层佣金
		List<Map<String, Object>> promoterFirstCommission = statisticPromoterDayLogDao.selectPromoterFirstCommission(param);			
		for(Map<String, Object> firstCommissionMap : promoterFirstCommission){
			StatisticPromoterDayLog log = dataMap.get(firstCommissionMap.get("promoter_id"));
			log.setFirstCommision((Double)(firstCommissionMap.get("first_commission") == null 
					? 0.0 : firstCommissionMap.get("first_commission")));//一层佣金
		}
		List<Map<String, Object>> promoterSecondCommission = statisticPromoterDayLogDao.selectPromoterSecondCommission(param);
		for(Map<String, Object> secondCommissionMap : promoterSecondCommission){
			StatisticPromoterDayLog log = dataMap.get(secondCommissionMap.get("promoter_id"));
			log.setSecondCommision((Double)(secondCommissionMap.get("second_commission") == null
					? 0.0 : secondCommissionMap.get("second_commission")));//二层佣金
		}
		//统计推广员的一层下线交易数据，二层下线交易数据
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
		
		List<StatisticPromoterDayLog> list = new ArrayList<StatisticPromoterDayLog>();
		
		for(int i = 0; i < promoters.size(); i ++){
			Promoter promoter = promoters.get(i);//推广员信息
			StatisticPromoterDayLog statisticPromoterDayLog  = dataMap.get(promoter.getUserId());
			//已结算佣金
			statisticPromoterDayLog.setBalanceCommision(DoubleUtils.add(statisticPromoterDayLog.getFirstCommision(),statisticPromoterDayLog.getSecondCommision()));
			//统计日期
			statisticPromoterDayLog.setStatisticTime(yesterdayStr);
			list.add(statisticPromoterDayLog);				
		}
		//更新推广汇总数据
		statisticPromoterSummaryLogDao.updatePromoterSummaryLogs(list);
		logger.info("------------更新推广汇总数据---完成----------");
		//生成佣金结算流水，更新佣金账户
		commisionService.balanceCommision(list);
		logger.info("------------生成佣金结算流水，更新佣金账户----完成---------");
	}
	
	@Override
	@Transactional
	public void initStatisticDayLog() throws Exception {
		try{
			//锁校验
			if(!threadLockServiceImpl.lock(SysThreadLockEnum.INIT_STATISTIC_DAY_LOG.getCode())){
				return;
			}
			//获取明天日期
			Calendar c = Calendar.getInstance();
			c.add(Calendar.DAY_OF_MONTH, 1);
			//明天日期
			Date day = c.getTime();
			String dayStr = DateTools.parseToDefaultDateString(day);
			logger.info("-------------定时任务生成推广日报和下线日报，统计日期为：{}---------------",dayStr);
			initPromoterAndBrancherDayLog(dayStr);	
			logger.info("-------------定时任务生成推广日报和下线日报完成---------------");
			
		}finally{
			Thread.currentThread().sleep(10000);//等待10秒后再解锁
			//解锁
			threadLockServiceImpl.unlock(SysThreadLockEnum.INIT_STATISTIC_DAY_LOG.getCode());
		}
		
	}

	@Override
	@Transactional
	public void initStatisticDayLog(String day) throws Exception {
		try{
			//锁校验
			if(!threadLockServiceImpl.lock(SysThreadLockEnum.INIT_STATISTIC_DAY_LOG.getCode())){
				return;
			}
			logger.info("-------------人工触发生成推广日报和下线日报，统计日期为：{}---------------",day);
			initPromoterAndBrancherDayLog(day);		
			logger.info("-------------人工触发生成推广日报和下线日报完成---------------");
		}finally{
			Thread.currentThread().sleep(10000);//等待10秒后再解锁
			//解锁
			threadLockServiceImpl.unlock(SysThreadLockEnum.INIT_STATISTIC_DAY_LOG.getCode());
		}
		
	}
	
	
	/**
	 * 初始化推广和下线日报数据
	 * @param dayStr
	 */
	private void initPromoterAndBrancherDayLog(String dayStr) {
		List<StatisticPromoterDayLog> promoterDayLogs = new ArrayList<StatisticPromoterDayLog>();
		List<Promoter> promoters = promoterDao.selectPromotersByTime(dayStr);
		for(Promoter p : promoters){
			StatisticPromoterDayLog log = new StatisticPromoterDayLog();
			log.setUserId(p.getUserId());
			log.setStatisticTime(dayStr);
			promoterDayLogs.add(log);
		}
		//初始化推广日报
		statisticPromoterDayLogDao.insertPromoterDayLogs(promoterDayLogs);	
		//初始化下线日报
		
		//查询推广员的下线人员
		List<StatisticBrancherDayLog> brancherDayLogs = new ArrayList<StatisticBrancherDayLog>();
		List<PromoterUserMapper> brancherList = promoterUserMapperDao.selectPromoterBranchersByTime(dayStr);
		for(PromoterUserMapper b : brancherList){
			StatisticBrancherDayLog log = new StatisticBrancherDayLog();
			log.setUserId(b.getUserId());
			log.setPromoterUserId(b.getPromoterId());
			log.setStatisticTime(dayStr);
			brancherDayLogs.add(log);
		}
		statisticBrancherDayLogDao.insertBrancherDayLogs(brancherDayLogs);
	}
}
