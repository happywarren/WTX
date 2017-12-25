package com.lt.manager.controller.user;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONObject;
import com.lt.manager.bean.user.ProductAccountMapper;
import com.lt.manager.service.product.IProductManageService;
import com.lt.manager.service.user.IInvestorAccountManageService;
import com.lt.manager.service.user.IProductAccountMapperService;
import com.lt.model.product.Product;
import com.lt.util.LoggerTools;
import com.lt.util.error.LTResponseCode;
import com.lt.util.utils.model.Response;



/**
 *
 * <br>
 * <b></b>ProductAccountMapperController<br>
 *   <br>
 */
@Controller
@RequestMapping(value="user")
public class ProductAccountMapperController {

	private Logger logger = LoggerTools.getInstance(getClass());

	@Autowired
	private IProductAccountMapperService iProductAccountMapperService;

	@Autowired
	private IProductManageService productManageServiceImpl;


	@Autowired
	private IInvestorAccountManageService iInvestorManageService;

	/**
	 *
	 * @param page
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/user/productAccountMapperlist")
	@ResponseBody
	public String  productAccountMapperlist(HttpServletRequest request,ProductAccountMapper param) throws Exception{
	    Response response = null;
		logger.info("查询产品券商账户关联信息param={}",param);
		try {
			List list = new ArrayList();
			Product p = new Product();
			p.setPlate(param.getPlateType());
			List<Product> plist = productManageServiceImpl.getProShortCode(p);
			List<ProductAccountMapper> pmlist = iProductAccountMapperService.queryProductAccountMapperPage(param);
			Integer isUse = -1;
			Integer direct = -1;
			Product pvo = new Product();
			ProductAccountMapper pamvo = new ProductAccountMapper();

			for(Iterator iter = plist.iterator(); iter.hasNext();){
				direct = 0;
				isUse = 0;
				pvo = (Product)iter.next();

				if(pmlist.size() != 0){
					for(Iterator iter2 = pmlist.iterator(); iter2.hasNext();){
						pamvo = (ProductAccountMapper)iter2.next();
						if(pamvo.getProductId().equals(pvo.getId())){
							isUse = 1;
							direct = pamvo.getDirect();
							break;
						}
					}
				}
				pamvo = new ProductAccountMapper();
				pamvo.setIsUse(isUse);
				pamvo.setProductId(pvo.getId());
				pamvo.setPlateType(pvo.getPlate());
				pamvo.setDirect(direct);
				pamvo.setProductType(pvo.getProductTypeId());
				pamvo.setProductName(pvo.getProductName()+"+"+pvo.getShortCode());
				list.add(pamvo);
			}

			response = LTResponseCode.getCode(LTResponseCode.SUCCESS, list);
		} catch (Exception e) {
			e.printStackTrace();
			response = LTResponseCode.getCode(e.getMessage());
			logger.info("查询产品券商账户关联信息异常，e={}",e);

		}
		return response.toJsonString();
	}
	/**
	 *
	 * @param page
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/user/productlist")
	@ResponseBody
	public String  productlist(HttpServletRequest request) throws Exception{
	    Response response = null;
		try {
			List list = new ArrayList();
			List<Product> plist = productManageServiceImpl.getProShortCode(new Product());
			Product pvo = new Product();
			ProductAccountMapper pamvo = new ProductAccountMapper();
			for(Iterator iter = plist.iterator(); iter.hasNext();){
				pamvo = new ProductAccountMapper();
				pvo = (Product)iter.next();
				pamvo.setPlateType(pvo.getPlate());
				pamvo.setProductId(pvo.getId());
				pamvo.setProductName(pvo.getProductName()+"+"+pvo.getShortCode());
				list.add(pamvo);
			}
			response = LTResponseCode.getCode(LTResponseCode.SUCCESS, list);
		} catch (Exception e) {
			e.printStackTrace();
			response = LTResponseCode.getCode(e.getMessage());
			logger.info("查询产品券商账户关联信息异常，e={}",e);

		}
		return response.toJsonString();
	}
	/**
	 * @param request
	 * @param param
	 * @return
	 */
	@RequestMapping(value="/user/addProductAccountMapper")
	@ResponseBody
	public String addProductAccountMapper(HttpServletRequest request,ProductAccountMapper param){

		Response response = null;
		logger.info("修改产品券商账户关联信息param={}",param);
		try {
			iProductAccountMapperService.addProductAccountMapper(param);
			response = LTResponseCode.getCode(LTResponseCode.SUCCESS);
		} catch (Exception e) {
			e.printStackTrace();
			response = LTResponseCode.getCode(e.getMessage());
			logger.info("修改产品券商账户关联信息异常，e={}",e);
		}
		return response.toJsonString();
	}


