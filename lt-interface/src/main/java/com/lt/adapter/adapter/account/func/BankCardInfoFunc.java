package com.lt.adapter.adapter.account.func;

import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.lt.adapter.adapter.BaseFunction;
import com.lt.api.user.IUserApiBussinessService;
import com.lt.util.error.LTException;
import com.lt.util.error.LTResponseCode;
import com.lt.util.utils.StringTools;
import com.lt.util.utils.model.Response;
import com.lt.vo.user.BankcardVo;

/**
 * 获取用户绑定的银行卡信息
 * @author guodw
 *
 */
@Service
public class BankCardInfoFunc extends BaseFunction {
	
	Logger logger = LoggerFactory.getLogger(getClass());

	@Autowired
	private IUserApiBussinessService userApiBussinessService;
	
	@Override
	public Response response(Map<String, Object> paraMap) {
		try{
			String userId = String.valueOf(paraMap.get("userId"));		
			Response response = LTResponseCode.getCode(LTResponseCode.SUCCESS);
			List<BankcardVo> list = userApiBussinessService.getBankcardVo(userId);
			response.setData(list);
			return response;	
		}catch(Exception e){
			logger.error("获取用户绑定的银行卡信息--执行失败，e={}",e);
			throw new LTException(e.getMessage());
		}
		
	}
}