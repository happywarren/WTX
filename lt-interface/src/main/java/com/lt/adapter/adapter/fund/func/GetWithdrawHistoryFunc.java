package com.lt.adapter.adapter.fund.func;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.lt.adapter.adapter.BaseFunction;
import com.lt.api.fund.IFundAccountApiService;
import com.lt.util.error.LTResponseCode;
import com.lt.util.utils.StringTools;
import com.lt.util.utils.model.Response;

/**
 * 获取出金记录
 * @author jingwb
 *
 */
@Service
public class GetWithdrawHistoryFunc  extends BaseFunction{

	Logger logger = LoggerFactory.getLogger(getClass());
	
	@Autowired
	private IFundAccountApiService fundAccountApiService;
	
	@Override
	public Response response(Map<String, Object> paraMap) {
		int page = StringTools.formatInt(paraMap.get("page"), 1);
        int rows = StringTools.formatInt(paraMap.get("rows"), 10);
        String userId = StringTools.formatStr(paraMap.get("userId"), "");
        
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("limit1", (page - 1) * rows);
        map.put("limit2", rows);
        map.put("userId", userId);
        List<Map<String, Object>> list =  fundAccountApiService.getWithdrawHistory(map);
        return LTResponseCode.getCode(LTResponseCode.SUCCESS, list);
	}
}
