package com.lt.business.core.service.promote.impl;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.lt.api.promote.IPromoteApiService;
import com.lt.business.core.service.promote.ICommisionService;
import com.lt.business.core.service.promote.IPromoteService;
import com.lt.business.core.service.task.IStatisticTaskService;
import com.lt.model.statistic.StatisticBrancherDayLog;
import com.lt.model.statistic.StatisticPromoterDayLog;
import com.lt.util.error.LTException;
import com.lt.util.utils.StringTools;
import com.lt.util.utils.model.Response;

@Service
public class PromoteApiServiceImpl implements IPromoteApiService{

	/**
	 * 
	 */
	private static final long serialVersionUID = 255947057579142015L;
	@Autowired
	private IPromoteService promoteService;
	@Autowired
	private ICommisionService commisionService;
	@Autowired
	private IStatisticTaskService statisticTaskService;
	
	@Override
	public void activatePromoter(String userId) throws Exception{
		promoteService.activatePromoter(userId);
	}

	@Override
	public void visitCount(Integer count, String userId) {
		promoteService.visitCount(count, userId);
	}

	@Override
	public void addPromoteLibrary(String userId, String ip, String tele) {
		promoteService.addPromoteLibrary(userId, ip, tele);
	}

	@Override
	public int getVisitCount(String userId) {
		return promoteService.getVisitCount(userId);
	}

	@Override
	public void addPromoteUserMapper(String userId, String ip, String tele)
			throws LTException {
		promoteService.addPromoteUserMapper(userId, ip, tele);
	}

	@Override
	public void commisionWidthdrawApply(String userId, Double amount)
			throws Exception {
		commisionService.commisionWidthdrawApply(userId, amount);
		
	}

	@Override
	public void commisionWidthdrawPass(String ioId) throws Exception {
		commisionService.commisionWidthdrawPass(ioId);
	}

	@Override
	public void commisionWidthdrawNoPass(String ioId) throws Exception {
		commisionService.commisionWidthdrawNoPass(ioId);
	}

	@Override
	public void balanceCommisionTask(String day) throws Exception {
		if(StringTools.isEmpty(day)){
			statisticTaskService.balanceCommisionTask();
		}else{
			statisticTaskService.balanceCommisionTask(day);
		}
		
	}

	@Override
	public Response isPromoter(String userId) throws Exception {
		return promoteService.isPromoter(userId);
	}

	@Override
	public Response getPromoterInfo(String userId) throws Exception {
		return promoteService.getPromoterInfo(userId);
	}

	@Override
	public Response getBrancherInfos(String userId) throws Exception {
		return promoteService.getBrancherInfos(userId);
	}

	@Override
	public Response getPromoterUrl(String userId) {
		return promoteService.getPromoterUrl(userId);
	}

	@Override
	public Map<String, StatisticPromoterDayLog> getPromoteInfoForToday(
			String userId) {
		return promoteService.getPromoteInfoForToday(userId);
	}

	@Override
	public Map<String, StatisticBrancherDayLog> getBrancherInfoForToday(
			String userId) {
		return promoteService.getBrancherInfoForToday(userId);
	}

	@Override
	public void initStatisticDayLog(String day) throws Exception {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Map<String, StatisticPromoterDayLog> getPromoteInfoForToday() {
		// TODO Auto-generated method stub
		return null;
	}

}
