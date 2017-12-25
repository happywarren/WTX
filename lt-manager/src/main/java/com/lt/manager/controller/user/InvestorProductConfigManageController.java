package com.lt.manager.controller.user;

import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.Page;
import com.lt.manager.bean.product.ProductParamVO;
import com.lt.manager.bean.sys.Staff;
import com.lt.manager.bean.user.InvestorFeeCfg;
import com.lt.manager.bean.user.InvestorProductConfig;
import com.lt.manager.service.product.IProductManageService;
import com.lt.manager.service.product.IProductTradeConfigService;
import com.lt.manager.service.user.IInvestorFeeCfgManageService;
import com.lt.manager.service.user.IInvestorProductConfigManageService;
import com.lt.model.product.Product;
import com.lt.model.product.ProductTradeConfig;
import com.lt.util.LoggerTools;
import com.lt.util.StaffUtil;
import com.lt.util.annotation.LimitLess;
import com.lt.util.error.LTException;
import com.lt.util.error.LTResponseCode;
import com.lt.util.utils.JqueryEasyUIData;
import com.lt.util.utils.StringTools;
import com.lt.util.utils.model.Response;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

/**
 * 券商商品控制器
 */
@Controller
@RequestMapping(value = "user")
public class InvestorProductConfigManageController {

    private Logger logger = LoggerTools.getInstance(getClass());

    @Autowired
    private IInvestorProductConfigManageService investorProductConfigManageServiceImpl;


    @RequestMapping(value = "/user/addInvestorProduct")
    @ResponseBody
    public String addInvestorProduct(HttpServletRequest request) {
        Response response = null;
        try {
            String productObj = request.getParameter("productObj");
            if (StringTools.isEmpty(productObj)) {
                return LTResponseCode.getCode(LTResponseCode.PR00001).toJsonString();
            }
            List<InvestorProductConfig> investorProductConfigList = JSONObject.parseArray(productObj, InvestorProductConfig.class);
            String creater = "system";
            Staff staff = StaffUtil.getStaff(request);
            if (StringTools.isNotEmpty(staff)) {
                creater = staff.getName();
            }
            investorProductConfigManageServiceImpl.batchAddInvestorProductConfig(creater, investorProductConfigList);
            response = LTResponseCode.getCode(LTResponseCode.SUCCESS);
        } catch (Exception e) {
            e.printStackTrace();
            response = LTResponseCode.getCode(e.getMessage());
            logger.info("添加券商商品手续费配置信息异常，e={}", e);
        }
        return response.toJsonString();
    }

    @RequestMapping(value = "/user/editInvestorProduct")
    @ResponseBody
    public String editInvestorProduct(HttpServletRequest request, InvestorProductConfig investorProductConfig) {
        Response response = null;
        try {
            if (StringTools.isEmpty(investorProductConfig.getId()) || StringTools.isEmpty(investorProductConfig.getCounterFee())){
                return LTResponseCode.getCode(LTResponseCode.PR00001).toJsonString();
            }
            investorProductConfigManageServiceImpl.editInvestorProductConfig(investorProductConfig);
            response = LTResponseCode.getCode(LTResponseCode.SUCCESS);
        } catch (Exception e) {
            e.printStackTrace();
            response = LTResponseCode.getCode(e.getMessage());
            logger.info("编辑券商商品手续费配置信息异常，e={}", e);
        }
        return response.toJsonString();
    }

    @RequestMapping(value = "/user/delInvestorProduct")
    @ResponseBody
    public String delInvestorProduct(HttpServletRequest request, InvestorProductConfig investorProductConfig) {
        Response response = null;
        try {
            if (StringTools.isEmpty(investorProductConfig.getIds())) {
                return LTResponseCode.getCode(LTResponseCode.PR00001).toJsonString();
            }
            investorProductConfigManageServiceImpl.deleteInvestorProductConfig(investorProductConfig.getIds());
            response = LTResponseCode.getCode(LTResponseCode.SUCCESS);
        } catch (Exception e) {
            e.printStackTrace();
            response = LTResponseCode.getCode(e.getMessage());
            logger.info("删除券商商品手续费配置信息异常，e={}", e);
        }
        return response.toJsonString();
    }

    @RequestMapping(value = "/user/noCheckinvestorProductList")
    @ResponseBody
    public String investorNoCheckProductList(HttpServletRequest request, InvestorProductConfig investorProductConfig) {
        Response response = null;
        try {
            if (StringTools.isEmpty(investorProductConfig.getInvestorAccountId())) {
                return LTResponseCode.getCode(LTResponseCode.PR00001).toJsonString();
            }
            List<InvestorProductConfig> investorProductConfigList = investorProductConfigManageServiceImpl.selectNoCheckProductList(investorProductConfig);
            response = LTResponseCode.getCode(LTResponseCode.SUCCESS, investorProductConfigList);
        } catch (Exception e) {
            e.printStackTrace();
            response = LTResponseCode.getCode(e.getMessage());
            logger.info("未配置券商商品交易手续费配置列表信息异常，e={}", e);
        }
        return response.toJsonString();
    }

    @RequestMapping(value = "/user/investorProductList")
    @ResponseBody
    public String investorProductList(HttpServletRequest request, InvestorProductConfig investorProductConfig) {
        Response response = null;
        try {
            if (StringTools.isEmpty(investorProductConfig.getInvestorAccountId())) {
                return LTResponseCode.getCode(LTResponseCode.PR00001).toJsonString();
            }
            Page<InvestorProductConfig> page = investorProductConfigManageServiceImpl.findInvestorProductConfig(investorProductConfig);

            return JqueryEasyUIData.init(page);
        } catch (Exception e) {
            e.printStackTrace();
            response = LTResponseCode.getCode(e.getMessage());
            logger.info("券商商品交易手续费配置列表信息异常，e={}", e);
        }
        return response.toJsonString();
    }
}