		/**
	 * @param request
	 * @param param
	 * @return
	 */
	@RequestMapping(value="/user/editProductAccountMapper")
	@ResponseBody
	public String editProductAccountMapper(HttpServletRequest request,ProductAccountMapper param){

		Response response = null;
		logger.info("修改产品券商账户关联信息param={}",JSONObject.toJSONString(param));
		try {
			String data=request.getParameter("productObj");
			logger.info("修改产品券商账户关联信息productObj={}",data);
			List<ProductAccountMapper> productList = JSONObject.parseArray(data, ProductAccountMapper.class);
			ProductAccountMapper bamparamvo = new ProductAccountMapper();
			ProductAccountMapper pamDBVO;
			for (Iterator iter = productList.iterator(); iter.hasNext();){
				bamparamvo = (ProductAccountMapper)iter.next();
				pamDBVO = iProductAccountMapperService.getProductAccountMapperBySecodeProId(bamparamvo.getProductId(),bamparamvo.getSecurityCode(),bamparamvo.getUserId());
				if(bamparamvo.getIsUse() == 0){//判断该产品对象关联中是否存在，=0表示该证券账号不关联该产品
					if(pamDBVO != null){//如果查出来该产品说明该账号关联和该产品现在需要从数据库中删除
						iProductAccountMapperService.removeProductAccountMapper(pamDBVO.getId());
					}
				}
				if(bamparamvo.getIsUse() == 1){//说明该用户关联看该产品
					if(pamDBVO != null){//说明之前没有这个产品需要添加
						iProductAccountMapperService.editProductAccountMapper(bamparamvo);//有这个产品只做update操作
					}else{
						iProductAccountMapperService.addProductAccountMapper(bamparamvo);//没有这个产品做插入操作
					}
				}
			}
			response = LTResponseCode.getCode(LTResponseCode.SUCCESS);
		} catch (Exception e) {
			e.printStackTrace();
			response = LTResponseCode.getCode(e.getMessage());
			logger.info("修改产品券商账户关联信息异常，e={}",e);
		}
		return response.toJsonString();
	}

	/**
	 * 用户删除
	 * @param request
	 * @param param
	 * @return
	 */
	@RequestMapping(value="/user/removeProductAccountMapper")
	@ResponseBody
	public String removeProductAccountMapper(HttpServletRequest request,String param){

		Response response = null;
		logger.info("删除产品券商账户关联信息param={}",param);

		 String[] productAccountMapperIDs = param.split(",");
		try {
			   for (String id : productAccountMapperIDs) {
				   iProductAccountMapperService.removeProductAccountMapper(id);
		        }
			response = LTResponseCode.getCode(LTResponseCode.SUCCESS);
		} catch (Exception e) {
			e.printStackTrace();
			response = LTResponseCode.getCode(e.getMessage());
			logger.info("修改产品券商账户关联信息异常，e={}",e);
		}
		return response.toJsonString();
	}

		/**
	 * 用户删除
	 * @param request
	 * @param param
	 * @return
	 */
	@RequestMapping(value="/user/getProductAccountMapper")
	@ResponseBody
	public String getProductAccountMapper(Integer id){

		Response response = null;
		logger.info("获取产品券商账户关联信息param={}",id);

		try {
				ProductAccountMapper productAccountMapper =  iProductAccountMapperService.getProductAccountMapper(id);
				response = LTResponseCode.getCode(LTResponseCode.SUCCESS,  productAccountMapper);
		} catch (Exception e) {
			e.printStackTrace();
			response = LTResponseCode.getCode(e.getMessage());
			logger.info("获取产品券商账户关联信息异常，e={}",e);
		}
		return response.toJsonString();
	}


}
