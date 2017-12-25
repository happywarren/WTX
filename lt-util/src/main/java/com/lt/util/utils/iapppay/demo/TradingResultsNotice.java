package com.lt.util.utils.iapppay.demo;

//package com.iapppay.demo; 
//
//import java.io.IOException;
//
//import javax.servlet.ServletException;
//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpServletResponse;
//import javax.servlet.http.HttpSession;
//
//import com.iapppay.sign.SignHelper; 
//
//public class TradingResultsNotice { 
//
//		
///* 
//* 此代码 是交易结果回调通知的demo代码。业务流程： 获取到爱贝服务端post给cp服务端的 同步数据，处理 处理数据，把数据传入 验签函数（SignHelper.verify(transdata, sign, PLATP_KEY)）。 
//* 获取同步数据的 方式： 如果是Java web 工程 可以使用Servlet ，使用getParameter("参数名"); 
//* 我们的 同步数据 由三个参数组成：transdata、sign、signtype。 
//* ①transdata={\"appid\":\"500000185\",\"cporderid\":\"1421220957285\",\"cpprivate\":\"cpprivateinfo123456\",\"currency\":\"RMB\",\"feetype\":6,\"money\":1.0,\"paytype\":5,\"result\":0,\"transid\":\"32011501141535580250\",\"transtime\":\"2015-01-14 15:36:23\",\"waresid\":4} 
//* ②sign=cbKD9RDjYpjgPMaaWJ0K4q7f6ldXOj8QA+IWkPdCfjmOlj2Gb9pRQg88CAONSB49wsNvHkiM+4OAOpqcohN/kdgDc92fmqzGcPb5GyUSdDacKHWFqFkyxxeboFiH0P5SrDIsklHBAfBiWP5DXTnL1Y6RiG52JyjX2sRWnGm1dAI= 
//* ③signtype=RSA 
//* 以下是两种获取post参数的两种方式： 
//* */ 
////①使用Servlet 根据参数名获取数据 
//	public void doPostByServlet(HttpServletRequest request, HttpServletResponse resp) 
//		throws ServletException, IOException { 
//			request.setCharacterEncoding("utf-8"); 
//			HttpSession session = request.getSession(); 
//			
//			String transdata = request.getParameter("transdata"); 
//			String sign = (String) request.getParameter("sign"); 
//			String signtype = request.getParameter("signtype"); 
//			if(signtype==null)
	//		{
	//			System.out.println(""+respData);
	//		}else{
//			
	//			/*
	//			 * 调用验签接口
	//			 * 
	//			 * 主要 目的 确定 收到的数据是我们 发的数据，是没有被非法改动的
	//			 */
	//			if (SignHelper.verify(transdata, sign, IAppPaySDKConfig.PLATP_KEY)) {
	//				System.out.println("verify ok");
	//			} else {
	//				System.out.println("verify fail");
	//
	//			}
//		}
//		
//
//	}
//
//
//} 
