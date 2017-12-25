package com.lt.tms.quota.ib;

import com.alibaba.fastjson.JSONObject;
import com.lt.tms.comm.constant.IConstants;
import com.lt.tms.comm.json.FastJsonUtils;
import com.lt.tms.operator.QuotaCache;
import com.lt.tms.quota.bean.QuotaDataBean;
import com.lt.tms.quota.ib.api.IbQuotaApi;
import com.lt.tms.quota.ib.api.bean.ContractBean;
import com.lt.tms.quota.ib.api.config.IbQuotaConfig;
import com.lt.tms.tcp.server.config.NettyConfig;
import com.lt.tms.utils.DateUtils;
import com.lt.tms.utils.FileUtils;
import com.lt.tms.utils.Utils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.InputStream;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Component
public class IbQuotaStart {

    private Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private IbQuotaApi ibQuotaApi;
    @Autowired
    private RedisTemplate redisTemplate;
    @Autowired
    private IbQuotaConfig ibQuotaConfig;
    @Autowired
    private NettyConfig nettyConfig;

    public void start() {
        if (ibQuotaApi.isConnected()) {
            logger.info("盈透行情已启动");
            return;
        }
        try {
            QuotaCache.getInstance().clearSubscribe();

            logger.info("盈透行情服务器start");
            ibQuotaApi.connect();
            boolean connected = ibQuotaApi.isConnected();
            logger.info("盈透行情服务器end {} ", connected);
            StringBuilder subscribeListKey = new StringBuilder();
            subscribeListKey.append(IConstants.QUOTA_IB_SUBSCRIBE_LIST).append(IConstants.SERVER_IP).append(":").append(nettyConfig.getPort());
            if (connected) {
                ValueOperations<String, ContractBean> valueOperations = redisTemplate.opsForValue();
                List<ContractBean> contractBeanList = null;
                Set<String> keySet = redisTemplate.opsForSet().members(subscribeListKey.toString());
                if (Utils.isNotEmpty(keySet)) {
                    Set<String> newKeySet = new HashSet<String>();
                    for (String key : keySet) {
                        newKeySet.add(IConstants.QUOTA_IB_SUBSCRIBE + key);
                    }
                    contractBeanList = valueOperations.multiGet(newKeySet);
                    for (ContractBean contractBean : contractBeanList) {
                        if (QuotaCache.getInstance().subscribe(contractBean.getTickerId())) {
                            continue;
                        }
                        ibQuotaApi.subscribe(contractBean);
                        init(contractBean.getTickerId(), contractBean.getProductCode());
                    }
                } else {
                    //订阅行情
                    InputStream stream = getClass().getResourceAsStream("/ib/quota.json");
                    String content = FileUtils.readFile(stream);
                    if (Utils.isEmpty(content)) {
                        return;
                    }
                    contractBeanList = JSONObject.parseArray(content, ContractBean.class);
                    for (ContractBean contractBean : contractBeanList) {
                        if (QuotaCache.getInstance().subscribe(contractBean.getTickerId())) {
                            continue;
                        }
                        ibQuotaApi.subscribe(contractBean);
                        init(contractBean.getTickerId(), contractBean.getProductCode());
                        boolean isMember = redisTemplate.opsForSet().isMember(subscribeListKey.toString(), contractBean.getProductCode());
                        if (!isMember) {
                            redisTemplate.opsForSet().add(subscribeListKey.toString(), contractBean.getProductCode());
                            valueOperations.set(IConstants.QUOTA_IB_SUBSCRIBE + contractBean.getProductCode(), contractBean);
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void init(Integer tickerId, String productCode) {
        String fileDir = ibQuotaConfig.getFileDir();
        String filePath = fileDir + File.separator + productCode + ".txt";
        String message = FileUtils.readLastLine(filePath, "utf-8");
        QuotaDataBean quotaDataBean = null;
        if (Utils.isNotEmpty(message)) {
            quotaDataBean = FastJsonUtils.getJson(message, QuotaDataBean.class);
        } else {
            quotaDataBean = new QuotaDataBean(productCode);
        }
        QuotaCache.getInstance().setQuotaDataBean(tickerId, quotaDataBean);
    }

    //检测ib服务连接状态
    public void checkConnected() {
        boolean connected = ibQuotaApi.isConnected();
        logger.info("checkConnected盈透行情服务器 {} ", connected);
        if (!connected) {
            logger.info("盈透行情服务器未连接，重新连接");
            start();
        }
    }

}
