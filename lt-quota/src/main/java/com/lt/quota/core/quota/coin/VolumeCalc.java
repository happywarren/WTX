package com.lt.quota.core.quota.coin;

import com.lt.quota.core.utils.NumberUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

/**
 * 成交量
 *
 * @author mcsong
 * @create 2017-10-27 9:12
 */
@Component
public class VolumeCalc {

    private static final String QUOTA_VOLUME = "quota:volume";

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    public void calc(String productCode, BigDecimal volume) {
        stringRedisTemplate.opsForHash().put(QUOTA_VOLUME, productCode, NumberUtil.toStr(volume));
        if (productCode.equals("BTCUSD")) {
            stringRedisTemplate.opsForHash().put(QUOTA_VOLUME, "BTCUSDmini", NumberUtil.toStr(volume));
        }
    }

    public Integer getVolume(String productCode) {
        String lastVolumeStr = (String) stringRedisTemplate.opsForHash().get(QUOTA_VOLUME, productCode);
        BigDecimal lastVolume = NumberUtil.formatBigDecimal(lastVolumeStr, new BigDecimal(0));
        return lastVolume.intValue();
    }
}
