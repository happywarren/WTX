package com.lt.adapter.adapter.user.func;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.lt.adapter.adapter.BaseFunction;
import com.lt.api.user.IUserApiBussinessService;
import com.lt.util.error.LTResponseCode;
import com.lt.util.utils.model.Response;

/**
 * 获取出金银行卡
 * @author jingwb
 *
 */

@Service
public class GetWithdrawBankInfoFunc extends BaseFunction{
	@Autowired
	private IUserApiBussinessService userApiBussinessService;
	
	@Override
	public Response response(Map<String, Object> paraMap) {
		String userId = String.valueOf(paraMap.get("userId"));
		Map<String, Object> map = userApiBussinessService.getWithdrawBankInfo(userId);
		return LTResponseCode.getCode(LTResponseCode.SUCCESS,map);
	}
}
