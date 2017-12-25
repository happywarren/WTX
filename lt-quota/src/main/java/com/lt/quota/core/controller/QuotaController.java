package com.lt.quota.core.controller;

import com.alibaba.fastjson.JSON;
import com.lt.quota.core.appServer.newer.NewChannelManger;
import com.lt.quota.core.appServer.newer.NewClientOperator;
import com.lt.quota.core.constant.SystemDataCount;
import com.lt.quota.core.quota.coin.SpreadSync;
import com.lt.quota.core.quota.ib.IbClientBox;
import com.lt.quota.core.quota.ib.IbTcpClient;
import com.lt.quota.core.quota.inner.InnerClientBox;
import com.lt.quota.core.quota.inner.InnerTcpClient;
import com.lt.quota.core.quota.rf.RfClientBox;
import com.lt.quota.core.quota.rf.RfTcpClient;
import com.lt.quota.core.utils.FastJsonUtils;
import com.lt.quota.core.utils.Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * 创建：cndym
 * 时间：2017/4/26 16:01
 */

@Controller
@RequestMapping("/quota")
public class QuotaController {

    @Autowired
    private RfTcpClient rfTcpClient;

    @Autowired
    private InnerTcpClient innerTcpClient;

    @Autowired
    private IbTcpClient ibTcpClient;

    @Autowired
    private NewClientOperator newClientOperator;

    @Autowired
    private SpreadSync spreadSync;

    /**
     * 启动RF服务
     *
     * @param ip
     * @param port
     * @return
     */

    @RequestMapping("/rf/startup")
    @ResponseBody
    public String rfStartup(String ip, Integer port) {
        Map<String, String> resMap = new HashMap<String, String>();
        resMap.put("code", "0000");
        resMap.put("msg", "操作成功");
        if (Utils.isEmpty(ip)) {
            resMap.put("code", "9999");
            resMap.put("msg", "请输入服务器ip地址");
            return FastJsonUtils.toJson(resMap);
        }
        if (!Utils.isNotEmpty(port)) {
            resMap.put("code", "9999");
            resMap.put("msg", "请输入服务器port");
            return FastJsonUtils.toJson(resMap);
        }
        rfTcpClient.start(ip, port);
        StringBuilder key = new StringBuilder();
        key.append(ip).append(":").append(port);
        boolean flag = RfClientBox.getInstance().contains(key.toString());
        if (flag) {
            return FastJsonUtils.toJson(resMap);
        }
        resMap.put("code", "9999");
        resMap.put("msg", "启动失败");
        return FastJsonUtils.toJson(resMap);
    }

    /**
     * 关闭RF服务
     *
     * @param ip
     * @param port
     * @return
     */

    @RequestMapping("/rf/shutdown")
    @ResponseBody
    public String rfShutdown(String ip, Integer port) {
        Map<String, String> resMap = new HashMap<String, String>();
        resMap.put("code", "0000");
        resMap.put("msg", "操作成功");
        if (Utils.isEmpty(ip)) {
            resMap.put("code", "9999");
            resMap.put("msg", "请输入服务器ip地址");
            return FastJsonUtils.toJson(resMap);
        }
        if (!Utils.isNotEmpty(port)) {
            resMap.put("code", "9999");
            resMap.put("msg", "请输入服务器port");
            return FastJsonUtils.toJson(resMap);
        }
        rfTcpClient.shutdown(ip, port);
        StringBuilder key = new StringBuilder();
        key.append(ip).append(":").append(port);
        boolean flag = RfClientBox.getInstance().contains(key.toString());
        if (!flag) {
            return FastJsonUtils.toJson(resMap);
        }
        resMap.put("code", "9999");
        resMap.put("msg", "关闭失败");
        return FastJsonUtils.toJson(resMap);
    }

    /**
     * 启动inner服务
     *
     * @param ip
     * @param port
     * @return
     */

