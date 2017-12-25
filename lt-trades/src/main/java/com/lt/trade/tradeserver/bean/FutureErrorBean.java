package com.lt.trade.tradeserver.bean;

/**
 * Created by sunch on 2016/12/11.
 */
public class FutureErrorBean extends BaseMatchBean {
	
	/** 交易平台订单id */
    private int platformId;
    /** 错误码ID */
    private int errorId;
    /**  错误信息 */
    private String errorMsg;
    /**  资金类型  0、现金；1、积分 */
    private int fundType=1;

    public FutureErrorBean() {
    }

	/** 
	 * 获取 交易平台订单id 
	 * @return platformId 
	 */
	public int getPlatformId() {
		return platformId;
	}

	/** 
	 * 设置 交易平台订单id 
	 * @param platformId 交易平台订单id 
	 */
	public void setPlatformId(int platformId) {
		this.platformId = platformId;
	}

	/** 
	 * 获取 错误码ID 
	 * @return errorId 
	 */
	public int getErrorId() {
		return errorId;
	}

	/** 
	 * 设置 错误码ID 
	 * @param errorId 错误码ID 
	 */
	public void setErrorId(int errorId) {
		this.errorId = errorId;
	}

	/** 
	 * 获取 错误信息 
	 * @return errorMsg 
	 */
	public String getErrorMsg() {
		return errorMsg;
	}

	/** 
	 * 设置 错误信息 
	 * @param errorMsg 错误信息 
	 */
	public void setErrorMsg(String errorMsg) {
		this.errorMsg = errorMsg;
	}

	public int getFundType() {
		return fundType;
	}

	public void setFundType(int fundType) {
		this.fundType = fundType;
	}

    
}
