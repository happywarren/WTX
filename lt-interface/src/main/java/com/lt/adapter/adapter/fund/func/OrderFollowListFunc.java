package com.lt.adapter.adapter.fund.func;

import com.lt.adapter.adapter.BaseFunction;
import com.lt.api.fund.IFundAccountApiService;
import com.lt.util.error.LTResponseCode;
import com.lt.util.utils.DateTools;
import com.lt.util.utils.StringTools;
import com.lt.util.utils.model.Response;
import com.lt.vo.fund.FundFlowVo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * 订单资金流水列表
 */
@Service
public class OrderFollowListFunc extends BaseFunction {

    Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private IFundAccountApiService fundAccountApiService;

    @Override
    public Response response(Map<String, Object> paraMap) {
        int fundType = StringTools.formatInt(paraMap.get("fundType"), 0);
        String orderId = StringTools.formatStr(paraMap.get("orderId"), "");
        String userId = StringTools.formatStr(paraMap.get("userId"), "");
        //查询近3个月的明细
        Calendar createDate = Calendar.getInstance();
        createDate.setTime(DateTools.getDefaultDate());
        createDate.add(Calendar.MONTH, -1);
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("userId", userId);
        map.put("orderId", orderId);
        List<FundFlowVo> list = fundAccountApiService.findFundFollowByOrder(map, fundType);
        return LTResponseCode.getCode(LTResponseCode.SUCCESS, list);


    }

}
