package com.lt.util.error;

import java.util.concurrent.ConcurrentHashMap;

import com.lt.util.utils.model.Response;

public class LTResponseCode {
    public static final String ER400 = "400";

    public static final String SUCCESS = "200";

    /**
     * 银生宝充值返回调用接口--异步执行失败
     */
    public static final String UNSPAY_ERROR_00001 = "UNSPAY_ERROR_00001";

    /**
     * 签名失败
     */
    public static final String SIGN_FAILED = "SF00001";

    private static ConcurrentHashMap<String, Response> respMap = new ConcurrentHashMap<String, Response>();
//	返回编码定义标准：
    /*-------------------开始：用户相关提示码定义 用户US，功能：2位，类型：1提示 0异常，编号：2位---------------------*/
    /**
     * 注册提示：操作数据库异常
     */
    public static final String US00001 = "US00001";
    /**
     * 注册提示：操作日志异常
     */
    public static final String US00002 = "US00002";
    /**
     * 注册提示：数据库修改失败
     */
    public static final String US00003 = "US00003";
    /**
     * 注册提示：程序异常
     */
    public static final String US00004 = "US00004";
    /**
     * 注册提示：用户已存在
     */
    public static final String US01101 = "US01101";
    /**
     * 注册提示：手机号码格式错误
     */
    public static final String US01102 = "US01102";
    /**
     * 注册提示：校验通信密令失败
     */
    public static final String US01103 = "US01103";
    /**
     * 注册提示：该手机号今日获取验证码达到上限
     */
    public static final String US01104 = "US01104";
    /**
     * 注册提示：用户不存在
     */
    public static final String US01105 = "US01105";
    /**
     * 注册提示：获取验证码失败
     */
    public static final String US01106 = "US01106";
    /**
     * 注册提示：发送短信异常
     */
    public static final String US01006 = "US01006";
    /**
     * 注册提示：验证码不能为空
     */
    public static final String US01107 = "US01107";
    /**
     * 注册提示：今日验证码校验已超过最大次数
     */
    public static final String US01108 = "US01108";
    /**
     * 注册提示：验证码错误
     */
    public static final String US01109 = "US01109";
    /**
     * 注册提示：密码不能为空
     */
    public static final String US01110 = "US01110";
    /**
     * 注册提示：密码格式错误
     */
    public static final String US01111 = "US01111";
    /**
     * 注册提示：账号正在处理中
     */
    public static final String US01112 = "US01112";
    /**
     * 注册提示：积分账户建立失败
     */
    public static final String US01113 = "US01113";
    /**
     * token过期或者无效,请重新登录
     */
    public static final String US01114 = "US01114";
    /**
     * token库找不到对应token，可能已在其他设备登录
     */
    public static final String US01115 = "US01115";
    /**
     * 账户异常，请联系客服
     */
    public static final String US01116 = "US01116";
    /**
     * 网络异常
     */
    public static final String USJ0000 = "USJ0000";

    /**
     * 登陆提示：用户状态异常
     */
    public static final String US02101 = "US02101";
    /**
     * 登陆提示：密码不匹配
     */
    public static final String US02102 = "US02102";
    /**
     * 登陆提示：密码次数超过最大限制
     */
    public static final String US02103 = "US02103";
    /**
     * 修改信息提示：缺失必填参数
     */
    public static final String US03101 = "US03101";
    /**
     * 修改信息提示：修改内容包含敏感词
     */
    public static final String US03102 = "US03102";
    /**
     * 修改信息提示：修改内容长度不符合要求
     */
    public static final String US03103 = "US03103";
    /**
     * 修改信息提示：修改内容包含非法字符
     */
    public static final String US03104 = "US03104";
    /**
     * 修改信息提示：昵称已被使用
     */
    public static final String US03105 = "US03105";
    /**
     * 修改信息提示：昵称已被修改一次
     */
    public static final String US03106 = "US03106";
    /**
     * 修改信息提示：新密码不可为空
     */
    public static final String US03107 = "US03107";
    /**
     * 修改信息提示：旧密码不可为空
     */
    public static final String US03108 = "US03108";
    /**
     * 用户业务处理：用户服务不存在
     */
    public static final String US04101 = "US04101";
    /**
     * 用户业务处理： 信息已存在
     */
    public static final String US04102 = "US04102";
    /**
     * 请重新选择证券账号
     */
    public static final String US01117 = "US01117";
    /**
     * 用户已加入黑名单
     */
    public static final String US01118 = "US01118";
    /**
     * 身份证号码不能为空
     */
    public static final String US01119 = "US01119";
    /**
     * 开户步骤不能为空
     */
    public static final String US01120 = "US01120";
    /**
     * 开户-银行卡验证
     **/
    public static final String US01121 = "US01121";
    /**
     * 开户异常
     **/
    public static final String US01122 = "US01122";

    /**
     * 银行卡验证剩余次数为0
     **/
    public static final String US01123 = "US01123";
    /**
     * 用户年龄不满20周岁
     **/
    public static final String US01124 = "US01124";
    /**
     * 已在其他设备登录，请重新登录
     **/
    public static final String US01125 = "US01125";
    /**
     * 您最多可以绑定三张银行卡，如需更换其他银行卡请先删除或解绑！
     */
    public static final String US05001 = "US05001";

    /**
     * 绑定银行卡重复
     **/
    public static final String US05002 = "US05002";

    /**
     * 编辑银行卡信息失败
     */
    public static final String US20000 = "US20000";
    /**
     * 缺少必要信息ID
     */
    public static final String US20001 = "US20001";
    /**
     * 该卡目前不支持充值，请切换其他银行卡
     */
    public static final String USY0001 = "USY0001";
    /**
     * 系统参数未配置
     */
    public static final String USY0002 = "USY0002";
    /**
     * 未匹配到充值渠道
     */
    public static final String USY0003 = "USY0003";
    /**
     * 当前不支持充值
     */
    public static final String USY0004 = "USY0004";
    /**
     * 超过平台限额提示：已超今日限额，请明日再试
     */
    public static final String USY0005 = "USY0005";
    /**
     * 今日充值已到达上限 请明天再来么
     **/
    public static final String USY0006 = "USY0006";

