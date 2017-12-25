package com.lt.manager.controller.quota;

import com.alibaba.fastjson.JSONObject;
import com.lt.manager.bean.quota.QuotaSourceVo;
import com.lt.manager.service.quota.IQuotaSourceService;
import com.lt.util.LoggerTools;
import com.lt.util.annotation.LimitLess;
import com.lt.util.error.LTResponseCode;
import com.lt.util.utils.HttpTools;
import com.lt.util.utils.JqueryEasyUIData;
import com.lt.util.utils.StringTools;
import com.lt.util.utils.model.Response;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping(value = "quota")
public class QuotaSourceController {

    private Logger logger = LoggerTools.getInstance(getClass());

    @Autowired
    private IQuotaSourceService quotaSourceService;

    @RequestMapping(value = "/listSource")
    @ResponseBody
    public String list(HttpServletRequest request) {
        Response response = null;
        try {
            String type = request.getParameter("type");
            if (StringTools.isEmpty(type)) {
                return LTResponseCode.getCode(LTResponseCode.PR00001).toJsonString();
            }
            List<QuotaSourceVo> quotaSourceVoList = quotaSourceService.listQuotaSource(type);
            Map<String, Object> dataMap = new HashMap<String, Object>();
            dataMap.put("rows", quotaSourceVoList);
            dataMap.put("total", quotaSourceVoList.size());
            response = LTResponseCode.getCode(LTResponseCode.SUCCESS, dataMap);
        } catch (Exception e) {
            e.printStackTrace();
            response = LTResponseCode.getCode(e.getMessage());
            logger.info("query行情服务信息异常，e={}", e);
        }
        return response.toJsonString();
    }

    @RequestMapping(value = "/addSource")
    @ResponseBody
    public String addSource(HttpServletRequest request, QuotaSourceVo quotaSourceVo) {
        Response response = null;
        try {
            String type = quotaSourceVo.getType();
            String name = quotaSourceVo.getName();
            String host = quotaSourceVo.getHost();
            Integer port = quotaSourceVo.getPort();
            String reqUrl = quotaSourceVo.getReqUrl();
            Integer status = quotaSourceVo.getStatus();
            logger.info("addSource 参数: {} {} {} {} {} {} ", type, name, host, port, reqUrl, status);
            if (StringTools.isEmpty(type)) {
                return LTResponseCode.getCode(LTResponseCode.PR00001).toJsonString();
            }
            if (StringTools.isEmpty(name)) {
                return LTResponseCode.getCode(LTResponseCode.PR00001).toJsonString();
            }
            if (StringTools.isEmpty(host)) {
                return LTResponseCode.getCode(LTResponseCode.PR00001).toJsonString();
            }
            if (StringTools.isEmpty(reqUrl)) {
                return LTResponseCode.getCode(LTResponseCode.PR00001).toJsonString();
            }
            if (status.intValue() == 1) {
                start(quotaSourceVo);
            }
            quotaSourceService.insertQuotaSource(quotaSourceVo);
            response = LTResponseCode.getCode(LTResponseCode.SUCCESS);
        } catch (Exception e) {
            e.printStackTrace();
            response = LTResponseCode.getCode(e.getMessage());
            logger.info("添加行情服务信息异常，e={}", e);
        }
        return response.toJsonString();
    }

    @RequestMapping(value = "/startSource")
    @ResponseBody
    public String startSource(HttpServletRequest request) {
        Response response = null;
        try {
            Long id = Long.parseLong(request.getParameter("id"));
            QuotaSourceVo quotaSourceVo = quotaSourceService.getQuotaSource(id);
            if (!StringTools.isNotEmpty(quotaSourceVo)) {
                response = LTResponseCode.getCode(LTResponseCode.FU00003);
            } else {
                if (quotaSourceVo.getStatus() == 0) {
                    start(quotaSourceVo);
                }
                quotaSourceService.startQuotaSource(quotaSourceVo.getId());
                response = LTResponseCode.getCode(LTResponseCode.SUCCESS);
            }
        } catch (Exception e) {
            e.printStackTrace();
            response = LTResponseCode.getCode(e.getMessage());
            logger.info("启动行情服务异常，e={}", e);
        }
        return response.toJsonString();
    }

    private void start(QuotaSourceVo quotaSourceVo) {
        try {
            String reqUrl = quotaSourceVo.getReqUrl();
            for (String subUrl : reqUrl.split(";")) {
                String url = subUrl + "/startup";
                Map<String, String> dataMap = new HashMap<String, String>();
                dataMap.put("ip", quotaSourceVo.getHost());
                dataMap.put("port", quotaSourceVo.getPort() + "");
                JSONObject jsonObject = HttpTools.doGet(url, dataMap);
                logger.info("启动行情服务: {} ", jsonObject.toJSONString());
            }
        } catch (Exception e) {
        }
    }

    @LimitLess
    @RequestMapping(value = "/endSource")
    @ResponseBody
    public String endSource(HttpServletRequest request) {
        Response response = null;
        try {
            Long id = Long.parseLong(request.getParameter("id"));
            QuotaSourceVo quotaSourceVo = quotaSourceService.getQuotaSource(id);
            if (!StringTools.isNotEmpty(quotaSourceVo)) {
                response = LTResponseCode.getCode(LTResponseCode.FU00003);
            } else {
                end(quotaSourceVo);
                quotaSourceService.endQuotaSource(id);
                response = LTResponseCode.getCode(LTResponseCode.SUCCESS);
            }
        } catch (Exception e) {
            e.printStackTrace();
            response = LTResponseCode.getCode(e.getMessage());
            logger.info("停止行情服务异常，e={}", e);
        }
        return response.toJsonString();
    }

    private void end(QuotaSourceVo quotaSourceVo) {
        try {
            String reqUrl = quotaSourceVo.getReqUrl();
            for (String subUrl : reqUrl.split(";")) {
                String url = subUrl + "/shutdown";
                Map<String, String> dataMap = new HashMap<String, String>();
                dataMap.put("ip", quotaSourceVo.getHost());
                dataMap.put("port", quotaSourceVo.getPort() + "");
                JSONObject jsonObject = HttpTools.doGet(url, dataMap);
                logger.info("停止行情服务: {} ", jsonObject.toJSONString());
            }
        } catch (Exception e) {
        }
    }

    @RequestMapping(value = "/deleteSource")
    @ResponseBody
    public String deleteSource(HttpServletRequest request) {
        Response response = null;
        try {
            String ids = request.getParameter("id");
            for (String id : ids.split(",")) {
                Long valueId = StringTools.formatLong(id);
                QuotaSourceVo quotaSourceVo = quotaSourceService.getQuotaSource(valueId);
                if (!StringTools.isNotEmpty(quotaSourceVo)) {
                    response = LTResponseCode.getCode(LTResponseCode.FU00003);
                    return response.toJsonString();
                }
                if (quotaSourceVo.getStatus().intValue() == 0) {
                    end(quotaSourceVo);
                }
                quotaSourceService.deleteQuotaSource(valueId);
            }
            response = LTResponseCode.getCode(LTResponseCode.SUCCESS);
        } catch (Exception e) {
            e.printStackTrace();
            response = LTResponseCode.getCode(e.getMessage());
            logger.info("删除行情服务异常，e={}", e);
        }
        return response.toJsonString();
    }
}
