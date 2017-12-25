package com.lt.adapter.adapter.order.func;

import com.lt.adapter.adapter.BaseFunction;
import com.lt.api.trade.IOrderApiService;
import com.lt.api.trade.IOrderScoreApiService;
import com.lt.enums.fund.FundTypeEnum;
import com.lt.util.error.LTResponseCode;
import com.lt.util.utils.model.Response;
import com.lt.vo.trade.PositionOrderVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * 单品持仓列表
 *
 * @author jingwb
 */
@Service
public class FindSinglePositionListFunc extends BaseFunction {
    @Autowired
    private IOrderApiService orderApiService;

    @Autowired
    private IOrderScoreApiService orderScoreApiService;

    @Override
    public Response response(Map<String, Object> paraMap) {
        String productCode = (String) paraMap.get("productCode");
        Integer fundType = Integer.parseInt(paraMap.get("fundType").toString());
        String userId = (String) paraMap.get("userId");//用户id
        List<PositionOrderVo> data = null;
        if (fundType == FundTypeEnum.SCORE.getValue()) {
            data = orderScoreApiService.findPositionOrderByUserAndProduct(userId, productCode, fundType);
        }else if(fundType == FundTypeEnum.CASH.getValue()){
            data = orderApiService.findPositionOrderByUserAndProduct(userId, productCode, fundType);
        }
        return LTResponseCode.getCode(LTResponseCode.SUCCESS, data);
    }
}
