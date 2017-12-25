package com.lt.manager.service.impl.user;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.lt.enums.fund.FundThirdOptCodeEnum;
import com.lt.manager.bean.user.BankVo;
import com.lt.manager.bean.user.UserBaseInfoQueryVO;
import com.lt.manager.dao.user.BankManageDao;
import com.lt.manager.service.user.IBankManageService;
import com.lt.model.user.charge.BankChargeMapper;
import com.lt.model.user.charge.UserChargeMapper;
import com.lt.util.error.LTException;
import com.lt.util.error.LTResponseCode;
import com.lt.util.utils.DateTools;
import com.lt.util.utils.StringTools;

import javolution.util.FastList;
import javolution.util.FastMap;

/**
 * 银行卡管理实现类
 * 
 * @author licy
 *
 */
@Service
public class BankManageServiceImpl implements IBankManageService {
	
	Logger logger = LoggerFactory.getLogger(getClass());


	@Autowired
	private BankManageDao bankManageDao;

	@Override
	@Transactional
	public void addBankVo(BankVo bankVo) {
		try{
			this.bankManageDao.insertBankCharge(bankVo);
		}catch (Exception e) {
			e.printStackTrace();
			logger.error("【保存银行信息失败】");
			throw new LTException("【保存银行信息失败】");
		}
	}


	@Override
	public void modifyBankVo(BankVo bankVo) {
		this.bankManageDao.updateBankCharge(bankVo);
	}

	@Override
	public void removeBankVo(BankVo bankVo) {
		this.bankManageDao.deleteBankInfo(bankVo);
	}

	@Override
	public BankVo queryBankVo(String bankCode) {
		
		BankVo bankVo =  this.bankManageDao.selectBankVo(bankCode);
		return bankVo;
	}

	@Override
	public List<BankVo> queryBankInfoList() {
		return this.bankManageDao.selectBankInfoList(null);
	}

	
	
	@Override
	public List<BankVo> queryBankChannelList() {
		return this.bankManageDao.selectChargeChannelList();
	}

	@Override
	public String addBankInfo(BankVo bankVo) {
		try{
			String bankCode = this.bankManageDao.selectMaxBankCode();//查询最大的银行编号
			if (StringTools.isEmpty(bankCode)) {
				bankCode = "0000";
			}else{
				bankCode = Integer.valueOf(bankCode)+1+"";
				while (bankCode.length()!=4) {
					bankCode = "0"+bankCode;
				}
			}
			bankVo.setBankCode(bankCode);
			this.bankManageDao.insertBankInfo(bankVo);
			return bankCode;
		}catch (Exception e) {
			e.printStackTrace();
			logger.error("【保存银行信息失败】");
			throw new LTException("【保存银行信息失败】");
		}
	}


	@Override
	public List<BankVo> queryChannelByBankCode(String bankCode) {
		return this.bankManageDao.selectChannelByBankCode(bankCode);
	}



