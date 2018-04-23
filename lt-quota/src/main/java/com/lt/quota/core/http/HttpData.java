package com.lt.quota.core.http;

import java.util.HashMap;

public class HttpData
{
	/**
	 * Http Header
	 */
    public HashMap<String, String> headers;
    /**
     * 回傳格式
     */
    public int S2CDataType;
    /**
     * 傳送時間
     */
    public long sendTime;
    /**
     * 傳送資料
     */
    public byte[] sendData;
	public int code;
	/**
	 * Http 错误讯息
	 */
	public String message;
	/**
	 * 回传资料
	 */
	public String data;
	public byte[] b;
	
	public String host;
	public String name;
	/**
	 * 股票代碼
	 */
	public String stockID;
	public long responseTime;
}
