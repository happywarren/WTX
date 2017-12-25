package com.lt.manager.controller.product;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.lt.manager.bean.product.ProductParamVO;
import com.lt.manager.service.product.IProductTagManageService;
import com.lt.model.product.ProductTagInfo;
import com.lt.util.LoggerTools;
import com.lt.util.error.LTResponseCode;
import com.lt.util.utils.StringTools;
import com.lt.util.utils.model.Response;

/**
 * 商品标签管理控制器
 * @author jingwb
 *
 */
@Controller
@RequestMapping(value="/product")
public class ProductTagManageController {
	private Logger logger = LoggerTools.getInstance(getClass());
	
	@Autowired
	private IProductTagManageService productTagManageServiceImpl;
	
	/**
	 * 添加商品标签
	 * @param request
	 * @param param
	 * @return
	 */
	@RequestMapping("/tag/addProductTag")
	@ResponseBody
	public String addProductTag(HttpServletRequest request,ProductParamVO param){
		Response response = null;
		try {
			if(StringTools.isEmpty(param.getName())){
				return LTResponseCode.getCode(LTResponseCode.PR00001).toJsonString();
			}
			String id = productTagManageServiceImpl.addProductTag(param);
			response = LTResponseCode.getCode(LTResponseCode.SUCCESS,id);
		} catch (Exception e) {
			logger.info("添加商品标签异常，e={}",e);
			response = LTResponseCode.getCode(e.getMessage());
		}
		return response.toJsonString();
	}
	
	/**
	 * 编辑商品标签
	 * @param request
	 * @param param
	 * @return
	 */
	@RequestMapping("/tag/editProductTag")
	@ResponseBody
	public String editProductTag(HttpServletRequest request,ProductParamVO param){
		Response response = null;
		try {
			if(param.getId() == null){
				return LTResponseCode.getCode(LTResponseCode.PR00001).toJsonString();
			}
			if(param.getId().equals("13")){
				return LTResponseCode.getCode(LTResponseCode.USL2006).toJsonString();
			}
			productTagManageServiceImpl.editProductTag(param);
			response = LTResponseCode.getCode(LTResponseCode.SUCCESS);
		} catch (Exception e) {
			logger.info("编辑商品标签异常，e={}",e);
			response = LTResponseCode.getCode(e.getMessage());
		}
		return response.toJsonString();
	}
	
	
	/**
	 * 删除商品标签
	 * @param request
	 * @param param
	 * @return
	 */
	@RequestMapping("/tag/removeProductTag")
	@ResponseBody
	public String removeProductTag(HttpServletRequest request,ProductParamVO param){
		Response response = null;
		try {
			if(StringTools.isEmpty(param.getIds())){
				return LTResponseCode.getCode(LTResponseCode.PR00001).toJsonString();
			}
			if(param.getIds().contains(",")){
				if(param.getIds().contains("13,")||param.getIds().endsWith(",13")){
					response = LTResponseCode.getCode(LTResponseCode.USL2006);
					return response.toJsonString();
				}
			}else{
				if(param.getIds().contains("13")){
					response = LTResponseCode.getCode(LTResponseCode.USL2006);
					return response.toJsonString();
				}
			}
			productTagManageServiceImpl.removeProductTag(param);
			response = LTResponseCode.getCode(LTResponseCode.SUCCESS);
		} catch (Exception e) {
			logger.info("删除商品标签异常，e={}",e);
			response = LTResponseCode.getCode(e.getMessage());
		}
		return response.toJsonString();
	}
	
	public static void main(String[] args) {
		String str = "13";
		if(str.contains(",")){
			if(str.contains("13,")||str.endsWith(",13")){
				System.out.println(str);
			}
		}else{
			if(str.contains("13")){
				System.out.println("----"+str);
			}
		}
	}
	/**
	 * 查询商品标签集
	 * @param request
	 * @param param
	 * @return
	 */
	@RequestMapping("/tag/queryProductTagList")
	@ResponseBody
	public String queryProductTagList(HttpServletRequest request){
		Response response = null;
		try {
			List<ProductTagInfo> list = productTagManageServiceImpl.queryProductTagList();
			response = LTResponseCode.getCode(LTResponseCode.SUCCESS,list);
		} catch (Exception e) {
			logger.info("查询商品标签列表异常，e={}",e);
			response = LTResponseCode.getCode(e.getMessage());
		}
		return response.toJsonString();
	}
}
