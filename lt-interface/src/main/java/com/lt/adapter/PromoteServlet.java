package com.lt.adapter;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.lt.adapter.utils.ConfigUtils;

public class PromoteServlet extends HttpServlet{

	private static final long serialVersionUID = 9091743478133190680L;

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String c = request.getParameter("c");
		
		String promote_html_url = ConfigUtils.getValue("promote_html_url");
		response.sendRedirect(promote_html_url+c);
	}
	
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		this.doGet(req, resp);
	}
}
