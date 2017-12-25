package com.lt.trade.utils;

import java.util.HashMap;
import java.util.Map;

/**
 * 内/外盘错误码
 *
 * Created by sunch on 2016/12/9.
 */
public class ErrorCode {
    
    private static Map<Integer, String> outerFutureMap_ = new HashMap<Integer, String>();
    private static Map<Integer, String> innerFutureMap_ = new HashMap<Integer, String>();

    public static String getOuterErrcodeDesc(Integer code){
        return outerFutureMap_.get(code) == null ? "该错误码没有错误描述" : outerFutureMap_.get(code);
    }

    public static String getInnerErrcodeDesc(Integer code){
        return innerFutureMap_.get(code) == null ? "该错误码没有错误描述" : innerFutureMap_.get(code);
    }

    // 外盘
    static {
        // 自定义错误码
        outerFutureMap_.put(5888, "下单失败, 未找到该证券账户");
        outerFutureMap_.put(5889, "报单失败, 通讯异常");

        // 易盛错误码
        outerFutureMap_.put(-1, "未知错误");
        outerFutureMap_.put(0, "成功");

        outerFutureMap_.put(1, "登录版本不允许");
        outerFutureMap_.put(2, "登录类型不允许");
        outerFutureMap_.put(3, "CA登录方式错误");
        outerFutureMap_.put(4, "请修改密码登录");
        outerFutureMap_.put(5, "登录身份不允许");
        outerFutureMap_.put(6, "登录名不存在");
        outerFutureMap_.put(7, "登录密码错");
        outerFutureMap_.put(8, "禁止登录");
        outerFutureMap_.put(9, "登陆数超限");
        outerFutureMap_.put(10, "第三方认证失败");
        outerFutureMap_.put(11, "未连接或者网络错误");
        outerFutureMap_.put(12, "应答中不包含有效数据");
        outerFutureMap_.put(13, "未登陆成功, 没有操作权限");
        outerFutureMap_.put(14, "没有权限操作该用户");
        outerFutureMap_.put(15, "没有本操作的权限");
        outerFutureMap_.put(16, "初始化数据出错1");
        outerFutureMap_.put(17, "初始化数据出错2");
        outerFutureMap_.put(18, "初始化数据出错3");
        outerFutureMap_.put(19, "未成功初始化");
        outerFutureMap_.put(20, "认证码已过期");
        outerFutureMap_.put(21, "用户已经登录, 请断开后再登陆");
        outerFutureMap_.put(22, "发送频率过高, 请稍后再试");
        outerFutureMap_.put(23, "数据查询不完整, 请稍后再试");
        outerFutureMap_.put(24, "应用程序ID与认证码不符");
        outerFutureMap_.put(25, "被强制退出");

        outerFutureMap_.put(101, "查询合约失败, 无此品种");
        outerFutureMap_.put(102, "查询持仓没有返回结束, 无法进行下一次查询");
        outerFutureMap_.put(103, "查询计算参数没有返回结束, 无法进行下一次查询");

        outerFutureMap_.put(201, "下单无此合约");
        outerFutureMap_.put(202, "上手不存在");
        outerFutureMap_.put(203, "此状态不能撤单");
        outerFutureMap_.put(204, "合约禁止交易");
        outerFutureMap_.put(205, "合约禁止开仓");
        outerFutureMap_.put(206, "委托超出最大下单量");
        outerFutureMap_.put(207, "持仓超出最大数量");
        outerFutureMap_.put(208, "客户禁止交易");
        outerFutureMap_.put(209, "客户禁止开仓");
        outerFutureMap_.put(210, "下单资金不足");
        outerFutureMap_.put(211, "LME未准备就绪");
        outerFutureMap_.put(212, "已删除报单不能转移");
        outerFutureMap_.put(213, "强平单不能撤销");
        outerFutureMap_.put(214, "客户号不存在");
        outerFutureMap_.put(215, "订单号不正确");
        outerFutureMap_.put(216, "录入成交数量不合法");
        outerFutureMap_.put(217, "录入成交找不到委托");
        outerFutureMap_.put(218, "强平单不能修改");
        outerFutureMap_.put(219, "平仓方式错误");

        outerFutureMap_.put(301, "数据库操作错误");

        outerFutureMap_.put(401, "登录时服务器版本错误");
        outerFutureMap_.put(402, "登录时上手号错误");
        outerFutureMap_.put(403, "登录时上手客户号错误");
        outerFutureMap_.put(404, "交易数据包转换错误");
        outerFutureMap_.put(405, "交易数据包发送错误");
        outerFutureMap_.put(406, "上手网关未就绪");
        outerFutureMap_.put(407, "上手网关撤单失败");
        outerFutureMap_.put(408, "系统正在进行强制初始化");
        outerFutureMap_.put(409, "被上手拒绝 ");
        outerFutureMap_.put(410, "市场或品种不许交易");
        outerFutureMap_.put(411, "报单类型不被支持 ");
        outerFutureMap_.put(412, "价格错误");
        outerFutureMap_.put(413, "报单类型错误 ");
        outerFutureMap_.put(414, "合约错误 ");
        outerFutureMap_.put(415, "交易所未就绪");
        outerFutureMap_.put(416, "报单不存在");
        outerFutureMap_.put(417, "报单状态不允许 ");
        outerFutureMap_.put(418, "交易所不支持改单");
        outerFutureMap_.put(419, "数量不合法 ");
        outerFutureMap_.put(420, "此类型不许修改");
        outerFutureMap_.put(421, "账户和用户不匹配");
        outerFutureMap_.put(422, "错误的方向, 买卖不明");
        outerFutureMap_.put(423, "不支持的商品类型");
        outerFutureMap_.put(424, "非法的报单模式");
        outerFutureMap_.put(425, "港交所报单操作类型错误");

        outerFutureMap_.put(501, "版本号不符合前置机要求");
        outerFutureMap_.put(502, "身份错误, 前置不允许前置登录");
        outerFutureMap_.put(503, "发送给服务器失败");
        outerFutureMap_.put(504, "不被支持的命令");
        outerFutureMap_.put(505, "客户没有权限");
        outerFutureMap_.put(506, "系统号错误");
        outerFutureMap_.put(508, "未登录无权限");
        outerFutureMap_.put(509, "前置登录客户数超限");
        outerFutureMap_.put(510, "前置不允许该类型客户登陆");
        outerFutureMap_.put(511, "前置没有准备好");
        outerFutureMap_.put(512, "前置没所需数据");
        outerFutureMap_.put(513, "客户应用没有授权");

        outerFutureMap_.put(600, "认证码错误");
        outerFutureMap_.put(601, "日志文件初始化失败");
        outerFutureMap_.put(602, "日志文件打开失败");
        outerFutureMap_.put(603, "用户无此权限");
        outerFutureMap_.put(604, "委托方式错误");
        outerFutureMap_.put(605, "开平标志错误");

        outerFutureMap_.put(700, "动态令牌时间同步失败");

        outerFutureMap_.put(800, "出金入金操作金额错误");
        outerFutureMap_.put(810, "出金入金审核序列号错误");
        outerFutureMap_.put(811, "出金入金审核无资金信息");
        outerFutureMap_.put(812, "出金入金审核资金不足");
    }
    
