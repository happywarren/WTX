package com.lt.manager.controller.sys;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.lt.manager.service.sys.IRulesOfTransactionsService;
import com.lt.model.sys.RulesOfTransactions;
import com.lt.util.LoggerTools;
import com.lt.util.error.LTException;
import com.lt.util.error.LTResponseCode;
import com.lt.util.utils.StringTools;

/**
 * 交易规则
 * @author guodw
 *
 */
@Controller
@RequestMapping(value = "/rules")
public class RulesOfTransactionsController {
	Logger logger = LoggerTools.getInstance(this.getClass());
	
	@Autowired
	private IRulesOfTransactionsService rulesOfTransactionsService;  
	//增删改查
	@ResponseBody
	@RequestMapping(value = "/save")
	public String save(RulesOfTransactions rulesOfTransactions, HttpServletRequest request) {
		try {
			rulesOfTransactionsService.saveRulesOfTransactions(rulesOfTransactions);
			return LTResponseCode.getCode(LTResponseCode.SUCCESS).toJsonString();
		} catch (Exception e) {
			e.printStackTrace();
			return LTResponseCode.getCode(e.getMessage()).toJsonString();
		}
	}

	@ResponseBody
	@RequestMapping(value = "/remove")
	public String remove(String id, HttpServletRequest request) {
		try {
			if (!StringTools.isNumeric(id)) {
				logger.error("id 为空或者不是数字");
				throw new LTException(LTResponseCode.MA00004);
			}
			int i = rulesOfTransactionsService.removeById(Integer.valueOf(id));
			if (i == 0) {
				throw new LTException(LTResponseCode.MA00018);
			}
			return LTResponseCode.getCode(LTResponseCode.SUCCESS).toJsonString();
		} catch (Exception e) {
			e.printStackTrace();
			return LTResponseCode.getCode(e.getMessage()).toJsonString();
		}
	}

	@ResponseBody
	@RequestMapping(value = "/edit")
	public String editRole(RulesOfTransactions rulesOfTransactions) {
		try {
			rulesOfTransactionsService.editRulesOfTransactions(rulesOfTransactions);
			return LTResponseCode.getCode(LTResponseCode.SUCCESS).toJsonString();
		} catch (Exception e) {
			e.printStackTrace();
			return LTResponseCode.getCode(e.getMessage()).toJsonString();
		}

	}

	@ResponseBody
	@RequestMapping(value = "/select")
	public String select(RulesOfTransactions rulesOfTransactions) {
		try {
			List<RulesOfTransactions> list = rulesOfTransactionsService.selectRulesOfTransactions(rulesOfTransactions);
			return LTResponseCode.getCode(LTResponseCode.SUCCESS,list).toJsonString();
		} catch (Exception e) {
			e.printStackTrace();
			return LTResponseCode.getCode(e.getMessage()).toJsonString();
		}
	}

}
