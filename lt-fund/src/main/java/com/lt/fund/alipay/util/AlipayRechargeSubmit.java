package com.lt.fund.alipay.util;


import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;

import org.apache.commons.httpclient.NameValuePair;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Node;
import org.dom4j.io.SAXReader;

import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.AlipayRequest;
import com.alipay.api.AlipayResponse;
import com.alipay.api.internal.util.AlipayEncrypt;
import com.alipay.api.internal.util.AlipayHashMap;
import com.alipay.api.internal.util.AlipayLogger;
import com.alipay.api.internal.util.AlipaySignature;
import com.alipay.api.internal.util.RequestParametersHolder;
import com.alipay.api.internal.util.StringUtils;
import com.alipay.api.internal.util.WebUtils;
import com.alipay.api.internal.util.json.JSONWriter;
import com.alipay.api.request.AlipayTradeWapPayRequest;
import com.lt.fund.alipay.config.AlipayRechargeConfig;
import com.lt.fund.alipay.util.httpClient.HttpRechargeProtocolHandler;
import com.lt.fund.alipay.util.httpClient.HttpRechargeRequest;
import com.lt.fund.alipay.util.httpClient.HttpRechargeResponse;
import com.lt.fund.alipay.util.httpClient.HttpRechargeResultType;
import com.lt.util.utils.alipay.alipayTranfer.sign.RSA;




/* *
 *类名：AlipaySubmit
 *功能：支付宝各接口请求提交类
 *详细：构造支付宝各接口表单HTML文本，获取远程HTTP数据
 *版本：3.3
 *日期：2012-08-13
 *说明：
 *以下代码只是为了方便商户测试而提供的样例代码，商户可以根据自己网站的需要，按照技术文档编写,并非一定要使用该代码。
 *该代码仅供学习和研究支付宝接口使用，只是提供一个参考。
 */

public class AlipayRechargeSubmit {
    
    /**
     * 支付宝提供给商户的服务接入网关URL(新)
     */
    private static final String ALIPAY_GATEWAY_NEW = "https://mapi.alipay.com/gateway.do?";
	
    /**
     * 生成签名结果
     * @param sPara 要签名的数组
     * @return 签名结果字符串
     */
	public static String buildRequestMysign(Map<String, String> sPara) {
    	String prestr = AlipayRechargeCore.createLinkString(sPara); //把数组所有元素，按照“参数=参数值”的模式用“&”字符拼接成字符串
        String mysign = "";
        if(AlipayRechargeConfig.sign_type.equals("RSA") ) {
        	mysign = RSA.sign(prestr, AlipayRechargeConfig.private_key, AlipayRechargeConfig.input_charset);
        }
        return mysign;
    }
	
    /**
     * 生成要请求给支付宝的参数数组
     * @param sParaTemp 请求前的参数数组
     * @return 要请求的参数数组
     */
    private static Map<String, String> buildRequestPara(Map<String, String> sParaTemp) {
        //除去数组中的空值和签名参数
        Map<String, String> sPara = AlipayRechargeCore.paraFilter(sParaTemp);
        //生成签名结果
        String mysign = buildRequestMysign(sPara);

        //签名结果与签名方式加入请求提交参数组中
        sPara.put("sign", mysign);
        sPara.put("sign_type", AlipayRechargeConfig.sign_type);

        return sPara;
    }

    /**
     * 建立请求，以表单HTML形式构造（默认）
     * @param sParaTemp 请求参数数组
     * @param strMethod 提交方式。两个值可选：post、get
     * @param strButtonName 确认按钮显示文字
     * @return 提交表单HTML文本
     */
    public static String buildRequest(Map<String, String> sParaTemp, String strMethod, String strButtonName) {
        //待请求参数数组
        Map<String, String> sPara = buildRequestPara(sParaTemp);
        List<String> keys = new ArrayList<String>(sPara.keySet());

        StringBuffer sbHtml = new StringBuffer();

        sbHtml.append("<form id=\"alipaysubmit\" name=\"alipaysubmit\" action=\"" + ALIPAY_GATEWAY_NEW
                      + "_input_charset=" + AlipayRechargeConfig.input_charset + "\" method=\"" + strMethod
                      + "\">");

        for (int i = 0; i < keys.size(); i++) {
            String name = (String) keys.get(i);
            String value = (String) sPara.get(name);

            sbHtml.append("<input type=\"hidden\" name=\"" + name + "\" value=\"" + value + "\"/>");
        }

        //submit按钮控件请不要含有name属性
        sbHtml.append("<input type=\"submit\" value=\"" + strButtonName + "\" style=\"display:none;\"></form>");
        sbHtml.append("<script>document.forms['alipaysubmit'].submit();</script>");

        return sbHtml.toString();
    }
    