	@Override
	public List<BankVo> queryBankVoList(BankVo bankVo) {
		try {
			List<BankVo> bankInfoList = this.bankManageDao.selectBankInfoList(bankVo);
			if(bankInfoList==null || bankInfoList.size()<1){
				throw new LTException("【查询银行信息为空！】");
			}
			logger.info("【查询银行信息总数】"+bankInfoList.size());
			for(int i = 0; i < bankInfoList.size(); i++){
				logger.info("bankCode============="+bankInfoList.get(i).getBankCode());
				List<BankVo> bankVos = this.bankManageDao.selectChannelByBankCode(bankInfoList.get(i).getBankCode());
				logger.info("【查询银行渠道总数】"+bankVos.size());
				Map<String, Object> alipay = FastMap.newInstance();
				Map<String, Object> kqpay = FastMap.newInstance();
				Map<String, Object> unspay = FastMap.newInstance();
				Map<String, Object> dinpay = FastMap.newInstance();
				Map<String, Object> xdpay = FastMap.newInstance();
				Map<String, Object> handalipay = FastMap.newInstance();
				Map<String, Object> xlpay1 = FastMap.newInstance();
				Map<String, Object> xlpay2 = FastMap.newInstance();
				Map<String, Object> xlpay3 = FastMap.newInstance();
				Map<String, Object> xlpay4 = FastMap.newInstance();
				Map<String, Object> xlpay5 = FastMap.newInstance();
				Map<String, Object> xlpay6 = FastMap.newInstance();
				Map<String, Object> xlpay7 = FastMap.newInstance();
				Map<String, Object> daddyPay = FastMap.newInstance();
				Date updateDate = null;
				for (int j = 0; j < bankVos.size(); j++) {
					String channelId = bankVos.get(j).getChannelId();
					if(updateDate==null){
						updateDate = bankVos.get(j).getUpdateDate();
					}else{
						Calendar calendar1 = Calendar.getInstance();
						calendar1.setTime(updateDate);
						Calendar calendar2 = Calendar.getInstance();
						calendar2.setTime(bankVos.get(j).getUpdateDate());
						if(calendar1.before(calendar2)){
							updateDate = bankVos.get(j).getUpdateDate();
						}
						
					}
					if(FundThirdOptCodeEnum.ZFBCZ.getThirdLevelCode().equals(channelId)){
						alipay.put("singleLimit", bankVos.get(j).getSingleLimit());
						alipay.put("dailyLimit", bankVos.get(j).getDailyLimit());
					}else if (FundThirdOptCodeEnum.KQCZ.getThirdLevelCode().equals(channelId)) {
						kqpay.put("singleLimit", bankVos.get(j).getSingleLimit());
						kqpay.put("dailyLimit", bankVos.get(j).getDailyLimit());
					}else if (FundThirdOptCodeEnum.YSBCZ.getThirdLevelCode().equals(channelId)) {
						unspay.put("singleLimit", bankVos.get(j).getSingleLimit());
						unspay.put("dailyLimit",bankVos.get(j).getDailyLimit());
					}else if (FundThirdOptCodeEnum.DINPAY_MOB.getThirdLevelCode().equals(channelId)) {
						dinpay.put("singleLimit", bankVos.get(j).getSingleLimit());
						dinpay.put("dailyLimit",bankVos.get(j).getDailyLimit());
					}else if(FundThirdOptCodeEnum.XDPAY.getThirdLevelCode().equals(channelId)){
						xdpay.put("singleLimit", bankVos.get(j).getSingleLimit());
						xdpay.put("dailyLimit",bankVos.get(j).getDailyLimit());
					}else if(FundThirdOptCodeEnum.HANDAIPAY.getThirdLevelCode().equals(channelId)){
						handalipay.put("singleLimit", bankVos.get(j).getSingleLimit());
						handalipay.put("dailyLimit", bankVos.get(j).getDailyLimit());
					}else if(FundThirdOptCodeEnum.XLPAY1.getThirdLevelCode().equals(channelId)){
						xlpay1.put("singleLimit", bankVos.get(j).getSingleLimit());
						xlpay1.put("dailyLimit", bankVos.get(j).getDailyLimit());
					}else if(FundThirdOptCodeEnum.XLPAY2.getThirdLevelCode().equals(channelId)){
						xlpay2.put("singleLimit", bankVos.get(j).getSingleLimit());
						xlpay2.put("dailyLimit", bankVos.get(j).getDailyLimit());
					}else if(FundThirdOptCodeEnum.XLPAY3.getThirdLevelCode().equals(channelId)){
						xlpay3.put("singleLimit", bankVos.get(j).getSingleLimit());
						xlpay3.put("dailyLimit", bankVos.get(j).getDailyLimit());
					}else if(FundThirdOptCodeEnum.XLPAY4.getThirdLevelCode().equals(channelId)){
						xlpay4.put("singleLimit", bankVos.get(j).getSingleLimit());
						xlpay4.put("dailyLimit", bankVos.get(j).getDailyLimit());
					}else if(FundThirdOptCodeEnum.XLPAY5.getThirdLevelCode().equals(channelId)){
						xlpay5.put("singleLimit", bankVos.get(j).getSingleLimit());
						xlpay5.put("dailyLimit", bankVos.get(j).getDailyLimit());
					}else if(FundThirdOptCodeEnum.XLPAY6.getThirdLevelCode().equals(channelId)){
						xlpay6.put("singleLimit", bankVos.get(j).getSingleLimit());
						xlpay6.put("dailyLimit", bankVos.get(j).getDailyLimit());
					}else if(FundThirdOptCodeEnum.XLPAY7.getThirdLevelCode().equals(channelId)){
						xlpay7.put("singleLimit", bankVos.get(j).getSingleLimit());
						xlpay7.put("dailyLimit", bankVos.get(j).getDailyLimit());
					}
					else if(FundThirdOptCodeEnum.DADDYPAY.getThirdLevelCode().equals(channelId)){
						daddyPay.put("singleLimit", bankVos.get(j).getSingleLimit());
						daddyPay.put("dailyLimit", bankVos.get(j).getSingleLimit());
					}

				}
				bankInfoList.get(i).setUpdateDate(updateDate);
				bankInfoList.get(i).setResver1(DateTools.parseToDefaultDateTimeString(updateDate));
				bankInfoList.get(i).setAlipay(alipay);
				bankInfoList.get(i).setKqpay(kqpay);
				bankInfoList.get(i).setUnspay(unspay);
				bankInfoList.get(i).setDinpay(dinpay);
				bankInfoList.get(i).setXdpay(xdpay);
				bankInfoList.get(i).setHandalipay(handalipay);
				bankInfoList.get(i).setXlpay1(xlpay1);
				bankInfoList.get(i).setXlpay2(xlpay2);
				bankInfoList.get(i).setXlpay3(xlpay3);
				bankInfoList.get(i).setXlpay4(xlpay4);
				bankInfoList.get(i).setXlpay5(xlpay5);
				bankInfoList.get(i).setXlpay6(xlpay6);
				bankInfoList.get(i).setXlpay7(xlpay7);
				bankInfoList.get(i).setDaddyPay(daddyPay);
			}
			return bankInfoList;
			
		} catch (Exception e) {
			e.printStackTrace();
			throw new LTException(LTResponseCode.ER400);
		}
	}


