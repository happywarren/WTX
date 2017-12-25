package com.lt.adapter;

import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.lt.util.utils.crypt.MD5Util;

import net.sf.json.JSONException;
import net.sf.json.JSONObject;

/**   
* 项目名称：lt-interface   
* 类名称：AliPayCallBackFServlet   
* 类描述： 支付宝回调接口（聚合支付）  
* 创建人：yuanxin   
* 创建时间：2017年7月17日 下午5:44:55      
*/
public class AliPayCallBackFServlet extends HttpServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Logger logger = LoggerFactory.getLogger(getClass());
	
	//private String key =  "lR4tGYhPMcCvVA3CdC7jIOsT2xh14Spn";
	private String key =  "JZ7kU20tjubUf0lOvoGcboqGbClBQpbI";
	
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException{
		logger.info("接收支付宝返回结果====");
    	Map<String,Object> params = new HashMap<String,Object>();
		Map<String, String[]> requestParams = req.getParameterMap();
		String requestUrl = req.getRequestURL()+"?";
		String requestParam = "";
		for (Iterator<String> iter = requestParams.keySet().iterator(); iter.hasNext();) {
			String name = (String) iter.next();
			String[] values = (String[]) requestParams.get(name);
			String valueStr = "";
			for (int i = 0; i < values.length; i++) {
				valueStr = (i == values.length - 1) ? valueStr + values[i]
						: valueStr + values[i] + ",";
			}
			requestUrl += name + "=" + valueStr + "&";
			if(!name.equals("sign")){
				requestParam += name + "=" + valueStr + "&";
			}
			params.put(name, valueStr);
			logger.info("{}:{}",name,valueStr);
		}
		
		if(params.containsKey("msg")){
			String order_msg = (String) params.get("msg") ;
			
			try{
				JSONObject jsonObject = JSONObject.fromObject(order_msg);
				if(jsonObject.containsKey("payMoney")){
					logger.info("是否正确读取到金额：{}",jsonObject.get("payMoney"));
					params.put("payMoney", jsonObject.get("payMoney"));
				}
				
			}catch(JSONException e){
	    		e.printStackTrace();
	    		logger.info("不是有效json 串 ：{}",order_msg);
	    	}
		}
		params.put("pay_msg", requestUrl);
		params.put("signParam", requestParam);
		logger.info("FULL REQUEST URL::{}",requestUrl);
		MainOperator mainOperator = new MainOperator();
		
		//if(verify(params.get("sign").toString(), requestParam)){
		//	mainOperator.aliPayFoperator(params);
		//}
		mainOperator.aliPayFoperator(params);
		resp.getWriter().print("success");
	}
	
	/**
	 * 校验是否聚合支付返回参数
	 * @param sign 获取传递参数的签名
	 * @param requestUrl 回传的参数链接
	 * @return    
	 * @return:       boolean    
	 * @throws 
	 * @author        yuanxin
	 * @Date          2017年7月17日 下午5:58:41
	 */
	public boolean verify(String sign,String requestUrl){
		requestUrl += "key=" + this.key ;
		logger.info("requestUrl:{}",requestUrl);
		String mysign = MD5Util.md5(requestUrl.toString()).toLowerCase();
		logger.info("sign:{},mysign:{}",sign,mysign);
		return sign.equals(mysign) ;
	}
	
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		doGet(req, resp);
	}
	public static void main(String[] args) {
		StringBuffer buffer = new StringBuffer();
		buffer.append("merchantOutOrderNo=ali1502160756647");
		buffer.append("&merid=120170804001");
		buffer.append("&msg={\"payMoney\":\"1.00\"}");
		buffer.append("&noncestr=fKBlhuYqyomJpBKzeXyTwMCLnQlGhbJY");
		buffer.append("&orderNo=00ali1502160756647190");
		buffer.append("&payResult=1");
		buffer.append("&key=JZ7kU20tjubUf0lOvoGcboqGbClBQpbI");
		System.out.println(MD5Util.md5(buffer.toString()).toLowerCase());
		//1cce6ff303172384528c3c5d690eed68
		//JZ7kU20tjubUf0lOvoGcboqGbClBQpbI
		
	}
}