    /**
     * 建立请求，以表单HTML形式构造，带文件上传功能
     * @param sParaTemp 请求参数数组
     * @param strMethod 提交方式。两个值可选：post、get
     * @param strButtonName 确认按钮显示文字
     * @param strParaFileName 文件上传的参数名
     * @return 提交表单HTML文本
     */
    public static String buildRequest(Map<String, String> sParaTemp, String strMethod, String strButtonName, String strParaFileName) {
        //待请求参数数组
        Map<String, String> sPara = buildRequestPara(sParaTemp);
        List<String> keys = new ArrayList<String>(sPara.keySet());

        StringBuffer sbHtml = new StringBuffer();

        sbHtml.append("<form id=\"alipaysubmit\" name=\"alipaysubmit\"  enctype=\"multipart/form-data\" action=\"" + ALIPAY_GATEWAY_NEW
                      + "_input_charset=" + AlipayRechargeConfig.input_charset + "\" method=\"" + strMethod
                      + "\">");

        for (int i = 0; i < keys.size(); i++) {
            String name = (String) keys.get(i);
            String value = (String) sPara.get(name);

            sbHtml.append("<input type=\"hidden\" name=\"" + name + "\" value=\"" + value + "\"/>");
        }
        
        sbHtml.append("<input type=\"file\" name=\"" + strParaFileName + "\" />");

        //submit按钮控件请不要含有name属性
        sbHtml.append("<input type=\"submit\" value=\"" + strButtonName + "\" style=\"display:none;\"></form>");

        return sbHtml.toString();
    }
    
    /**
     * 建立请求，以模拟远程HTTP的POST请求方式构造并获取支付宝的处理结果
     * 如果接口中没有上传文件参数，那么strParaFileName与strFilePath设置为空值
     * 如：buildRequest("", "",sParaTemp)
     * @param strParaFileName 文件类型的参数名
     * @param strFilePath 文件路径
     * @param sParaTemp 请求参数数组
     * @return 支付宝处理结果
     * @throws Exception
     */
    public static String buildRequest(String strParaFileName, String strFilePath,Map<String, String> sParaTemp) throws Exception {
        //待请求参数数组
        Map<String, String> sPara = buildRequestPara(sParaTemp);

        HttpRechargeProtocolHandler httpProtocolHandler = HttpRechargeProtocolHandler.getInstance();

        HttpRechargeRequest request = new HttpRechargeRequest(HttpRechargeResultType.BYTES);
        //设置编码集
        request.setCharset(AlipayRechargeConfig.input_charset);

        request.setParameters(generatNameValuePair(sPara));
        request.setUrl(ALIPAY_GATEWAY_NEW+"_input_charset="+AlipayRechargeConfig.input_charset);

        HttpRechargeResponse response = httpProtocolHandler.execute(request,strParaFileName,strFilePath);
        if (response == null) {
            return null;
        }
        
        String strResult = response.getStringResult();

        return strResult;
    }

    /**
     * MAP类型数组转换成NameValuePair类型
     * @param properties  MAP类型数组
     * @return NameValuePair类型数组
     */
    private static NameValuePair[] generatNameValuePair(Map<String, String> properties) {
        NameValuePair[] nameValuePair = new NameValuePair[properties.size()];
        int i = 0;
        for (Map.Entry<String, String> entry : properties.entrySet()) {
            nameValuePair[i++] = new NameValuePair(entry.getKey(), entry.getValue());
        }

        return nameValuePair;
    }
    
    /**
     * 用于防钓鱼，调用接口query_timestamp来获取时间戳的处理函数
     * 注意：远程解析XML出错，与服务器是否支持SSL等配置有关
     * @return 时间戳字符串
     * @throws IOException
     * @throws DocumentException
     * @throws MalformedURLException
     */
	public static String query_timestamp() throws MalformedURLException, DocumentException, IOException {

        //构造访问query_timestamp接口的URL串
        String strUrl = ALIPAY_GATEWAY_NEW + "service=query_timestamp&partner=" + AlipayRechargeConfig.partner;
        StringBuffer result = new StringBuffer();

        SAXReader reader = new SAXReader();
        Document doc = reader.read(new URL(strUrl).openStream());

        List<Node> nodeList = doc.selectNodes("//alipay/*");

        for (Node node : nodeList) {
            // 截取部分不需要解析的信息
            if (node.getName().equals("is_success") && node.getText().equals("T")) {
                // 判断是否有成功标示
                List<Node> nodeList1 = doc.selectNodes("//response/timestamp/*");
                for (Node node1 : nodeList1) {
                    result.append(node1.getText());
                }
            }
        }

        return result.toString();
    }
	
