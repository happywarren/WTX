package com.lt.trade;

import com.lt.api.user.IInvestorFeeCfgApiService;
import com.lt.enums.trade.PlateEnum;
import com.lt.model.user.InvestorAccount;
import com.lt.trade.netty.BaseClient;
import com.lt.trade.netty.OnClientStartupListener;
import com.lt.trade.netty.OnStartupListener;
import com.lt.trade.order.executor.ContractPersistExecutor;
import com.lt.trade.order.executor.InnerPersistExecutor;
import com.lt.trade.order.executor.OuterPersistExecutor;
import com.lt.trade.order.service.IOrderBusinessService;
import com.lt.trade.order.service.IOrderSellTradeService;
import com.lt.trade.order.service.IProductInfoService;
import com.lt.trade.tradeserver.BaseTrade;
import com.lt.trade.tradeserver.ContractTrade;
import com.lt.trade.tradeserver.InnerFutureTrade;
import com.lt.trade.tradeserver.OuterFutureTrade;
import com.lt.trade.tradeserver.bean.ProductInfoBean;
import com.lt.trade.utils.TradeUtils;
import com.lt.util.utils.StringTools;
import com.lt.util.utils.prop.CustomerPropertiesConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.*;

/**
 * 交易模块
 * <p>
 * Created by sunch on 2016/12/12.
 */
public class TradeServer implements InitializingBean {

    private static Logger LOGGER = LoggerFactory.getLogger(TradeServer.class);

    @Autowired
    private CustomerPropertiesConfig propertyConfig;

    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    @Autowired
    private IProductInfoService productInfoService;

    @Autowired
    private IOrderBusinessService orderBusinessService;

    @Autowired
    private OuterPersistExecutor outerPersistExecutor;

    @Autowired
    private InnerPersistExecutor innerPersistExecutor;

    @Autowired
    private ContractPersistExecutor contractPersistExecutor;

    @Autowired
    private IOrderSellTradeService scoreOrderSellTradeServiceImpl;

    @Autowired
    private ProductTimeCache productTimeCache;

    @Autowired
    private IInvestorFeeCfgApiService feeCfgApiService;

    private Map<String, BaseClient> serverMap = new ConcurrentHashMap<>(16);

    /**
     * 内盘交易
     */
    private InnerFutureTrade innerFutureTrade = new InnerFutureTrade();

    /**
     * 外盘交易
     */
    private OuterFutureTrade outerFutureTrade = new OuterFutureTrade();

    /**
     * 差价合约交易
     */
    private ContractTrade contractTrade = new ContractTrade();

    private ExecutorService executorService = Executors.newCachedThreadPool();

    /**
     * java行情服务器是否已连接标记
     */
    private volatile Boolean quotaFlag = false;


    public InnerFutureTrade getInnerFutureTrade() {
        return innerFutureTrade;
    }

    public OuterFutureTrade getOuterFutureTrade() {
        return outerFutureTrade;
    }

    public ContractTrade getContractTrade() {
        return contractTrade;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        Set<String> set = redisTemplate.opsForSet().members("TELE_WAIN");
        if (StringTools.isNotEmpty(set) && set.size() > 0) {
            TradeUtils.teleSet.addAll(set);
        }

        startup();
    }

    private void startup() {
        Map<String, ProductInfoBean> productInfoBeanMap = productTimeCache.getProductInfoMap();
        if (null == productInfoBeanMap || productInfoBeanMap.size() <= 0) {
            LOGGER.info("商品加载失败");
            System.exit(1);
        }

        //读取券商帐号配置
        List<InvestorAccount> investorAccountList = feeCfgApiService.listInvestorAccountForServer();
        LOGGER.info("investorAccountList：{}", investorAccountList.size());

        if (StringTools.isNotEmpty(investorAccountList)) {
            //内外盘交易模块启动
            for (InvestorAccount investorAccount : investorAccountList) {
                if (PlateEnum.OUTER_PLATE.getValue().equals(investorAccount.getPlateType())) {
                    startupNext(outerFutureTrade, investorAccount);
                } else if (PlateEnum.INNER_PLATE.getValue().equals(investorAccount.getPlateType())) {
                    startupNext(innerFutureTrade, investorAccount);
                }
            }

            //差价合约交易模块启动
            startupNext(contractTrade, null);
            LOGGER.info("【" + contractTrade.getPlateName() + "】交易模块启动成功...");
            //启动内外盘报单线程
            executorService.execute(outerFutureTrade);
            LOGGER.info("【" + outerFutureTrade.getPlateName() + "】交易模块启动成功...");
            executorService.execute(innerFutureTrade);
            LOGGER.info("【" + innerFutureTrade.getPlateName() + "】交易模块启动成功...");

        } else {
            LOGGER.info("加载不到c++交易模块服务器配置...");
        }
    }

