package com.lt.trade.defer;

import com.alibaba.druid.support.json.JSONUtils;
import com.lt.api.fund.IFundAccountApiService;
import com.lt.api.sms.ISmsApiService;
import com.lt.api.user.IUserApiLogService;
import com.lt.trade.defer.service.DeferService;
import com.lt.trade.defer.service.ProductDeferRunRecordService;
import com.lt.trade.order.service.ICoinPositionSumService;
import com.lt.trade.order.service.IOrderFunctionService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.*;
import java.util.concurrent.*;

/**
 * 项目名称：lt-trade 类名称：DeferInterestScheduTask 类描述：递延费定时清仓任务 创建人：yuanxin
 * 创建时间：2017年4月24日 下午1:47:21
 */
@Component
public class DeferInterestScheduTask implements InitializingBean {

    /**
     * 启动线程
     */
    private final ScheduledExecutorService deferScheduler = new ScheduledThreadPoolExecutor(1);

    private ScheduledFuture<?> deferFuture_ = null;

    /**
     * 时间对应的商品id 09:10:00 ---------- 178
     */
    private final Map<String, Set<String>> deferTimeMap = new LinkedHashMap<String, Set<String>>();
    /**
     * 品种对应的时间点，178 ---- 09:10:11,11:10:10
     */
    private final Map<String, TreeSet<String>> productDeferTime = new ConcurrentHashMap<String, TreeSet<String>>();

    private Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private ProductDeferRunRecordService productDeferRunRecordService;

    @Autowired
    private DeferService deferServiceImpl;
    @Autowired
    private IFundAccountApiService fundAccountService;
    /**
     * 订单服务
     */
    @Autowired
    private IOrderFunctionService orderService;
    /**
     * 短信接口
     */
    @Autowired
    private ISmsApiService smsService;
    /**
     * 用户操作日志接口
     */
    @Autowired
    private IUserApiLogService userLogServiceImpl;

    @Resource(name = "coinPositionSumServiceImpl")
    private ICoinPositionSumService coinPositionSumService;


    @Override
    public void afterPropertiesSet() throws Exception {
        logger.info("执行递延定时任务");
        //startUpDeferTimerLoop();
    }

    public class DeferTimerLoopTask implements Runnable {

        /**
         * 执行的小时
         */
        private String hours;
        /**
         * 执行的分钟
         */
        private String minute;
        /**
         * 执行的秒钟
         */
        private String second;

        public DeferTimerLoopTask(String hours, String minute, String second) {
            this.hours = hours;
            this.minute = minute;
            this.second = second;
        }

        @Override
        public void run() {
            String key = hours + ":" + minute + ":" + second;
            Date date = new Date();
            if (deferTimeMap.containsKey(key)) {
                StringBuilder deferPro = new StringBuilder();
                StringBuilder nextProid = new StringBuilder();
                Set<String> prdSet = deferTimeMap.get(key);

                for (String product : prdSet) {
                    // 处理下一个清仓时间点
                    if (!productDeferTime.containsKey(product)) {
                        logger.info("品种：{}，未设置清仓时间点，本次递延：{} 不执行", product, date);
                        continue;
                    } else {

                        TreeSet<String> tree = productDeferTime.get(product);
                        Iterator<String> it = tree.iterator();
                        while (it.hasNext()) {
                            String timeString = it.next();
                            // 跨天 同小时 跨小时
                            String[] result = timeString.split(":");
                            int deferHour = Integer.parseInt(result[0]);
                            int deferMinute = Integer.parseInt(result[1]);
                            int deferSecond = Integer.parseInt(result[2]);
                            // 配置时间大于当前 递延执行时间 ，则取该时间为下一个清仓时间点
                            if ((deferHour * 3600 + deferMinute * 60 + deferSecond) >= (Integer.parseInt(hours) * 3600 + Integer.parseInt(minute) * 60 + Integer.parseInt(second))) {
                                nextProid.append(";" + product + "-" + timeString);
                            } else {
                                //否则判断是否还有后续元素，如果是最后一个的话，取第一个元素
                                if (!it.hasNext()) {
                                    nextProid.append(";" + product + "-" + tree.first());
                                }
                            }
                        }

                        if (nextProid.indexOf(product) < 0) {
                            logger.info("未成功匹配品种：{} 的清仓时间点配置", product);
                        } else {
                            // 拼接产品，逗号分隔
                            deferPro.append("," + product);
                        }
                    }
                }

                if (deferPro.indexOf(",") < 0 || nextProid.indexOf(";") < 0) {
                    logger.info("本次递延费扣除未成功匹配任何数据");
                } else {
                    String futureType = deferPro.toString().replaceFirst(",", "");
                    String nextPeriod = nextProid.toString().replaceFirst(";", "");

                    NextPeriodRateCaluate nextPeroid = new NextPeriodRateCaluate(futureType, nextPeriod);
                    nextPeroid.setDeferServiceImpl(deferServiceImpl);
                    nextPeroid.setFundAccountService(fundAccountService);
                    nextPeroid.setOrderService(orderService);
                    nextPeroid.setSmsService(smsService);
                    nextPeroid.setUserLogServiceImpl(userLogServiceImpl);
                    nextPeroid.setProductDeferRunRecordService(productDeferRunRecordService);
                    nextPeroid.setCoinPositionSumService(coinPositionSumService);
                    nextPeroid.run();
                }

                Set<String> clone = new HashSet<String>();
                clone.addAll(prdSet);
                deferTimeMap.remove(key);
                deferTimeMap.put(key, clone);

                Iterator<String> iterator = deferTimeMap.keySet().iterator();
                String nextTimeKey = iterator.next();
                String[] result = nextTimeKey.split(":");
                int nextDeferHour = Integer.parseInt(result[0]);
                int nextDeferMinute = Integer.parseInt(result[1]);
                int nextDeferSecond = Integer.parseInt(result[2]);

                // 延迟指定时间后触发清仓(补充10秒)
                Calendar calendar = Calendar.getInstance();
                int nowSecond = calendar.get(Calendar.SECOND);
                int nowHour = calendar.get(Calendar.HOUR_OF_DAY);
                int nowminute = calendar.get(Calendar.MINUTE);
                int diffHour = nextDeferHour - nowHour;
                int diffMinute = nextDeferMinute - nowminute;
                int diffSecond = nextDeferSecond - nowSecond;
                diffHour = (diffHour < 0) ? diffHour + 24 : diffHour;
                int delayTime = diffHour * 3600 + diffMinute * 60 - diffSecond;
                deferFuture_ = deferScheduler.schedule(new DeferTimerLoopTask(result[0], result[1], result[2]), delayTime, TimeUnit.SECONDS);

            } else {
                logger.info("在时间" + hours + ":" + minute + ":" + second + "未执行定时扣除递延费任务");
            }
        }

    }

