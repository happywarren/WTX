package com.lt.user.charge.service.impl;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.lt.api.fund.IFundAccountApiService;
import com.lt.enums.fund.FundThirdOptCodeEnum;
import com.lt.user.charge.UserRechargeSuper;
import com.lt.user.charge.bean.BaseChargeBean;
import com.lt.user.charge.bean.RechargeByZFBM;
import com.lt.user.charge.service.UserChargeFunc;
import com.lt.util.error.LTException;
import com.lt.util.error.LTResponseCode;

/**   
* 项目名称：lt-user   
* 类名称：UserCharge1010101Func   
* 类描述：支付宝充值   
* 创建人：yuanxin   
* 创建时间：2017年6月29日 下午5:43:37      
*/
@Component
public class UserCharge1010101Func extends UserRechargeSuper implements UserChargeFunc {
	
	@Override
	public String encapsulatePacket(BaseChargeBean baseCharge) throws LTException {
		return "";
	}

	@Override
	public Map<String, Object> returnParam(String urlCode,String packet,BaseChargeBean baseCharge) throws LTException {
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("code", LTResponseCode.SUCCESS);
		map.put("msg", "支付宝充值成功");
		return map;
	}

	@Override
	public String requestUrl(String packet,BaseChargeBean baseCharge) throws LTException {
		return "";
	}

	@Override
	public boolean insertDataBase(BaseChargeBean baseCharge) throws LTException {
		try{
			RechargeByZFBM zfbCharge = (RechargeByZFBM) baseCharge.getObject();
			super.getFundAccountServiceImpl().insertAlipayRechargeIo(baseCharge.getUserId(), baseCharge.getPayOrderId(), baseCharge.getAmt(),
					baseCharge.getRmbAmt(), baseCharge.getBankCardNum(), zfbCharge.getZfbNum(), baseCharge.getBankCode(),
					FundThirdOptCodeEnum.ZFBCZ.getThirdLevelCode(), baseCharge.getRate());
			return true ;
		}catch(LTException e){
			throw new LTException(e.getMessage());
		}
	}

	@Override
	public Map<String, Object> excute(Map<String, Object> map, IFundAccountApiService fundAccountServiceImpl)
			throws LTException {
		super.setChargeFunc(this);
		super.setFundAccountServiceImpl(fundAccountServiceImpl);
		return super.excute(map);
	}

}
