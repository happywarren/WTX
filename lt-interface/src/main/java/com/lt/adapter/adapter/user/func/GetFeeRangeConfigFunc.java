package com.lt.adapter.adapter.user.func;

import java.text.ParseException;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSONObject;
import com.lt.adapter.adapter.BaseFunction;
import com.lt.api.business.product.IProductApiService;
import com.lt.api.business.product.IProductRiskConfigService;
import com.lt.api.user.IInvestorFeeCfgApiService;
import com.lt.api.user.IInvestorProductConfigApiService;
import com.lt.api.user.IUserApiService;
import com.lt.constant.LTConstant;
import com.lt.model.product.ProductRiskConfig;
import com.lt.model.user.InvestorFeeCfg;
import com.lt.model.user.InvestorProductConfig;
import com.lt.model.user.UserBaseInfo;
import com.lt.util.error.LTException;
import com.lt.util.error.LTResponseCode;
import com.lt.util.utils.DateTools;
import com.lt.util.utils.StringTools;
import com.lt.util.utils.model.Response;
import com.lt.vo.product.ProductVo;

/**   
* 项目名称：lt-interface   
* 类名称：GetFeeRangeConfigFunc   
* 类描述：   获取用户费用配置相关数据
* 创建人：yuanxin   
* 创建时间：2017年2月28日 下午5:40:21      
*/
@Component
public class GetFeeRangeConfigFunc extends BaseFunction {

	Logger logger = LoggerFactory.getLogger(getClass());
	
	@Autowired
	private IInvestorFeeCfgApiService userFeeCfgApiService;
	
	@Autowired
	private IProductApiService productApiServiceImpl;
	
	@Autowired
	private IUserApiService userApiService;
	
	@Autowired
	private IInvestorProductConfigApiService investorProductConfigApiService;
	@Autowired
	private IProductRiskConfigService productRiskConfigService;
	
	
	@Override
	public Response response(Map<String, Object> paraMap) {
		
		
		Integer productId = Integer.parseInt(StringTools.formatStr(paraMap.get("productId"), "-1"));
		
		String productCode = StringTools.formatStr(paraMap.get("productCode"), "");
		String userId = StringTools.formatStr(paraMap.get("userId"), "");
		//String investorId = String.valueOf(paraMap.get("investorId"));
		
		//商品ID
		if(productId == null || productId <= 0){
			return LTResponseCode.getCode(LTResponseCode.PR00004);
		}

		
		Response response = null;

		String investorId = LTConstant.DEFAULT_INVESTOR_ID+"";
		//手续费替换
		UserBaseInfo info = userApiService.findUserByUserId(userId);
		if(info != null && StringTools.isNotEmpty(info.getInvestorAccountId())){
			//设置券商ID
			investorId = info.getInvestorAccountId();

		}

		InvestorFeeCfg investorFeeCfgVo = userFeeCfgApiService.getInvestorFeeCfgByProId(productId,investorId);
		logger.info("=======用户信息:{}=======",JSONObject.toJSONString(info));
		info.setRiskLevel(info.getRiskLevel()==null?"4":info.getRiskLevel());//默认高风险
		//获取商品区间配置
		List<ProductRiskConfig> productRiskConfigList = productRiskConfigService.queryProductRiskConfig(productId,Integer.valueOf( info.getRiskLevel()));
		if(productRiskConfigList != null && productRiskConfigList.size() > 0){
			ProductRiskConfig productRiskConfig = productRiskConfigList.get(0);
			investorFeeCfgVo.setStopLossRange(productRiskConfig.getStopLossRange()==null?investorFeeCfgVo.getStopLossRange():productRiskConfig.getStopLossRange());
			investorFeeCfgVo.setStopProfitRange(productRiskConfig.getStopProfitRange()==null?investorFeeCfgVo.getStopProfitRange():productRiskConfig.getStopProfitRange());
			investorFeeCfgVo.setSurcharge(productRiskConfig.getSurcharge()==null?investorFeeCfgVo.getSurcharge():productRiskConfig.getSurcharge());
			investorFeeCfgVo.setDeferFee(productRiskConfig.getDeferFee()==null?investorFeeCfgVo.getDeferFee():productRiskConfig.getDeferFee());
			investorFeeCfgVo.setDeferFund(productRiskConfig.getDeferFund()==null?investorFeeCfgVo.getDeferFund():productRiskConfig.getDeferFund());
			investorFeeCfgVo.setIsSupportDefer(productRiskConfig.getIsDefer()==null?investorFeeCfgVo.getIsSupportDefer():productRiskConfig.getIsDefer());
			investorFeeCfgVo.setDefaultCount(productRiskConfig.getDefaultCount()==null?investorFeeCfgVo.getDefaultCount():productRiskConfig.getDefaultCount());
			investorFeeCfgVo.setDefaultStopLoss(productRiskConfig.getDefaultStopLoss()==null?investorFeeCfgVo.getDefaultStopLoss():productRiskConfig.getDefaultStopLoss());
			investorFeeCfgVo.setDefaultStopProfit(productRiskConfig.getDefaultStopProfit()==null?investorFeeCfgVo.getDefaultStopProfit():productRiskConfig.getDefaultStopProfit());
			investorFeeCfgVo.setMultipleRange(productRiskConfig.getMultipleRange()==null?investorFeeCfgVo.getMultipleRange():productRiskConfig.getMultipleRange());
			
		}

		// 是否支持递延 1 支持  -1 即将到达交割日 0 品种不支持递延
		if(investorFeeCfgVo != null && investorFeeCfgVo.getIsSupportDefer() == 1){
			ProductVo product = productApiServiceImpl.getProductInfo(productCode);
			logger.info("=======product={}========",JSONObject.toJSONString(product));
			if(product != null ){
				Date expiration = null ;
				try {
					expiration = DateTools.toDefaultDateTime(product.getExpirationTime());
					if(DateTools.compareTo(new Date(),DateTools.add(expiration,-3))){
						investorFeeCfgVo.setIsSupportDefer(-1);
					}
				} catch (ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					throw new LTException(LTResponseCode.US00004);
				}
			}else{
				investorFeeCfgVo.setIsSupportDefer(-1);
			}
		}

		//查询用户是否存在所属券商
		InvestorProductConfig config =  investorProductConfigApiService.findInvestorProductConfig(investorId, productCode);
		if(StringTools.isNotEmpty(config)&&StringTools.isNotEmpty(config.getCounterFee())){
			investorFeeCfgVo.setInvestorCounterfee(Double.valueOf(config.getCounterFee()));
		}

		logger.info("========investorFeeCfgVo={}=========",JSONObject.toJSONString(investorFeeCfgVo));
		response = new Response(LTResponseCode.SUCCESS, "查询成功", investorFeeCfgVo);
		return response;
	}

	public static void main(String[] args) {
		Date expiration=null;
		try {
			expiration = DateTools.toDefaultDateTime("2017-05-15 05:00:00");
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println(DateTools.compareTo(new Date(),DateTools.add(expiration,-3)));
	}
}
