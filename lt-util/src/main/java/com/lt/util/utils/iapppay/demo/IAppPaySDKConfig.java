package com.lt.util.utils.iapppay.demo;

/**
 *应用接入iAppPay云支付平台sdk集成信息 
 */
public class IAppPaySDKConfig{

	/**
	 * 应用名称：
	 * 应用在iAppPay云支付平台注册的名称
	 */
	//public final static  String APP_NAME = "testFAQ";
	public final static  String APP_NAME = "ProLtrader";
	
	/**
	 * 应用编号：
	 * 应用在iAppPay云支付平台的编号，此编号用于应用与iAppPay云支付平台的sdk集成 
	 请用商户自己的appid!!
	 */
	//public final static  String APP_ID = "3002495803";
	public final static  String APP_ID = "3015203700";
	

	/**
	 * 商品编号：
	 * 应用的商品在iAppPay云支付平台的编号，此编号用于iAppPay云支付平台的sdk到iAppPay云支付平台查找商品详细信息（商品名称、商品销售方式、商品价格）
	 * 编号对应商品名称为：1
	 */
	//public final static  int WARES_ID_1=6;
	public final static  int WARES_ID_1=1;

	/**
	 * 应用私钥：
	 * 用于对商户应用发送到平台的数据进行加密
	 
	 JAVA请到后台复制pkcs8格式的私钥！！
	 */
	//public final static String APPV_KEY = "MIICdwIBADANBgkqhkiG9w0BAQEFAASCAmEwggJdAgEAAoGBANalBqEqqglu5mu6oYKERBb8mK32b5wewFZKXPiPN47YGC5MEUHnHniMW6ziBzJMPuYTk0dgVqesb5DrN+bETT1m7Bkf+/xYOwMTqNFA4A0rgwxPrXwVaK+kvBhkLainV91gX03+jaH1U/xB7DeXdFKG9JPFFavoOKkUTuS+qEw1AgMBAAECgYEAyS8Gprspgwv6V1EnnwDAHVeG9887T3aXSooK2ELMjUvIZmSP7FhnfA/6wXFd02wZrGb+rXED58c9DvUZgWfPCB2kmK65WHnMo3Z+0clFcW3M2XrHi1JVNdruBYaDt+UiQckfBKMu4+KSYYDXGrMbzvH2Y/qd7iOm4oiOt0NIHuUCQQDyuxxmor5DOEeVz3M0ZKNRy2DRzv2vTluggE3cVtMfAbVwp0Y6sm6TAslXZ/4Vp1NnXXcS6c7ijCVi0EYM6JQXAkEA4mDc1jaCYzj680bkv2oxxVqJWTcmSw/DPmAjewTQHpnd0sSJxEiHGY8bhdBBUO4S/bX8TCRM32DQdj9pVDG1kwJACLjmjV2dvqagE+cPebuADuljAkcQ4KLNMhhKM/e+wGd8UJ8CLAvLY2b/Oy/Wdoq7uYQnSjuynntwwHtBeSdGYwJAUMCgxkE3jdF7o2B8pNNq4a2EJTEJFemPiiadQXrcCq50dITrZ/s/RhzIgplhU085swtB7p9cVKqiciv4lLIOxQJBAJJeeDmp6R0zvs0j83q4KeDAkgUa/xB/EUE44u7j79AvvL2wFfuog3H+BUGYp7SIMUM901xltnkcXLuQfvCXNOw=";
	public final static String APPV_KEY = "MIICdwIBADANBgkqhkiG9w0BAQEFAASCAmEwggJdAgEAAoGBAIvyByPmy/xOKDpoGEO9rdX1myFtxBjdT20udEP5WD71/xFYr9HLvQuWurEX+jCZlzl7HH+F66eD5yHmJ/Li8Mmaw4tPeKYej3on9WU++kpJxvmmwS/RJLJToPmm3v6qlvHh5lccwwjW62PzoigXCcq+npStuz+0srELCZn3lF2vAgMBAAECgYAlVJiHASfk08xzWBUw7MYUTwHktu0aXN61FzE4eKkLkn9J10h/REPXdYuzddvtXusyEB8X/VdRRiQ/rK93YQYtuuXBLOLMbdpTHehvYMWGaL3ZR4v4hZaCBqJE1YIE9b5tinPzwz+5+0kW1veBBFdqt5rSebvF0o4KLv/eNciToQJBAN6RAvBwSRclDKj+maG4XxUQXuAbKlak63fAIz9mx+8YGAErCbv/YC8MkRikWdqPCZbU+XN+q0/jW7vaVfxBu5kCQQCg98CI6fGm/bL9wVPIS+rwPGN2D5nasVYlEBXueKzwdLLps3M6lMf6cd+JN2J2HEmeft00HKm9Ses+sfT4Q/CHAkEAlPXrSK9uS95RLd5RRurWQIvXZBjqalkw+9IOBUYuNHkkv8tlVX0ji/nWNu7w8JXhbiEW39T8pZhe8ki8WfxIqQJAHNFeKeTgO4pOAjobWs+kpw/YqqlOXSXEi2we0QvPwljSMx7KcWFzj/XVFEbTHNwhnfuOdKbxCI90S0wm1E+tfwJBANfIQJaBKjOByMxHEsfL615lEzlCjG/BrMPthweE2L9qZ4AWXYKQJJ1MXO1k91DeGsYYGCvySmSkYFDL7PKy6Rk=";

	/**
	 * 平台公钥：
	 * 用于商户应用对接收平台的数据进行解密
	请用商户自己的公钥！！
	 */
	//public final static String PLATP_KEY = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQC5uCCKGlN3r8GeFRm+JHYxlePa2jHbNH7pxLaXweXT71z2Ui5fCYMJRlseYfCvvma9lp2dggmwuGZSVdMgNdOCNcr6Q7O0dTUPIISvRGhjcEFtp+8gl4jAOEdfW62MeWwUdR6bhfXM1wauIKYZ86m55pkYX5jAqq+FhUfWMmbR5wIDAQAB";

	public final static String PLATP_KEY = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCEH5d5wPMXawH8FTgV/fjB8YXa8+sKEQFkRISf3gZgnkVkj5SVxadQykJesmmfzxx3pVcCgoPhyOK5B4eYDqYYIHSdILfSWT45B1FF4Io2YIGfDdugKLckn1aqyLyn5qTgGfgtrvEzFeCNf0BRGItRfcaj/hGX/pWf3vrA4kFNAwIDAQAB";
}
