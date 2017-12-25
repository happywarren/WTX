package com.lt.enums.sys;

/**
 * 注意：该枚举类仅用于线程锁，
 * 		  每个定义都是每个方法独有的，不可共享
 * @author jingwb
 *
 */
public enum SysThreadLockEnum {

	/**每分钟刷新市场状态*/
	REFRESH_MARKET_STATUS("每分钟刷新市场状态","refreshProMarketStatusForRedis"),
	/**佣金结算*/
	BALANCE_COMMISION_TASK("佣金结算","balanceCommisionTask"),
	/**佣金结算*/
	INIT_STATISTIC_DAY_LOG("初始化推广统计日报","initStatisticDayLog"),
	/**智付查询任务**/
	DINPAY_QUERY_TASK("智付查询任务","dinpayQueryTask"),
	
	/**熙大支付宝查询任务**/
	XDPPAY_QUERY_TASK("熙大支付宝查询任务","xdpayQueryTask"),

	/**银生宝查询任务**/
	UNSPAY_QUERY_TASK("银生宝查询任务","unspayQueryTask"),

	/**入账处理**/
	RECORDS_EXEC_TASK("入账处理任务","recordsExecTask"),

	/** 递延费结算 **/
	PERIOD_RATE_CALUATE_TASK("递延费结算任务","periodRateCaluateTask"),

	/**银生宝提现查询任务**/
	UNSPAY_DRAW_QUERY_TASK("银生宝提现查询任务","unspayDrawQueryTask"),

	;
	
	private String name;
	private String code;
	SysThreadLockEnum(String name,String code){
		this.name = name;
		this.code = code;
	}
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	
	
}
