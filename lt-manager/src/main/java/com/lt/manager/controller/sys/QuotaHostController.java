package com.lt.manager.controller.sys;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.Page;
import com.lt.manager.bean.sys.AuthInfoVo;
import com.lt.manager.bean.sys.Menus;
import com.lt.manager.bean.sys.Staff;
import com.lt.manager.service.sys.IAuthService;
import com.lt.model.sys.QuotaHostBean;
import com.lt.util.error.LTException;
import com.lt.util.error.LTResponseCode;
import com.lt.util.utils.HttpTools;
import com.lt.util.utils.JqueryEasyUIData;
import com.lt.util.utils.StringTools;
import com.lt.util.utils.model.Response;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.BoundHashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Created by guodawang on 2017/8/4.
 */
@Controller
@RequestMapping(value = "/host")
public class QuotaHostController {

    @Autowired
    private RedisTemplate redisTemplate;

    @RequestMapping(value = "/findList")
    @ResponseBody
    public String findHostList(HttpServletRequest request) {
        Response response = null;
        try {
            List<QuotaHostBean> list = new ArrayList<QuotaHostBean>();
            BoundHashOperations<String,String, QuotaHostBean> redis = redisTemplate.boundHashOps("REDIS_QUOTA_HOST");
            Map<String,QuotaHostBean> map = redis.entries();
            Iterator<Map.Entry<String, QuotaHostBean>> entries = map.entrySet().iterator();
            int i = 1;
            while (entries.hasNext())
            {
                Map.Entry<String, QuotaHostBean> entry = entries.next();
                QuotaHostBean bean = entry.getValue();
                bean.setId(i);
                list.add(bean);
                i++;
            }
            Page<QuotaHostBean> pg = new Page<QuotaHostBean>();
            pg.setPageNum(1);
            pg.setPageSize(20);
            pg.addAll(list);
            pg.setTotal(list.size());
            return JqueryEasyUIData.init(pg);
        } catch (Exception e) {
            response = LTResponseCode.getCode(e.getMessage());
        }
        return response.toJsonString();
    }


    @RequestMapping(value = "/delete")
    @ResponseBody
    public String delete(HttpServletRequest request) {
        String host = request.getParameter("host");
        Integer port = Integer.valueOf(request.getParameter("port"));
        Response response = null;
        try {
            BoundHashOperations<String,String, QuotaHostBean> redis = redisTemplate.boundHashOps("REDIS_QUOTA_HOST");
            String url = redis.get(host+port).getUrl();
            redis.delete(host+port);
            HttpTools.doGet(url,null);
            response = LTResponseCode.getCode(LTResponseCode.SUCCESS);
        } catch (Exception e) {
            response = LTResponseCode.getCode(e.getMessage());
        }
        return response.toJsonString();
    }

    @RequestMapping(value = "/add")
    @ResponseBody
    public String add(HttpServletRequest request) {
        String host = request.getParameter("host");
        Integer port = Integer.valueOf(request.getParameter("port"));
        String url = request.getParameter("url");
        Response response = null;
        try {
            QuotaHostBean bean = new QuotaHostBean();
            bean.setHost(host);
            bean.setPort(port);
            bean.setUrl(url);
            BoundHashOperations<String,String, QuotaHostBean> redis = redisTemplate.boundHashOps("REDIS_QUOTA_HOST");
            redis.put(host+port,bean);
            response = LTResponseCode.getCode(LTResponseCode.SUCCESS);
        } catch (Exception e) {
            response = LTResponseCode.getCode(e.getMessage());
        }
        return response.toJsonString();
    }
}
