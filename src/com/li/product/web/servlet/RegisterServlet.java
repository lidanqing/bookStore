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
 * �����෽�����õȶ��ǰ�����ƺõ��ĵ��涨�������ģ���servlet��service�ٵ�dao���ϵ��¶��巽�����ٴ��µ���ʵ�־���ķ���
 * @author li
 *
 */
public class RegisterServlet extends HttpServlet {
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		//������֤��
		String ckcode = req.getParameter("ckcode");
		String checkcode_session = (String)req.getSession().getAttribute("checkcode_session");
		if(!checkcode_session.equals(ckcode)) {//���������֤�벻һ�£�����ע��ҳ��
			req.setAttribute("ckcode_msg", "����ɵ��");
			req.getRequestDispatcher("/register.jsp").forward(req, resp);
		}
		//��ȡ������
		User user = new User();
		try {
			BeanUtils.populate(user, req.getParameterMap());
			//����ҵ���߼�
			UserService us = new UserService();
			us.register(user);
			//�ַ�ת��
			req.getSession().setAttribute("user", user);//���û���Ϣ��װ��session������
			//registersuccess.jsp��5s����ת����ҳ��ֱ����ʾ��ӭxxx�����԰��û���Ϣ���浽session��
			req.getRequestDispatcher("/registersuccess.jsp").forward(req, resp);
		}catch(UserException e) {
			req.setAttribute("user_msg", e.getMessage());
			req.getRequestDispatcher("/register.jsp").forward(req, resp);
			return;//��������������
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		// TODO Auto-generated method stub
		//����Ҫд������Ӵ����Ȼ�ᱨ��405 HTTP method GET is not supported by this URL��
		//��������ĸ���������ҳ���ύ��ʽ�����������ڱ��ʱ���ÿ����������ĸ��������������doget���������dopost������Ȼ���еĴ���д��dopost������������ܵ��õ���doget��������dopost���������Դ���
		doGet(req, resp);
	}
}
