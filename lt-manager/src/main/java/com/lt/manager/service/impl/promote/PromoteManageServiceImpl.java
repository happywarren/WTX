package com.lt.manager.service.impl.promote;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.Page;
import com.lt.api.promote.IPromoteApiService;
import com.lt.manager.bean.promote.PromoteParamVo;
import com.lt.manager.bean.user.UserBase;
import com.lt.manager.bean.user.UserServiceMapper;
import com.lt.manager.dao.promote.PromoteLevelManageDao;
import com.lt.manager.dao.promote.PromoterFeeConfigManageDao;
import com.lt.manager.dao.promote.PromoterManageDao;
import com.lt.manager.dao.user.UserServiceMapperDao;
import com.lt.manager.service.promote.IPromoteManageService;
import com.lt.model.product.Product;
import com.lt.model.promote.Promoter;
import com.lt.model.promote.PromoterFeeConfig;
import com.lt.model.promote.PromoterLevel;
import com.lt.model.promote.PromoterUserMapper;
import com.lt.model.statistic.StatisticBrancherDayLog;
import com.lt.model.statistic.StatisticBrancherSummaryLog;
import com.lt.model.statistic.StatisticPromoterDayLog;
import com.lt.model.user.ServiceContant;
import com.lt.util.LoggerTools;
import com.lt.util.error.LTException;
import com.lt.util.error.LTResponseCode;
import com.lt.util.utils.DateTools;
import com.lt.util.utils.StringTools;
import com.lt.util.utils.jms.MessageQueueProducer;
import com.lt.util.utils.model.Response;

/**
 * 推广管理实现类
 * @author jingwb
 *
 */
@Service
public class PromoteManageServiceImpl implements IPromoteManageService{

	private Logger logger = LoggerTools.getInstance(getClass());
	
	@Autowired
	private PromoteLevelManageDao promoteLevelManageDao;
	@Autowired
	private PromoterFeeConfigManageDao promoterFeeConfigManageDao;
	@Autowired
	private PromoterManageDao promoterManageDao;
	@Autowired
	private RedisTemplate<String, String> redisTemplate;
	@Autowired
	private UserServiceMapperDao userServiceMapperDao;
	@Autowired
	private IPromoteApiService promoteApiService;
	
	
	@Override
	public Response addPromoterLevel(String level)throws Exception {
		//判断当前等级是否已存在
		PromoterLevel promoterLevel  = promoteLevelManageDao.selectPromoterLevelOne(level);
		if(promoterLevel != null){
			return LTResponseCode.getCode(LTResponseCode.PROMJ0011);
		}
		//添加等级
		promoteLevelManageDao.insertPromoterLevel(level);
		return LTResponseCode.getCode(LTResponseCode.SUCCESS);
	}

	@Override
	public void addShortCodes(String level, String promoterFeeConfig) throws Exception {				
		//获取推广员等级id
		PromoterLevel promoterLevel  = promoteLevelManageDao.selectPromoterLevelOne(level);
		if(promoterLevel  == null){
			throw new LTException(LTResponseCode.PROMJ0001);
		}
		
		//佣金设置
		List<PromoterFeeConfig> firstCommisionList = JSONObject.parseArray(promoterFeeConfig, PromoterFeeConfig.class);
				
		for(PromoterFeeConfig config : firstCommisionList){
			config.setLevelId(promoterLevel.getId());
		}
		
		//新增品种
		promoterFeeConfigManageDao.insertPromoterFeeConfigs(firstCommisionList);
	}

	@Override
	public void modifyPromoterConfig(PromoterFeeConfig promoterFeeConfig) throws Exception {
		promoterFeeConfigManageDao.updatePromoterFeeConfig(promoterFeeConfig);
	}

