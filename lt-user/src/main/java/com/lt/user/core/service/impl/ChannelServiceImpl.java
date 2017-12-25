package com.lt.user.core.service.impl;

import com.lt.constant.redis.RedisUtil;
import com.lt.model.user.Channel;
import com.lt.user.core.dao.sqldb.IChannelDao;
import com.lt.user.core.service.IChannelService;
import com.lt.util.utils.StringTools;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.BoundHashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

@Service
public class ChannelServiceImpl implements IChannelService {

    @Autowired
    private IChannelDao channelDao;

    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    @Override
    public Channel getChannelByCode(String code) {
        return channelDao.getChannelByCode(code);
    }

    @Override
    public String getChannelCode(String code, String deviceType) {
        try {
            Channel channel = channelDao.getChannelByCode(code);
            if (StringTools.isNotEmpty(channel)) {
                return code;
            }
        } catch (Exception e) {

        }
        BoundHashOperations<String, String, String> sysCfgRedis = redisTemplate.boundHashOps(RedisUtil.SYS_CONFIG);
        if (deviceType.equalsIgnoreCase("Android")) {
            //Android 默认渠道
            return sysCfgRedis.get("android_default_channel");
        } else {
            //IOS 默认渠道
            return sysCfgRedis.get("ios_default_channel");
        }
    }
}

