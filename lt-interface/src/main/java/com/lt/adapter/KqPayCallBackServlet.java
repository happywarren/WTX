package com.lt.adapter;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**   
* 项目名称：lt-interface   
* 类名称：KqPayCallBackServlet   
* 类描述：   
* 创建人：yuanxin   
* 创建时间：2017年4月20日 下午2:18:21      
*/
public class KqPayCallBackServlet extends HttpServlet {
	
	private Logger logger = LoggerFactory.getLogger(getClass());
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * tr3回调地址:通知支付结果
	 * String tr3xml ="<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?><MasMessage xmlns=\"http://www.99bill.com/mas_cnp_merchant_interface\"><version>1.0</version><TxnMsgContent><txnType>PUR</txnType><interactiveStatus>TR3</interactiveStatus><amount>1</amount><merchantId>812123445110001</merchantId><terminalId>19911014</terminalId><entryTime>20151221180512</entryTime><externalRefNumber>702</externalRefNumber><transTime>20151221180513</transTime><refNumber>000012748921</refNumber><responseCode>00</responseCode><cardOrg>CU</cardOrg><storableCardNo>6217005738</storableCardNo><authorizationCode>316417</authorizationCode><signature>eKgPQ8a9Efh4sVcmeMaxEfus6St83bZEmRV/Gy667kU9WEn8MIj67Oi7ipPeZAXntJnCD23kt88EBJcHebb9wOjrjv+ouidV/Ts/5rbUjVPT/c1ET+vo7cbFEF/Zr7Xmz9r98PdXjtcvuoMAulhz8xRpbxs1XGrQujXL5cas1o0=</signature></TxnMsgContent></MasMessage>";
	 * <?xml version="1.0" encoding="utf-8"?>
		<MasMessage xmlns="http://www.99bill.com/mas_cnp_merchant_interface">
		  <version>1.0</version>
		  <TxnMsgContent>
		    <txnType>PUR</txnType>
		    <interactiveStatus>TR3</interactiveStatus>
		    <amount>1</amount>
		    <merchantId>812123445110001</merchantId>
		    <terminalId>19911014</terminalId>
		    <entryTime>20151221180512</entryTime>
		    <externalRefNumber>702</externalRefNumber>
		    <transTime>20151221180513</transTime>
		    <refNumber>000012748921</refNumber>
		    <responseCode>00</responseCode>
		    <cardOrg>CU</cardOrg>
		    <storableCardNo>6217005738</storableCardNo>
		    <authorizationCode>316417</authorizationCode>
		    <signature>eKgPQ8a9Efh4sVcmeMaxEfus6St83bZEmRV/Gy667kU9WEn8MIj67Oi7ipPeZAXntJnCD23kt88EBJcHebb9wOjrjv+ouidV/Ts/5rbUjVPT/c1ET+vo7cbFEF/Zr7Xmz9r98PdXjtcvuoMAulhz8xRpbxs1XGrQujXL5cas1o0=</signature>
		  </TxnMsgContent>
		</MasMessage>

	 * @param request
	 * @return
	 * @throws Exception
	 */
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		
		logger.info("[快钱API充值渠道]tr3回调,通知支付结果======================================");
				
		InputStream is = req.getInputStream();

		BufferedReader in = new BufferedReader(new InputStreamReader(is,
				"UTF-8"));
		String line = null;
		StringBuilder sb = new StringBuilder();
		while ((line = in.readLine()) != null) {
			sb.append(line);
		}
		String tr3xml = sb.toString();
		
		//截取参数值
	    String respXml = tr3xml.replaceAll("\"","\'");
	    //外部跟踪编号(本地系统订单编号)
	    final String  externalRefNumber = respXml.substring(respXml.indexOf("<externalRefNumber>")+19,respXml.indexOf("</externalRefNumber>"));
    	//缩略卡号
    	final String storableCardNo = respXml.substring(respXml.indexOf("<storableCardNo>")+16,respXml.indexOf("</storableCardNo>"));
    	
    	String amount =  respXml.substring(respXml.indexOf("<amount>")+8,respXml.indexOf("</amount>"));
    	

 		logger.info("[快钱API充值渠道]tr3数据:" + sb.toString());
 		
 		Map<String,Object> params = new HashMap<String,Object>();
 		params.put("externalRefNumber", externalRefNumber);
 		params.put("storableCardNo", storableCardNo);
 		params.put("amount", amount);
		
 		try{
 			MainOperator mainOperator = new MainOperator();
 	 		mainOperator.kqoperator(params);
 		}catch (Exception e) {
			e.printStackTrace();
			logger.info("[快钱API充值渠道]tr3回调接口不通过,通知结果为：======================================：{}",respXml);
			logger.info("返回错误码为：{}",e.getMessage());
		}
 		
	}
	
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(req, resp);
	}
}
