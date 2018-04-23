package com.lt.quota.core.http;

import java.util.Hashtable;

/**
 * Http協定參數物件
 * @version 20150511[李欣駿]   物件改為僅負責傳遞值。
 * @version 20150515[李欣駿]   加入記錄最大可重新連線次數與更換Server IP的連線次數。
 * @version 20151120[李欣駿]   加入keep-alive变数。
 * @author 李欣駿
 */
public class MitakeHttpParams
{
	/**
	 * Client到Server的參數型態為字串
	 */
	public static final int C_S_DATA_TYPE_STRING = 0;
	/**
	 * Client到Server的參數型態為位元組
	 */
	public static final int C_S_DATA_TYPE_BYTES = 1;
	/**
	 * Server回傳Client的結果型態為字串
	 */
	public static final int S_C_DATA_TYPE_STRING = 2;
	/**
	 * Server回傳Client的結果型態為位元組
	 */
	public static final int S_C_DATA_TYPE_BYTES = 3;

	public String api;
	public int C2SDataType;
	public int S2CDataType;
	/**
	 * 只用在Http Post傳遞變數用
	 */
	public Hashtable<String, String> kv;
	/**
	 * 只用在Http Post傳遞變數用
	 */
	public byte[] b;
    /**
     * User-Agent
     */
	public String userAgent;
    /**
     * 設定Headers
     */
    public String[][] headers;
	/**
	 * 保持连线
	 */
	public boolean keepAlive = true;
	/**
	 * 封包序号
	 */
	public String packageNo;

	public String method;

	public long sendTelegramTime = -1;
	
	public String acceptEncoding;
	public String contentType;
	public String stockID;
}
