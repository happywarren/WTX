package com.lt.model.user;

/**   
* 项目名称：lt-api   
* 类名称：UserContant   
* 类描述：   
* 创建人：yuanxin   
* 创建时间：2016年11月29日 下午2:14:25      
*/
public class UserContant {
	
	/*--------------开始：用户短信常量定义 --------------------------------*/
	
	/** 注册验证短信类型*/
	public final static String REGISTER_MSG_TYPE = "100";
	/** 注册验证短信说明*/
	public final static String REGISTER_MSG_MARK = "发送注册短信";	
	/** 找回密码短信类型*/
	public final static String 	FINDPWD_MSG_TYPE = "102";
	/** 找回密码短信说明*/
	public final static String 	FINDPWD_MSG_MARK = "发送找回密码短信";
	/** 递延通知用户*/
	public final static String 	EXPIRATION_MSG_TYPE = "103";
	/** 递延短信*/
	public final static String 	EXPIRATION_MSG_MARK = "合约到期提醒";
	/** 管理后台登录验证短信类型*/
	public final static String 	MESSAGE_LOGIN_CHECK_MSG_TYPE = "104";
	/** 管理后台登录验证短信*/
	public final static String 	MESSAGE_LOGIN_CHECK_MSG_MARK = "后台登录验证短信";
	/** 平仓结算异常类型*/
	public final static String SALE_ERROR_MSG_TYPE = "105";
	/** 平仓结算异常*/
	public final static String 	SALE_ERROR_MSG_MARK = "平仓结算异常";
	/** 平台升级资金通知短信 （升级新系统使用 ）*/
	public final static String APP_CHANGE_FUND_INFO = "106";
	/** 平台升级资金通知短信*/
	public final static String APP_CHANGE_FUND_MARK = "平台升级资金通知短信";
	/** 验证短信最大发送次数*/
	public final static int REGISTER_MSG_MAX_COUNT = 5 ;
	/**  信息类型为 2（短信）*/
	public final static int SMS_SHORT_TYPE = 2 ;
	/**  验证短信有效时间*/
	public final static int REGISTER_CODE_TIME = 30 ;
	/**  每个用户每天验证验证码的次数不超过100次（防止恶意破解）*/
	public final static int CHECKREG_CODE_COUNT = 100 ;
	
	/*--------------结束：用户短信常量定义 --------------------------------*/
	
	/*--------------------开始：日志常量---------------------*/
	/** 用户注册*/
	public final static int OPERATE_USER__REGISTER_LOG = 0;
	/** 用户登录*/
	public final static int OPERATE_USER_LOGIN_LOG = 1;
	/** 用户退出*/
	public final static int OPERATE_USER_LOGOUT_LOG = 2;
	/** 修改用户昵称*/
	public final static int OPERATE_USER_UPTNICKNAME_LOG = 3;
	/** 用户修改止盈止损递延*/
	public final static int OPERATE_USER_UPTEPROLOSSDEF = 4;
	/** 用户修改止盈止损递延*/
	public final static String OPERATE_USER_UPTEPROLOSSDEF_MARK = "用户修改止盈止损递延";
	/** 用户找回密码操作*/
	public final static int OPERATE_USER_FINDPWD = 5;
	
	/** 用户修改密码操作*/
	public final static int OPERATE_USER_UPTPWD = 6;
	
	/** 用户上线*/
	public final static int OPERATE_USER_GO_LIVE_LOG = 7;
	
	/** 用户下线*/
	public final static int OPERATE_USER_OFFLINE_LOG = 8;
	
	/*--------------------结束：日志常量---------------------*/
	