    private void startupNext(BaseTrade baseTrade, InvestorAccount investorAccount) {
        //初始化各盘参数
        if (baseTrade.getClientMap().size() == 0) {
            baseTrade.setRedisTemplate(redisTemplate);
            baseTrade.setProductInfoService(productInfoService);
            if (PlateEnum.OUTER_PLATE.getName().equals(baseTrade.getPlateName())) {
                baseTrade.setPersistExecutor(outerPersistExecutor);
            } else if (PlateEnum.INNER_PLATE.getName().equals(baseTrade.getPlateName())) {
                baseTrade.setPersistExecutor(innerPersistExecutor);
            } else if (PlateEnum.CONTRACT_FOR_DIFFERENCE.getName().equals(baseTrade.getPlateName())) {
                baseTrade.setPersistExecutor(contractPersistExecutor);
            }
            baseTrade.setOrderBusinessService(orderBusinessService);
            baseTrade.setScoreOrderSellTradeService(scoreOrderSellTradeServiceImpl);
            baseTrade.setProductTimeCache(productTimeCache);
        }

        //连接c++交易模块(差价合约不需要连接)
        if(!PlateEnum.CONTRACT_FOR_DIFFERENCE.getName().equals(baseTrade.getPlateName())){
            if (!serverMap.containsKey(investorAccount.getServerIp() + ":" + investorAccount.getServerPort())) {
                startupTrade(baseTrade, investorAccount, false);
            } else {
                baseTrade.getClientMap().put(investorAccount.getSecurityCode(), serverMap.get(investorAccount.getServerIp() + ":" + investorAccount.getServerPort()));
            }
        }

        //初始化风控队列、加载数据、执行清仓调度器（各盘分别只触发一次）
        if (baseTrade.getClientMap().size() == 1
                || PlateEnum.CONTRACT_FOR_DIFFERENCE.getName().equals(baseTrade.getPlateName())) {
            startupRiskControl(baseTrade);
            startupLoadRuntimeData(baseTrade);
            startupClearScheduler(baseTrade);
        }
    }

    /**
     * 1.连接c++交易模块
     *
     * @param baseTrade       交易对象
     * @param investorAccount 券商帐号配置对象
     * @param apiFlag         是否接口调方法
     */
    private void startupTrade(final BaseTrade baseTrade, final InvestorAccount investorAccount, boolean apiFlag) {
        LOGGER.info("1.开始连接【" + baseTrade.getPlateName() + "】c++交易模块...");
        final CountDownLatch signal0 = new CountDownLatch(1);
        baseTrade.startupFutureTrade(investorAccount.getServerIp(), Integer.parseInt(investorAccount.getServerPort()), new OnClientStartupListener() {
            @Override
            public void onCompletion(boolean result, BaseClient baseClient) {
                if (result) {
                    LOGGER.info("连接【{}】c++交易模块成功,ip:{},port:{}", baseTrade.getPlateName(), investorAccount.getServerIp(), investorAccount.getServerPort());
                    serverMap.put(investorAccount.getServerIp() + ":" + investorAccount.getServerPort(), baseClient);
                    baseTrade.getClientMap().put(investorAccount.getSecurityCode(), baseClient);
                    signal0.countDown();
                }
            }
        });

        try {
            boolean countDownFlag = signal0.await(5 * 60, TimeUnit.SECONDS);
            if (!countDownFlag) {
                LOGGER.info("连接【{}】c++交易模块超时,ip:{},port:{}", baseTrade.getPlateName(), investorAccount.getServerIp(), investorAccount.getServerPort());
                if (!apiFlag) {
                    System.exit(1);
                }
                else{
                    //中断线程
                    if(!baseTrade.getTradeClientThread().isInterrupted()){
                        baseTrade.getTradeClientThread().interrupt();
                    }
                }
            }
        } catch (InterruptedException e) {
            LOGGER.info("连接【{}】c++交易模块失败,ip:{},port:{},错误消息:{}", baseTrade.getPlateName(), investorAccount.getServerIp(), investorAccount.getServerPort(), e.getMessage());
            if (!apiFlag) {
                System.exit(1);
            }
        }

    }