	@Override
	@Transactional
	public Response removePromoterLevel(String id) throws Exception {
		//判断等级是否被占用		
		Promoter promoter = new Promoter();
		promoter.setLevelId(Integer.valueOf(id));
		Integer count = promoterManageDao.selectPromoterCount(promoter);
		if(count > 0){
			return LTResponseCode.getCode(LTResponseCode.PROMJ0010);
		}
		//删除等级
		promoteLevelManageDao.deletePromoteLevel(Integer.valueOf(id));
		//删除佣金配置
		PromoterFeeConfig promoterFeeConfig = new PromoterFeeConfig();
		promoterFeeConfig.setLevelId(Integer.valueOf(id));
		promoterFeeConfigManageDao.deletePromoterFeeConfig(promoterFeeConfig);
		return LTResponseCode.getCode(LTResponseCode.SUCCESS);
	}

	@Override
	public List<PromoterLevel> getPromoterLevel() throws Exception {
		return promoteLevelManageDao.selectPromoterLevels();
	}

	@Override
	public List<PromoterFeeConfig> getPromoterFeeConfig(String levelId)
			throws Exception {
		return promoterFeeConfigManageDao.selectPromoterFeeConfigByLevelId(levelId);
	}

	@Override
	public Page<PromoteParamVo> queryPromoterPage(PromoteParamVo param) throws Exception {
		Page<PromoteParamVo> page = new Page<PromoteParamVo>();
		page.setPageNum(param.getPage());
		page.setPageSize(param.getRows());
		
		List<PromoteParamVo>  list = promoterManageDao.selectPromoterPage(param);
		Map<String, StatisticPromoterDayLog> map = promoteApiService.getPromoteInfoForToday();
		if(map != null){
			for(PromoteParamVo vo : list){			
				StatisticPromoterDayLog log = map.get(vo.getUserId());
				vo.setFirstRegisterCount((vo.getFirstRegisterCount()==null?0:vo.getFirstRegisterCount())+(log.getFirstRegisterCount()==null?0:log.getFirstRegisterCount()));
				vo.setFirstTraderCount((vo.getFirstTraderCount()==null?0:vo.getFirstTraderCount())+(log.getFirstTraderCount()==null?0:log.getFirstTraderCount()));
				vo.setFirstHandCount((vo.getFirstHandCount()==null?0:vo.getFirstHandCount())+(log.getFirstHandCount()==null?0:log.getFirstHandCount()));
				vo.setSecondHandCount((vo.getSecondHandCount()==null?0:vo.getSecondHandCount())+(log.getSecondHandCount()==null?0:log.getSecondHandCount()));
			}
		}
					
		page.setTotal(promoterManageDao.selectPromoterPageCount(param));
		page.addAll(list);
		return page;
	}

	@Override
	public Page<PromoteParamVo> queryPromoterDataPage(PromoteParamVo param)
			throws Exception {
		Page<PromoteParamVo> page = new Page<PromoteParamVo>();
		page.setPageNum(param.getPage());
		page.setPageSize(param.getRows());
		List<PromoteParamVo> list = promoterManageDao.selectPromoterDataPage(param);
		Map<String, StatisticPromoterDayLog> map = promoteApiService.getPromoteInfoForToday();
		for(PromoteParamVo vo : list){			
			StatisticPromoterDayLog log = map.get(vo.getUserId());
			vo.setFirstRegisterCount((vo.getFirstRegisterCount()==null?0:vo.getFirstRegisterCount())+(log.getFirstRegisterCount()==null?0:log.getFirstRegisterCount()));
			vo.setFirstRechargerCount((vo.getFirstRechargerCount()==null?0:vo.getFirstRechargerCount())+(log.getFirstRechargerCount()==null?0:log.getFirstRechargerCount()));
			vo.setFirstRechargeAmount((vo.getFirstRechargeAmount()==null?0.0:vo.getFirstRechargeAmount())+(log.getFirstRechargeAmount()==null?0.0:log.getFirstRechargeAmount()));
			vo.setFirstTraderCount((vo.getFirstTraderCount()==null?0:vo.getFirstTraderCount())+(log.getFirstTraderCount()==null?0:log.getFirstTraderCount()));
			vo.setFirstHandCount((vo.getFirstHandCount()==null?0:vo.getFirstHandCount())+(log.getFirstHandCount()==null?0:log.getFirstHandCount()));
			vo.setFirstTradeAmount((vo.getFirstTradeAmount()==null?0.0:vo.getFirstTradeAmount())+(log.getFirstTradeAmount()==null?0.0:log.getFirstTradeAmount()));
		}
		
		page.setTotal(promoterManageDao.selectPromoterPageCount(param));
		page.addAll(list);
		return page;
	}

