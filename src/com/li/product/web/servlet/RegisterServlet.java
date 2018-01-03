package com.li.product.web.servlet;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.beanutils.BeanUtils;

import com.li.product.domain.User;
import com.li.product.exception.UserException;
import com.li.product.service.UserService;

/**
 * 各个类方法调用等都是按照设计好的文档规定来命名的，从servlet到service再到dao从上到下定义方法，再从下到上实现具体的方法
 * @author li
 *
 */
public class RegisterServlet extends HttpServlet {
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		//处理验证码
		String ckcode = req.getParameter("ckcode");
		String checkcode_session = (String)req.getSession().getAttribute("checkcode_session");
		if(!checkcode_session.equals(ckcode)) {//如果两个验证码不一致，跳回注册页面
			req.setAttribute("ckcode_msg", "错了傻逼");
			req.getRequestDispatcher("/register.jsp").forward(req, resp);
		}
		//获取表单数据
		User user = new User();
		try {
			BeanUtils.populate(user, req.getParameterMap());
			//调用业务逻辑
			UserService us = new UserService();
			us.register(user);
			//分发转向
			req.getSession().setAttribute("user", user);//把用户信息封装到session对象中
			//registersuccess.jsp中5s后跳转到首页，直接显示欢迎xxx，所以把用户信息保存到session中
			req.getRequestDispatcher("/registersuccess.jsp").forward(req, resp);
		}catch(UserException e) {
			req.setAttribute("user_msg", e.getMessage());
			req.getRequestDispatcher("/register.jsp").forward(req, resp);
			return;//不继续往下走了
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		// TODO Auto-generated method stub
		//这里要写成这样哟，不然会报错“405 HTTP method GET is not supported by this URL”
		//具体调用哪个方法由网页的提交方式决定，不过在编程时不用考虑它调用哪个方法。你可以在doget方法里调用dopost方法，然所有的处理写在dopost方法里。这样不管调用的是doget方法还是dopost方法都可以处理。
		doGet(req, resp);
	}
}
