package com.lt.adapter.adapter.order.func;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.lt.adapter.adapter.BaseFunction;
import com.lt.api.trade.IOrderApiService;
import com.lt.util.error.LTResponseCode;
import com.lt.util.utils.StringTools;
import com.lt.util.utils.model.Response;
/**
 * 查询委托记录加成交记录
 * @author jingwb
 *
 */
@Service
public class FindEntrustAndSuccRecordFunc extends BaseFunction{
	@Autowired
    private IOrderApiService orderApiService;
	
	@Override
	public Response response(Map<String, Object> paraMap) {
        String orderId = StringTools.formatStr(paraMap.get("orderId"), "");
        List<Map<String,Object>>  data = orderApiService.findEntrustAndSuccRecord(orderId);
        return LTResponseCode.getCode(LTResponseCode.SUCCESS, data);
	}
}