    /**
     * 没有满足条件的证券账户
     */
    public static final String USL2000 = "USL2000";
    /**
     * 实名审核状态不一致
     */
    public static final String USL2002 = "USL2002";
    /**
     * 系统未执行任何数据操作
     */
    public static final String USL2003 = "USL2003";
    /**
     * 实名制审核成功不允许更改状态
     */
    public static final String USL2004 = "USL2004";
    /**
     * 审核失败不允许在平台更改状态
     */
    public static final String USL2005 = "USL2005";
    /**
     * 使用中的标签不允许删除
     */
    public static final String USL2006 = "USL2006";
    /**
     * 已存在的标签不允许在添加
     */
    public static final String USL2007 = "USL2007";
    /**
     * 校验密码次数超限
     */
    public static final String USJ2001 = "USJ2001";
    /**
     * 用户已激活 btc 功能
     */
    public static final String USJ2002 = "USJ2002";

	/*-------------------结束：用户相关提示码定义 用户US，功能：2位，类型：1提示 2异常，编号：2位---------------------*/
    /**
     * 提示：请重新选择券商
     */
    public static final String IV00001 = "IV00001";
    /**
     * 券商业务处理：费用配置异常
     */
    public static final String IV00002 = "IV00002";
    /**
     * 券商ID不能为空
     */
    public static final String IV00003 = "IV00003";
    /**
     * 止盈止损金额超出平台范围，请重新设置
     */
    public static final String IV00004 = "IV00004";

//	商品相关错误：PR001  返回码提示
    /*-----------------商品相关错误返回码以PR开头开始---------------------------------*/

    public static final String PR001 = "PR001";
    /**
     * 参数缺失
     */
    public static final String PR00001 = "PR00001";
    /**
     * 商品未查询到
     */
    public static final String PR00002 = "PR00002";
    /**
     * 信息没有任何改变
     */
    public static final String PR00003 = "PR00003";
    /**
     * 商品类型未找到
     */
    public static final String PR00004 = "PR00004";
    /**
     * 商品不可买入
     */
    public static final String PR00005 = "PR00005";
    /**
     * 商品不可卖出
     */
    public static final String PR00006 = "PR00006";
    /**
     * 商品不可买卖
     */
    public static final String PR00007 = "PR00007";
    /**
     * 商品未上架
     */
    public static final String PR00008 = "PR00008";
    /**
     * 商品已下架
     */
    public static final String PR00009 = "PR00009";
    /**
     * 商品清仓时间错误
     */
    public static final String PR00010 = "PR00010";
    /**
     * 产品波动点位错误
     */
    public static final String PR00011 = "PR00011";
    /**
     * 产品波动价格错误
     */
    public static final String PR00012 = "PR00012";
    /**
     * 数据已被商品占用,无法删除
     */
    public static final String PR00013 = "PR00013";
    /**
     * 商品名称不能为空
     */
    public static final String PR00014 = "PR00014";
    /**
     * 商品到期时间不能为空
     */
    public static final String PR00015 = "PR00015";
    /**
     * 商品即将到达交割日，不允许开启递延
     */
    public static final String PR00016 = "PR00016";

    /**
     * 商品已暂停交易
     */
    public static final String PR00017 = "PR00017";

    /**
     * 未获取到商品时间配置信息
     */
    public static final String PRJ0001 = "PRJ0001";
    /**
     * 商品已存在
     */
    public static final String PRJ0002 = "PRJ0002";
    /**
     * 交易时间配置有误
     */
    public static final String PRJ0003 = "PRJ0003";
    /**
     * 商品在售中，不允许修改商品代码
     */
    public static final String PRJ0004 = "PRJ0004";
    /**
     * 商品在售中，不允许删除
     */
    public static final String PRJ0005 = "PRJ0005";
    /**
     * 商品被订单占用，不可下架
     */
    public static final String PRJ0006 = "PRJ0006";
    /**
     * 该商品不支持递延
     */
    public static final String PRJ0007 = "PRJ0007";
	
	
	/*-----------------商品相关错误返回码，结束---------------------------------*/
    /**
     * 商品不存在
     */
    public static final String PRO0001 = "PRO0001";
    /**
     * 参数不在范围内
     */
    public static final String PRO0002 = "PRO0002";
	/*---------------------推广提示码，开始-----------------------------*/
    /**
     * 推广等级未配置
     */
    public static final String PROMJ0001 = "PROMJ0001";
    /**
     * 参数缺失
     */
    public static final String PROMJ0002 = "PROMJ0002";
    /**
     * 未获取到推广员信息
     */
    public static final String PROMJ0003 = "PROMJ0003";
    /**
     * 佣金账户不存在
     */
    public static final String PROMJ0004 = "PROMJ0004";
    /**
     * 佣金存取明细不存在
     */
    public static final String PROMJ0005 = "PROMJ0005";
    /**
     * 状态已发生变化
     */
    public static final String PROMJ0006 = "PROMJ0006";
    /**
     * 佣金流转方式不存在
     */
    public static final String PROMJ0007 = "PROMJ0007";
    /**
     * 参数异常
     */
    public static final String PROMJ0008 = "PROMJ0008";
    /**
     * 佣金余额不足
     */
    public static final String PROMJ0009 = "PROMJ0009";
    /**
     * 推广等级已被占用
     */
    public static final String PROMJ0010 = "PROMJ0010";
    /**
     * 推广等级已存在
     */
    public static final String PROMJ0011 = "PROMJ0011";
    /**
     * 已是推广员
     */
    public static final String PROMJ0012 = "PROMJ0012";
    /**
     * 请先激活推广员
     */
    public static final String PROMJ0013 = "PROMJ0013";
    /**
     * 已是该用户的上线，无需修改
     */
    public static final String PROMJ0014 = "PROMJ0014";
    /**
     * 不可互相推广
     */
    public static final String PROMJ0015 = "PROMJ0015";
    /**
     * 不可将自己作为上线
     */
    public static final String PROMJ0016 = "PROMJ0016";
    /**
     * 无上线历史，无须恢复
     */
    public static final String PROMJ0017 = "PROMJ0017";
	/*---------------------推广提示码，结束-----------------------------*/

//	订单相关错误：OR001  返回码提示
//	交易相关错误：TR001  返回码提示
	/*--------------------------交易相关错误：TR001  返回码提示----------------------------*/
    /**
     * 参数缺失
     */
    public static final String TRJ0000 = "TRJ0000";

