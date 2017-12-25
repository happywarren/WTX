package com.lt.user.core.service.impl;

import com.lt.constant.redis.RedisUtil;
import com.lt.model.brand.BrandInfo;
import com.lt.model.user.Channel;
import com.lt.user.core.dao.sqldb.IBrandInfoDao;
import com.lt.user.core.dao.sqldb.IChannelDao;
import com.lt.user.core.service.IBrandInfoService;
import com.lt.user.core.service.IChannelService;
import com.lt.util.utils.StringTools;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.BoundHashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

@Service
public class BrandInfoServiceImpl implements IBrandInfoService {

    @Autowired
    private IBrandInfoDao brandInfoDao;

    @Override
    public BrandInfo getBrandInfoByCode(String brandCode) {
        return brandInfoDao.getBrandInfoByCode(brandCode);
    }
}

