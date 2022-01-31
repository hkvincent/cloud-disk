package com.vincent.user.dao;

import java.sql.SQLException;
import java.util.Map;


import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.MapHandler;

import com.vincent.user.domain.User;

import cn.itcast.commons.CommonUtils;
import cn.itcast.jdbc.TxQueryRunner;

public class UserDao {
	private QueryRunner qr = new TxQueryRunner();

	public User findUserByNameAndPassword(String name, String password) {
		String sql = "select * from user where userName=? and uPassword=?";
		User user = null;
		try {
			Map<String, Object> beanMap = qr.query(sql, new MapHandler(), name,
					password);
			if (beanMap == null)
				return null;
			user = CommonUtils.toBean(beanMap, User.class);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return user;

	}

	public int createUser(User user) {
		int reuslt = 0;
		try {
			String sql = "insert into user(uId,userName,uPassword,cId,uTime,role,fileSize) values(?,?,?,?,?,?,?) ";
			Object[] paras = { user.getuId(), user.getUserName(),
					user.getuPassword(), user.getcId(), user.getuTime(),
					user.getRole(), user.getFileSize() };

			reuslt = qr.update(sql, paras);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return reuslt;
	}

	public User selectUserByUid(String uid) {
		String sql = "select * from user where uId = ?";
		User user = null;
		try {
			Map<String, Object> beanMap = qr.query(sql, new MapHandler(), uid);
			if (beanMap == null)
				return null;
			user = CommonUtils.toBean(beanMap, User.class);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return user;
	}

	public int updateSizeByUid(User user, int type) {
		int reuslt = 0;
		User tempUser = selectUserByUid(user.getuId());
		if (type == 0) {//upload
			try {
				String sql = "update user set fileSize=? where uId=?";
				reuslt = qr.update(sql, Long.parseLong(tempUser.getFileSize())
						+ Long.parseLong(user.getFileSize()), user.getuId());
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} else if (type == 1) {//delete
			try {
				String sql = "update user set fileSize=? where uId=?";
				reuslt = qr.update(sql, Long.parseLong(tempUser.getFileSize())
						- Long.parseLong(user.getFileSize()), user.getuId());
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return reuslt;
	}
}
