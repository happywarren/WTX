package com.lt.business.core.service.product.impl;

import java.util.List;

import com.lt.constant.redis.RedisUtil;
import com.lt.util.utils.StringTools;
import com.lt.vo.defer.ProductDeferTimeInfoVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.BoundHashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import com.lt.business.core.dao.product.IProductTimeConfigDao;
import com.lt.business.core.service.product.IProductTimeConfigService;
import com.lt.model.product.ProductTimeConfig;

@Service
public class ProductTimeConfigServiceImpl implements IProductTimeConfigService {

	@Autowired
	private IProductTimeConfigDao productTimeConfigDao;
    @Autowired
    private RedisTemplate<String, String> redisTemplate;
	
	@Override
	public List<ProductTimeConfig> findProductTimeConfigByProductId(
			Integer productId) {
		return productTimeConfigDao.findProductTimeConfigByProductId(productId);
	}

	@Override
	public List<ProductDeferTimeInfoVo> queryAllProductDeferTime() {
        //获取现在是什么时区（冬令时/夏令时）
        BoundHashOperations<String, String, String> sysCfgRedis = redisTemplate.boundHashOps(RedisUtil.SYS_CONFIG);
        String cfg_value = sysCfgRedis.get("winter_summer_change");
        if(StringTools.isEmpty(cfg_value) || "1".equals(cfg_value)){//夏令时
            return productTimeConfigDao.queryAllProductDeferTime();
        }else{//冬令时
            return productTimeConfigDao.queryAllProductDeferTimeWinter();
        }

	}

	@Override
	public List<ProductDeferTimeInfoVo> queryAllProductClearTime() {
        //获取现在是什么时区（冬令时/夏令时）
        BoundHashOperations<String, String, String> sysCfgRedis = redisTemplate.boundHashOps(RedisUtil.SYS_CONFIG);
        String cfg_value = sysCfgRedis.get("winter_summer_change");
        if(StringTools.isEmpty(cfg_value) || "1".equals(cfg_value)) {//夏令时
            return productTimeConfigDao.queryAllProductClearTime();
        }else{
            return productTimeConfigDao.queryAllProductClearTimeWinter();
        }

	}

}