	@Override
	public List<BankVo> queryBankVoByBankCode(String bankCode) {
		return this.bankManageDao.selectBankVoByBankCode(bankCode);
	}

	@Override
	public void initUserChargeMapper(String channelCode) {
		List<UserBaseInfoQueryVO> userList =  bankManageDao.qryAllUser();
		
		List<UserChargeMapper> mapperList = new ArrayList<UserChargeMapper>();
		if(CollectionUtils.isNotEmpty(userList)){
			for(UserBaseInfoQueryVO baseInfoQueryVO : userList){
				if(channelCode == null || channelCode.equals("")){
					UserChargeMapper chargeMapper = new UserChargeMapper(baseInfoQueryVO.getUserId(), FundThirdOptCodeEnum.KQCZ.getThirdLevelCode());
					UserChargeMapper chargeMapper1 = new UserChargeMapper(baseInfoQueryVO.getUserId(), FundThirdOptCodeEnum.YSBCZ.getThirdLevelCode());
					UserChargeMapper chargeMapper2 = new UserChargeMapper(baseInfoQueryVO.getUserId(), FundThirdOptCodeEnum.ZFBCZ.getThirdLevelCode());
					UserChargeMapper chargeMapper3 = new UserChargeMapper(baseInfoQueryVO.getUserId(), FundThirdOptCodeEnum.DINPAY_MOB.getThirdLevelCode());
					UserChargeMapper chargeMapper4 = new UserChargeMapper(baseInfoQueryVO.getUserId(), FundThirdOptCodeEnum.XDPAY.getThirdLevelCode());
					mapperList.add(chargeMapper2);
					mapperList.add(chargeMapper1);
					mapperList.add(chargeMapper);
					
					mapperList.add(chargeMapper3);
					mapperList.add(chargeMapper4);
				}else{
					UserChargeMapper chargeMapper = new UserChargeMapper(baseInfoQueryVO.getUserId(), channelCode);
					mapperList.add(chargeMapper);
				}
				
			}
			
			bankManageDao.insertUserChargeMapperMutil(mapperList);
		}
	}


	@Override
	public void modifyBankInfo(BankVo bankVo) {
		try{
			this.bankManageDao.updateBankInfo(bankVo);
		}catch(Exception e){
			e.printStackTrace();
			logger.error("【修改银行基础信息异常】");
			throw new LTException("【修改银行基础信息异常】");
		}
		
	}