	/**
	 * 返回支付宝请求地址
	 * @param alipayRequest
	 * @param alipayClient
	 * @return    
	 * @return:       String    
	 * @throws 
	 * @author        yuanxin
	 * @Date          2016年11月13日 上午10:36:47
	 */
	public static String postToZfb(AlipayRequest alipayRequest ,AlipayClient alipayClient){
		String url = "";
		try {
			RequestParametersHolder requestHolder = getRequestHolderWithSign(alipayRequest,alipayClient);
			url = getRequestUrl(requestHolder);
		} catch (AlipayApiException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return url;
		
	}
	
	/**
	 * 拼接支付宝入参对象
	 * @param request
	 * @param alipayClient
	 * @return
	 * @throws AlipayApiException    
	 * @return:       RequestParametersHolder    
	 * @throws 
	 * @author        yuanxin
	 * @Date          2016年11月13日 上午10:37:13
	 */
	public static RequestParametersHolder getRequestHolderWithSign(AlipayRequest request,AlipayClient alipayClient)throws AlipayApiException{
		RequestParametersHolder requestHolder = new RequestParametersHolder();
		AlipayHashMap appParams = new AlipayHashMap(request.getTextParams());
		try {
			if ((request.getClass().getMethod("getBizContent", new Class[0]) != null)
					&& (StringUtils.isEmpty((String) appParams.get("biz_content"))) && (request.getBizModel() != null)){
				appParams.put("biz_content", new JSONWriter().write(request.getBizModel(), true));
			}
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
		} catch (SecurityException e) {
			e.printStackTrace();
		}
		
		requestHolder.setApplicationParams(appParams);
		AlipayHashMap protocalMustParams = new AlipayHashMap();
		protocalMustParams.put("method", request.getApiMethodName());
		protocalMustParams.put("version", request.getApiVersion());
		protocalMustParams.put("app_id", AlipayRechargeConfig.app_id);
		protocalMustParams.put("sign_type",  AlipayRechargeConfig.sign_type);
		protocalMustParams.put("terminal_type", request.getTerminalType());
		protocalMustParams.put("terminal_info", request.getTerminalInfo());
		protocalMustParams.put("notify_url", request.getNotifyUrl());
		protocalMustParams.put("return_url", request.getReturnUrl());
		protocalMustParams.put("charset", AlipayRechargeConfig.input_charset);
	
		Long timestamp = Long.valueOf(System.currentTimeMillis());
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		df.setTimeZone(TimeZone.getTimeZone("GMT+8"));
		protocalMustParams.put("timestamp", df.format(new Date(timestamp.longValue())));
		requestHolder.setProtocalMustParams(protocalMustParams);

		AlipayHashMap protocalOptParams = new AlipayHashMap();
		protocalOptParams.put("format", AlipayRechargeConfig.format);
		protocalOptParams.put("auth_token", null);
		protocalOptParams.put("alipay_sdk", "alipay-sdk-java-dynamicVersionNo");
		protocalOptParams.put("prod_code", request.getProdCode());
		requestHolder.setProtocalOptParams(protocalOptParams);
		
		String signContent = AlipaySignature.getSignatureContent(requestHolder);
		protocalMustParams.put("sign", AlipaySignature.rsaSign(signContent, AlipayRechargeConfig.private_key,
				AlipayRechargeConfig.input_charset, AlipayRechargeConfig.sign_type));
		return requestHolder;
	}
	
	
	public static String getRequestUrl(RequestParametersHolder requestHolder) throws AlipayApiException {
		StringBuffer urlSb = new StringBuffer(AlipayRechargeConfig.ali_pay_url);
		try {
			String sysMustQuery = WebUtils.buildQuery(requestHolder.getProtocalMustParams(),
					AlipayRechargeConfig.input_charset);

			String sysOptQuery = WebUtils.buildQuery(requestHolder.getProtocalOptParams(), AlipayRechargeConfig.input_charset);

			urlSb.append("?");
			urlSb.append(sysMustQuery);
			if ((((sysOptQuery != null) ? 1 : 0) & ((sysOptQuery.length() > 0) ? 1 : 0)) != 0) {
				urlSb.append("&");
				urlSb.append(sysOptQuery);
			}
		} catch (IOException e) {
			throw new AlipayApiException(e);
		}

		return urlSb.toString();
	}
}