    /**
     * 该时间段无法执行修改操作
     */
    public static final String TRY0000 = "TRY0000";

//	策略相关错误：PO001 返回码提示
	
	
	/*----------------------资金相关错误码定义以FU开头，开始--------------------------------------------*/
    /**
     * 系统异常
     */
    public static final String FU00000 = "FU00000";
    /**
     * 请先绑定银行卡
     */
    public static final String FU00001 = "FU00001";
    /**
     * 用户资金账户不存在
     */
    public static final String FU00002 = "FU00002";
    /**
     * 参数缺失
     */
    public static final String FU00003 = "FU00003";
    /**
     * 修改资金账户信息失败
     */
    public static final String FU00004 = "FU00004";
    /**
     * 下单支付失败
     */
    public static final String FU00005 = "FU00005";
    /**
     * 退款失败
     */
    public static final String FU00006 = "FU00006";
    /**
     * 用户现金主账户未初始化或账户余额是0!
     */
    public static final String FU00007 = "FU00007";
    /**
     * 用户现金账户余额不足
     */
    public static final String FU00008 = "FU00008";
    /**
     * 券商现金主账户未初始化
     */
    public static final String FU00009 = "FU00009";
    /**
     * 券商现金账户余额不足
     */
    public static final String FU00010 = "FU00010";
    /**
     * 用户积分主账户未初始化
     */
    public static final String FU00011 = "FU00011";
    /**
     * 用户积分账户余额不足
     */
    public static final String FU00012 = "FU00012";
    /**
     * 用户资金账户已存在
     */
    public static final String FU00013 = "FU00013";
    /**
     * 用户姓名或者银行卡号为空
     */
    public static final String FU00014 = "FU00014";


    /**
     * 用户余额不足，操作失败
     */
    public static final String FUY00001 = "FUY00001";
    /**
     * 资金sql报错
     */
    public static final String FUY00002 = "FUY00002";
    /**
     * 补单功能异常
     */
    public static final String FUY00003 = "FUY00003";
    /**
     * 生成支付宝充值链接失败
     */
    public static final String FUY00004 = "FUY00004";
    /**
     * 快钱最少充值金额50
     */
    public static final String FUY00005 = "FUY00005";
    /**
     * 快钱 第三方未查询到充值记录
     */
    public static final String FUY00006 = "FUY00006";
    /**
     * 快钱 未查询到充值记录
     */
    public static final String FUY00007 = "FUY00007";
    /**
     * 快钱充值异常（非程序异常）  统一返回码
     */
    public static final String FUY00008 = "FUY00008";
    /**
     * 超过单日入金限额
     */
    public static final String FUY00009 = "FUY00009";
    /**
     * 转账已成功，不可重复转账
     */
    public static final String FUY00010 = "FUY00010";
    /**
     * 数据已操作，不可重复操作
     */
    public static final String FUY00011 = "FUY00011";
    /**
     * 银生宝参数异常
     */
    public static final String FUY00012 = "FUY00012";
    /**
     * 请求充值接口异常
     */
    public static final String FUY00013 = "FUY00013";
    /**
     * 美元人民币不匹配
     */
    public static final String FUY00014 = "FUY00014";
    /**
     * 生成daddyPay充值链接失败
     */
    public static final String FUY00015 = "FUY00015";
    /**
     * 未获取到业务码
     */
    public static final String FUJ0001 = "FUJ0001";
    /**
     * 亲，最小提现金额为20元
     */
    public static final String FUJ0002 = "FUJ0002";
    /**
     * 亲，单日限提2次哦！
     */
    public static final String FUJ0003 = "FUJ0003";
    /**
     * 未获取到出金流水
     */
    public static final String FUJ0004 = "FUJ0004";
    /**
     * 该出金流水状态已发生变化，不可撤销
     */
    public static final String FUJ0005 = "FUJ0005";
    /**
     * 出金次数已用完
     **/
    public static final String FUJ0006 = "FUJ0006";
	
	/*----------------------资金相关错误码定义结束--------------------------------------------*/
    /**
     * 下单手数应该大于0
     */
    public static final String TD00001 = "TD00001";
    /**
     * 订单方向有误
     */
    public static final String TD00002 = "TD00002";
    /**
     * 目前只支持现金和积分交易
     */
    public static final String TD00003 = "TD00003";
    /**
     * 订单不存在
     */
    public static final String TD00004 = "TD00004";
    /**
     * 订单缓存数据不存在
     */
    public static final String TD00005 = "TD00005";
    /**
     * 交易接口获取失败
     */
    public static final String TD00006 = "TD00006";
    /**
     * 委托消息发送失败
     */
    public static final String TD00007 = "TD00007";
    /**
     * 订单的买入方向错误
     */
    public static final String TD00008 = "TD00008";
    /**
     * 订单存入缓存失败
     */
    public static final String TD00009 = "TD00009";
    /**
     * 订单持仓数据缓存失败
     */
    public static final String TD00010 = "TD00010";
    /**
     * 委托单缓存失败
     */
    public static final String TD00011 = "TD00011";
    /**
     * 订单扣款记录缓存失败
     */
    public static final String TD00012 = "TD00012";
    /**
     * 订单已平仓
     */
    public static final String TD00013 = "TD00013";
    /**
     * 止损保证金不能小于0
     */
    public static final String TD00014 = "TD00014";
    /**
     * 止盈保证金不能小于0
     */
    public static final String TD00015 = "TD00015";
    /**
     * 下单手数不能大于最大单次开仓量
     */
    public static final String TD00016 = "TD00016";


    /**
     * 未来有长假期，禁止开启递延功能
     */
    public static final String TDY0001 = "TDY0001";
    /**
     * 止盈区间不符合设置
     */
    public static final String TDY0002 = "TDY0002";
    /**
     * 止损区间不符合设置
     */
    public static final String TDY0003 = "TDY0003";
    /**
     * 设置的止损金额过小
     */
    public static final String TDY0004 = "TDY0004";
    /**
     * 即将到交割日，不能开启递延功能
     */
    public static final String TDY0005 = "TDY0005";
    /**
     * 不在交易时段，已闭市
     */
    public static final String TDY0006 = "TDY0006";
    /**
     * 您当前可设置的最低止损金额为{},请重新设置
     */
    public static final String TDY0007 = "TDY0007";

