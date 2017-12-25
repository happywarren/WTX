package com.lt.manager.controller.fund;

import com.lt.manager.service.fund.IFundWithdrawService;
import com.lt.util.utils.SpringUtils;
import com.lt.util.utils.prop.CustomerPropertiesConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import javax.jws.WebService;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;

/**
 * 描述: mvc层无法接收到回调流，改用servlet
 *
 * @author lvx
 * @created 2017/8/24
 */
public class QianTongCallBackServlet extends HttpServlet {
    /**
     *
     */
    private static final long serialVersionUID = 1L;
    private Logger logger = LoggerFactory.getLogger(getClass());

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            //取得通知信息
            StringBuffer notifyResultStr = new StringBuffer("");
            InputStream is = req.getInputStream();
            byte[] b = new byte[1024];
            int len = -1;
            while((len = is.read(b)) != -1){
                notifyResultStr.append(new String(b,0,len,"utf-8"));
            }
            logger.info("===钱通提现回调开始，入参{}", notifyResultStr);
/*            ServletContext servletContext = this.getServletContext();
            WebApplicationContext context =
                    WebApplicationContextUtils.getWebApplicationContext(servletContext);*/
            IFundWithdrawService fundWithDrawServiceImpl = (IFundWithdrawService)SpringUtils.getBean("fundWithdrawServiceImpl");
            fundWithDrawServiceImpl.callbackForQtong(notifyResultStr.toString());
            logger.info("===钱通提现回调结束===");
            resp.getWriter().print("SUCCESS");
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("钱通提现结果处理异常");
            resp.getWriter().print(e.getMessage());
        }

    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        doGet(req, resp);
    }
}