    // 内盘
    static {
        // 自定义错误码
    	innerFutureMap_.put(1111, "下单失败, 获取行情失败！");
        innerFutureMap_.put(5888, "下单失败, 未找到该证券账户");
        innerFutureMap_.put(5889, "报单失败, 通讯异常");

        // 易盛错误码
        innerFutureMap_.put(-999, "未知错误");
        innerFutureMap_.put(-3, "每秒发送请求数量超限");
        innerFutureMap_.put(-2, "未处理请求队列总数量超限");
        innerFutureMap_.put(-1, "因网络原因发送失败");
        innerFutureMap_.put(0, "正确");
        innerFutureMap_.put(1, "不在已同步状态");
        innerFutureMap_.put(2, "会话信息不一致");
        innerFutureMap_.put(3, "不合法的登录");
        innerFutureMap_.put(4, "用户不活跃");
        innerFutureMap_.put(5, "重复的登录");
        innerFutureMap_.put(6, "还没有登录");
        innerFutureMap_.put(7, "还没有初始化");
        innerFutureMap_.put(8, "前置不活跃");
        innerFutureMap_.put(9, "无此权限");

        innerFutureMap_.put(10, "修改别人的口令");
        innerFutureMap_.put(11, "找不到该用户");
        innerFutureMap_.put(12, "找不到该经纪公司");
        innerFutureMap_.put(13, "找不到投资者");
        innerFutureMap_.put(14, "原口令不匹配");
        innerFutureMap_.put(15, "报单字段有误");
        innerFutureMap_.put(16, "找不到合约");
        innerFutureMap_.put(17, "合约不能交易");
        innerFutureMap_.put(18, "经纪公司不是交易所的会员");
        innerFutureMap_.put(19, "投资者不活跃");

        innerFutureMap_.put(20, "投资者未在交易所开户");
        innerFutureMap_.put(21, "该交易席位未连接到交易所");
        innerFutureMap_.put(22, "报单错误：不允许重复报单");
        innerFutureMap_.put(23, "错误的报单操作字段");
        innerFutureMap_.put(24, "撤单已报送，不允许重复撤单");
        innerFutureMap_.put(25, "撤单找不到相应报单");
        innerFutureMap_.put(26, "报单已全成交或已撤销，不能再撤");
        innerFutureMap_.put(27, "不支持的功能");
        innerFutureMap_.put(28, "没有报单交易权限");
        innerFutureMap_.put(29, "只能平仓");

        innerFutureMap_.put(30, "平仓量超过持仓量");
        innerFutureMap_.put(31, "资金不足");
        innerFutureMap_.put(32, "主键重复");
        innerFutureMap_.put(33, "找不到主键");
        innerFutureMap_.put(34, "设置经纪公司不活跃状态失败");
        innerFutureMap_.put(35, "经纪公司正在同步");
        innerFutureMap_.put(36, "经纪公司已同步");
        innerFutureMap_.put(37, "现货交易不能卖空");
        innerFutureMap_.put(38, "不合法的结算引用");
        innerFutureMap_.put(39, "交易所网络连接失败");

        innerFutureMap_.put(40, "交易所未处理请求超过许可数");
        innerFutureMap_.put(41, "交易所每秒发送请求数超过许可数");
        innerFutureMap_.put(42, "结算结果未确认");
        innerFutureMap_.put(43, "没有对应的入金记录");
        innerFutureMap_.put(44, "交易所已经进入连续交易状态");
        innerFutureMap_.put(45, "找不到预埋（撤单）单");
        innerFutureMap_.put(46, "预埋（撤单）单已经发送");
        innerFutureMap_.put(47, "预埋（撤单）单已经删除");
        innerFutureMap_.put(48, "无效的投资者或者密码");
        innerFutureMap_.put(49, "不合法的登录IP地址");

        innerFutureMap_.put(50, "平今仓位不足");
        innerFutureMap_.put(51, "平昨仓位不足");
        innerFutureMap_.put(52, "经纪公司没有足够可用的条件单数量");
        innerFutureMap_.put(53, "投资者没有足够可用的条件单数量");
        innerFutureMap_.put(54, "经纪公司不支持条件单");
        innerFutureMap_.put(55, "重发未知单经济公司/投资者不匹配");
        innerFutureMap_.put(56, "同步动态令牌失败");
        innerFutureMap_.put(57, "动态令牌校验错误");
        innerFutureMap_.put(58, "找不到动态令牌配置信息");
        innerFutureMap_.put(59, "不支持的动态令牌类型");

        innerFutureMap_.put(60, "用户在线会话超出上限");
        innerFutureMap_.put(61, "该交易所不支持套利/做市商类型报单");
        innerFutureMap_.put(62, "没有条件单交易权限");
        innerFutureMap_.put(63, "客户端认证失败");
        innerFutureMap_.put(64, "客户端未认证");
        innerFutureMap_.put(65, "该合约不支持互换类型报单");
        innerFutureMap_.put(66, "该期权合约只支持投机类型报单");
        innerFutureMap_.put(67, "执行宣告错误，不允许重复执行");
        innerFutureMap_.put(68, "重发未知执行宣告经纪公司/投资者不匹配");
        innerFutureMap_.put(69, "只有期权合约可执行");

        innerFutureMap_.put(70, "该期权合约不支持执行");
        innerFutureMap_.put(71, "执行宣告字段有误");
        innerFutureMap_.put(72, "执行宣告撤单已报送，不允许重复撤单");
        innerFutureMap_.put(73, "执行宣告撤单找不到相应执行宣告");
        innerFutureMap_.put(74, "执行仓位不足");
        innerFutureMap_.put(75, "连续登录失败次数超限，登录被禁止");
        innerFutureMap_.put(76, "非法银期代理关系");
        innerFutureMap_.put(77, "无此功能");
        innerFutureMap_.put(78, "发送报单失败");
        innerFutureMap_.put(79, "发送报单操作失败");

        innerFutureMap_.put(80, "交易所不支持的价格类型");
        innerFutureMap_.put(81, "错误的执行类型");
        innerFutureMap_.put(82, "无效的组合合约");
        innerFutureMap_.put(83, "该合约不支持询价");
        innerFutureMap_.put(84, "重发未知报价经纪公司/投资者不匹配");
        innerFutureMap_.put(85, "该合约不支持报价");
        innerFutureMap_.put(86, "报价撤单找不到相应报价");
        innerFutureMap_.put(87, "该期权合约不支持放弃执行");
        innerFutureMap_.put(88, "该组合期权合约只支持IOC");
        innerFutureMap_.put(89, "打开文件失败");

        innerFutureMap_.put(90, "查询未就绪，请稍后重试");
        innerFutureMap_.put(91, "交易所返回的错误");
        innerFutureMap_.put(92, "报价衍生单要等待交易所返回才能撤单");
        innerFutureMap_.put(93, "找不到组合合约映射");
    }

}
