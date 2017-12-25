package com.lt.manager.controller.user;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.github.pagehelper.Page;
import com.lt.manager.bean.user.InvestorFeeCfg;
import com.lt.manager.dao.product.ProductTradeConfigManageDao;
import com.lt.manager.service.product.IProductManageService;
import com.lt.manager.service.product.IProductTradeConfigService;
import com.lt.manager.service.user.IInvestorFeeCfgManageService;
import com.lt.model.product.Product;
import com.lt.model.product.ProductTradeConfig;
import com.lt.util.LoggerTools;
import com.lt.util.error.LTException;
import com.lt.util.error.LTResponseCode;
import com.lt.util.utils.JqueryEasyUIData;
import com.lt.util.utils.StringTools;
import com.lt.util.utils.model.Response;

/**
 * 商品类型控制器
 * @author licy
 *
 */
@Controller
@RequestMapping(value="user")
public class InvestorFeeCfgManageController {
	private Logger logger = LoggerTools.getInstance(getClass());
	
	@Autowired
	private IInvestorFeeCfgManageService iInvestorFeeCfgManageService;
	
	@Autowired
	private IProductManageService productManageServiceImpl;
	
	@Autowired
	private IProductTradeConfigService productTradeConfigService ;
	/**
	 * 添加商品类型
	 * @param request
	 * @param param
	 * @return
	 */
	@RequestMapping(value="/user/addInvestorFeeCfg")
	@ResponseBody
	public String addInvestorFeeCfg(HttpServletRequest request){
		Response response = null;
		try {
			String userId = request.getParameter("userId");
			String productIds = request.getParameter("productIds");
			String arrayId[] = productIds.split(",");
			if(arrayId.length > 0){
				for(int i =0;i<arrayId.length;i++){
					InvestorFeeCfg ifc = new InvestorFeeCfg();
					if(userId != null && arrayId[i] != null){
						ifc = iInvestorFeeCfgManageService.getInvestorFeeCfgByModel(Integer.valueOf(arrayId[i]));
					}
					ifc.setAccountId(userId);
					ifc.setIsModel(2);
					iInvestorFeeCfgManageService.addInvestorFeeCfg(ifc);
				}
			}
			response = LTResponseCode.getCode(LTResponseCode.SUCCESS);
		} catch (Exception e) {
			e.printStackTrace();
			response = LTResponseCode.getCode(e.getMessage());
			logger.info("添加券商费用信息异常，e={}",e);
		}
		return response.toJsonString();
	}
	
	/**
	 * 编辑商品类型
	 * @param request
	 * @param param
	 * @return
	 */
	@RequestMapping(value="/user/editInvestorFeeCfg")
	@ResponseBody
	public String editInvestorFeeCfg(HttpServletRequest request,InvestorFeeCfg ifc){
		Response response = null;
		try {
			//查询product实体信息
			if(StringTools.isNotEmpty(ifc)&&StringTools.isNotEmpty(ifc.getProductId())){
				ProductTradeConfig config = productTradeConfigService.getProductTradeConfigByProductId(ifc.getProductId());
				if(StringTools.isNotEmpty(config)){
					if(ifc.getInvestorCounterfee() != null){
						if(config.getMinCounterFee()>ifc.getInvestorCounterfee() || config.getMaxCounterFee()<ifc.getInvestorCounterfee()){
							throw new LTException(LTResponseCode.PRO0002) ;
						}
					}
					if(ifc.getSurcharge() != null){
						if(config.getMinSurcharge()>ifc.getSurcharge() || config.getMaxSurcharge()<ifc.getSurcharge()){
							throw new LTException(LTResponseCode.PRO0002) ;
						}
					}
					if(ifc.getDeferFee() != null){
						if(config.getMinDeferFee()>ifc.getDeferFee() || config.getMaxDeferFee()<ifc.getDeferFee()){
							throw new LTException(LTResponseCode.PRO0002) ;
						}					
					}
					if(ifc.getDeferFund() != null){
						if(config.getMinDeferFund()>ifc.getDeferFund() || config.getMaxDeferFund()<ifc.getDeferFund()){
							throw new LTException(LTResponseCode.PRO0002) ;
						}
					}
					
					//如果开启递延，判断商品配置递延是否已开启，否则不允许开启
					if(ifc.getIsSupportDefer() != null && ifc.getIsSupportDefer() == 1){//开启
						if(config.getIsDefer() == 1){//关闭
							throw new LTException(LTResponseCode.PRJ0007) ;
						}
					}
					iInvestorFeeCfgManageService.editInvestorFeeCfg(ifc);
					response = LTResponseCode.getCode(LTResponseCode.SUCCESS);
					
				}else{
					throw new LTException(LTResponseCode.PRO0001) ;
				}
			}else{
				response = LTResponseCode.getCode(LTResponseCode.FU00003);
			}
		} catch (Exception e) {
			response = LTResponseCode.getCode(e.getMessage());
			logger.info("编辑券商费用信息异常，e={}",e);
		}
		return response.toJsonString();
	}
	
	/**
	 * 删除商品类型
	 * @param request
	 * @param param
	 * @return
	 */
	@RequestMapping(value="/user/removeInvestorFeeCfg")
	@ResponseBody
	public String removeInvestorFeeCfg(HttpServletRequest request,InvestorFeeCfg param){
		Response response = null;
		try {
			String ids = request.getParameter("ids");
			String arrayId[] = ids.split(",");
			if(arrayId.length > 0){
				for(int i =0;i<arrayId.length;i++){
					iInvestorFeeCfgManageService.removeInvestorFeeCfg(Integer.valueOf(arrayId[i]));
				}
			}
			response = LTResponseCode.getCode(LTResponseCode.SUCCESS);
			
		} catch (Exception e) {
			response = LTResponseCode.getCode(e.getMessage());
			logger.info("删除券商费用信息异常，e={}",e);
		}
		return response.toJsonString();
	}
	
	@RequestMapping(value="/user/findInvestorFeeCfg")
	@ResponseBody
	public String findInvestorFeeCfg(HttpServletRequest request,InvestorFeeCfg param){
		
		Response response = null;
		logger.info("查询券商手续费信息param={}",param);
		try {
			Page<InvestorFeeCfg> page = iInvestorFeeCfgManageService.findInvestorFeeCfg(param);
			return JqueryEasyUIData.init(page);
		} catch (Exception e) {
			e.printStackTrace();
			response = LTResponseCode.getCode(e.getMessage());
			logger.info("查询券商费用信息异常，e={}",e);
		}
		return response.toJsonString();
	}
	
}
