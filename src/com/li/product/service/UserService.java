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
			String emailMsg = "注册成功，请<a href='http://www.product.com/activeServlet?activeCode="+user.getActiveCode()+"'>激活</a>后登录";
			SendJMail.sendMail(user.getEmail(), emailMsg); 
		} catch (SQLException e) {
			e.printStackTrace();
			//用自定义异常来给用户提示，保存到req中
			throw new UserException("注册失败！");
		}
	}
	
	public void activeUser(String activeCode) throws UserException {
		//根据激活码查找用户
		try {
			User user = ud.findUserByActiveCode(activeCode);
			if(user!=null){
				//激活用户
				ud.activeCode(activeCode);
				return;
			}
			throw new UserException("激活失败!");//用自定义异常为了把信息告诉用户
		} catch (SQLException e) {
			e.printStackTrace();
			throw new UserException("激活失败!");
		}
	}

	public User login(String username, String password) throws UserException {
		User user=null;
		try {
			user = ud.findUserByUserNameAndPassword(username,password);
			if(user==null){
				throw new UserException("用户名或密码错误!");
			}
			if(user.getState()==0){
				throw new UserException("用户未激活!");
			}
		} catch (SQLException e) {
			e.printStackTrace();
			throw new UserException("用户名或密码错误!");
		}
		return user;
	}
	
	public User findUserById(String id) throws UserException {
		try {
			return ud.findUserById(id);
		} catch (SQLException e) {
			e.printStackTrace();
			throw new UserException("用户查找失败！");
		}
	}

	public void modifyUser(User user) throws UserException {
		try {
			ud.modifyUser(user);
		} catch (SQLException e) {
			e.printStackTrace();
			throw new UserException("修改用户信息失败");
		}
	}
}
