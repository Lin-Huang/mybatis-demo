package com.lin.service.impl;

import com.lin.dao.UserDao;
import com.lin.entity.UserBean;
import com.lin.service.UserService;
import org.apache.ibatis.session.RowBounds;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

public class UserServiceImpl implements UserService {
    @Autowired
    private UserDao userDao;

    @Override
    public UserBean getUser(Integer id) {
        return userDao.getUser(id);
    }

    @Override
    public int insertUser(UserBean user) {
        return userDao.insertUser(user);
    }

    @Override
    public int deleteUser(Integer id) {
        return userDao.deleteUser(id);
    }

    @Override
    public int updateUser(UserBean user) {
        return userDao.updateUser(user);
    }

    @Override
    public List<UserBean> findUsers(String userName, int start, int limit) {
        return userDao.findUsers(userName, new RowBounds(start, limit));
    }
}
