package com.lt.quota.core.constant;

//import com.lt.quota.core.utils.IpUtils;

/**
 * 创建：cndym
 * 时间：2017/5/3 7:30
 */
public interface IConstants {

    String QUEUE_USER_ONLINE_LOG = "queue.user.online.log";

    String QUEUE_ORDER_MESSAGE = "queue.order.message";

    /**
     * 配置参数调整
     */
    String TOPIC_CONFIG_CHANGE = "topic.config.change";

    /**
     * 多品种订阅
     */
    String QUEUE_ORDER_MESSAGE_QUOTA_SUBSCRIPTION = "queue.order.message.quota.subscription";

    final String REDIS_PRODUCT_LAST_QUOTA = "redis:product:last:quota:";

    final String REDIS_USER_ONLINE_LIST = "redis:user:online:list";


    /**
     * K线最后一条数据
     */
    final String QUOTA_KLINE_LAST = "quota:kline:last";

    /**
     * 所有K线数据最后数据
     */
    final String QUOTA_KINE_LAST_DATA = "quota:kline:last:data";

   // final static String SERVER_IP = IpUtils.getRealIp();


    //行情相关错误：QU001 返回码提示
    public final static String QU_001 = "QU001";
    public final static String QU_002 = "QU002";
    //客户端通知退出软件
    public final static String QU_005 = "QU005";
    //下去
    public final static String QU_003 = "QU003";
    /**
     * 用户上线
     */
    public final static int OPERATE_USER_GO_LIVE_LOG = 7;

    /**
     * 用户下线
     */
    public final static int OPERATE_USER_OFFLINE_LOG = 8;

//----------------------------------新版行情系统，客户端请求命令-----------------------------------------//

    /**
     * 心跳
     */
    String REQ_PING = "1000";
    String RES_PING = "1001";

    /**
     * 登录
     */
    String REQ_AUTH = "1002";
    String RES_AUTH = "1003";


    /**
     * 行情订阅
     */
    String REQ_MKT_DATA = "1004";
    String RES_MKT_DATA = "1005";

    /**
     * 取消行情订阅
     */
    String REQ_CANCEL_MKT_DATA = "1006";
    String RES_CANCEL_MKT_DATA = "1007";

    /**
     * 登出
     */
    String REQ_LOGOUT = "1008";
    String RES_LOGOUT = "1009";

    /**
     * 通知客户端退出
     */
    String RES_CLIENT_QUIT = "1010";

    /**
     * 通知交易平仓
     */
    String RES_CLOSING_TRANSACTION = "1011";

    /**
     * 订阅所有行情
     */
    String REQ_MKT_ALL = "2000";

    int DATA_TYPE_1 = 1;
    int DATA_TYPE_5 = 5;
    int DATA_TYPE_15 = 15;
    int DATA_TYPE_30 = 30;
    int DATA_TYPE_60 = 60;

    int DATA_TYPE_DAY = 1440;
    int DATA_TYPE_WEEK = 7200;
    int DATA_TYPE_MONTH = 31680;

    /**
     * 分时图
     */
    int DATA_TYPE_FST = 0;
    /**
     * 实时行情
     */
    int DATA_TYPE_REAL = -1;


    String BTC = "BTC";
    String ETH = "ETH";
}
