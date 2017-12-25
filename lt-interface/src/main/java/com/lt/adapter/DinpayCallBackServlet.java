package com.lt.adapter;

import java.io.IOException;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javolution.util.FastMap;

/**
 * 项目名称：lt-interface<br>
 * 类名称：DinPayCallBackServlet <br>
 * 类描述： 智付回调接口接收<br>
 * 创建人：yubei<br>
 * 创建时间：2017年6月2日 上午9:44:35 <br>
 */
public class DinpayCallBackServlet extends HttpServlet {

	private Logger logger = LoggerFactory.getLogger(getClass());
	
	private static final long serialVersionUID = 1L;

	/**
	 * @param req
	 * @param resp
	 * @return
	 * @author
	 * @hrows Exception
	 */
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		logger.info("【智付回调地址】"+req.getRemoteAddr());
		logger.info("============[智付API充值渠道],收到支付结果通知================");
		String interfaceVersion = (String) req.getParameter("interface_version");
		String merchantCode = (String) req.getParameter("merchant_code");
		String notifyType = (String) req.getParameter("notify_type");
		String notifyId = (String) req.getParameter("notify_id");
		String signType = (String) req.getParameter("sign_type");
		String sign = (String) req.getParameter("sign");
		String orderNo = (String) req.getParameter("order_no");
		String orderTime = (String) req.getParameter("order_time");
		String orderAmount = (String) req.getParameter("order_amount");
		String extraReturnParam = (String) req.getParameter("extra_return_param");
		String tradeNo = (String) req.getParameter("trade_no");
		String tradeTime = (String) req.getParameter("trade_time");
		String tradeStatus = (String) req.getParameter("trade_status");
		String bankSeqNo = (String) req.getParameter("bank_seq_no");

		StringBuilder signStr = new StringBuilder();
		if (null != bankSeqNo && !bankSeqNo.equals("")) {
			signStr.append("bankSeqNo=").append(bankSeqNo).append("&");
		}
		if (null != extraReturnParam && !extraReturnParam.equals("")) {
			signStr.append("extraReturnParam=").append(extraReturnParam).append("&");
		}
		signStr.append("interfaceVersion=").append(interfaceVersion).append("&");
		signStr.append("merchantCode=").append(merchantCode).append("&");
		signStr.append("notifyId=").append(notifyId).append("&");
		signStr.append("signType=").append(signType).append("&");
		signStr.append("orderAmount=").append(orderAmount).append("&");
		signStr.append("orderNo=").append(orderNo).append("&");
		signStr.append("ordeTime=").append(orderTime).append("&");
		signStr.append("tradeNo=").append(tradeNo).append("&");
		signStr.append("tradeStatus=").append(tradeStatus).append("&");
		signStr.append("tradeTime=").append(tradeTime);

		String signInfo = signStr.toString();
		Map<String, Object> map = FastMap.newInstance();
		map.put("interfaceVersion", interfaceVersion);
		map.put("merchantCode", merchantCode);
		map.put("notifyType", notifyType);
		map.put("notifyId", notifyId);
		map.put("signType", signType);
		map.put("sign", sign);
		map.put("orderNo", orderNo);
		map.put("orderTime", orderTime);
		map.put("orderAmount", orderAmount);
		map.put("extraReturnParam", extraReturnParam);
		map.put("tradeNo", tradeNo);
		map.put("tradeTime", tradeTime);
		map.put("tradeStatus", tradeStatus);
		map.put("bankSeqNo", bankSeqNo);
		map.put("signInfo", signInfo);

		try {
			MainOperator mainOperator = new MainOperator();
			mainOperator.dinpayOperator(map);
		} catch (Exception e) {
			logger.error("[智付支付结果接收处理失败!]" + e.getMessage());
		}

	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		doGet(req, resp);
	}
}