	@Override
	public Page<PromoteParamVo> queryBrancherPage(PromoteParamVo param)
			throws Exception {
		Page<PromoteParamVo> page = new Page<PromoteParamVo>();
		page.setPageNum(param.getPage());
		page.setPageSize(param.getRows());
		List<PromoteParamVo> list = promoterManageDao.selectBrancherPage(param);
		Map<String, StatisticBrancherDayLog> map = promoteApiService.getBrancherInfoForToday(param.getUserId());
		for(PromoteParamVo vo : list){
			StatisticBrancherDayLog log = map.get(vo.getUserId());
			vo.setRechargeAmount(vo.getRechargeAmount()+(log.getRechargeAmount()==null?0.0:log.getRechargeAmount()));
			vo.setHandCount(vo.getHandCount()+log.getHandCount());
			vo.setTradeAmount(vo.getTradeAmount()+(log.getTradeAmount()==null?0.0:log.getTradeAmount()));
		}
		
		page.setTotal(promoterManageDao.selectBrancherPageCount(param));
		page.addAll(list);
		return page;
	}

	@Override
	public Response setPromoterLevel(String userId,String level) throws Exception {
		//判断等级是否存在 
		PromoterLevel promoterLevel  = promoteLevelManageDao.selectPromoterLevelOne(level);
		if(promoterLevel == null){
			return LTResponseCode.getCode(LTResponseCode.PROMJ0001);
		}
		//判断用户是否为推广员
		Promoter promoter = new Promoter();
		promoter.setUserId(userId);
		promoter.setFlag(1);
		Integer count = promoterManageDao.selectPromoterCount(promoter);
		if(count == 0){
			return LTResponseCode.getCode(LTResponseCode.PROMJ0013);
		}
		
		//修改等级
		promoter.setLevelId(promoterLevel.getId());
		promoter.setModifyUserId(0);
		promoterManageDao.updatePromoter(promoter);
		return LTResponseCode.getCode(LTResponseCode.SUCCESS);
	}

