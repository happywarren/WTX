package com.lt.adapter.adapter.order.func;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.lt.api.trade.IOrderScoreApiService;
import com.lt.enums.fund.FundTypeEnum;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;
import com.lt.adapter.adapter.BaseFunction;
import com.lt.api.trade.IOrderApiService;
import com.lt.util.error.LTResponseCode;
import com.lt.util.utils.StringTools;
import com.lt.util.utils.model.Response;
import com.lt.vo.trade.OrderBalanceVo;
/**
 * 查询用户结算记录
 * @author jingwb
 *
 */
@Service
public class FindBalanceRecordFunc extends BaseFunction {
	Logger logger = LoggerFactory.getLogger(getClass());
	@Autowired
    private IOrderApiService orderApiService;

    @Autowired
    private IOrderScoreApiService orderScoreApiService;
	
	@Override
	public Response response(Map<String, Object> paraMap) {
		int page = StringTools.formatInt(paraMap.get("page"), 1);
        int rows = StringTools.formatInt(paraMap.get("rows"), 10);
        int fundType = StringTools.formatInt(paraMap.get("fundType"), 0);
        String userId = StringTools.formatStr(paraMap.get("userId"), "");

        //超过10页不显示
        if(page > 10){
            return LTResponseCode.getCode(LTResponseCode.SUCCESS, null);
        }

        Map<String, Object> map = new HashMap<String, Object>();
        map.put("limit1", (page - 1) * rows);
        map.put("limit2", rows);
        map.put("userId", userId);
        map.put("fundType", fundType);
        List<OrderBalanceVo>  data = null;
        if (fundType == FundTypeEnum.SCORE.getValue()) {
            data = orderScoreApiService.findBalanceRecord(map);
        }else{
            data = orderApiService.findBalanceRecord(map);
        }

        logger.info("===========data={}=============",JSONObject.toJSONString(data));
        return LTResponseCode.getCode(LTResponseCode.SUCCESS, data);
	}
}
