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
			//��Ҫ���û���Ϣ���Գ���
			request.getRequestDispatcher("/modifyuserinfo.jsp").forward(request, response);
		} catch (UserException e) {
//			response.getWriter().write(e.getMessage());
			//��½��ʱ�䲻��sessionʧЧ��û��id�ˣ��Ҳ���user����Ҫ���µ�½
			response.sendRedirect(request.getContextPath()+"/login.jsp");
		}
	}

	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doGet(request, response);
	}

}
