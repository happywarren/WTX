package com.lt.tms.tcp.server;

import com.lt.tms.comm.constant.IConstants;
import com.lt.tms.comm.exception.ITmsErrCode;
import com.lt.tms.comm.json.FastJsonUtils;
import com.lt.tms.operator.QuotaCache;
import com.lt.tms.quota.bean.ErrorBean;
import com.lt.tms.quota.bean.QuotaDataBean;
import com.lt.tms.trade.bean.TradeResBean;
import com.lt.tms.utils.Utils;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.util.concurrent.GlobalEventExecutor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class TcpClient {

    private Logger logger = LoggerFactory.getLogger(getClass());

    private final ChannelGroup channels = new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);

    public void addChannel(Channel channel) {
        channels.add(channel);
    }

    public void removeChannel(Channel channel) {
        channels.remove(channel);
    }

    public void sendQuotaInfo(QuotaDataBean quotaDataBean) {
        Map<String, Object> dataMap = new HashMap<String, Object>();
        dataMap.put("cmd", IConstants.RES_QUOTA_DATA);
        dataMap.put("data", quotaDataBean);
        String message = FastJsonUtils.toJson(dataMap);
        logger.info("分发行情 {} ", message);
        channels.writeAndFlush(message);
    }

    public void sendTradeInfo(TradeResBean tradeResBean) {
        logger.debug("下发报单结果 {} ", FastJsonUtils.toJson(tradeResBean));
        String tradeId = tradeResBean.getTradeId();
    }

    public void sendErrorMessage(ChannelHandlerContext ctx, ErrorBean errorBean) {
        Map<String, Object> dataMap = new HashMap<String, Object>();
        dataMap.put("cmd", IConstants.RES_ERROR);
        dataMap.put("data", errorBean);
        logger.error("下发错误消息 {} {}", ctx.channel().remoteAddress(), FastJsonUtils.toJson(errorBean));
        ctx.writeAndFlush(FastJsonUtils.toJson(dataMap));
    }

    public void sendErrorMessage(Integer id, int errorCode, String message) {
        //判断行情
        QuotaDataBean quotaDataBean = QuotaCache.getInstance().getQuotaDataBean(id);
        ErrorBean errorBean = null;
        //行情错误
        if (Utils.isNotEmpty(quotaDataBean)) {
            errorBean = new ErrorBean();
            errorBean.setErrorType(ErrorBean.QUOTA);
            errorBean.setErrorCode(ITmsErrCode.ERR_1006);
            StringBuilder errorMessage = new StringBuilder();
            errorMessage.append("errorCode: ").append(errorCode).append(" errorMessage: ").append(message);
            errorBean.setErrorMessage(errorMessage.toString());
        }

        if (Utils.isNotEmpty(errorBean)) {
            Map<String, Object> dataMap = new HashMap<String, Object>();
            dataMap.put("cmd", IConstants.RES_ERROR);
            dataMap.put("data", errorBean);
            channels.writeAndFlush(FastJsonUtils.toJson(dataMap));
        }
    }
}
