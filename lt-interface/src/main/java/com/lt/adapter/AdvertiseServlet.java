package com.lt.adapter;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import com.lt.util.utils.model.Response;

/**   
* 项目名称：lt-interface   
* 类名称：AdvertiseServlet   
* 类描述：   
* 创建人：yuanxin   
* 创建时间：2017年7月5日 上午11:08:56      
*/
public class AdvertiseServlet extends HttpServlet {
	private Logger logger = Logger.getLogger(getClass());
	
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		logger.info("进入到广告图显示界面");
		String advertiseId = req.getParameter("advertiseId");
		
		resp.setContentType("text/html; charset=utf-8");
		PrintWriter printOut = resp.getWriter();
		
		MainOperator mainOperator = new MainOperator();
		Response response = mainOperator.advertiseOperator(advertiseId);
		String comment = (String) response.getData();
		printOut.print(comment);
//    	printOut.print("<p>&nbsp; &nbsp; &nbsp; &nbsp;国外知名金融博客ZeroHedge评论美国当周石油钻井数称，油价在接连经历了美联储主席耶伦讲话、也门导弹击中沙特石油设施和美联储副主席费希尔的讲话造成的波动后，贝克休斯数据显示石油钻井结束八周连涨，与上周数量持平，随后油价微幅上涨。</p><p>&nbsp; &nbsp; &nbsp; 此前金十曾报道，周五（8月26日）22:00美联储官网公布了主席耶伦以“美联储的货币政策工具”为主题的演讲稿，油价先跌后涨，此后美联储副主席费希尔则直接表态称，耶伦这次的讲话意味着9月可能加息，油价再度反转抹平此前涨幅。</p><p>&nbsp; &nbsp; &nbsp; 本周油价在欧佩克冻产传闻的影响中震荡下行。继伊朗最终确认出席9月在阿尔及利亚召开的欧佩克部长级非正式会议之后，欧佩克秘书长巴尔金都在周五谈及冻产协议达成可能性时称，在当前情况下没有什么是不可能的，暗示冻产计划正在逐渐趋向内外一致。</p><p>&nbsp; &nbsp; &nbsp; 但因为欧佩克内部竞相增产，事实上欧佩克的产油量目前已经处在历史高位，因此也有投行指出，基于此高位产量水平的“冻产”，事实上根本“于事无补”，并不会对原油市场提供长期支持。</p><p>&nbsp; &nbsp; &nbsp; 高盛称，即使冻产协议也无法有效支撑油价的进一步上涨。在那些因为爆发武装冲突而发生原油供应中断的地区，冲突双方关系的缓和对市场再平衡的影响要超过冻产协议。</p><p><br/></p>");
    	printOut.flush();
    	printOut.close();
    	//printOut.flush();
//    	resp.setContentType("text/html");
//    	resp.flushBuffer();
	}
	
	
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		this.doGet(req, resp);
	}
}
