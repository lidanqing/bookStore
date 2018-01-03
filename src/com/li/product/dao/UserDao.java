package com.li.product.dao;

import java.sql.SQLException;

import org.apache.commons.dbutils.QueryRunner;

import com.li.product.domain.User;
import com.li.product.util.C3P0Util;

public class UserDao {

	public void addUser(User user) throws SQLException{
		QueryRunner qr = new QueryRunner(C3P0Util.getDataSource());
		String sql = "INSERT INTO USER(username, password, gender, email, telephone, introduce, activecode, state, registtime)"
				+ " VALUES(?,?,?,?,?,?,?,?,?)";
		qr.update(sql, user.getUsername(), user.getPassword(),
				user.getGender(), user.getEmail(), user.getTelephone(),
				user.getIntroduce(), user.getActiveCode(), user.getState(),
				user.getRegistTime());
	}
	
}