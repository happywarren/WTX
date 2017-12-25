package com.lt.model.user;

/**   
* 项目名称：lt-api   
* 类名称：ServiceContant   
* 类描述：用户服务常量   
* 创建人：yuanxin   
* 创建时间：2016年12月7日 下午5:33:12      
*/
public class ServiceContant {
	
	/*-----------------------开始：用户服务定义----------------------------*/
	
	/** 用户积分账户功能服务码及名称*/
	public final static String SCORE_SERVICE_CODE = "1000";
	public final static String SCORE_SERVICE_NAME = "用户积分账户功能";
	/** 用户现金账户功能服务码及名称*/
	public final static String CASH_SERVICE_CODE = "1001";
	public final static String CASH_SERVICE_NAME = "用户现金账户功能";
	/** 用户递延功能服务吗及名称*/
	public final static String OVERNIGHT_SERVICE_CODE = "1004";
	public final static String OVERNIGHT_SERVICE_NAME = "用户隔夜功能";
	
	public final static String PROMOTER_SERVICE_CODE = "1003";
	public final static String PROMOTER_SERVICE_NAME = "用户隔夜功能";
	/**券商服务功能**/
	public final static String INVESTOR_SERVICE_CODE = "1002";
	public final static String INVESTOR_SERVICE_NAME = "券商功能";	
	/*-----------------------结束：用户服务定义----------------------------*/
	
	/*---------------------------开始：用户服务状态定义---------------------------*/
	
	/** 用户服务状态 ：1  正在运行 */
	public final static String USERSERVICE_PROCESSING = "1";
	/** 用户服务状态 ：0 停止*/
	public final static String USERSERVICE_STOP = "0";
	/** 服务默认状态 1：默认开启*/
	public final static int USERSERVICE_DEFAULT_OPEN = 1;
	/** 服务默认状态 0：默认关闭*/
	public final static int USERSERVICE_DEFAULT_CLOSED = 0;
	
	/** 服务类型 0 ：账户型服务*/
	public final static int SERVICE_TYPE_ACCOUNT = 0 ;
	/** 服务类型 1：功能型服务*/
	public final static int SERVICE_TYPE_FUNCTION = 1 ;
	/*---------------------------结束：用户服务状态定义---------------------------*/
}