    /**
     * 不满足条件
     */
    public static final String TDJ0001 = "TDJ0001";
    /**
     * 不可重复下单
     */
    public static final String TDJ0002 = "TDJ0002";
    /**
     * 当前已无持仓中订单
     */
    public static final String TDJ0003 = "TDJ0003";
    /**
     * 您当前持仓已超过最大持仓量
     */
    public static final String TDJ0004 = "TDJ0004";
     /**
     * 当前此商品的系统持仓数已超过最大限制
     */
    public static final String TDJ0005 = "TDJ0005";

	/*----------------------积分相关错误码定义开始--------------------------------------------*/
    /**
     * 积分功能开发中
     */
    public static final String SC00001 = "SC00001";
	/*----------------------积分相关错误码定义结束--------------------------------------------*/

    //	管理系统相关：MA001 返回码提示
//	行情相关错误：QU001 返回码提示
    public static final String QU001 = "QU001";
    public static final String QU002 = "QU002";
    public static final String QU003 = "QU003";
    //客户端通知退出软件
    public static final String QU005 = "QU005";
//	系统相关错误：SY001 返回码提示
//	配置相关错误：CO001 返回码提示
	
	/*---------------------订单相关返回值------------------------------*/
    /**
     * 用户查询持仓数量，未能解析出会话用户
     */
    public static final String OR10001 = "OR10001";
    /**
     * 获取资金类型异常
     */
    public static final String OR10002 = "OR10002";

    /**
     * 查询用户持仓订单数量异常
     */
    public static final String OR10003 = "OR10003";
    /**
     * 用户不存在
     */
    public static final String OR10004 = "OR10004";
    /**
     * 参数为空
     */
    public static final String OR10005 = "OR10005";
	/*----------------------Redis连接异常定义开始-----------------------*/
    /**
     * Redis连接异常
     */
    public static final String RD00001 = "RD00001";
    /**
     * RedisTemplate配置异常
     */
    public static final String RD00002 = "RD00002";
	
	/*----------------------新闻、策略资讯相关错误码定义开始--------------------------------------------*/
    /**
     * 新闻标识错误
     */
    public static final String NA00001 = "NA00001";
    /**
     * 评论失败，请稍后再试
     */
    public static final String NA00002 = "NA00002";
    /**
     * 评论失败，用户标识丢失
     */
    public static final String NA00003 = "NA00003";
    /**
     * 评论过于频繁，请稍后再试
     */
    public static final String NA00004 = "NA00004";
    /**
     * 评论内容不可超过500字
     */
    public static final String NA00005 = "NA00005";
    /**
     * 评论成功
     */
    public static final String NA00006 = "NA00006";
    /**
     * 回复过于频繁，请稍后再试
     */
    public static final String NA00007 = "NA00007";
    /**
     * 评论或回复内容不可为空
     */
    public static final String NA00008 = "NA00008";
    /**
     * 回复内容不可超过500字
     */
    public static final String NA00009 = "NA00009";
    /**
     * 评论标识错误
     */
    public static final String NA00010 = "NA00010";
    /**
     * 不可回复自己的评论
     */
    public static final String NA00011 = "NA00011";
    /**
     * 不可对自己回复
     */
    public static final String NA00012 = "NA00012";
    /**
     * 回复成功
     */
    public static final String NA00013 = "NA00013";
    /**
     * 服务异常，评论失败
     */
    public static final String NA00014 = "NA00014";
    /**
     * 未找到对应资源
     */
    public static final String NA00015 = "NA00015";
    /**
     * 已存在栏目，添加失败
     */
    public static final String NAY00001 = "NAY00001";
    /**
     * 栏目信息已存在使用，删除失败
     */
    public static final String NAY00002 = "NAY00002";
    /**
     * 已存在标签，添加失败
     */
    public static final String NAY00003 = "NAY00003";
    /**
     * 标签信息已存在使用，删除失败
     */
    public static final String NAY00004 = "NAY00004";
	/*----------------------新闻、策略资讯相关错误码定义结束--------------------------------------------*/
	
	
	/*--------------------后台管理系统对应编码------------------------------------*/
    /**
     * 会话过期，请重新登录
     */
    public static final String MA00001 = "MA00001";
    /**
     * 访问被阻止，权限不够
     */
    public static final String MA00002 = "MA00002";
    /**
     * 未登录，请登录后操作
     */
    public static final String MA00003 = "MA00003";
    /**
     * 参数不能为空  或者  参数类型不匹配
     */
    public static final String MA00004 = "MA00004";
    /**
     * 用户名含有非法字符
     */
    public static final String MA00005 = "MA00005";
    /**
     * 密码不可为空
     */
    public static final String MA00006 = "MA00006";
    /**
     * 用户不存在
     */
    public static final String MA00007 = "MA00007";
    /**
     * 错误次数已超过5次，已被禁用，请联系后台管理员
     */
    public static final String MA00008 = "MA00008";
    /**
     * 登录失败，已被禁用
     */
    public static final String MA00009 = "MA00009";
    /**
     * 登录失败，用户密码错误
     */
    public static final String MA00010 = "MA00010";
    /**
     * 用户角色未分配
     */
    public static final String MA00011 = "MA00011";
    /**
     * 密码长度需要在6~20位之间
     */
    public static final String MA00012 = "MA00012";
    /**
     * 密码不一致
     */
    public static final String MA00013 = "MA00013";
    /**
     * 该账号已被注册
     */
    public static final String MA00014 = "MA00014";
    /**
     * 参数唯一
     */
    public static final String MA00015 = "MA00015";
    /**
     * 时间转换异常
     */
    public static final String MA00016 = "MA00016";
    /**
     * 查询信息为空
     */
    public static final String MA00017 = "MA00017";
    /**
     * 删除操作权限失败,存在角色权限关联关系
     */
    public static final String MA00018 = "MA00018";
    /**
     * IP地址验证未通过
     */
    public static final String MA00019 = "MA00019";
    /**
     * 短信验证码错误，请重试
     */
    public static final String MA00020 = "MA00020";
    /**
     * ip白名单验证Sql异常，运营的妹妹叫声哥哥就改
     */
    public static final String MA00021 = "MA00021";
    /**
     * 同一个账户仅可在一个设备登录
     */
    public static final String MA00022 = "MA00022";
    /**
     * 登录验证码拦截处理Sql异常，运营的妹妹叫声哥哥就改
     */
    public static final String MA00023 = "MA00023";
    /**
     * 登录失败，账户已被禁用
     */
    public static final String MA00024 = "MA00024";