    @RequestMapping("/inner/startup")
    @ResponseBody
    public String innerStartup(String ip, Integer port) {
        Map<String, String> resMap = new HashMap<String, String>();
        resMap.put("code", "0000");
        resMap.put("msg", "操作成功");
        if (Utils.isEmpty(ip)) {
            resMap.put("code", "9999");
            resMap.put("msg", "请输入服务器ip地址");
            return FastJsonUtils.toJson(resMap);
        }
        if (!Utils.isNotEmpty(port)) {
            resMap.put("code", "9999");
            resMap.put("msg", "请输入服务器port");
            return FastJsonUtils.toJson(resMap);
        }
        innerTcpClient.start(ip, port);
        StringBuilder key = new StringBuilder();
        key.append(ip).append(":").append(port);
        boolean flag = InnerClientBox.getInstance().contains(key.toString());
        if (flag) {
            return FastJsonUtils.toJson(resMap);
        }
        resMap.put("code", "9999");
        resMap.put("msg", "启动失败");
        return FastJsonUtils.toJson(resMap);
    }

    /**
     * 关闭inner服务
     *
     * @param ip
     * @param port
     * @return
     */

    @RequestMapping("/inner/shutdown")
    @ResponseBody
    public String innerShutdown(String ip, Integer port) {
        Map<String, String> resMap = new HashMap<String, String>();
        resMap.put("code", "0000");
        resMap.put("msg", "操作成功");
        if (Utils.isEmpty(ip)) {
            resMap.put("code", "9999");
            resMap.put("msg", "请输入服务器ip地址");
            return FastJsonUtils.toJson(resMap);
        }
        if (!Utils.isNotEmpty(port)) {
            resMap.put("code", "9999");
            resMap.put("msg", "请输入服务器port");
            return FastJsonUtils.toJson(resMap);
        }
        innerTcpClient.shutdown(ip, port);
        StringBuilder key = new StringBuilder();
        key.append(ip).append(":").append(port);
        boolean flag = InnerClientBox.getInstance().contains(key.toString());
        if (!flag) {
            return FastJsonUtils.toJson(resMap);
        }
        resMap.put("code", "9999");
        resMap.put("msg", "关闭失败");
        return FastJsonUtils.toJson(resMap);
    }

    /**
     * 启动IB服务
     *
     * @param ip
     * @param port
     * @return
     */

    @RequestMapping("/ib/startup")
    @ResponseBody
    public String ibStartup(String ip, Integer port) {
        Map<String, String> resMap = new HashMap<String, String>();
        resMap.put("code", "0000");
        resMap.put("msg", "操作成功");
        if (Utils.isEmpty(ip)) {
            resMap.put("code", "9999");
            resMap.put("msg", "请输入服务器ip地址");
            return FastJsonUtils.toJson(resMap);
        }
        if (!Utils.isNotEmpty(port)) {
            resMap.put("code", "9999");
            resMap.put("msg", "请输入服务器port");
            return FastJsonUtils.toJson(resMap);
        }
        ibTcpClient.start(ip, port);
        StringBuilder key = new StringBuilder();
        key.append(ip).append(":").append(port);
        boolean flag = IbClientBox.getInstance().contains(key.toString());
        if (flag) {
            return FastJsonUtils.toJson(resMap);
        }
        resMap.put("code", "9999");
        resMap.put("msg", "启动失败");
        return FastJsonUtils.toJson(resMap);
    }

    /**
     * 关闭IB服务
     *
     * @param ip
     * @param port
     * @return
     */

    @RequestMapping("/ib/shutdown")
    @ResponseBody
    public String ibShutdown(String ip, Integer port) {
        Map<String, String> resMap = new HashMap<String, String>();
        resMap.put("code", "0000");
        resMap.put("msg", "操作成功");
        if (Utils.isEmpty(ip)) {
            resMap.put("code", "9999");
            resMap.put("msg", "请输入服务器ip地址");
            return FastJsonUtils.toJson(resMap);
        }
        if (!Utils.isNotEmpty(port)) {
            resMap.put("code", "9999");
            resMap.put("msg", "请输入服务器port");
            return FastJsonUtils.toJson(resMap);
        }
        ibTcpClient.shutdown(ip, port);
        StringBuilder key = new StringBuilder();
        key.append(ip).append(":").append(port);
        boolean flag = IbClientBox.getInstance().contains(key.toString());
        if (!flag) {
            return FastJsonUtils.toJson(resMap);
        }
        resMap.put("code", "9999");
        resMap.put("msg", "关闭失败");
        return FastJsonUtils.toJson(resMap);
    }

