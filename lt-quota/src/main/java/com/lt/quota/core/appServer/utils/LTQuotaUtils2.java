package com.lt.quota.core.appServer.utils;


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
public class LTQuotaUtils2 {

    static Logger logger = LoggerFactory.getLogger(LTQuotaUtils2.class);
    /**
     * 商品_用户 map
     */
    private static Map<String, Set<Channel>> product_user_map = new ConcurrentHashMap<String, Set<Channel>>();

    /**
     * 用户_通道 map
     */
    private static Map<String, Channel> user_channel_map = new ConcurrentHashMap<String, Channel>();

    /**
     * 通道_用户 map
     */
    public static Map<Channel, String> channel_user_map = new ConcurrentHashMap<Channel, String>();

    /**
     * 用户商品关系
     */
    private static Set<String> user_product_map = new HashSet<String>();

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
     * @param type    1:单个行情 2 所有行情
     */
    public static void saveUserChannelMap(String product, String userId, Channel channel) throws RuntimeException {
        Set<Channel> set = product_user_map.get(product);
        if (null == set) {
            set = new HashSet<Channel>();
        }
        set.add(channel);
        user_channel_map.put(userId, channel);
        channel_user_map.put(channel, userId);
        user_product_map.add(userId + product);

    }

    /**
     * 刪除用戶通道關係
     *
     * @param type
     * @param product
     * @param userId
     * @param channel
     */
    public static void removeUserChannelMap(String product, String userId) throws RuntimeException {
        if (Utils.isNotEmpty(product) && Utils.isNotEmpty(userId)) {
            Set<Channel> set = product_user_map.get(product);
            if (Utils.isNotEmpty(set)) {
                Channel channel = user_channel_map.get(userId);
                set.remove(userId);
                user_channel_map.remove(userId);
                channel_user_map.remove(channel);
                user_product_map.remove(userId + product);
            } else {
                product_user_map.put(product, new HashSet<Channel>());
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
        String userId = channel_user_map.get(channel);
        user_channel_map.remove(userId);
        channel_user_map.remove(channel);
        List<String> list = removeProductChannelMap(userId);
        if (Utils.isNotEmpty(list)) {
            for (String product : list) {
                user_product_map.remove(userId + product);
            }
        }
        return userId;
    }

    /**
     * 删除商品用户关系
     *
     * @param userId
     */
    public static List<String> removeProductChannelMap(String userId) {
        List<String> list = new ArrayList<String>();
        Channel channel = user_channel_map.get(userId);
        if (product_user_map != null && !product_user_map.isEmpty()) {
            for (Entry<String, Set<Channel>> entry : product_user_map.entrySet()) {
                if (!entry.getValue().isEmpty()) {
                    Set<Channel> set = entry.getValue();
                    set.remove(channel);
                    list.add(entry.getKey());
                }
            }
        }
        return list;
    }

    /**
     * 刪除用戶通道關係
     *
     * @param channel
     */
    public static void removeUserChannelMap(String userId) throws RuntimeException {
        List<String> list = removeProductChannelMap(userId);
        if (Utils.isNotEmpty(list)) {
            for (String product : list) {
                user_product_map.remove(userId + product);
            }
        }
        user_channel_map.remove(userId);
        Channel channel = user_channel_map.get(userId);
        channel_user_map.remove(channel);
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
    public static void userChannelMapSendMsg(String product, String json) throws RuntimeException {
        Set<Channel> set = product_user_map.get(product);
        Set<Channel> rmSet = new HashSet<Channel>();
        for (Channel channel : set) {
            String userId = channel_user_map.get(channel);
            if (Utils.isNotEmpty(userId)) {
                if (user_product_map.contains(userId + product)) {
                    if (channel.isWritable()) {
                        channel.writeAndFlush(json);
                    }
                }
            } else {
                rmSet.add(channel);
            }
        }
        set.removeAll(rmSet);
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

