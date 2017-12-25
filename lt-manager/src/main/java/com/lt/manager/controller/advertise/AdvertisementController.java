package com.lt.manager.controller.advertise;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.Page;
import com.lt.manager.bean.advertise.AdvertisementPage;
import com.lt.manager.bean.advertise.AdvertisementVo;
import com.lt.manager.bean.sys.Staff;
import com.lt.manager.service.advertise.AdvertisementService;
import com.lt.model.advertise.Advertisement;
import com.lt.util.error.LTException;
import com.lt.util.error.LTResponseCode;
import com.lt.util.utils.JqueryEasyUIData;
import com.lt.util.utils.model.Response;

/**   
* 项目名称：lt-manager   
* 类名称：AdvertisementController   
* 类描述：   
* 创建人：yuanxin   
* 创建时间：2017年7月4日 上午9:21:28      
*/
@Controller
@RequestMapping("/advertisement")
public class AdvertisementController {
	
	Logger logger = LoggerFactory.getLogger(this.getClass());
	
	@Autowired
	private AdvertisementService advertiseServiceImpl;
	
	/** 查询所有提现记录*/
	@RequestMapping(value="/insertAdvertiseMent")
	@ResponseBody
	public String insertAdvertiseMent(HttpServletRequest request,Advertisement advertisement){
		Response response = null ;
		try{
			Staff staff = JSON.parseObject((String) request.getSession()
					.getAttribute("staff"), Staff.class);
			advertisement.setCreaterUserId(staff.getId().toString());
			advertiseServiceImpl.insertAdvertisement(advertisement);
			response = new Response(LTResponseCode.SUCCESS, "添加成功");
		}catch(LTException e){
			e.printStackTrace();
			response = new Response(e.getMessage(), "执行异常");
		}
		return response.toJsonString();
	}
	
	/** 置顶功能*/
	@RequestMapping(value="/topAdvertiseMent")
	@ResponseBody
	public String topAdvertiseMent(HttpServletRequest request,String adverId,String adverType){
		Response response = null ;
		try{
			Staff staff = JSON.parseObject((String) request.getSession()
					.getAttribute("staff"), Staff.class);
			advertiseServiceImpl.topAdvertiseMent(adverId, adverType,staff.getId().toString());
			response = new Response(LTResponseCode.SUCCESS, "置顶成功");
		}catch(LTException e){
			e.printStackTrace();
			response = new Response(e.getMessage(), "执行异常");
		}
		return response.toJsonString();
	}
	
	/** 转换广告图显示或隐藏*/
	@RequestMapping(value="/changeShowAdvertisement")
	@ResponseBody
	public String changeShowAdvertisement(HttpServletRequest request,String adverId,Integer status){
		Response response = null ;
		try{
			Staff staff = JSON.parseObject((String) request.getSession()
					.getAttribute("staff"), Staff.class);
			advertiseServiceImpl.changeShowStatusAdver(adverId, status,staff.getId().toString());
			response = new Response(LTResponseCode.SUCCESS, "修改成功");
		}catch(LTException e){
			e.printStackTrace();
			response = new Response(e.getMessage(), "执行异常");
		}
		return response.toJsonString();
	}
	
	/** 根据id查询广告图内容*/
	@RequestMapping(value="/qryAdvertiseMentById")
	@ResponseBody
	public String qryAdvertiseMentById(String adverId){
		Response response = null ;
		try{
			Advertisement advertisement = advertiseServiceImpl.getAdvertisementById(adverId);
			response = new Response(LTResponseCode.SUCCESS, "查询成功",advertisement);
		}catch(LTException e){
			e.printStackTrace();
			response = new Response(e.getMessage(), "执行异常");
		}
		return response.toJsonString();
	}
	
	/** 修改广告图内容*/
	@RequestMapping(value="/updateAdvertisement")
	@ResponseBody
	public String updateAdvertisement(HttpServletRequest request,Advertisement advertisement){
		Response response = null ;
		try{
			
			if(advertisement.getAdverId() == null || advertisement.getAdverId().equals("")){
				throw new LTException(LTResponseCode.FU00003);
			}
			
			Staff staff = JSON.parseObject((String) request.getSession()
					.getAttribute("staff"), Staff.class);
			advertisement.setUpdateUserId(staff.getId().toString());
			
			advertiseServiceImpl.updateAdvertisement(advertisement);
			response = new Response(LTResponseCode.SUCCESS, "修改成功");
		}catch(LTException e){
			e.printStackTrace();
			response = new Response(e.getMessage(), "执行异常");
		}
		return response.toJsonString();
	}
	
	/** 根据广告图id删除内容*/
	@RequestMapping(value="/delAdvertisement")
	@ResponseBody
	public String delAdvertisement(String adverId){
		Response response = null ;
		try{
			
			if(adverId == null || adverId.equals("")){
				throw new LTException(LTResponseCode.FU00003);
			}
			
			advertiseServiceImpl.delAdvertisement(adverId);
			response = new Response(LTResponseCode.SUCCESS, "删除成功");
		}catch(LTException e){
			e.printStackTrace();
			response = new Response(e.getMessage(), "执行异常");
		}
		return response.toJsonString();
	}
	
	/**返回广告图的分页数据*/
	@RequestMapping(value="/qryAdvertisementPage")
	@ResponseBody
	public String qryAdvertisementPage(AdvertisementVo advertisementVo){
		Response response = null ;
		try{
			System.out.println("advertisementVo:{}"+JSONObject.toJSONString(advertisementVo));
			Page<AdvertisementPage> page = advertiseServiceImpl.qryAdvertistMentPage(advertisementVo);
			return JqueryEasyUIData.init(page);
		}catch(LTException e){
			e.printStackTrace();
			response = new Response(e.getMessage(), "执行异常");
		}
		return response.toJsonString();
	}
	
	/**
	 * 生成二维码链接
	 * @param adverId
	 * @return    
	 * @return:       String    
	 * @throws 
	 * @author        yuanxin
	 * @Date          2017年7月6日 上午9:22:30
	 */
	@RequestMapping(value="/createAdvertiseQR")
	@ResponseBody
	public String createAdvertiseQR(String adverId) {
		Response response = null ;
		try{
			
			String url = advertiseServiceImpl.createAdvertiseQR(adverId);
			response = new Response(LTResponseCode.SUCCESS, "查询成功",url);
			return response.toJsonString();
		}catch(LTException e){
			e.printStackTrace();
			response = new Response(e.getMessage(), "执行异常");
		}
		return response.toJsonString();
	}
	
}
