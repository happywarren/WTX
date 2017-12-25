package com.lt.fund.schedule;

import java.util.Date;
import java.util.List;

import com.lt.api.sys.IThreadLockService;
import com.lt.enums.sys.SysThreadLockEnum;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;
import com.lt.enums.fund.FundCashOptCodeEnum;
import com.lt.enums.fund.IFundOptCode;
import com.lt.fund.dao.FundFlowCashDao;
import com.lt.fund.dao.FundMainCashDao;
import com.lt.fund.dao.SettleTempDao;
import com.lt.model.fund.FundFlow;
import com.lt.model.fund.FundMainCash;
import com.lt.model.settle.SettleTmpBean;
import com.lt.util.error.LTException;
import com.lt.util.utils.DoubleUtils;
import com.lt.util.utils.StringTools;

/**
 * 入账处理
 * @author guodw
 *
 */
@Service
public class RecordsExec {
	private Logger logger = LoggerFactory.getLogger(getClass());

	// 定时处理 每5秒数据库获取一次
	@Autowired
	private SettleTempDao settleDao;
	@Autowired
	private FundMainCashDao fundMainCashDao;
	@Autowired
	private FundFlowCashDao fundFlowCashDao;
	@Autowired
	private IThreadLockService threadLockService;
	
	public void exec() {
		logger.info("执行入账操作开始---------------------------");

		if(!threadLockService.lock(SysThreadLockEnum.RECORDS_EXEC_TASK.getCode())){
			logger.info("入账操作执行中，入账操作结束---------------------------");
			return;
		}
		// 查询临时表数据
		List<SettleTmpBean> list = settleDao.findAll();
		for (SettleTmpBean settleTmpBean : list) {
			try {
				doExecRecords(settleTmpBean);
			} catch (Exception e) {
				continue;
			}
		}

		threadLockService.unlock(SysThreadLockEnum.RECORDS_EXEC_TASK.getCode());

		logger.info("执行入账操作结束---------------------------");
	}
	
	/**
	 * 执行入账操作
	 * @param settleTmpBean
	 */
	@Transactional(rollbackFor=Exception.class)
	private void doExecRecords(SettleTmpBean settleTmpBean){
		try {
			logger.info("入账模块增加settleTmpBean:{}",JSONObject.toJSON(settleTmpBean));
			// 处理用户资金 新增
			String userId = settleTmpBean.getUserId();
			// 锁用户资金表
			FundMainCash fundMainCash = fundMainCashDao
					.queryFundMainCashForUpdate(userId);
			// 余额新增
			
			double balance = DoubleUtils.add(fundMainCash.getBalance() , settleTmpBean.getAmt());
			// 增加用户资金新增明细
			FundFlow fundFlow = adapte(settleTmpBean, balance);
			// 流水日志插入
			fundFlowCashDao.addFundFlowCash(fundFlow);
			logger.info("入账模块增加userId:{}流水明细",userId);
			//余额变动
			fundMainCash.setBalance(settleTmpBean.getAmt());
			fundMainCashDao.updateFundMainCash(fundMainCash);
			logger.info("入账模块增加userId:{},余额:{}",userId,balance);
			settleDao.delete(settleTmpBean.getId());
			// 更新订单结算状态
			int i = settleDao.updateOrderStatus(settleTmpBean.getExternId());
			if(i>0){
				logger.info("入账2模块增加userId:{},余额:{}",userId,balance);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		
		// 操作日志记录
	}
	
	/**
	 * 根据结算类型 选择对应的现金业务编码
	 * @param settleTmpBean
	 * @param balance
	 * @return
	 */
	private FundFlow adapte(SettleTmpBean settleTmpBean,double balance){
		switch (settleTmpBean.getBalanceType()) {
		case 1:
			return execFund(settleTmpBean, balance,FundCashOptCodeEnum.INCOME_INVESTOR_FEE);
		case 2:
			return execFund(settleTmpBean, balance,FundCashOptCodeEnum.USER_PROFIT);
		default:
			throw new LTException("结算类型没有对应的处理方法");
		}
	}
	
	/**
	 * 组装流水对象
	 * @param settleTmpBean
	 * @param balance
	 * @param IFundOptCode
	 * @return
	 */
	private FundFlow execFund(SettleTmpBean settleTmpBean,double balance,IFundOptCode IFundOptCode){
		// 当前时间
		Date currentTime = new Date();
		// 增加用户资金新增明细
		FundFlow fundFlow = new FundFlow(settleTmpBean.getUserId(), IFundOptCode.getInout(), 
				IFundOptCode.getFirstLevelCode(),
				IFundOptCode.getCode(), IFundOptCode.getCode(),
				settleTmpBean.getAmt(), balance, settleTmpBean.getExternId(),
//				settleTmpBean.getRemark(),currentTime, currentTime);
				IFundOptCode.getName(),currentTime, currentTime);
		return fundFlow;
		
	}
	
}