	/*-------------------开始：用户状态常量-------------------*/
	/** 用户手机绑定状态 已绑定*/
	public final static int USER_TELE_BIND = 0;
	/** 用户手机绑定状态 未绑定*/
	public final static int USER_TELE_UNBIND = 1;
	/** 用户修改头像操作*/
	public final static int USER_UPDATE_HEAD = 2;
	/** 用户修改昵称*/
	public final static int USER_UPDATE_NICKNAME = 3;
	/** 修改用户身份证信息*/
	public final static int USER_UPDATE_IDINFO = 4 ;
	/** 修改身份证照片*/
	public final static int USER_UPDATE_IDPIC = 5;
	/** 用户修改银行卡信息*/
	public final static int USER_UPDATE_BANKINFO = 6 ;
	/** 用户修改个性签名*/
	public final static int USER_UPDATE_PERSONSIGN = 7;
	/** 用户修改身份证正反面照片和银行卡照片*/
	public final static int USER_UPDATE_IDBANKPIC = 8;
	/**用户风险测评操作**/
	public final static int USER_UPDATE_RISK = 9;
	/**开户--签名图片**/
	public final static int USER_UPDATE_SIGN = 10;
	/** 用户退出登录操作*/
	public final static int UUSER_lOGOUT_OPERATE = 5;
	/** 用户注册方式 ：1 手机注册*/
	public final static int USER_REGMODE_PHONE = 1;
	/** 用户昵称修改状态 0：未修改*/
	public final static int USER_NICK_STATUS_NOR = 0;
	/** 用户昵称修改状态 1：已修改*/
	public final static int USER_NICK_STATUS_UPD = 1;
	
	/** 用户状态 ： -10 注销 用户信息在后台清空*/
	public final static int USER_STATUS_LOGOUT = -10;
	/** 用户状态 0 正常*/
	public final static int USER_STATUS_NORMAL = 0;
	/** 用户状态 -20 黑名单 前端无法登录，用户信息在后台可以查看*/
	public final static int USER_STATUS_BLACKLIST = -20;
	/** 用户状态 -30冻结 前端可以登录，只能查询，不能操作交易和资金*/
	public final static int USER_STATUS_FREEZE = -30;
	/*-------------------结束：用户状态常量-------------------*/
	
	/*-------------------开始：用户系统配置--------------------*/
	/** 用户在线登陆方式 1 ：手机*/
	public final static int USER_LOGIN_PHONE = 1 ;
	/** 用户在线登陆方式 2：其他*/
	public final static int USER_LOGIN_OTHER = 2;
	
	/** 用户系统名称 1：android*/
	public final static int USER_SYSNAME_ANDROID = 1;
	/** 用户系统名称 1：IOS*/
	public final static int USER_SYSNAME_IOS = 2;
	
	public final static int USER_TOKEN_INVALID = 41022;
	
	/**-------------------------------------开始：短信模块配置------------------------------------**/
	/** 用户短信系统配置：短信格式(验证码)*/
	public final static String SYSCONFIG_SMS_IDENTIFY_CONFIG = "sms_identify_content";
	/** 用户短信系统配置：短信格式(合约到期提醒)*/
	public final static String SYSCONFIG_SMS_EXPIRATION_CONFIG = "sms_expiration_content";
	
	/** 用户短信系统配置：用户账号*/
	public final static String SYSCONFIG_SMS_USER_CODE = "sms_user_code";
	/** 用户短信系统配置：用户密码*/
	public final static String SYSCONFIG_SMS_USER_PASS = "sms_user_pass";
	/** 用户短信系统配置：短信访问链接*/
	public final static String SYSCONFIG_SMS_SERVICE_URL = "sms_service_url";
	
	/** 用户短息系统配置： 发送渠道：即使短信 验证码*/
	public final static String SYSCONFIG_SMS_TYPE_RIGHT = "240"; 
	/** 用户短息系统配置： 发送渠道：触发短信  通知*/
	public final static String SYSCONFIG_SMS_TYPE_TRIGGER = "182"; 
	
	/**-------------------------------------结束：短信模块配置------------------------------------**/
	
