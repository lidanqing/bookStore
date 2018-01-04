package com.li.product.web.servlet;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.beanutils.BeanUtils;

import com.li.product.domain.User;
import com.li.product.service.UserService;

public class ModifyUserServlet extends HttpServlet {

	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		
		//��װ������
		User user = new User();
		try {
			BeanUtils.populate(user, request.getParameterMap());
			
			UserService us = new UserService();
			us.modifyUser(user);
			//session ������������һ���Ự�ڼ䣬����û����ʷ��������ͻ��ж��session�����ڣ��Ҿͼ����������û�A��B��C��
			//���Ƿ��ʷ��������ֱ𴴽�������Session����ΪS1,S2,S3.
			//session.invalidate()����ĳһ���û����õģ�����˵S1����û������������������
			//��ô����ֻ��s1�û���session ��ɾ���������û���session����s1û��ϵ��
			//session.invalidate()����ʵ���ϵ��õ���session�����е�destroy������Ҳ����˵���´�Ҫ��ʹ��session���������´�����  
			//�򵥵�˵������û�ˣ�������ֵΪnull
			request.getSession().invalidate();//�൱��ע���û�
			response.sendRedirect(request.getContextPath()+"/modifyUserInfoSuccess.jsp");
		} catch (Exception e) {
			response.getWriter().write(e.getMessage());
		}
		
		
	}

	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doGet(request, response);
	}

}
