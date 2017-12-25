package com.lt.adapter.adapter.fund.func;

import java.util.Date;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.druid.util.StringUtils;
import com.lt.adapter.adapter.BaseFunction;
import com.lt.api.business.product.IProductApiService;
import com.lt.api.fund.IFundAccountApiService;
import com.lt.api.trade.IOrderApiService;
import com.lt.api.user.IUserApiBussinessService;
import com.lt.enums.fund.FundThirdOptCodeEnum;
import com.lt.enums.sys.CurrencyEnum;
import com.lt.model.user.UserBankInfo;
import com.lt.model.user.UserBussinessInfo;
import com.lt.util.error.LTException;
import com.lt.util.error.LTResponseCode;
import com.lt.util.utils.DateTools;
import com.lt.util.utils.StringTools;
import com.lt.util.utils.model.Response;

@Service
public class WithdrawApplyFunc extends BaseFunction {
	
	Logger logger = LoggerFactory.getLogger(getClass());
	
	@Autowired
	private IFundAccountApiService fundAccountApiService;
	
	@Autowired
	private IUserApiBussinessService userApiBussinessService;
	
	@Autowired
	private IOrderApiService orderApiService;
	
	@Autowired
	private IProductApiService productApiServiceImpl;
	
	@Override
	public Response response(Map<String, Object> paraMap) {
		Double outAmount = Double.valueOf(paraMap.get("outAmount").toString());
		Double rate = productApiServiceImpl.getRate(CurrencyEnum.DEFAULT_CURRENCY.getCurrencyCode());
		logger.info("==========outAmount={}==",outAmount);
		/*String withdrawCode = Utils.formatStr("withdrawCode","");//提现业务码
		String faxCode =Utils.formatStr("faxCode","");//提现手续费业务码
		*/		
		String userId = StringTools.formatStr(paraMap.get("userId"), "");
		String sign = StringTools.formatStr(paraMap.get("externalId"), String.valueOf(System.currentTimeMillis()));
		Response  resp = null;
		try{
				
			String withdrawCode = FundThirdOptCodeEnum.WITHDRAW_AMT.getThirdLevelCode();//提现业务码
			String faxCode = FundThirdOptCodeEnum.WITHDRAW_FEE.getThirdLevelCode();//提现手续费业务码
			if(outAmount == null || StringUtils.isEmpty(withdrawCode) || StringUtils.isEmpty(faxCode)){
				return LTResponseCode.getCode(LTResponseCode.FU00003);
			}
			//获取用户历史下单手数
			Integer count = orderApiService.queryOrdersCounts(userId);
			boolean flag = count>0?false:true;//true:收取手续费
			//获取用户信息
			UserBankInfo bankInfo = new UserBankInfo();
			bankInfo.setUserId(userId);
			UserBussinessInfo ubi = userApiBussinessService.getUserBankList(bankInfo);
			//提现申请
			Map<String, String> map = fundAccountApiService.doWithdrawApply(userId, outAmount,rate,withdrawCode,faxCode,flag,sign);
			if(ubi == null){
				map.put("userName", "****");
				map.put("bank", "****");
			}else{
				map.put("userName", ubi.getUserName());
				String bankName = ubi.getBankName();
				String bankNum = ubi.getBankCardNum();
				String bank = bankName + "(尾号"
						+ ((bankNum.length() > 4) ? bankNum.substring(bankNum.length() - 4) : "****") + ")";
				map.put("bank", bank);
			}
			resp = LTResponseCode.getCode(LTResponseCode.SUCCESS, map);
		}catch(LTException e){
			resp = LTResponseCode.getCode(e.getMessage());
			logger.info("提现申请失败，e={}",e);
		}
		return resp;

	}
	
}