    /**
     * 通知客户端退出
     *
     * @return
     */
    @RequestMapping("/clientQuit")
    @ResponseBody
    public String clientQuit() {
        newClientOperator.sendCloseMessage();
        return "true";
    }

    /**
     * RF行情命中统计
     *
     * @return
     */
    @RequestMapping("/sourceCount")
    @ResponseBody
    public String rfSourceCount() {
        Map<String, Long> quotaMap = SystemDataCount.getInstance().getSourceQuotaCount();
        return JSON.toJSONString(quotaMap);
    }

    /**
     * 行情数秒统计
     *
     * @return
     */
    @RequestMapping("/productQuotaMinute")
    @ResponseBody
    public String productQuotaMinute() {
        Map<String, SystemDataCount.SecondQuota> quotaMap = SystemDataCount.getInstance().getProductQuotaCount();
        return JSON.toJSONString(quotaMap);
    }

    /**
     * 商品订阅统计
     *
     * @return
     */
    @RequestMapping("/productSubscribe")
    @ResponseBody
    public String productSubscribe() {
        Map<String, Long> dataMap = new HashMap<String, Long>();
        Map<String, Set<String>> newMap = NewChannelManger.getInstance().getProductUserMap();
        for (String key : newMap.keySet()) {
            long size = newMap.get(key).size();
            if (dataMap.containsKey(key)) {
                dataMap.put(key, dataMap.get(key) + size);
            } else {
                dataMap.put(key, size);
            }
        }
        return JSON.toJSONString(dataMap);
    }

    /**
     * 商品行情下发次数
     *
     * @return
     */
    @RequestMapping("/quotaSendSum")
    @ResponseBody
    public String quotaSendSum() {
        Map<String, Long> quotaMap = SystemDataCount.getInstance().quotaSendSum();
        Map<String, Long> successQuotaMap = SystemDataCount.getInstance().quotaSuccessSum();
        Map<String, Map<String, Long>> dataMap = new HashMap<String, Map<String, Long>>();
        for (Map.Entry<String, Long> entry : quotaMap.entrySet()) {
            Map<String, Long> map = new HashMap<String, Long>();
            map.put("sendSum", entry.getValue());
            map.put("successSum", successQuotaMap.get(entry.getKey()));
            dataMap.put(entry.getKey(), map);
        }
        return JSON.toJSONString(dataMap);
    }

    /**
     * 同步行情源比例
     *
     * @return
     */
    @RequestMapping("/syncSourceRate")
    @ResponseBody
    public String syncQuotaSourceRate() {
        spreadSync.syncSourceRate();
        Map<String, String> resMap = new HashMap<String, String>();
        resMap.put("code", "0000");
        resMap.put("msg", "操作成功");
        return JSON.toJSONString(resMap);
    }

    /**
     * 同步点差倍数
     *
     * @return
     */
    @RequestMapping("/syncSpreadMultiple")
    @ResponseBody
    public String syncSpreadMultiple() {
        spreadSync.syncSpreadMultiple();
        Map<String, String> resMap = new HashMap<String, String>();
        resMap.put("code", "0000");
        resMap.put("msg", "操作成功");
        return JSON.toJSONString(resMap);
    }

    /**
     * 通知客户端退出
     *
     * @return
     */
    @RequestMapping("/userQuit")
    @ResponseBody
    public String userQuit(String userId) {
        newClientOperator.sendCloseMessage(userId);
        return "true";
    }
}
