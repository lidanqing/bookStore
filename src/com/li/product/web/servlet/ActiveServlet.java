package com.li.product.web.servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.li.product.exception.UserException;
import com.li.product.service.UserService;

//�õ��ʼ��������ļ����룬�ü����뵽���ݿ���Ѱ���û���Ȼ���state��Ϊ1�������Ѿ�����
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
			//ֱ������ҳ�����û���ʾʧ����Ϣ
			response.getWriter().write(e.getMessage());
		}
	}

	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doGet(request, response);
	}

}
