package com.lt.quota.core.appServer.old;


import com.lt.quota.core.utils.Utils;
import io.netty.channel.Channel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collections;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 通道相關公共操作
 *
 * @author guodw
 */
public class OldChannelManger {

    private Logger logger = LoggerFactory.getLogger(OldChannelManger.class);

    /**
     * 用户 通道
     */
    private final Map<String, Channel> USER_CHANNEL_MAP = new ConcurrentHashMap<String, Channel>();

    /**
     * 通道 用户
     */
    private final Map<Channel, String> CHANNEL_USER_MAP = new ConcurrentHashMap<Channel, String>();

    /**
     * 商品 用户
     */
    private final Map<String, Set<String>> PRODUCT_USER_MAP = new ConcurrentHashMap<String, Set<String>>();

    /**
     * 用户 商品
     */
    private final Map<String, Set<String>> USER_PRODUCT_MAP = new ConcurrentHashMap<String, Set<String>>();

    private static OldChannelManger instance;

    private OldChannelManger() {
    }


    public static synchronized OldChannelManger getInstance() {
        if (instance == null)
            instance = new OldChannelManger();
        return instance;
    }


    /**
     * 保存用户 商品 通道 三者之间关系
     *
     * @param product CL1601
     * @param userId
     * @param channel 通道
     */
    public void add(String product, String userId, Channel channel) throws RuntimeException {
        if (Utils.isEmpty(userId) || !Utils.isNotEmpty(channel)) {
            return;
        }
        remove(userId);

        USER_CHANNEL_MAP.put(userId, channel);
        CHANNEL_USER_MAP.put(channel, userId);
        if (Utils.isNotEmpty(product)) {
            add(userId, product);
        }
    }

    public void add(String userId, String product) {
        Set<String> productSet = USER_PRODUCT_MAP.get(userId);
        if (Utils.isEmpty(productSet)) {
            productSet = new HashSet<String>();
            USER_PRODUCT_MAP.put(userId, productSet);
        }
        productSet.add(product);

        Set<String> userSet = PRODUCT_USER_MAP.get(product);
        if (Utils.isEmpty(userSet)) {
            userSet = new HashSet<String>();
            PRODUCT_USER_MAP.put(product, userSet);
        }
        userSet.add(userId);
    }

    /**
     * 删除 用户 商品 通道 之间关系
     *
     * @param userId
     * @param product
     */
    public void remove(String userId, String product) {
        if (Utils.isEmpty(product) || Utils.isEmpty(userId)) {
            return;
        }
        Channel channel = USER_CHANNEL_MAP.get(userId);
        if (!Utils.isNotEmpty(channel)) {
            return;
        }

        Set<String> userSet = PRODUCT_USER_MAP.get(product);
        if (Utils.isNotEmpty(userSet)) {
            userSet.remove(userId);
            PRODUCT_USER_MAP.put(product, userSet);
        }

        Set<String> productSet = USER_PRODUCT_MAP.get(userId);
        if (Utils.isNotEmpty(productSet)) {
            productSet.remove(product);
            USER_PRODUCT_MAP.put(userId, productSet);
        }
    }

    /**
     * 删除 用户 商品 通道 之间关系
     *
     * @param channel
     */
    public void remove(Channel channel) {
        if (!Utils.isNotEmpty(channel)) {
            return;
        }
        String userId = CHANNEL_USER_MAP.get(channel);
        if (Utils.isEmpty(userId)) {
            return;
        }

        USER_CHANNEL_MAP.remove(userId);
        CHANNEL_USER_MAP.remove(channel);

        //删除用户订阅的商品
        Set<String> productSet = USER_PRODUCT_MAP.get(userId);
        if (!Utils.isNotEmpty(productSet)) {
            return;
        }
        USER_PRODUCT_MAP.remove(userId);

        for (String product : productSet) {
            Set<String> userSet = PRODUCT_USER_MAP.get(product);
            userSet.remove(userId);
            PRODUCT_USER_MAP.put(product, userSet);
        }
    }

    /**
     * 删除 用户 商品 通道 之间关系
     *
     * @param userId
     */
    public void remove(String userId) {
        if (Utils.isEmpty(userId)) {
            return;
        }
        //删除用户订阅的商品
        Set<String> productSet = USER_PRODUCT_MAP.get(userId);
        if (!Utils.isNotEmpty(productSet)) {
            return;
        }
        USER_PRODUCT_MAP.remove(userId);

        for (String product : productSet) {
            Set<String> userSet = PRODUCT_USER_MAP.get(product);
            userSet.remove(userId);
            PRODUCT_USER_MAP.put(product, userSet);
        }
    }

    /**
     * 删除 用户 商品 通道 之间关系
     *
     * @param userId
     */
    public void removeByUserId(String userId) {
        try {
            if (Utils.isEmpty(userId)) {
                return;
            }
            Channel channel = USER_CHANNEL_MAP.get(userId);
            if (Utils.isNotEmpty(channel)) {
                USER_CHANNEL_MAP.remove(userId);
                CHANNEL_USER_MAP.remove(channel);
                channel.close();
            }

            //删除用户订阅的商品
            Set<String> productSet = USER_PRODUCT_MAP.get(userId);
            if (Utils.isNotEmpty(productSet)) {
                USER_PRODUCT_MAP.remove(userId);
            }

            for (String product : productSet) {
                Set<String> userSet = PRODUCT_USER_MAP.get(product);
                userSet.remove(userId);
                PRODUCT_USER_MAP.put(product, userSet);
            }
        } catch (Exception e) {
        }
    }

    public String getUserId(Channel channel) {
        return CHANNEL_USER_MAP.get(channel);
    }

    public Set<String> getUserSet(String product) {
        Map<String, Set<String>> productUserMap = Collections.unmodifiableMap(PRODUCT_USER_MAP);
        return productUserMap.get(product);
    }

    public Channel getChannel(String userId) {
        return USER_CHANNEL_MAP.get(userId);
    }

    public Map<String, Set<String>> getProductUserMap() {
        return Collections.unmodifiableMap(PRODUCT_USER_MAP);
    }

    public Map<String, Channel> getUserChannelMap() {
        return Collections.unmodifiableMap(USER_CHANNEL_MAP);
    }
}

