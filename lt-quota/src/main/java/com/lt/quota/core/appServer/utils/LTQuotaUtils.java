package com.lt.quota.core.appServer.utils;


import com.alibaba.fastjson.JSONObject;
import com.lt.quota.core.quota.bean.QuotaBean;
import com.lt.quota.core.utils.Utils;
import io.netty.channel.Channel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 通道相關公共操作
 *
 * @author guodw
 */
public class LTQuotaUtils {

    static Logger logger = LoggerFactory.getLogger(LTQuotaUtils.class);
    /**
     * 商品_用户 map
     */
    private static Map<String, Set<String>> product_user_map = new ConcurrentHashMap<String, Set<String>>();

    /**
     * 用户_通道 map
     */
    private static Map<String, Channel> user_channel_map = new ConcurrentHashMap<String, Channel>();

    /**
     * 订阅了多少通道
     *
     * @return
     */
    public static int getCount() {
        int count = 0;
        Iterator<Entry<String, Set<String>>> it = product_user_map.entrySet().iterator();
        while (it.hasNext()) {
            Entry<String, Set<String>> obj = it.next();
            count += obj.getValue().size();
        }
        return count;
    }

    /**
     * 在线人数
     *
     * @return
     */
    public static int getUserIdCount() {
        return user_channel_map.size();
    }

    /**
     * 保存用戶通道關係
     *
     * @param product CL1601
     * @param userId
     * @param channel 通道
     */
    public static void saveUserChannelMap(String product, String userId, Channel channel) throws RuntimeException {
        Set<String> set = product_user_map.get(product);
        if (null == set) {
            set = new HashSet<String>();
            product_user_map.put(product, set);
        }
        set.add(userId);
        user_channel_map.put(userId, channel);
    }

    /**
     * 刪除用戶通道關係
     *
     * @param product
     * @param userId
     */
    public static void removeUserChannelMap(String product, String userId) throws RuntimeException {
        if (Utils.isNotEmpty(product) && Utils.isNotEmpty(userId)) {
            Set<String> set = product_user_map.get(product);
            if (Utils.isNotEmpty(set)) {
                set.remove(userId);
                user_channel_map.remove(userId);
            } else {
                product_user_map.put(product, new HashSet<String>());
            }
        } else {
            logger.error("参数为空 product:{},userId:{}", product, userId);
        }

    }

    /**
     * 刪除用戶通道關係
     *
     * @param channel
     */
    public static String removeUserChannelMap(Channel channel) throws RuntimeException {
        String userId = null;
        if (user_channel_map != null && !user_channel_map.isEmpty()) {
            Iterator<Entry<String, Channel>> it = user_channel_map.entrySet().iterator();
            while (it.hasNext()) {
                Entry<String, Channel> obj = it.next();
                if (obj.getValue() == channel) {
                    userId = obj.getKey();
                    removeProductChannelMap(userId);
                    it.remove();
                    break;
                }
            }
        }
        return userId;
    }

    /**
     * 删除商品用户关系
     *
     * @param userId
     */
    public static void removeProductChannelMap(String userId) {
        if (product_user_map != null && !product_user_map.isEmpty()) {
            for (Entry<String, Set<String>> entry : product_user_map.entrySet()) {
                if (!entry.getValue().isEmpty()) {
                    Set<String> set = entry.getValue();
                    set.remove(userId);
                    break;
                }
            }
        }
    }

    /**
     * 刪除用戶通道關係
     *
     * @param channel
     */
    public static void removeUserChannelMap(String userId) throws RuntimeException {
        removeProductChannelMap(userId);
        user_channel_map.remove(userId);
    }

    /**
     * 用戶鏈接人數    （登陆人數）
     *
     * @return
     */
    public static int findUserChannelMapSize() throws RuntimeException {
        return user_channel_map.size();

    }

    /**
     * 在线人数
     *
     * @return
     * @throws RuntimeException
     */
    public static int findChannelMapSize() throws RuntimeException {
        return findUserChannelMapSize();

    }


    /**
     * 获取用户通道
     *
     * @param userId
     * @return
     */
    public static Channel findUserChannelByUserId(String userId) throws RuntimeException {
        return user_channel_map.get(userId);
    }

    /**
     * 在线用户信息发送
     *
     * @param product
     * @param json
     */
    public static void userChannelMapSendMsg(String product, QuotaBean quotaBean) throws RuntimeException {
        Map<String, Object> dataMap = new HashMap<>();
        dataMap.put("CMDID", 1001);
        quotaBean.setSource(null);
        dataMap.put("DATA", quotaBean);
        String json = JSONObject.toJSONString(dataMap);
        Set<String> set = product_user_map.get(product);
        logger.info("分发商品 {} 行情 {} ", product, json);
        if (!Utils.isNotEmpty(set)) {
            return;
        }
        logger.info("订阅商品 {} 行情数量 {}", product, json, set.size());
        Set<String> rmSet = new HashSet<String>();
        for (String userId : set) {
            Channel channel = user_channel_map.get(userId);
            if (Utils.isNotEmpty(channel)) {
                if (channel.isWritable()) {
                    channel.writeAndFlush(json);
                }
            } else {
                rmSet.add(userId);
            }
        }
        set.removeAll(rmSet);
    }

    /**
     * 群發信息給游客與用戶
     *
     * @param json
     * @param product
     */
    public static void sendMsg(QuotaBean quotaBean, String product) throws RuntimeException {
        userChannelMapSendMsg(product, quotaBean);
    }

    /**
     * 验证是否是合法请求
     *
     * @param token
     * @param sign
     * @param product
     * @param type
     * @return
     */
    public static boolean checkSign(String token, String sign, String product, Integer type) throws RuntimeException {
        if (!Utils.isNotEmpty(sign)) {
            return false;
        }
        logger.info(token + type + product);
////		String str = Md5Encrypter.MD5(token + type + product);
//		logger.info("str:" + str);
//		logger.info("sign:" + sign);
//		if (sign.equalsIgnoreCase(str)) {
//			return true;
//		} else {
//			return false;
//		}
        return false;
    }

    public static void main(String[] args) {

        System.out.println(checkSign("ebac3ba59987b97be9fa80b395f2a3f662a73b0e78b4db18caef6a856bc8a2d60cb7ceee0ccdb994badb598c88ed950f4e5670bdc199227eff07bf9203cd5037", "b174b85a1aca23286b5c9944a25b97a6", null, 2));
    }

    /**
     * 给所有在线用户发送信息
     *
     * @param msg
     */
    public static void sendMsg(String msg) {
        for (Entry<String, Channel> entry : user_channel_map.entrySet()) {
            Channel channel = entry.getValue();
            if (channel.isWritable()) {
                channel.writeAndFlush(msg);
            }
            logger.info("通道channel = {} ,发送信息 msg = {}", channel, msg);
        }
    }
}

