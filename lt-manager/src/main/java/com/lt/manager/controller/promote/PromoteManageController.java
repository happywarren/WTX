package com.lt.manager.controller.promote;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.Page;
import com.lt.api.promote.IPromoteApiService;
import com.lt.manager.bean.promote.PromoteParamVo;
import com.lt.manager.service.product.IProductManageService;
import com.lt.manager.service.promote.IPromoteManageService;
import com.lt.model.product.Product;
import com.lt.model.promote.PromoterFeeConfig;
import com.lt.model.promote.PromoterLevel;
import com.lt.util.LoggerTools;
import com.lt.util.error.LTResponseCode;
import com.lt.util.utils.JqueryEasyUIData;
import com.lt.util.utils.StringTools;
import com.lt.util.utils.jms.MessageQueueProducer;
import com.lt.util.utils.model.Response;
/**
 * 推广系统控制器
 * @author jingwb
 *
 */
@Controller
@RequestMapping(value="promote")
public class PromoteManageController {
	private Logger logger = LoggerTools.getInstance(getClass());
	
	@Autowired
	private IPromoteManageService promoteManageService;
	@Autowired
	private IProductManageService productManageService;
	@Autowired
	private IPromoteApiService promoteApiService;
	@Autowired
	private MessageQueueProducer promoteProducer;
	
	/**
	 * 新建级别
	 * @param request
	 * @param level
	 * @param shortCodes
	 * @return
	 */
	@RequestMapping(value="/addPromoterLevel")
	@ResponseBody
	public String addPromoterLevel(HttpServletRequest request,String level){
		Response response = null;
		try{
			if(StringTools.isEmpty(level)){
				return LTResponseCode.getCode(LTResponseCode.PROMJ0002).toJsonString();
			}
			response = promoteManageService.addPromoterLevel(level);
		}catch(Exception e){
			logger.info("新建级别异常，e={}",e);
			response = LTResponseCode.getCode(e.getMessage());
		}
		return response.toJsonString();
	}
	
	/**
	 * 新增品种
	 * @param request
	 * @param level
	 * @param firstCommision
	 * @param secondCommision
	 * @return
	 */
	@RequestMapping(value="/addShortCodes")
	@ResponseBody
	public String addShortCodes(HttpServletRequest request,String level,String promoterFeeConfig){
		Response response = null;
		try{
			if(StringTools.isEmpty(level) || StringTools.isEmpty(promoterFeeConfig) ){
				return LTResponseCode.getCode(LTResponseCode.PROMJ0002).toJsonString();
			}			
			
			promoteManageService.addShortCodes(level, promoterFeeConfig);
			response = LTResponseCode.getCode(LTResponseCode.SUCCESS);
		}catch(Exception e){
			logger.info("新增品种异常，e={}",e);
			response = LTResponseCode.getCode(e.getMessage());
		}
		return response.toJsonString();
	}
	
	
	/**
	 * 获取所有品种
	 * @param request
	 * level 如果level为空表示查询所有品种，如果level不为空，表示查询该等级下没有的品种
	 * @return
	 */
	@RequestMapping(value="/getShortCodes")
	@ResponseBody
	public String getShortCodes(HttpServletRequest request,Product product,String level){
		Response response = null;
		try{
			List<Product> list = null;
			if(StringTools.isEmpty(level)){
				list = productManageService.getProShortCode(product);
			}else{
				list = promoteManageService.getProShortCode(level);
			}
			
			response = LTResponseCode.getCode(LTResponseCode.SUCCESS,list);
		}catch(Exception e){
			logger.info("获取所有品种异常，e={}",e);
			response = LTResponseCode.getCode(e.getMessage());
		}
		return response.toJsonString();
	}
	
	/**
	 * 修改推广配置信息
	 * @param request
	 * @param promoterFeeConfig
	 * @return
	 */
	@RequestMapping(value="/modifyPromoterConfig")
	@ResponseBody
	public String modifyPromoterConfig(HttpServletRequest request,PromoterFeeConfig promoterFeeConfig){
		Response response = null;
		try{
			if(promoterFeeConfig.getId() == null){
				return LTResponseCode.getCode(LTResponseCode.PROMJ0002).toJsonString();
			}
			promoteManageService.modifyPromoterConfig(promoterFeeConfig);
			response = LTResponseCode.getCode(LTResponseCode.SUCCESS);
		}catch(Exception e){
			logger.info("修改推广配置信息异常，e={}",e);
			response = LTResponseCode.getCode(e.getMessage());
		}
		return response.toJsonString();
	}
	
	/**
	 * 删除推广等级
	 * @param request
	 * @param id
	 * @return
	 */
	@RequestMapping(value="/removePromoterLevel")
	@ResponseBody
	public String removePromoterLevel(HttpServletRequest request,String levelId){
		Response response = null;
		try{
			if(StringTools.isEmpty(levelId)){
				return LTResponseCode.getCode(LTResponseCode.PROMJ0002).toJsonString();
			}
			response = promoteManageService.removePromoterLevel(levelId);
			
		}catch(Exception e){
			logger.info("删除推广等级异常，e={}",e);
			response = LTResponseCode.getCode(e.getMessage());
		}
		return response.toJsonString();
	}
	