    /**
     * 验证码不能为空
     */
    public static final String MA00025 = "MA00025";
    /**
     * 权限分配异常
     */
    public static final String MA00026 = "MA00026";


    //品牌编码已存在
    public static final String MA00027 = "MA00027";


    //此品牌不存在
    public static final String MA00028 = "MA00028";

    //此品牌下有渠道，不可删除
    public static final String MA00029 = "MA00029";

    //渠道需指定所属品牌，请选择一个品牌
    public static final String MA00030 = "MA00030";

    //操作失败
    public static final String MA00031 = "MA00031";


    //渠道编码已存在
    public static final String MA00032 = "MA00032";


    /**
     * 已存在相关类型的权重广告
     */
    public static final String ADY0001 = "ADY0001";
    /**
     * 广告图删除异常
     */
    public static final String ADY0002 = "ADY0002";
    /**
     * 内容获取异常
     */
    public static final String ADY0003 = "ADY0003";

    /**
     * 通用
     * 查询结果为空
     */
    public static final String GE00001 = "GE00001";
    /**
     * 上传文件过大
     */
    public static final String F000001 = "F000001";
    static {
        respMap.put(SUCCESS, new Response(SUCCESS, "操作成功"));
        respMap.put(ER400, new Response(ER400, "自定义错误"));
        respMap.put(QU001, new Response(QU001, "注册验证参数"));
        respMap.put(PR001, new Response(PR001, "商品修改市场状态异常"));
        respMap.put(QU002, new Response(QU002, "停止发送数据"));
        respMap.put(QU003, new Response(QU003, "参数为空"));
		
		/*--------------------开始：用户相关返回提示-------------------------*/
        respMap.put(US00001, new Response(US00001, "操作数据库异常"));
        respMap.put(US00003, new Response(US00003, "数据库修改异常"));
        respMap.put(US00002, new Response(US00002, "操作日志异常"));
        respMap.put(US00004, new Response(US00004, "程序异常"));
        respMap.put(US01101, new Response(US01101, "用户已存在"));
        respMap.put(US01102, new Response(US01102, "手机号格式有误"));
        respMap.put(US01103, new Response(US01103, "校验通信密令失败"));
        respMap.put(US01104, new Response(US01104, "该手机号今日获取验证码达到上限"));
        //respMap.put(US01105, new Response(US01105, "用户不存在"));
        //respMap.put(US01105, new Response(US01105, "该号码已经注册，是否登录？"));
        respMap.put(US01105, new Response(US01105, "该手机号未注册，请注册登录"));
        respMap.put(US01106, new Response(US01106, "获取验证码失败"));
        respMap.put(US01006, new Response(US01006, "发送短信方法异常"));
        respMap.put(US01107, new Response(US01107, "验证码不能为空"));
        respMap.put(US01108, new Response(US01108, "今日验证码校验已超过最大次数"));
        respMap.put(US01109, new Response(US01109, "验证码错误"));
        respMap.put(US01110, new Response(US01110, "密码不能为空"));
        respMap.put(US01111, new Response(US01111, "请输入正确格式的密码 6-20位字母、数字（特殊符号除外）"));
        respMap.put(US01112, new Response(US01112, "账号正在处理中"));
        respMap.put(US01113, new Response(US01113, "积分账户建立失败"));
        respMap.put(US02101, new Response(US02101, "用户状态异常"));
        respMap.put(US02102, new Response(US02102, "密码不匹配"));
        respMap.put(US02103, new Response(US02103, "密码次数超过最大限制"));

        respMap.put(US03101, new Response(US03101, "系统参数缺失"));
        respMap.put(US03102, new Response(US03102, "修改内容包含敏感词"));
        respMap.put(US03103, new Response(US03103, "修改内容长度不符合要求"));
        respMap.put(US03104, new Response(US03104, "修改内容包含非法字符"));
        respMap.put(US03105, new Response(US03105, "昵称已被使用"));
        respMap.put(US03106, new Response(US03106, "昵称已被修改一次"));
        respMap.put(US03107, new Response(US03107, "新密码不可为空"));
        respMap.put(US03108, new Response(US03108, "旧密码不可为空"));
        respMap.put(US04101, new Response(US04101, "用户服务不存在"));
        respMap.put(US04102, new Response(US04102, "信息已存在"));
        respMap.put(US01114, new Response(US01114, "请重新登录"));
        respMap.put(US01115, new Response(US01115, "登录超时"));
        respMap.put(US01116, new Response(US01116, "账户异常，请联系客服"));
        respMap.put(USJ0000, new Response(USJ0000, "网络异常"));
        respMap.put(US20000, new Response(US20000, "编辑银行卡信息失败"));
        respMap.put(US20001, new Response(US20001, "缺少必要信息ID"));
        respMap.put(US01117, new Response(US01117, "请重新选择券商"));
        respMap.put(US01118, new Response(US01118, "用户已加入黑名单"));
        respMap.put(US01119, new Response(US01119, "用户身份证号为空"));
        respMap.put(US01120, new Response(US01120, "开户步骤为空"));
        respMap.put(US01121, new Response(US01121, "用户银行卡验证异常"));
        respMap.put(US01122, new Response(US01122, "开户异常"));
        respMap.put(US01123, new Response(US01123, "银行卡验证剩余次数为0"));
        respMap.put(US01124, new Response(US01123, "用户年龄未满20周岁"));
        respMap.put(US01125, new Response(US01125, "已在其他设备登录，请重新登录"));
        respMap.put(USY0001, new Response(USY0001, "该卡目前不支持充值，请切换其他银行卡"));
        respMap.put(USY0002, new Response(USY0002, "未配置系统参数"));
        respMap.put(USY0003, new Response(USY0003, "未匹配充值渠道"));
        respMap.put(USY0004, new Response(USY0004, "当前不支持充值，可联系客服处理"));
        respMap.put(USY0005, new Response(USY0005, "已超今日限额，请明日再试"));
        respMap.put(USY0006, new Response(USY0006, "您今日充值次数已到达上限，请明天再来"));
        respMap.put(USL2000, new Response(USL2000, "没有满足条件的证券账户"));
        respMap.put(USL2002, new Response(USL2002, "用户实名状态不一致"));
        respMap.put(USL2003, new Response(USL2003, "系统未执行任何数据操作"));
        respMap.put(USL2004, new Response(USL2004, "实名制审核成功后不允许更改状态"));
        respMap.put(USL2005, new Response(USL2005, "审核失败不允许在平台更改成功状态"));
        respMap.put(USL2006, new Response(USL2006, "使用中的标签不允许删除"));
        respMap.put(USL2007, new Response(USL2007, "已存在的标签不允许在添加"));
        respMap.put(USJ2001, new Response(USJ2001, "校验密码次数超限"));
        respMap.put(USJ2002, new Response(USJ2002, "用户已激活 btc 功能"));

		/*--------------------结束：用户相关返回提示-------------------------*/
		
		/*----------------------券商配置相关---------------------*/

        respMap.put(IV00001, new Response(IV00001, "请重新选择券商"));
        respMap.put(IV00002, new Response(IV00002, "费用配置异常"));
        respMap.put(IV00003, new Response(IV00003, "券商ID不能为空"));
        respMap.put(IV00004, new Response(IV00004, "止盈止损金额超出平台范围，请重新设置"));
		
		
		/*----------------------券商配置相关---------------------*/
		
		/*----------------------资金相关---------------------*/
        respMap.put(FU00000, new Response(FU00000, "系统异常"));
        respMap.put(FU00001, new Response(FU00001, "请先绑定银行卡"));
        respMap.put(FU00002, new Response(FU00002, "用户资金账户不存在"));
        respMap.put(FU00003, new Response(FU00003, "参数缺失"));
        respMap.put(FU00004, new Response(FU00004, "修改资金账户信息失败"));
        respMap.put(FU00005, new Response(FU00005, "下单支付失败"));
        respMap.put(FU00006, new Response(FU00006, "退款失败"));
        respMap.put(FU00007, new Response(FU00007, "用户现金主账户未初始化"));
        respMap.put(FU00008, new Response(FU00008, "用户现金账户余额不足"));
        respMap.put(FU00009, new Response(FU00009, "券商现金主账户未初始化"));
        respMap.put(FU00010, new Response(FU00010, "券商现金账户余额不足"));
        respMap.put(FU00011, new Response(FU00011, "用户积分主账户未初始化"));
        respMap.put(FU00012, new Response(FU00012, "用户积分账户余额不足"));


        respMap.put(FUY00001, new Response(FUY00001, "用户余额不足，操作失败"));
        respMap.put(FUY00002, new Response(FUY00002, "资金数据库异常"));
        respMap.put(FUY00003, new Response(FUY00003, "补单功能异常"));
        respMap.put(FUY00004, new Response(FUY00004, "生成支付宝充值链接失败"));
        respMap.put(FUY00005, new Response(FUY00005, "最小充值金额50美元"));
        respMap.put(FUY00006, new Response(FUY00006, "未查询到充值记录"));
        respMap.put(FUY00007, new Response(FUY00007, "第三方未查询到充值记录"));
        respMap.put(FUY00008, new Response(FUY00008, "快钱充值异常统一返回"));
        respMap.put(FUY00009, new Response(FUY00009, "充值总额超过单日入金限额"));
        respMap.put(FUY00010, new Response(FUY00010, "转账已操作，不可重复转账"));
        respMap.put(FUY00011, new Response(FUY00011, "数据已操作，不可重复操作"));
        respMap.put(FUY00012, new Response(FUY00012, "银生宝参数错误"));
        respMap.put(FUY00013, new Response(FUY00013, "充值端网络异常"));
        respMap.put(FUY00014, new Response(FUY00014, "系统币种与人民币值不匹配"));
        respMap.put(FUY00015, new Response(FUY00015, "生成daddyPay充值链接失败"));
        respMap.put(FUJ0001, new Response(FUJ0001, "未获取到业务码"));
        respMap.put(FUJ0002, new Response(FUJ0002, "亲，最小提现金额为20元"));
        respMap.put(FUJ0003, new Response(FUJ0003, "亲，单日限提2次哦"));
        respMap.put(FUJ0004, new Response(FUJ0004, "未获取到出金流水"));
        respMap.put(FUJ0005, new Response(FUJ0005, "该出金流水状态已发生变化，不可撤销"));
        respMap.put(FUJ0006, new Response(FUJ0006, "亲，单日限提次数已超限"));
		
		
		/*----------------------资金相关---------------------*/
		
		/*----------------------商品相关--------------------*/
        respMap.put(PR00001, new Response(PR00001, "参数缺失"));
        respMap.put(PR00002, new Response(PR00002, "商品未查询到"));
        respMap.put(PR00003, new Response(PR00003, "信息没任何改变"));
        respMap.put(PR00004, new Response(PR00004, "商品类型未找到"));
        respMap.put(PR00005, new Response(PR00005, "商品不可买入"));
        respMap.put(PR00006, new Response(PR00006, "商品不可卖出"));
        respMap.put(PR00007, new Response(PR00007, "商品不可买卖"));
        respMap.put(PR00008, new Response(PR00008, "商品未上架"));
        respMap.put(PR00009, new Response(PR00009, "商品已下架"));
        respMap.put(PR00010, new Response(PR00010, "商品清仓时间错误"));
        respMap.put(PR00011, new Response(PR00011, "产品波动点位错误"));
        respMap.put(PR00012, new Response(PR00012, "产品波动价格错误"));
        respMap.put(PR00013, new Response(PR00013, "数据已被商品占用,无法删除"));
        respMap.put(PR00014, new Response(PR00014, "商品名称不能为空"));
        respMap.put(PR00015, new Response(PR00015, "商品到期时间不能为空"));
        respMap.put(PR00016, new Response(PR00016, "商品即将到达交割日，不允许开启递延"));
        respMap.put(PR00017, new Response(PR00017, "已达开仓限制，无法开仓"));


        respMap.put(PRJ0001, new Response(PRJ0001, "未获取到商品时间配置信息"));
        respMap.put(PRJ0002, new Response(PRJ0002, "商品已存在"));
        respMap.put(PRJ0003, new Response(PRJ0003, "交易时间配置有误"));
        respMap.put(PRJ0004, new Response(PRJ0004, "商品在售中，不允许修改商品代码"));
        respMap.put(PRJ0005, new Response(PRJ0005, "商品在售中，不允许删除"));
        respMap.put(PRJ0006, new Response(PRJ0006, "商品被订单占用，不可下架"));
        respMap.put(PRJ0007, new Response(PRJ0007, "该商品不支持递延"));
		
		/*----------------------商品相关--------------------*/
		
		/*----------------推广相关-----------------------*/

        respMap.put(PROMJ0001, new Response(PROMJ0001, "推广等级未配置"));
        respMap.put(PROMJ0002, new Response(PROMJ0002, "参数缺失"));
        respMap.put(PROMJ0003, new Response(PROMJ0003, "未获取到推广员信息"));
        respMap.put(PROMJ0004, new Response(PROMJ0004, "佣金账户不存在"));
        respMap.put(PROMJ0005, new Response(PROMJ0005, "佣金存取明细不存在"));
        respMap.put(PROMJ0006, new Response(PROMJ0006, "状态已发生变化"));
        respMap.put(PROMJ0007, new Response(PROMJ0007, "佣金流转方式不存在"));
        respMap.put(PROMJ0008, new Response(PROMJ0008, "参数异常"));
        respMap.put(PROMJ0009, new Response(PROMJ0009, "佣金余额不足"));
        respMap.put(PROMJ0010, new Response(PROMJ0010, "推广等级已被占用"));
        respMap.put(PROMJ0011, new Response(PROMJ0011, "推广等级已存在"));
        respMap.put(PROMJ0012, new Response(PROMJ0012, "已是推广员"));
        respMap.put(PROMJ0013, new Response(PROMJ0013, "请先激活推广员"));
        respMap.put(PROMJ0014, new Response(PROMJ0014, "已是该用户的上线，无需修改"));
        respMap.put(PROMJ0015, new Response(PROMJ0015, "不可互相推广"));
        respMap.put(PROMJ0016, new Response(PROMJ0016, "不可将自己作为上线"));
        respMap.put(PROMJ0017, new Response(PROMJ0017, "无上线历史，无须恢复"));
		/*----------------推广相关-----------------------*/
		
		
		/*----------------------交易相关--------------------*/
        respMap.put(TD00001, new Response(TD00001, "买入手数应该大于0"));
        respMap.put(TD00002, new Response(TD00002, "订单方向有误"));
        respMap.put(TD00003, new Response(TD00003, "只支持现金和积分交易"));
        respMap.put(TD00004, new Response(TD00004, "订单不存在"));
        respMap.put(TD00005, new Response(TD00005, "订单缓存数据不存在"));
        respMap.put(TD00006, new Response(TD00006, "交易接口获取失败"));
        respMap.put(TD00008, new Response(TD00008, "订单的买入方向错误"));
        respMap.put(TD00009, new Response(TD00009, "订单存入缓存失败"));
        respMap.put(TD00010, new Response(TD00010, "订单持仓数据缓存失败 "));
        respMap.put(TD00011, new Response(TD00011, "委托单缓存失败 "));
        respMap.put(TD00012, new Response(TD00012, "订单扣款记录缓存失败"));
        respMap.put(TD00013, new Response(TD00013, "订单已平仓"));
        respMap.put(TD00014, new Response(TD00014, "止损保证金不能小于0"));
        respMap.put(TD00015, new Response(TD00015, "止盈保证金不能小于0"));
        respMap.put(TD00016, new Response(TD00016, "下单手数不能大于最大单次开仓量"));


        respMap.put(TDY0001, new Response(TDY0001, "未来有长假期，禁止开启递延功能"));
        respMap.put(TDY0002, new Response(TDY0002, "止盈区间不符合设置"));
        respMap.put(TDY0003, new Response(TDY0003, "止损区间不符合设置"));
        respMap.put(TDY0004, new Response(TDY0004, "设置的止损金额过小已达到止损线，请重新设置"));
        respMap.put(TDY0005, new Response(TDY0005, "即将到交割日，不能开启递延功能"));
        respMap.put(TDY0006, new Response(TDY0006, "不在交易时段,已闭市"));
        respMap.put(TDY0007, new Response(TDY0007, "您当前可设置的最低止损金额为{},请重新设置"));

        respMap.put(TDJ0001, new Response(TDJ0001, "不满足条件"));
        respMap.put(TDJ0002, new Response(TDJ0002, "不可重复下单"));
        respMap.put(TDJ0003, new Response(TDJ0003, "当前已无持仓中订单"));
        respMap.put(TDJ0004, new Response(TDJ0004, "您当前持仓已超过最大持仓量"));
        respMap.put(TDJ0005, new Response(TDJ0005, "当前此商品的系统持仓数已超过最大限制"));

		/*----------------------交易相关--------------------*/
        respMap.put(TRJ0000, new Response(TRJ0000, "参数缺失"));

        respMap.put(TRY0000, new Response(TRY0000, "该时间段无法执行修改"));
		
		/*----------------------积分相关--------------------*/
        respMap.put(SC00001, new Response(SC00001, "积分功能开发中"));
		/*----------------------订单相关--------------------*/
        respMap.put(OR10001, new Response(OR10001, "用户查询持仓数量，未能解析出会话用户"));
        respMap.put(OR10002, new Response(OR10002, "获取资金类型异常"));
        respMap.put(OR10003, new Response(OR10003, "查询用户持仓订单数量异常"));
        respMap.put(OR10004, new Response(OR10004, "用户不存在"));
        respMap.put(OR10005, new Response(OR10005, "参数为空"));
		
		/*----------------------Redis相关--------------------*/
        respMap.put(RD00001, new Response(RD00001, "Redis连接异常"));
        respMap.put(RD00002, new Response(RD00002, "RedisTemplate配置异常"));
		
		/*----------------------新闻、策略资讯相关--------------------*/
        respMap.put(NA00001, new Response(NA00001, "新闻标识错误"));
        respMap.put(NA00002, new Response(NA00002, "评论失败，请稍后再试"));
        respMap.put(NA00003, new Response(NA00003, "评论失败，用户标识丢失"));
        respMap.put(NA00004, new Response(NA00004, "评论过于频繁，请稍后再试"));
        respMap.put(NA00005, new Response(NA00005, "评论内容不可超过500字"));
        respMap.put(NA00006, new Response(NA00006, "评论成功"));
        respMap.put(NA00007, new Response(NA00007, "回复过于频繁，请稍后再试"));
        respMap.put(NA00008, new Response(NA00008, "评论或回复内容不可为空"));
        respMap.put(NA00009, new Response(NA00009, "回复内容不可超过500字"));
        respMap.put(NA00010, new Response(NA00010, "评论标识错误"));
        respMap.put(NA00011, new Response(NA00011, "不可回复自己的评论"));
        respMap.put(NA00012, new Response(NA00012, "不可对自己回复"));
        respMap.put(NA00013, new Response(NA00013, "回复成功"));
        respMap.put(NA00014, new Response(NA00014, "服务异常，评论失败"));
        respMap.put(NA00015, new Response(NA00015, "未找到对应资源"));

        respMap.put(NAY00001, new Response(NAY00001, "已存在栏目，添加失败"));
        respMap.put(NAY00002, new Response(NAY00002, "栏目信息已存在，删除失败"));
        respMap.put(NAY00003, new Response(NAY00003, "已存在标签，添加失败"));
        respMap.put(NAY00004, new Response(NAY00004, "标签信息已存在，删除失败"));

		/*--------------------后台相关----------------------*/
        respMap.put(PRO0001, new Response(PRO0001, "商品不存在 "));
        respMap.put(PRO0002, new Response(PRO0002, "参数不在范围内"));
        respMap.put(MA00001, new Response(MA00001, "会话过期，请重新登录"));
        respMap.put(MA00002, new Response(MA00002, "访问被阻止，权限不够"));
        respMap.put(MA00003, new Response(MA00003, "未登录，请登录后操作"));
        respMap.put(MA00004, new Response(MA00004, "参数不能为空  或者  参数类型不匹配"));
        respMap.put(MA00005, new Response(MA00005, "用户名含有非法字符"));
        respMap.put(MA00006, new Response(MA00006, "密码不可为空"));
        respMap.put(MA00007, new Response(MA00007, "用户不存在"));
        respMap.put(MA00008, new Response(MA00008, "错误次数已超过5次，已被禁用，请联系后台管理员"));
        respMap.put(MA00009, new Response(MA00009, "登录失败，已被禁用"));
        respMap.put(MA00010, new Response(MA00010, "登录失败，用户密码错误"));
        respMap.put(MA00011, new Response(MA00011, "用户角色未分配"));
        respMap.put(MA00012, new Response(MA00012, "密码长度需要在6~20位之间 "));
        respMap.put(MA00013, new Response(MA00013, "密码不一致"));
        respMap.put(MA00014, new Response(MA00014, "该账号已被注册"));
        respMap.put(MA00015, new Response(MA00015, "参数必须唯一"));
        respMap.put(MA00016, new Response(MA00016, "时间转换异常"));
        respMap.put(MA00017, new Response(MA00017, "查询信息为空"));
        respMap.put(MA00018, new Response(MA00018, "删除操作权限失败,存在角色权限关联关系"));
        respMap.put(MA00019, new Response(MA00019, "IP地址验证未通过"));
        respMap.put(MA00020, new Response(MA00020, "短信验证码错误，请重试"));
        respMap.put(MA00021, new Response(MA00021, "ip白名单验证Sql异常，找后台，叫声哥哥就给你改"));
        respMap.put(MA00022, new Response(MA00022, "同一个账户仅可在一个设备登录"));
        respMap.put(MA00023, new Response(MA00023, "登录验证码拦截处理Sql异常"));
        respMap.put(MA00024, new Response(MA00024, "登录失败，账户已被禁用"));
        respMap.put(MA00025, new Response(MA00025, "验证码不能为空"));
        respMap.put(MA00026, new Response(MA00026, "权限分配异常"));
        respMap.put(MA00027, new Response(MA00027, "品牌编码已存在"));
        respMap.put(MA00028, new Response(MA00028, "此品牌不存在"));
        respMap.put(MA00029, new Response(MA00029, "此品牌下有渠道，不可删除！"));
        respMap.put(MA00030, new Response(MA00030, "渠道需指定所属品牌，请选择一个品牌"));
        respMap.put(MA00031, new Response(MA00031, "操作失败"));
        respMap.put(MA00032, new Response(MA00032, "渠道编码已存在"));


        respMap.put(ADY0001, new Response(ADY0001, "已存在相关类型的权重广告"));
        respMap.put(ADY0002, new Response(ADY0002, "广告图删除异常"));
        respMap.put(ADY0003, new Response(ADY0003, "内容获取异常"));
        respMap.put(SIGN_FAILED, new Response(SIGN_FAILED, "签名失败"));
        respMap.put(UNSPAY_ERROR_00001, new Response(UNSPAY_ERROR_00001, "银生宝充值返回调用接口--异步执行失败"));
        respMap.put(US05001, new Response(US05001, "您最多可以绑定三张银行卡，如需更换其他银行卡请先删除或解绑！"));
        respMap.put(US05002, new Response(US05002, "此银行卡已绑定"));
        respMap.put(FU00013, new Response(FU00013, "用户资金账户已存在"));
        respMap.put(FU00014, new Response(FU00014, "用户姓名或者银行卡号为空"));
        respMap.put(GE00001, new Response(GE00001, "查询结果为空"));

        respMap.put("F000001", new Response(F000001, "上传文件过大"));
    }

    public static Response getCode(String errorCode) {
        if (respMap.containsKey(errorCode)) {
            Response sub = respMap.get(errorCode);
            sub.setData(null);
            return sub;
        }
        if (errorCode != null) {
            Response sub = new Response("4444", errorCode);
            return sub;
        }

        Response sub = new Response("9999", "未知异常");
        return sub;

    }

    public static Response getCode(String errorCode, Object object) {
        Response sub = null;
        if (respMap.containsKey(errorCode)) {
            sub = respMap.get(errorCode);
            sub.setData(object);
        } else {
            sub = new Response("9999", "未知异常");
        }
        return sub;
    }

    /**
     * 该方法用于提示错误时，额外传的参数
     *
     * @param errorCode
     * @param param
     * @return
     */
    public static String getMsg(String errorCode, String param) {
        Response r = respMap.get(errorCode);
        r.setMsg(r.getMsg().replace("{}", param));
        respMap.put(errorCode, r);
        return errorCode;
    }
}

