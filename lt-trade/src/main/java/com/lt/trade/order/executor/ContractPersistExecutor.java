/*
 * PROJECT NAME: lt-trade
 * PACKAGE NAME: com.lt.trade.order.executor
 * FILE    NAME: OuterPersistExecutor.java
 * COPYRIGHT: Copyright(c) 2017 LT All Rights Reserved.
 */
package com.lt.trade.order.executor;

import com.alibaba.fastjson.JSONObject;
import com.lt.trade.order.service.ICashOrderPersistService;
import com.lt.trade.tradeserver.bean.BaseMatchBean;
import com.lt.trade.tradeserver.bean.FutureErrorBean;
import com.lt.trade.tradeserver.bean.FutureMatchBean;
import com.lt.trade.tradeserver.bean.FutureMatchWrapper;
import com.lt.util.error.LTException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * 差价合约订单委托、成交回执结果数据持久化执行器
 */
@Service
public class ContractPersistExecutor extends AbstractPersistExecutor implements InitializingBean {
    private Logger logger = LoggerFactory.getLogger(getClass());

    /**
     * TODO（用一句话描述这个变量表示什么）
     */
    private static final long serialVersionUID = 4055658253011319857L;

    /**
     * 任务执行服务
     */
    private final ExecutorService executorService = Executors.newFixedThreadPool(1);
    /**
     * C++返回的消息队列
     */
    private final BlockingQueue<FutureMatchWrapper> queue = new LinkedBlockingQueue<FutureMatchWrapper>();
    /**
     * 现金外盘订单持久化服务
     */
    @Autowired
    private ICashOrderPersistService cashOrderContractPlatePersistServiceImpl;

    /**
     * @throws Exception
     * @author XieZhibing
     * @date 2016年12月29日 下午3:24:51
     * @see InitializingBean#afterPropertiesSet()
     */
    @Override
    public void afterPropertiesSet() throws Exception {
        // TODO Auto-generated method stub
        this.execute();
    }

    /**
     * @return
     * @author XieZhibing
     * @date 2016年12月12日 上午9:59:23
     */
    @Override
    public BlockingQueue<FutureMatchWrapper> getQueue() {
        // TODO Auto-generated method stub
        return this.queue;
    }

    /**
     * @return
     * @author XieZhibing
     * @date 2016年12月12日 上午9:59:23
     */
    @Override
    public ICashOrderPersistService getCashOrderPersistService() {
        // TODO Auto-generated method stub
        return this.cashOrderContractPlatePersistServiceImpl;
    }


    @Override
    protected void persist() {
        FutureMatchWrapper matchWrapper = null;
        FutureMatchBean matchBean = null;
        FutureErrorBean errorBean = null;
        try {
            matchWrapper = getQueue().take();
            if (matchWrapper == null) {
                return;
            }
            int messageId = matchWrapper.getMessageId();
            BaseMatchBean baseMatchBean = matchWrapper.getBaseMatchBean();
            if (baseMatchBean == null) {
                return;
            }

			logger.info("------------处理收到回执消息messageId={},回执信息baseMatchBean={}----------------",messageId,JSONObject.toJSONString(baseMatchBean));

            switch (messageId) {
                case 2002:
                    errorBean = (FutureErrorBean) baseMatchBean;
                    final FutureErrorBean bean2002 = errorBean;
                    set.add(bean2002.getPlatformId());
                    executorService.execute(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                    getCashOrderPersistService().doPersist2002(bean2002);
                            } catch (Exception e) {
                                logger.error("baseMatchBean:{}", JSONObject.toJSON(bean2002));
                                logger.error("处理2002回执消息异常：e:{}", e);
                            } finally {
                                set.remove(bean2002.getPlatformId());
                            }
                        }
                    });
                    break;
                case 2004:
                    matchBean = (FutureMatchBean)baseMatchBean;
                    final FutureMatchBean bean2004 = matchBean;
                    set.add(bean2004.getPlatformId());

                    executorService.execute(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                if(bean2004.getFundType()!=1){
                                    getCashOrderPersistService().doPersist2004(bean2004);
                                }
                            } catch (LTException e) {
                                logger.error("baseMatchBean:{}",JSONObject.toJSON(bean2004));
                                logger.error("处理2004回执消息异常：e:{}",e);
                            }finally{
                                set.remove(bean2004.getPlatformId());
                            }
                        }
                    });
                    break;
                default:
                    break;
            }
        } catch (Exception e) {
            logger.error(e.getMessage());
            e.printStackTrace();
        }

    }
}
