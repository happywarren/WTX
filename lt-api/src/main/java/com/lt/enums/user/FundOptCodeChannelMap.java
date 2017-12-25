package com.lt.enums.user;

import java.util.HashMap;
import java.util.Map;

/**   
* 项目名称：lt-api   
* 类名称：FundOptCodeChannelMap   
* 类描述：   
* 创建人：yuanxin   
* 创建时间：2017年6月13日 下午3:24:32      
*/
public class FundOptCodeChannelMap {
	
	/** 充值业务码 和 渠道关联关系 如： 1010101 0001 支付宝充值*/
	public static final Map<String,String> fundOptChannel = new HashMap<String,String>(){
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		{
			// 支付宝
			put("1010101", "1010101");
			//快钱
			put("1010102", "1010102");
			// 银生宝
			put("1010103", "1010103");
			//快钱
			put("1010104", "1010104");
			//智付
			put("1010105", "1010105");
			//支付宝2
			put("1010107","1010107");
			/**支付宝(手工)**/
			put("1010108","1010108");
			//兴联支付宝1
			put("1010109","1010109");
			//兴联支付宝2
			put("1010110","1010110");
			//兴联支付宝3
			put("1010111","1010111");
			//兴联支付宝4
			put("1010112","1010112");
			//兴联支付宝5
			put("1010113","1010113");
			//兴联支付宝6
			put("1010114","1010114");
			//兴联支付宝7
			put("1010115","1010115");
		}
	};
}
