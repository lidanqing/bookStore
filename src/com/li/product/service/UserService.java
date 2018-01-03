package com.li.product.service;

import java.sql.SQLException;

import com.li.product.dao.UserDao;
import com.li.product.domain.User;
import com.li.product.exception.UserException;
import com.li.product.util.SendJMail;

public class UserService {

	UserDao ud = new UserDao();
	public void register(User user) throws UserException{
		try {
			ud.addUser(user);
			String emailMsg = "ע��ɹ�����<a href='http://www.product.com/activeServlet?activeCode="+user.getActiveCode()+"'>����</a>���¼";
			SendJMail.sendMail(user.getEmail(), emailMsg); 
		} catch (SQLException e) {
			e.printStackTrace();
			//���Զ����쳣�����û���ʾ�����浽req��
			throw new UserException("ע��ʧ�ܣ�");
		}
	}
	
	public void activeUser(String activeCode) throws UserException {
		//���ݼ���������û�
		try {
			User user = ud.findUserByActiveCode(activeCode);
			if(user!=null){
				//�����û�
				ud.activeCode(activeCode);
				return;
			}
			throw new UserException("����ʧ��!");//���Զ����쳣Ϊ�˰���Ϣ�����û�
		} catch (SQLException e) {
			e.printStackTrace();
			throw new UserException("����ʧ��!");
		}
	}
}