	/**
	 * 删除推广配置
	 * @param request
	 * @param promoterFeeConfig
	 * @return
	 */
	@RequestMapping(value="/removePromoterFeeConfig")
	@ResponseBody
	public String removePromoterFeeConfig(HttpServletRequest request,PromoterFeeConfig promoterFeeConfig){
		Response response = null;
		try{
			if(StringTools.isEmpty(promoterFeeConfig.getId())){
				return LTResponseCode.getCode(LTResponseCode.PROMJ0002).toJsonString();
			}
			promoteManageService.removePromoterFeeConfig(promoterFeeConfig);
			
			return  LTResponseCode.getCode(LTResponseCode.SUCCESS).toJsonString();
		}catch(Exception e){
			logger.info("删除推广配置异常，e={}",e);
			response = LTResponseCode.getCode(e.getMessage());
		}
		return response.toJsonString();
	}
	
	/**
	 * 查询推广等级
	 * @param request
	 * @return
	 */
	@RequestMapping(value="/getPromoterLevel")
	@ResponseBody
	public String getPromoterLevel(HttpServletRequest request){
		Response response = null;
		try{
			List<PromoterLevel> list = promoteManageService.getPromoterLevel();
			response = LTResponseCode.getCode(LTResponseCode.SUCCESS,list);
		}catch(Exception e){
			logger.info(" 查询推广等级异常，e={}",e);
			response = LTResponseCode.getCode(e.getMessage());
		}
		return response.toJsonString();
	}
	
	/**
	 * 查询推广配置信息
	 * @param request
	 * @param levelId
	 * @return
	 */
	@RequestMapping(value="/getPromoterFeeConfig")
	@ResponseBody
	public String getPromoterFeeConfig(HttpServletRequest request,PromoteParamVo param){
		Response response = null;
		try{
			if(param.getLevelId() == null){
				return LTResponseCode.getCode(LTResponseCode.PROMJ0002).toJsonString();
			}
			Page<PromoterFeeConfig>  page = promoteManageService.queryPromoterFeeConfigPage(param);
			return JqueryEasyUIData.init(page);
		}catch(Exception e){
			logger.info("查询推广配置信息异常，e={}",e);
			response = LTResponseCode.getCode(e.getMessage());
		}
		return response.toJsonString();
	}
	
	/**
	 * 查询推广员列表
	 * @param request
	 * @param param
	 * @return
	 */
	@RequestMapping(value="/queryPromoterPage")
	@ResponseBody
	public String queryPromoterPage(HttpServletRequest request,PromoteParamVo param){
		Response response = null;
		try{
			Page<PromoteParamVo> page = promoteManageService.queryPromoterPage(param);
			return JqueryEasyUIData.init(page);
		}catch(Exception e){
			logger.info("查询推广员列表异常，e={}",e);
			response = LTResponseCode.getCode(e.getMessage());
		}
		return response.toJsonString();
	}
	
	/**
	 * 查询推广员数据列表
	 * @param request
	 * @param param
	 * @return
	 */
	@RequestMapping(value="/queryPromoterDataPage")
	@ResponseBody
	public String queryPromoterDataPage(HttpServletRequest request,PromoteParamVo param){
		Response response = null;
		try{
			Page<PromoteParamVo> page = promoteManageService.queryPromoterDataPage(param);
			return JqueryEasyUIData.init(page);
		}catch(Exception e){
			logger.info("查询推广员数据列表异常，e={}",e);
			response = LTResponseCode.getCode(e.getMessage());
		}
		return response.toJsonString();
	}
	
	/**
	 * 查询下线列表--分页
	 * @param request
	 * @param param
	 * @return
	 */
	@RequestMapping("/queryBrancherPage")
	@ResponseBody
	public String queryBrancherPage(HttpServletRequest request,PromoteParamVo param){
		Response response = null;
		try{
			if(param.getUserId() == null){
				return LTResponseCode.getCode(LTResponseCode.PROMJ0002).toJsonString();
			}
			
			Page<PromoteParamVo> page = promoteManageService.queryBrancherPage(param);
			return JqueryEasyUIData.init(page);
		}catch(Exception e){
			logger.info("查询下线列表异常，e={}",e);
			response = LTResponseCode.getCode(e.getMessage());
		}
		return response.toJsonString();
	}
	
	/**
	 * 成为推广员
	 * @param request
	 * @return
	 */
	@RequestMapping(value="/becomePromoter")
	@ResponseBody
	public String becomePromoter(HttpServletRequest request,String userId){
		Response response = null;
		try{
			if(StringTools.isEmpty(userId)){
				return LTResponseCode.getCode(LTResponseCode.PROMJ0002).toJsonString();
			}
			promoteApiService.activatePromoter(userId);
			response = LTResponseCode.getCode(LTResponseCode.SUCCESS);
		}catch(Exception e){
			logger.info("成为推广员异常，e={}",e);
			response = LTResponseCode.getCode(e.getMessage());
		}
		return response.toJsonString();
	}
	