    /**
     * 2.启动风控模块(内部连接外盘c++行情模块)
     */
    private void startupRiskControl(final BaseTrade baseTrade) {
        String outerQuoteHost = propertyConfig.getProperty("outer_quota_host").trim();
        String outerQuotePort = propertyConfig.getProperty("outer_quota_port").trim();
        LOGGER.info("2.开始启动【" + baseTrade.getPlateName() + "】风控(连接java行情)模块...");
        baseTrade.startupRiskControl(outerQuoteHost, outerQuotePort, new OnStartupListener() {
            @Override
            public void onCompletion(boolean result) {
                if (result) {
                    LOGGER.info("启动【" + baseTrade.getPlateName() + "】风控(连接java行情)模块...");
                    quotaFlag = true;
                } else {
                    LOGGER.info("启动【" + baseTrade.getPlateName() + "】风控(连接java行情)模块失败,java行情服务器连接失败 ");
                    System.exit(1);
                }
            }
        }, quotaFlag);
    }

    /**
     * 3.加载运行时数据(交易时间/商品基础数据/持仓订单数据等)
     *
     * @param baseTrade 交易对象
     */
    private void startupLoadRuntimeData(BaseTrade baseTrade) {
        LOGGER.info("3.开始加载【" + baseTrade.getPlateName() + "】运行时数据...");
        try {
            baseTrade.loadRuntimeData();
            LOGGER.info("加载【" + baseTrade.getPlateName() + "】运行时数据成功...");
        } catch (Exception e) {
            LOGGER.error("加载【" + baseTrade.getPlateName() + "】运行时数据失败: " + e.getMessage(), e);
            System.exit(1);
        }
    }

    /**
     * 4.启动清仓调度器
     *
     * @param baseTrade 交易对象
     */
    private void startupClearScheduler(BaseTrade baseTrade) {
        LOGGER.info("4.开始启动【" + baseTrade.getPlateName() + "】清仓调度器...");
        try {
            baseTrade.startupClearScheduler();
            LOGGER.info("启动【" + baseTrade.getPlateName() + "】清仓调度器成功...");
        } catch (Exception e) {
            LOGGER.info("启动【" + baseTrade.getPlateName() + "】清仓调度器失败: " + e.getMessage());
            System.exit(1);
        }
    }

    /**
     * 外部接口调用修改券商帐号对应的c++服务器ip和端口号
     * startupSingle
     *
     * @param investorAccount 券商帐号配置对象
     */
    public void startupSingle(final InvestorAccount investorAccount) {
        BaseTrade baseTrade = null;
        if (PlateEnum.OUTER_PLATE.getValue().equals(investorAccount.getPlateType())) {
            baseTrade = this.outerFutureTrade;
        } else if (PlateEnum.INNER_PLATE.getValue().equals(investorAccount.getPlateType())) {
            baseTrade = this.innerFutureTrade;
        }

        if(baseTrade == null){
            return;
        }

        LOGGER.info("serverMap:{}", serverMap);
        if (serverMap.containsKey(investorAccount.getServerIp() + ":" + investorAccount.getServerPort())) {
            baseTrade.getClientMap().put(investorAccount.getSecurityCode(), serverMap.get(investorAccount.getServerIp() + ":" + investorAccount.getServerPort()));
            return;
        }

        startupTrade(baseTrade, investorAccount, true);
    }
}
