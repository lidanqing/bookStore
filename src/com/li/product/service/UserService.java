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
			//用自定义异常来给用户提示，保存到req中
			throw new UserException("注册失败！");
		}
	}
}
