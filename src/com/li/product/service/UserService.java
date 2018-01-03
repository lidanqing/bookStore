package com.li.product.service;

import java.sql.SQLException;

import com.li.product.dao.UserDao;
import com.li.product.domain.User;
import com.li.product.exception.UserException;

public class UserService {

	UserDao ud = new UserDao();
	public void register(User user) throws UserException{
		try {
			ud.addUser(user);
		} catch (SQLException e) {
			e.printStackTrace();
			//���Զ����쳣�����û���ʾ�����浽req��
			throw new UserException("ע��ʧ�ܣ�");
		}
	}
}
