package com.lt.quota.core.http;

public interface ISocketListener
{
	/**
	 * 接收Socket連線狀態
	 * 00: 连线
	 * 01: 正常断线,无法自动连线
	 * 02: 非正常断线,要自动连线再重传
	 */
	public void onNetworkStatusChanged(MitakeSocket mitakeSocket, String packageNo, String code);
	/**
	 * 接收由伺服器回傳的封包
	 * @param httpData
	 */
	public void onContent(HttpData httpData);
}
