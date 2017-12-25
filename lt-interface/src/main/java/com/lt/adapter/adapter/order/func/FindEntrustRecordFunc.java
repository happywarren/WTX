package com.lt.adapter.adapter.order.func;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.lt.adapter.adapter.BaseFunction;
import com.lt.api.trade.IOrderApiService;
import com.lt.util.error.LTResponseCode;
import com.lt.util.utils.StringTools;
import com.lt.util.utils.model.Response;
import com.lt.vo.trade.OrderEntrustVo;

/**
 * 作者: 邓玉明
 * 时间: 2017/2/24 下午2:43
 * email:cndym@163.com
 */
@Service
public class FindEntrustRecordFunc extends BaseFunction {
	@Autowired
    private IOrderApiService orderApiService;

    @Override
    public Response response(Map<String, Object> paraMap) {

        int page = StringTools.formatInt(paraMap.get("page"), 1);
        int rows = StringTools.formatInt(paraMap.get("rows"), 10);
        int fundType = StringTools.formatInt(paraMap.get("fundType"), 0);
        String userId = StringTools.formatStr(paraMap.get("userId"), "");

        Map<String, Object> map = new HashMap<String, Object>();
        map.put("limit1", (page - 1) * rows);
        map.put("limit2", rows);
        map.put("userId", userId);
        map.put("fundType", fundType);
        List<OrderEntrustVo> data = orderApiService.findEntrustRecord(map);
        return LTResponseCode.getCode(LTResponseCode.SUCCESS, data);

    }
}
