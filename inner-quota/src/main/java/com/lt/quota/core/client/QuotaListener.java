package com.lt.quota.core.client;

import com.alibaba.fastjson.JSONObject;
import com.lt.quota.core.comm.netty.client.listener.OnMessageListener;
import com.lt.quota.core.quota.CleanInstance;
import com.lt.quota.core.quota.ProductTimeCache;
import com.lt.quota.core.utils.FastJsonUtils;
import com.lt.tradeclient.mdata.model.NewQuotaBean;
import com.lt.tradeclient.mdata.model.QuotaBean;
import com.lt.tradeclient.utils.MapUtils;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.EventLoop;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

public class QuotaListener implements OnMessageListener {

    Logger logger = LoggerFactory.getLogger(getClass());

    private volatile ScheduledFuture<?> heartBeatScheduler;

    private ProductTimeCache productTimeCache;

    public void setProductTimeCache(ProductTimeCache productTimeCache){
        this.productTimeCache = productTimeCache;
    }


    @Override
    public void onMessage(ChannelHandlerContext ctx, String msg) {
        logger.info(msg);
        /*
        JSONObject msgJson =  JSONObject.parseObject(msg);
        String data =  msgJson.getString("DATA");
        String cmd = msgJson.getString("CMD");
        if(cmd.equals("1005")){
            NewQuotaBean quotaBean =  FastJsonUtils.getJson(data, NewQuotaBean.class);
            QuotaBean quotaBean1 = new QuotaBean(quotaBean);
            CleanInstance.getInstance().setInnerMarketQueue(quotaBean1);
        }*/
    }


    @Override
    public void onPing(ChannelHandlerContext ctx, String msg) {

    }

    @Override
    public void onActive(ChannelHandlerContext ctx) {

        logger.info("连接到服务器:"+ctx.channel().remoteAddress());

        /*
        //发送订阅所有行情的消息
        Map map =  MapUtils.getMap("2000",null);
        ctx.writeAndFlush(FastJsonUtils.toJson(map));

        //启动心跳定时器
        EventLoop loop =  ctx.channel().eventLoop();
        loop.scheduleAtFixedRate(new HeartBeatRunnable(ctx),0,5, TimeUnit.SECONDS);*/
        Map map =new HashMap();
        map.put("code","GC1806");
        ctx.writeAndFlush(FastJsonUtils.toJson(map));
    }

    @Override
    public void onInactive(ChannelHandlerContext ctx) {
        System.out.println("onInactive");
    }

    @Override
    public void onException(ChannelHandlerContext ctx) {
        ctx.close();
    }

    private class TmsCmdBean{
        String CMD;
        String host;

        public String getCMD() {
            return CMD;
        }

        public void setCMD(String CMD) {
            this.CMD = CMD;
        }

        public String getHost() {
            return host;
        }

        public void setHost(String host) {
            this.host = host;
        }
    }
}
