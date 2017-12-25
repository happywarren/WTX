package com.lt.controller.promote;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.lt.controller.promote.service.IPromoteServiceOld;
import com.lt.util.error.LTResponseCode;
import com.lt.util.utils.model.Response;
/**
 * 推广相关
 * @author jingwb
 *
 */
@Controller
public class PromoteController {

	@Autowired
	private IPromoteServiceOld promoteServiceImplOld;
	
	@RequestMapping(value="/promote")
	@ResponseBody
	public String promote(){
		Response response = null;
		try{
			promoteServiceImplOld.promote();
			response = LTResponseCode.getCode(LTResponseCode.SUCCESS);
		}catch(Exception e){
			response = LTResponseCode.getCode(LTResponseCode.ER400);
			e.printStackTrace();
		}
		return response.toJsonString();
	}
}
