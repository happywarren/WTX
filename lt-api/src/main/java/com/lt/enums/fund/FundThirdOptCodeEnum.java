package com.lt.enums.fund;

/**   
* 项目名称：lt-api   
* 类名称：FundThirdOptCodeEnum   
* 类描述： 三级业务码枚举类  
* 创建人：yuanxin   
* 创建时间：2017年2月23日 下午2:00:16      
*/
public enum FundThirdOptCodeEnum {
	
	/********** 现金收入（三级业务码）*************/
	/** 支付宝充值*/
	ZFBCZ("10101","1010101","支付宝充值"),
	/** 快钱充值*/
	KQCZ("10101","1010102","快钱充值"),
	/** 银生宝充值*/
	YSBCZ("10101","1010103","银生宝充值"),
	/** 银行汇款*/
	YHHK("10101","1010104","平安银行充值"),
	/**支付支付手机端**/
	DINPAY_MOB("10101","1010105","智付充值移动端"),
	/**智付支付web**/
	DINPAY_WEB("10101","1010106","智付充值WEB端"),
	/**兴联支付宝**/
	XDPAY("10101","1010107","深圳熙大支付宝充值"),
	/**手工支付宝**/
	HANDAIPAY("10101","1010108","手工支付宝充值"),
	/**兴联支付宝1**/
	XLPAY1("10101","1010109","兴联支付宝充值1"),
	/**兴联支付宝2**/
	XLPAY2("10101","1010110","兴联支付宝充值2"),
	/**兴联支付宝3**/
	XLPAY3("10101","1010111","兴联支付宝充值3"),
	/**兴联支付宝4**/
	XLPAY4("10101","1010112","兴联支付宝充值4"),
	/**兴联支付宝4**/
	XLPAY5("10101","1010113","兴联支付宝充值5"),
	/**兴联支付宝4**/
	XLPAY6("10101","1010114","兴联支付宝充值6"),
	/**兴联支付宝4**/
	XLPAY7("10101","1010115","兴联支付宝充值7"),
	/**爸爸付**/
	DADDYPAY("10101","1010116","爸爸付充值"),
	/**钱通支付**/
	QIANTONGPAY("10101","1010117","钱通充值"),
	/** 佣金转现*/
	YJZX("10201","1020101","佣金转现"),
	/** 任务奖励*/
	RWJL("10201","1020102","任务奖励"),
	/** 推广奖励*/
	TGJL("10201","1020103","推广奖励"),
	
	/** 系统迁移*/
	XTQY("10202","1020201","人民币转美金"),
	
	/** 员工福利*/
	XTCR("10301","1030101","员工福利"),
	/** 活动奖励*/
	HDJL("10301","1030102","活动奖励"),
	/** 用户奖励*/
	YHJL("10301","1030103","用户奖励"),
	/** 用户补偿*/
	YHBC("10301","1030104","用户补偿"),
	/** 支付宝转账*/
	ZFBRG("10301","1030105","支付宝转账"),
	/** 资金利息*/
	ZJLX("10301","1030106","资金利息"),
	/** 测试存入*/
	CSCR("10301","1030107","测试存入"),
	/** 人工取出拒绝*/
	XTQU_REJECT("10302","1030301","人工取出拒绝"),
	/** 提现拒绝*/
	WITHDRAW_REJECT("10602","1060201","出金拒绝"),
	/** 提现撤销*/
	WITHDRAW_CANCLE("10603","1060301","出金撤销"),
	
	/********** 现金取出（三级业务码）*************/
	
	/** 支付宝提现*/
	ZFBTX("20101","2010101","支付宝提现"),
	/** 快钱提现*/
	KQTX("20101","2010102","快钱提现"),
	/** 银生宝提现*/
	YSBTX("20101","2010103","银生宝提现"),
	/** 银行提现*/
	YHTX("20101","2010104","人工汇款"),
	/** 智付提现*/
	ZFTX("20101","2010107","智付提现"),
	/** 爸爸付提现*/
	DPTX("20101","2010108","爸爸付提现"),
	/** 钱通提现*/
	QTTX("20101","2010109","钱通提现"),
	/** 用户出金手续费*/
	WITHDRAW_FEE("20101","2010105","用户出金手续费"),
	/** 用户出金*/
	WITHDRAW_AMT("20101","2010106","用户出金"),
	
	/** 系统取出*/
	XTQC("20301","2030101","系统取出"),
	/** 结算取出*/
	JSQC("20301","2030102","结算取出"),
	/** 测试取出*/
	CSQC("20301","2030103","测试取出"),
	/** 兑换积分*/
	DHJF("20503","2050301","兑换积分"),
	/** 赠金收回*/
	ZJSH("20701","2070101","赠金收回"),
	
	/********** 积分收入（三级业务码）*************/
	
	/** 3030101兑换积分*/
	SXTCR("30101","3030101","兑换积分"),
	/** 3020101消费结算*/
	XFJS("30201","3020101","消费结算"),
	/** 3020102任务奖励*/
	SSRWJL("30201","3020102","任务奖励"),
	/** 3020103推广奖励*/
	STGJL("30201","3020103","推广奖励"),
	/** 3020104赠送体验金*/
	ZSTYJ("30201","3020104","赠送体验金"),
	/** 3030101积分充值*/
	SRECHARGE("30301","3030101","积分充值"),
	/** 3030102任务奖励*/
	SRRWJL("30301","3030102","任务奖励"),
	/** 3030103内部存入*/
	SNBCR("30301","3030103","内部存入"),
	/** 3030104测试存入*/
	SCSCR("30301","3030104","测试存入"),
	/** 3030201人工取出拒绝*/
	MANUALOUT_REJECT("30302","3030201","人工取出拒绝"),
	/** 3030201系统取出拒绝*/
	SXTQC_REJECT("30302","3030201","系统取出拒绝"),
	/********** 积分取出（三级业务码）*************/
	/** 4010101积分消费*/
	SXF("40101","4010101","积分消费"),
	/** 4030102内部取出*/
	SNBQC("40301","4030102","内部取出"),
	/** 4030101测试取出*/
	SXTQC("40301","4030101","测试取出");
	
	
	FundThirdOptCodeEnum(String secondCode,String thirdLevelCode,String thirdLevelName){
		this.secondCode = secondCode;
		this.thirdLevelCode = thirdLevelCode;
		this.thirdLevelname = thirdLevelName;
	}
	
	
	private String secondCode;
	private String thirdLevelCode;
	private String thirdLevelname;
	/**
	 * @return the secondCode
	 */
	public String getSecondCode() {
		return secondCode;
	}
	/**
	 * @param secondCode the secondCode to set
	 */
	public void setSecondCode(String secondCode) {
		this.secondCode = secondCode;
	}
	/**
	 * @return the thirdLevelCode
	 */
	public String getThirdLevelCode() {
		return thirdLevelCode;
	}
	/**
	 * @param thirdLevelCode the thirdLevelCode to set
	 */
	public void setThirdLevelCode(String thirdLevelCode) {
		this.thirdLevelCode = thirdLevelCode;
	}
	/**
	 * @return the thirdLevelname
	 */
	public String getThirdLevelname() {
		return thirdLevelname;
	}
	/**
	 * @param thirdLevelname the thirdLevelname to set
	 */
	public void setThirdLevelname(String thirdLevelname) {
		this.thirdLevelname = thirdLevelname;
	}
	
}
