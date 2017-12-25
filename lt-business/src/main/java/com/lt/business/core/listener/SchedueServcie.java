package com.lt.business.core.listener;

import com.alibaba.fastjson.JSONObject;
import com.lt.business.core.utils.KLine;
import com.lt.business.core.utils.QuotaUtils;
import com.lt.constant.redis.RedisUtil;
import com.lt.util.utils.DateTools;
import com.lt.util.utils.StringTools;
import com.lt.vo.product.KLineBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.BoundHashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@Component
public class SchedueServcie implements InitializingBean {

    private Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    /**
     * 执行k线记录生成
     */
    public void exec() {
        long i = new Date().getTime() % (60 * 1000);
        timer(1, i);
        i = new Date().getTime() % (5 * 60 * 1000);
        timer(5, i);
        i = new Date().getTime() % (15 * 60 * 1000);
        timer(15, i);
        i = new Date().getTime() % (30 * 60 * 1000);
        timer(30, i);
        i = new Date().getTime() % (60 * 60 * 1000);
        timer(60, i);

    }

    public void execCreateDay() {
        Calendar calendar = Calendar.getInstance();
        int hour = calendar.get(Calendar.HOUR_OF_DAY);//当前时间
        //判断当前时区，如果是冬令时执行6点定时任务，如果是夏令时执行5点定时任务
        BoundHashOperations<String, String, String> sysCfgRedis = redisTemplate.boundHashOps(RedisUtil.SYS_CONFIG);
        String winter_summer_change = sysCfgRedis.get("winter_summer_change");//1：夏令时，2：冬令时
        if ("1".equals(winter_summer_change) && hour == 5) {
            execute();
        } else if ("2".equals(winter_summer_change) && hour == 6) {
            execute();
        }
    }

    private void execute(){
        Date now = new Date();
        logger.info("执行创建日K 时间:{}", DateTools.parseToDefaultDateTimeString(now));
        try {
            Date date = new Date();
            date = DateTools.toDefaultDate(DateTools.parseToDefaultDateString(date));
            //执行日K生成
            createKLine(1440);

            //如果今天是周6 生成周K
            if (DateTools.getWeekNumber(date) == 6) {
                createKLine(1440 * 5);
            }
            //如果今天是本月第一天  生成月K,
            Date firstDayOfMonth = DateTools.firstDayOfMonth();
            if (firstDayOfMonth.getTime() == date.getTime()) {
                createKLine(1440 * 22);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    ScheduledExecutorService service = Executors.newScheduledThreadPool(10);

    private void timer(final int a, long i) {
        Runnable runnable = new Runnable() {
            public void run() {
                logger.info("执行生成K线图");
                createKLine(a);
            }
        };
        // 第二个参数为首次执行的延时时间，第三个参数为定时执行的间隔时间
        logger.info("initialDelay = {} , period = {}", a * 60 - i / 1000, 60 * a);
        service.scheduleAtFixedRate(runnable, a * 60 - i / 1000, 60 * a, TimeUnit.SECONDS);
    }


    /**
     * 写入K线图
     *
     * @param type
     */
    private void createKLine(int type) {
        logger.info("map:" + JSONObject.toJSONString(KLine.kLineMap));
        try {
            Map<String, KLineBean> beanMap = KLine.kLineMap.get(type);
            if (StringTools.isNotEmpty(beanMap)) {
                for (Map.Entry<String, KLineBean> entry : beanMap.entrySet()) {
                    KLineBean bean = entry.getValue();
                    logger.info("bean:" + JSONObject.toJSONString(bean));
                    // 执行
                    bean = processTime(entry.getValue(), type);
                    QuotaUtils.wirteFileK(JSONObject.toJSONString(bean), StringTools.formatStr(type + ""), bean.getProductName());
                    KLine.clearProductKLineByType(type, bean.getProductName());
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 处理时间
     *
     * @return
     */
    private KLineBean processTime(KLineBean bean, int type) throws ParseException {
        Date dates = null;
        String time = bean.getTimeStamp();
        if (!StringTools.isNotEmpty(time)) {
            return bean;
        }
        if (time.contains("-")) {
            dates = DateTools.toDefaultDateTime(time);
        } else {
            dates = new Date(Long.valueOf(time).longValue());
        }

        Date date = null;
        switch (type) {
            case 1:
                date = DateTools.addMinute(dates, -1);
                date = parseDate(date);
                bean.setTimeStamp(DateTools.parseToDefaultDateTimeString(date));
                logger.info("时间：" + bean.getTimeStamp());
                break;
            case 5:
                date = DateTools.addMinute(dates, -5);
                date = parseDate(date);
                bean.setTimeStamp(DateTools.parseToDefaultDateTimeString(date));
                break;
            case 15:
                date = DateTools.addMinute(dates, -15);
                date = parseDate(date);
                bean.setTimeStamp(DateTools.parseToDefaultDateTimeString(date));
                break;
            case 30:
                date = DateTools.addMinute(dates, -30);
                date = parseDate(date);
                bean.setTimeStamp(DateTools.parseToDefaultDateTimeString(date));
                break;
            case 60:
                date = DateTools.addMinute(dates, -60);
                date = parseDate(date);
                bean.setTimeStamp(DateTools.parseToDefaultDateTimeString(date));
                break;
            case 1440:
                date = DateTools.addDay(dates, -1);
                date = parseDate(date);
                bean.setTimeStamp(DateTools.parseToDefaultDateTimeString(date));
                break;
            case 7200:
                date = DateTools.addDay(dates, -7);
                date = parseDate(date);
                bean.setTimeStamp(DateTools.parseToDefaultDateTimeString(date));
                break;
            case 1440 * 22:
                date = DateTools.addMonth(dates, -1);
                date = parseDate(date);
                bean.setTimeStamp(DateTools.parseToDefaultDateString(date));
                break;

            default:
                break;
        }
        return bean;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        exec();
    }

    /**
     * 秒数大于10的 全部加1分钟
     * 如2017-08-16 15:43:21,973 因为 21>=10 所以2017-08-16 15:44:00,000
     * 如2017-08-16 15:43:09,973 因为 9<10 所以2017-08-16 15:43:00,000
     *
     * @param date
     * @return
     */
    public Date parseDate(Date date) {
        long time = date.getTime();
        long time1 = 0;
        long t = time / 1000 % 60;
        if (t >= 10) {
            time1 = (time / 1000 / 60 + 1) * 1000 * 60;
        } else {
            time1 = (time / 1000 / 60) * 1000 * 60;
        }
        return new Date(time1);
    }

    public static void main(String[] args) {
        try {
            long l = System.currentTimeMillis();
            System.out.println(l);
            Date date = DateTools.toDefaultDateTime("2017-10-09 13:46:00");
            System.out.println(date);
            date = DateTools.toDefaultDateTime(l + "");
            System.out.println(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }
}
