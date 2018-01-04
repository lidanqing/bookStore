package com.li.product.web.servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.li.product.domain.User;
import com.li.product.exception.UserException;
import com.li.product.service.UserService;

public class FindUserById extends HttpServlet {

	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String id = request.getParameter("id");
		
		UserService us = new UserService();
		try {
			User user = us.findUserById(id);
			request.setAttribute("u", user);
			//需要把用户信息回显出来
			request.getRequestDispatcher("/modifyuserinfo.jsp").forward(request, response);
		} catch (UserException e) {
//			response.getWriter().write(e.getMessage());
			//登陆后长时间不动session失效，没有id了，找不到user，需要重新登陆
			response.sendRedirect(request.getContextPath()+"/login.jsp");
		}
	}

	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doGet(request, response);
	}

}
