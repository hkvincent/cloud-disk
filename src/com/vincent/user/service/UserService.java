package com.vincent.user.service;

import java.sql.SQLException;

import com.vincent.Catalog.dao.CatalogDao;
import com.vincent.Catalog.domain.Catalog;
import com.vincent.user.dao.UserDao;
import com.vincent.user.domain.User;

import cn.itcast.jdbc.JdbcUtils;

public class UserService {

    UserDao userDao = new UserDao();
    CatalogDao catalogDao = new CatalogDao();

    public User findUserByNameAndPassword(String name, String password) {
        User user = userDao.findUserByNameAndPassword(name, password);
        return user;
    }

    public User findUserByUid(String uid) {
        User user = userDao.selectUserByUid(uid);
        return user;
    }

    public boolean createUser(User user) {
        try {
            JdbcUtils.beginTransaction();
            Catalog c = new Catalog();
            c.setcId(user.getcId());
            c.setcName("root");
            c.setcDate(user.getuTime());
            c.setIsShare("0");
            c.setParent(null);
            c.setuId(user.getuId());
            c.setcF(user.getcId());

            int result = userDao.createUser(user);
            catalogDao.createCatalog(c);
            JdbcUtils.commitTransaction();
            if (result > 0) {
                return true;
            }

        } catch (SQLException e) {
            try {
                JdbcUtils.rollbackTransaction();

            } catch (SQLException e1) {
                e1.printStackTrace();
            }
            e.printStackTrace();
        }
        return false;
    }
}
