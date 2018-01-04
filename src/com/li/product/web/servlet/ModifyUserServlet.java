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
		
		//封装表单数据
		User user = new User();
		try {
			BeanUtils.populate(user, request.getParameterMap());
			
			UserService us = new UserService();
			us.modifyUser(user);
			//session 的作用域是在一个会话期间，多个用户访问服务器，就会有多个session，现在，我就假设有三个用户A、B、C，
			//他们访问服务器，分别创建了三个Session，记为S1,S2,S3.
			//session.invalidate()，是某一个用户调用的，比如说S1这个用户，调用了这个方法，
			//那么，就只有s1用户的session 被删除，其他用户的session，跟s1没关系。
			//session.invalidate()，它实际上调用的是session对象中的destroy方法，也就是说你下次要再使用session，得再重新创建。  
			//简单的说，就是没了，而不是值为null
			request.getSession().invalidate();//相当于注销用户
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
