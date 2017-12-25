package com.lt.business.core.service.promote.impl;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSONObject;
import com.lt.api.fund.IFundAccountApiService;
import com.lt.business.core.dao.promote.ICommisionFlowDao;
import com.lt.business.core.dao.promote.ICommisionIoDao;
import com.lt.business.core.dao.promote.ICommisionMainDao;
import com.lt.business.core.dao.promote.ICommisionOptcodeDao;
import com.lt.business.core.service.promote.ICommisionService;
import com.lt.enums.promote.CommisionOptcodeEnum;
import com.lt.model.promote.CommisionFlow;
import com.lt.model.promote.CommisionIo;
import com.lt.model.promote.CommisionMain;
import com.lt.model.promote.CommisionOptcode;
import com.lt.model.statistic.StatisticPromoterDayLog;
import com.lt.util.error.LTException;
import com.lt.util.error.LTResponseCode;
import com.lt.util.utils.DoubleUtils;
/**
 * 佣金实现类
 * @author jingwb
 *
 */
@Service
public class CommisionServiceImpl implements ICommisionService{

	Logger logger = LoggerFactory.getLogger(getClass());
	
	@Autowired
	private ICommisionFlowDao commisionFlowDao;
	@Autowired
	private ICommisionMainDao commisionMainDao;
	@Autowired
	private ICommisionOptcodeDao commisionOptcodeDao;
	@Autowired
	private ICommisionIoDao commisionIoDao;
	@Autowired
	private IFundAccountApiService fundAccountApiService;
	
	@Override
	@Transactional
	public void modifyCommisionMains(List<CommisionMain> list) throws Exception {
		commisionMainDao.updateCommisionMains(list);
	}

	@Override
	@Transactional
	public void balanceCommision(List<StatisticPromoterDayLog> list)
			throws Exception {
		//获取佣金流转方式配置
		CommisionOptcode commisionOptcode = new CommisionOptcode("","",CommisionOptcodeEnum.BALANCE.getThirdOptcode());
		commisionOptcode = getCommisionOptcode(commisionOptcode);
		
		
		List<CommisionMain> mains = new ArrayList<CommisionMain>();
		List<CommisionFlow> flows = new ArrayList<CommisionFlow>();
		for(StatisticPromoterDayLog statisticPromoterDayLog : list){
			CommisionMain main = new CommisionMain();
			main.setUserId(statisticPromoterDayLog.getUserId());
			main.setCommisionBalance(statisticPromoterDayLog.getBalanceCommision());
			mains.add(main);
			
			CommisionFlow flow = new CommisionFlow(statisticPromoterDayLog.getUserId(), commisionOptcode.getFlowType(), 
					commisionOptcode.getFirstOptcode(), commisionOptcode.getSecondOptcode(), commisionOptcode.getThirdOptcode(),
					statisticPromoterDayLog.getBalanceCommision(), 
					DoubleUtils.add(statisticPromoterDayLog.getCommisionBalance(),statisticPromoterDayLog.getBalanceCommision()), 
					commisionOptcode.getRemark());
			flows.add(flow);
		}
		//更新佣金账户
		commisionMainDao.updateCommisionMains(mains);
		//插入佣金结算流水
		logger.info("------------flows={}---------",JSONObject.toJSONString(flows));
		commisionFlowDao.insertCommisionFlows(flows);
	}

	/**
	 * 获取佣金流转方式信息
	 * @param code
	 * @return
	 */
	public CommisionOptcode getCommisionOptcode(CommisionOptcode code){
		List<CommisionOptcode> list = commisionOptcodeDao.selectCommisionOptcode(code);
		code =list.get(0);
		return code;
	}