	@Override
	@Transactional
	public Response modifyPromoterMapper(String userId, String ssy)
			throws Exception {
		Boolean isInsert_=false;
		//如果ssy（所属于）为空，则认为原关系破裂--需求如此
		if(StringTools.isEmpty(ssy)){
			//修改原用户的关系为破裂
			PromoterUserMapper mapper = new PromoterUserMapper();
			mapper.setUserId(userId);
			mapper.setFlag(0);
			mapper.setModifyUserId(0);
			promoterManageDao.updatePromoterUserMapper(mapper);			
			//修改下线汇总状态为删除
			Map<String,Object> map = new HashMap<String,Object>();
			map.put("flag", 0);
			map.put("userId", userId);
			promoterManageDao.updateBrancherSummaryStatus(map);
		}else{
			//判断ssy用户是否存在
	        UserBase tele = new UserBase();
	        tele.setTele(ssy);
			String ssyId = promoterManageDao.selectUserId(tele);
			if(ssyId == null){
				 UserBase id = new UserBase();
				 id.setId(ssy);
				 ssyId = promoterManageDao.selectUserId(id);
				 if(ssyId == null){
					 return LTResponseCode.getCode(LTResponseCode.US01105);
				 }
			}
			
			if(userId.equals(ssyId)){
				return LTResponseCode.getCode(LTResponseCode.PROMJ0014);
			}
			
			//如果userId的上线已是ssyId,则不做修改
			Map<String, Object> um = promoterManageDao.selectPromoterUserMapper(userId);
			if(um != null){
				if(ssyId.equals(um.get("promoter_id"))){
					return LTResponseCode.getCode(LTResponseCode.PROMJ0014);
				}
			}
			
			//如果userId是ssyId的上线，则不做修改
			Map<String, Object> sm = promoterManageDao.selectPromoterUserMapper(ssyId.toString());
			if(sm != null){
				if(userId.equals(sm.get("promoter_id"))){
					return LTResponseCode.getCode(LTResponseCode.PROMJ0015);
				}
			}
			
			//修改原用户的关系为破裂
			PromoterUserMapper mapper = new PromoterUserMapper();
			mapper.setUserId(userId);
			mapper.setFlag(0);
			mapper.setModifyUserId(0);
			promoterManageDao.updatePromoterUserMapper(mapper);
			//修改原下线汇总数据状态为删除
			Map<String,Object> map = new HashMap<String,Object>();
			map.put("flag", 0);
			map.put("userId", userId);
			promoterManageDao.updateBrancherSummaryStatus(map);
			
			//判断下线汇总数据是否存在过，如果有，则修改状态即可
			StatisticBrancherSummaryLog log = new StatisticBrancherSummaryLog();
			log.setPromoterUserId(ssyId);
			log.setUserId(userId);
			log.setFlag(0);
			Integer lc = promoterManageDao.selectBrancherSummaryLogCount(log);
			if(lc != 0){
				//修改
				map.put("flag", 1);
				map.put("promoterUserId", ssyId);
				promoterManageDao.updateBrancherSummaryStatus(map);
			}else{
				//新增下线汇总数据
				promoterManageDao.initBrancherSummaryLog(log);
			}
			
			//判断下线日报数据是否存在，如果没有，则创建
			StatisticBrancherDayLog brancherDayLog = new StatisticBrancherDayLog();
			brancherDayLog.setPromoterUserId(ssyId);
			brancherDayLog.setUserId(userId);
			Calendar c = Calendar.getInstance();
			Date day = c.getTime();
			String dayStr = DateTools.parseToDefaultDateString(day);
			brancherDayLog.setStatisticTime(dayStr);
			Integer bc = promoterManageDao.selectBrancherDayLogCount(brancherDayLog);
			if(bc == 0){
				promoterManageDao.insertBrancherDayLog(brancherDayLog);
			}
					
			//判断是否与原上线建立过关系，如果有，则修改状态即可
			mapper.setPromoterId(ssyId);
			Integer count = promoterManageDao.selectPromoterUserMapperCount(mapper);
			if(count != 0){
				//修改
				mapper.setFlag(1);				
				promoterManageDao.updatePromoterUserMapper(mapper);
			}else{
				//新增关系
				mapper.setFlag(1);
				mapper.setModifyUserId(null);
				promoterManageDao.insertPromoterUserMapper(mapper);
				isInsert_=true;			
			}
			
		}	
		return LTResponseCode.getCode(LTResponseCode.SUCCESS,isInsert_);
	}

	@Override
	public Response queryPromoterInfo(String userId) throws Exception {
		Map<String, Object> map = promoterManageDao.selectPromoterInfo(userId);
		if(map != null){
			Map<String, StatisticPromoterDayLog> logMap = promoteApiService.getPromoteInfoForToday(userId);
			StatisticPromoterDayLog log = logMap.get(userId);
			map.put("first_register_count", Integer.valueOf(map.get("first_register_count").toString())+(log.getFirstRegisterCount()==null?0:log.getFirstRegisterCount()));
			map.put("second_register_count", Integer.valueOf(map.get("second_register_count").toString())+(log.getSecondRegisterCount()==null?0:log.getSecondRegisterCount()));
			map.put("first_trader_count", Integer.valueOf(map.get("first_trader_count").toString())+(log.getFirstTraderCount()==null?0:log.getFirstTraderCount()));
			map.put("second_trader_count", Integer.valueOf(map.get("second_trader_count").toString())+(log.getSecondTraderCount()==null?0:log.getSecondTraderCount()));
			map.put("first_hand_count", Integer.valueOf(map.get("first_hand_count").toString())+(log.getFirstHandCount()==null?0:log.getFirstHandCount()));
			map.put("second_hand_count", Integer.valueOf(map.get("second_hand_count").toString())+(log.getSecondHandCount()==null?0:log.getSecondHandCount()));
		}
		
		Map<String, Object> map1 = promoterManageDao.selectPromoterUserMapper(userId);
		if(map1 != null){
			if(map == null){
				map = new HashMap<String,Object>();
			}
			map.putAll(map1);
		}
		return LTResponseCode.getCode(LTResponseCode.SUCCESS,map);
	}

