package com.lt.util.utils.kqRecharge.kqutil;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLSession;

public class MyHostnameVerifier implements HostnameVerifier {

	public boolean verify(String hostname, SSLSession session) {
		if("localhost".equals(hostname)){
			return true;
		} else {
			return false;
		}
	}

}
