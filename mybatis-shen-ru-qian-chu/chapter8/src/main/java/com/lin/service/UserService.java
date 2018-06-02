package com.lin.service;

import com.lin.entity.UserBean;
import org.apache.ibatis.session.RowBounds;

import java.util.List;

public interface UserService {
    UserBean getUser(Integer id);
    int insertUser(UserBean user);
    int deleteUser(Integer id);
    int updateUser(UserBean user);
    List<UserBean> findUsers(String userName, int start, int limit);
}