	/**
	 * 取消成为推广员
	 * @param request
	 * @param userId
	 * @return
	 */
	@RequestMapping(value="/cancelPromoter")
	@ResponseBody
	public String cancelPromoter(HttpServletRequest request,String userId){
		Response response = null;
		try{
			response = promoteManageService.cancelPromoter(userId);
		}catch(Exception e){
			logger.info("取消成为推广员异常，e={}",e);
			response = LTResponseCode.getCode(e.getMessage());
		}
		return response.toJsonString();
	}
	
	/**
	 * 修改推广员等级
	 * @param request
	 * @param userId
	 * @return
	 */
	@RequestMapping(value="/modifyPromoterLevel")
	@ResponseBody
	public String modifyPromoterLevel(HttpServletRequest request,String userId,String level){
		Response response = null;
		try{
			if(StringTools.isEmpty(userId) || StringTools.isEmpty(level)){
				return LTResponseCode.getCode(LTResponseCode.PROMJ0002).toJsonString();
			}
			response = promoteManageService.setPromoterLevel(userId, level);
		}catch(Exception e){
			logger.info("修改推广员等级异常，e={}",e);
			response = LTResponseCode.getCode(e.getMessage());
		}
		return response.toJsonString();
	}
	
	/**
	 * 修改所属于关系
	 * @param request
	 * @param userId
	 * @param proomoter
	 * @return
	 */
	@RequestMapping("/modifyPromoterMapper")
	@ResponseBody
	public String modifyPromoterMapper(HttpServletRequest request,String userId,String ssy){
		Response response = null;
		try{
			if(StringTools.isEmpty(userId)){
				return LTResponseCode.getCode(LTResponseCode.PROMJ0002).toJsonString();
			}
			response = promoteManageService.modifyPromoterMapper(userId, ssy);
			if((Boolean)response.getData()){//新增的下线
				//用于统计推广数据
				JSONObject json = new JSONObject();
				json.put("userId", userId);
				json.put("register",true);
				logger.info("==============通知推广统计====================");
				promoteProducer.sendMessage(json);
			}			
		}catch(Exception e){
			logger.info("修改所属于关系异常，e={}",e);
			response = LTResponseCode.getCode(e.getMessage());
		}
		return response.toJsonString();
	}
	
	/**
	 * 判断当前用户是否为推广员
	 * @param request
	 * @param userId
	 * @return
	 */
	@RequestMapping(value="/isPromoter")
	@ResponseBody
	public String isPromoter(HttpServletRequest request,String userId){
		Response response = null;
		try{
			if(StringTools.isEmpty(userId)){
				return LTResponseCode.getCode(LTResponseCode.PROMJ0002).toJsonString();
			}
			response = promoteApiService.isPromoter(userId);
		}catch(Exception e){
			logger.info("判断当前用户是否为推广员异常，e={}",e);
			response = LTResponseCode.getCode(e.getMessage());
		}
		return response.toJsonString();
	}
	
	/**
	 * 查询推广员信息
	 * @param request
	 * @param userId
	 * @return
	 */
	@RequestMapping(value="/queryPromoterInfo")
	@ResponseBody
	public String queryPromoterInfo(HttpServletRequest request,String userId){
		Response response = null;
		try{
			response = promoteManageService.queryPromoterInfo(userId);
		}catch(Exception e){
			logger.info("查询推广员信息异常，e={}",e);
			response = LTResponseCode.getCode(e.getMessage());
		}
		return response.toJsonString();
	}
	
	/**
	 * 恢复上级关系
	 * @param request
	 * @param userId 为空表示删除上级关系
	 * @return
	 */
	@RequestMapping(value="/renewPromoterMapper")
	@ResponseBody
	public String renewPromoterMapper(HttpServletRequest request,String userId){
		Response response = null;
		try{
			response = promoteManageService.renewPromoterMapper(userId);
		}catch(Exception e){
			logger.info("恢复上级关系异常，e={}",e);
			response = LTResponseCode.getCode(e.getMessage());
		}
		return response.toJsonString();
	}
	
	/**
	 * 查询是否有新的品种
	 * @param request
	 * @param level
	 * @return
	 */
	@RequestMapping(value="/isHaveNewCode")
	@ResponseBody
	public String isHaveNewCode(HttpServletRequest request,String level){
		Response response = null;
		try{
			if(StringTools.isEmpty(level)){
				return LTResponseCode.getCode(LTResponseCode.PROMJ0002).toJsonString();
			}
			Integer count = promoteManageService.getProShortCodeCount(level);
			response = LTResponseCode.getCode(LTResponseCode.SUCCESS,count);
		}catch(Exception e){
			logger.info("查询是否有新的品种异常，e={}",e);
			response = LTResponseCode.getCode(e.getMessage());
		}
		return response.toJsonString();
	}
}
