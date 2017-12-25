package com.lt.adapter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;


/***
 *  daddy pay转帐支付回调
 */
public class DaddypayTransferCallBackServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        String pay_time = req.getParameter("pay_time");
        String bank_id  = req.getParameter("bank_id");
        String amount = req.getParameter("amount");
        String company_order_num = req.getParameter("company_order_num");
        String mownecum_order_num = req.getParameter("mownecum_order_num");
        String pay_card_num = req.getParameter("pay_card_num");
        String pay_card_name = req.getParameter("pay_card_name");
        String channel = req.getParameter("channel");
        String area = req.getParameter("area");
        String fee= req.getParameter("fee");
        String transaction_charge = req.getParameter("transaction_charge");
        String key = req.getParameter("key");
        String deposit_mode = req.getParameter("deposit_mode");
        String base_info = req.getParameter("base_info");
        String operating_time= req.getParameter("operating_time");


    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        doGet(req,resp);
    }
}