	@Override
	@Transactional
	public void commisionWidthdrawApply(String userId, Double amount)
			throws Exception {
		//获取佣金账户
		CommisionMain main = commisionMainDao.selectCommisionMain(userId);
		if(main == null){
			throw new LTException(LTResponseCode.PROMJ0004);
		}
		//判断余额
		if(main.getCommisionBalance() < amount){
			throw new LTException(LTResponseCode.PROMJ0009);	
		}
		
		//获取佣金提现申请业务码
		CommisionOptcode commisionOptcode = new CommisionOptcode("", "", CommisionOptcodeEnum.APPLY.getThirdOptcode());
		commisionOptcode = getCommisionOptcode(commisionOptcode);
		if(commisionOptcode == null){
			throw new LTException(LTResponseCode.PROMJ0007);		
		}
				
		//更新佣金余额
		CommisionMain main1 = new CommisionMain();
		main1.setUserId(userId);
		main1.setCommisionBalance(-amount);
		commisionMainDao.updateCommisionMain(main1);
				
		//生成佣金存取明细，状态待审核
		CommisionIo io = new CommisionIo(userId, commisionOptcode.getFlowType(), commisionOptcode.getFirstOptcode(),
				commisionOptcode.getSecondOptcode(), commisionOptcode.getThirdOptcode(), amount, main.getCommisionBalance()-amount, 
				0, commisionOptcode.getRemark(), 0);
		commisionIoDao.insertCommisionIo(io);
		//生成佣金提现申请流水
		CommisionFlow flow = new CommisionFlow(userId, commisionOptcode.getFlowType(), commisionOptcode.getFirstOptcode(),
				commisionOptcode.getSecondOptcode(), commisionOptcode.getThirdOptcode(), amount,main.getCommisionBalance()-amount, 
				commisionOptcode.getRemark());		
		commisionFlowDao.insertCommisionFlow(flow);
	}

	@Override
	@Transactional
	public void commisionWidthdrawPass(String ioId) throws Exception {
		//获取佣金存取明细
		CommisionIo io = commisionIoDao.selectCommisionIoById(ioId);
		if(io == null){
			throw new LTException(LTResponseCode.PROMJ0005);
		}
		if(io.getStatus() != 0){
			throw new LTException(LTResponseCode.PROMJ0006);
		}
		//获取佣金账户
		CommisionMain main = commisionMainDao.selectCommisionMain(io.getUserId().toString());
		if(main == null){
			throw new LTException(LTResponseCode.PROMJ0004);
		}
		//更新佣金账户--已提现佣金
		CommisionMain main1 = new CommisionMain();
		main1.setUserId(io.getUserId());
		main1.setWithdrawCommision(io.getAmount());
		commisionMainDao.updateCommisionMain(main1);
		//更新佣金存取明细，状态为通过
		CommisionIo io1 = new CommisionIo();
		io1.setId(io.getId());
		io1.setStatus(1);
		io1.setModifyUserId(0);
		commisionIoDao.updateCommisionIoById(io1);
		//生成佣金转现流水（调用资金接口）
		fundAccountApiService.commisionWithdraw(io);
	}

	@Override
	@Transactional
	public void commisionWidthdrawNoPass(String ioId) throws Exception {
		//获取佣金存取明细
		CommisionIo io = commisionIoDao.selectCommisionIoById(ioId);
		if(io == null){
			throw new LTException(LTResponseCode.PROMJ0005);
		}
		if(io.getStatus() != 0){
			throw new LTException(LTResponseCode.PROMJ0006);
		}
		//获取佣金账户
		CommisionMain main = commisionMainDao.selectCommisionMain(io.getUserId().toString());
		if(main == null){
			throw new LTException(LTResponseCode.PROMJ0004);
		}
		//获取佣金退回业务码
		CommisionOptcode commisionOptcode = new CommisionOptcode("", "", CommisionOptcodeEnum.NOPASS.getThirdOptcode());
		commisionOptcode = getCommisionOptcode(commisionOptcode);
		if(commisionOptcode == null){
			throw new LTException(LTResponseCode.PROMJ0007);		
		}
		//更新佣金账户--佣金余额，返回提现金额
		CommisionMain main1 = new CommisionMain();
		main1.setUserId(io.getUserId());
		main1.setCommisionBalance(io.getAmount());
		commisionMainDao.updateCommisionMain(main1);
		//更新佣金存取明细，状态为拒绝
		CommisionIo io1 = new CommisionIo();
		io1.setId(io.getId());
		io1.setStatus(2);
		io1.setModifyUserId(0);
		commisionIoDao.updateCommisionIoById(io1);
		//生成佣金退回流水
		CommisionFlow flow = new CommisionFlow(io.getUserId(), commisionOptcode.getFlowType(), commisionOptcode.getFirstOptcode(),
				commisionOptcode.getSecondOptcode(), commisionOptcode.getThirdOptcode(), io.getAmount(),DoubleUtils.add(main.getCommisionBalance(),io.getAmount()), 
				commisionOptcode.getRemark());	
		commisionFlowDao.insertCommisionFlow(flow);
	}
}