	@Override
	@Transactional
	public Response cancelPromoter(String userId) throws Exception {
		//判断当前用户是否为推广员
		Promoter promoter = new Promoter();
		promoter.setUserId(userId);
		promoter.setFlag(1);
		Integer count = promoterManageDao.selectPromoterCount(promoter);
		if(count == 0){
			return LTResponseCode.getCode(LTResponseCode.PROMJ0013);
		}
		
		//修改推广员的状态为删除
		promoter.setFlag(0);
		promoter.setModifyUserId(0);
		promoterManageDao.updatePromoter(promoter);
		//删除推广员服务
		UserServiceMapper mapper  = new UserServiceMapper();
		mapper.setUserId(userId);
		mapper.setServiceCode(ServiceContant.PROMOTER_SERVICE_CODE);
		userServiceMapperDao.deleteUserServiceMapper(mapper);
		return LTResponseCode.getCode(LTResponseCode.SUCCESS);
	}

	@Override
	@Transactional
	public Response renewPromoterMapper(String userId) throws Exception {
		//判断该用户目前是否只有一个上线历史
		
		//查询该用户最早的上线信息
		PromoterUserMapper mapp = promoterManageDao.selectFirstPromoterUserMapper(userId);
		if(mapp == null){
			return LTResponseCode.getCode(LTResponseCode.PROMJ0017);
		}
		//修改原用户的关系为破裂
		PromoterUserMapper mapper = new PromoterUserMapper();
		mapper.setUserId(userId);
		mapper.setFlag(0);
		mapper.setModifyUserId(0);
		promoterManageDao.updatePromoterUserMapper(mapper);
		//修改原下线汇总数据状态为删除
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("flag", 0);
		map.put("userId", userId);
		promoterManageDao.updateBrancherSummaryStatus(map);
		//建立原关系
		mapper.setFlag(1);	
		mapper.setPromoterId(mapp.getPromoterId());
		promoterManageDao.updatePromoterUserMapper(mapper);
		//恢复下线汇总数据
		map.put("flag", 1);
		map.put("promoterUserId", mapp.getPromoterId());
		promoterManageDao.updateBrancherSummaryStatus(map);
		return LTResponseCode.getCode(LTResponseCode.SUCCESS);
	}

	@Override
	public Page<PromoterFeeConfig> queryPromoterFeeConfigPage(
			PromoteParamVo param) throws Exception {
		Page<PromoterFeeConfig> page = new Page<PromoterFeeConfig>();
		page.setPageNum(param.getPage());
		page.setPageSize(param.getRows());
		page.setTotal(promoterFeeConfigManageDao.selectPromoterFeeConfigCount(param));
		page.addAll(promoterFeeConfigManageDao.selectPromoterFeeConfigPage(param));
		return page;
	}

	@Override
	public List<Product> getProShortCode(String level) throws Exception {
		return promoterFeeConfigManageDao.selectProShortCode(level);
	}

	@Override
	public Integer getProShortCodeCount(String level) throws Exception {
		Integer count = promoterFeeConfigManageDao.selectProShortCodeCount(level);
		if(count == 0){
			return 0;
		}else{
			return 1;
		}
	}

	@Override
	public void removePromoterFeeConfig(PromoterFeeConfig promoterFeeConfig) throws Exception {
		promoterFeeConfigManageDao.deletePromoterFeeConfig(promoterFeeConfig);
	}

}
