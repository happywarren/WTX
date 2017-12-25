package com.lt.adapter.adapter.user.func;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.lt.adapter.adapter.BaseFunction;
import com.lt.api.user.IInvestorFeeCfgApiService;
import com.lt.model.user.InvestorFeeCfg;
import com.lt.util.error.LTResponseCode;
import com.lt.util.utils.StringTools;
import com.lt.util.utils.model.Response;

/**   
* 此类已废除
*/
@Deprecated
@Component
public class GetInvestorFeeCfgFunc extends BaseFunction {
	
	@Autowired
	private IInvestorFeeCfgApiService userFeeCfgApiService;
	
	@Override
	public Response response(Map<String, Object> paraMap) {
		String productIdPara = StringTools.formatStr(paraMap.get("productId"), "-1");
		String userIdPara = StringTools.formatStr(paraMap.get("userId"), "-1");
		String plateTypePara = StringTools.formatStr(paraMap.get("plateType"), "-1");
		String supportDirectPara = StringTools.formatStr(paraMap.get("supportDirect"), "-1");
		
		Integer productId = Integer.parseInt(productIdPara);
		String userId = userIdPara;
		Integer plateType = Integer.parseInt(plateTypePara);
		Integer supportDirect= Integer.parseInt(supportDirectPara);
		
		if(productId == null){
			return LTResponseCode.getCode(LTResponseCode.PR00004);
		}
		InvestorFeeCfg investorFeeCfgVo = userFeeCfgApiService.getInvestorFeeCfg(userId,productId,plateType,supportDirect);
		return LTResponseCode.getCode(LTResponseCode.SUCCESS, investorFeeCfgVo);
	}

}
