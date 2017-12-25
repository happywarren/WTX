package com.lt.trade.settle.service.impl.func;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSONObject;
import com.lt.enums.settle.CaluateTypeEnum;
import com.lt.model.settle.SettleInnerBean;
import com.lt.model.settle.SettleTmpBean;
import com.lt.trade.settle.service.SettleMentTypeFunc;

/**   
* 项目名称：lt-trade   
* 类名称：TradeFeeSettleFunc   
* 类描述：交易手续费计算逻辑   
* 创建人：yuanxin   
* 创建时间：2017年3月16日 下午1:49:19      
*/
@Component
public class TradeFeeSettleFunc extends BaseCommonSettle implements SettleMentTypeFunc {

	private Logger logger = LoggerFactory.getLogger(getClass());
	
	@Override
	public void settle(Double amt, List<SettleInnerBean> innerBean) {
		
		if(CollectionUtils.isNotEmpty(innerBean)){
			List<SettleTmpBean> beanList = new ArrayList<SettleTmpBean>();
			
			for(SettleInnerBean settleInnerBean : innerBean){
				double actualAmt = 0.0;
				
				if(CaluateTypeEnum.ADD.getType() == settleInnerBean.getCounterType()){
					actualAmt = settleInnerBean.getScale();
				}else if(CaluateTypeEnum.MULTI.getType() == settleInnerBean.getCounterType()){
					actualAmt = amt * settleInnerBean.getScale();
				}
				
				SettleTmpBean settleTmpBean = new SettleTmpBean(settleInnerBean.getUserId(), actualAmt, settleInnerBean.getRemark(), settleInnerBean.getExternId(), 0,1);
				
				beanList.add(settleTmpBean);
			}
			
			logger.info("是执行这里报错么？：{}",JSONObject.toJSONString(beanList));
			
			super.insertSettle(beanList);
		}
		
	}
	
	

}
