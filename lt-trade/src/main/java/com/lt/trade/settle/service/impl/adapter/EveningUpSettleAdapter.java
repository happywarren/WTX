package com.lt.trade.settle.service.impl.adapter;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSONObject;
import com.lt.api.user.IInvestorFeeCfgApiService;
import com.lt.enums.settle.CaluateTypeEnum;
import com.lt.enums.settle.SettleEnum;
import com.lt.model.settle.SettleBean;
import com.lt.model.settle.SettleInnerBean;
import com.lt.model.settle.SettleTypeInfo;
import com.lt.model.trade.OrderCashInfo;
import com.lt.model.user.InvestorFeeCfg;
import com.lt.trade.settle.service.SettleMentAdapter;
import com.lt.trade.settle.service.SettleResolverUtil;
import com.lt.util.utils.StringTools;

/**   
* 项目名称：lt-trade   
* 类名称：EveningUpSettleAdapter   
* 类描述：   清仓结算处理类
* 创建人：yuanxin   
* 创建时间：2017年3月19日 下午10:55:42      
*/
@Component
public class EveningUpSettleAdapter implements SettleMentAdapter {

	@Autowired
	private IInvestorFeeCfgApiService investorFeeCfgApiService;
	
	private Logger logger = LoggerFactory.getLogger(getClass());
	
	@Override
	public void excute(SettleTypeInfo settleTypeInfo, Object object) {
		
		SettleResolverUtil resolverUtil = new SettleResolverUtil();
		
		logger.info("查询结果为：{}",JSONObject.toJSONString(settleTypeInfo));
		String settleStr = settleTypeInfo.getSettleList();
		
		OrderCashInfo cashOrdersInfo = (OrderCashInfo)object;
		
		if(StringTools.isNotEmpty(settleStr)){
			for(String settle : settleStr.split(",")){
				if(StringTools.isNotEmpty(settle)){
					String func = "";
					String remark = "";
					Double amt = 0.0;
					Integer counterfee_type = null ;
					List<SettleInnerBean> innerBeanList = new ArrayList<SettleInnerBean>();
					List<SettleBean> beanList = new ArrayList<SettleBean>();
					
					logger.info("获取费用配置信息");
					InvestorFeeCfg investorFeeCfg = investorFeeCfgApiService.getInvestorFeeCfg(cashOrdersInfo.getInvestorId(), cashOrdersInfo.getProductId(),cashOrdersInfo.getPlate(),cashOrdersInfo.getTradeDirection());
					logger.info("获取费用配置信息，结果为：{}",JSONObject.toJSONString(investorFeeCfg));
					
					if(SettleEnum.FEE_SETTLE_TYPE.getFuncCode() == Integer.parseInt(settle)){
						func = SettleEnum.FEE_SETTLE_TYPE.getFuncValue();
						remark = SettleEnum.FEE_SETTLE_TYPE.getFuncName();
						amt = cashOrdersInfo.getActualCounterFee();
						counterfee_type = CaluateTypeEnum.ADD.getType();//investorFeeCfg.getCounterFeeType();
						SettleInnerBean settleInnerBean = new SettleInnerBean(cashOrdersInfo.getInvestorId(), investorFeeCfg.getInvestorCounterfee(), cashOrdersInfo.getOrderId(),counterfee_type,remark);
						innerBeanList.add(settleInnerBean);
						
						SettleInnerBean settleInnerBean2 = new SettleInnerBean("9999", investorFeeCfg.getPlatformCounterfee(), cashOrdersInfo.getOrderId(),counterfee_type,remark);
						innerBeanList.add(settleInnerBean2);
					}
					
					SettleBean bean = new SettleBean(func, amt, innerBeanList);
					
					if(!bean.getFunc().equals("")){
						beanList.add(bean);
					}
					
					logger.info("beanList的组装结果为：{}",JSONObject.toJSONString(beanList));
					resolverUtil.resolver(beanList);
				}
			}
		}
	}

}
