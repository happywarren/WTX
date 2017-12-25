package com.lt.manager.controller.user;

import com.github.pagehelper.Page;
import com.lt.api.business.product.IProductApiService;
import com.lt.manager.bean.product.ProductRiskConfigVO;
import com.lt.manager.service.product.IProductRiskConfigManageService;
import com.lt.util.LoggerTools;
import com.lt.util.error.LTResponseCode;
import com.lt.util.utils.JqueryEasyUIData;
import com.lt.util.utils.StringTools;
import com.lt.util.utils.model.Response;
import com.lt.vo.user.UserProductSelectListVo;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;
@Controller
@RequestMapping(value="/user")
public class UserSelectController {
    
	private Logger logger = LoggerTools.getInstance(getClass());
	@Autowired 
	private IProductApiService productApiService;
	@Autowired
	private IProductRiskConfigManageService productRiskConfigManageService;
	
	@RequestMapping(value="/getProductKinds")
	@ResponseBody
	public String queryUserSelect(Integer productTypeId,String userId){
		try {
			int type = StringTools.formatInt(productTypeId, -1);
			Response response = LTResponseCode.getCode(LTResponseCode.SUCCESS);
			//查询
			List<UserProductSelectListVo> list = productApiService.findAttentionList(userId,type);
			response.setData(list);
			return response.toJsonString();	
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("获取自选股异常",e.fillInStackTrace());
			Response response = LTResponseCode.getCode(e.getMessage());
			return response.toJsonString();	
		}
		
	}

	/**
	 * 用户当前风险等级对应的产品杠杆配置查询
	 * @param param
	 * @return
	 */
	@RequestMapping(value="/getUserRiskProductConfigs")
	@ResponseBody
	public String getUserRiskProductConfigs(ProductRiskConfigVO param){
		try {
			//分页查询
			Page<ProductRiskConfigVO> page = productRiskConfigManageService.queryPage(param);
			return JqueryEasyUIData.init(page);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("获取用户当前风险对应的商品杠杆配置错误",e.fillInStackTrace());
			Response response = LTResponseCode.getCode(e.getMessage());
			return response.toJsonString();
		}

	}
}
