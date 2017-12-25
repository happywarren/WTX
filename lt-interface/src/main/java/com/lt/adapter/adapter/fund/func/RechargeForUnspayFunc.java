package com.lt.adapter.adapter.fund.func;

import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;
import com.lt.adapter.adapter.BaseFunction;
import com.lt.api.business.product.IProductApiService;
import com.lt.api.fund.IFundAccountApiService;
import com.lt.api.user.IUserApiBussinessService;
import com.lt.enums.fund.FundThirdOptCodeEnum;
import com.lt.enums.sys.CurrencyEnum;
import com.lt.model.user.UserBankInfo;
import com.lt.model.user.UserBussinessInfo;
import com.lt.util.error.LTException;
import com.lt.util.error.LTResponseCode;
import com.lt.util.utils.StringTools;
import com.lt.util.utils.model.Response;
import com.lt.vo.user.BankcardVo;

@Service
public class RechargeForUnspayFunc extends BaseFunction {
	
	Logger logger = LoggerFactory.getLogger(getClass());
	
	@Autowired
	private IFundAccountApiService fundAccountApiService;
	
	@Autowired
	private IUserApiBussinessService userApiBussinessService;
	
	@Autowired
	private IProductApiService productApiServiceImpl;

	@Override
	public Response response(Map<String, Object> paraMap) {

		String amount = StringTools.formatStr(paraMap.get("amount"),"");
		String rmbAmt = StringTools.formatStr(paraMap.get("rmbAmt"),"");
		String responseUrl = StringTools.formatStr(paraMap.get("responseUrl"),"");
		String userId = StringTools.formatStr(paraMap.get("userId"), "");
		String bankCardId = StringTools.formatStr(paraMap.get("bankCardId"), "0");
		String bankCode = StringTools.formatStr(paraMap.get("bankCode"), "0");
		Response  resp = null;
		try{
			logger.info("银生宝充值接口请求参数，amount={}",amount);
			if(StringTools.isEmpty(amount) || StringTools.isEmpty(responseUrl) || !StringTools.isNumeric(bankCardId)){
				throw new LTException(LTResponseCode.FU00003);
			}
			
			
			UserBankInfo bankInfo = new UserBankInfo();
			bankInfo.setUserId(userId);
			UserBussinessInfo ubi = userApiBussinessService.getUserBankList(bankInfo);
			if(ubi == null){
				logger.info("银生宝充值,获取用户银行卡信息为null，userid={}",userId);		
				throw new LTException(LTResponseCode.FU00001);
			}
			
			/** 临时处理银行卡列表 beg*/
			List<BankcardVo> bankList =  userApiBussinessService.getBankcardVo(userId);
			logger.info("查询到用户的银行卡列表为：{}",JSONObject.toJSONString(bankList));
			if(CollectionUtils.isNotEmpty(bankList)){
				for(BankcardVo bankCard : bankList){
					if(bankCard.getId().intValue() == Integer.parseInt(bankCardId)){
						ubi.setBankCardNum(bankCard.getNum());
					}
				}
			}else{
				throw new LTException(LTResponseCode.FU00001);
			}
			
			/** 临时处理银行卡列表 beg*/
			
			Double rate = productApiServiceImpl.getRate(CurrencyEnum.DEFAULT_CURRENCY.getCurrencyCode());
			Map<String,String> map = fundAccountApiService.rechargeForUnspay(ubi, amount,rmbAmt, responseUrl,bankCode,FundThirdOptCodeEnum.YSBCZ.getThirdLevelCode(),rate);
			resp = LTResponseCode.getCode(LTResponseCode.SUCCESS, map);
		}catch(Exception e){		
			resp = LTResponseCode.getCode(e.getMessage());
			logger.info("银生宝充值失败，e={}",e);
		}
		return resp;

	}
}
