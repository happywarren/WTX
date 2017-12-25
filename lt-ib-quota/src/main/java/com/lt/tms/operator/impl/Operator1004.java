package com.lt.tms.operator.impl;

import com.alibaba.fastjson.JSONObject;
import com.lt.tms.comm.constant.IConstants;
import com.lt.tms.comm.exception.ITmsErrCode;
import com.lt.tms.operator.BaseOperator;
import com.lt.tms.operator.QuotaCache;
import com.lt.tms.quota.bean.ErrorBean;
import com.lt.tms.quota.bean.QuotaDataBean;
import com.lt.tms.quota.ib.api.IbQuotaApi;
import com.lt.tms.quota.ib.api.bean.ContractBean;
import com.lt.tms.tcp.server.TcpClient;
import com.lt.tms.tcp.server.config.NettyConfig;
import com.lt.tms.utils.Utils;
import io.netty.channel.ChannelHandlerContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class Operator1004 extends BaseOperator {

    @Autowired
    private IbQuotaApi ibQuotaApi;
    @Autowired
    private TcpClient tcpClient;
    @Autowired
    private RedisTemplate redisTemplate;
    @Autowired
    private NettyConfig nettyConfig;

    @Override
    public void operator(ChannelHandlerContext ctx, String data) {
        ValueOperations<String, ContractBean> valueOperations = redisTemplate.opsForValue();
        try {
            List<ContractBean> contractBeanList = JSONObject.parseArray(data, ContractBean.class);
            for (ContractBean contractBean : contractBeanList) {

                if (!Utils.isNotEmpty(contractBean)) {
                    ErrorBean errorBean = errorBean(ErrorBean.QUOTA, ITmsErrCode.ERR_0001);
                    tcpClient.sendErrorMessage(ctx, errorBean);
                    continue;
                }

                if (!Utils.isNotEmpty(contractBean.getTickerId())) {
                    ErrorBean errorBean = errorBean(ErrorBean.QUOTA, ITmsErrCode.ERR_1000);
                    tcpClient.sendErrorMessage(ctx, errorBean);
                    continue;
                }

                if (Utils.isEmpty(contractBean.getProductCode())) {
                    ErrorBean errorBean = errorBean(ErrorBean.QUOTA, ITmsErrCode.ERR_1001);
                    tcpClient.sendErrorMessage(ctx, errorBean);
                    continue;
                }

                if (Utils.isEmpty(contractBean.getSecType())) {
                    ErrorBean errorBean = errorBean(ErrorBean.QUOTA, ITmsErrCode.ERR_1002);
                    tcpClient.sendErrorMessage(ctx, errorBean);
                    continue;
                }

                if (Utils.isEmpty(contractBean.getSymbol())) {
                    ErrorBean errorBean = errorBean(ErrorBean.QUOTA, ITmsErrCode.ERR_1005);
                    tcpClient.sendErrorMessage(ctx, errorBean);
                    continue;
                }

                if (Utils.isEmpty(contractBean.getCurrency())) {
                    ErrorBean errorBean = errorBean(ErrorBean.QUOTA, ITmsErrCode.ERR_1003);
                    tcpClient.sendErrorMessage(ctx, errorBean);
                    continue;
                }

                if (Utils.isEmpty(contractBean.getExchange())) {
                    ErrorBean errorBean = errorBean(ErrorBean.QUOTA, ITmsErrCode.ERR_1004);
                    tcpClient.sendErrorMessage(ctx, errorBean);
                    continue;
                }

                if (QuotaCache.getInstance().subscribe(contractBean.getTickerId())) {
                    continue;
                }

                ibQuotaApi.subscribe(contractBean);
                QuotaCache.getInstance().setQuotaDataBean(contractBean.getTickerId(), new QuotaDataBean(contractBean.getProductCode()));

                StringBuilder subscribeListKey = new StringBuilder();
                subscribeListKey.append(IConstants.QUOTA_IB_SUBSCRIBE_LIST).append(IConstants.SERVER_IP).append(":").append(nettyConfig.getPort());
                boolean isMember = redisTemplate.opsForSet().isMember(subscribeListKey.toString(), contractBean.getProductCode());
                if (!isMember) {
                    redisTemplate.opsForSet().add(subscribeListKey.toString(), contractBean.getProductCode());
                    valueOperations.set(IConstants.QUOTA_IB_SUBSCRIBE + contractBean.getProductCode(), contractBean);
                }
            }
        } catch (Exception e) {
            ErrorBean errorBean = errorBean(ErrorBean.QUOTA, ITmsErrCode.ERR_0001);
            tcpClient.sendErrorMessage(ctx, errorBean);
        }
    }
}
