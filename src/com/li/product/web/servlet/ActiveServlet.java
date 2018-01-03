package com.li.product.web.servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.li.product.exception.UserException;
import com.li.product.service.UserService;

//得到邮件传过来的激活码，用激活码到数据库中寻找用户，然后把state置为1，代表已经激活
public class ActiveServlet extends HttpServlet {

	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		//
		String activeCode = request.getParameter("activeCode");
		
		UserService us = new UserService();
		try {
			us.activeUser(activeCode);
		} catch (UserException e) {
			e.printStackTrace();
			//直接在网页中向用户提示失败信息
			response.getWriter().write(e.getMessage());
		}
	}

	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doGet(request, response);
	}

}