	@Override
	public BankVo queryBankChannelPriorityList() {
		try {
			List<BankVo> chargeChannelList= this.bankManageDao.selectChargeChannelPriorityList();
			if(chargeChannelList==null || chargeChannelList.size()<1){
				throw new LTException("【查询银行信息为空！】");
			}
			logger.info("【查询银行信息总数】"+chargeChannelList.size());
			BankVo bankChannelPriority = new BankVo();
			Map<String, Object> alipay = FastMap.newInstance();
			Map<String, Object> kqpay = FastMap.newInstance();
			Map<String, Object> unspay = FastMap.newInstance();
			Map<String, Object> dinpay = FastMap.newInstance();
			Map<String, Object> xdpay = FastMap.newInstance();
			for(int i = 0; i < chargeChannelList.size(); i++){

				String channelId = chargeChannelList.get(i).getChannelId();
				if("1010101".equals(channelId)){
					alipay.put("priority", chargeChannelList.get(i).getPriority());
				}else if ("1010102".equals(chargeChannelList)) {
					kqpay.put("priority", chargeChannelList.get(i).getPriority());
				}else if ("1010103".equals(channelId)) {
					unspay.put("priority", chargeChannelList.get(i).getPriority());
				}else if ("1010105".equals(channelId)) {
					dinpay.put("priority", chargeChannelList.get(i).getPriority());
				}else if("1010107".equals(channelId)){
					xdpay.put("priority", chargeChannelList.get(i).getPriority());
				}
			}
			bankChannelPriority.setAlipay(alipay);
			bankChannelPriority.setKqpay(kqpay);
			bankChannelPriority.setUnspay(unspay);
			bankChannelPriority.setDinpay(dinpay);
			bankChannelPriority.setXdpay(xdpay);
			return bankChannelPriority;
			
		} catch (Exception e) {
			e.printStackTrace();
			throw new LTException("error");
		}
	}


	@Override
	public Integer queryBankVoCount() {
		return this.bankManageDao.selectBankInfoCount();
	}



	public List<BankVo> queryBankInfoList1(BankVo bankVo) {
		try {
			List<BankVo> bankInfoList = this.bankManageDao.selectBankInfoList(bankVo);
			for(int i = 0; i < bankInfoList.size(); i++){
				logger.info("bankCode============="+bankInfoList.get(i).getBankCode());
				List<BankChargeMapper> bankLimitInfo = this.bankManageDao.selectBankLimitInfoList(bankInfoList.get(i).getBankCode());
				bankInfoList.get(i).setLimitInfo(bankLimitInfo);
			}
			if(bankInfoList==null || bankInfoList.size()<1){
				throw new LTException("【查询银行信息为空！】");
			}
			logger.info("【查询银行信息总数】"+bankInfoList.size());
			return bankInfoList;
		}catch(Exception e){
			logger.error("【查询银行卡信息异常】");
			e.printStackTrace();
			throw new LTException("【查询银行卡信息异常】");
		}
	}
	@Override
	public List<Map<String, Object>> queryBankInfoList(BankVo bankVo) {
		try {
			List<BankVo> bankInfoList = this.bankManageDao.selectBankInfoList(bankVo);
			if(bankInfoList==null || bankInfoList.size()<1){
				throw new LTException("【查询银行信息为空！】");
			}
			List<Map<String, Object>> resultMap = FastList.newInstance();
			
			for(int i=0;i<bankInfoList.size();i++){
				Map<String, Object> map = FastMap.newInstance();
				map.put("bankCode", bankInfoList.get(i).getBankCode());
				map.put("bankPic", bankInfoList.get(i).getBankPic());
				map.put("bankName", bankInfoList.get(i).getBankName());
				List<BankChargeMapper> bankLimitInfo = this.bankManageDao.selectBankLimitInfoList(bankInfoList.get(i).getBankCode());
				for(BankChargeMapper chargeMapper:bankLimitInfo){
					map.put("singleLimit_"+chargeMapper.getChannelId(), chargeMapper.getSingleLimit());
					map.put("dailyLimit_"+chargeMapper.getChannelId(), chargeMapper.getDailyLimit());
					if(chargeMapper.getUpdateDate()!=null){
						map.put("reserve", DateTools.formatDate(chargeMapper.getUpdateDate(), "yyyy-MM-dd HH:mm:ss"));
					}else{
						map.put("reserve", DateTools.formatDate(chargeMapper.getCreateDate(), "yyyy-MM-dd HH:mm:ss"));
					}
					
				}
				resultMap.add(map);
			}
			logger.info("【查询银行信息总数】"+bankInfoList.size());
			return resultMap;
		}catch(Exception e){
			logger.error("【查询银行卡信息异常】");
			e.printStackTrace();
			throw new LTException("【查询银行卡信息异常】");
		}
	}


}