    public void startUpDeferTimerLoop() {

        logger.info("初始化递延执行时间");
        deferTimeMap.clear();

        Map<String, Set<String>> tmp = deferServiceImpl.getDeferTimeProduct();

        if (tmp != null) {
            deferTimeMap.putAll(tmp);
        }

        logger.info("从数据库获取递延执行时间：{}", JSONUtils.toJSONString(deferTimeMap));

        Map<String, TreeSet<String>> tmpTree = deferServiceImpl.getProductClearTime();

        if (tmpTree != null) {
            productDeferTime.putAll(tmpTree);
        }

        logger.info("从数据库获取品种递延清仓时间：{}", JSONUtils.toJSONString(productDeferTime));

        logger.info("启动递延定时逻辑");

        Set<String> deferTimeSet = deferTimeMap.keySet();

        TreeSet<String> rollTime = new TreeSet<String>();

        // 查找第一个清仓时间节点
        Calendar calendar = Calendar.getInstance();
        int nowHour = calendar.get(Calendar.HOUR_OF_DAY);
        int nowMinute = calendar.get(Calendar.MINUTE);
        int nowSecond = calendar.get(Calendar.SECOND);

        int deferHour = 0, deferMinute = 0, deferSecond = 0;
        String deferTimer = "";

        for (String timer : deferTimeSet) {
            String[] result = timer.split(":");
            deferHour = Integer.parseInt(result[0]);
            deferMinute = Integer.parseInt(result[1]);
            deferSecond = Integer.parseInt(result[2]);
            if ((deferHour * 3600 + deferMinute * 60 + deferSecond) > (nowHour * 3600 + nowMinute * 60 + nowSecond)) {
                logger.info("获取第一个递延时间节点: " + timer);
                deferTimer = timer;
                break;
            } else {
                rollTime.add(timer);
            }
        }

        for (String timer : rollTime) {
            Set<String> deferTime = deferTimeMap.get(timer);
            deferTimeMap.remove(timer);
            deferTimeMap.put(timer, deferTime);
        }

        //刷新执行时间
        nowHour = calendar.get(Calendar.HOUR_OF_DAY);
        nowMinute = calendar.get(Calendar.MINUTE);
        nowSecond = calendar.get(Calendar.SECOND);

        int diffHour = deferHour - nowHour;
        int diffMinute = deferMinute - nowMinute;
        int diffSecond = deferSecond - nowSecond;
        diffHour = (diffHour < 0) ? diffHour + 24 : diffHour;
        int delayTime = diffHour * 3600 + diffMinute * 60 + diffSecond;
        logger.info("clearHour = {},clearMinute = {},delayTime = {},deferTimer:{}", deferHour, deferMinute, delayTime, deferTimer);
        String[] deferTimers = deferTimer.split(":");
        deferFuture_ = deferScheduler.schedule(new DeferTimerLoopTask(deferTimers[0], deferTimers[1], deferTimers[2]), delayTime, TimeUnit.SECONDS);

    }

    public void updateDeferTime() {
        logger.info("---------------开始更新递延时间配置-----------------");

        if (!deferFuture_.isCancelled()) {
            deferFuture_.cancel(true);
        }

        this.startUpDeferTimerLoop();

        logger.info("---------------结束更新时间配置----------------------");
    }
}