	/** 后台支付宝转账配置：回调接口*/
	public final static String ALIPAY_TRAFER_NOTIFY_URL = "alipayTranfer_notifyUrl";
	/** 后台支付宝转账配置：调用API*/
	public final static String ALIPAY_TRAFER_GATEWAY = "alipayTranfer_gateWay";
	/** 后台支付宝转账配置：签名*/
	public final static String ALIPAY_TRAFER_SIGN = "alipayTranfer_sign";
	/** 后台支付宝转账配置：公司名称*/
	public final static String ALIPAY_TRAFER_COMPANY = "alipayTranfer_company";
	/** 后台支付宝转账配置：备注*/
	public final static String ALIPAY_TRAFER_REMARK = "alipayTranfer_remark";
	/** 后台支付宝转账配置：私钥*/
	public final static String ALIPAY_TRAFER_PRIVATEKEY = "alipayTranfer_privateKey";
	/** 后台支付宝转账配置：公钥*/
	public final static String ALIPAY_TRAFER_PUBLICKEY = "alipayTranfer_publicKey";
	/** 后台支付宝转账配置：开发者名称*/
	public final static String ALIPAY_TRAFER_PARTNER = "alipayTranfer_partner";
	/** 支付宝配置：编码格式*/
	public final static String ALIPAY_INPUT_CHARSET = "alipay_input_charset";
	
	/** 用户实名初始状态*/
	public final static int USER_REALNAME_INIT= 0;
	/** 用户实名 ： 申请中*/
	public final static int USER_REALNAME_APPLY= 1;
	/** 用户实名 ：通过*/
	public final static int USER_REALNAME_OVER= 2;
	/** 用户实名： 拒绝*/
	public final static int USER_REALNAME_REJECT= 3;
	/** 非黑名单用户 */
	public final static int IS_NOTBLACK_USER = 0;
	/** 黑名单用户 */
	public final static int IS_BLACK_USER = 1;
	/*-------------------结束：用户系统配置--------------------*/
	
	/*-------------------开始：用户修改止盈止损递延操作日志记录格式----------------*/
	/** 用户修改止损金额 older new 日志格式 */
	public final static String ORDER_UPDATE_LOSS_LOG = "止损由原:older,改为:new;";
	/** 用户修改止盈金额 older new 日志格式 */
	public final static String ORDER_UPDATE_PROFIT_LOG = "止盈由原:older,改为:new;";
	/** 用户开启递延 日志格式 */
	public final static String ORDER_OPEN_DEFER_LOG = "开启递延功能;";
	/** 用户关闭递延 日志格式 */
	public final static String ORDER_CLOSE_DEFER_LOG = "关闭递延功能;";
	/** 用户开启移动止损 日志格式 */
	public final static String ORDER_OPEN_MOVELOSS_LOG = "开启移动止损;";
	/** 用户关闭移动止损 日志格式 */
	public final static String ORDER_CLOSE_MOVELOSS_LOG = "关闭移动止损;";
	/** 用户扣除递延费value 日志格式 */
	public final static String ORDER_DEFER_FEE_LOG = "递延费扣除：value 扣除成功;";
	/** 用户修改递延时间older new  日志格式 */
	public final static String ORDER_DEFER_TIME_LOG = "递延单执行递延逻辑,清仓时间由:older 改成:new";
	/*-------------------结束：用户修改止盈止损递延操作日志记录格式----------------*/

	/*-------------------开始：用户下单时间行情价格操作日志记录格式----------------*/
	public final static String ORDER_SUBMIT_BUY_LOG = " 用户 提交开仓(buy) 价格";
	public final static String ORDER_SUBMIT_SELL_LOG = " 用户 提交平仓(sell) 价格";
	/*-------------------结束：用户下单时间行情价格操作日志记录格式----------------*/
	
	/**开户**/
	public final static int OPEN_STEP_ONE = 31;
	public final static int OPEN_STEP_TWO = 32;
	public final static int OPEN_STEP_THREE = 33;
	
	/**添加银行卡**/
	public final static int BINDING_BANK_CARD = 61;
	
	/*-------------------开始：用户操作数据锁定------------------------------*/
	/** 用户提现并发锁*/
	public final static String USER_WITHDRAW_LOCK = "withdraw_lock";
	/** 用户注册手机号锁定*/
	public final static String USER_REGISTER_TELE_LOCK = "register_tele_lock";
	/** 后台转账锁定*/
	public final static String STAFF_TRANSFER_LOCK = "staff_transfer_lock";
	/*-------------------结束：用户操作数据锁定------------------------------*/
	/**
	 * 在线用户数
	 */
	public static final String REDIS_USER_ONLINE_LIST = "redis:user:online:list";
}
