package com.lt.adapter.adapter.user.func;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.lt.adapter.adapter.BaseFunction;
import com.lt.api.user.IUserApiService;
import com.lt.util.error.LTException;
import com.lt.util.error.LTResponseCode;
import com.lt.util.utils.StringTools;
import com.lt.util.utils.model.Response;
import com.lt.vo.user.UserProductSelectListVo;

@Service
public class GetAttentionProductListFunc extends BaseFunction {

	@Autowired
	private IUserApiService userApiService;

	@Override
	public Response response(Map<String, Object> paraMap) {
		Response response = null;
		String userId = String.valueOf(paraMap.get("userId"));

		//用户app-版本号
		String clientVersion = StringTools.formatStr(paraMap.get("clientVersion"), "");

		List<UserProductSelectListVo> list = null;
		try {
			list = userApiService.selectProductOptional(userId,clientVersion);
			response = LTResponseCode.getCode(LTResponseCode.SUCCESS,list);

		} catch (Exception e) {
			e.printStackTrace();
			throw new LTException(e.getMessage());
		}
		return response;
	}
}